/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;

public class Ex_B_1c implements IProcessingApp
{
    private int[] posXinc = {0,450, 900, 900, 450, 0,  0, 450, 900};
    private int[] posYinc = {0,0, 0, 325, 325, 325,  650, 650, 650};
    private int i=0;
    private float lastUpdateTime;
    private double easing = 0.05;
    private double x;
    private double y;
    @Override
    public void setup(PApplet parent) {
        parent.background(0);
        lastUpdateTime = parent.millis();
    }

    @Override
    public void draw(PApplet parent, float dt) {

        int intervaloEntrePosicoes = 2000; //milissegundos

        double auxX = posXinc[i] - x;
        x = x+auxX*easing;

        double auxY = posYinc[i] - y;
        y = y+auxY*easing;

        PepsiLogo.pepsiIcon(parent, (float)x, (float)y);

        float now=parent.millis();
        float diffTime = now - lastUpdateTime;

        if (diffTime > intervaloEntrePosicoes){
            //System.out.println(diffTime);
            i++;
            lastUpdateTime = parent.millis(); //reset da variavel temporal
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
