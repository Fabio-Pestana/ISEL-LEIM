from agente.controlo_react.reaccoes.resposta.resposta_mover import RespostaMover
from sae import Direccao
from random import choice

class RespostaEvitar(RespostaMover):

    """
    Esta classe herda da classe RespostaMover (especialização de uma resposta a um estímulo
    que permite o movimento do agente numa direção). Esta classe é uma especialização
    de uma resposta que permite o agente ao percepcionar o ambiente consiga
    deslocar-se com o objetivo de evitar obstáculos.
    """

    def __init__(self, dir_inicial=Direccao.ESTE):
        """
        Método construtor de uma Resposta para uma ação mover com o objetivo de
        evitar obstáculos, utilizando o método construtor da classe RespostaMover
        cria-se uma Accao segundo uma direção (dir_inicial), esta instância de Accao
        será guardada na Classe Resposta no atributo protegido _accao, no construtor
        também guarda-se a lista de direções do agente (direções que o agente consegue se
        deslocar no ambiente virtual).

        Args:
            dir_inicial: direção inicial do movimento do agente (por omissão 
            tem o valor de Direccao.ESTE).
        """

        # não é necessário guardar dir_inicial pois ele será a direccao da accao 
        # self.dir_inicial = dir_inicial

        self.__direccoes = list(Direccao) #direções possiveis para o movimento do agente definidas no enumerador Direccao
        super().__init__(dir_inicial)


    def activar(self, percepcao, intensidade): 
        """
        Método que segundo a percepção do agente detecta um obstáculo
        na direção da sua ação atual, caso isso aconteça é atualizada 
        a ação que o agente deve executar. Atualiza-se a sua direccao
        segundo uma direcao livre (sem obstáculos) para assim o agente
        conseguir evitar os obstáculos. No return deste método usa-se
        o super() para assim chamar o método activar da classe Resposta
        para assim atualizar a prioridade da accao segundo a intensidade
        do estimulo.

        Args:
            percepcao: percepção do agente, do ambiente onde-se encontra
            intensidade: intensidade do estímulo.

        Returns:
            accao: ação que o agente deve executar (direccao atualizada)

        Nota:
            Alteração na versão anterior testava-se o contacto com um obstáculo
            segundo a direção inicial (anteriormente um atributo da classe)
            o que iria dar erros pois esta não é a direção certa. Deve se testar
            segundo a direção atual da accao, sendo que esta depois é atualizada para assim permitir
            o agente ao percepcionar obstáculos conseguir evitá-los.
        """
        if percepcao.contacto_obst(self._accao.direccao):
            # errado -> self._accao.direccao = self.__direccao_livre(percepcao)
            # pois a __direccao_livre pode retornar None

            if direccao_livre := self.__direccao_livre(percepcao): # condição com afetação
                self._accao.direccao = direccao_livre
            else:
                return None # Como na arquitetura é indicado que caso não exista um direção livre o método termina (retorna-se None)

        return super().activar(percepcao, intensidade)


    def __direccao_livre(self, percepcao):
        """
        Método que segundo uma percepção do agente do ambiente,
        obtém uma lista das direções livres (sem obstáculos), 
        sendo que caso exista uma ou mais direções retorna-se
        uma aleatoriamente.

        Args:
            percepcao: percepção do agente, do ambiente onde-se encontra

        Returns:
            direccao: uma direção aleatória que seja livre (sem obstáculos)
        """

        direcoes_livres = []
        for dir in self.__direccoes:
            if not percepcao.contacto_obst(dir):
                direcoes_livres.append(dir)

        """
        # Resolução do Docente:
        direccoes_livres = [direccao for direccao in self.__direccoes
        if not percepcao.contacto_obst(direccao)] 
        """

        if direcoes_livres:
            return choice(direcoes_livres)