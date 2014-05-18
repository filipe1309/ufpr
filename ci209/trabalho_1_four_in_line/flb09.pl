verifica_pecas([]).

verifica_pecas([[Lin,Col]|L]):-
    proxima_peca([Linha, Coluna]),
	Col = Coluna,
	Lin >= Linha,
	NovaLinha is Lin + 1,
	retract(proxima_peca([_, Coluna])),
    assert(proxima_peca([NovaLinha, Coluna])),
	verifica_pecas(L),!;
	verifica_pecas(L),!.

verifica_jogadores([X|[Y]]):-
	verifica_pecas(X),
	verifica_pecas(Y).
	
insereInicio(H, L, [H|L]):- !.

insereFim(T, [H], L):- insereInicio(H,[T],L), !.

insereFim(N, [H|T], L):- insereFim(N,T,X), insereInicio(H, X, L).

insereFim(T, [], L):- insereInicio(T,[],L), !.

mostra_proximo(P, [A|[B]], J, Proximo):-
	J = b,
    insereFim(P, B, L),
    insereInicio(A, [L], Proximo);
    J = a,
    insereFim(P, A, L),
    insereFim(B, [L], Proximo).

calcula_proximo(L, J, Proximo):-
	verifica_jogadores(L),
	proxima_peca([Lin,Col]),
	Lin < 7,	
	mostra_proximo([Lin,Col], L, J, Proximo);
	proxima_peca([_, Coluna]),
    Coluna < 7,
	retract(proxima_peca([_, Coluna])),
	NovaColuna is Coluna + 1,
	assert(proxima_peca([1, NovaColuna])),
	calcula_proximo(L, J, Proximo).
	
adjacente(L, J, Proximo):-
    retractall(proxima_peca(_)),
	assert(proxima_peca([1,1])),
	calcula_proximo(L, J, Proximo).

