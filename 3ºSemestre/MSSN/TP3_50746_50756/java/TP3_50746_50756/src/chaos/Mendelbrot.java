package chaos;
/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;
import tools.SubPlot;
import tools.Complex;

public class Mendelbrot {
    private int numIter;
    private int x0;
    private int y0;
    private int dimx;
    private int dimy;

    public Mendelbrot(int niter, SubPlot plt) {
        this.numIter = niter;
        float[] bb = plt.getBoundingBox();
        x0 = (int) bb[0];
        y0 = (int) bb[1];
        dimx = (int) bb[2];
        dimy = (int) bb[3];
    }

    public void display(PApplet p, SubPlot plt){
        //Para um melhor tempo de processamento
        p.loadPixels();
        for (int xx = x0; xx<x0+dimx; xx++){
            for (int yy = y0; yy<y0+dimy; yy++){
                double[] cc = plt.getWorldCoord(xx, yy);
                Complex c = new Complex(cc);
                Complex x = new Complex();
                int i;
                for (i = 0; i< numIter; i++){
                    x.mult(x).add(c);
                    if(x.norm()>2){
                        break;
                    }
                }
                // x ? se x : se !x
                p.pixels[yy*p.width+xx] = p.color((i==numIter) ? p.color(0) : p.color((i%16)*16));
            }
        }
        p.updatePixels();
    }
}
