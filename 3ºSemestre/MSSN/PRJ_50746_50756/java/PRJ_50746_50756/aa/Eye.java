package aa;

import ecosystem.Animal;
import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Eye {

    private List<Body> allTrackingBodies;
    private List<Body> farSight;
    private List<Body> nearSight;

    private List<Body> obstaclesInSight;
    private Boid me;

    protected Body target;

    public List<Body> getAllTrackingBodies() {
        return allTrackingBodies;
    }

    public void setAllTrackingBodies(List<Body> allTrackingBodies) {
        this.allTrackingBodies = allTrackingBodies;
    }

    /**
     * Construtor da classe Eye.
     * @param me                Referencia do agente Boid ao qual o olho pertence.
     * @param allTrackingBodies Lista de todos os corpos rastreados pelo olho.
     */
    public Eye(Boid me, List<Body> allTrackingBodies){
        this.me = me;
        this.allTrackingBodies = allTrackingBodies;
        if (allTrackingBodies.size()>0){
            target = allTrackingBodies.get(0);
        }
    }

    /**
     * Construtor da classe Eye utilizado para criar uma copia do olho.
     * @param me Referencia do agente Boid ao qual o olho pertence.
     * @param e  Eye original a ser copiado.
     */
    public Eye(Boid me, Eye e){
        this.me = me;
        this.allTrackingBodies = e.allTrackingBodies;
        target = e.target;
    }

    /**
     * Realiza a verificacao visual do ambiente ao redor do agente, identificando corpos proximos.
     */
    public void look(){
        farSight = new ArrayList<Body>();
        nearSight = new ArrayList<Body>();
        obstaclesInSight = new ArrayList<Body>();
        for(Body b : allTrackingBodies){
            if (b!=null){
                if (farSight(b.getPos()))
                    farSight.add(b);
                if (nearSight(b.getPos()))
                    nearSight.add(b);
                if (farSight(b.getPos()) && !(b instanceof Animal))
                    obstaclesInSight.add(b);
            }
        }
    }

    public List<Body> getFarSight() {
        return farSight;
    }

    public List<Body> getObstaclesInSight() {
        return obstaclesInSight;
    }

    public List<Body> getNearSight() {
        return nearSight;
    }

    /**
     * Verifica se um corpo esta na visao do olho, considerando uma distancia maxima e um angulo maximo.
     * @param target       Posicao do corpo a ser verificado.
     * @param maxDistance  Distancia maxima para que o corpo esteja na visao.
     * @param maxAngle     Angulo maximo para que o corpo esteja na visao.
     * @return Verdadeiro se o corpo estiver na visao, falso caso contrário.
     */
    private boolean inSight(PVector target, float maxDistance, float maxAngle){
        PVector r = PVector.sub(target, me.getPos());
        float d = r.mag();
        float angle = PVector.angleBetween(r, me.getVel());
        return ((d>0) && (d < maxDistance) && (angle < maxAngle));
    }

    /**
     * Exibe visualmente a visao do olho no ambiente.
     * @param p   Objeto PApplet usado para desenhar.
     * @param plt Subplot associado ao olho.
     */
    public void display(PApplet p, SubPlot plt){
        p.pushStyle();
        p.pushMatrix();
        float[] pp = plt.getPixelCoord(me.getPos().x, me.getPos().y);
        p.translate(pp[0], pp[1]);
        p.rotate(-me.getVel().heading());
        p.noFill();
        p.stroke(255, 0, 0);
        p.strokeWeight(3);
        float[] dd1 = plt.getVectorCoord(me.adn.visionDistance, me.adn.visionDistance);
        float[] dd2 = plt.getVectorCoord(me.adn.visionSafeDistance, me.adn.visionSafeDistance);
        p.rotate(me.adn.visionAngle);
        p.line(0,0,dd1[0], 0);
        p.rotate(-2*me.adn.visionAngle);
        p.line(0,0,dd1[0], 0);
        p.rotate(me.adn.visionAngle);
        p.arc(0,0,2*dd1[0], 2*dd1[0], -me.adn.visionAngle, me.adn.visionAngle);
        p.stroke(255, 0, 255);
        p.circle(0, 0, 2*dd2[1]);
        p.popMatrix();
        p.popStyle();
    }

    /**
     * Verifica se um corpo esta na visao de longa distancia do olho.
     * @param target Posicao do corpo a ser verificado.
     * @return Verdadeiro se o corpo estiver na visao, falso caso contrario.
     */
    private boolean farSight(PVector target){
        return inSight(target, me.adn.visionDistance, me.adn.visionAngle);
    }

    /**
     * Verifica se um corpo esta na visao de curta distancia do olho.
     * @param target Posicao do corpo a ser verificado.
     * @return Verdadeiro se o corpo estiver na visao, falso caso contrario.
     */
    private boolean nearSight(PVector target){
        return inSight(target, me.adn.visionSafeDistance, (float) Math.PI);
    }

    /**
     * Obtem a lista de corpos Boid na visao.
     * @return Lista de corpos Boid em visao.
     */
    public List<Body> getBoidsInSight() {
        List<Body> boidsInSight = new ArrayList<>();
        for (Body b : allTrackingBodies) {
            if (farSight(b.getPos()) || nearSight(b.getPos())) {
                boidsInSight.add(b);
            }
        }
        return boidsInSight;
    }

    /**
     * Define o proximo alvo para o olho, escolhendo o animal mais proximo.
     * @return Corpo (Animal) mais proximo.
     */
    public Body setNextTarget(){

        List<Body> boidsInSight = getBoidsInSight();

        List<Body> animals = new ArrayList<Body>();

        if (boidsInSight.size() == 0){
            return null;
        }

        for (Body b : boidsInSight){
            if (b instanceof Animal){
                animals.add(b);
            }
        }

        if (animals.size() == 0){
            return null;
        }

        Body body = animals.get(0);

        //distancia entre o boid me e o primeiro body
        float dist = PVector.sub(me.getPos(), body.getPos()).mag();

        for (Body b : animals){
            float distAux =  PVector.sub(me.getPos(),b.getPos()).mag();

            if (distAux < dist){
                //se a distacia for menor atualiza-se a distancia e o body
                dist = distAux;
                body = b;
            }
        }
        //target passa a ser o alvo com menor distancia ao boid me
        target = body;
        return body;
    }
}
