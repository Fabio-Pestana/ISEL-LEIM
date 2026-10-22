from pdm.modelo.modelo_pdm import ModeloPDM
from plan.modelo.modelo_plan import ModeloPlan

class ModeloPDMPlan(ModeloPDM, ModeloPlan):
    """
    Esta classe herda da classe abstrata ModeloPDM e ModeloPlan, sendo que representa
    um modelo de um problema que pode ser resolvido através de um processo
    de decisão de Markov.

    Utiliza-se fatorização sem herança para assim delegar ao modelo do plano, 
    utilizando os seus métodos, para se obter o estado atual do agente, os 
    estados do mundo e seus operadores.
    """

    def __init__(self, modelo_plan, objectivos, rmax=1000):
        self.__modelo_plan = modelo_plan
        self.__objectivos = objectivos
        self.__rmax = rmax # recompensa máxima

        # Pré calcula-se as transições, isto é utilizado quando o modelo
        # do mundo o permite, que no nosso caso permite. Isto irá melhorar
        # o desempenho do mesmo, pois assim no raciocinio do nosso agente não 
        # é necessário calcularmos as transições. Como o nosso modelo é determinista
        # consegue-se saber o próximo estado de um agente depois da aplicação de 
        # uma ação, logo neste dicionário __transicoes associa-se apenas estados a operadores e 
        # não a probabilidades.

        self.__transicoes = {
            (s, a): a.aplicar(s)
            for s in self.obter_estados()
            for a in self.obter_operadores()
        }

    def obter_estado(self):
        """
        Método que permite obter o estado atual do agente.

        Returns:
            estado: estado atual do agente.
        """
        return self.__modelo_plan.obter_estado()

    def obter_estados(self):
        """
        Método que permite obter a lista de todas as configurações 
        possiveis do mundo.

        Returns:
            estados: estados possiveis no modelo do mundo.
        """
        return self.__modelo_plan.obter_estados()


    def obter_operadores(self):
        """
        Método que permite obter a lista dos operadores

        Returns:
            operadores: operadores do modelo do mundo
        """
        return self.__modelo_plan.obter_operadores()


    def S(self):
        """
        Método abstrato que permite obter uma lista de estados do mundo.

        Returns:
            estados: lista de estados do mundo.
        """
        return self.obter_estados()

    def A(self, s):
        """
        Método abstrato que permite obter uma lista de operadores
        possíveis segundo um estado.

        Args:
            s: estado para o qual queremos saber os seus operadores 

        Returns:
            operadoes: lista de operadores do mundo
        """
        return self.obter_operadores() if s not in self.__objectivos else []

    def T(self, s, a, sn):
        """
        Método abstrato que irá permitir obter a probabilidade
        de transição de um estado s para um estado sn, através 
        do operador a.

        Args:
            s: estado
            a: operador para qual se testa a probabilidade de transição entre os estados.
            sn: outro estado, que se deseja saber a probabilidade da transição de s para este
            através da aplicação do operador a.
        
        Returns:
            probabilidade: probabilidade da transição.

        Como este modelo é determinista, caso haja estados sucessores para
        o estado recebido, retorna-se 1 (pois neste modelo é possivel prever
        um estado segundo o operador aplicado sobre ele).
        Como no construtor, pré calcula-se as transições entre estados, caso 
        o tuplo do estado e o operador retorne um estado igual ao estado sucessor
        retorna-se 1, se não 0.
        """
        #return 1 if self.suc(s, a) else 0

        return 1 if self.__transicoes.get((s, a)) == sn else 0

    def R(self, s, a, sn):
        """
        Método abstrato que irá permitir obter a recompensa da transição
        do estado s para o estado sucessor sn, através do operador a.

        Args:
            s: estado
            a: operador a aplicar ao estado s.
            sn: estado sucessor, obtido através da aplicação de um operador a 
            ao estado s.

        Returns:
            recompensa: recompensa da transição.
        """
        return self.__rmax if sn in self.__objectivos else -a.custo(s, sn)

    def suc(self, s, a):
        """
        Método abstrato que irá permitir obter a lista de estados sucessores
        para um certo estado, segundo um operador.

        Args:
            s: estado
            a: operador.

        Returns:
            estados_sucessores: lista de estados sucessores.
        """
        sn = self.__transicoes.get((s, a))
        return [sn] if sn else [] 