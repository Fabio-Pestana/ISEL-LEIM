"""
Acção do agente
@author: Luís Morgado
"""

from dataclasses import dataclass

from ..ambiente.direccao import Direccao

#_______________________________________________________________________________

@dataclass
class Accao:
    """Representação de acção"""
    direccao: Direccao
    """Direcção de movimento"""
    passo: int = 1
    """Distância de movimento"""
    prioridade: int = 0
    """Prioridade da acção"""

    @property
    def ang(self):
        """
        Ângulo da direcção da acção
        """
        return self.direccao.value

    def __hash__(self):
        """
        Identificação por valor
        """
        return hash(self.direccao)    
  
    def __repr__(self):
        """
        Representação de acção
        """
        return "Accao(%s)" % self.direccao
