from ecr.resposta import Resposta
from sae import Accao

class RespostaMover(Resposta):
    """
    Esta classe representa uma especialização de uma resposta a um estímulo. Que 
    irá provocar uma ação ao agente (o seu movimento numa certa direção, atributo da classe).
    """
    def __init__(self, direccao):
        """
        Método construtor de uma Resposta para uma ação mover, utilizando 
        o método construtor da classe Resposta guarda-se a ação que o 
        agente deve realizar segundo uma certa direção (instância-se
        uma Accao segundo o argumento direccao).

        Args:
            direccao: direção do movimento que o agente deve executar.
        """
        super().__init__(Accao(direccao))