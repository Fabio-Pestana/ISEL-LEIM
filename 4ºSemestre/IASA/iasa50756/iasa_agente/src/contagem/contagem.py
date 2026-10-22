from mod_prob.heuristica_contagem import HeuristicaContagem
from mod_prob.problema_contagem import ProblemaContagem
from pee.mec_proc.larg.procura_largura import ProcuraLargura
from pee.mec_proc.melhor_prim.procura_aa import ProcuraAA
from pee.mec_proc.melhor_prim.procura_custo_unif import ProcuraCustoUnif
from pee.mec_proc.melhor_prim.procura_sofrega import ProcuraSofrega
from pee.mec_proc.prof.procura_profundidade import ProcuraProfundidade


class Contagem():
    """
    Classe que resolve o problema dado pelo docente (problema da contagem).
    """

    #Configuração do problema:
    VALOR_INICIAL = 0
    VALOR_FINAL = 9
    INCREMENTOS = [1, 2, -1]

    #Definir o problema
    problema = ProblemaContagem(VALOR_INICIAL, VALOR_FINAL, INCREMENTOS)
    
    #Definir a heuristica de contagem
    heuristica = HeuristicaContagem(VALOR_FINAL)

    # Iniciar o mecanismo de procura
    mec_proc = ProcuraSofrega()
    
    # Resolver o problema
    solucao = mec_proc.procurar(problema, heuristica)

    for passo in solucao:
        print(passo.estado, passo.operador.incremento)


