package aa;

import processing.core.PVector;

public class Wander extends Behavior{

    public Wander(float weight) {
        super(weight);
    }

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
