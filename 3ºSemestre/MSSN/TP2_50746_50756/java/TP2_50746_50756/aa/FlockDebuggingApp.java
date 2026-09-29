package aa;

import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class FlockDebuggingApp implements IProcessingApp {

    private Boid wander, seeker, pursuiter, boid;
    private Flock flock;
    private float[] sacWeights = {1f, 1f, 1f, 0f, 0f};
    private double[] window = {-10, 10, -10, 10};
    private float[] viewport1 = {0.02f, 0.51f, 0.96f, 0.47f};
    private float[] viewport2 = {0.02f, 0.02f, 0.47f, 0.47f};
    private float[] viewport3 = {0.51f, 0.02f, 0.47f, 0.47f};
    private SubPlot plt1, plt2, plt3;
    private int controlledBoidIndex = 0; // Boid a ser controlado

    private Boid predador;

    private List<Body> allTrackingBodies;

    private Eye eye;

    private Body b;

    private final float buttonWidth = 100;

    private final float buttonHeight = 30;
    private final float margin = 20;
    private final float buttonx = buttonWidth + 2*margin;

    private int idx;

    @Override
    public void setup(PApplet p) {
        plt1 = new SubPlot(window, viewport1, p.width, p.height);
        plt2 = new SubPlot(window, viewport2, p.width, p.height);
        plt3 = new SubPlot(window, viewport3, p.width, p.height);

        flock = new Flock(20, .1f, .3f,p.color(0, 100, 200) , sacWeights, p, plt1);
        boid = flock.getBoid(4);

        wander = new Boid(new PVector(p.random((float) window[0], (float) window[1]), p.random((float) window[2], (float) window[3])), new PVector() , 0.5f, 0.3f, p.color(255, 0, 0), p, plt2);
        wander.addBehavior(new Wander(1f));

        pursuiter = new Boid(new PVector(p.random((float) window[0], (float) window[1]), p.random((float) window[2], (float) window[3])), new PVector() , 0.5f, 0.3f, p.color(0, 0, 255), p, plt2);
        pursuiter.addBehavior(new Pursuit(1f));
        List<Body> allTrackingBodies = new ArrayList<Body>();
        allTrackingBodies.add(wander);
        pursuiter.setEye(new Eye(pursuiter, allTrackingBodies));

        seeker = new Boid(new PVector(p.random((float) window[0], (float) window[1]), p.random((float) window[2], (float) window[3])), new PVector() , 0.5f, 0.3f, p.color(0, 255, 255), p, plt3);
        seeker.addBehavior(new Seek(1f));
        seeker.addBehavior(new Flee(1f));
        //seeker.addBehavior(new Flee(1f));
        b = new Body(new PVector(), new PVector(), 1f, 0.3f, p.color(0));
        allTrackingBodies = new ArrayList<Body>();
        allTrackingBodies.add(b);
        seeker.setEye(new Eye(seeker, allTrackingBodies));
    }

    @Override
    public void draw(PApplet p, float dt) {
        p.background(0);

        float[] bb = plt1.getBoundingBox();
        p.fill(122);
        p.rect(bb[0], bb[1], bb[2], bb[3]);
        drawText(p, "Flock com um Boid (Debugging)", bb);

        List<Body> boidsInSight = boid.getEye().getBoidsInSight();

        for (Boid b : flock.getBoidList()) {
            if (boidsInSight.contains(b)) {
                b.setShape(p, plt1, p.color(255, 0, 0));   // Cor dos Boids no campo de visao do Boid escolhido dentro do flock
            } else {
                b.setShape(p, plt1, p.color(0, 100, 200));// Cor "padrão" para boids fora da visão do boid escolhido dentro do flock
            }
        }

        flock.applyBehavior(dt);
        flock.display(p, plt1);
        boid.getEye().display(p, plt1);
        boid.display(p, plt1, true);

        bb = plt2.getBoundingBox();
        p.fill(120, 175, 40);
        p.rect(bb[0], bb[1], bb[2], bb[3]);
        drawText(p, "Boids Wander e Pursuiter", bb);

        wander.applyBehaviors(dt);
        pursuiter.applyBehaviors(dt);
        wander.display(p, plt2);
        pursuiter.display(p, plt2);

        bb = plt3.getBoundingBox();
        p.fill(160, 100, 15);
        p.rect(bb[0], bb[1], bb[2], bb[3]);
        drawText(p, "Boid Seeker", bb);

        seeker.applyBehavior(idx, dt);
        seeker.display(p, plt3);
        b.display(p, plt3);
    }

    private void drawText(PApplet p, String text, float[] boundingBox) {
        float centerX = boundingBox[0] + boundingBox[2] / 2;
        float topY = boundingBox[1] + 20;

        p.pushStyle();
        p.fill(0);
        p.textAlign(PApplet.CENTER, PApplet.TOP);
        p.textSize(14);
        p.text(text, centerX, topY);
        p.popStyle();
    }

    @Override
    public void keyPressed(PApplet p) {
        if (p.key == 't' || p.key == 'T') {
            idx = (idx+1)%2;
        }
    }

    @Override
    public void mousePressed(PApplet parent) {
        if (plt3.isInside(parent.mouseX, parent.mouseY)) {
            double[] w = plt3.getWorldCoord(parent.mouseX, parent.mouseY);
            b.setPos(new PVector((float) w[0], (float) w[1]));
        }
    }
}

