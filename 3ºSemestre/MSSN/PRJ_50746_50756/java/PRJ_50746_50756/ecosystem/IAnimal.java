package ecosystem;

public interface IAnimal {
    public Animal reproduce(boolean mutate);
    public void eat(Terrain t);
    public void energy_consumption(float dt, Terrain t);
    public boolean die();
}
