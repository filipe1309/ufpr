#include<stdio_ext.h>
#include"prototipos.h"
int main(int argc,char *argv[])
{
	FILE *img_in,*img_out;
	img_struct nova_img;
	int *medoids,tam,i=0,j=0,*rot, custo,tam_img;
	char *coment;
	if(argc==4) /* Condição para evitar quantidade errada de argumentos */
	{
		tam=atoi(argv[2]);

		if(tam<2)
		{
			fprintf(stderr,"apenas valores maiores 	que 2 sao validos, setando para 2\n");
			tam = 2;
		}
		img_in=fopen(argv[1],"r");/* abre a imagem de entrada, r = leitura*/
		if(img_in==NULL)
		{
			fprintf(stderr,"Erro ao abrir %s \n",argv[1]);
			fprintf(stderr,"Possivelmente nao exista a imagem %s ou nome da imagem esta incorreto \n",argv[1]);
			exit(1);
		}
		img_out=fopen(argv[3],"w+b");/* abre a imagem de saida, w+ =ler e gravar b= modificador binario */
		if(img_out==NULL)
		{
			fprintf(stderr,"Erro ao abrir %s \n",argv[3]);
			exit(1);
		}
		medoids=(int *)malloc(sizeof(int)*tam);
		nova_img.tipo=(char *)malloc(sizeof(char)*2);
		/*LEITURA*/
		fscanf(img_in,"%s",nova_img.tipo);
		fprintf(img_out,"%s\n",nova_img.tipo);
		printf("nova_img.tipo: %s\n",nova_img.tipo);
		free(nova_img.tipo);
		coment = (char *)malloc(sizeof(char));
		fread(coment,1,1,img_in);
		
		/* Condição para tirar o comentario */
		
		while ((coment[0] < 48) || (coment[0] > 57))
		{
				fread(coment,1,1,img_in);
			
		}
		
		fseek(img_in, -1, SEEK_CUR);
		
		fscanf( img_in, "%d %d", &nova_img.largura, &nova_img.altura);
		fread(coment,1,1,img_in);
		while ((coment[0] < 48) || (coment[0] > 57))
		{
				fread(coment,1,1,img_in);
			
		}
		fseek(img_in, -1, SEEK_CUR);
		
		/*Obtem maxval*/
		fscanf( img_in, "%d",&nova_img.maxval);
		
		nova_img.pontos = (int *) malloc(sizeof(int *)*nova_img.largura*nova_img.altura);
		if(nova_img.pontos==NULL)
		{
			fprintf(stderr,"2 Erro no Malloc \n");
			exit(1);
		}
		
		/* 
		nova_img.imagem=(int **)malloc(sizeof(int *)*nova_img.largura);
		if(nova_img.imagem==NULL)
		{
			fprintf(stderr,"2 Erro no Malloc \n");
			exit(1);
		}
		printf("l: %d, a: %d, mv: %d\n", nova_img.largura, nova_img.altura, nova_img.maxval);
		for(cont=0;cont<nova_img.largura;cont++)
		{	
			nova_img.imagem[cont]=(int *)malloc(sizeof(int)*nova_img.altura);
			if(nova_img.imagem[cont]==NULL)
			{
				fprintf(stderr,"3 Erro no Malloc \n");
				exit(1);
			}
		}*/
		
		/*Cópia da imagem para memória*/
		i=0;
		tam_img = nova_img.largura*nova_img.altura;
		while(i< tam_img)
		{
			fscanf(img_in,"%d",&nova_img.pontos[i]);
			i++;
		}

		
		/*Fim de leitura*/
		define_medoids(nova_img,medoids,tam);
		
		rot=(int*)malloc(sizeof(int )*nova_img.largura*nova_img.altura);
		if(rot==NULL)
		{
			fprintf(stderr,"4 Erro no Malloc \n");
			exit(1);
		}
		
		
		custo = rotulacao(medoids,nova_img,tam,rot);/* rotula a matriz da imagem em uma matriz auxiliar, e retorna o custo*/
		
		calcula_medoids(nova_img,medoids,tam,custo);/*calcula a média*/
		fflush(stdout);
		
		/*Rotulação Final*/
		custo = rotulacao(medoids,nova_img,tam,rot);	
		printf("5dasdasdasdas - Custo final: %d\n",custo);	
		
		/*Escrita na nova imagem*/
		fprintf(img_out,"%d %d\n%d\n",nova_img.largura,nova_img.altura,nova_img.maxval);
		i=0;
		while(i<nova_img.altura)
		{
			j=0;
			while(j<nova_img.largura)
			{
				fprintf(img_out,"%d\n",rot[j + i*nova_img.largura]);
				j++;
			}
			i++;
		}
		/*Libera alocações de mémoria*/	
		free(nova_img.pontos);	
		free(rot);
		
		/*fecha arquivos*/
		fclose(img_in);
		fclose(img_out);
	}
	else
		fprintf(stderr,"Quantidade de Argumentos incorretos!! \n");
	return 0;
}
