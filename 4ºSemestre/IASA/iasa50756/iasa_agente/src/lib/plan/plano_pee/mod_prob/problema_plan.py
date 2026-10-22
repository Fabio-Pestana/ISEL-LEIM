from mod.problema import Problema


class ProblemaPlan(Problema):
    """
    Esta classe representa uma especialização de um Problema, sendo que representa
    um problema de um planeador de um agente deliberativo. 
    """
    def __init__(self, modelo_plan, estado_final):
        """
        Método construtor de um Problema de um planeador. Onde se utiliza o super()
        para guardar o estado inicial do problema e os seus operadores.

        Args:
            modelo_plan: modelo do mundo
            estado_final: estado que representa o objetivo 
        """
        super().__init__(modelo_plan.obter_estado(), modelo_plan.obter_operadores())
        self.__estado_final = estado_final
    
    def objectivo(self, estado):
        """
        Método que compara o estado que recebe como parametro com o estado final 
        do problema (objetivo), para assim saber-se se o objetivo já foi ou não atingido.

        Args:
            estado: estado que será comparado com o estado que representa 
            o objetivo do problema.

        Returns:
            true: se o estado recebido for igual ao estado final 
            false: caso contrário
        """
        return estado == self.__estado_final
