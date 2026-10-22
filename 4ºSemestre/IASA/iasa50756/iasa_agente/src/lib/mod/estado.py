from abc import ABC, abstractmethod


class Estado(ABC):
    """
    Classe abstrata que representa um Estado, que representa uma configuração de um
    problema.
    """

    @abstractmethod
    def id_valor(self):
        """
        Método abstrado que permite indentificar cada estado (com um id único).
        """


    def __hash__(self):
        """
        Método do Python que permite retornar um valor único 
        para um objeto.

        Returns:
            id: id único para um estado
        """
        return self.id_valor()
    

    def __eq__(self, other):
        """
        Método que permite comparação entre dois objetos do tipo Estado.
        Este método irá permitir a comparação de dois estados,
        para sabermos se estes são duas configurações iguais de um problema.
        Para isto compara-se o identificador único de cada Estado.

        Args:
            other: estado a ser comparado.
        
        Returns:
            true: se ambos os estados tiverem o mesmo identificador.
            false: caso contrário.
        """
        if isinstance(other, Estado):
            return self.__hash__() == other.__hash__()