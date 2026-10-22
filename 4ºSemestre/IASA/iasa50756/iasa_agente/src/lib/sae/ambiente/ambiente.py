"""
Simulação de ambiente
@author: Luís Morgado
"""

import math

from .direccao import Direccao
from .elemento import Elemento

#__________________________________________________

class Ambiente:
	"""
	Simulação do ambiente
	"""	
	def __init__(self, def_amb):
		"""
		Criar ambiente
		@param def_amb: definição do ambiente
		"""
		self.__def_amb = def_amb
		self._elementos = None
		self._posicao_agente = None
		self._direccao_agente = None
		self._dim_x = None
		self._dim_y = None
		self._dist_max = None
		self._colisao = False
		self._recolha = False
		self._per_dir = None
		self.iniciar()
		
	@property
	def elementos(self):
		"""
		Obter dicionário <pos, elem> com todos os 
		elementos do ambiente
		"""
		return self._elementos
		
	@property
	def posicao_agente(self):
		"""
		Obter posição do agente
		"""
		return self._posicao_agente
		
	@property
	def direccao_agente(self):
		"""
		Obter posição do agente
		"""
		return self._direccao_agente
		
	@property
	def colisao(self):
		"""
		Obter informação de colisão
		após último movimento
		"""
		return self._colisao
		
	@property
	def recolha(self):
		"""
		Obter informação de recolha de alvo
		após último movimento
		"""
		return self._recolha
		
	@property
	def dim_x(self):
		"""
		Obter dimensão X ambiente
		"""
		return self._dim_x
		
	@property
	def dim_y(self):
		"""
		Obter dimensão Y ambiente
		"""
		return self._dim_y
		
	@property
	def per_dir(self):
		"""
		Obter última percepção direccional
		"""
		return self._per_dir

	def iniciar(self):
		"""
		Iniciar elementos do ambiente
		"""
		self._elementos = {}
		y = 0
		for linha in self.__def_amb:
			x = 0
			for codigo_elem in linha:
				elem = Elemento(codigo_elem)
				posicao = (x, y)
				self._elementos[posicao] = elem
				if elem == Elemento.AGENTE:
					self._posicao_agente = posicao
					self._direccao_agente = Direccao.ESTE
					self._colisao = False
				x += 1
			y += 1
		# Definir dimensões do ambiente
		self._dim_x = len(self.__def_amb[0])
		self._dim_y = y
		self._dist_max = max(self._dim_x, self._dim_y)
		# Actualizar percepção direccional
		self.detectar_dir()
			   
	def posicoes(self, tipo=None):
		"""
		Obter posições dos elementos do ambiente
		@param tipo: tipo dos elementos a obter posição
		"""
		return [elem.posicao
				for elem in self._elementos
				if tipo is None or elem == tipo]
			   
	def elemento(self, posicao):
		"""
		Obter elemento de uma posição
		Qualquer posição não definida é considerada como obstáculo
		@param posicao: posição a observar
		"""
		return self._elementos.get(posicao, Elemento.OBSTACULO)
			
	def mover_agente(self, direccao):
		"""
		Mover agente com com detecção de colisão
		e recolha automática de alvo		
		@param direccao: direccão do movimento
		"""
		self._direccao_agente = direccao
		nova_posicao = self.mover(self._posicao_agente, direccao)
		self._colisao = self.verificar_colisao(nova_posicao)
		if not self._colisao:
			self._recolha = self.verificar_recolha(nova_posicao)
			self._elementos[self._posicao_agente] = Elemento.VAZIO
			self._elementos[nova_posicao] = Elemento.AGENTE
			self._posicao_agente = nova_posicao
		else:
			self._recolha = False
		# Actualizar percepção direccional
		self.detectar_dir()
			
	def mover(self, posicao, direccao):
		"""
		Mover a partir de uma posição numa direcção		
		@param posicao: posição inicial
		@param direccao: direccão do movimento
		@return: nova posição
		"""
		x, y = posicao
		dx = math.cos(direccao.value)
		dy = -math.sin(direccao.value)
		return x + round(dx), y + round(dy)
							   
	def verificar_colisao(self, posicao):
		"""
		Verificar colisão com obstáculo
		@param posicao: posição a testar
		"""
		return self.elemento(posicao) == Elemento.OBSTACULO
							   
	def verificar_recolha(self, posicao):
		"""
		Verificar recolha de alvo
		@param posicao: posição a testar
		"""
		return self.elemento(posicao) == Elemento.ALVO
					
	def detectar(self, direccao):
		"""
		Detectar elemento numa direcção de movimento
		a partir da posição do agente
		@param direccao: direccão da detecção
		@return: elemento, distância
		"""
		posicao = self._posicao_agente
		distancia = 0
		while distancia <= self._dist_max:
			nova_posicao = self.mover(posicao, direccao)			 
			distancia += round(math.dist(posicao, nova_posicao))
			posicao = nova_posicao
			elemento = self.elemento(posicao)
			if elemento and elemento != Elemento.VAZIO:
				return elemento, distancia, posicao
						
	def detectar_dir(self):
		"""
		Detectar elementos nas várias direcções
		a partir da posição do agente
		@return: associações direcção: (elemento, distância)
			     para as várias direcções
		"""
		self._per_dir = {
			direccao: self.detectar(direccao)
			for direccao in Direccao
		}
		return self._per_dir

