package chaos;
/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;

import java.util.ArrayList;

public class ChaosGamePointsXApp implements IProcessingApp {

    PVector v;
    PVector pAtual;

    float lerpPercent = 0.5f;

    // X e Y
    float x;
    float y;

    int n = 3;
    int resetCount;
    double angulo;
    ArrayList <PVector> points;

    //R
    int r;

    @Override
    public void setup(PApplet p) {
        resetCount = 200;
        reset(p);
    }

    public void reset(PApplet p){
        points = new ArrayList<PVector>();
        // atirar os pontos fixos aleatóriamente
        for(int i = 0; i<n; i++){
            angulo = i * (2*Math.PI)/n;
            v = PVector.fromAngle((float) angulo);
            v.mult(p.width/2);
            v.add(p.width/2, p.height/2);
            points.add(v);
        }

        pAtual = new PVector(p.random(p.width), p.random(p.height));

        p.background(0);
        p.stroke(255);
        p.strokeWeight(5);
        for (PVector pt : points){
            p.point(pt.x, pt.y);
        }

    }

    @Override
    public void draw(PApplet p, float dt) {
        if(p.frameCount % resetCount == 0){
            reset(p);
        }

        //100 pontos por vez
        for (int i =0; i<100; i++) {
            p.strokeWeight(2);
            p.point(pAtual.x, pAtual.y);
            p.stroke(255, 0, 0, 100);

            int r = (int) Math.floor(p.random(points.size()));
            pAtual.x = PApplet.lerp(pAtual.x, points.get(r).x, lerpPercent);
            pAtual.y = PApplet.lerp(pAtual.y, points.get(r).y, lerpPercent);
        }

    }

    @Override
    public void keyPressed(PApplet p) {

        if (p.key == '3'){
            n = 3;
        }
        if (p.key == '4'){
            n = 4;
        }
        if (p.key == '5'){
            n = 5;
        }
        if (p.key == '6'){
            n = 6;
        }
        if (p.key == '7'){
            n = 7;
        }
        if (p.key == '8'){
            n = 8;
        }
        if (p.key == '9'){
            n = 9;
        }
        if (p.key == '+'){
            resetCount+=200;
            System.out.println(resetCount);
        }
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
