======================================================
Especificação
======================================================
 Trabalho CI092/CI765 - 1o sem 2014

O trabalho consiste em implementar um injetor da linguagem do HIVE (http://hive.apache.org/) para Ecore, isto é, receber um texto como entrada e produzir um modelo correspondente.

O injetor deverá implementar uma especificação usando o formato xText (https://www.eclipse.org/Xtext/‎).

Implementar apenas um subconjunto da linguagem, que deverá conter os elementos descritos abaixo:

    SELECTs : https://cwiki.apache.org/confluence/display/Hive/LanguageManual+Select. Expressões que deverão ser suportadas
        - SELECT * FROM <lista de tableas> WHERE <expressão>
        - SELECT <lista de colunas> FROM <lista de tableas> WHERE <expressão>
        - <expressao>: comparacao (>, <, =, <=, >=, IN), AND, OR, SUBSELECT
        - CLUSTER BY, DISTRIBUTE BY
    JOINs: https://cwiki.apache.org/confluence/display/Hive/LanguageManual+Joins. Implementar apenas a especificação mais simples do JOIN:
        - join_table:
        table_reference JOIN table_factor [join_condition]
    UNIONS: https://cwiki.apache.org/confluence/display/Hive/LanguageManual+Union. Exemplo da sintaxe:
        - select_statement UNION ALL select_statement UNION ALL select_statement ...

 

Implementação: a escolha das ferramentas utilizadas para a implementação fica a critério do aluno, mas recomenda-se o uso da API EMF (Eclipse Modeling Framework) e xText. O pacote Eclipse Modeling Tools pode ser baixado e os componentes descritos podem ser adicionados facilmente através de um menu de instalação.

ENTREGA

Todo o projeto deve ser zipado.

DATA DE ENTREGA : 20/maio/2014 (nova data : 25 de maio 2014). Cada dia de atraso será descontado 10 pontos da nota.

MODO DE ENTREGA : enviar o arquivo com projeto Eclipse zipado por email para marcos.ddf _at_ inf.ufpr.br (até as 24h do dia 20/05).
No corpo do email, colocar o nome dos integrantes da equipe (no mínimo 2 e no máximo 3). No assunto, preencher com "Entrega trabalho 765/092".

