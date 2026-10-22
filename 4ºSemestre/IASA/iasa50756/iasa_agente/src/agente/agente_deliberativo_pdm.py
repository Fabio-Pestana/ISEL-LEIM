from agente.controlo_delib.controlo_delib import ControloDelib
from plan.plan_pdm.planeador_pdm import PlaneadorPDM
from sae import Agente, Simulador


class AgenteDelibPDM(Agente):
    def __init__(self):
        planeador = PlaneadorPDM(0.98)
        controlo_deliberativo = ControloDelib(planeador)
        super().__init__(controlo_deliberativo)


# Executar simulação
if __name__ == "__main__":        
    Simulador(4, AgenteDelibPDM()).executar()    
