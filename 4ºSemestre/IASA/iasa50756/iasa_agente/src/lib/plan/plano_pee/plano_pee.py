from plan.plano import Plano

class PlanoPEE(Plano):
    """
    Esta classe herda da classe abstrata Plano, sendo que representa um plano de ações
    para uma procura de espaço de estados.
    """

    def __init__(self, solucao):
        """
        Método construtor da classe PlanoPEE.

        Guarda-se na variável privada __passos, os vários passos da solução do plano, 
        sendo isto possível, pois a solução é iterável segundo os passos desta 
        (método __iter__ da classe Solucao).

        Args:
            solucao: solução para a PEE
        """
        self.__solucao = solucao
        self.__passos = [passo for passo in solucao]

    
    def obter_accao(self, estado):
        """
        Método que, caso uma solução exista, permite obter um operador de
        um passo da solução. Se a solução existir, remove-se o primeiro elemento
        da lista __passos e caso o estado deste for igual ao estado recebido
        retorna-se o operador do passo da solução.

        Args:
            estado: estado do agente.

        Returns:
            operador: operador do passo da solução. Ou None se a solução não existir, ou
            se o estado do passo removido for diferente do estado recebido.
        """
        if self.__passos:
            passo = self.__passos.pop(0)
            if passo.estado == estado:
                return passo.operador


    def mostrar(self, vista):
        """"""
        if self.__passos:
            for passo in self.__passos:
                vista.mostrar_vector(passo.estado.posicao, passo.operador.ang)