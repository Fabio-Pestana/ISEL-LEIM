"""
Agente de simulação
@author: Luís Morgado
"""

from .transdutor import Transdutor

#_______________________________________________________________________________

class Agente:
    """
    Agente base para simulação
    """
    def __init__(self, controlo):
        self._controlo = controlo
        self.__transdutor = Transdutor()

    @property
    def controlo(self):
        return self._controlo

    @property
    def transdutor(self):
        return self.__transdutor

    def _actuar(self, accao):
        self.__transdutor.actuar(accao)

    def _percepcionar(self):
        return self.__transdutor.percepcionar()
    
    def executar(self):
        """
        Executar passo de processamento
        """
        percepcao = self._percepcionar()
        accao = self._controlo.processar(percepcao)
        self._actuar(accao)