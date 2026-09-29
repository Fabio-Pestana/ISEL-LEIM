package aa;

import physics.Body;
import physics.ParticleSystem;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Eye {

    private List<Body> allTrackingBodies;
    private List<Body> farSight;
    private List<Body> nearSight;
    private Boid me;
    protected Body target;

    public List<Body> getAllTrackingBodies() {
        return allTrackingBodies;
    }

    public void setAllTrackingBodies(List<Body> allTrackingBodies) {
        this.allTrackingBodies = allTrackingBodies;
    }

    public Eye(Boid me, List<Body> allTrackingBodies){
        this.me = me;
        this.allTrackingBodies = allTrackingBodies;
        target = allTrackingBodies.get(0);
    }

    public void look(){
        farSight = new ArrayList<Body>();
        nearSight = new ArrayList<Body>();
        for(Body b : allTrackingBodies){
            if (b!=null){
                if (farSight(b.getPos()))
                    farSight.add(b);
                if (nearSight(b.getPos()))
                    nearSight.add(b);
            }
        }
    }

    public List<Body> getFarSight() {
        return farSight;
    }

    public List<Body> getNearSight() {
        return nearSight;
    }

    private boolean inSight(PVector target, float maxDistance, float maxAngle){
        PVector r = PVector.sub(target, me.getPos());
        float d = r.mag();
        float angle = PVector.angleBetween(r, me.getVel());
        return ((d>0) && (d < maxDistance) && (angle < maxAngle));
    }

    public void display(PApplet p, SubPlot plt){
        p.pushStyle();
        p.pushMatrix();
        float[] pp = plt.getPixelCoord(me.getPos().x, me.getPos().y);
        p.translate(pp[0], pp[1]);
        p.rotate(-me.getVel().heading());
        p.noFill();
        p.stroke(255, 0, 0);
        p.strokeWeight(3);
        float[] dd1 = plt.getDimInPixel(me.adn.visionDistance, me.adn.visionDistance);
        float[] dd2 = plt.getDimInPixel(me.adn.visionSafeDistance, me.adn.visionSafeDistance);
        p.rotate(me.adn.visionAngle);
        p.line(0,0,dd1[0], 0);
        p.rotate(-2*me.adn.visionAngle);
        p.line(0,0,dd1[0], 0);
        p.rotate(me.adn.visionAngle);
        p.arc(0,0,2*dd1[0], 2*dd1[0], -me.adn.visionAngle, me.adn.visionAngle);
        p.stroke(255, 0, 255);
        p.circle(0, 0, 2*dd2[1]);
        p.popMatrix();
        p.popStyle();
    }

    private boolean farSight(PVector target){
        return inSight(target, me.adn.visionDistance, me.adn.visionAngle);
    }

    private boolean nearSight(PVector target){
        return inSight(target, me.adn.visionSafeDistance, (float) Math.PI);
    }


    public List<Body> getBoidsInSight() {
        List<Body> boidsInSight = new ArrayList<>();
        for (Body b : allTrackingBodies) {
            if (farSight(b.getPos()) || nearSight(b.getPos())) {
                boidsInSight.add(b);
            }
        }
        return boidsInSight;
    }
}
