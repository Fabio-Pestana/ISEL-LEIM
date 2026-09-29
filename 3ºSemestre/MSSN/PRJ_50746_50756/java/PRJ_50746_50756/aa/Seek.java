package aa;

import physics.Body;
import processing.core.PVector;

public class Seek extends Behavior{

    /**
     * Construtor da classe Seek.
     * @param weight Peso associado a este comportamento.
     */
    public Seek(float weight) {
        super(weight);
    }

    /**
     * Calcula e retorna a velocidade desejada para o comportamento de busca.
     * @param me Boid que esta executando o comportamento.
     * @return Vetor de velocidade desejada para se mover em direcao ao alvo.
     */
    @Override
    public PVector getDesiredVelocity(Boid me){
        Body bodyTarget = me.eye.target;
        return PVector.sub(bodyTarget.getPos(), me.getPos());
    }
}
