from ecr.estimulo import Estimulo

class EstimuloObst(Estimulo):
    """
    Esta classe implementa um estímulo que permite o agente, ao 
    percepcionar o ambiente numa certa direção, detectar um obstáculo.
    """

    def __init__(self, direccao, intensidade=1):
        """
        Método construtor de um estímulo que permite o detectação de obstáculos, EstimuloObst.
    
        Args:
            direccao: direção onde se podem encontrar obstáculos.
            intensidade: intensidade do estímulo percepcionado pelo agente (por omissão o seu valor é 1).
        """

        self.__direccao = direccao
        self.__intensidade = intensidade # este valor está relacionado com a distância de um obstáculo.

    def detectar(self, percepcao):
        """
        Este método permite detectar estímulos segundo uma percepção do ambiente
        por parte do agente. Este método possibilida o agente de detectar um obstáculo
        segundo uma direção (atributo privado __direccao), caso ele encontre um obstáculo
        retorna-se a intensidade deste estímulo, se não retorna-se 0.

        Args:
            percepcao: percepção do agente, do ambiente onde-se encontra

        Returns:
            intensidade: intensidade de uma percepção (0 caso o agente não detectar nenhum obstáculo
                numa certa direção).
        """
        
        if percepcao.contacto_obst(self.__direccao):
            return self.__intensidade
        else:
            return 0