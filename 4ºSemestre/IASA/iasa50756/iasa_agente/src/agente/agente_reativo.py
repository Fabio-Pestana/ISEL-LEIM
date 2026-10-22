from sae import Agente, Simulador
from agente.controlo_react.controlo_react import ControloReact
from agente.controlo_react.reaccoes.recolher import Recolher


class AgenteReativo(Agente):
    def __init__(self):
        recolher = Recolher()
        controlo_reactive = ControloReact(recolher)
        super().__init__(controlo_reactive)


# Executar simulação
if __name__ == "__main__":        
    Simulador(1, AgenteReativo()).executar()    
