package aa;

import physics.Body;
import processing.core.*;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Boid extends Body {

    private SubPlot plt;
    private PShape shape;
    private List<Behavior> behaviorList;
    protected ADNBoid adn;
    protected Eye eye;
    protected float phiWander;
    private float sumWeights;


    protected Boid(PVector pos, PVector vel, float mass, float radius, int color, PApplet p, SubPlot plt) {
        super(pos, vel, mass, radius, color);

        adn = new ADNBoid();
        behaviorList = new ArrayList<Behavior>();
        this.plt = plt;
        setShape(p, plt, color);
    }

    public void setEye(Eye eye){
        this.eye = eye;
    }

    public Eye getEye(){return this.eye;}
    public void setShape(PApplet p, SubPlot plt, int color) {
        float[] rr = plt.getDimInPixel(radius, radius);
        shape = p.createShape();
        shape.beginShape();
        shape.vertex(-rr[0], rr[0] / 2);
        shape.vertex(rr[0], 0);
        shape.vertex(-rr[0], -rr[0] / 2);
        shape.vertex(-rr[0] / 2, 0);
        shape.fill(color);
        shape.endShape(PConstants.CLOSE);
    }


    private void updateSumWeights(){
        sumWeights = 0;
        for(Behavior behavior : behaviorList){
            sumWeights += behavior.getWeight();
        }
    }

    public void addBehavior(Behavior behavior){
        behaviorList.add(behavior);
        updateSumWeights();
    }

    public void removeBehavior(Behavior behavior){
        if (behaviorList.contains(behavior))
            behaviorList.remove(behavior);
        updateSumWeights();
    }

    public void applyBehavior(int i, float dt){
        if (eye!=null){
            eye.look();
        }
        Behavior behavior = behaviorList.get(i);
        PVector vd = behavior.getDesiredVelocity(this);
        move(dt, vd, plt.getWindow());
    }
    public void applyBehaviors(float dt){
        if (eye!=null){
            eye.look();
        }
        PVector vd = new PVector();
        for(Behavior behavior : behaviorList){
            PVector vdd = behavior.getDesiredVelocity(this);
            vdd.mult(behavior.getWeight()/sumWeights);
            vd.add(vdd);
        }
        move(dt, vd, plt.getWindow());
    }

    public void move(float dt, PVector vd, double[] window){

        vd.normalize().mult(adn.maxSpeed);
        PVector fs = PVector.sub(vd, vel);
        applyForce(fs.limit(adn.maxForce));
        super.move(dt);
        // Verifica se o boid está fora dos limites da janela
        if (pos.x >= window[1]) {
            pos.x = (float) window[0];
        } else if (pos.x <= window[0]) {
            pos.x = (float) window[1];
        }

        if (pos.y >= window[3]) {
            pos.y = (float) window[2];
        } else if (pos.y <= window[2]) {
            pos.y = (float) window[3];
        }
    }

    public void display(PApplet p, SubPlot plt) {
        p.pushMatrix(); //guarda sistema de coordenadas
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        p.translate(pp[0], pp[1]);
        p.rotate(-vel.heading()); // Angulo em radianos que o vetor "vel" faz com o eixo do x
        p.shape(shape);
        p.popMatrix();
    }

    public void display(PApplet p, SubPlot plt, boolean debugger) {
        p.pushStyle();
        p.pushMatrix();
        display(p, plt);

        // usa-se o valor maximo da viewport e obtem se a sua dimensao em x para usar como o tamanho da arrow
        float arrowSize = plt.getDimInPixel(plt.getViewport()[3], 0)[0] * 3;

        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        p.translate(pp[0], pp[1]);
        p.rotate(-vel.heading());

        if (debugger) {
            p.stroke(0);
            //linha principal da seta
            p.line(0, 0, arrowSize, 0);
            // lado esquerdo do pico
            p.line(arrowSize, 0, arrowSize - arrowSize / 5, arrowSize / 5);
            // lado direito do pico
            p.line(arrowSize, 0, arrowSize - arrowSize / 5, -arrowSize / 5);
        }

        p.popMatrix();
        p.popStyle();
    }

    public boolean isMouseOver(float x, float y) {
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float halfWidth = shape.width / 2;
        float halfHeight = shape.height / 2;

        float leftX = pp[0] - halfWidth;
        float rightX = pp[0] + halfWidth;
        float topY = pp[1] - halfHeight;
        float bottomY = pp[1] + halfHeight;

        return x > leftX && x < rightX && y > topY && y < bottomY;
    }
}
