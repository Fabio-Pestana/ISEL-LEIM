from abc import ABC, abstractmethod

class Operador(ABC):
    """
    Classe abstrada que representa um Operador, ou seja, uma ação
    que permite a alteração de um estado.
    """

    @abstractmethod
    def aplicar(self, estado):
        """
        Método abstrado que produz uma transformação a um estado.

        Args:
            estado: estado que será alterado com a aplicação do operador.
        """

    @abstractmethod
    def custo(self, estado, estado_suc):
        """
        Método abstrado que indica o custo da transição 
        entre estados (uma métrica).

        Args:
            estado: estado que será aplicado do operador
            estado_suc: estado sucessor
        """