"""
Elemento do ambiente
@author: Luís Morgado
"""

from enum import Enum

#__________________________________________________

class Elemento(Enum):
	"""
	Elemento do ambiente
	"""
	AGENTE 	  = '@'
	ALVO 	  = 'A'
	OBSTACULO = 'O'
	VAZIO 	  = '.'
