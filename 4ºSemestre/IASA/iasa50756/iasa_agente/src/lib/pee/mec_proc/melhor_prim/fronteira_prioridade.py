from pee.mec_proc.fronteira import Fronteira
from heapq import heappush, heappop

class FronteiraPrioridade(Fronteira):
    """
    Esta classe herda da classe abstrada Fronteira, sendo que representa
    uma fronteira de exploração onde os seus elementos são inseridos
    e removidos segundo um avaliador de prioridade dos nós. Ou seja, os nós 
    nesta fronteira de exploração são listados segundo a avaliação 
    fornecida (variável privada, __avaliador), quando um nó é inserido na fronteira e é 
    colocado na lista segundo uma ordem crescente, para isto utiliza-se o método heappush, 
    que permite inserir um nó na fronteira. Para remover nós da fronteira de exploração utiliza-se o
    método heappop que segundo uma lista, remove e retorna o elemento mais pequeno (neste caso,
    segundo a sua prioridade). Em ambos os métodos depois de serem inseridos ou removidos os nós, estes 
    métodos referidos anteriormente ajustam a ordem da lista segundo a sua prioridade mantendo 
    a sua estrutura. 
    """

    def __init__(self, avaliador):
        """
        Método construtor de uma Fronteira, onde-se cria uma lista de nós vazia, 
        através do super().

        Args:
            avaliador: Avaliador da prioridade dos nós.
        """
        super().__init__()
        self.__avaliador = avaliador

    def inserir(self, no):
        """
        Método para inserir um nó na lista de nós da fronteira, sem alterar a sua estrutura.

        No método heappush, não é obrigatório passar o tuplo (prioridade, no), pois na classe
        Nó já foi implementado um método __lt__ (less than), ou seja já foi implementado um 
        mecanismo para comparar os nós segundo o seu custo.

        Args:
            no: nó a ser inserido na fronteira.
        """
        prioridade = self.__avaliador.prioridade(no)
        heappush(self._nos, (prioridade, no))


    def remover(self):
        """
        Método para remover um nó na lista de nós da fronteira, sem alterar a sua estrutura.

        Para permitir uma clareza de código e a sua simplicidade, utiliza-se
        o _ no retorno do heappop, pois esta é uma variável anónima, 
        que não é utilizada.

        Returns:
            no: nó removido da fronteira.
        """
        _, no = heappop(self._nos)
        return no
