"""
Controlador MVC de simulação
@author: Luís Morgado
"""

import pygame

#_____________________________________________________________
# Teclas de eventos

TERMINAR = 't'
"""Terminar simulação"""
INICIAR = 'i'
"""Iniciar ambiente"""
PAUSA = 'p'
"""Activar modo de pausa (execução passo-a-passo)"""
EXECUTAR = 'e'
"""Executar passo"""
VELOCIDADE = 'v'
"""Comutar velocidade (máxima/normal)"""

#_____________________________________________________________

class ControladorSimul:
	"""
	Controlador de simulação
	"""
	def __init__(self, vista, modelo, escala, fps):
		"""
		Iniciar controlador de simulação
		@param modelo_simul: modelo de simulação
		@param escala: dimensão dos elementos do ambiente
		@param fps: taxa de actualização da imagem em 'Frames Per Second
		"""
		self.__vista = vista
		self.__modelo = modelo
		self.__fps = fps		
		# Iniciar pygame
		pygame.init()
		self.__relogio = pygame.time.Clock()
		# Iniciar configuração de estado
		self.finalizar = False
		"""Finalizar simulação"""
		self.pausa = False
		"""Pausa de execução"""
		self.passo = False
		"""Modo passo-a-passo"""
		self.vmax = False
		"""Velocidade máxima"""
 
	def __processar_eventos(self):
		"""
		Processar eventos de controlo da simulação
		"""
		self.reinicio = False
		for evento in pygame.event.get():
			if evento.type == pygame.QUIT:
				self.finalizar = True
			elif evento.type == pygame.KEYDOWN:
				key = evento.unicode.lower()
				if key == TERMINAR:
					self.finalizar = True
				elif key == INICIAR:
					self.__modelo.iniciar()
				elif key == PAUSA:
					self.pausa = not self.pausa
				elif key == EXECUTAR:
					self.passo = True
				elif key == VELOCIDADE:
					self.vmax = not self.vmax
		
	def __actualizar_vista(self):
		"""
		Actualizar vista de simulação de acordo
		com a taxa de actualização (FPS)
		"""
		self.__vista.actualizar()
		if not self.vmax:
			self.__relogio.tick(self.__fps)

	def processar(self):
		"""
		Processar eventos
		"""
		# Ciclo de simulação
		while not self.finalizar:
			# Processar eventos
			self.__processar_eventos()
			# Executar passo de simulação de acordo
			# com o modo de execução (pausa sim/não)
			if not self.pausa or self.passo:
				self.__modelo.executar_passo()
				self.passo = False
			# Actualizar visualização
			self.__actualizar_vista()		
		# Terminar simulação
		self.__terminar()

	def __terminar(self):
		"""
		Terminar simulação
		"""
		pygame.quit()

		
