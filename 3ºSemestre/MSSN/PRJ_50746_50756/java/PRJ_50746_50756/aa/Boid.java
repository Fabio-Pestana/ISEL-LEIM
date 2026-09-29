package aa;

import physics.Body;
import processing.core.*;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Boid extends Body {

    private SubPlot plt;
    public PShape shape;

    protected List<Behavior> behaviors;
    protected DNA adn;
    protected Eye eye;
    protected float phiWander;
    private float sumWeights;


    /**
     * Construtor da classe Boid.
     * @param pos    Posicao inicial do Boid.
     * @param mass   Massa do Boid.
     * @param radius Raio do Boid.
     * @param color  Cor do Boid.
     * @param p      Instancia de PApplet para renderizacao grafica.
     * @param plt    SubPlot associado ao Boid.
     */
    protected Boid(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
        super(pos, new PVector(), mass, radius, color);

        adn = new DNA();
        behaviors = new ArrayList<Behavior>();
        this.plt = plt;
        setShape(p, plt, color);
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public void setEye(Eye eye){
        this.eye = eye;
    }

    public Eye getEye(){return this.eye;}

    public void setDna(DNA adn){
        this.adn = adn;
    }

    public DNA getDna(){return this.adn;}

    /**
     * Define a forma (shape) grafica do Boid.
     * @param p     Instancia de PApplet para renderizacao grafica.
     * @param plt   SubPlot associado ao Boid.
     * @param color Cor do Boid.
     */
    public void setShape(PApplet p, SubPlot plt, int color) {
        float[] rr = plt.getVectorCoord(radius, radius);
        shape = p.createShape();
        shape.beginShape();
        shape.vertex(-rr[0], rr[0] / 2);
        shape.vertex(rr[0], 0);
        shape.vertex(-rr[0], -rr[0] / 2);
        shape.vertex(-rr[0] / 2, 0);
        shape.fill(color);
        shape.endShape(PConstants.CLOSE);
    }

    /**
     * Atualiza a soma dos pesos dos comportamentos associados ao Boid.
     */
    private void updateSumWeights(){
        sumWeights = 0;
        for(Behavior behavior : behaviors){
            sumWeights += behavior.getWeight();
        }
    }

    /**
     * Adiciona um comportamento a lista de comportamentos do Boid.
     * @param behavior Comportamento a ser adicionado.
     */
    public void addBehavior(Behavior behavior){
        behaviors.add(behavior);
        updateSumWeights();
    }

    /**
     * Remove um comportamento da lista de comportamentos do Boid.
     * @param behavior Comportamento a ser removido.
     */
    public void removeBehavior(Behavior behavior){
        if (behaviors.contains(behavior))
            behaviors.remove(behavior);
        updateSumWeights();
    }

    /**
     * Remove um comportamento da lista de comportamentos do Boid com base no indice.
     * @param idx Indice do comportamento a ser removido.
     */
    public void removeBehavior(int idx){
        if (behaviors.size()  > idx)
            behaviors.remove(idx);
        updateSumWeights();
    }

    /**
     * Aplica um comportamento especifico ao Boid com base no indice do comportamento.
     * @param i  Indice do comportamento a ser aplicado.
     * @param dt Intervalo de tempo.
     */
    public void applyBehavior(int i, float dt){
        if (eye!=null){
            eye.look();
        }
        Behavior behavior = behaviors.get(i);
        PVector vd = behavior.getDesiredVelocity(this);
        move(dt, vd, plt.getWindow());
    }

    /**
     * Aplica todos os comportamentos associados ao Boid.
     * @param dt Intervalo de tempo.
     */
    public void applyBehaviors(float dt){
        if (eye!=null){
            eye.look();
        }
        PVector vd = new PVector();
        for(Behavior behavior : behaviors){
            PVector vdd = behavior.getDesiredVelocity(this);
            vdd.mult(behavior.getWeight()/sumWeights);
            vd.add(vdd);
        }
        move(dt, vd, plt.getWindow());
    }

    /**
     * Move o Boid com base na velocidade desejada e verifica se esta dentro dos limites da janela.
     * @param dt     Intervalo de tempo.
     * @param vd     Velocidade desejada.
     * @param window  Array com os limites da janela.
     */
    public void move(float dt, PVector vd, double[] window){

        vd.normalize().mult(adn.maxSpeed);
        PVector fs = PVector.sub(vd, vel);
        applyForce(fs.limit(adn.maxForce));
        super.move(dt);

        // Verifica se o boid está fora dos limites da janela

        if (pos.x >= window[1]) {
            pos.x = (float) window[0];
        } else if (pos.x <= window[0]) {
            pos.x = (float) window[1];
        }

        if (pos.y >= window[3]) {
            pos.y = (float) window[2];
        } else if (pos.y <= window[2]) {
            pos.y = (float) window[3];
        }
    }

    /**
     * Exibe o Boid na tela.
     * @param p   Instancia de PApplet para renderizacao grafica.
     * @param plt SubPlot associado ao Boid.
     */
    public void display(PApplet p, SubPlot plt) {
        p.pushMatrix(); //guarda sistema de coordenadas
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        p.translate(pp[0], pp[1]);
        p.rotate(-vel.heading()); // Angulo em radianos que o vetor "vel" faz com o eixo do x
        p.shape(shape);
        p.popMatrix();
    }

    /**
     * Realiza a mutacao dos comportamentos do Boid, especificamente para o comportamento de evitar obstaculos.
     */
    public void mutateBehaviors(){
        for (Behavior b: behaviors){
            if (b instanceof AvoidObstacle){
                b.weight += DNA.random(-0.5f, 0.5f);
                b.weight = Math.max(0, b.weight);
            }
        }
        updateSumWeights();
    }
}
