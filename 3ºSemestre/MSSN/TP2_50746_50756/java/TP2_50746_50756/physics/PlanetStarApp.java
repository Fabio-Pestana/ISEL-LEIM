package physics;

import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

public class PlanetStarApp implements IProcessingApp {

    private float sunMass = 1.989e30f;
    private float earthMass = 5.97e24f;
    private float distancePlanetStar = 1.496e11f;
    private float earthSpeed = 3e4f;

    private float[] viewport = {0.2f,0.2f,0.6f,0.6f};
    private double[] window = {-1.2*distancePlanetStar, 1.2*distancePlanetStar, -1.2*distancePlanetStar, 1.2*distancePlanetStar};

    private SubPlot plt;
    private Body sun;
    private Body earth;

    private float speedUp = 60 * 60 * 24 * 30;
    // 1 segundo de simulação = mais ou menos 1 mes

    @Override
    public void setup(PApplet p) {
        plt = new SubPlot(window, viewport, p.width, p.height);
        sun = new Body(new PVector(), new PVector(), sunMass, distancePlanetStar/10, p.color(255,128,0));
        earth = new Body(new PVector(0,distancePlanetStar), new PVector(earthSpeed,0), earthMass, distancePlanetStar/20, p.color(0,180,120));
    }

    @Override
    public void draw(PApplet parent, float dt) {

        parent.background(0);
        sun.display(parent, plt);

        PVector f = sun.attraction(earth);
        earth.applyForce(f);

        earth.move(dt * speedUp);

        earth.display(parent, plt);

    }

    @Override
    public void keyPressed(PApplet parent) {

    }

    @Override
    public void mousePressed(PApplet parent) {

    }
}
