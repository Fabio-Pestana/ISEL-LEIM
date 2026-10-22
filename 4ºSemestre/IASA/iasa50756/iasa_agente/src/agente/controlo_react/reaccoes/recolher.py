from controlo_react.reaccoes.aproximar.aproximar_alvo import AproximarAlvo
from controlo_react.reaccoes.evitar.evitar_obst import EvitarObst
from controlo_react.reaccoes.explorar.explorar import Explorar
from controlo_react.reaccoes.explorar.explorar_rodar import ExplorarRodar
from ecr.hierarquia import Hierarquia


class Recolher(Hierarquia):
    """
    Esta classe herda da classe Hierarquia, esta classe instância três
    comportamentos do agente (AproximarAlvo(), EvitarObst(), Explorar()), 
    ou seja esta classe representa um comportamento composto
    com os comportamentos mencionados anteriormente. Sendo que a seleção de uma ação deste comportamento
    é feito através de uma hierarquia, a sua ordem é AproximarAlvo(), EvitarObst(), Explorar().

    Quando este comportamento for ativado, o agente terá como objetivo recolher todos
    os seus targets. Para isto ele deve: 

    Aproximar-se dos targets caso os encontre -> AproximarAlvo()
    Evitar os obtáculos que existem no ambiente -> EvitarObst()
    Explorar o ambiente á procura dos seus alvos -> Explorar()
    """

    __comportamentos = [AproximarAlvo(), EvitarObst(), Explorar()]

    def __init__(self):
        """
        Método construtor de um Comportamento composto, utilizando 
        o método construtor da classe abstrata ComportComp guarda-se
        a lista de comportamentos que fazem parte do comportamento composto
        Recolher.
        """
        super().__init__(self.__comportamentos)