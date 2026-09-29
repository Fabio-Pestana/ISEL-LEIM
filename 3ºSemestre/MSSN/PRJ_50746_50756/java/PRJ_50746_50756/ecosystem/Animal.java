package ecosystem;

import aa.DNA;
import aa.Behavior;
import aa.Boid;
import aa.Eye;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import tools.SubPlot;

public abstract class Animal extends Boid implements IAnimal{

    private boolean dead;

    protected float energy;

    private Animal target;

    protected PShape img;

    /**
     * Construtor da classe Animal.
     * @param pos    Posição inicial do animal.
     * @param mass   Massa do animal.
     * @param radius Raio do animal.
     * @param color  Cor do animal.
     * @param p      Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt    Referencia ao subplot que define as dimensoes e a posicao do animal.
     */
    protected Animal(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
        super(pos, mass, radius, color, p, plt);
        dead = false;
    }

    /**
     * Construtor da classe Animal utilizado para clonar um animal existente.
     * @param a      Animal a ser clonado.
     * @param mutate Indica se o DNA deve sofrer mutacao durante a clonagem.
     * @param p      Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt    Referencia ao subplot que define as dimensoes e a posicao do animal.
     */
    protected Animal (Animal a, boolean mutate, PApplet p, SubPlot plt){
        super(a.pos, a.mass, a.radius, a.getColor(), p, plt);

        for (Behavior b: a.behaviors){
            this.addBehavior(b);
        }
        if (a.eye != null){
            eye = new Eye(this, a.eye);
        }

        adn = new DNA(a.adn, mutate);
        dead = false;
    }

    /**
     * Verifica se o animal morreu.
     * @return true se o animal esta morto, false caso contrario.
     */
    @Override
   public boolean die(){
        return energy < 0;
   }

    /**
     * Calcula o consumo de energia do animal com base no tempo decorrido e no ambiente (terreno) em que se encontra.
     * @param dt Tempo decorrido desde a ultima atualizacao.
     * @param t  Terreno em que o animal esta.
     */
    @Override
    public void energy_consumption(float dt, Terrain t){
        energy -= dt; //metabolismo
        energy -= mass * Math.pow(vel.mag(), 2) * dt;
        Patch patch = (Patch) t.world2Cell(pos.x, pos.y);
        if (patch.getState() == WorldConstants.PatchType.OBSTACLE.ordinal()){
            if (this instanceof Predator){
                energy -= WorldConstants.PREDATOR_OBSTACLE_EFFECT*dt;
            } else if (this instanceof Prey){
                energy -= WorldConstants.PREY_OBSTACLE_EFFECT*dt;
            }
            //Cacador nao e prejudicado por estar em obstaculos
        }
    }

    /**
     * Exibe a representacao grafica do animal.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do animal.
     */
    @Override
    public void display(PApplet p, SubPlot plt) {
        p.pushMatrix(); // guarda sistema de coordenadas
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        p.shape(this.img, pp[0] - 15f, pp[1] - 15f, 30f, 30f); // Draw the image
        p.popMatrix();
    }

    public void setDead(boolean b){
        this.dead = b;
    }

    public boolean isDead() {return dead;}

    public Animal getTarget() {
        return target;
    }

    public void setTarget(Animal a) {
        this.target = a;
    }

    public void addEnergy(float add){
        energy += add;
    }
}
