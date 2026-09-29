package aa;

import processing.core.PVector;

public abstract class Behavior implements IBehavior {

    protected float weight;

    /**
     * Construtor da classe Behavior.
     * @param weight Peso associado ao comportamento.
     */
    public Behavior(float weight){
        this.weight = weight;
    }

    @Override
    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public float getWeight() {
        return weight;
    }
}
