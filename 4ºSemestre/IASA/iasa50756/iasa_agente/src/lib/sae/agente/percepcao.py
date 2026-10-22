"""
Acção do agente
@author: Luís Morgado
"""

from dataclasses import dataclass

from ..ambiente.posicao import Posicao
from ..ambiente.direccao import Direccao
from ..ambiente.elemento import Elemento
from .per_dir import PerDir

#_______________________________________________________________________________

@dataclass
class Percepcao:
    """Registo de informação sensorial"""
    per_dir: dict[Direccao, PerDir]
    """Percepção direccional, direcção: (elemento, distância, posição)"""
    posicao: Posicao
    """Posição do agente"""
    direccao: Direccao
    """Direcção do agente"""
    posicoes: list[Posicao]
    """Posições do ambiente"""
    elementos: dict[Posicao, Elemento]
    """Elementos do ambiente"""
    recolha: bool = False
    """Ocorreu uma recolha de alvo"""
    colisao: bool = False
    """Ocorreu colisão com obstáculo"""
        
    def __getitem__(self, direccao):
        """
        Acesso indexado à percepção direccional
        @param direccao: direcção de percepção
        @return: elemento, distância, posição
        """
        return self.per_dir.get(direccao)

    def contacto_obst(self, direccao):
        """
        Obter informação de contacto com obstáculo
        @param direccao: direcção de percepção
        @return: contacto com obstáculo True/False
        """
        elemento, distancia, _ = self.per_dir[direccao]
        return elemento == Elemento.OBSTACULO and distancia == 1 
