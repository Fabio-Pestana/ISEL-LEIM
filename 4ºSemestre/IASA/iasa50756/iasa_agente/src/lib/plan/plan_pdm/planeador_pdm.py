from pdm.pdm import PDM
from plan.plan_pdm.modelo.modelo_pdm_plan import ModeloPDMPlan
from plan.plan_pdm.plano_pdm import PlanoPDM
from plan.planeador import Planeador

class PlaneadorPDM(Planeador):
    """
    Esta classe herda da classe abstrata Planeador, sendo que representa
    um planeador automático com base em processos de decisão de Markov, 
    PDM.

    Este planeador possui um atributo privado __gama, que num processo 
    de decisão de Markov, consiste numa taxa de desconto em relação ao 
    tempo. A taxa de desconto determina a importância das recompensas 
    futuras em comparação com as recompensas imediatas.
    """

    def __init__(self, gama=0.85, delta_max=1):
        """
        Método construtor de um planeador de PDM.
        
        Args:
            gama: intensidade do estímulo (Default = 0.85)
            delta_max: delta máximo (Default = 1)
        """
        self.__gama = gama
        self.__delta_max = delta_max

    def planear(self, modelo_plan, objectivos):
        """
        Método que permite a realização do planeamento das ações, para se
        obter o plano de ação.
        Inicialmente verifica-se se os objetivos existem, caso existam
        instância-se o modelo pdm e pdm (processo de decisão de Markov).
        De seguida utiliza-se o método resolver da classe PDM, que resolve o 
        processo de decisão de Markov, retornando as utilidades e a política 
        óptima para o conjunto de estados possíveis, estes valores depois são usados
        para instanciar um PlanoPDM. 

        Args:
            modelo_plan: modelo do mundo
            objectivos: lista de objetivos

        Returns:
            plano: plano de ações para uma procura segundo os processos de decisão
            de Markov.
        """
        if objectivos:
            modelo_pdm = ModeloPDMPlan(modelo_plan, objectivos)
            pdm = PDM(modelo_pdm, self.__gama, self.__delta_max)
            utilidade, politica = pdm.resolver()
            return PlanoPDM(utilidade, politica)
