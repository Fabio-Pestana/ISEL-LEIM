
from agente.controlo_delib.modelo.estado_agente import EstadoAgente
from agente.controlo_delib.modelo.operador_mover import OperadorMover
from plan.modelo.modelo_plan import ModeloPlan
from math import dist
from sae import Direccao
from sae import Elemento

class ModeloMundo(ModeloPlan):
    """
    Esta classe herda da classe abstrada ModeloPlan, sendo que representa
    o modelo do mundo, ou seja, uma representação interna do ambiente, sendo esta 
    criada e atualizada segundo as perceções do agente. 
    Nestes agentes deliberativos utiliza-se memória para guardar-se informações 
    sobre o ambiente como:
        - Os elementos, que abrange todos os constituintes percecionados pelo agente 
        (como alvos e obstáculos);
        - O estado atual do agente;
        - Todos os estados ou seja configurações possiveis do mundo; 
        - Os operadores que consistem em ações que permitem que o estado do agente
        seja alterado, sendo que isto leva á sua deslocação no mundo.

    Esta representação do mundo é essencial para um agente deliberativo, pois ela permite
    a compreensão e interação do agente com o mundo.
    """

    def __init__(self):
        """
        Método construtor de um modelo do mundo (representação interna do agente).
        Inicializa-se a lista de operadores, segundo instâncias de OperadorMover,
        onde se passa o modelo do Mundo (através do self) e uma direção sendo que esta representa
        uma das direções do enumerator Direccao (direções em que o agente consegue se mover).
        """
        self.__elementos = {}
        self.__estados = []
        self.__recolha = False
        self.__estado = None
        self.__operadores = [OperadorMover(self, direccao) for direccao in Direccao]

    @property
    def alterado(self):
        """
        Propriedade que permite fazer uma leitura da alteração do modelo do mundo (retorna false
        se este não foi alterado).
        """
        return self.__recolha

    @property
    def elementos(self):
        """
        Propriedade que permite fazer uma leitura dos elementos percecionados pelo agente.
        """
        return self.__elementos

    def obter_estado(self):
        """
        Método que permite obter o estado atual do agente.

        Returns:
            estado: estado atual do agente
        """
        return self.__estado

    def obter_estados(self):
        """
        Método que permite obter a lista de todas as configurações possiveis do mundo.

        Returns:
            estados: estados possiveis
        """
        return self.__estados

    def obter_operadores(self):
        """
        Método que permite obter a lista dos operadores.

        Returns:
            operadores: operadores do modelo do mundo
        """
        return self.__operadores

    def obter_elemento(self, estado):
        """
        Método que permite obter um elemento da lista de elementos, segundo a posição
        do estado recebido (propriedade do estado).

        Args:
            estado: estado para o qual o elemento deve ser obtido.

        Returns:
            elemento: elemento relacionado com a posição do estado. Se não existir no 
                dicionário retorna None.
        """
        return self.__elementos.get(estado.posicao)

    def actualizar(self, percepcao):
        """
        Método que atualiza o modelo do mundo segundo a perceção do agente.

        Inicialmente atualiza-se o estado atual do agente, segundo a posição da perceção, de seguida
        atualiza-se a lista dos estados possíveis segundo a perceção do agente e atualiza-se os 
        elementos percecionados. Por fim atualiza-se a variável
        privada __recolha, para assim identificar-se a atualização ou não do modelo do mundo. Esta
        variável é atualizada segundo a variável recolha da percepção, esta é alterada caso o agente
        tenha recolhido um alvo, o que significa que o modelo do mundo foi alterado.
        
        Args:
            percepcao: perceção atual do agente
        """
        self.__estado = EstadoAgente(percepcao.posicao)
        """
        # Primeira implementação do método, no entanto esta pode ser simplificada

        if percepcao.elementos == self.__elementos:
            self.__alterado = False
        else:
            self.__elementos = percepcao.elementos
            self.__estados = [] 
            for posicao in percepcao.posicoes:
                self.__estados.append(EstadoAgente(posicao))

            self.__alterado = True
        """
        # Refeito com o docente
        self.__estados = [EstadoAgente(posicao) for posicao in percepcao.posicoes]
        self.__elementos = percepcao.elementos
        self.__recolha = percepcao.recolha

    
    def distancia(self, estado):
        """
        Método que calcula a distância entre o estado atual do agente e um estado fornecido.

        Args:
            estado: estado para calcular a distância com o estado atual do agente.

        Returns:
            distância entre o estado atual do agente e o estado fornecido.
        """
        return dist(estado.posicao, self.__estado.posicao)

    def mostrar(self, vista):
        """
        """
        for posicao, elemento in self.__elementos.items():
            if elemento in [Elemento.ALVO, Elemento.OBSTACULO]:
                vista.mostrar_elemento(posicao, elemento)

            vista.marcar_posicao(self.__estado.posicao)