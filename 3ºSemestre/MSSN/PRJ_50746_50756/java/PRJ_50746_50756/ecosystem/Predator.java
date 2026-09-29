package ecosystem;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import tools.SubPlot;

public class Predator extends Animal{

    private PApplet parent;
    private SubPlot plt;

    /**
     * Construtor da classe Predator.
     * @param pos    Posicao inicial do predador.
     * @param mass   Massa do predador.
     * @param radius Raio do predador.
     * @param color  Cor do predador.
     * @param p      Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt    Referencia ao subplot que define as dimensoes e a posicao do predador.
     */
    protected Predator(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
        super(pos, mass, radius, color, p, plt);
        this.parent = p;
        this.plt = plt;
        if (Math.random()>WorldConstants.PREDATOR_GENDER_PROBABILITY){
            this.img = p.loadShape(WorldConstants.PREDATOR_MALE_SRC);
        }else {
            this.img = p.loadShape(WorldConstants.PREDATOR_FEMALE_SRC);
        }
        energy = WorldConstants.INI_PREDATOR_ENERGY;
    }

    /**
     * Construtor da classe Predator utilizado para clonar um predador existente.
     * @param a      Predador a ser clonado.
     * @param mutate Indica se o DNA deve sofrer mutacao durante a clonagem.
     * @param p      Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt    Referencia ao subplot que define as dimensoes e a posicao do predador.
     */
    protected Predator(Animal a, boolean mutate, PApplet p, SubPlot plt) {
        super(a, mutate, p, plt);
        this.parent = p;
        this.plt = plt;
        if (Math.random()>WorldConstants.PREDATOR_GENDER_PROBABILITY){
            this.img = p.loadShape(WorldConstants.PREDATOR_MALE_SRC);
        }else {
            this.img = p.loadShape(WorldConstants.PREDATOR_FEMALE_SRC);
        }
        energy = WorldConstants.INI_PREDATOR_ENERGY;
    }

    /**
     * O predador ganha energia ao se alimentar de presas.
     * @param terrain Terreno em que o predador está.
     */
    @Override
    public void eat(Terrain terrain){
        if (getTarget() != null) {
            if (!(getTarget().isDead())) {
                float dist = PVector.dist(pos, getTarget().getPos());
                if (dist < 2.2 * getRadius()) {
                    energy += WorldConstants.ENERGY_FROM_PREY;
                    getTarget().setDead(true);
                }
            }
        }
    }

    /**
     * O predador se reproduz se tiver energia suficiente.
     * @param mutate Indica se o novo individuo deve sofrer mutacao genetica.
     * @return Novo individuo gerado pela reproducao, ou null se a reproducao nao ocorrer.
     */
    @Override
    public Animal reproduce(boolean mutate) {
        Animal child = null;
        if (energy > WorldConstants.PREDATOR_ENERGY_TO_REPRODUCE){
            energy -= WorldConstants.INI_PREDATOR_ENERGY;
            child = new Predator(this, mutate, parent, plt);
            if (mutate){
                child.mutateBehaviors();
            }
        }
        return child;
    }
}
