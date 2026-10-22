from pee.mec_proc.melhor_prim.aval.avaliador import Avaliador

class AvaliadorHeur(Avaliador):
    """
    Esta classe herda da classe abstrata Avaliador, sendo que representa
    um avaliador de nós, que permite utilizar heurísticas para definir 
    a prioridade de um nó.
    """
    def definir_heuristica(self, heuristica):
        """
        Método que define a heurística do avaliador.

        Args:
            heuristica: função que permite obter o custo estimado do nó até ao estado objetivo.
        """
        self._heuristica = heuristica