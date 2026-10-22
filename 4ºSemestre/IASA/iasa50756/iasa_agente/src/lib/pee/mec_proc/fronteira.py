from abc import ABC, abstractmethod

class Fronteira(ABC):
    """
    Classe abstrata que representa uma Fronteira de Exploração, ou seja
    um nível de nós que ainda não foram explorados dentro de uma árvore
    de procura.

    Uma árvore de procura baseia-se em nós, em cada nó têm-se informação sobre o
    estado, o operador que resultou esse estado e o seu estado antecessor. Sendo 
    a fronteira de exploração é uma parte da árvore que consiste em nós abertos 
    (por expandir). 
    """
    def __init__(self):
        """
        Método construtor de uma Fronteira, onde-se cria uma lista de nós vazia.
        """
        self.iniciar()

    
    def iniciar(self):
        """
        Método que inicializa a lista de nós vazia.
        """
        self._nos = []
    

    @abstractmethod
    def inserir(self, no):
        """
        Método abstrado que permite inserir um nó na fronteira.

        Args:
            no: nó que será inserido na fronteira.
        """

    def remover(self):
        """
        Método que remove e retorna o primeiro nó na fronteira.

        Returns:
            no: primeiro nó na lista nós da fronteira.
        """
        return self._nos.pop(0)

    @property    
    def vazia(self):
        """
        Propriedade que permite ver se a lista de nós tem elementos ou não.

        Returns:
            true: lista está vazia
            false: caso contrário
        """
        return len(self._nos) == 0
