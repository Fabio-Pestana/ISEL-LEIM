from abc import ABC, abstractmethod


class Heuristica(ABC):
    """
    Classe abstrata que representa a função heurística, designada por h(n), que consiste 
    no custo estimado de um nó até ao estado objetivo, sem que o percurso até este 
    tenha sido explorado. Para isto é necessário existir conhecimento acerca do domínio do problema,
    para assim ser possível orientar a procura para o objetivo desejado.
    Esta função h(n) necessita apenas de conhecer o estado do nó e o estado objetivo do problema.
    """
    @abstractmethod
    def h(self, estado):
        """
        Método abstrado que obtém o custo estimado do nó até ao estado objetivo do problema.

        Args:
            estado: estado do nó
        """