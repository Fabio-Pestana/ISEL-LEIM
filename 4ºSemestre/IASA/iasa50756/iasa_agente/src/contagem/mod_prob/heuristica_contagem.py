from pee.mec_proc.melhor_prim.heuristica import Heuristica


class HeuristicaContagem(Heuristica):
    """
    Esta classe representa uma especialização de uma Heuristica, representa a função
    que permite obter uma estimativa do custo estimado de um nó até ao estado objetivo, no
    problema da contagem, esta função h(n) permite obter a diferença entre o estado objetivo
    (__valor_final) e o valor da contagem.
    """
    def __init__(self, valor_final):
        """
        Método construtor de uma Heuristica da contagem, onde se guarda o valor
        do estado objetivo (__valor_final), que neste problema consiste no valor
        final da contagem.

        Args:
            valor_final: valor final da contagem (estado objetivo do problema)
        """
        self.__valor_final = valor_final

    def h(self, estado):
        """
        Método que obtém o custo estimado do nó até ao estado objetivo do problema,
        consiste apenas na diferença entre o estado objetivo (__valor_final) e
        o valor do estado recebido.

        Args:
            estado: estado do nó
        """
        return abs(self.__valor_final - estado.valor)