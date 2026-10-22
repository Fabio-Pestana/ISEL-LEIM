from abc import abstractmethod
from .comportamento import Comportamento


class ComportComp(Comportamento):
    """
    Esta classe representa um comportamento composto, por isso herda de Comportamento.
    Um comportamento composto é um comportamento que reune várias reações (comportamentos
    simples) e também pode possuir outros comportamentos compostos.
    """

    def __init__(self, comportamentos):
        """
        Método construtor de um Comportamento composto

        Args:
            comportamentos: lista de comportamentos que pertencem ao comportamento composto
        """
        self.__comportamentos = comportamentos

    def activar(self, percepcao):
        """
        Método que ativa a ação do agente segundo o ambiente percepcionado. 
        O estímulo criado pela percepção do ambiente, leva a uma ação 
        por parte do comportamento. Depois dentro de todos os comportamentos 
        existentes no comportamento composto, escolhe-se a ação mais 
        adequada para o agente a executar.
        
        Utilizando um ciclo for-each vai-se percorrer a lista
        de comportamentos existentes no comportamento composto
        para obter-se as acções dos comportamentos segundo uma 
        percepção do ambiente (seu estado atual), é possível 
        um comportamento não ter uma resposta a um estímulo, nesses
        casos a variável accao não vai ter nenhum valor logo não é incluída
        na lista para a seleção da acção.
       
        Args:
            percepcao: percepção do agente, do ambiente onde-se encontra

        Returns:
            accao: ação que o agente reativo deve executar
        """
        accoes = []

        for comportamento in self.__comportamentos:
            accao = comportamento.activar(percepcao)
            if accao:
                accoes.append(accao)


        if accoes:
            return self.seleccionar_accao(accoes)


    @abstractmethod
    def seleccionar_accao(self, accoes):
        """
            Método abstrado que quando implementado deve selecionar uma acção 
            dentro de uma lista de acções, através de um mecanismos de escolha.

            Args:
                accoes: lista de ações dos comportamentos
        """