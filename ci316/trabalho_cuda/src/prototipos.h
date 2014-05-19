#include<stdio.h>
#include<stdlib.h>
#include<time.h>
typedef struct {
		char *tipo;
		int largura, altura, maxval;
		int *pontos;
		} img_struct;
		
void define_medoids(img_struct nova_img,int medoids[],int tam);

int escolhe_caso(int medoids[],int x,int tam, int *dist);

void calcula_medoids(img_struct nova_img,int medoids[],int tam,int custo);

int rotulacao(int medoids[],img_struct nova_img,int tam,int *rot);

