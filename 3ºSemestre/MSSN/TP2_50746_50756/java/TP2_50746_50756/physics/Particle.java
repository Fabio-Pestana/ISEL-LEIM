package physics;

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.Arrays;

public class Particle  extends Mover{

    private float lifespan;
    private int color;
    private float timer;

    public Particle(PVector pos, PVector vel, float radius, int color, float lifespan) {
        super(pos, vel, 0f, radius);
        this.color = color;
        this.lifespan = lifespan;
        timer = 0;
    }

    public void move(float dt){
        super.move(dt);
        timer += dt;
    }

    public boolean isDead(){
        return timer > lifespan;
    }

    public void display(PApplet p, SubPlot plt){
        p.pushStyle(); //Salvaguarda o estilo do Processing, o estado do renderizador

        //Fazer o alpha variar com o passar do tempo
        //Alpha atrubui um certo nivel de transparência
        float alpha = PApplet.map(timer, 0, lifespan, 255, 0);

        p.fill(color, alpha);

        float [] pp = plt.getPixelCoord(pos.x, pos.y);
        float [] r = plt.getDimInPixel(radius, radius);

        p.noStroke();

        // x,y,diametro
        p.circle(pp[0], pp[1],2*r[0]);

        p.popStyle(); //Salvaguarda o estilo do Processing, o estado do renderizador
    }
}
