from pee.mec_proc.prof.procura_prof_lim import ProcuraProfLim


class ProcuraProfIter(ProcuraProfLim):
    """      
    Esta classe é uma especialização do mecanismo de procura em profundidade limitada, 
    representando o mecanismo de procura em profundidade iterativa. 
    
    Este mecanismos é uma método que combina os benefícios do mecanismo de procura em profundidade 
    com a garantia de encontrar uma solução óptima. Este expande gradualmente o limite 
    de profundidade, explorando mais a fundo em cada iteração, até que a solução seja 
    encontrada. Isso garante que a solução encontrada seja a mais profunda possível, 
    garantindo que esta seja óptima.
    """

    def procurar(self, problema, inc_prof = 1, limite_prof = 100):
        """
        Método responsável por realizar a busca em profundidade iterativa.
        Este método realiza uma busca em profundidade iterativa, aumentando gradualmente
        o limite de profundidade a cada iteração, até que uma solução seja encontrada
        ou até que o limite máximo de profundidade seja atingido.

        Args:
            problema: problema a resolver
            inc_prof: incrementação da profundidade a ser utilizado em cada iteração (default é 1)
            limite_prof: limite máximo de profundidade a ser explorado (default é 100).

        Returns:
            solucao: A solução do problema (se não existir retorna None)
        """

        for profundidade in range(0, limite_prof +1 , inc_prof):
            self.prof_max = profundidade
            solucao = super().procurar(problema)
            if solucao:
                return solucao
            