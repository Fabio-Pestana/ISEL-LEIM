from pee.mec_proc.larg.fronteira_fifo import FronteiraFIFO
from pee.mec_proc.procura_grafo import ProcuraGrafo


class ProcuraLargura(ProcuraGrafo):
    """      
    Esta classe é uma especialização de um mecanismo de procura (procura em grafo), sendo
    este o mecanismo de procura em largura (método de procura não informada). 
    
    Este tipo de mecanismo consiste em explorar os nós mais antigos primeiro, ou seja
    explora primeiro todos os nós de uma certa profundidade para depois passar para os 
    nós da profundidade seguinte. Este mecanismo consiste nos seguintes passos:

    1. Começa-se com o nó inicial e este é colocado na fronteira de exploração.
    
    2. Exploração em largura: Até se encontrar um estado do nó que corresponde
    ao estado objetivo, repete-se os seguintes passos:
       - Remove-se o nó mais antigo da fronteira (primeiro elemento).
       - Com a aplicação de operadores sobre o estado do nó removido, gera-se os seus nós sucessores.
       - Adiciona-se esses nós sucessores à fronteira (os nós mais recentes são colocados no fim da 
       fronteira de exploração).
    
    3. Verificação do objetivo: Durante a exploração, sempre que um nó é removido da fronteira, 
        verifica-se se o estado de este corresponde ao estado objetivo. Se for, a procura termina.

    Este tipo de algoritmo considera-se completo pois encontra sempre uma solução, se esta existir,
    isto é garantido através da sua exploração sistemática. Este mecanismo também é considerado óptimo
    pois encontra uma solução ótima em termos de números de passos, pois necessita de menos passos 
    em termos de profundidade (obtém a solução com menos etapas). No entanto este mecanismo pode ter 
    um grande custo computacional devido à sua complexidade espacial exponencial.
    """

    def __init__(self):
        """
        Método construtor de um mecanismo de procura em Largura,
        onde se passa no super a fronteira que segue a lógica FIFO.
        """
        super().__init__(FronteiraFIFO())