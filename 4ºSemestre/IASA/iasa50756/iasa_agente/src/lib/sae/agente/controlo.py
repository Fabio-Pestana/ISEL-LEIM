"""
Controlo de agente
@author: Luís Morgado
"""

from abc import ABC, abstractmethod

from sae.vistas.vista_amb import VistaAmb

#_______________________________________________________________________________

class Controlo(ABC):
    """
    Controlo de agente
    """
    __vista = None
    """Vista associada"""
    __mostrar_per_dir = False
    """Mostrar percepção direccional True/False"""

    @property
    def vista(self):
        return self.__vista

    @property
    def mostrar_per_dir(self):
        return self.__mostrar_per_dir

    @mostrar_per_dir.setter
    def mostrar_per_dir(self, valor):
        self.__mostrar_per_dir = valor

    def definir_vista(self, vista):
        """
        Definir vista de informação do controlo
        """
        self.__vista = vista

    @abstractmethod
    def processar(self, percepcao):
        """
        Processar percepção
        @param percepcao: percepção a processar
        @return: acção a realizar
        """
        