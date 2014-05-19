#include"prototipos.h"

void define_medoids(img_struct nova_img,int medoids[],int tam)
{
	int i=0,k,sair,largura,altura,novo_med;
	/*srand(time(NULL));*/
	printf("nova_img.largura: %d, nova_img.altura: %d, tam: %d\n",nova_img.largura,nova_img.altura,tam);	
	while(i<tam)
	{
		do{
			sair = 0;
			largura = rand()%(nova_img.largura-1);
			altura = rand()%(nova_img.altura-1);
			novo_med = nova_img.pontos[largura + altura*nova_img.largura];/*medoids é o vetor contendo os k medoids*/
			for(k = 0; k < i ; k++)/* Para não ocorrer repetiçoes de medoids */
				if(medoids[k] == novo_med){
					sair = 1;
				}	
		}while(sair);
		medoids[i]=novo_med;
		i++;	
		
	}
	
}
