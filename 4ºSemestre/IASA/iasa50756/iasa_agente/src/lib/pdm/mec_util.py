class MecUtil():
    """
    Esta classe representa um dos componentes fundamentais dos processos de decisão
    de Markov, a utilidade. A utilidade, representada por U, consiste na recompensa
    de um agente ao executar uma ação num estado, para assim transitar para outro.
    Esta é diferente da função R da recompensa, pois a utilidade é a soma das
    recompensas "atuais" (segundo o estado atual do agente) e as recompensas futuras 
    esperadas, ponderadas pela taxa de desconto, gama (Recompensa descontadas no tempo).
    """

    def __init__(self, modelo, gama, delta_max):
        """
        Método construtor da classe MecUtil.

        Args:
            modelo: modelo de PDM, processo de decisão de Markov
            gama: taxa de desconto, no tempo
            delta_max: limiar de convergência (para o calculo da utilidade)
        """
        self.__modelo = modelo
        self.__gama = gama
        self.__delta_max = delta_max

        
    def utilidade(self):
        """
        Método que calcula o valor da utilidade de estado.

        Inicialmente guarda-se as referencias dos métodos do modelo de PDM
        para assim facilitar a leitura e compreensão do código. 

        Inicializa-se um dicionário onde cada estado tem o seu valor a 0
        (valor da utilidade). De seguida utiliza-se um loop while True, onde se 
        guarda um cópia do dicionário e depois entra-se num ciclo for onde se vai iterar
        segundo os estados da lista de estados possiveis. Neste for atualiza-se os valores
        da utilidade para cada estado, segundo o valor máximo obtido no cálculo da
        utilidade para cada operador.
        Depois de ciclo for verifica-se a condição de paragem do ciclo while foi atingida,
        esta consiste no valor da diferença da utilidade da cópia do dicionario e o modificado.
        Se este valor for inferior ou igual ao delta_max, o ciclo pára e retorna-se o dicionário U.

        Returns:
            U: dicionário com as utilidades calculadas para cada estado.
        """
        S, A = self.__modelo.S, self.__modelo.A

        # Nota -> trocou-se a inicialização do dicionário de 0 (valor inteiro)
        # para 0.0 (valor em float)

        U = {s: 0.0 for s in S()}

        while True:
            #Copia-se os valores do dicionário U
            Uant = U.copy()
            delta = 0

            for s in S():
                #Nota: adicionou-se o default = 0, pois se um estado não tiver ações possiveis,
                #considera-se o valor por omissão 0
                U[s] = max([self.util_accao(s, a, Uant) for a in A(s)], default=0)
                delta = max(delta, abs(U[s] - Uant[s]))

            if delta <= self.__delta_max:
                break

        return U
        
    def util_accao(self, s, a, U):
        """
        Método que calcula a utilidade de uma ação.
        Este método consiste no somatório da fórmula dada pelos
        docentes para todos os estados sucessores de um estado quando lhe 
        é aplicado um operador.

        Fórmula -> somatório para todos os estados sucessores, sn:
                        T(s,a,sn) * [R(s,a,sn) + gama * U[sn]]

        Args:
            s: estado inicial
            a: operador que faz a transição do estado s para outro.
            U: dicionário com os valores da utilidade para cada estado.

        Returns:
            utilidade de uma ação.
        """
        T, R, suc = self.__modelo.T, self.__modelo.R, self.__modelo.suc

        return sum(T(s, a, sn) * (R(s, a, sn) + self.__gama * U[sn]) for sn in suc(s, a))