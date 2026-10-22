from .comportamento import Comportamento
from .estimulo import Estimulo
from .resposta import Resposta


class Reaccao(Comportamento):
    """
    Esta classe representa uma reação, ou seja uma relação entre um
    estímulo e uma resposta (associação estímulo-resposta). Segundo uma 
    percepção do ambiente feita pelo agente reativo, é ativado um estímulo que 
    irá provocar um resposta que leva a uma acção do mesmo.
    """

    def __init__(self, estimulo, resposta):
        """
        Método construtor de uma Reação

        Args:
            estimulo: estímulo relacionado como uma percepção do agente
            resposta: resposta ao estímulo.
        """
        self.__estimulo = estimulo
        self.__resposta = resposta

    def activar(self, percepcao):
        """
        Método que implementa a associação estímulo-resposta, ou seja
        segundo uma percepção do ambiente obtém-se a intensidade de um certo
        estímulo. Se esta intensidade for maior que zero, significa que foi detectado
        um estímulo, logo ativa-se uma resposta a este estímulo para assim o agente 
        conseguir reagir a este e tomar uma acção. 
        
        Args:
            percepcao: percepção do agente, do ambiente onde-se encontra

        Returns:
            accao: ação que o agente deve executar (resposta ao estímulo detectado)
        """
        
        intensidade = self.__estimulo.detectar(percepcao)

        if intensidade > 0:
            accao = self.__resposta.activar(percepcao, intensidade)
            return accao
        
        #else retorna None