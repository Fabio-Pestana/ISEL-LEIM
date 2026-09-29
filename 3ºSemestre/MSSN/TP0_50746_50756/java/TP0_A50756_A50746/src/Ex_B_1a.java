/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;

public class Ex_B_1a implements IProcessingApp {
    @Override
    public void setup(PApplet parent) {
        parent.background(0);
    }

    @Override
    public void draw(PApplet parent, float dt) {
        PepsiLogo.pepsiIcon(parent, 0, 0);
    }

    @Override
    public void keyPressed(PApplet parent) {
    }

    @Override
    public void mousePressed(PApplet parent) {
    }

}
