
class No():
    """
    Classe que representa um nó em uma árvore de procura.
    """

    def __init__(self, estado, operador = None, antecessor = None, custo = 0):
        """
        Método construtor de um nó. Caso este nó tenha um nó antecessor, 
        a profundidade deste nó vai ser a do seu antecessor mais um, caso este nó
        não tenha antecessor a __profundidade fica a 0. 

        Args:
            estado: estado representado pelo nó.
            operador: operador que levou ao estado (opcional).
            antecessor: nó antecessor (opcional).
            custo: custo até este nó (opcional).
        """
        self.__estado = estado
        self.__operador = operador
        self.__antecessor = antecessor
        self.__custo = custo
        
        if antecessor:
            self.__profundidade = antecessor.profundidade + 1
        else:
            self.__profundidade = 0

    @property
    def profundidade(self):
        """
        Propriedade que permite fazer uma leitura da profundidade do nó na 
        árvore de procura.
        """
        return self.__profundidade

    @property
    def custo(self):
        """
        Propriedade que permite fazer uma leitura do custo do nó.
        """
        return self.__custo

    @property
    def estado(self):
        """
        Propriedade que permite fazer uma leitura do estado do nó.
        """
        return self.__estado

    @property
    def operador(self):
        """
        Propriedade que permite fazer uma leitura do operador que levou
        ao estado do nó.
        """
        return self.__operador

    @property
    def antecessor(self):
        """
        Propriedade que permite fazer uma leitura do nó antecessor do nó.
        """
        return self.__antecessor
    
    def __lt__(self, other):
        """
        Método que permite comparar dois objetos em python

        Args:
            other: O outro objeto No a ser comparado.

        Returns:
            true: se o custo deste nó for menor que o custo do outro nó.
            false: caso contrário.
        """
        return self.custo < other.custo
