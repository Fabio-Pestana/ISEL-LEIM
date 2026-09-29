package aa;

import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class FlockTextApp implements IProcessingApp {

    private Flock flock;
    private float[] sacWeights = {1f, 1f, 1f, 0f, 0f};
    private double[] window = {-10, 10, -10, 10};
    private float[] viewport = {0, 0, 1, 1};
    private SubPlot plt;
    private int controlledBoidIndex = 0; // Boid a ser controlado

    private Boid predador;

    private List<Body> allTrackingBodies;

    private Eye eye;

    private Body b;

    private final float buttonWidth = 100;

    private final float buttonHeight = 30;
    private final float margin = 20;
    private final float buttonx = buttonWidth + 2*margin;

    @Override
    public void setup(PApplet p) {
        plt = new SubPlot(window, viewport, p.width, p.height);
        flock = new Flock(5, .1f, 0.8f, p.color(200, 200, 0) , sacWeights, p, plt);
        flock.getBoid(controlledBoidIndex).setShape(p, plt, p.color(100, 200, 0)); //lider
        allTrackingBodies = new ArrayList<Body>();
        predador = null;
        b = null;
    }

    @Override
    public void draw(PApplet p, float dt) {
        p.background(153, 217, 234);
        flock.applyBehavior(dt);
        flock.display(p, plt);
        float[] bb = plt.getBoundingBox();

        if (predador!=null){
            if (b!=null) {
                predador.applyBehaviors(dt);
            }else {
                drawText(p, "Antes de libertar o predador escolha uma das presas!", bb);
            }
            predador.display(p, plt);
        }

        // Botao Flock Liderança
        p.fill(100);
        p.rect(margin, margin, buttonWidth, buttonHeight);
        p.fill(255);
        p.text("Flock Liderança", margin + 5, margin + 20);

        // Botao Predador/Presa
        p.fill(100);
        p.rect(buttonx, margin, buttonWidth, buttonHeight);
        p.fill(255);
        p.text("Predador/Presa", buttonx + 5, margin + 20);

        p.fill(0);
        p.text("Flock Liderança -> WASD para mexer Boid Verde", 20, p.height - 60);
        p.text("Predador/Presa -> Clique sobre um boid para o predador o seguir, este fica destacado com uma cor random", 20, p.height - 40);
        p.text("O predador vai sempre atras do ultimo boid pressionado", 20, p.height - 20);
    }

    @Override
    public void keyPressed(PApplet p) {
        float speed = 3f;
        Boid controlledBoid = flock.getBoid(controlledBoidIndex); //lider

        if (p.key == 'w' || p.key == 'W') {
            controlledBoid.applyForce(new PVector(0, speed));
        }
        if (p.key == 's' || p.key == 'S') {
            controlledBoid.applyForce(new PVector(0, -speed));
        }
        if (p.key == 'a' || p.key == 'A') {
            controlledBoid.applyForce(new PVector(-speed, 0));
        }
        if (p.key == 'd' || p.key == 'D') {
            controlledBoid.applyForce(new PVector(speed, 0));
        }
        flock.setFollowTarget(controlledBoid.getPos());
    }

    @Override
    public void mousePressed(PApplet p) {
        Boid aux = flock.getBoidMouse(p);
        int i = flock.getBoidIndex(aux);
        //Flock liderança
        if (BoidApp.isMouseOverButton(p, margin, buttonWidth, margin, margin+buttonHeight)) {
            float[] weights = {1f, 1f, 1f, 0f, 0f};
            flock.removeAllBehavior();
            flock.setBehaviorAllBoids(weights);
            predador = null;
            resetColors(p, plt);
        }
        //Predador/Presa
        if (BoidApp.isMouseOverButton(p, buttonx, buttonx+buttonWidth, margin, margin+buttonHeight)) {
            flock.getBoid(controlledBoidIndex).setShape(p, plt, p.color(200, 200, 0));
            float[] weights = {1f, 0.5f, 0.5f, 0f, 2f};
            flock.removeAllBehavior();
            flock.setBehaviorAllBoids(weights);
            predador = new Boid(new PVector(), new PVector(), 1, 1F, p.color(100, 200, 0), p, plt);
            predador.addBehavior(new Seek(2f));
            b=aux;
            allTrackingBodies.add(b);
            eye = new Eye(predador,allTrackingBodies);
            predador.setEye(eye);
        }
        if (i!=-1 && allTrackingBodies.size()!=0){
            b = aux;
            allTrackingBodies.set(0, b);
            if (predador!=null)
            {
                flock.getBoid(i).setShape(p, plt, p.color(p.random(255), p.random(255), p.random(255)));
                eye = new Eye(predador, allTrackingBodies);
                predador.setEye(eye);
            }
        }
    }

    public void resetColors(PApplet p, SubPlot plt){
        for (Boid b : flock.getBoidList()){
            b.setShape(p, plt, p.color(200, 200, 0));
        }
        flock.getBoid(controlledBoidIndex).setShape(p, plt, p.color(100, 200, 0));
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
}
