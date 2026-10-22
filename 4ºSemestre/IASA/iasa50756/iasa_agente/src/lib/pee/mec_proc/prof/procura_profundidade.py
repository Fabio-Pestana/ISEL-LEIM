from pee.mec_proc.mecanismo_procura import MecanismoProcura
from pee.mec_proc.prof.fronteira_lifo import FronteiraLIFO


class ProcuraProfundidade(MecanismoProcura):
    """      
    Esta classe é uma especialização de um mecanismo de procura, sendo este o 
    mecanismo de procura em profundidade (método de procura não informada). 
    
    Este tipo de mecanismo consiste em explorar os nós mais recentes primeiro, ou seja
    explora-se um ramo da árvore de procura até o nó explorado não ter mais sucessores,
    neste caso, volta-se para um nó antecessor (capaz de expandir a árvore) para continuar
    a exploração. Este mecanismo consiste nos seguintes passos:

    1. Começa-se com o nó inicial e este é colocado na fronteira de exploração.
    
    2. Exploração em profundidade: Até se encontrar um estado do nó que corresponde
    ao estado objetivo, repete-se os seguintes passos:
       - Remove-se o nó mais recente da fronteira (primeiro elemento).
       - Com a aplicação de operadores sobre o estado do nó removido, gera-se os seus nós sucessores.
       - Adiciona-se esses nós sucessores à fronteira (os nós mais recentes são colocados no início da 
       fronteira de exploração, pois segundo a estratégia de controlo estes nós, como são mais 
       profundos, colocam-se no inicio da lista).
    
    3. Verificação do objetivo: Durante a exploração, sempre que um nó é removido da fronteira, 
        verifica-se se o estado de este corresponde ao estado objetivo. Se for, a procura termina.
    
    Este tipo de algoritmo não é completo, pois pode ficar preso em ciclos. Também não é optimo pois
    não garante a melhor solução, dependendo da ordem dos estados, ou seja este algoritmo termina 
    ao encontrar a primeira solução do problema, sendo que esta pode não ser a mais curta ou 
    eficiente em termos de custo.
    """

    def __init__(self):
        """
        Método construtor de um mecanismo de procura em profundidade,
        onde se passa no super a fronteira que segue a lógica LIFO.
        """
        super().__init__(FronteiraLIFO())


    def _memorizar(self, no):
        """
        Método que adiciona um nó a fronteira de exploração.

        Args:
            no: nó a adicionar
        """
        self._fronteira.inserir(no)