Trabalho 5 - Ruby On Rails - Filipe Leuch Bonfim - GRR20092368

Programa: 
	O software é basicamente um programa em que existem livros, lojas, edições e códigos de livros.

Relações: 
	1:1 -> Livros:Códigos
	1:N -> Livros:Edições
	N:N -> Livros:Lojas

   	Usabilidade:
		comando:
			rails s -p 2368
		Criar usuário com nome 'admin' para ter acesso total (CRUD).
		Usuário comuns podem somente visualizar os dados.

