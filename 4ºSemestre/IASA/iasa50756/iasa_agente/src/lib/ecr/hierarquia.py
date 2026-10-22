from ecr.comport_comp import ComportComp


class Hierarquia(ComportComp):
    """
    Esta classe implementa o mecanismo de seleção de ações por hierarquia.
    Neste mecanismo a prioridade de cada ação é predefinida (estática). A ação
    que resulta da reação de mais alto nível, é esta que o agente executa.

    Esta classe é uma especialização de um Comportamento Composto (ComportComp), 
    onde implementa o mecanismo de seleção de ações segundo uma hierarquia.
    """

    def seleccionar_accao(self, accoes):
        """
        Método que seleciona uma ação dentro de uma lista de ações, 
        através de um mecanismos de escolha, neste caso seleciona-se segundo a
        sua hierarquia. Onde se considera que o primeiro elemento na lista de accoes
        é a reação de mais alto nível, suprimindo assim as outras.

        Args:
            accoes: lista de ações dos comportamentos
        
        Returns:
            accao: ação que o agente reativo deve executar
        """
        if accoes:
            accao_sel = accoes[0]
            return accao_sel