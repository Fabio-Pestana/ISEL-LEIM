from pee.mec_proc.melhor_prim.aval.avaliador import Avaliador


class AvaliadorCustoUnif(Avaliador):
    """
    Esta classe herda da classe abstrata Avaliador, sendo que representa
    um avaliador de nós, onde a avaliação da sua prioridade é segundo o 
    custo acumulado do nó (o custo do percurso deste o estado inicial até 
    ao estado do nó).

    No mecanismo de procura de custo uniforme, a função f(n) é igual ao
    custo acumulado de um nó, representado por g(n). Ou seja, nesta variante
    do método de procura melhor-primeiro f(n) = g(n).
    """
    def prioridade(self, no):
        """
        Método que segundo o custo acumulado do nó, obtém a sua
        prioridade.

        Args:
            no: Nó que será avaliado
        Returns:
            prioridade: custo acumulado do nó.
        """
        return no.custo