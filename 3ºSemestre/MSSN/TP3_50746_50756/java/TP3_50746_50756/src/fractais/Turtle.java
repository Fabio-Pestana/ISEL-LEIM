package fractais;

/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Turtle {
    private float len;
    private float angle;

    private int color;

    private int counter;

    private boolean fruitOrLeave;

    private List<Float> prob;

    public Turtle(float len, float angle) {
        this.len = len;
        this.angle = angle;
        this.color = 0;
        this.fruitOrLeave = false;
        this.counter=0;
        this.prob = new ArrayList<Float>();
    }

    public void setPose(PVector pos , float orientation, SubPlot plt, PApplet p){
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        p.translate(pp[0], pp[1]);
        p.rotate(-orientation);
    }

    public void scaling(float s){
        this.len = this.len * s;
    }

    public void render(LSystem ls, SubPlot plt, PApplet p) {
        p.stroke(0);

        float[] lenInPix = plt.getVectorCoord(len, len);

        int aux = Fcounter(ls);
        int j = 0;

        //atualizar o numero de F´s
        if (aux != this.counter){
            for (int k = 0; k < aux-this.counter ; k++) {
                float pLeaveFruit = p.random(100);
                this.prob.add(pLeaveFruit);
            }
            this.counter = aux;
        }

        for (int i = 0; i < ls.getSequence().length(); i++) {
            char c = ls.getSequence().charAt(i);
            if (c == 'F') {
                p.line(0, 0, lenInPix[0], 0);
                if (fruitOrLeave && j<prob.size()) {
                    drawLeaveFruit(p, prob.get(j), lenInPix[0]); //desenhar a fruta, folha ou nada
                    j++;
                }
                p.translate(lenInPix[0], 0);
            } else if (c == 'G') {
                p.line(0, 0, lenInPix[0], 0);
                p.translate(lenInPix[0], 0);
            } else if (c == 'f') p.translate(lenInPix[0], 0);
            else if (c == '+') p.rotate(this.angle);
            else if (c == '-') p.rotate(-1 * this.angle);
            else if (c == '[') p.pushMatrix();
            else if (c == ']') p.popMatrix();
        }
    }

    public int Fcounter(LSystem ls){
        int aux = 0;
        for (int i = 0; i < ls.getSequence().length(); i++) {
            char c = ls.getSequence().charAt(i);
            if (c == 'F') aux++;
        }
        return aux;
    }

    public void drawLeaveFruit(PApplet p, float prob, float x){
        p.pushStyle();
        if (prob>25 && prob<50){ // probabilidade para folha
            this.color = p.color(0, 255, 0);
            p.fill(this.color);
            p.ellipse(x, 0f,3 , 6);
        } else if (prob<=25 && prob>0){ // probabilidade para fruta
            this.color = p.color(255, 0, 0);
            p.fill(this.color);
            p.circle(x, 0f, 5);
        }
        p.popStyle();
    }


    public boolean isFruitOrLeave() {
        return fruitOrLeave;
    }

    public void setFruitOrLeave(boolean fruitOrLeave) {
        this.fruitOrLeave = fruitOrLeave;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getLen() {
        return len;
    }

    public void setLen(float len) {
        this.len = len;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
