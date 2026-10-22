from pee.mec_proc.melhor_prim.fronteira_prioridade import FronteiraPrioridade
from pee.mec_proc.procura_grafo import ProcuraGrafo

class ProcuraMelhorPrim(ProcuraGrafo):
    """
    Esta classe é uma especialização do mecanismo de procura em profundidade grafos, 
    representando o mecanismo de procura melhor-primeiro. 
    Este mecanismo, obtém uma solução óptima segundo o valor do seu custo, onde verifica
    se um nó gerado já foi explorado (pertence aos nós abertos ou fechados), caso este já
    tenha sido explorado, verifica-se o custo deste nó é menor que o custo do nó existente, se sim
    este é guardado na fronteira de exploração.

    Isto permite ignorar a exploração de nós que já existem no dicionário, caso o custo deste nó
    gerado for maior que o custo do nó com o mesmo estado.
    """

    def __init__(self, avaliador):
        """
        Método construtor de um mecanismo de procura melhor-primeiro.
        Onde se utiliza o super(), para criar uma fronteira de prioridade, onde se passa
        um avaliador (uma função de avaliação de custo). 

        Args:
            avaliador: uma função de avaliação de custo.
        """
        self._avaliador = avaliador
        super().__init__(FronteiraPrioridade(avaliador)) 

    def _manter(self, no):
        """
        Método que indica se um nó deve ser guardado ou descartado, utiliza-se o 
        super() para verificar se o nó já foi explorado e verifica-se o custo do 
        nó gerado é menor que o nó explorado com o mesmo estado.

        Args:
            no: nó a verificar.  

        Returns:
            true: se não existe no dicionário dos nós explorados, ou se o seu custo 
            é menor a um nó explorado com o mesmo estado.
            false: caso exista no dicionário, ou seja já foi explorado e se o seu custo for superior
            a um nó já explorado.
        """
        return super()._manter(no) or \
            no < self._explorados[no.estado]
