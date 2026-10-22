from abc import ABC, abstractmethod


class Problema(ABC):
    """
    Classe abstrata que representa um Problema. Um problema é um dos 
    conceitos principais de um Raciocínio Automático, sendo que este 
    conceito é constituído por:

    Estado Inicial -> estado inicial do agente (ponto de partida do raciocinio), 
    representa uma configuração de um problema sendo esta a percepção atual do agente.
    Operadores -> Ações que se podem executar que permitem a alteração do estado.
    Objetivo -> Estado que representa o objetivo do agente.

    Para que seja possível Raciocínio Automático é necessário conhecimento de um certo 
    domínio. Raciocínio é uma simulação sobre o problema apresentado. 
    """

    def __init__(self, estado_inicial, operadores):
        """
        Método construtor de um Problema

        Args:
            estado_inicial: Estado inicial do agente
            operadores: ações que podem ser executadas que alteram o estado do problema.
        """
        self.__estado_inicial = estado_inicial
        self.__operadores = operadores

    @abstractmethod
    def objectivo(self, estado):
        """
        Método abstrado que compara o estado que recebe como 
        parametro com o objetivo do problema, para assim saber-se
        se o objetivo já foi ou não atingido.

        Args:
            estado: estado que será comparado com o estado que representa 
            o objetivo do problema.
        """


    # @property funciona com um get em Java, permitindo assim
    # atributos "read-only"

    @property
    def estado_inicial(self):
        """
        Propriedade que permite fazer uma leitura do estado inicial do problema.
        """
        return self.__estado_inicial
    
    @property
    def operadores(self):
        """
        Propriedade que permite fazer uma leitura dos operadores do problema.
        """
        return self.__operadores

