from pee.mec_proc.melhor_prim.aval.avaliador_heur import AvaliadorHeur

class AvaliadorSof(AvaliadorHeur):
    """
    Esta classe herda da classe abstrata AvaliadorHeur, sendo que representa
    um avaliador de nós, onde a avaliação da sua prioridade é segundo o 
    custo estimado do nó até ao estado objetivo (estimativa do custo a partir 
    do estado do nó até ao estado objetivo, sem que o percurso até este tenha sido explorado).

    No mecanismo de procura sôfrega, a função f(n) é igual ao
    o custo estimado do nó até ao estado objetivo, representado por h(n). Ou seja, 
    nesta variante do método de procura melhor-primeiro f(n) = h(n).
    """
    def prioridade(self, no):
        """
        Método que segundo o custo estimado do nó até ao estado objetivo (definido através de
        uma heurística), obtém a prioridade do nó.

        Args:
            no: Nó que será avaliado
        Returns:
            prioridade: custo estimado do nó até ao estado objetivo.
        """
        return self._heuristica.h(no.estado)