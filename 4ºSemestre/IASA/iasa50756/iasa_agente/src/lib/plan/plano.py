from abc import ABC, abstractmethod

class Plano(ABC):
    """
    Classe abstrata que representa um plano de ações de um agente deliberativo, 
    ou seja um plano consiste no resultado de um planeador, uma sequência de
    ações que o agente deve executar para atingir os seus objetivos.
    """
    @abstractmethod
    def obter_accao(self, estado):
        """
        Método abstrato que irá permitir obter um operador segundo o estado recebido.
        """

    @abstractmethod
    def mostrar(self, vista):
        """"""