from abc import ABC, abstractmethod

class Avaliador(ABC):
    """
    Classe abstrata que permite avaliar um nó, esta avaliação consiste
    na avaliação da sua prioridade segundo o seu custo, esta avaliação
    depende do mecanismo de procura escolhido.

    Uma função que avalia o custo de uma solução segundo um certo nó, 
    designa-se função f(n). Quando maior for o seu valor, pior será
    a solução.
    """

    @abstractmethod
    def prioridade(self, no):
        """
        Método abstrado que segundo o custo de um nó 
        define a sua prioridade.

        Args:
            no: Nó que será avaliado
        """