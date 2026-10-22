from abc import ABC, abstractmethod

class ModeloPlan(ABC):
    """
    Classe abstrata que representa um modelo de planeamento de um agente deliberativo.
    Estes modelos quando implementados irão criar representações internas do ambiente, 
    onde irão guardar informações sobre o mundo, como o estado atual do agente, os 
    vários estados possíveis do mundo e os vários operadores que permitem a transição
    entre estados.
    """
    
    @abstractmethod
    def obter_estado(self):
        """
        Método abstrato que irá permitir obter o estado atual do agente.
        
        Returns:
            estado: estado atual do agente.
        """

    @abstractmethod
    def obter_estados(self):
        """
        Método abstrato que irá permitir obter a lista de todas as configurações 
        possiveis do mundo.
        
        Returns:
            estados: estados possiveis no modelo do mundo.
        """
    
    @abstractmethod
    def obter_operadores(self):
        """
        Método abstrato que irá permitir obter a lista dos operadores
        
        Returns:
            operadores: operadores do modelo do mundo
        """