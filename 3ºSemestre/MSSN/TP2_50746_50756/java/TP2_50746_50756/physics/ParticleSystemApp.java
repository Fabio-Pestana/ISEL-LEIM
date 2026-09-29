package physics;

import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

public class ParticleSystemApp implements IProcessingApp {

    private ParticleSystem particleSystem;

    private ParticleSystemControl particleSystemControl;
    private double[] window = {-10, 10, -10, 10};
    private float[] viewport = {0, 0, 1, 1};
    private SubPlot plt;

    private  float[] velControl = {0, PApplet.radians(20), 1, 3};

    @Override
    public void setup(PApplet p) {
        plt = new SubPlot(window, viewport, p.width, p.height);
        particleSystemControl = new ParticleSystemControl(velControl);
        particleSystem = new ParticleSystem(new PVector(), new PVector(),  1F, .2F, p.color(255,0,0), 5F, particleSystemControl);
    }

    @Override
    public void draw(PApplet p, float dt) {
        p.background(255);

        particleSystem.move(dt);
        particleSystem.display(p, plt);
    }

    @Override
    public void keyPressed(PApplet p) {

    }

    @Override
    public void mousePressed(PApplet p) {

    }
}
