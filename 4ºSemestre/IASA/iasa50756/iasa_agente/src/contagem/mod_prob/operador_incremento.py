from mod_prob.estado_contagem import EstadoContagem
from mod.operador import Operador


class OperadorIncremento(Operador): 
    """
    Esta classe representa uma especialização de um Operador, representa a ação
    que permite a alteração de um estado que neste caso é o valor da incremento
    da contagem.
    """

    def __init__(self, incremento):
        """
        Método construtor de um Operador da contagem.

        Args:
            incremento: valor da incremento
        """
        self.__incremento = incremento 

    def aplicar(self, estado):
        """
        Método que produz uma transformação a um estado, neste
        caso instância um novo estado segundo a soma do valor do estado recebido
        como argumento e o valor do incremento.

        Args:
            estado: estado que será alterado com a aplicação do operador.

        Returns:
            estado: estado sucessor com o valor da soma do estado recebido com o valor
            do incremento.
        """
        return EstadoContagem(estado.valor + self.__incremento)

    def custo(self, estado, estado_suc):
        """
        Método que indica o custo da transição entre estados (uma métrica).
        Neste caso não é necessário utilizar os parametros, 
        pois o custo não é calculado apartir dos estados
        mas sim pelo valor do incremento.

        Args:
            estado: estado que será aplicado do operador (não é utilizado)
            estado_suc: estado sucessor (não é utilizado)

        Returns:
            custo: o valor do custo (valor do incremento elevado ao quadrado).
        """
        return self.__incremento**2
    
    @property
    def incremento(self):
        """
        Propriedade que permite fazer uma leitura do valor do incremento.
        """
        return self.__incremento