
verifica_pecas([]).

verifica_pecas([[Lin,Col]|L]):-
    proxima_peca([Linha, Coluna]),
	Col = Coluna,
	Lin >= Linha,
	NovaLinha is Lin + 1,
	retract(proxima_peca([_, Coluna])),
    assert(proxima_peca([NovaLinha, Coluna])),
	verifica_pecas(L),!;
	proxima_peca([Linha, Coluna]),
	Col < Coluna,
	verifica_pecas(L),!;
	true.

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

% TESTES:

% BASICO: adjacente([[[1,4],[2,4],[3,4],[1,5]],[[1,3],[2,5],[1,6]]], a, Proximo).

% RESTA UM: adjacente([[[1,1],[2,1],[3,1],[4,1],[5,1],[6,1],[1,2],[2,2],[3,2],[4,2],[5,2],[6,2],[1,3],[2,3],[3,3],[4,3],[5,3],[6,3],[1,4],[2,4],[3,4],[4,4],[5,4],[6,4],[1,5],[2,5],[3,5],[4,5],[5,5],[6,5],[1,6],[2,6],[3,6],[4,6],[5,6],[6,6],[1,7],[2,7],[3,7],[4,7],[5,7]],[]], b, Proximo).

% TABULEIRO VAZIO: adjacente([[],[]], b, Proximo).

% TABULEIRO CHEIO: adjacente([[[1,1],[2,1],[3,1],[4,1],[5,1],[6,1],[1,2],[2,2],[3,2],[4,2],[5,2],[6,2],[1,3],[2,3],[3,3],[4,3],[5,3],[6,3],[1,4],[2,4],[3,4],[4,4],[5,4],[6,4],[1,5],[2,5],[3,5],[4,5],[5,5],[6,5],[1,6],[2,6],[3,6],[4,6],[5,6],[6,6],[1,7],[2,7],[3,7],[4,7],[5,7],[6,7]],[]], b, Proximo).

% TABULEIRO CHEIO(EU ACHO): adjacente([[...],[...]], b, Proximo).
