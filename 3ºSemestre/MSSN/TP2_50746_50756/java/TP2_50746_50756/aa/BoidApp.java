package aa;

import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class BoidApp implements IProcessingApp {

    private  Boid boid;
    private double[] window = {-10, 10, -10, 10};
    private float[] viewport = {0, 0, 1, 1};
    private  SubPlot plt;
    private  float[] maxSpeed = {20, 20};
    private Body target;
    private List<Body> allTrackingBodies;
    private  int idx = 0;

    private final float buttonWidth = 80;
    private final float buttonHeight = 30;
    private final float margin = 10;
    private final float buttonx = buttonWidth + 2*margin;

    private final float buttonx2 = 2*buttonx - margin;


    @Override
    public void setup(PApplet p) {
        plt = new SubPlot(window, viewport, p.width, p.height);
        boid = new Boid(new PVector(), new PVector(), 1, 0.5F, p.color(100, 200, 0), p, plt);

        boid.addBehavior(new Seek(1f));
        boid.addBehavior(new Flee(1f));
        boid.addBehavior(new Wander(1f));

        target = new Body(new PVector(), new PVector(), 1f, 0.2f, p.color(250,0,0));
        allTrackingBodies = new ArrayList<Body>();
        allTrackingBodies.add(target);
        Eye eye = new Eye(boid, allTrackingBodies);
        boid.setEye(eye);
    }

    public static boolean isMouseOverButton(PApplet p, float a, float b, float c, float d){
        return (p.mouseX>= a && p.mouseX<=b && p.mouseY>=c && p.mouseY<=d);
    }

    @Override
    public void draw(PApplet p, float dt) {
        p.background(153, 217, 234);

        boid.applyBehavior(idx, dt);

        boid.display(p, plt);

        // Botao Seek
        p.fill(100);
        p.rect(margin, margin, buttonWidth, buttonHeight);
        p.fill(255);
        p.text("Seek", margin + 25, margin + 20);

        // Botao Flee
        p.fill(100);
        p.rect(buttonx, margin, buttonWidth, buttonHeight);
        p.fill(255);
        p.text("Flee", buttonx + 25, margin + 20);

        // Botao Wander
        p.fill(100);
        p.rect(buttonx2, margin, buttonWidth, buttonHeight);
        p.fill(255);
        p.text("Wander", buttonx2 + 20, margin + 20);

        p.fill(0);
        p.text("Acelera Boid -> +", p.width - 120, 20);
        p.text("Desacelera Boid -> -", p.width - 120, 40);
    }

    @Override
    public void keyPressed(PApplet p) {
        if (p.key == '+') {
           if (boid.getPos().dist(target.getPos()) < 5) {
                //Map para relacionar o "raio de reação" com a velocidade
                // e quando chegar ao target parar
                float mappedSpeed = p.map(boid.getVel().mag(), maxSpeed[0], 0, 5f, 0);
                float smoothFactor = 0.1f; // Ajuste conforme necessário
                float smoothedSpeed = boid.getVel().mag() + (mappedSpeed - boid.getVel().mag()) * smoothFactor;
                boid.setVel(boid.getVel().normalize().mult(smoothedSpeed));
            } else {
                float newSpeed = boid.getVel().mag() * 2;
                float limiteVel = Math.min(newSpeed, maxSpeed[0]);
                boid.setVel(boid.getVel().normalize().mult(limiteVel));
            }
        }

        if (p.key == '-') {
            // Desacelera a velocidade do Boid pela metade, mas não vai abaixo de zero
            float newSpeed = boid.getVel().mag() / 2;
            float limiteVel = Math.max(newSpeed, 0); // Garante que não vai abaixo de zero
            boid.setVel(boid.getVel().normalize().mult(limiteVel));
        }
    }

    @Override
    public void mousePressed(PApplet p) {
        double[] ww = plt.getWorldCoord(p.mouseX, p.mouseY);
        target.setPos(new PVector((float) ww[0], (float) ww[1]));
        if (isMouseOverButton(p, margin, buttonWidth, margin, margin+buttonHeight)) {
            idx = 0; //Seek
        }
        if (isMouseOverButton(p, buttonx, buttonx+buttonWidth, margin, margin+buttonHeight)) {
            idx = 1; //Flee
        }
        if (isMouseOverButton(p, buttonx2, buttonx2+buttonWidth, margin, margin+buttonHeight)) {
            idx = 2; //Wander
        }
    }
}
