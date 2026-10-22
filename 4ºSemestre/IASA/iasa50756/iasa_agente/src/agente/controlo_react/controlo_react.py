from sae import Controlo
from ecr.comportamento import Comportamento

class ControloReact(Controlo):
    """
    Esta classe representa um controlo reativo de um agente.
    """

    def __init__(self, comportamento):
        """
        Método construtor de um Controlo reativo de um agente.

        Args:
            comportamento: comportamento geral do agente segundo a sua
              percepção do ambiente
        """
        self.__comportamento = comportamento
        self.mostrar_per_dir = True  # visão sensorial do agente (painel da direita)


    def processar(self, percepcao):
        """
        Método que utiliza o método activar do comportamento para assim
        processar o ambiente (através da percepção do agente) e obter a resposta
        do comportamento a este estímulo (obtém-se a ação que o agente deve
        executar).
       
        Args:
            percepcao: percepção do agente, do ambiente onde-se encontra

        Returns:
            accao: ação do comportamento
        """
        return self.__comportamento.activar(percepcao)