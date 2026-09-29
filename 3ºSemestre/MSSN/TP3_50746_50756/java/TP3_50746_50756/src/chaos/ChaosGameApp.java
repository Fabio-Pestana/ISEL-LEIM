package chaos;
/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;
import setup.IProcessingApp;

public class ChaosGameApp implements IProcessingApp {

    //ponto A -> 0
    float ax;
    float ay;

    //ponto B -> 1
    float bx;
    float by;

    //potno C -> 2
    float cx;
    float cy;

    // X e Y
    float x;
    float y;

    //R
    int r;

    @Override
    public void setup(PApplet p) {
        float margin = 20f;

        // atirar os pontos fixos aleatóriamente
        ax = p.width/2;

        ay = margin;
        bx = margin;
        by = p.height - margin;
        cx = p.width - margin;
        cy = p.height - margin;

        x = p.random(margin, p.width - margin);
        y = p.random(margin, p.height - margin);

        p.background(0);
        p.stroke(255);
        p.strokeWeight(8);

        p.point(ax, ay);
        p.point(bx, by);
        p.point(cx, cy);

    }

    @Override
    public void draw(PApplet p, float dt) {
        //100 pontos por vez
        for (int i =0; i<100; i++){
            p.strokeWeight(2);
            p.point(x,y);

            r = (int) Math.floor(p.random(3));

            if (r == 0){
                p.stroke(255,0,0, 100);
                x = p.lerp(x, ax, 0.5f);
                y = p.lerp(y, ay, 0.5f);
            }else if (r == 1){
                p.stroke(0,255,0, 100);
                x = p.lerp(x, bx, 0.5f);
                y = p.lerp(y, by, 0.5f);
            }else if (r == 2) {
                p.stroke(0,100,255, 100);
                x = p.lerp(x, cx, 0.5f);
                y = p.lerp(y, cy, 0.5f);
            }
        }

    }

    @Override
    public void keyPressed(PApplet p) {

    }

    @Override
    public void mousePressed(PApplet p) {

    }

    @Override
    public void mouseReleased(PApplet p) {

    }

    @Override
    public void mouseDragged(PApplet p) {

    }
}
