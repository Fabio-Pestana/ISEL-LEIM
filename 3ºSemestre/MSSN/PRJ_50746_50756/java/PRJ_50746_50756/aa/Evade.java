package aa;

import physics.Body;
import processing.core.PVector;

public class Evade extends Behavior{

    /**
     * Construtor da classe Evade.
     * @param weight Peso associado a este comportamento.
     */
    public Evade(float weight) {
        super(weight);
    }

    /**
     * Calcula e retorna a velocidade desejada para o comportamento de evade.
     * @param me Boid que esta executando o comportamento.
     * @return Vetor de velocidade desejada para evasao em relacao ao alvo.
     */
    @Override
    public PVector getDesiredVelocity(Boid me) {
        Body bodyTarget = me.eye.target;
        PVector bVelocity = bodyTarget.getVel().copy();
        PVector d = bVelocity.mult(me.adn.deltaTPursuit);
        PVector target = PVector.add(bodyTarget.getPos(), d);
        PVector vd = PVector.sub(target, me.getPos());
        return vd.mult(-1);
    }
}
