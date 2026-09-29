package aa;

import physics.Body;
import processing.core.PVector;

public class Pursuit extends Behavior{

    /**
     * Construtor da classe Pursuit.
     * @param weight Peso associado a este comportamento.
     */
    public Pursuit(float weight) {
        super(weight);
    }

    /**
     * Calcula e retorna a velocidade desejada para o comportamento de pursuit.
     * @param me Boid que esta executando o comportamento.
     * @return Vetor de velocidade desejada para se mover em direcao ao alvo com previsao da posicao futura.
     */
    @Override
    public PVector getDesiredVelocity(Boid me) {
        Body bodyTarget = me.eye.target;
        PVector bVelocity = bodyTarget.getVel().copy();
        PVector d = bVelocity.mult(me.adn.deltaTPursuit);
        PVector target = PVector.add(bodyTarget.getPos(), d);
        return  PVector.sub(target, me.getPos());
    }
}
