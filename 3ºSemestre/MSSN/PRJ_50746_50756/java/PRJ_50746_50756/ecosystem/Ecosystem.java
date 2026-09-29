package ecosystem;

import aa.*;
import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Ecosystem {
    private List<Animal> allAnimals;
    private double[] window;

    private boolean mutate = true;

    /**
     * Construtor da classe Ecosystem.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do ecossistema.
     * @param t   Terreno que define o ambiente do ecossistema.
     */
    public Ecosystem(PApplet p, SubPlot plt, Terrain t){
        window = plt.getWindow();
        allAnimals = new ArrayList<Animal>();
        initializePopulations(p, plt, t);
    }

    /**
     * Atualiza o ecossistema em um intervalo de tempo especificado.
     * @param dt Intervalo de tempo.
     * @param t  Terreno que define o ambiente do ecossistema.
     */
    public void update(float dt, Terrain t){
        move(dt, t);
        eat(t);
        energy_consumption(dt, t);
        reproduce(mutate);
        die();
    }

    /**
     * Move os animais no ecossistema com base em seus comportamentos.
     * Predadores caso tenham um alvo fazem Pursuit (comportamento com maior peso) e os restantes comportamentos.
     * Se nao fazem wander e evitam obstaculos (os restantes comportamentos).
     * Presas caso tenham um alvo fazem Evade (comportamento com maior peso) e os restantes comportamentos.
     * Se nao fazem wander e evitam obstaculos (os restantes comportamentos).
     * Hunters caso tenham um alvo fazem Pursuit se nao ficam parados.
     * Player o seu movimento depende do jogador.
     * @param dt Intervalo de tempo.
     * @param t  Terreno que define o ambiente do ecossistema.
     */
    public void move(float dt, Terrain t){
        for (Animal a : allAnimals){
            if (a instanceof Predator) {
                if (getNumPreys() > 0) {
                    animalNewTarget(a, t);
                    if (a.getTarget() == null){
                        a.removeBehavior(2);
                        animalUpdate(a, t);
                        a.applyBehaviors(dt);
                    }else {
                        if (a.getBehaviors().size() != 3){
                            a.addBehavior(new Pursuit(9));
                        }
                        a.applyBehaviors(dt);
                    }
                } else {
                    //nao ha presas logo vagueia
                    a.removeBehavior(2);
                    a.applyBehaviors(dt);
                }
            }else if (a instanceof Prey){
                if (getNumPredators() > 0) {
                    animalNewTarget(a, t);
                    if (a.getTarget() == null){
                        a.removeBehavior(2);
                        animalUpdate(a, t);
                        a.applyBehaviors(dt);
                    }else {
                        if (a.getBehaviors().size() != 3){
                            a.addBehavior(new Evade(8));
                        }
                        a.applyBehaviors(dt);
                    }
                } else {
                    //nao ha predatores logo vagueia
                    a.removeBehavior(2);
                    a.applyBehaviors(dt);
                }
            }else if (a instanceof Hunter){
                Hunter h = ((Hunter) a);
                if (!h.getActivatePs()) {
                    animalNewTarget(a, t);
                    if (a.getTarget() == null) {
                        a.removeBehavior(0);
                        a.setVel(new PVector());
                        a.applyBehaviors(dt);
                    } else {
                        if (a.getBehaviors().size() != 1) {
                            a.addBehavior(new Pursuit(6));
                        }
                        a.applyBehaviors(dt);
                    }
                }else {
                    h.movePs(dt);
                }
            }else {
                Player player = ((Player) a);
                player.applyBehaviors(dt);
            }
        }
    }

    /**
     * Realiza a acao de comer para todos os animais no ecossistema.
     * @param t Terreno que define o ambiente do ecossistema.
     */
    private void eat(Terrain t){
        for (Animal a: allAnimals){
            a.eat(t);
        }
        if (getPlayer()!=null)
            allAnimals = getPlayer().eat(allAnimals);
    }

    /**
     * Realiza a acao de consumir energia para todos os animais no ecossistema.
     * @param dt Intervalo de tempo.
     * @param t  Terreno que define o ambiente do ecossistema.
     */
    public void energy_consumption(float dt, Terrain t){
        for (Animal a: allAnimals){
            a.energy_consumption(dt, t);
        }
    }

    /**
     * Lida com a morte dos animais no ecossistema.
     */
    public void die(){
        for (int i=allAnimals.size()-1; i>=0; i--){
            Animal a = allAnimals.get(i);
            if (a.isDead()){
                allAnimals.remove(a);
            }else if (a.die()){
                allAnimals.remove(a);
            }
        }
    }

    /**
     * Realiza a acao de reproducao para todos os animais no ecossistema.
     * @param mutate Indica se a mutacao genetica deve ocorrer durante a reproducao.
     */
    public void reproduce(boolean mutate){
        for (int i = allAnimals.size()-1; i>=0; i--){
            Animal a = allAnimals.get(i);
            Animal child = a.reproduce(mutate);
            if (child != null){
                allAnimals.add(child);
            }
        }
    }

    /**
     * Adiciona um predador ao ecossistema.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do ecossistema.
     * @param t   Terreno que define o ambiente do ecossistema.
     */
    public void addPredator(PApplet p, SubPlot plt, Terrain t){
        PVector pos = new PVector(p.random((float) window[0], (float) window[1]), p.random((float) window[2], (float) window[3]));
        int color = p.color(
                WorldConstants.PREDATOR_COLOR[0],
                WorldConstants.PREDATOR_COLOR[1],
                WorldConstants.PREDATOR_COLOR[2]
        );
        Animal predator = new Predator(pos, WorldConstants.PREDATOR_MASS, WorldConstants.PREDATOR_SIZE, color, p, plt);

        predator.addBehavior(new Wander(1));
        predator.addBehavior(new AvoidObstacle(2));
        predator.addBehavior(new Pursuit(9));

        animalNewTarget(predator, t);
        allAnimals.add(predator);
    }

    /**
     * Adiciona uma presa ao ecossistema.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do ecossistema.
     * @param t   Terreno que define o ambiente do ecossistema.
     */
    public void addPrey(PApplet p, SubPlot plt, Terrain t){
        PVector pos = new PVector(p.random((float) window[0], (float) window[1]), p.random((float) window[2], (float) window[3]));
        int color = p.color(
                WorldConstants.PREY_COLOR[0],
                WorldConstants.PREY_COLOR[1],
                WorldConstants.PREY_COLOR[2]
        );

        Animal prey = new Prey(pos, WorldConstants.PREY_MASS, WorldConstants.PREY_SIZE, color, p, plt);

        prey.addBehavior(new Wander(1));
        prey.addBehavior(new AvoidObstacle(2));
        prey.addBehavior(new Evade(8));

        animalNewTarget(prey, t);
        allAnimals.add(prey);
    }

    /**
     * Adiciona um cacador ao ecossistema.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do ecossistema.
     * @param t   Terreno que define o ambiente do ecossistema.
     */
    public void addHunter(PApplet p, SubPlot plt, Terrain t){
        PVector pos = new PVector(p.random((float) window[0], (float) window[1]), p.random((float) window[2], (float) window[3]));
        int color = p.color(
                WorldConstants.HUNTER_COLOR[0],
                WorldConstants.HUNTER_COLOR[1],
                WorldConstants.HUNTER_COLOR[2]
        );

        Animal hunter = new Hunter(pos, WorldConstants.HUNTER_MASS, WorldConstants.HUNTER_SIZE, color, p, plt);
        hunter.addBehavior(new Pursuit(6));

        animalNewTarget(hunter, t);
        allAnimals.add(hunter);
    }

    /**
     * Inicializa as populacoes de presas, predadores no ecossistema.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do ecossistema.
     * @param t   Terreno que define o ambiente do ecossistema.
     */
    public void initializePopulations(PApplet p, SubPlot plt, Terrain t){
        for (int i=0; i < WorldConstants.INI_PREY_POPULATION; i++){
            PVector pos = new PVector(p.random((float) window[0], (float) window[1]), p.random((float) window[2], (float) window[3]));
            int color = p.color(
                    WorldConstants.PREY_COLOR[0],
                    WorldConstants.PREY_COLOR[1],
                    WorldConstants.PREY_COLOR[2]
            );

            Animal prey = new Prey(pos, WorldConstants.PREY_MASS, WorldConstants.PREY_SIZE, color, p, plt);

            prey.addBehavior(new Wander(1));
            prey.addBehavior(new AvoidObstacle(2));
            prey.addBehavior(new Evade(8));

            allAnimals.add(prey);
        }

        for (int i=0; i < WorldConstants.INI_PREDATOR_POPULATION; i++){
            PVector pos = new PVector(p.random((float) window[0], (float) window[1]), p.random((float) window[2], (float) window[3]));
            int color = p.color(
                    WorldConstants.PREDATOR_COLOR[0],
                    WorldConstants.PREDATOR_COLOR[1],
                    WorldConstants.PREDATOR_COLOR[2]
            );
            Animal predator = new Predator(pos, WorldConstants.PREDATOR_MASS, WorldConstants.PREDATOR_SIZE, color, p, plt);

            predator.addBehavior(new Wander(1));
            predator.addBehavior(new AvoidObstacle(2));
            predator.addBehavior(new Pursuit(9));

            allAnimals.add(predator);
        }

        for(Animal a: allAnimals){
            if (!(a instanceof Player)) {
                animalNewTarget(a, t);
            }
        }
    }

    /**
     * Inicializa as populacao de cacadores no ecossistema.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do ecossistema.
     * @param t   Terreno que define o ambiente do ecossistema.
     */
    public void initializeHunters(PApplet p, SubPlot plt, Terrain t){
        for (int i = 0; i < WorldConstants.INI_HUNTER_POPULATION; i++) {
            PVector pos = new PVector(p.random((float) window[0], (float) window[1]), p.random((float) window[2], (float) window[3]));
            int color = p.color(
                    WorldConstants.HUNTER_COLOR[0],
                    WorldConstants.HUNTER_COLOR[1],
                    WorldConstants.HUNTER_COLOR[2]
            );

            Animal hunter = new Hunter(pos, WorldConstants.HUNTER_MASS, WorldConstants.HUNTER_SIZE, color, p, plt);
            hunter.addBehavior(new Pursuit(6));

            animalNewTarget(hunter, t);
            allAnimals.add(hunter);
        }
    }

    /**
     * Inicializa o jogador no ecossistema.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do ecossistema.
     */
    public void initializePlayer(PApplet p, SubPlot plt){
        int color = p.color(
                WorldConstants.PLAYER_COLOR[0],
                WorldConstants.PLAYER_COLOR[1],
                WorldConstants.PLAYER_COLOR[2]
        );

        Animal player = new Player(new PVector(), WorldConstants.PLAYER_MASS, WorldConstants.PLAYER_SIZE, color, p, plt);
        allAnimals.add(player);
    }

    /**
     * Exibe todos os animais no ecossistema.
     * @param p   Referencia ao objeto PApplet, usado para renderizacao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do ecossistema.
     */
    public void display(PApplet p, SubPlot plt){
        for (Animal a: allAnimals){
            a.display(p, plt);
        }
    }

    public int getNumAnimals(){
        return getNumPreys() + getNumPredators();
    }

    public int getNumPreys(){
        int numberPrey = 0;
        for (Animal a: allAnimals){
            if (a instanceof Prey){
                numberPrey++;
            }
        }
        return numberPrey;
    }

    public int getNumPredators(){
        int numberPredators = 0;
        for (Animal a: allAnimals){
            if (a instanceof Predator){
                numberPredators++;
            }
        }
        return numberPredators;
    }

    public int getNumHunters(){
        int numberHunter = 0;
        for (Animal a: allAnimals){
            if (a instanceof Hunter){
                numberHunter++;
            }
        }
        return numberHunter;
    }

    public int getNumPlayer(){
        int number = 0;
        for (Animal a: allAnimals){
            if (a instanceof Player){
                number++;
            }
        }
        return number;
    }

    public Player getPlayer(){
        for (Animal a: allAnimals){
            if (a instanceof Player){
                return (Player) a;
            }
        }
        return null;
    }

    /**
     * Define um novo alvo para um animal com base no tipo de animal e nos obstaculos no terreno.
     * @param a Animal para o qual um novo alvo sera definido.
     * @param t Terreno que define os obstaculos no ecossistema.
     */
    public void animalNewTarget(Animal a, Terrain t){

        List<Body> preys = t.getObstacles();
        List<Body> predators = t.getObstacles();
        List<Body> hunters = new ArrayList<Body>();
        for (Animal p : allAnimals){
            if (!p.isDead() && !p.die()){

                if (p instanceof Prey){
                    preys.add(p); // presas sao so do tipo Prey
                } else{
                    predators.add(p); // predatores sao so do tipo Predator e Hunter
                }

                // caca todos menos o seu tipo
                if (!(p instanceof Hunter)){
                    hunters.add(p);
                }
            }
        }

        if (a instanceof Prey){
            Eye eye = new Eye(a, predators);
            a.setEye(eye);
        }else if (a instanceof Predator){
            Eye eye = new Eye(a, preys);
            a.setEye(eye);
        }else{
            Eye eye = new Eye(a, hunters);
            a.setEye(eye);
        }

        Animal target = (Animal) a.getEye().setNextTarget();
        a.setTarget(target);
    }

    /**
     * Atualiza as informacoes do olho para um animal.
     * @param a Animal para o qual o olho sera atualizado.
     * @param t Terreno que define os obstaculos no ecossistema.
     */
    public void animalUpdate(Animal a, Terrain t){
        List<Body> obstacles = t.getObstacles();

        if (a.getTarget() == null) {
            Eye eye = new Eye(a, obstacles);
            a.setEye(eye);
        }
    }
}
