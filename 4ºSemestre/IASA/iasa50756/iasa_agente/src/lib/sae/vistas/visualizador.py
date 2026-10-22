"""
Visualizador de ambiente
@author: Luís Morgado
"""

import math
import pygame
import pygame.gfxdraw

#___________________________________________________________

# Definição de cores
BRANCO = (255, 255, 255)
PRETO = (0, 0, 0)
AMARELO = (255, 255, 0)
VERMELHO = (255, 0, 0)
VERDE = (0, 255, 0)
AZUL = (0, 0, 255)
	
# Cores específicas de elementos
COR_AGENTE = (255, 220, 0)
COR_ALVO = (0, 250, 0)
COR_BASE = (0, 250, 0)
COR_OBST = (150, 150, 150)
COR_COLIS = (255, 0, 0)
COR_AGLINHA = (0, 0, 0)

#___________________________________________________________

class Visualizador:
	def __init__(self, dim_x, dim_y, svis_base, rect_base, escala, cor_fundo):
		"""
		Iniciar visualizador
		@param dim_x: dimensão do eixo x
		@param dim_y: dimensão do eixo y
		@param svis_base: superfície de visualização base
		@param rect_base: rectângulo de visualização base
		@param escala: dimensão de cada elemento
		@param cor_fundo: cor de fundo
		"""
		self._svis_base = svis_base
		self._rect_base = rect_base
		self._escala = escala
		self._cor_fundo = cor_fundo
		self._svis = pygame.Surface((dim_x, dim_y))
		self.limpar()
		
	def actualizar(self):
		"""
		Actualizar visualização
		"""
		self._svis_base.blit(self._svis, self._rect_base)
		pygame.display.update(self._rect_base)	
		
	def limpar(self):
		"""
		Limpar visualizador
		"""
		self._svis.fill(self._cor_fundo)
	
	def agente(self, pos, ang=None, col=False, carga=False):
		"""
		Visualizar agente
		@param pos: posição do elemento
		@param ang: ângulo de orientação
		@param col: colisão True/False
		@param carga: carga True/False
		"""
		r = round(0.4 * self._escala)
		margem = 0.1 * self._escala
		x, y = self.pvpix(pos)
		x0 = round(x + r + margem)
		y0 = round(y + r + margem)
		cor = COR_COLIS if col else COR_AGENTE
		pygame.gfxdraw.filled_circle(self._svis, x0, y0, r, cor)
		pygame.gfxdraw.aacircle(self._svis, x0, y0, r, cor)
		if ang is not None:
			dx = r * math.cos(ang)
			dy = -r * math.sin(ang)
			x1 = round(x0 + dx)
			y1 = round(y0 + dy)
			pygame.draw.line(self._svis, COR_AGLINHA, (x0, y0), (x1, y1))
		if carga:
			self.rect(pos, int(0.3 * self._escala), COR_ALVO, 0)
	
	def alvo(self, pos):
		"""
		Visualizar alvo
		@param pos: posição do elemento
		"""
		self.rect(pos, 2, COR_ALVO, 0)
	
	def obstaculo(self, pos):
		"""
		Visualizar obstáculo
		@param pos: posição do elemento
		"""
		self.rect(pos, 0, COR_OBST, 0)
	
	def vazio(self, pos):
		"""
		Visualizar vazio
		@param pos: posição do elemento
		"""
		self.rect(pos, 0, self._cor_fundo, 0)

	def rect(self, pos, margem=0, cor=AMARELO, linha=1):
		"""
		Visualizar rectângulo
		@param pos: posição do ambiente
		@param margem: margem em pixeis
		@param cor: cor RGB
		@param linha: espessura de linha (0 - preencher)
		"""
		x, y = self.pvpix(pos)
		spx = margem
		spy = margem
		rect = pygame.Rect(x + spx, y + spy, self._escala - spx*2, self._escala - spy*2)
		pygame.draw.rect(self._svis, cor, rect, linha)

	def vector(self, pos, mod, ang, cor=(255, 255, 0), linha=1, seta=True):
		"""
		Visualizar vector
		@param pos: posição do elemento
		@param mod: módulo (dimensão do vector)
		@param ang: ângulo de orientação
		@param cor: cor RGB
		@param linha: espessura de linha
		@param seta: seta no final True/False
		"""
		x, y = self.pvpix(pos)
		xi = x + self._escala / 2.0
		yi = y + self._escala / 2.0
		PROP_VECT = 0.5 # Proporção do vector em relação à escala
		dim = PROP_VECT * mod * self._escala
		linhas = self.linhas_vect((xi,yi), dim, ang, 0.17 * math.pi, 0.5)  
		if seta:
			for posini, posfin in linhas:
				pygame.draw.line(self._svis, cor, posini, posfin, linha)
		else:
			posini, posfin = linhas[0]
			pygame.draw.line(self._svis, cor, posini, posfin, linha)

	def marcar(self, posicoes, margem=2, cor=AMARELO, linha=0):
		"""
		Marcar posições
		@param posicoes: conjunto de posições
		@param margem: margem em pixeis
		@param cor: cor RGB
		@param linha: espessura de linha (0 - preencher)
		"""
		for pos in posicoes:
			self.rect(pos, margem, cor, linha)			  
	
	def pvpix(self, pos_virt):
		"""
		Converter posição virtual em pixeis
		@param pos_virt: posição virtual
		@return: posição (x, y) em pixeis
		"""
		xv, yv = pos_virt
		x = round(xv * self._escala)
		y = round(yv * self._escala)
		return x, y

	def inc_pos(self, mod, ang):   
		"""
		Gerar incremento de posição (dx,dy)
		@param mod: módulo da distância de movimentação
		@param ang: ângulo de movimentação
		@return: incremento de posição (dx, dy)
		"""
		dx = mod * math.cos(ang)
		dy = -mod * math.sin(ang)
		return dx, dy

	def linhas_vect(self, pos_i, mod, ang, beta, factor_seta):
		"""
		Gerar linhas de um vector
		@param pos_i: posição inicial
		@param mod: módulo do vector
		@param ang: ângulo do vector
		@param beta: ângulo da seta
		@param factor_seta: factor de dimensão da seta
		"""
		xi, yi = pos_i
		dx, dy = self.inc_pos(mod, ang)
		xf = xi + dx
		yf = yi + dy
		gama = ang + math.pi
		gama1 = gama + beta
		gama2 = gama - beta
		nmod = factor_seta * mod
		dx1, dy1 = self.inc_pos(nmod, gama1)        
		dx2, dy2 = self.inc_pos(nmod, gama2) 
		linha1 = (xi, yi), (xf, yf)
		linha2 = (xf, yf), (xf + dx1, yf + dy1)
		linha3 = (xf, yf), (xf + dx2, yf + dy2)
		return [linha1, linha2, linha3]
