import math
from agente.controlo_delib.modelo.estado_agente import EstadoAgente
from mod.operador import Operador
from sae import Accao

class OperadorMover(Operador):
    """
    Esta classe herda da classe abstrata Operador, sendo que representa
    uma ação que permite a alteração de um estado, sendo esta a deslocação
    do agente no mundo. Como um estado consiste na posição do agente deliberativo
    no ambiente, o operador vai ser o que permite a transição entre estados, logo 
    os operadores consistem na deslocação do agente no mundo. Para isto é necessário
    conhecer o mundo, para se conhecer os vários estados possíveis e é necessário saber
    a direção da deslocação do agente.
    """

    def __init__(self, modelo_mundo, direccao):
        """
        Método construtor de um operador que permite a deslocação de
        um agente deliberativo. 
        Instância-se uma Accao segundo a direccao fornecida, para assim permitir
        a deslocação do agente no ambiente.

        Args:
            modelo_mundo: representação interna do ambiente
            direccao: direção do movimento do agente
        """
        self.__modelo_mundo = modelo_mundo
        self.__accao = Accao(direccao)

    @property
    def ang(self):
        """
        Propriedade que permite fazer uma leitura do ângulo da direção da ação
        """
        return self.__accao.ang

    @property
    def accao(self):
        """
        Propriedade que permite fazer uma leitura da ação.
        """
        return self.__accao

    def aplicar(self, estado):
        """
        Método que aplica o operador ao estado atual do agente, permitindo a deslocação
        do agente, caso o novo estado criado, seja uma das configurações possíveis do
        mundo.

        Args:
            estado: estado atual do agente.

        Returns:
            novo_estado: estado do agente após a aplicação do operador. Ou None se o estado
                criado não for válido, ou seja não é um dos estados possíveis.
        """
        nova_posicao = self.__translaccao(estado.posicao, self.accao.passo, self.ang)
        novo_estado = EstadoAgente(nova_posicao)
        if novo_estado in self.__modelo_mundo.obter_estados():
            return novo_estado

    def custo(self, estado, estado_suc):
        """
        Método que indica o custo da transição entre estados, 
        através do cálculo da distância entre os estados recebidos (estado
        atual do agente, e o futuro estado deste, após a aplicação do operador).

        Args:
            estado: estado atual do agente.
            estado_suc: futuro estado do agente.
        
        Returns:
            distância entre os estados fornecidos.
        """
        return math.dist(estado.posicao, estado_suc.posicao)

    def __translaccao(self, posicao, distancia, angulo):
        """
        Método privado que realiza a translação da posição do agente com base na distância e
        ângulo fornecidos.
        Utiliza-se a modularização, para assim criarmos um método que apenas faz a translação 
        geométrica, sem se preocupar com o contexto do problema onde este se encontra.

        Args:
            posicao: posição atual do agente.
            distancia: distância a ser percorrida.
            angulo: ângulo da direção do movimento.

        Returns:
            nova_posicao: nova posição do agente.
        """
        
        x, y = posicao
        dx = round(distancia * math.cos(angulo))
        dy = -round(distancia * math.sin(angulo))
        nova_posicao = x + dx, y + dy
        return nova_posicao
