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
	static int n, token, event, r, i;
	static char fa_name[5];

	if(argc != 2){
		puts("Uso correto: tempo <num-nodo>");
		exit(1);
	}

	n = atoi(argv[1]);

	smpl(0,"Um Exemplo de Simulação");
	reset();
	stream(1);
	nodo = (tnodo *) malloc(sizeof(tnodo)*n);

	for( i = 0; i < n; i++){
		memset(fa_name,'\0',5);
		sprintf(fa_name,"%d",i);
		nodo[i].id = facility(fa_name,1);
	}

	for( i = 0; i < n; i++)
		schedule(test, 30.0, i);

	schedule(fault,40.0,2);
	schedule(repair,70.0,2);
   schedule(fault,40.0,3);
	schedule(fault,60.0,4);

	while(time() < 100.0){
		cause(&event,&token);
		switch(event){
			case test:
				if(status(nodo[token].id != 0) ) break;
				int nodo_testado = (token+1)%n;
				printf("Sou o nodo %d, vou testar no tempo %5.1f.\n",token,time());
				schedule(test,30.0,token);
				printf("\tTestes feitos pelo nodo %d:\n", token);
				while (status(nodo[nodo_testado].id) != 0){
					printf("\t[%d]\t Teste do nodo %d: O nodo %d está falho.\n", token, nodo_testado, nodo_testado);
					nodo_testado = (nodo_testado+1)%n;
				}
				if(nodo_testado != token)
					printf("\t[%d]\t Teste do nodo %d: O nodo %d está sem falha.\n", token, nodo_testado, nodo_testado);

				break;
			case fault:
				r = request(nodo[token].id, token,0);
				if(r != 0){
					printf("Não consegui falhar o nodo %d\n",token);
					exit(1);
				}
				printf("O nodo %d falhou no tempo %5.1f\n",token,time());
				break;
			case repair:
				printf("O nodo %d recuperou no tempo %5.1f\n",token,time());
				release(nodo[token].id, token);
				schedule(test,30.0,token);
				break;
		}
	}
}
