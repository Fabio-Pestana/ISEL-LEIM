from mod.estado import Estado

class EstadoAgente(Estado):
    """
    Esta classe herda da classe abstrata Estado, sendo que representa
    uma configuração de um problema, sendo esta a posição do agente deliberativo 
    no ambiente virtual (mundo). Esta posição consiste nas coordenadas do agente
    no mundo. Para representar a localização do agente, utiliza-se um tuplo 
    com as suas coordenadas em x e y (classe Posicao fornecida pelos docentes no
    package lib.sae). 
    """
    def __init__(self, posicao):
        """
        Método construtor de um estado do agente deliberativo.

        Args:
            posicao: coordenadas do agente no mundo (ambiente virtual)
        """
        self.__posicao = posicao
        self.__id_valor = hash(self.__posicao)

    def id_valor(self):
        """
        Método que permite indentificar cada estado, com um id único, sendo este
        uma posição do agente no mundo.

        Returns:
            id: id único para um estado agente
        """
        return self.__id_valor
    
    @property
    def posicao(self):
        """
        Propriedade que permite fazer uma leitura da posição do agente.
        """
        return self.__posicao