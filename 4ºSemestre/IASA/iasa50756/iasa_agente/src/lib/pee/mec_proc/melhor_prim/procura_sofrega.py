from pee.mec_proc.melhor_prim.aval.avaliador_sof import AvaliadorSof
from pee.mec_proc.melhor_prim.procura_informada import ProcuraInformada


class ProcuraSofrega(ProcuraInformada):
    """      
    Esta classe é uma especialização do mecanismo de procura informada,
    representando o mecanismo de procura Sôfrega. 

    Este mecanismo baseia-se na seleção dos nós para a expansão da fronteira, onde se expande primeiro 
    aqueles com menores valores f(n), função de avaliação, que neste método consiste apenas no custo 
    estimado do nó até ao estado objetivo, h(n). Como f(n) é apenas igual a h(n), não tem em conta
    o custo acumulado do nó, este mecanismo interessa-se apenas em diminuir o custo estimado para 
    atingir o objetivo do problema. No entanto como este mecanismo não tem em conta o custo do percurso 
    explorado, este pode levar a soluções sub-óptimas.
    """
    def __init__(self):
        """
        Método construtor de um mecanismo de procura sôfrega.
        Onde se utiliza o super(), para se guardar a instância do avaliador especializado para
        este tipo de procura na variavél protegida da classe ProcuraMelhorPrim, _avaliador. 
        """
        super().__init__(AvaliadorSof())