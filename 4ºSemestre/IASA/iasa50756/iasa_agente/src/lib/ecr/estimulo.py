from abc import ABC, abstractmethod


class Estimulo(ABC):
    """
     Classe abstrada que representa um estímulo detetado por um agente.
     Um estímulo é um elemento de uma percepção que leva a ações por parte
     do agente.
    """

    @abstractmethod
    def detectar(self, percepcao):
        """
            Detectar estimulo segundo uma percepção.

            Args:
                percepcao: percepção do agente, do ambiente onde-se encontra
        """