======================================================
Especificação
======================================================
Trabalho 2:

Este trabalho tem por objetivo a implementação de uma versão modificada do algoritmo de diagnóstico de sistemas distribuídos Hi-ADSD(Hierarchical Adaptive Distributed System-Level Diagnosis), que surgiu para diminuir a alta latência de diagnóstico que o algoritmo Adaptive-DSD possui, além da grande quantidade de testes, possivelmente, realizados pelo Hi-ADSD.
Especificação do Trabalho
Implemente a nova estratégia de testes para o algoritmo Hi-ADSD. Na sua implementação faça cada nodo testar os logN clusters a cada rodada de testes, ao invés de testar um único cluster por rodada. Esta é a única diferença da especificação que foi vista em aula. Adapte a função c(i,s,j) para a expressão vista em sala de aula. Utilize o vetor Timestamps[0..N-1] para armazenar informações de diagnóstico: uma entrada Timestamp[i] deste vetor é -1 se o estado do nodo i é desconhecido; 0 se o nodo i é inicialmente detectado como sem-falha; 1 se o nodo i se falho, a cada novo estado diagnosticado o timestamp é incrementado. Um nodo que inicializa desconhece o estado de todos os outros, menos o seu próprio estado. Na medida em que testes são executados todos os nodos descobrem o estado dos demais. Estruture seu log de forma a mostrar quais testes vão sendo executados e qual informação é obtida dos nodos testados. Simule a execução de um evento: um nodo sem-falha fica falho, ou vice-versa. Repita para diversos tamanhos de sistema e eventos diferentes. Em cada caso mostre: (a) a latência, isto é tempo desde a ocorrência do evento até todos os nodos completarem seu diagnóstico; (b) o número de testes executados por todos os nodos durante o intervalo da latência. Force o caso que resulta no maior número de testes no Hi-ADSD e mostre sua execução para a versão da Fase 1 e a versão da Fase 2. Deve ser feita uma página Web, que contém:

    Relatório HTML explicando como o trabalho foi feito (use desenhos, palavras, o que você quiser): o objetivo é detalhar as suas decisões para implementar seu trabalho.
    Código fonte dos programas, comentados. ATENÇÃO: acrescente a todo programa a terminação ".txt" para que possa ser diretamente aberto em um browser. Exemplos: cliente.py.txt ou servidor.c.txt
    Log dos testes executados: mostre explicitamente diversos casos testados, lembre-se é a partir desta listagem de testes que o professor vai medir até que ponto o trabalho está funcionando.



