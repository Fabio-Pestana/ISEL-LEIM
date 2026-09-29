package ecosystem;

import org.omg.PortableInterceptor.SUCCESSFUL;
import physics.HunterParticleSystem;
import physics.ParticleSystem;
import physics.ParticleSystemControl;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import tools.SubPlot;

public class Hunter extends Animal{

    private PApplet parent;
    private SubPlot plt;
    protected PShape imgHunter, imgStone, imgPlant;

    private HunterParticleSystem particleSystem;

    private boolean activatePs;

    /**
     * Construtor da classe Hunter.
     * @param pos    Posicao inicial do cacador.
     * @param mass   Massa do cacador.
     * @param radius Raio do cacador.
     * @param color  Cor do cacador.
     * @param p      Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt    Referencia ao subplot que define as dimensoes e a posicao do cacador.
     */
    protected Hunter(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
        super(pos, mass, radius, color, p, plt);
        this.parent = p;
        this.plt = plt;
        this.imgHunter = p.loadShape(WorldConstants.HUNTER_SRC);
        this.imgStone = p.loadShape(WorldConstants.HUNTER_STONE_SRC);
        this.imgPlant = p.loadShape(WorldConstants.HUNTER_PLANT_SRC);
        this.img = imgHunter;
        energy = WorldConstants.INI_HUNTER_ENERGY;
        particleSystem = new HunterParticleSystem(new PVector(), new PVector(), mass, p);
        activatePs = false;
    }

    /**
     * O caçador nao se reproduz.
     * @param mutate Indica se o novo individuo deve sofrer mutacao genetica.
     * @return Retorna sempre null, ja que o caçador nao se reproduz.
     */
    @Override
    public Animal reproduce(boolean mutate) {
        return null;
    }

    /**
     * O caçador ganha energia ao cacar predadores e presas, sendo que pode morrer ao cacar leoes.
     * @param t Terreno em que o caçador esta.
     */
    @Override
    public void eat(Terrain t) {
        Animal target = this.getTarget();

        if (target != null) {
            if (!(target.isDead()))
            {
                resetImg();
                float dist = PVector.dist(pos, target.getPos());
                if (dist < 2 * getRadius())
                {
                    double probability = Math.random();

                    if (target instanceof Predator && probability > WorldConstants.KILL_LION_PROBABILITY){
                        energy += WorldConstants.ENERGY_FROM_LION;
                        this.getTarget().setDead(true);
                    }else if (target instanceof Predator && probability <= WorldConstants.KILL_LION_PROBABILITY){
                        setActivatePs(); //predator mata o cacador
                        this.getTarget().addEnergy(WorldConstants.ENERGY_LION_HUNTER);
                    }

                    if (target instanceof Prey && probability > WorldConstants.KILL_ZEBRA_PROBABILITY){
                        energy += WorldConstants.ENERGY_FROM_PREY;
                        this.getTarget().setDead(true);
                    }
                }
            }
        }else {
            camouflage(t);
        }
    }

    /**
     * Move o sistema de particulas associado ao cacador.
     * @param dt Variacao de tempo.
     */
    public void movePs(float dt){
        if (activatePs) {
            particleSystem.move(dt);
            if (particleSystem.isDead()){
                setDead(true);
            }
        }
    }

    /**
     * Exibe o caçador ou o sistema de particulas, dependendo se o cacador esta morto ou vivo.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do cacador.
     */
    @Override
    public void display(PApplet p, SubPlot plt) {
        if (activatePs){
            particleSystem.display(p, plt);
        }else {
            p.pushMatrix(); // guarda sistema de coordenadas
            float[] pp = plt.getPixelCoord(pos.x, pos.y);
            p.shape(this.img, pp[0] - 15f, pp[1] - 15f, 30f, 30f); // Draw the image
            p.popMatrix();
        }
    }

    /**
     * Ativa o sistema de particulas quando o cacador morre.
     * @return retorna sempre false.
     */
    @Override
    public boolean die(){
        boolean aux = energy < 0;
        if (aux)
            setActivatePs();

        return false; // para nao ser removido na populacao mas ativar o particle system
    }

    /**
     * Ativa o sistema de particulas associado ao cacador.
     */
    public void setActivatePs(){
        activatePs = true;
        particleSystem.setPos(pos);
    }

    public boolean getActivatePs(){return this.activatePs;}

    /**
     * Camufla o cacador com base no estado do terreno.
     * @param t Terreno em que o caçador esta.
     */
    public void camouflage(Terrain t){
        Patch patch = (Patch) t.world2Cell(pos.x, pos.y);
        if (patch.getState() == WorldConstants.PatchType.OBSTACLE.ordinal()){
            this.img = imgStone;
        } else if (patch.getState() == WorldConstants.PatchType.FOOD.ordinal()) {
            this.img = imgPlant;
        }
    }

    public void resetImg(){
        this.img = imgHunter;
    }
}
