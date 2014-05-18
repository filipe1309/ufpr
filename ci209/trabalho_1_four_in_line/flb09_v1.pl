insereInicio(H, L, [H|L]):- !.
insereFim(T, [H], L):- insereInicio(H,[T],L), !.
insereFim(N, [H|T], L):- insereFim(N,T,X), insereInicio(H, X, L).

insere_a_ou_b(La, Lb,J):-
	J is a,
	retract(base_de_pecas([X|L]),
	insereFim(X,La,La);
	retract(base_de_pecas([X|L]),
	insereFim(X,Lb,Lb)

verifica_coluna(Coluna2):- Coluna2 > 7, !.

prox_coluna(J):-
	retract(base_de_pecas([[Linha,Coluna]|L]),
	Coluna2 is Coluna + 1,
	assert(base_de_pecas([[Linha,Coluna]|L])),
	verifica_coluna(Coluna2),
	assert(base_de_pecas([[Linha,Coluna2]|L]))

verifica_posicao(E_L,E_BP):- not(E_L is E_BP).

verifica_posicao_livre([]).

verifica_posicao_livre([E_L|L]):-
	retract(base_de_pecas([E_BP|L1]),
	verifica_posicao(E_L,E_BP),
	assert(base_de_pecas([E_BP|L1]),
	verifica_posicao_livre(L)


prox_linha(La, Lb, J, Proximo):-
	writeln(J),
	verifica_posicao_livre(La),
	verifica_posicao_livre(Lb),
	insere_a_ou_b(La, Lb,J);
	retract(base_de_pecas([[Linha,Coluna]|L]),
	Linha2 is Linha+1,
	Linha2 < 7,
	assert(base_de_pecas([[Linha2,Coluna]|L])),
	prox_linha(La, Lb, J, Proximo);
	retract(base_de_pecas([X|L])),
	assert(base_de_pecas(L)),
	prox_coluna(J)


adjacente([La|[Lb|L]], J, Proximo):-
	writeln(La),
	writeln(Lb),
	retractall(base_de_pecas(_)),
	assert(base_de_pecas([[1,1]])),
	prox_linha(La,Lb, J, Proximo);
	prox_coluna(J),
	adjacente(La, Lb, J, Proximo)
.
