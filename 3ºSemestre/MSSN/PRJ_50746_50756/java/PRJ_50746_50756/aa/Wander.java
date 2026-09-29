package aa;

import processing.core.PVector;

public class Wander extends Behavior{

    /**
     * Construtor da classe Wander.
     * @param weight Peso associado a este comportamento.
     */
    public Wander(float weight) {
        super(weight);
    }

    /**
     * Calcula e retorna a velocidade desejada para o comportamento wander.
     * @param me Boid que esta executando o comportamento.
     * @return Vetor de velocidade desejada para o comportamento wander.
     */
    @Override
    public PVector getDesiredVelocity(Boid me) {
        PVector center = me.getPos().copy();
        center.add(me.getVel().copy().mult(me.adn.deltaTWander));
        PVector target = new PVector((float) (me.adn.radiusWander * Math.cos(me.phiWander)), (float) (me.adn.radiusWander * Math.sin(me.phiWander)));
        target.add(center);
        me.phiWander +=  2*(Math.random()-0.5) * me.adn.deltaPhiWander;
        return PVector.sub(target, me.getPos());
    }
}
