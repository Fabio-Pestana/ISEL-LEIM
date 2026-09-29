package aa;

import physics.Body;
import processing.core.PVector;

public class Separate extends Behavior{

    public Separate(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        PVector vd = new PVector();
        for(Body body : me.eye.getNearSight()){
            PVector r = PVector.sub(me.getPos(), body.getPos());
            float d = r.mag();
            r.div(d*d);
            vd.add(r);
        }
        return vd;
    }
}
