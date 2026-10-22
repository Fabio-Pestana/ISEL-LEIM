from ecr.prioridade import Prioridade
from sae import Direccao

from .aproximar_dir import AproximarDir


class AproximarAlvo(Prioridade):
    """
    Esta classe herda da classe Prioridade e representa um comportamento composto
    constituido por sub-comportamentos específicos para assim o agente conseguir
    aproximar-se dos seus targets através de uma das direções (NORTE, SUL,
	ESTE, OESTE).

    Esta classe instância um comportamento AproximarDir para cada uma das 4 direções
    que o agente pode se movimentar, sendo este um comportamento que permite ao agente
    deslocar-se numa certa direção para assim estar mais próximo do seu objetivo, os alvos.

    A seleção de uma ação deste comportamento é feita através de um mecanismo de prioridade. 
    O agente ao percepcionar o ambiente, cada comportamento obtém um estímulo, aquele
    que tiver uma maior intensidade significa que o agente está mais próximo de um target na direção 
    que esse comportamento corresponde, logo este vai ser o comportamento com a ação mais prioritária 
    para ser executada.
    """

    def __init__(self):
        """
        Método construtor de um Comportamento composto, utilizando 
        o método construtor da classe abstrata ComportComp guarda-se
        a lista de comportamentos (instâncias de AproximarDir para cada uma das
        direções existentes na classe Direccao - NORTE, SUL, ESTE, OESTE) que 
        fazem parte do comportamento composto AproximarAlvo.
        """
        accoes = []
        for dir in list(Direccao):
            accoes.append(AproximarDir(dir))

        super().__init__(accoes)