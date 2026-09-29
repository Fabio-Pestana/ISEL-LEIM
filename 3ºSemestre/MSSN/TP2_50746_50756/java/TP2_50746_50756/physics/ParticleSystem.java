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

    public ParticleSystem(PVector pos, PVector vel, float mass, float radius, int particleColor, float lifetime, ParticleSystemControl particleSystemControl) {
        super(pos, vel, mass, radius);
        this.particleColor = particleColor;
        this.lifetime = lifetime;
        this.particleSystemControl = particleSystemControl;
        this.particles = new ArrayList<Particle>();
    }


    public void setParticleSystem(PVector pos, PVector vel) {
        setPos(pos);
        setVel(vel);
    }

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

    private void addParticle(){
        Particle particle = new  Particle(pos, particleSystemControl.getRandomVel(), radius, particleColor, lifetime);
        particles.add(particle);
    }

    public void display(PApplet p, SubPlot plt){
        p.pushStyle();
        float [] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] r = plt.getDimInPixel(radius, radius);

        p.noStroke();
        p.fill(p.color(255));
        p.circle(pp[0], pp[1], 2*r[0]);
        p.popStyle();
        for (Particle particle : particles){
            particle.display(p, plt);
        }
    }

    /***
     * Calcula o angulo em relação ao Sol que o cintura de
     * asteroides deve assumir à medida que cumpre a sua órbita
     *
     * @param p
     * @param sun Sol
     */
    public void changeAngleOfSP(PApplet p, Body sun) {
        // calcula o angulo da sua posição em relação ao Sol
        float angleToSun = PVector.sub(sun.getPos(), this.pos).heading();

        // Calcula angulo perpendicular
        float perpendicularAngle = angleToSun - p.PI / 2;

        float[] aux = particleSystemControl.getControl();
        aux[0] = perpendicularAngle; //Atualiza o angulo no particleSystemControl
        particleSystemControl.setVelControl(aux);
    }

}
