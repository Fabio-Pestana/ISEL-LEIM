from plan.plano import Plano

class PlanoPDM(Plano):
    """
    Esta classe herda da classe abstrata Plano, sendo que representa um plano de ações
    para um PDM, processos de decisão de Markov.
    """

    def __init__(self, utilidade, politica):
        """
        Método construtor da classe PlanoPDM.
        
        Args:
            utilidade: dicionário com as utilidades calculadas para cada estado do mundo
            politica: dicionário da politica de estados
        """
        self.__utilidade = utilidade
        self.__politica = politica

    def obter_accao(self, estado):
        """
        Método que retorna um operador segundo o estado recebido, 
        caso exista politica.

        Args:
            estado: estado do agente.

        Returns:
            operador: operador segundo o estado recebido. Ou None se a 
            politica não existir,
        """
        if self.__politica:
            return self.__politica.get(estado)


    def mostrar(self, vista):
        """
        """
        if self.__politica:
            for estado, valor in self.__utilidade.items():
                vista.mostrar_valor_posicao(estado.posicao, valor)

            for estado, accao in self.__politica.items():
                vista.mostrar_vector(estado.posicao, accao.ang)
        