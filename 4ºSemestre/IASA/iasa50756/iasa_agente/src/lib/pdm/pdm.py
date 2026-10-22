from pdm.mec_util import MecUtil

class PDM():
    """
    Classe que representa o processo de decisão de Markov, PDM. Este
    processo é um processo de decisão sequencial, onde os estados são afetados por ações,
    Utiliza-se funções da probabilidade de transição de estados e de recompensa, 
    para que o agente consiga tomar as melhores decisões no ambiente.

    Um PDM consiste num processo de decisão sequencial com a propriedade de
    Markov, esta propriedade diz que o futuro é independente do passado, ou seja
    para prever os estados sucessores do estado atual de um agente apenas importa esse 
    estado e não os seus valores anteriores.

    Um dos componentes fundamentais dos processos de decisão de Markov é a 
    política comportamental, nesta classe foi implementada uma politica comportamental 
    não determinista, esta envolve probabilidades na escolha da ação a ser 
    executada pelo agente, neste politica é feita uma distribuição de probabilidade sobre
    as possíveis ações para cada estado.
    """

    def __init__(self, modelo, gama, delta_max):
        """
        Método construtor da classe PDM.

        Args:
            modelo: modelo de PDM, processo de decisão de Markov
            gama: taxa de desconto no tempo
            delta_max: limiar de convergência (para o calculo da utilidade, classe
                MecUtil)
        """
        self.__modelo = modelo
        self.__mec_util = MecUtil(modelo, gama, delta_max)


    def politica(self, U):
        """
        Método que retorna o dicionário da política óptima para o conjunto de estados
        possíveis com base no valor máximo do calculo da utilidade das ações possiveis 
        para cada estado.

        Inicialmente guarda-se as referencias dos métodos do modelo de PDM para assim 
        facilitar a leitura e compreensão do código e inicializa-se um dicionário vazio.
        De seguida percorre-se a lista de estados possiveis e caso este estado tenha uma 
        lista de ações, faz-se o cálculo da utilidade para cada ação, para depois obter-se
        o seu valor máximo, sendo que este é guardado no dicionário.

        Args:
            U: dicionário com as utilidades calculadas para cada estado

        Returns:
            pol: dicionário da politica de estados (key -> estado ; value -> operador)
        """
        S, A = self.__modelo.S, self.__modelo.A
        pol = {}
        for s in S():
            if A(s):
                pol[s] = max(A(s), key=lambda a: self.__mec_util.util_accao(s, a, U))
        return pol

        
    def resolver(self):
        """
        Método que resolve o processo de decisão de Markov, calculando as utilidades
        e a política óptima para o conjunto de estados possíveis.

        Returns:
            U: dicionário com as utilidades calculadas para cada estado.
            pol: dicionário da politica de estados (key -> estado ; value -> operador)
        """
        U = self.__mec_util.utilidade()
        pol = self.politica(U)
        return U, pol
