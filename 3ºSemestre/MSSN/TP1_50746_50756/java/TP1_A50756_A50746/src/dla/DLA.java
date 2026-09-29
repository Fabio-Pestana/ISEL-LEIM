/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

package dla;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import setup.IProcessingApp;

public class DLA implements IProcessingApp {

    private static final int NUM_WALKERS = 300;
    private static final int NUM_STEPS_PER_FRAME = 10;
    List<Walker> walkers;

    private final int level=3;

    private final int NUM_MAX=1000;
    private int nStoppedWalker;

    @Override
    public void setup(PApplet p) {
        walkers = new ArrayList<Walker>();

        //NORMAL
        // Seed estacionária
        Walker w = new Walker(p.width / 2, p.height / 2, this.level);
        if (level==1){
            walkers.add(w);
            nStoppedWalker = 1;
        }

        // Restantes partículas
        for(int i = 0; i != NUM_WALKERS; ++i) {
            w = new Walker(p, this.level);
            walkers.add(w);
            w.updateState(walkers);
        }

        if (level==2 || level == 3) {
            // Seed estacionária Linha
            for (int x = 0; x != p.width; x += w.getRadius() * 2) {
                //lado debaixo
                w = new Walker(x, p.height, this.level);
                walkers.add(w);
                w.updateState(walkers);
                //lado de cima
                if (level==3) {
                    w = new Walker(x, 0, this.level);
                    walkers.add(w);
                    w.updateState(walkers);
                }
            }
        }if (level == 3) {
            for (int y = 0; y != p.height; y += w.getRadius() * 2) {
                //lado esquerdo
                w = new Walker(0, y, this.level);
                walkers.add(w);
                w.updateState(walkers);
                //lado direito
                w = new Walker(p.width, y, this.level);
                walkers.add(w);
                w.updateState(walkers);
            }
        }
    }

    @Override
    public void draw(PApplet p, float dt) {
        for (int i = 0; i != NUM_STEPS_PER_FRAME; ++i) {
            p.background(255);
            for (Walker w : walkers) {
                w.updateState(walkers);
                w.wander(p);
            }

            for (Walker w : walkers) {
                if (w.getState() == Walker.State.WANDER) {
                    p.fill(255, 0, 0);
                    w.display(p);
                } else {
                    p.fill(0, 255, 0);
                    w.display(p);
                }
            }
        }

        int aux = updateNumstopped();
        if (nStoppedWalker != aux && walkers.size()!=NUM_MAX) {
            nStoppedWalker = aux;
            Walker wplus = new Walker(p, this.level);
            walkers.add(wplus);
        }
        //System.out.println(walkers.size()+ " " + aux + " " + nStoppedWalker);
    }

    @Override
    public void keyPressed(PApplet parent) {
    }

    @Override
    public void mousePressed(PApplet parent) {

    }

    public int updateNumstopped(){
        int num=0;
        for(Walker w : walkers) {
            if (w.getState()== Walker.State.STOPPED){
                num++;
            }
        }
        return num;
    }
}