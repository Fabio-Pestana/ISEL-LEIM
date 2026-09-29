package physics;

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleSystem extends Mover{

    private List<Particle> particles;
    private float lifetime;

    private int particleColor;

    private ParticleSystemControl particleSystemControl;

    protected float radius;

    /**
     * Construtor da classe ParticleSystem
     * @param pos                    Vetor representando a posição inicial do sistema.
     * @param vel                    Vetor representando a velocidade inicial do sistema.
     * @param mass                   A massa do sistema.
     * @param radius                 O raio da particula central.
     * @param particleColor          A cor das particulas.
     * @param lifetime               O tempo de vida das particulas.
     * @param particleSystemControl Objeto ParticleSystemControl que controla os parametros do sistema.
     */
    public ParticleSystem(PVector pos, PVector vel, float mass, float radius, int particleColor, float lifetime, ParticleSystemControl particleSystemControl) {
        super(pos, vel, mass);
        this.particleColor = particleColor;
        this.lifetime = lifetime;
        this.particleSystemControl = particleSystemControl;
        this.particles = new ArrayList<Particle>();
        this.radius = radius;
    }

    /**
     * Atualiza o movimento do sistema de particulas, adiciona novas particulas e remove as mortas.
     * @param dt Intervalo de tempo para o movimento.
     */
    @Override
    public void move(float dt){
        super.move(dt);

        addParticle();

        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()){
            Particle particle = iterator.next();
            particle.move(dt);
            if (particle.isDead()){
                iterator.remove();
            }
        }
    }

    /**
     * Adiciona uma nova particula ao sistema com base no controle atual.
     */
    private void addParticle(){
        Particle particle = new  Particle(pos, particleSystemControl.getRandomVel(), radius, particleColor, lifetime);
        particles.add(particle);
    }

    /**
     * Exibe a representacao grafica do sistema de particulas, incluindo a particula central e as geradas.
     * @param p   Objeto PApplet usado para desenhar.
     * @param plt Objeto SubPlot usado para mapear as coordenadas.
     */
    public void display(PApplet p, SubPlot plt){
        p.pushStyle();
        float [] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] r = plt.getVectorCoord(radius, radius);

        p.noStroke();
        p.fill(particleColor);
        p.circle(pp[0], pp[1], 2*r[0]);
        p.popStyle();
        for (Particle particle : particles){
            particle.display(p, plt);
        }
    }
}
