from pee.mec_proc.fronteira import Fronteira


class FronteiraFIFO(Fronteira):
    """
    Esta classe herda da classe abstrata Fronteira, sendo que representa
    uma fronteira de exploração onde os seus elementos são inseridos
    e removidos segundo a lógica FIFO (First-In-First-Out). Ou seja, 
    o primeiro nó inserido na fronteira será o primeiro a ser removido
    desta.
    """
    
    def inserir(self, no):
        """
        Método para inserir um nó no fim da lista de nós da fronteira.

        Args:
            no: nó a ser inserido na fronteira.

        Nota: Na versão anterior utilizava-se o método insert, sendo que não se
        colocou o indice que especifica a posição onde se deve colocar o nó na 
        lista, como o nó será inserido no fim podia-se passar como parametro no método
        insert o indice -1 no entanto para facilitar a leitura do código 
        decidiu-se utilizar o método append do python que coloca o elemento no fim da lista.
        """
        self._nos.append(no)