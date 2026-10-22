from pee.mec_proc.melhor_prim.aval.avaliador_aa import AvaliadorAA
from pee.mec_proc.melhor_prim.procura_informada import ProcuraInformada


class ProcuraAA(ProcuraInformada):
    """      
    Esta classe é uma especialização do mecanismo de procura informada,
    representando o mecanismo de procura A*. 
    Este mecanismo baseia-se na seleção dos nós para a expansão da fronteira, onde se expande primeiro 
    aqueles com menores valores f(n), função de avaliação, que neste método consiste na soma
    do custo acumulado até ao nó, g(n), e o custo estimado do nó até ao estado objetivo, h(n). Esta
    combinação permite a exploração de uma forma eficiente do espaço de estados, direcionando a busca 
    para uma solução óptima.

    Este tipo de algoritmo considera-se completo pois encontra sempre uma solução, se esta existir.
    Este mecanismo também é considerado óptimo, pois a heurística é admissível, ou seja, o custo estimado
    de um nó até ao estado objetivo nunca é superior ao custo real da solução.
    """
    def __init__(self):
        """
        Método construtor de um mecanismo de procura A*.
        Onde se utiliza o super(), para se guardar a instância do avaliador especializado para
        este tipo de procura na variavél protegida da classe ProcuraMelhorPrim, _avaliador. 
        """
        super().__init__(AvaliadorAA())