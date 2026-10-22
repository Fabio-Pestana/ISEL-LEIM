"""
Direcção de movimentação no ambiente
@author: Luís Morgado
"""

import math
from enum import Enum

#__________________________________________________

class Direccao(Enum):
	"""
	Direcção de movimento
	"""
	NORTE = math.pi / 2
	SUL   = -math.pi / 2
	ESTE  = 0
	OESTE = math.pi
