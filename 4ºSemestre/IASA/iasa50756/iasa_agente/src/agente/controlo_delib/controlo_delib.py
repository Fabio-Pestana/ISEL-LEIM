from agente.controlo_delib.mec_delib import MecDelib
from agente.controlo_delib.modelo.modelo_mundo import ModeloMundo
from sae import Controlo

class ControloDelib(Controlo):
    """
    Esta classe herda da classe abstrata Controlo, sendo que representa
    um controlo deliberativo de um agente.
    Estes agentes tomam decisões segundo uma representação interna do ambiente,
    o modelo do mundo, e segundo os seus objetivos. Estes objetivos e o plano como
    estes vão ser concretizados são definidos segundo um raciocínio orientado para ações,
    também designado por raciocinio prático.
    Este raciocinio é composto por duas componentes:
    -Raciocínio sobre fins, designado por deliberação, que segundo a perceção do agente
    tem de analisar os elementos que se encontram no ambiente/mundo e escolher aqueles que
    representam os objetivos a atingir.
    -Raciocínio sobre meios, designado por planeamento, que segundo os vários objetivos tem de
    planear um plano de ações para assim conseguir atingir o resultado esperado.
    """

    def __init__(self, planeador):
        """
        Método construtor de um Controlo deliberativo de um agente. Onde se
        instância o modelo do mundo e um mecanismo deliberativo. Pois este
        controlo necessita de uma representação do mundo para compreender e interagir
        com ele.

        Args:
            planeador: planeador de um agente 
        """
        self.__planeador = planeador
        self.__plano = None
        self.__objectivos = None
        self.__modelo_mundo = ModeloMundo()
        self.__mec_delib = MecDelib(self.__modelo_mundo)

    def processar(self, percepcao):
        """
        Método que processa as perceções do agente e decide qual ação tomar.
        Este método segue o processo de tomada de decisão e ação, onde inicialmente
        recebe como parâmetro o que o agente observa no ambiente, atualiza o modelo
        do mundo, segundo essa perceção, e caso haja uma alteração no modelo do mundo,
        gera-se um conjunto de objetivos (deliberação) e gera-se um plano de ação (planeamento).
        Por fim executa-se o plano de ação, sendo que o agente irá executar a ação planeada pelo
        raciocinio.

        Args:
            percepcao: perceção atual do agente segundo o ambiente

        Returns:
            accao: A ação que o agente deve executar.
        """
        self.__assimilar(percepcao)
        if self.__reconsiderar():
            self.__deliberar()
            self.__planear()

        # Ação planeada pelo raciocinio prático que será executada pelo agente
        self.__mostrar()
        return self.__executar()


    def __assimilar(self, percepcao):
        """
        Método privado que atualiza o modelo do mundo com a perceção do agente.

        Args:
            percepcao: perceção atual do agente segundo o ambiente
        """
        self.__modelo_mundo.actualizar(percepcao)


    def __reconsiderar(self):
        """
        Método privado que verifica se devemos reconsiderar o plano de ações.

        Returns:
            true: se nenhum plano estiver definido ou se o modelo do mundo foi alterado.
            false: caso contrário.
        """
        return not self.__plano or self.__modelo_mundo.alterado

        
    def __deliberar(self):
        """
        Método privado que atualiza a lista de objetivos do agente.
        """
        self.__objectivos = self.__mec_delib.deliberar()

    def __planear(self):
        """
        Método privado que planea as ações e atualiza o plano de ação.
        """
        self.__plano = self.__planeador.planear(self.__modelo_mundo, self.__objectivos)

    def __executar(self):
        """
        Método privado que se houver um plano definido, ele obtém o operador de um passo da
        solução segundo o estado atual do agente. Caso o plano e o agente estejam assincrono
        (operador retornado pelo método obter_accao do plano é None), coloca-se o plano a
        None, pois este está desatualizado em relação ao agente.

        Returns:
            accao: ação que o agente vai executar, se o plano estiver definido.
            None caso contrário.
        """
        if self.__plano:
            operador = self.__plano.obter_accao(self.__modelo_mundo.obter_estado())
            if operador:
                return operador.accao
            else:
                self.__plano = None

    def __mostrar(self):
        """
        """
        self.vista.limpar()
        self.__modelo_mundo.mostrar(self.vista)
        if self.__plano:
            self.__plano.mostrar(self.vista)
        if self.__objectivos:
            for objetivo in self.__objectivos:
                self.vista.marcar_posicao(objetivo.posicao)