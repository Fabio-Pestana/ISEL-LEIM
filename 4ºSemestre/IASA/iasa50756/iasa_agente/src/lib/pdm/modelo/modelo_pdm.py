from abc import ABC, abstractmethod

class ModeloPDM(ABC):
    """
    Classe abstrata que representa um modelo do processo de decisão de Markov.
    Com este modelo é possivel representar o mundo através dos seguintes componentes:
    - S -> Conjunto de estados do mundo, ou seja conjunto de componentes que
    definem o mundo, como as suas posições e configurações.
    - A(s) -> Conjunto das ações possíveis que podem ser tomadas num certo 
    estado, s. Ou seja as ações que um agente pode tomar num certo estado.
    - T(s, a, sn) -> Função de transição de estado, esta função define a probabilidade
    de transição de um estado s para um estado sn, através de uma ação a.
    - R(s, a, sn) -> Função de recompensa, esta função determina a recompensa ao transitar
    de um estado s para um estado sn através de uma ação a. A recompensa representa o ganho
    ou perda num determinado estado, s.

    Nesta classe não é definido um componente que representa a taxa de desconto (representado
    por um gama). Esta taxa determina a importância das recompensas futuras em comparação com as
    recompensas imediatas.
    """

    @abstractmethod
    def S(self):
        """
        Método abstrato que permite obter uma lista de estados do mundo.

        Returns:
            estados: lista de estados do mundo.
        """

    @abstractmethod
    def A(self, s):
        """
        Método abstrato que permite obter uma lista de operadores
        possíveis segundo um estado.

        Args:
            s: estado para o qual queremos saber os seus operadores 

        Returns:
            operadoes: lista de operadores do mundo
        """

    @abstractmethod
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
        """

    @abstractmethod
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

    @abstractmethod
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