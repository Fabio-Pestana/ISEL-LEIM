from ecr.comportamento import Comportamento
from sae import Direccao
from ..resposta.resposta_mover import RespostaMover

class ExplorarRodar(Comportamento):
    """
    Classe que representa um comportamento fixo (explorar rodando segundo 
    os ponteiros do relógio) que consiste no movimento do agente 
    segundo a direccao da percepção anterior.
    """

    def activar(self, percepcao):
        """
        Método que permite o agente explorar no ambiente, rodando segundo os ponteiros
        do relógio, para permitir isto cria-se um dicionário indicando a futura
        direção do agente, demonstrando assim que este comportamento tem uma
        sequência de activação fixa.

        Args:
            percepcao: percepção do agente, do ambiente onde-se encontra

        Returns:
            accao: ação que o agente vai realizar. Representa o seu movimento
            segundo uma direção.
        """

        direcao = percepcao.direccao

        direccoes_rodar = {
            Direccao.NORTE : Direccao.ESTE,
            Direccao.ESTE : Direccao.SUL,
            Direccao.SUL : Direccao.OESTE,
            Direccao.OESTE : Direccao.NORTE
        }


        resposta = RespostaMover(direccoes_rodar[direcao])
        return resposta.activar(percepcao)