from ecr.estimulo import Estimulo
from sae import Elemento


class EstimuloAlvo(Estimulo):
    """
    Esta classe representa um estímulo que permite o agente, ao 
    percepcionar o ambiente numa certa direção, detectar um alvo.
    """
    
    def __init__(self, direccao, gama=0.9):
        """
        Método construtor de um estímulo que permite o detectação de alvo, EstimuloAlvo.
    
        Args:
            direccao: direção onde se podem encontrar alvos.
            gama: atributo que permite quantificar a intensidade de um estímulo
            (por omissão o seu valor é 0.9).
        """
        self.__direccao = direccao
        self.__gama = gama # base da potência de uma função 

    def detectar(self, percepcao):
        """
        Este método permite detectar estímulos segundo uma percepção do ambiente
        por parte do agente. Este método possibilida o agente de detectar um alvo
        segundo uma direção (atributo privado __direccao), caso ele encontre um alvo
        quanto maior for a sua distância, menor vai ser a intensidade do estímulo
        (faz-se o produto entre a distância do alvo e o valor do atributo privado __gama
        o seu resultado, a intensidade do estímulo, é um valor inversamente proporcional à
        distância) se não encontrar-se nenhum alvo a intensidade do estímulo é 0.

        Args:
            percepcao: percepção do agente, do ambiente onde-se encontra

        Returns:
            intensidade: intensidade de um estímulo (0 caso o agente não detectar nenhum alvo
                numa certa direção).
        """
         
        elemento, distancia, _ = percepcao[self.__direccao] # chama-se o método __getitem__ da percepcao

        if elemento == Elemento.ALVO:
            intensidade = self.__gama ** distancia # quantificação da intensidade do estímulo
        else:
            intensidade = 0

        return intensidade