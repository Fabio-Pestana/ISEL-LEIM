package physics;

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.Arrays;

public class Particle  extends Mover{

    private float lifespan;
    private int color;
    private float timer;
    protected float radius;

    /**
     * Construtor da classe Particle
     * @param pos      Vetor representando a posição inicial da particula.
     * @param vel      Vetor representando a velocidade inicial da particula.
     * @param radius   O raio da particula.
     * @param color    A cor da particula.
     * @param lifespan O tempo de vida da particula.
     */
    public Particle(PVector pos, PVector vel, float radius, int color, float lifespan) {
        super(pos, vel, 0f);
        this.color = color;
        this.lifespan = lifespan;
        timer = 0;
        this.radius = radius;
    }

    /**
     * Atualiza o movimento da particula com base no intervalo de tempo fornecido.
     * @param dt Intervalo de tempo para o movimento.
     */
    public void move(float dt){
        super.move(dt);
        timer += dt;
    }

    /**
     * Verifica se a particula está morta (excedeu seu tempo de vida).
     * @return true se a partícula estiver morta, false caso contrário.
     */
    public boolean isDead(){
        return timer > lifespan;
    }

    /**
     * Exibe a representacao grafica da partícula.
     * @param p   Objeto PApplet usado para desenhar.
     * @param plt Objeto SubPlot usado para mapear as coordenadas.
     */
    public void display(PApplet p, SubPlot plt){
        p.pushStyle(); //Salvaguarda o estilo do Processing, o estado do renderizador

        //Fazer o alpha variar com o passar do tempo
        //Alpha atrubui um certo nivel de transparência
        float alpha = PApplet.map(timer, 0, lifespan, 255, 0);

        p.fill(color, alpha);

        float [] pp = plt.getPixelCoord(pos.x, pos.y);
        float [] r = plt.getVectorCoord(radius, radius);

        p.noStroke();

        // x,y,diametro
        p.circle(pp[0], pp[1],2*r[0]);

        p.popStyle(); //Salvaguarda o estilo do Processing, o estado do renderizador
    }
}
