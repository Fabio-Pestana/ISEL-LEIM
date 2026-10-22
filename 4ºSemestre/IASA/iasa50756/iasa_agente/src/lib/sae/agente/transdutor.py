"""
Transdutor de percepção e actuação
@author: Luís Morgado
"""

from ..ambiente.elemento import Elemento
from .percepcao import Percepcao

#_______________________________________________________________________________

class Transdutor:
    """
    Transdutor de percepção e actuação
    """
    def iniciar(self, ambiente):
        self.__ambiente = ambiente

    def percepcionar(self):
        """
        Percepcionar ambiente
        @return: percepção
        """
        items_ambiente = self.__ambiente.elementos.items()

        posicoes = [pos for pos, elem in items_ambiente
                    if elem != Elemento.OBSTACULO]

        elementos = {pos: elem for pos, elem in items_ambiente
                     if elem in [Elemento.ALVO, Elemento.OBSTACULO]}

        return Percepcao(
					self.__ambiente.per_dir,
					self.__ambiente.posicao_agente,
					self.__ambiente.direccao_agente,
					posicoes,
					elementos,
					self.__ambiente.recolha,
					self.__ambiente.colisao
				)

    def actuar(self, accao):
        """
        Activar actuador com acção
        @param accao: acção a executar
        """
        if accao:
            self.__ambiente.mover_agente(accao.direccao)
