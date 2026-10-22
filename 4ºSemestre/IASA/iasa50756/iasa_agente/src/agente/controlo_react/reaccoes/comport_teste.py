from ecr.comportamento import Comportamento
from sae import Accao

class ComportTeste(Comportamento):

    def activar(self, percepcao):
        return Accao(percepcao.direccao)
