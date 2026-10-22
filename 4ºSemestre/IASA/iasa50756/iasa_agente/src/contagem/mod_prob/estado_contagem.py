from mod.estado import Estado

class EstadoContagem(Estado):
    """
    Esta classe representa uma especialização de um Estado, representa uma configuração
    de um problema que neste caso é o valor da contagem.
    """

    def __init__(self, valor):
        """
        Método construtor de um Estado da contagem.

        Args:
            valor: valor da contagem
        """
        self.__valor = valor

    @property
    def valor(self):
        """
        Propriedade que permite fazer uma leitura do valor do estado.
        """
        return self.__valor
    
    def id_valor(self):
        """
        Método que permite indentificar cada estado segundo o seu valor.

        Returns:
            id: id único para um estado
        """
        return hash(self.__valor)