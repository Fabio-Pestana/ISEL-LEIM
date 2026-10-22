from random import choice

from .resposta_mover import RespostaMover
from sae import Direccao


class RespostaMoverAleat(RespostaMover):
    """
    Esta classe representa uma especialização de uma resposta a um estímulo. Que 
    irá provocar uma ação ao agente (o seu movimento numa certa direção, 
    escolhida de uma forma aleatória).
    """
    def __init__(self):
        """
        Método construtor de uma Resposta para uma ação mover com uma direção random,
        utilizando o método construtor da classe RespostaMover passa-se uma direção 
        random para depois ser associada a uma Accao (atributo da classe Resposta).
        """

        direcao_aleatoria = choice(list(Direccao))
        super().__init__(direcao_aleatoria)