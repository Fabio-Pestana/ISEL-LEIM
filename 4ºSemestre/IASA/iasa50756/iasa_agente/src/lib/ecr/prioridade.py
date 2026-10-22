from ecr.comport_comp import ComportComp


class Prioridade(ComportComp):
    """
    Esta classe implementa o mecanismo de seleção de ações por prioridade. Para isto
    a classe Prioridade tem de implementar a classe abstrada ComportComp (que representa um
    comportamento composto).
    
    Neste mecanismo a prioridade de cada ação é dinâmica, ou seja através da percepção
    do ambiente (feito pelo agente), é implementada uma prioridade para cada reação, sendo
    que a ação com maior prioridade será executada pelo agente.    
    """

    def seleccionar_accao(self, accoes):
        """
        Método que seleciona uma acção dentro de uma lista de acções, 
        através de um mecanismos de escolha, neste caso seleciona-se segundo a
        sua prioridade.

        Args:
            accoes: lista de ações dos comportamentos
        
        Returns:
            accao: ação que o agente reativo deve executar
        """
        if accoes:
            accao_sel = max(accoes, key=lambda accao: accao.prioridade) 
            # função funcional que retorna o maior valor da lista
            # lambda -> utilizada para funções inline no python
            return accao_sel