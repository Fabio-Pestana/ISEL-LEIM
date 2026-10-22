from abc import ABC, abstractmethod

class Planeador(ABC):
    """
    Classe abstrata que representa um planeador de um agente deliberativo, ou seja o
    mecanismo que permite decidir a sequência de ações a executar de forma a 
    realizar todos os objetivos deliberados.
    """
    
    @abstractmethod
    def planear(self, modelo_plan, objectivos):
        """
        Método abstrato que irá realizar o planeamento das ações, para se
        obter o plano de ação.
        """

        