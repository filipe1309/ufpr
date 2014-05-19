#include<stdio.h>
#include<stdlib.h>
#include"smpl.h"

#define test 1
#define fault 2
#define repair 3

typedef struct {
	int id;
}tnodo;

tnodo *nodo;
void main(int argc, char *argv[]){
	static int n, token, event, r, i, j;
	static char fa_name[5];
	int **state, nodo_testado;

	if(argc != 2){
		puts("Uso correto: tempo <num-nodo>");
		exit(1);
	}

	n = atoi(argv[1]);

	smpl(0,"Um Exemplo de Simulação");
	reset();
	stream(1);
	nodo = (tnodo *) malloc(sizeof(tnodo)*n);
	state = (int **) malloc(sizeof(int *)*n);

	for( i = 0; i < n; i++){
		state[i] = (int *) malloc(sizeof(int)*n);
		memset(fa_name,'\0',5);
		sprintf(fa_name,"%d",i);
		nodo[i].id = facility(fa_name,1);
	}

	for( i = 0; i < n; i++)
		schedule(test, 30.0, i);


	for( i = 0; i < n; i++)
		for(j = 0; j < n; j++){
			if(i != j)
				state[i][j] = -1;
			else
				state[i][j] = 0;
		}


	schedule(fault,40.0,2);
	schedule(repair,70.0,2);
   schedule(fault,40.0,3);
	schedule(fault,60.0,4);

	while(time() < 100.0){
		cause(&event,&token);
		switch(event){
			case test:
				if(status(nodo[token].id) != 0 ) break;
				nodo_testado = (token+1)%n;
				printf("\nSou o nodo %d, vou testar no tempo %5.1f.\n",token,time());
				schedule(test,30.0,token);
				printf("\tTestes feitos pelo nodo %d:\n", token);
				while (status(nodo[nodo_testado].id) != 0){
					printf("\t[%d]\tTeste do nodo %d: está falho.\n", token, nodo_testado);
					state[token][nodo_testado] = 1;
					nodo_testado = (nodo_testado+1)%n;

				}
				if((nodo_testado != token) && (status(nodo[nodo_testado].id) == 0) ){
					printf("\t[%d]\tTeste do nodo %d: está sem falha.\n", token, nodo_testado);
					state[token][nodo_testado] = 0;
					printf("\t[%d]\tstate[",token);
					for( i = (nodo_testado+1)%n; i != token; i=(i+1)%n){
						if((i != token) && (i != nodo_testado))
							state[token][i] = state[nodo_testado][i];
					}
					for(i = 0; i < n; i++)
						printf(" %d",state[token][i]);
					printf(" ]\n\n");
				}
				break;
			case fault:
				r = request(nodo[token].id, token,0);
				printf("----------------------------------------------------\n");
				if(r != 0){
					printf("Não consegui falhar o nodo %d\n",token);
					exit(1);
				}
				printf("O nodo %d falhou no tempo %5.1f\n",token,time());
				printf("----------------------------------------------------\n");
				break;
			case repair:
				printf("----------------------------------------------------\n");
				printf("O nodo %d recuperou no tempo %5.1f\n",token,time());
				release(nodo[token].id, token);
				schedule(test,30.0,token);
				printf("----------------------------------------------------\n");
				break;
		}
	}
}
