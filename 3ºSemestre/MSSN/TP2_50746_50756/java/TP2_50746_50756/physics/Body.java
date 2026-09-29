package physics;

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;


public class Body extends Mover {

    private int color;
    private static double G = 6.67e-11; //constante Gravitacional

    private String name;


    public Body(PVector pos, PVector vel, float mass, float radius, int color) {
        super(pos, vel, mass, radius);
        this.color = color;
        this.name=null;
    }

    /***
     * Calcula e retorna o vetor de atração gravitacional entre o corpo e outro objeto Mover
     * @param m
     * @return Vetor de atração gravitacional
     */
    public PVector attraction(Mover m){
        PVector r = PVector.sub(pos, m.pos);
        float dist = r.mag();
        float strength = (float) ((G * mass * m.mass) / Math.pow(dist, 2));
        return r.normalize().mult(strength);
    }


    public void display(PApplet p, SubPlot plt) {
        p.pushStyle();
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] r = plt.getDimInPixel(radius, radius);

        // Display the circle
        p.noStroke();
        p.fill(this.color);
        p.circle(pp[0], pp[1], 2 * r[0]);

        // Check if the mouse is over the circle
        if (isOver(p, plt)) {
            // Display information when the mouse is over the circle
            displayInfo(p, plt);
        }

        p.popStyle();
    }

    public void setBody(PVector pos, PVector vel) {
        setPos(pos);
        setVel(vel);
    }


    public void setName(String name) {
        this.name = name;
    }

    public static double getG() {
        return G;
    }

    public static void setG(double g) {
        G = g;
    }

    public static void resetG() {
        G = 6.67e-11;
    }

    /***
     * Exibe as informações completas do corpo.
     *
     * @param p
     * @param plt
     */
    private void displayInfo(PApplet p, SubPlot plt) {
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] r = plt.getDimInPixel(radius, radius);
        p.pushStyle();
        p.fill(255);
        if (name!=null){
            p.text("Nome: " + name, pp[0] + 10, pp[1] - r[1] + 20);
            p.text("Posição: (" + pos.x + ", " + pos.y + ")", pp[0] + 10, pp[1] - r[1] + 40);
            p.text("Velocidade: (" + vel.x + ", " + vel.y + ")", pp[0] + 10, pp[1] - r[1] + 60);
            p.text("Massa: " + mass, pp[0] + 10, pp[1] - r[1] + 80);
        }
        p.popStyle();
    }

    /***
     * Verifica se o rato está sobre o corpo.
     *
     * @param p
     * @param plt
     * @return {@code true} se o rato tem rato por cima, caso contrário retorna {@code  false}
     */
    public boolean isOver(PApplet p, SubPlot plt) {
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] r = plt.getDimInPixel(radius, radius);

        return (p.dist(pp[0], pp[1], p.mouseX, p.mouseY) < r[0]);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
