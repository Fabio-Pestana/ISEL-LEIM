from sae import Accao

class Resposta:
    """
    Classe que representa uma resposta a um estímulo do ambiente, 
    o agente responde a esses estímulos executando uma certa ação predefinida
    (atributo accao da classe), ou seja, uma resposta produz uma ação 
    que será executada por um agente.
    Esta resposta pode ter uma prioridade segundo a intensidade do 
    estímulo percepcionado, para assim permitir que haja um mecanismo de
    seleção de ação segundo a sua prioridade.

    """
    def __init__(self, accao):
        """
        Método construtor de uma Resposta

        Args:
            accao: ação que o agente executa como resposta ao estímulo do ambiente
        """
        self._accao = accao


    def activar(self, percepcao, intensidade=0):
        """
        Método que ativa uma ação alterando a sua prioridade,
        caso uma percepção exista. Quando maior for a intensidade
        de um estímulo, maior será a prioridade da acção. Sendo assim
        a execução da ação do agente depende da intensidade do estímulo.
        
        Args:
            percepcao: percepção do agente, do ambiente onde-se encontra
            intensidade: intensidade do estímulo (Default = 0).

        Returns:
            accao: ação que o agente deve executar (prioridade atualizada)
        """
        if percepcao is not None:
            self._accao.prioridade = intensidade
            return self._accao
        