package physics;

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

public class Mover {
    protected PVector pos; //posição
    protected PVector vel; //velocidade
    protected PVector acc; //aceleração
    protected float mass; //massa

    /**
     * Construtor da classe Mover
     * @param pos  Vetor representando a posição inicial do mover.
     * @param vel  Vetor representando a velocidade inicial do mover.
     * @param mass A massa do mover.
     */
    public Mover(PVector pos, PVector vel, float mass) {
        this.pos = pos.copy();
        this.vel = vel;
        this.mass = mass;
        this.acc = new PVector();
    }

    /***
     * Funcao para aplicar um vetor de Força no corpo/objeto
     * @param f Vetor que representa a Força
     */
    public void applyForce(PVector f) {
        acc.add(PVector.div(f, mass));
    }

    /**
     * Move o objeto com base na aceleracao e no intervalo de tempo fornecidos.
     * @param dt Intervalo de tempo para o movimento.
     */
    public void move(float dt){
        acc.mult(dt);
        vel.add(acc);
        pos.add(PVector.mult(vel,dt));
        acc.mult(0);
    }

    public void display(PApplet p, SubPlot plt){};

    /***
     * Retorna o vetor da posição
     * @return pos - Vetor da posição
     */
    public PVector getPos() {
        return pos;
    }

    /***
     * Altera o vetor da posição
     * @param pos Vetor com o valor da posição pretendida
     */
    public void setPos(PVector pos) {
        this.pos = pos;
    }

    /***
     * Retorna o vetor da velocidade
     * @return vel - Vetor da velocidade
     */
    public PVector getVel() {
        return vel;
    }

    /***
     * Altera o vetor da velociddde
     * @param vel Vetor com o valor da velocidade pretendida
     */
    public void setVel(PVector vel) {
        this.vel = vel;
    }
}
