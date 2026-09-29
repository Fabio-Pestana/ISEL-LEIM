package aa;

import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Flock {

    private List<Boid> boidList;
    private PVector followTarget;

    private Separate s;
    private Align a;
    private Cohesion c;
    private Seek seek;
    private Flee f;

    public Flock(int nboids, float mass, float radius, int color, float[] sacWeights, PApplet p, SubPlot plt){

        double[] w = plt.getWindow();
        followTarget = new PVector();
        boidList = new ArrayList<Boid>();
        for(int i =0; i<nboids; i++){
            float x = p.random((float) w[0], (float) w[1]);
            float y = p.random((float) w[2], (float) w[3]);
            Boid boid = new Boid((new PVector(x,y)), new PVector(), mass, radius, color, p, plt);
            s = new Separate(sacWeights[0]);
            a = new Align(sacWeights[1]);
            c = new Cohesion(sacWeights[2]);
            seek = new Seek(sacWeights[3]);
            f = new Flee(sacWeights[4]);
            boid.addBehavior(s);
            boid.addBehavior(a);
            boid.addBehavior(c);
            boid.addBehavior(seek);
            boid.addBehavior(f);
            boidList.add(boid);
        }

        List<Body> bodyList = boidList2BodyList(boidList);
        for (Boid boid : boidList){
            boid.setEye(new Eye(boid, bodyList));
        }
    }


    public void removeAllBehavior(){
        for (Boid boid : boidList){
            boid.removeBehavior(s);
            boid.removeBehavior(a);
            boid.removeBehavior(c);
            boid.removeBehavior(seek);
            boid.removeBehavior(f);
        }
    }

    public void setBehaviorAllBoids(float[] sacWeights){
        for (Boid boid : boidList){
            s = new Separate(sacWeights[0]);
            a = new Align(sacWeights[1]);
            c = new Cohesion(sacWeights[2]);
            seek = new Seek(sacWeights[3]);
            f = new Flee(sacWeights[4]);
            boid.addBehavior(s);
            boid.addBehavior(a);
            boid.addBehavior(c);
            boid.addBehavior(seek);
            boid.addBehavior(f);
        }
    }

    public void setFollowTarget(PVector target) {
        this.followTarget = target.copy();
    }

    private List<Body> boidList2BodyList(List<Boid> boidList){
        List<Body> bodyList = new ArrayList<Body>();
        for(Boid boid : boidList){
            bodyList.add(boid);
        }
        return bodyList;
    }

    public Boid getBoid(int i){
        return boidList.get(i);
    }

    public void applyBehavior(float dt){
       /* for(Boid boid : boidList){
            boid.applyBehaviors(dt);
        }*/
        for (Boid boid : boidList) {
            PVector followForce = PVector.sub(followTarget, boid.getPos());
            followForce.setMag(0.1f);
            boid.applyForce(followForce);


            boid.applyBehaviors(dt);
        }
    }

    public void display(PApplet p, SubPlot plt){
        for(Boid b : boidList){
            b.display(p, plt);
        }
    }

    public List<Boid> getBoidList() {
        return boidList;
    }

    public void setBoidList(List<Boid> boidList) {
        this.boidList = boidList;
    }

    public List<Body> boidListToBodyList(List<Boid> boidList) {
        List<Body> bodyList = new ArrayList<>();
        bodyList.addAll(boidList);
        return bodyList;
    }

    public Boid getBoidMouse(PApplet p){
        for(Boid b : boidList){
            if (b.isMouseOver(p.mouseX, p.pmouseY))
                return b;
        }
        return null;
    }

    public int getBoidIndex(Body b) {
        return boidListToBodyList(this.boidList).indexOf(b);
    }
}
