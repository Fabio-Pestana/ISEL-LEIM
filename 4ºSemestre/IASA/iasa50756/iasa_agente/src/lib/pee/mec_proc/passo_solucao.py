from dataclasses import dataclass
from mod.estado import Estado
from mod.operador import Operador

@dataclass
class PassoSolucao:
    """
    Esta classe é útil para agregar dados, sendo estes:
    Estado -> Configuração de um problema;
    Operador -> Ação que o agente executa que altera este estado para outro.
    """
    estado: Estado
    operador: Operador