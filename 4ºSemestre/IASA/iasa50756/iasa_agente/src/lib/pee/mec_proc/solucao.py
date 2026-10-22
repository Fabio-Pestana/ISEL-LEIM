from pee.mec_proc.passo_solucao import PassoSolucao

class Solucao():
    """
    Classe que representa uma solução de um certo problema, esta
    consiste numa sequência de estados e operadores que liga o estado
    inicial do problema com o estado que representa o objetivo.
    """

    def __init__(self, no_final):
        """
        Método construtor de uma Solução. Conhecendo o nó final, cria-se uma lista
        (__passos) que consiste na sequencia de estados e operadores entre o estado 
        final (objetivo) e o estado inicial, para isso faz-se um ciclo while onde se 
        verifica se o nó atual tem antecessores, se tiver significa que não é o nó raiz
        da árvore logo cria-se uma instância de PassoSolução (classe que guarda informação sobre
        um estado e o operador que o altera noutro), insere-se este objecto na lista dos
        passos e atualiza-se o nó atual da verificação, este processo continua até encontrarmos
        um nó sem antecessor sendo que isso significa que já atingimos todos os nós da 
        árvore deste o seu fim até ao inicio.

        Args:
            no_final: nó final, apartir deste obtem-se o percurso deste a solução até o estado inicial.
        """
        self.__no_final = no_final
        self.__passos = []
        no = no_final
        while no.antecessor:
            # Agrupa-se a informação do estado antecessor tal como o 
            # operador que levam ao estado no nó atual.
            # Para assim obtermos a informação de todos os nós da arvore
            # deste o final até ao inicial.
            passo = PassoSolucao(no.antecessor.estado, no.operador)
            self.__passos.insert(0, passo)
            no = no.antecessor

    @property
    def dimensao(self):
        """
        Propriedade que permite fazer uma leitura da dimensão do percurso entre
        o estado inicial até ao final através da propriedade profundidade do nó.
        """
        return self.__no_final.profundidade

    @property
    def custo(self):
        """
        Propriedade que permite fazer uma leitura do custo da solução.
        """
        return self.__no_final.custo
    
    
    def __iter__(self):
        """
        Método do Python que permite iterar sobre os passos da solução.
        
        Returns:
            iter: Um iterador sobre os passos da solução.
        """
        return iter(self.__passos)
    
    
    def __getitem__(self, index):
        """
        Método do Python que permite aceder os passos da solução através de índices.
        
        Args:
            index: O índice do passo da solução que se deseja acessar.

        Returns:
            PassoSolucao: O passo da solução no índice especificado.
        """
        return self.__passos[index]