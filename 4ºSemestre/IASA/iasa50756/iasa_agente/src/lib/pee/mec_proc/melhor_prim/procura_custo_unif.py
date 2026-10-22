from pee.mec_proc.melhor_prim.aval.avaliador_custo_unif import AvaliadorCustoUnif
from pee.mec_proc.melhor_prim.procura_melhor_prim import ProcuraMelhorPrim


class ProcuraCustoUnif(ProcuraMelhorPrim):
    """      
    Esta classe é uma especialização do mecanismo de procura melhor-primeiro, 
    representando o mecanismo de procura de custo uniforme.
    Neste mecanismo o valor da função de avaliação do custo dos nós é feita segundo 
    o custo acumulado destes.
    Neste mecanismo de procura, a fronteira de exploração é ordenada pelo custo acumulado
    de cada nó, o nó com custo mais baixo vai ser expandido primeiro, pois este 
    representa a melhor opção em termos de custo (no momento).
    """

    def __init__(self):
        """
        Método construtor de um mecanismo de procura de custo uniforme.
        Onde se utiliza o super(), para se guardar a instância do avaliador especializado para
        este tipo de procura na variavél protegida da classe ProcuraMelhorPrim, _avaliador. 
        """
        super().__init__(AvaliadorCustoUnif())