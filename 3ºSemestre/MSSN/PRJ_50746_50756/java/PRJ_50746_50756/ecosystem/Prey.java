package ecosystem;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import tools.SubPlot;

public class Prey extends Animal{
    private PApplet parent;
    private SubPlot plt;

    /**
     * Construtor da classe Prey.
     * @param pos    Posicao inicial da presa.
     * @param mass   Massa da presa.
     * @param radius Raio da presa.
     * @param color  Cor da presa.
     * @param p      Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt    Referencia ao subplot que define as dimensoes e a posicao da presa.
     */
    protected Prey(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
        super(pos, mass, radius, color, p, plt);
        this.parent = p;
        this.plt = plt;
        this.img = p.loadShape(WorldConstants.PREY_SRC);
        energy = WorldConstants.INI_PREY_ENERGY;
    }

    /**
     * Construtor da classe Prey utilizado para clonar uma presa existente.
     * @param prey   Presa a ser clonada.
     * @param mutate Indica se o DNA deve sofrer mutacao durante a clonagem.
     * @param p      Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt    Referencia ao subplot que define as dimensoes e a posicao da presa.
     */
    protected Prey(Prey prey, boolean mutate, PApplet p, SubPlot plt) {
        super(prey, mutate, p, plt);
        this.parent = p;
        this.plt = plt;
        this.img = p.loadShape(WorldConstants.PREY_SRC);
        energy = WorldConstants.INI_PREY_ENERGY;
    }

    /**
     * Faz a presa se alimentar, consumindo energia de uma celula do terreno (patch) se estiver disponivel.
     * @param t Terreno em que a presa esta.
     */
    @Override
    public void eat(Terrain t) {
        Patch patch = (Patch) t.world2Cell(pos.x, pos.y);
        if (patch.getState() == WorldConstants.PatchType.FOOD.ordinal()){
            energy += WorldConstants.ENERGY_FROM_PLANT;
            patch.setFertile();
        }
    }

    /**
     * Reproduz a presa, gerando um novo individuo se ela tiver energia suficiente.
     * @param mutate Indica se o novo individuo deve sofrer mutacao genetica.
     * @return Novo individuo gerado pela reproducao, ou null se a reproducao nao ocorrer.
     */
    @Override
    public Animal reproduce(boolean mutate) {
        Animal child = null;
        if (energy > WorldConstants.PREY_ENERGY_TO_REPRODUCE){
            energy -= WorldConstants.INI_PREY_ENERGY;
            child = new Prey(this, mutate, parent, plt);
            if (mutate){
                child.mutateBehaviors();
            }
        }
        return child;
    }
}
