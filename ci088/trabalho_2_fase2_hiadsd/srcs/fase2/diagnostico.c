#include <stdio.h>
#include <stdlib.h>
#include "smpl.h"
#include "cisj.h"

#define test 1
#define fault 2
#define repair 3

typedef struct {
	int id, s, *timestamps;/* s: guarda o cluster que deve ser diagnosticado
						 timestamps: guarda o estado dos outros nodos.
					  */
} tnodo;

tnodo *nodo;

main(int argc, char *argv[]) {
	static int N, token, event, r, i, j, k, aux, ln, pow2s, barra = 0;
	static char fa_name[5];
	node_set *nodes, *testar;
 
	if(argc != 2) {
		puts("Uso correto: diagnostico < num_nodos >");
		exit(1);
	}
	N = atoi(argv[1]);
	ln = (int)ceil(log2((double)N));
	smpl(0, "Um exemplo de simulação");
	reset();
	stream(1);
	nodo = (tnodo *)malloc(sizeof(tnodo)*N);
	for(i=0; i < N; i++) {
		memset(fa_name, '\0', 5);
		sprintf(fa_name, "%d", i);
		nodo[i].id = facility(fa_name, 1);
		nodo[i].s = (N < 2) ? 0 : 1;
		nodo[i].timestamps = (int *)malloc(sizeof(int)*N);
		for(j=0; j < N; j++)
			nodo[i].timestamps[j] = -1;
		nodo[i].timestamps[i] = 0;
	}
	
	for(i=0; i < N; i++)
		schedule(test, 30.0, i);
	schedule(fault, 40.0, 2);
	schedule(repair, 70.0, 2);
	
	
	while(time() < 100) {
		cause(&event, &token);
		if(time() < 100) // Para evitar que sejam feitos testes no tempo limite.
			switch(event) {
				case test:
					if(status(nodo[token].id) != 0) break;
					if(nodo[token].s != ln) { // Verifica se o nodo está diagnosticando o ultimo cluster
						if(!barra && (nodo[token].s == 1)) { // Auxilia na vizualiação do log
							barra++;
							printf("================================================================================\n\n");
						} else if((barra > 0) && (nodo[token].s > barra)) {
							barra++;
							printf("--------------------------------------------------------------------------------\n\n");
						}
						schedule(test,0.0,token);
					} else {
						if(barra) {
							barra = 0;
							printf("--------------------------------------------------------------------------------\n\n");
						}
						schedule(test,30.0,token);
					}
				
					printf("Sou o nodo %d vou testar o cluster s=%d no tempo %5.1f",
									token, nodo[token].s, time());
					nodes = cis(token, nodo[token].s);
					pow2s = 1 << (nodo[token].s-1);
					j = 0;
					while(j < pow2s) {
						if(nodes->nodes[j] < N) {
							testar = cis(nodes->nodes[j], nodo[token].s);
							k = 0;
							//while((k < pow2s) && (nodo[token].timestamps[aux] % 2 || aux >= N)) { // Verifica se o nodo a ser testado existe ou está falho.
							while(nodo[token].timestamps[testar->nodes[k]] % 2 || testar->nodes[k] >= N) // Verifica se o nodo a ser testado existe ou está falho.
								k++;
							if(testar->nodes[k] == token) {
								aux = nodes->nodes[j];
								if(status(nodo[aux].id) != 0) {
									printf("\n\tTestei o nodo %d falho", aux);
									if(nodo[token].timestamps[aux] == -1)
										nodo[token].timestamps[aux] = 1;
									else if(!(nodo[token].timestamps[aux] % 2))
										nodo[token].timestamps[aux]++;
								} else {
									printf("\n\tTestei o nodo %d sem falha", nodes->nodes[j]);
									if(nodo[token].timestamps[aux] % 2)
										nodo[token].timestamps[aux]++;
									for(i = 0; i < N; i++)
										if(i != token)
											if(nodo[token].timestamps[i] < nodo[nodes->nodes[j]].timestamps[i])
												nodo[token].timestamps[i] = nodo[nodes->nodes[j]].timestamps[i];
								}
							}
						}
						j++;
					}
					printf(", timestamps: [");
					for(i=0; i < N; i++) {
							printf(" %d", nodo[token].timestamps[i]);
						}
						puts(" ]");
					if(ln) 
						nodo[token].s = (nodo[token].s)%ln + 1; // incrementa o cluster s 
					
					puts("");
					break;
				case fault:
					if ((N-1) < token){ 
						printf("Não consegui falhar o nodo %d\n\n", token);
						exit(1);
					}
					r = request(nodo[token].id, token, 0);
					printf("================================================================================\n\n");
					if(r != 0) {
						printf("Não consegui falhar o nodo %d\n\n", token);
						exit(1);
					}
					printf(" => => => O nodo %d falha no tempo %5.1f\n\n", token, time());
					break;
				case repair:
					printf("================================================================================\n\n");
					printf(" => => => O nodo %d recuperou no tempo %5.1f\n\n", token, time());
					release(nodo[token].id, token);
					for(i = 0; i < N; i++)
						nodo[token].timestamps[i] = -1;
					nodo[token].timestamps[token] = 0;
					schedule(test, 30.0,token);
					break;
			}
	}
	printf("================================================================================\n");
}