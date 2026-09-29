package aa;

import physics.Body;
import processing.core.PVector;

public class Align extends Behavior{

    public Align(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        PVector vd = me.getVel().copy();
        for(Body body : me.eye.getFarSight()){
            vd.add(body.getVel());
        }
        //"+1" porque usamos a nossa própria velocidade, além da dos boids na lista
        return vd.div(me.eye.getFarSight().size()+1);
    }
}
