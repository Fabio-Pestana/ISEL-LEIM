package ecosystem;

import aa.Eye;
import aa.Seek;
import physics.Body;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Player extends Animal {

    private PApplet parent;
    private SubPlot plt;
    private Body b;
    private int lifes;
    private boolean kill;

    /**
     * Construtor da classe Player.
     * @param pos    Posicao inicial do jogador.
     * @param mass   Massa do jogador.
     * @param radius Raio do jogador.
     * @param color  Cor do jogador.
     * @param p      Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt    Referencia ao subplot que define as dimensoes e a posicao do jogador.
     */
    protected Player(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
        super(pos, mass, radius, color, p, plt);
        this.parent = p;
        this.plt = plt;
        this.img = p.loadShape(WorldConstants.PLAYER_SRC);
        energy = WorldConstants.INI_PLAYER_ENERGY;

        getDna().setMaxSpeed(WorldConstants.PLAYER_SPEED);

        this.addBehavior(new Seek(1f));
        b = new Body(pos.copy(), new PVector(), 1f, 0f, p.color(0));
        ArrayList<Body> allTrackingBodies = new ArrayList<Body>();
        allTrackingBodies.add(b);
        this.setEye(new Eye(this, allTrackingBodies));
        kill = false;
        lifes = 3;
    }

    /**
     * O jogador nao se reproduz.
     * @param mutate Indica se o novo individuo deve sofrer mutacao genetica.
     * @return Retorna sempre null, ja que o jogador nao se reproduz.
     */
    @Override
    public Animal reproduce(boolean mutate) {
        return null;
    }

    /**
     * O jogador nao se alimenta atraves este metodo.
     * @param t Terreno em que o jogador esta.
     */
    @Override
    public void eat(Terrain t) {}

    /**
     * O jogador mata outros animais quando esta no modo de ataque (kill = true).
     * @param allAnimals Lista de todos os animais no ecossistema.
     * @return Lista atualizada de animais apos os ataques do jogador.
     */
    public List<Animal> eat(List<Animal> allAnimals) {
        if (kill && allAnimals.size() > 0) {
            setKeyPressed(false);
            List<Animal> animals = new ArrayList<Animal>();
            for (Animal b : allAnimals){
                if (!(b instanceof Player)){
                    animals.add(b);
                }
            }

            Animal body = animals.get(0);

            //distancia entre o player e o primeiro animal da lista
            float dist = PVector.sub(this.getPos(), body.getPos()).mag();

            for (Animal b : animals){
                float distAux =  PVector.sub(this.getPos(),b.getPos()).mag();

                if (distAux < dist && !(b instanceof Player)){
                    //se a distacia for menor atualiza-se a distancia e o body
                    dist = distAux;
                    body = b;
                }
            }

            if (dist < 2 * getRadius() )
            {
                if (body instanceof Hunter){
                    energy += WorldConstants.ENERGY_FROM_HUNTER;
                    ((Hunter)body).setActivatePs();
                }

                if (body instanceof Predator){
                    energy += WorldConstants.ENERGY_FROM_LION;
                    body.setDead(true);
                    lifes--;
                }

                if (body instanceof Prey){
                    energy += WorldConstants.ENERGY_FROM_PREY;
                    body.setDead(true);
                    lifes--;
                }
                animals.add(this);
                return animals;
            }
        }
        return allAnimals;
    }

    /**
     * Atualiza a posicao do corpo associado ao jogador com base nas coordenadas do mouse.
     */
    public void playerMouse(){
        double[] w = plt.getWorldCoord(parent.mouseX, parent.mouseY);
        b.setPos(new PVector((float) w[0], (float) w[1]));
    }

    /**
     * Define se o jogador está no modo de matar.
     * @param t true se o jogador está no modo de matar, false caso contrário.
     */
    public void setKeyPressed(boolean t){
        this.kill = t;
    }

    public int getLifes(){
        return this.lifes;
    }
}