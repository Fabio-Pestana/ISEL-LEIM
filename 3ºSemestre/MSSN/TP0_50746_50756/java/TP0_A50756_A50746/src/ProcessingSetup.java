/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;

public class ProcessingSetup extends PApplet{
    private static IProcessingApp app;
    private float lastUpdateTime;

    @Override
    public void settings(){
        size(1200,1200);
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
        //app = new Ex_B_1a();
        //app = new Ex_B_1b();
        //app = new Ex_B_1c();
        //app = new Ex_B_1d();
        app = new Ex_B_2();
        PApplet.main(ProcessingSetup.class);
    }
}

