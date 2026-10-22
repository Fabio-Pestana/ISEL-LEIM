from agente.controlo_react.reaccoes.evitar.estimulo_obst import EstimuloObst
from ecr.reaccao import Reaccao


class EvitarDir(Reaccao):

    """
    Esta classe representa uma reação, ou seja um comportamento simples
    que permite ao agente, caso encontre um obstáculo no ambiente 
    possui uma resposta, sendo esta a sua deslocação numa direção para assim
    evitar o elemento.
    """

    def __init__(self, direccao, resposta):
        """
        Método construtor de um Comportamento simples, EvitarDir, utilizando 
        o método construtor da classe Reaccao guarda-se o estímulo (instância-se 
        um EstimuloObst(), estímulo captado pelo agente ao precepcionar
        o ambiente, detectou um obstáculo) e a resposta a este estímulo.
    
        Args:
            direccao: direção onde se podem encontrar obstáculos.
            resposta: resposta ao estímulo percepcionado pelo agente.
        """

        super().__init__(EstimuloObst(direccao), resposta)