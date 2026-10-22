from agente.controlo_delib.controlo_delib import ControloDelib
from plan.plano_pee.planeador_pee import PlaneadorPEE
from sae import Agente, Simulador


class AgenteDelibPEE(Agente):
    def __init__(self):
        planeador = PlaneadorPEE()
        controlo_deliberativo = ControloDelib(planeador)
        super().__init__(controlo_deliberativo)


# Executar simulação
if __name__ == "__main__":        
    Simulador(4, AgenteDelibPEE()).executar()    
