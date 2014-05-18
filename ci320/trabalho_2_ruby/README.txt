======================================================
Especificação
======================================================
Trabalho 2: (TP1) Ruby
 Banco de Dados usando ActiveRecord:

    Objetivo: Criar e usar um banco de dados via ActiveRecord.
    Caracteristicas:
        - Baixe os arquivos-exemplo Cria o bd e Exemplos de uso
        - Criar um banco de dados contendo tabelas com as relações (a) um para um, (b) um para muitos e (c) muitos para um. Não expliquei como fazer isto em sala. Pesquisem.
        - A inserção, alteração e exclusão de elementos devem ser realizados de acordo com o que for indicado pelo usuário na linha de comando. Comandos: insere, altera, exclui. A especificação de como são os comandos é escolha dos alunos. Optem por mecanismos simples e fáceis. Terei de testar um a um....
        - Data e forma de Entrega: email enviado até 21/03/2014, um arquivo ".tar.bz2" onde estão os programas ruby. Indiquem em README como faço para fazer funcionar.
        - Avaliação>: Todos os trabalhos que criarem os bd recebem nota 80. Outros 20 pontos serão atribuídos por facilidades para eu testar os programas. 10% de desconto por semana de atraso.

======================================================
Funcionamento
======================================================
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
