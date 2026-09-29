/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

package dla;

import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class Walker {
    private static final int radius = 5;
    private PVector pos;
    private State state;
    private int type;
    private final double Stickiness = 0.05;

    public int getRadius(){
        return radius;
    }


    public enum State{
        WANDER,
        STOPPED
    }

    // Seed estacionÃ¡ria
    public Walker(int x, int y, int type) {
        this.pos = new PVector(x, y);
        this.state = State.STOPPED;
        this.type=type;
    }

    public Walker(PApplet p, int type) {
        this.pos = new PVector(p.random(p.width) + 5, p.random(p.height) + 5);
        this.state = State.WANDER;
        this.type=type;
    }

    public State getState() {
        return state;
    }

    public void updateState(List<Walker> walkers) {
        if (state == State.STOPPED) return;

        for(Walker w : walkers) {
            if(w.state == State.STOPPED) {
                float dist = PVector.dist(pos, w.pos);
                if(dist < 2 * radius) {
                    double aux = Math.random();
                    if (aux <= Stickiness){
                        state = State.STOPPED;
                        break;
                    }
                }
            }
        }
    }

    public void wander(PApplet p) {
        if (state == State.WANDER) {
            PVector step = PVector.random2D();
            pos.add(step);

            if (this.type==1){
                pos.lerp(new PVector(p.width / 2, p.height / 2), 0.0002f); // ficam atraidos para o centro da janela
            } else if (this.type==2) {
                pos.lerp(new PVector(pos.x, p.height), 0.0008f);  // ficam atraidos para a parte de baixo da janela
            }else {
                //Atraidos para os cantos da janela
                float attractionX, attractionY;

                if (pos.x < p.width / 2) {
                // atraido a esquerda
                attractionX = 0;
                } else {
                    // atraido a direita
                    attractionX = p.width;
                }

                if (pos.y < p.height / 2) {
                    // atraido para a parte de cima da janela
                    attractionY = 0;
                } else {
                    // atraido para a parte debaixo da janela
                    attractionY = p.height;
                }

                PVector target = new PVector(attractionX, attractionY);
                pos.lerp(target, 0.0015f);
            }
            pos.x = PApplet.constrain(pos.x, 0, p.width);
            pos.y = PApplet.constrain(pos.y, 0, p.height);
        }
    }

    public void display(PApplet p) {
        p.circle(pos.x, pos.y, radius * 2);
    }

}