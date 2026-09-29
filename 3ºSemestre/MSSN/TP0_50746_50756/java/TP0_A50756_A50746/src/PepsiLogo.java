/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */
import processing.core.PApplet;

public class PepsiLogo {

    /**
     * Método que tem a base do logotipo da Pepsi e tem duas variáveis que alteram a sua posicao na janela.
     * @param parent
     * @param x incrementa um valor nas abcissas.
     * @param y incrementa um valor nas ordenadas.
     */
    public static void pepsiIcon(PApplet parent , float x, float y){
        parent.background(0);
        //parte vermelha
        parent.fill(parent.color(198, 12, 48));
        parent.ellipse(150 + x, 150 + y, 300, 300);

        parent.noStroke();
        parent.fill(255);

        //parte branca
        parent.beginShape();
        parent.vertex(12 + x, 209+ y);
        parent.bezierVertex(150 + x, 140+ y, 212 + x, 120+ y, 247+ x, 35+ y);
        parent.vertex(275+ x, 65+ y);
        parent.bezierVertex(260+ x, 200+ y, 185+ x, 190+ y, 18+ x, 219+ y);
        parent.endShape();
        parent.stroke(255);

        parent.fill(parent.color(0, 0, 200));
        parent.beginShape();
        parent.vertex(275+ x, 65+ y);
        parent.bezierVertex(260+ x, 200+ y, 185+ x, 190+ y, 18+ x, 219+ y);
        parent.vertex(17+ x,219+ y);
        parent.bezierVertex(110+ x, 375+ y, 375+ x,280+ y , 275+ x, 65+ y);
        parent.endShape();

        parent.stroke(255);
    }
}
