from sae import Elemento


class MecDelib():
    """
    Esta classe representa um dos componentes do raciocínio prático, sendo este o Raciocínio
    sobre Fins (Deliberação). Este mecanismo deliberativo consiste em analisar o modelo do mundo e
    determinar segundo os elementos percecionados pelo agente, quais destes devem ser considerados
    objetivos (no nosso agente estes objetivos a realizar são os alvos que o agente tem de recolher).
    """
    def __init__(self, modelo_mundo):
        """
        Método construtor da classe MecDelib, mecanismo deliberativo.

        Args:
            modelo_mundo: representação interna do ambiente
        """
        self.__modelo_mundo = modelo_mundo
    

    def deliberar(self):
        """
        Método responsável por decidir quais dos elementos existentes no modelo do
        mundo, são os objetivos do agente. 

        Inicialmente obtemos os vários estados possíveis do modelo do mundo, para 
        depois percorrermos a lista destes, e testar se o elemento nessa posição do mundo
        é do tipo alvo, se sim guardamos o estado com esse elemento na lista dos objetivos
        para assim guardar-se a posição dos vários alvos.

        Returns:
            objetivos: estados que representam os objetivos do agente.
        """
        estados = self.__modelo_mundo.obter_estados()

        objetivos = []

        for estado in estados:
            elemento = self.__modelo_mundo.obter_elemento(estado)
            # Nota -> não é Alvo mas sim ALVO
            if elemento == Elemento.ALVO:
                objetivos.append(estado)

        if objetivos:
            objetivos.sort(key=self.__modelo_mundo.distancia)  
            # por cada estado, avalia-se a distancia destes ao estado atual do agente,
            # para assim ordená-los segundo essa distância.
            return objetivos