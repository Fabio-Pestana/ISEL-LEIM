from mod_prob.estado_contagem import EstadoContagem
from mod_prob.operador_incremento import OperadorIncremento
from mod.problema import Problema


class ProblemaContagem(Problema):
    """
    Esta classe representa uma especialização de um Problema, sendo que neste caso
    é um problema de contagem (dado pelo docente).
    """

    def __init__(self, valor_inicial, valor_final, incrementos):
        """
        Método construtor de um Problema da contagem.

        Args:
            valor_inicial: estado inicial do problema (valor inicial da contagem)
            valor_final: estado que representa o objetivo (atingir ou ultrapassar
            esse valor na contagem).
            incrementos: valor do incremento
        """

        estado_inicial = EstadoContagem(valor_inicial)
        operadores = [OperadorIncremento(inc) for inc in incrementos]

        super().__init__(estado_inicial, operadores)

        self.__valor_final = valor_final

    def objectivo(self, estado):
        """
        Método que compara o estado que recebe como parametro com o objetivo 
        do problema, para assim saber-se se o objetivo já foi ou não atingido.

        Args:
            estado: estado que será comparado com o estado que representa 
            o objetivo do problema.

        Returns:
            true: se o valor do estado recebido for igual ou superior ao valor do estado objetivo 
            (__valor_final)
            false: caso contrário
        """
        return estado.valor >= self.__valor_final
