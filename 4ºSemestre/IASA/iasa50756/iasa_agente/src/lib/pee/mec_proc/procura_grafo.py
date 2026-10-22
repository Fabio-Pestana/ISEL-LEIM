from abc import ABC
from pee.mec_proc.mecanismo_procura import MecanismoProcura


class ProcuraGrafo(MecanismoProcura, ABC):
    """
    Classe abstrata (para assim não ser instânciada) que é uma especialização de um mecanismo de procura, 
    sendo este o método de procura em grafos. O mecanismo de procura em grafos, evita a repetição da exploração 
    de um nó através dos seguintes verificações:

    - Verifica se um nó não existe na fronteira de exploração (nós abertos), nem nos 
    nós já expandidos (nós fechados). Neste caso, este nó gerado é adicionado á fronteira
    de exploração.

    - Verifica se um nó gerado pertence á fronteira de exploração, se sim verifica se este nó
    tem um menor custo que o nó existente na fronteira, se tiver o nó aberto é removido e insere-se
    o nó gerado na fronteira.

    - Verifica se um nó gerado pertence aos nós expandidos, se sim verifica se este nó
    tem um menor custo que o nó existente, se tiver o nó fechado é removido e insere-se
    o nó gerado na fronteira.

    Nesta implementação do mecanismo apenas se verifica se um nó já foi explorado pelo 
    método, através da criação de um dicionário onde se guarda o estado do nó e o seu respetivo nó.
    Isto permite ignorar a exploração de nós que já existem no dicionário.
    """
    
    def _iniciar_memoria(self):
        """      
        Método que coloca a lista de nós da fronteira de exploração vazia, 
        utilizando o super() e inicializa um dicionário vazio (_explorados), 
        que representa os nós que já foram explorados pelo mecanismo de procura.
        """
        super()._iniciar_memoria()

        self._explorados = {}

    def _memorizar(self, no):
        """ 
        Método que adiciona nós á fronteira de exploração e ao dicionário 
        de nós explorados, caso o estado deste nó não exista no dicionário dos
        nós explorados, o que indica que este ainda não foi explorado.

        Args:
            no: nó a ser inserido na fronteira e no dicionário _explorados.  
        """
        estado = no.estado
        if self._manter(no):
            self._fronteira.inserir(no)
            self._explorados[estado] = no

    def _manter(self, no):
        """
        Método que indica se um nó deve ser guardado ou descartado.

        Args:
            no: nó a verificar se o seu estado, existe nos dicionários dos nós explorados.  

        Returns:
            true: se não existe no dicionário dos nós explorados, indicando que este deve
            ser adicionado á fronteira de exploração pois ainda não foi explorado.
            false: caso exista no dicionário, ou seja já foi explorado.
        """
        return no.estado not in self._explorados