"""
Processamento de erro
@author: Luís Morgado
"""

import sys
from enum import Enum

#_____________________________________________________________

class Erro(Enum):
    """
    Tipos de erro
    """
    AMB_NAO_DEF = "Ambiente não definido"
    CONTROLO_INV = "Argumento <controlo> não deriva da classe Controlo"
    PARAM_INV = "Parâmetros inválidos"

#_____________________________________________________________

def erro_terminar(erro, *param):
    """
    Indicar erro e terminar execução
    @param erro: erro ocorrido
    @param param: parâmetros a apresentar
    """
    texto = "\n*** Simulador de ambiente ***\nErro: %s\n%s\n" % (
        erro.value, 
        '\n'.join(map(str, param))
    )
    print(texto, file=sys.stderr)
    sys.exit()

#_____________________________________________________________

class ErroParam(Exception):
    """
    Excepção de parâmetro inválido
    """
    def __init__(self, erro, param):
        """
        iniciar excepção
        @param erro: erro ocorrido
        @param param: parâmetros a apresentar
        """
        self.__erro = erro
        self.__param = param

    def __str__(self):
        return "%s: %s\n" % (self.__erro.value, self.__param)
