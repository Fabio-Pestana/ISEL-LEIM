from ecr.reaccao import Reaccao
from ..resposta.resposta_mover import RespostaMover
from .estimulo_alvo import EstimuloAlvo

class AproximarDir(Reaccao):
    """
    Esta classe representa uma reação, ou seja um comportamento simples
    que permite ao agente, caso encontre um elemento no ambiente do tipo alvo
    possui uma resposta sendo esta a sua deslocação na sua direção.
    """
    
    def __init__(self, direccao):
        """
        Método construtor de um Comportamento simples, AproximarDir, utilizando 
        o método construtor da classe Reaccao guarda-se o estímulo (instância-se 
        um EstimuloAlvo(), estímulo captado pelo agente ao precepcionar
        o ambiente, detectou um alvo) e a resposta a este estímulo (instância-se 
        uma RespostaMover(), que irá permitir o agente se deslocar na direção do alvo).

        Args:
            direccao: direção onde se podem encontrar alvos.
        """

        super().__init__(EstimuloAlvo(direccao), RespostaMover(direccao))