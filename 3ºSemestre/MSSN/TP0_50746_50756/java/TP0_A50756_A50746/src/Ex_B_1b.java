/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;

public class Ex_B_1b implements IProcessingApp {

    private int[] posXinc = {0,450, 900, 900, 450, 0,  0, 450, 900};
    private int[] posYinc = {0,0, 0, 325, 325, 325,  650, 650, 650};
    private int i=0;
    private float lastUpdateTime;

    @Override
    public void setup(PApplet parent) {
        parent.background(0);
        lastUpdateTime = parent.millis();;
    }

    @Override
    public void draw(PApplet parent, float dt) {

        int intervaloEntrePosicoes = 2000; //milissegundos

        float now=parent.millis();
        float diffTime = now - lastUpdateTime;
        if (diffTime > intervaloEntrePosicoes){
            PepsiLogo.pepsiIcon(parent, posXinc[i], posYinc[i]);
            //System.out.println(diffTime);
            lastUpdateTime = parent.millis(); //reset da variavel temporal
            i++;
            if (i==posXinc.length) i = 0; //volta a posicao inicial quando chega a ultima
        }

    }

    @Override
    public void keyPressed(PApplet parent) {
    }

    @Override
    public void mousePressed(PApplet parent) {
    }

}

