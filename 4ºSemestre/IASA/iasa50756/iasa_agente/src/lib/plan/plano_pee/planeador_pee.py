from pee.mec_proc.melhor_prim.procura_aa import ProcuraAA
from plan.planeador import Planeador
from plan.plano_pee.mod_prob.heur_dist import HeurDist
from plan.plano_pee.mod_prob.problema_plan import ProblemaPlan
from plan.plano_pee.plano_pee import PlanoPEE

class PlaneadorPEE(Planeador):
    """
    Esta classe herda da classe abstrata Planeador, sendo que representa
    um planeador baseado no mecanismo de raciocínio automático, procura 
    em espaço de estados.
    """

    def __init__(self):
        """
        Método construtor de um planeador de procura em espaço de estados.
        Onde se instância um mecanismo de procura, sendo este o mecanismo de 
        procura informada, A*.
        """
        self.__mec_pee = ProcuraAA()
    
    def planear(self, modelo_plan, objectivos):
        """
        Método que permite a realização do planeamento das ações, para se
        obter o plano de ação.

        Considera-se que a lista de objectivos vem ordenada
        segundo a sua prioridade. Logo escolhe-se o primeiro elemento
        desta lista.
        Cria-se uma instância de ProblemaPlan e de HeurDist, pois 
        no mecanismos de procura A* é necessário passar um problema e uma
        heuristica, para assim obter-se uma solução ao problema, sendo que
        caso esta exista instância-se um plano sobre ela.

        Args:
            modelo_plan: modelo do mundo
            objectivos: lista de objetivos

        Returns:
            plano: plano de ações para uma procura de espaço de estados, caso exista 
            solução ao problema. No caso de não existir retorna-se None.
        """
        if objectivos:
            estado_final = objectivos[0]
            problema = ProblemaPlan(modelo_plan, estado_final)
            heuristica = HeurDist(estado_final)
            solucao = self.__mec_pee.procurar(problema, heuristica)
            if solucao:
                return PlanoPEE(solucao)