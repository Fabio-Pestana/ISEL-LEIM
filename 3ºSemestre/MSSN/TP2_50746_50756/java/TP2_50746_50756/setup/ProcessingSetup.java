package setup;

/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import aa.BoidApp;
import aa.FlockDebuggingApp;
import aa.FlockTextApp;
import physics.ParticleSystemApp;
import physics.PlanetStarApp;
import physics.SistemaSolarAPP;
import processing.core.PApplet;

public class ProcessingSetup extends PApplet{
    private static IProcessingApp app;
    private float lastUpdateTime;

    @Override
    public void settings(){
        size(1200,1000);
    }

    @Override
    public void setup(){
        app.setup(this);
        lastUpdateTime = millis();
    }

    @Override
    public void draw(){
        float now = millis();
        float dt = (now - lastUpdateTime) / 1000f; //intervalo de tempo
        lastUpdateTime = now;
        app.draw(this, dt);
    }

    @Override
    public void mousePressed() {
        app.mousePressed(this);
    }

    @Override
    public void keyPressed() {
        app.keyPressed(this);
    }

    public static void main(String[] args) {
        //app = new PlanetStarApp();
        //app = new ParticleSystemApp();
        app = new SistemaSolarAPP();
        //app = new BoidApp();
        //app = new FlockTextApp();
        //app = new FlockDebuggingApp();
        PApplet.main(ProcessingSetup.class);
    }
}
