dist_cidades([X,Y,Dist]):-
	dist_cidade([X,Y,Dist]).

dist_cidades([X,Y,Dist]):-
	dist_cidade([Y,X,Dist]).

insereDistancias([]).
insereDistancias([Distancia|ListaDeDistancia]) :-
	asserta(dist_cidade(Distancia)),
	insereDistancias(ListaDeDistancia)
.
replicaL(ListaAnterior ,ListaAnterior).

tspV(Inicio, NumCidade, ListaAnterior, CustoAnterior, CustoMelhor, ListaMelhor, Iteracao):-
	
	novaLCidade(NumCidade, ListaCidade),
	custoTsp(ListaCidade, Custo),
	get_time(Fim),
	Tempo is Fim - Inicio,
	(  Tempo < 8 , Iteracao < 1000  -> 	
		( Custo < CustoAnterior ->
			tspV(Inicio, NumCidade, ListaCidade, Custo, CustoMelhor, ListaMelhor, 0);
			IteracaoNova is Iteracao + 1,
			tspV(Inicio, NumCidade, ListaAnterior, CustoAnterior, CustoMelhor, ListaMelhor, IteracaoNova) );
	CustoMelhor is CustoAnterior,
	replicaL(ListaMelhor ,ListaAnterior) ).

cidadeInicial([Cidade|_], Cidade).

insere_cidade(Cidade,[], [Cidade]).
insere_cidade(Cidade, [Cidade2|ListaCidade], [Cidade2|ListaFinal]):-
	insere_cidade(Cidade,ListaCidade, ListaFinal)
.
	
retiraAresta(X,[X|Xs],1,Xs).
retiraAresta(X,[Y|Xs],K,[Y|Ys]) :- K > 1, 
   K1 is K - 1, retiraAresta(X,Xs,K1,Ys).
		
geraSeqCidades(I,I,[I]).
geraSeqCidades(I,K,[I|L]) :- 
	I < K, 
	I1 is I + 1, 
	geraSeqCidades(I1,K,L).	
	
cidadesRandom(_,0,[]).
cidadesRandom(Xs,N,[X|Zs]) :- N > 0,
    length(Xs,L),
    I is random(L) + 1,
    retiraAresta(X,Xs,I,Ys),
    N1 is N - 1,
    cidadesRandom(Ys,N1,Zs).	
		
novaLCidade(N,ListaRandomica) :- 
	geraSeqCidades(1,N,ListaSequencial), 
	cidadesRandom(ListaSequencial,N,ListaRandomica).	
 
achaCidadeFinal(Cidade,[Cidade]).
achaCidadeFinal(Cidade,[_|L]) :- achaCidadeFinal(Cidade,L).
	
custoTsp([Cidade|ListaCidade], CustoTotal):-
	
	achaCidadeFinal(Cidade2,ListaCidade),
	dist_cidades([Cidade, Cidade2, Dist]),
	custoTsp2(Cidade, ListaCidade, CustoSubTotal),
	CustoTotal is CustoSubTotal + Dist.


custoTsp2(_, [], CustoTotal):-
	CustoTotal is 0.

custoTsp2(Cidade, [Cidade2|ListaCidade], CustoTotal):-
	dist_cidades([Cidade, Cidade2, Dist]),
	custoTsp2(Cidade2, ListaCidade, CustoSubTotal),
	CustoTotal is CustoSubTotal + Dist.


caixeiro(NumCidade, ListaDeDistancia) :-
	get_time(Inicio),
	retractall(dist_cidade([])),
	insereDistancias(ListaDeDistancia),
	
	novaLCidade(NumCidade, ListaCidadeAnterior),
	custoTsp(ListaCidadeAnterior, CustoAnterior),

	tspV(Inicio, NumCidade, ListaCidadeAnterior, CustoAnterior, CustoMelhor, ListaCidade,0),

	cidadeInicial(ListaCidade, Cidade),
	insere_cidade(Cidade, ListaCidade, ListaCidade2),
	
	get_time(Fim),
	TempoTotal is Fim - Inicio,
	
	write('Ciclo = '),
	write_ln(ListaCidade2),
	
	write('Custo = '),
	write_ln(CustoMelhor),:
	
	write('Tempo de execucao = '),
	write(TempoTotal),
	write(' segundo(s)\n'),
	
	Produto is CustoMelhor*TempoTotal,
	write('Produto = '),
	write_ln(Produto)
	.


