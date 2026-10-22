"""
Vista de ambiente
@author: Luís Morgado
"""

from ..erro import Erro, ErroParam
from ..ambiente.ambiente import Elemento
from .visualizador import AMARELO, Visualizador

#_____________________________________________________________

class VistaAmb(Visualizador):
    def mostrar_elemento(self, posicao, elemento):
        """
        Mostrar elemento numa posição excepto agente
        @param posicao: posição do elemento
        @param elemento: elemento a mostrar
        """
        try:
            if elemento == Elemento.ALVO:
                self.alvo(posicao)
            elif elemento == Elemento.OBSTACULO:
                self.obstaculo(posicao)
            elif elemento == Elemento.VAZIO:
                self.vazio(posicao)  
        except:
            raise ErroParam(Erro.PARAM_INV, [posicao, elemento])
             
    def mostrar_valor_posicao(self, posicao, valor, vmin=-2, vmax=1000):
        """
        Mostrar posição com cor correspondente ao valor
        @param posicao: posição do ambiente
        @param valor: valor a mostrar
        @param vmin: valor mínimo
        @param vmax: valor máximo
        """
        try:
            # Definir cor (R,G,B) com normalização de valor
            r, g, b = 0, 0, 0
            if valor > 0:
                g = min(valor / vmax, 1) * 255
            elif valor < 0: 
                r = min(valor / vmin, 1) * 255
            cor = (r, g, b)
            # Mostrar posição com cor respectiva
            self.rect(posicao, 0, cor, 0)
        except:
            raise ErroParam(Erro.PARAM_INV, [posicao, valor, vmin, vmax])
            
    def marcar_posicao(self, posicao, margem=2, cor=AMARELO, linha=1):
        """
        Marcar posição
        @param posicao: posição a marcar
        @param margem: margem em pixeis
        @param cor: cor RGB
        @param linha: espessura de linha (0 - preencher)
        """
        try:
            self.marcar([posicao], margem, cor, linha)
        except:
            raise ErroParam(Erro.PARAM_INV, [posicao, margem, cor, linha])
        
    def mostrar_vector(self, posicao, angulo):
        """
        Visualizar vector
        @param posicao: posição inicial do vector
        @param angulo: ângulo de orientação
        """
        try:
            super().vector(posicao, 1, angulo)
        except:
            raise ErroParam(Erro.PARAM_INV, [posicao, angulo])
        
            
