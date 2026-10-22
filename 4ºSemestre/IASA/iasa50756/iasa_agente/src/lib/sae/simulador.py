"""
Simulador de ambiente com visualização gráfica
@author: Luís Morgado
"""

from .defamb import DEF_AMB
from .erro import Erro, erro_terminar
from .agente.controlo import Controlo
from .ambiente.ambiente import Ambiente
from .vistas.vista_simul import VistaSimul
from .modelo.modelo_simul import ModeloSimul
from .controlador.controlador_simul import ControladorSimul

#_____________________________________________________________

# Configuração por omissão
LARGURA = 600
"""Largura do ambiente em pixeis"""
FPS = 20
"""Taxa de actualização da imagem"""

#_____________________________________________________________

class Simulador:
    def __init__(self, num_amb, agente, largura=LARGURA, reiniciar=False):
        """
        Iniciar simulador
        @param num_amb: número do ambiente
        @param controlo: controlo do agente a activar
        @param largura: largura do ambiente em pixeis
        @param reiniciar: reiniciar simulação
        """
        # Iniciar ambiente
        ambiente = self.__iniciar_ambiente(num_amb)
        # Escala de visualização (dimensão em pixeis de cada posição)
        escala = round(largura / ambiente.dim_x)
        # Iniciar modelo de simulação
        self.__modelo = ModeloSimul(ambiente, agente, reiniciar)
        # Iniciar vista de simulação
        self.__vista = VistaSimul(self.__modelo, escala)
        # Iniciar controlo de simulação
        self.__controlador = ControladorSimul(self.__vista, self.__modelo, escala, FPS)
        # Iniciar controlo do agente
        self.__inciar_controlo(agente.controlo)
        # Iniciar transdutor do agente
        agente.transdutor.iniciar(ambiente)

    def __iniciar_ambiente(self, num_amb):
        """
        Obter definição de ambiente
        @param num_amb: número do ambiente
        @return: ambiente
        """
        if num_amb in DEF_AMB:
            return Ambiente(DEF_AMB[num_amb])
        else:
            erro_terminar(Erro.AMB_NAO_DEF, num_amb)

    def __inciar_controlo(self, controlo):
        """
        Verificar controlo, definir vista de informação
        do controlo do agente e modo de visualização de
        percepção direccional
        """
        if issubclass(type(controlo), Controlo):
            controlo.definir_vista(self.__vista.vista_mod)
        else:
            erro_terminar(Erro.CONTROLO_INV)

    def executar(self):
        """
        Executar simulação
        """
        # Activar processamento do controlador de simulação
        self.__controlador.processar()


        
