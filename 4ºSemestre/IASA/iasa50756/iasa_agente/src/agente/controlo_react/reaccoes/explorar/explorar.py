from ecr.comportamento import Comportamento
from ..resposta.resposta_mover_aleatoria import RespostaMoverAleat

class Explorar(Comportamento):
    """
    Classe que representa um comportamento fixo (explorar) que consiste no 
    movimento do agente de uma forma aleatória.

    Um comportamento fixo é uma comportamento que não tem estímulo, mas tem uma
    resposta fixa, ou seja o agente não precisa de percepcionar o ambiente para 
    executar esta ação.
    """

    def activar(self, percepcao):
        """
        Método que ativa a ação do comportamento (deslocar numa direção aleatória), 
        como este é um comportamento fixo, estes comportamentos não precisam de 
        percepcionar o ambiente para executar ações (por isso, não se passa um segundo
        argumento no método ativar da resposta, intensidade do estímulo = 0)

        Args:
            percepcao: percepção do agente, do ambiente onde-se encontra

        Returns:
            accao: ação que o agente vai realizar. Representa o seu movimento
            segundo uma direção aleatória.
        """
        resposta = RespostaMoverAleat()
        return resposta.activar(percepcao)