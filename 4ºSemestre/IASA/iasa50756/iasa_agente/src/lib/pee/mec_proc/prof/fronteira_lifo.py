from pee.mec_proc.fronteira import Fronteira


class FronteiraLIFO(Fronteira):
    """
    Esta classe herda da classe abstrata Fronteira, sendo que representa
    uma fronteira de exploração onde os seus elementos são inseridos
    e removidos segundo a lógica LIFO (Last-In-First-Out). Ou seja, 
    o último nó inserido na fronteira será o primeiro a ser removido
    desta.
    """

    def inserir(self, no):
        """
        Método para inserir um nó no inicio da lista de nós da fronteira.

        Args:
            no: nó a ser inserido na fronteira.
        """
        self._nos.insert(0, no)