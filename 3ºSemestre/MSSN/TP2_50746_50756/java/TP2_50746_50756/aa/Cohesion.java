package aa;

import physics.Body;
import processing.core.PVector;

public class Cohesion extends Behavior{

    public Cohesion(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        PVector target = me.getPos().copy();
        for(Body body : me.eye.getFarSight()){
            target.add(body.getPos());
        }
        target.div(me.eye.getFarSight().size()+1);
        return PVector.sub(target, me.getPos());
    }
}
