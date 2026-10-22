from pee.mec_proc.prof.procura_profundidade import ProcuraProfundidade


class ProcuraProfLim(ProcuraProfundidade):
    """
    Esta classe é uma especialização do mecanismo de procura em profundidade, 
    representando o mecanismo de procura em profundidade limitada. 
    
    Este método de procura restringe a profundidade máxima dos nós que serão explorados. 
    Isso é útil para evitar a exploração excessiva em profundidades muito grandes, 
    o que pode levar a um consumo excessivo de recursos.
    """

    def _init_(self, prof_max):
        """
        Método construtor de um mecanismo de procura em profundidade limitada.
        Onde se utiliza o super(), para criar uma fronteira que segue a lógica LIFO. 

        Args:
            prof_max: profundidade máxima da procura.
        """
        super().__init__()
        self.__prof_max = prof_max


    def _expandir(self, problema, no):
        """
        Método responsável por expandir os nós na árvore de procura.
        Caso a profundidade do nó for menor que a profundidade máxima
        utiliza-se o método da classe abstrada mecanismo de procura, para
        expandir a árvore de procura segundo o nó recebido, se não retorna-se uma
        lista vazia.

        Args:
            problema: problema que se deseja obter uma solução.
            no: nó para expandir a árvore de procura.

        Returns:
            Uma lista contendo os nós sucessores válidos do nó atual, ou uma lista vazia
            se a profundidade máxima permitida for alcançada.

        """
        return super()._expandir(problema, no) \
                if no.profundidade < self.prof_max else []


    @property
    def prof_max(self):
        """
        Propriedade que permite fazer uma leitura da profundidade máxima da procura.
        """
        return self.__prof_max


    @prof_max.setter
    def prof_max(self, valor):
        """
        Set em Python 
        Permite alterar o valor da profundidade máxima da procura.
        
        Args:
            valor: novo valor da profundidade máxima
        """
        self.__prof_max = valor