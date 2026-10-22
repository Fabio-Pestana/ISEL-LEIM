from pee.mec_proc.melhor_prim.aval.avaliador_heur import AvaliadorHeur

class AvaliadorAA(AvaliadorHeur):
    """
    Esta classe herda da classe abstrata AvaliadorHeur, sendo que representa
    um avaliador de nós, onde a avaliação da sua prioridade é segundo o 
    custo acumulado do nó e segundo o custo estimado do nó até ao estado objetivo,
    custo mínimo (heurística admissível).

    No mecanismo de procura A*, a função f(n) é igual a soma do
    custo acumulado de um nó, g(n), e o seu custo minimo até ao objetivo, 
    h(n). Ou seja, nesta variante do método de procura melhor-primeiro f(n) = g(n) + h(n).
    """
    def prioridade(self, no):
        """
        Método que segundo a minimização do custo global, obtém a
        prioridade do nó.
        Minimização do custo global -> g(n) + h(n)

        Args:
            no: Nó que será avaliado
        Returns:
            prioridade: soma do custo acumulado do nó, g(n), e o custo 
            mínimo do nó até ao estado objetivo, h(n).
        """
        return no.custo + self._heuristica.h(no.estado)