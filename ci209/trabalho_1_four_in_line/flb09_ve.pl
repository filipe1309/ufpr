
define_pecas_correspondentes([]).

define_pecas_correspondentes([[Lin,Col]|L]):-
    base_de_pecas([Linha, Coluna]),
	Col = Coluna,
	Lin >= Linha,
	NovaLinha is Lin + 1,
	retract(base_de_pecas([_, Coluna])),
    assert(base_de_pecas([NovaLinha, Coluna])),
	define_pecas_correspondentes(L),!;
	base_de_pecas([Linha, Coluna]),
	Col < Coluna,
	define_pecas_correspondentes(L),!;
	true.

define_jogador_a_b([X|[Y]]):-
	define_pecas_correspondentes(X),
	define_pecas_correspondentes(Y).
	
insercao_no_comeco(H, L, [H|L]):- !.

insercao_no_final(T, [H], L):- insercao_no_comeco(H,[T],L), !.

insercao_no_final(N, [H|T], L):- insercao_no_final(N,T,X), insercao_no_comeco(H, X, L).

insercao_no_final(T, [], L):- insercao_no_comeco(T,[],L), !.

exibe_posicao_subsequente(P, [A|[B]], J, Proximo):-
	J = b,
    insercao_no_final(P, B, L),
    insercao_no_comeco(A, [L], Proximo);
    J = a,
    insercao_no_final(P, A, L),
    insercao_no_final(B, [L], Proximo).

proxima_posicao(L, J, Proximo):-
	define_jogador_a_b(L),
	base_de_pecas([Lin,Col]),
	Lin < 7,	
	exibe_posicao_subsequente([Lin,Col], L, J, Proximo);
	base_de_pecas([_, Coluna]),
    Coluna < 7,
	retract(base_de_pecas([_, Coluna])),
	NovaColuna is Coluna + 1,
	assert(base_de_pecas([1, NovaColuna])),
	proxima_posicao(L, J, Proximo).
	
adjacente(L, J, Proximo):-
    retractall(base_de_pecas(_)),
	assert(base_de_pecas([1,1])),
	proxima_posicao(L, J, Proximo).

