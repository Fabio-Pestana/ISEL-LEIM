from agente.controlo_react.reaccoes.evitar.evitar_dir import EvitarDir
from agente.controlo_react.reaccoes.evitar.resposta_evitar import RespostaEvitar
from ecr.hierarquia import Hierarquia
from sae import Direccao

class EvitarObst(Hierarquia):

    """
    Esta classe herda da classe Hierarquia, esta classe instância 
    comportamentos com memória, EvitarDir um para cada direção que o agente pode se movimentar,
    ou seja esta classe representa um comportamento composto constituido por sub-comportamentos 
    específicos para assim o agente conseguir evitar obstáculos através de uma das direções 
    (NORTE, SUL, ESTE, OESTE, direções do enumerador Direccao). 
    """

    def __init__(self):
        """
        Método construtor de um Comportamento composto, utilizando 
        o método construtor da classe abstrata ComportComp guarda-se
        a lista de comportamentos (instâncias de EvitarDir para cada uma das
        direções existentes na classe Direccao - NORTE, SUL, ESTE, OESTE) que 
        fazem parte do comportamento composto EvitarObst.

        Neste construtor também se instância uma RespostaEvitar (atributo privado __resposta),
        uma resposta a um estímulo que permite o agente evitar obstáculos, sendo que este 
        atributo é passado a cada comportamento EvitarDir(), para assim o agente conseguir
        evitar obstáculos em todas as direções.
        """

        self.__resposta = RespostaEvitar()
        super().__init__(
            [EvitarDir(direccao, self.__resposta) for direccao in Direccao]) 
        
        # Lista de comportamentos gerados segundo o enumerador Direccao