from abc import ABC, abstractmethod
from pee.mec_proc.no import No
from pee.mec_proc.solucao import Solucao


class MecanismoProcura(ABC):
    """
    Classe abstrada que representa um mecanismo de procura, ou seja
    um mecanismo que segundo um problema, ele vai percorrer um conjunto
    de estados possiveis para a resolução do problema (Espaço de estados)
    até encontrar um solução.
    """

    def __init__(self, fronteira):
        """
        Método construtor de um mecanismo de procura.

        Args:
            fronteira: fronteira de exploração
        """
        self._fronteira = fronteira


    def _iniciar_memoria(self):
        """
        Método que coloca a lista de nós da fronteira de exploração vazia.
        """
        self._fronteira.iniciar()

    @abstractmethod
    def _memorizar(self, no):
        """
        Método abstrado que adiciona um nó a fronteira de exploração.

        Args:
            no: nó a adicionar
        """

    def procurar(self, problema):
        """
        Método que procura uma solução segundo um problema.

        Inicialmente inicializa-se uma lista de nós vazia, 
        e instância-se um nó segundo o estado inicial do problema, 
        este depois será guardado na fronteira (método abstrato que 
        depende do mecanismo de procura).

        Percorre-se a fronteira de exploração até esta for vazia com
        o objetivo de encontrar uma solução para o problema, para isto
        remove-se o nó da fronteira e testa-se, se o estado deste nó é 
        o objetivo deste problema. Se o estado for o objetivo do 
        problema instância-se uma Solução segundo o nó removido, se não
        expande-se a árvore de procura e atualiza-se a fronteira de exploração
        (com os nós filhos) para assim repetir-se o ciclo da
        procura pelo estado que representa o objetivo do problema.

        Args:
            problema: problema que se deseja obter uma solução.
        """
        self._iniciar_memoria()
        no = No(problema.estado_inicial)
        self._memorizar(no)

        while not self._fronteira.vazia:
            
            no = self._fronteira.remover()
            if problema.objectivo(no.estado):
                return Solucao(no)
            for no_sucessor in self._expandir(problema, no):
                self._memorizar(no_sucessor)


    def _expandir(self, problema, no):
        """
        Método que expande a árvore de procura através do nó que recebe como 
        parametro, percorre-se todos os operadores que existem no problema, 
        onde se aplica cada um ao estado do nó, se o estado sucessor não for
        None calcula-se o custo do novo nó, para depois o instânciar e o adicionar
        a uma lista.

        Args:
            problema: problema que se deseja obter uma solução.
            no: nó para expandir a árvore de procura
        
        Returns:
            sucessores: lista de nós filhos do nó recebido como parametro
        """
        sucessores = []
        estado = no.estado
        for operador in problema.operadores:
            estado_suc = operador.aplicar(estado)
            if estado_suc is not None:
                custo = no.custo + operador.custo(estado, estado_suc)
                no_sucessor = No(estado_suc, operador, no, custo)
                sucessores.append(no_sucessor)
        
        return sucessores
        
