from abc import ABC
from pee.mec_proc.melhor_prim.procura_melhor_prim import ProcuraMelhorPrim


class ProcuraInformada(ProcuraMelhorPrim, ABC):
    """      
    Esta classe é uma especialização do mecanismo de procura melhor-primeiro,
    sendo esta abstrata para assim não ser instanciada, pois apenas representa
    métodos de procura informada, ou seja representa os mecanismos de procura que 
    utilizam informações sobre o problema para assim orientar a sua exploração
    do espaço de estados em direção a uma solução de uma forma mais rápida que os métodos
    de procura não informada. Ou seja, estes mecanismos ordenam a fronteira de exploração
    para assim os nós mais promissores (custo estimado até ao objetivo for menor) 
    serem explorados primeiro. Essas informações sobre o problema, são obtidas através
    de uma função heurística que estima o custo de um nó em relação ao objetivo do problema. 
    """

    def procurar(self, problema, heuristica):
        """
        Método que procura uma solução segundo um problema. Definindo
        a heuristica do avaliador.

        Args:
            problema: problema que se deseja obter uma solução.
            heuristica: heurística do problema
        """
        self._avaliador.definir_heuristica(heuristica)
        return super().procurar(problema)