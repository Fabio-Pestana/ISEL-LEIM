import math
from pee.mec_proc.melhor_prim.heuristica import Heuristica


class HeurDist(Heuristica):
    """
    Esta classe representa uma especialização de uma Heuristica, representa a função
    que permite obter uma estimativa do custo de um nó até ao estado final.
    """
    def __init__(self, estado_final):
        """
        Método construtor de uma Heuristica da distância, onde se guarda o valor
        do estado final (estado_final).

        Args:
            estado_final: estado objetivo do problema
        """
        self.__estado_final = estado_final

    def h(self, estado):
        """
        Método que obtém o custo estimado do nó até ao estado final do problema,
        consiste no calculo da distância euclidiana entre a posição do estado recebido e a posição
        do estado final.

        Args:
            estado: estado do nó

        Returns:
            custo: custo estimado
        """
        return math.dist(estado.posicao, self.__estado_final.posicao)