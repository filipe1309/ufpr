Modo de execução:
	ruby criaBdTrabalhoRuby.rb
	ruby trabalhoRuby.rb

Programa: O software é basicamente um programa em que existem livros, lojas, edições e códigos de livros.

Relações: 
	1:1 -> Livros:Códigos
	1:N -> Livros:Edições
	N:N -> Livros:Lojas

Execução inicial:
	1 - Insere
	2 - Altera
	3 - Exclui
	4 - Pesquisa
	0 - Sair

	Este é basicaente um menu geral, e depois de escohida a opção, é escolhido à que elemento aquela opção será aplicada, através do submenu abaixo:
		<<<<<<<INSERCAO>>>>>>>
		1 - Livro
		2 - Loja
		3 - Edicao
		0 - Menu
	
	E então para cada submenu é definido suas opções respectivas, ex:
	<<<<<<<INSERCAO/LIVRO>>>>>>>
	Informe o titulo:

	O mesmo procedimento pode ser aplicado para todas as outras opções iniciais(Insere,Altera,Exclui,Pesquisa).
