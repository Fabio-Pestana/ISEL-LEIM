/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;

public class Ex_B_1d implements IProcessingApp{

    private float x;
    private float y;
    private double easing = 0.05;

    @Override
    public void setup(PApplet parent) {
        parent.noStroke();
    }

    @Override
    public void draw(PApplet parent, float dt) {
        parent.background(0);

        //obtem as coordenadas do cursor
        float targetX = parent.mouseX;
        float targetY = parent.mouseY;

        float dx = targetX - x;
        x += dx * easing;

        float dy = targetY - y;
        y += dy * easing;

        PepsiLogo.pepsiIcon(parent, x, y);
    }

    @Override
    public void keyPressed(PApplet parent) {
    }

    @Override
    public void mousePressed(PApplet parent) {

    }

}
