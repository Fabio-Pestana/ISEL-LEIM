package physics;

import ecosystem.WorldConstants;
import processing.core.PApplet;
import processing.core.PVector;

public class HunterParticleSystem extends ParticleSystem{

    private float timer;

    private float timeOfPs;

    /**
     * Construtor da classe HunterParticleSystem
     * @param pos  Vetor representando a posição inicial do sistema.
     * @param vel  Vetor representando a velocidade inicial do sistema.
     * @param mass A massa do sistema.
     * @param p Objeto PApplet usado para desenhar.
     */
    public HunterParticleSystem(PVector pos, PVector vel, float mass, PApplet p)
    {
        super(pos, vel, mass, .2f, p.color(250, 200, 60), .4f, new ParticleSystemControl(WorldConstants.DEATH_CONTROL));
        timer = 0;
        this.timeOfPs = WorldConstants.TIME_PS;
    }

    /**
     * Atualiza o movimento do sistema de particulas e incrementa o temporizador.
     * @param dt Intervalo de tempo para o movimento.
     */
    public void move(float dt){
        super.move(dt);
        timer += dt;
    }

    /**
     * Verifica se o sistema de particulas de caçadores atingiu o final de sua vida util.
     * @return true se o sistema de partículas estiver morto, false caso contrário.
     */
    public boolean isDead(){
        return timer > timeOfPs;
    }
}
