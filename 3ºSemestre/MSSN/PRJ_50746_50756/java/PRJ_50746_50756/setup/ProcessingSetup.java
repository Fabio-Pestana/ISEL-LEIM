package setup;

/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */


import ecosystem.GameApp;
import processing.core.PApplet;

public class ProcessingSetup extends PApplet{
    private static IProcessingApp app;
    private float lastUpdateTime;

    @Override
    public void settings(){
        size(1060,800);
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

    @Override
    public void mouseReleased() {
        app.mouseReleased(this);
    }

    @Override
    public void mouseDragged() {
        app.mouseDragged(this);
    }

    public static void main(String[] args) {
        app = new GameApp();
        PApplet.main(ProcessingSetup.class);
    }
}
