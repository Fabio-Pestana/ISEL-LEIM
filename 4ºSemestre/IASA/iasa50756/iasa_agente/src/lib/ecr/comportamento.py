from abc import ABC, abstractmethod


class Comportamento(ABC):
    """
    Classe abstrada que representa um módulo comportamental (um comportamento).
    Um comportamento pode ser um conjunto de reações ou apenas uma, com o objetivo
    de criarem um certo resultado. Para isto um comportamento tem de conseguir relacionar
    os padrões de percepção com os de acção para assim atingir sempre o seu resultado.
    """

    @abstractmethod
    def activar(self, percepcao):
        """
            Método abstrado que quando implementado deve ativar uma acção 
            segundo uma percepção do ambiente.

            Args:
                percepcao: percepção do agente, do ambiente onde-se encontra
        """
        pass 