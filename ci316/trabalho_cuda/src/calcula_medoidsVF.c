#include"prototipos.h"
#if defined(__CUDA_ARCH__) && (__CUDA_ARCH__ < 200)
    #define printf(f, ...) ((void)(f, __VA_ARGS__),0)
#endif
#define MAX_R 1000
#define BLOCK_SIZE 16

/*
	VF:
		Cada thread para cada ponto.
		2 reduces; 1 de threads por blocks, e outros de blocks 
*/

__global__ void sum_reduce_blocks(int *vcustos,int pBlock,int nBlock, int tam){
    /*nBlock = (nova_img.largura / dimBlock.x)*(nova_img.altura / dimBlock.y); Para Reduce dos blocks
	
	nGrid = nBlock/1024 + (nBlock % 1024 == 0 ? 0 : 1);*/

  /*printf("::::::threadIdx.x: %d,inicio: %d,fim: %d\n",threadIdx.x,inicio,fim);*/
	
	__shared__ int sdata[1024]; /*https://gist.github.com/wh5a/4424992*/

	int novo_custo = 0,i;
	int inicio = threadIdx.x*(pBlock);
	int fim =  ((threadIdx.x+1)*pBlock) - 1;
	if(threadIdx.x == (nBlock-1)) fim = tam-1;
	

	for(i = inicio; i <= fim; i++)
			novo_custo+=vcustos[i];
	
	sdata[threadIdx.x] = novo_custo;

	__syncthreads();

	if(!threadIdx.x){
		vcustos[0] = 0;
		for(i = 0; i < blockDim.x; i++)
			vcustos[0] += sdata[i];
	}				
}

__global__ void sum_reduce(int *vcustos,int *vcustos_out ){
	const int dim = BLOCK_SIZE*BLOCK_SIZE;
	__shared__ int sdata[dim]; /*https://gist.github.com/wh5a/4424992*/

	int i = (blockIdx.y*gridDim.x +blockIdx.x)*blockDim.x*blockDim.y + threadIdx.y*blockDim.x + threadIdx.x ;
	int prox_tid,s;
    
    int tid = i%(blockDim.x*blockDim.y);
	
	sdata[tid] = vcustos[i];
	for(s=1; s < dim; s *= 2) {
		if (!(tid % (2*s))) {
			prox_tid = tid + s;
			if(prox_tid < dim)
				sdata[tid] += sdata[tid + s];
	
		}
		__syncthreads();
	}
	
	if(tid == 0)
		vcustos_out[ blockIdx.y*gridDim.x +blockIdx.x] = sdata[0];
}

__global__ void calcula_custo(img_struct nova_img, int tam,int *medoids,int *vcustos){
	int j,dist1,dist2,x,tid;

	int lin = blockIdx.y * blockDim.y + threadIdx.y;
    int col = blockIdx.x * blockDim.x + threadIdx.x;
    tid = lin*nova_img.largura + col; /*ID global da thread*/
	x = nova_img.pontos[tid];
    
 	dist1 = fabsf(medoids[0] - x);
	for( j = 1; j < tam; j++)
	{	
		dist2 = fabsf(medoids[j] - x);
		if(dist2 < dist1)
			dist1 = dist2;
	}
	vcustos[tid] = dist1;
	__syncthreads();
}


void calcula_medoids(img_struct nova_img,int medoids[],int tam, int custo)
{
	int k, rodadas,largura, altura, novo_med, antigo_med, novo_custo, novo_cont, *d_vcustos, sair, *d_medoids, nPontos, nGrid, nBlock,pThread,*d_vcustos_out;
	img_struct d_nova_img;	
	
	srand(time(NULL));
	k = rodadas = 0;
	novo_custo = custo;
	nPontos = nova_img.largura*nova_img.altura;
	d_nova_img.largura = nova_img.largura;
	d_nova_img.altura = nova_img.altura;
	cudaMalloc(&d_medoids,tam*sizeof(int));
	cudaMalloc(&d_vcustos,nPontos*sizeof(int));
	cudaMalloc(&d_nova_img.pontos, nPontos*sizeof(int));
    cudaMemcpy(d_nova_img.pontos, nova_img.pontos, nPontos*sizeof(int),cudaMemcpyHostToDevice);

	dim3 dimBlock(BLOCK_SIZE, BLOCK_SIZE); /* Block: 16x16: 256 threads*/
    dim3 dimGrid(nova_img.largura / dimBlock.x, nova_img.altura / dimBlock.y); /* 1920x1200 -> 1920/16,1200/16 = 120,75 blocks = 9000 blocks*/ 
    cudaMalloc(&d_vcustos_out,(nova_img.largura/dimBlock.x)*(nova_img.altura / dimBlock.y)*sizeof(int));
	nBlock = 1024;
	nGrid = (nova_img.largura / dimBlock.x)*(nova_img.altura / dimBlock.y);
	pThread = nGrid/nBlock;
	/* Recalcula o custo por MAX_R rodadas */
	do{
		if(novo_custo < custo)
			custo = novo_custo;
		
		novo_custo = 0;
		
		/* Escolhe um novo medoid */
		do{
			sair = 0;
			largura=rand()%(nova_img.largura-1);
			altura=rand()%(nova_img.altura-1);
			novo_cont = nova_img.pontos[largura + altura*nova_img.largura];
			for(k = 0; k < tam ; k++)/* Para não ocorrer repetiçoes de medoids */
				if(medoids[k] == novo_cont)
					sair = 1;
		}while(sair);
		novo_med = rand()%(tam-1); /* Define ramdomicamente o medoid a ser substituído */
		
		antigo_med = medoids[novo_med];	
		medoids[novo_med]=novo_cont;/* medoids é o vetor de medoids */
		/*medoids[1]= 90;
		medoids[2]= 166;
		medoids[0]= 27;*/
		cudaMemcpy(d_medoids, medoids, tam*sizeof(int),cudaMemcpyHostToDevice); /* Novo vetor de medoids é transferido para o device */

		/* O Custo é recalculado */	
	    calcula_custo<<<dimGrid, dimBlock>>>(d_nova_img,tam,d_medoids,d_vcustos);	    
	    cudaDeviceSynchronize();
	    sum_reduce<<<dimGrid,dimBlock>>>(d_vcustos,d_vcustos_out);  /*Reduce das threads dos blocks */
  	    cudaDeviceSynchronize(); 
		sum_reduce_blocks<<<1,nBlock>>>(d_vcustos_out,pThread,nBlock,nGrid);  /*Reduce dos blocks */
  	   
	    cudaMemcpy(&novo_custo, d_vcustos_out, sizeof(int), cudaMemcpyDeviceToHost);

	    //printf("** Novo Custo: %d, custo: %d **\n",novo_custo,custo);

	    if(novo_custo >= custo)
		medoids[novo_med]=antigo_med;
	    rodadas++;	
	}while(rodadas < MAX_R);	
	/* Free device memory */
	cudaFree(d_nova_img.pontos);
	cudaFree(d_medoids);
	cudaFree(d_vcustos);
}		
