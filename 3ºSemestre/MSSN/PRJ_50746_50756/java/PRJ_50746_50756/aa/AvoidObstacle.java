package aa;

import physics.Body;
import processing.core.PVector;

public class AvoidObstacle extends Behavior{


    /**
     * Construtor da classe AvoidObstacle.
     * @param weight Peso associado a este comportamento.
     */
    public AvoidObstacle(float weight) {
        super(weight);
    }

    /**
     * Calcula e retorna a velocidade desejada para o comportamento de evitar obstaculos.
     * @param me Boid que esta executando o comportamento.
     * @return Vetor de velocidade desejada para evitar obstaculos.
     */
    @Override
    public PVector getDesiredVelocity(Boid me) {
       float s = hasObstacles(me);
       if (s == 0){
           return me.getVel().copy();
       }

       PVector vd = new PVector(me.getVel().y, -me.getVel().x);
       if (s>0){
           vd.mult(-1);
       }
       return vd;
    }

    /**
     * Verifica se ha obstaculos no campo de visao do Boid.
     * @param me Boid que esta executando o comportamento.
     * @return Valor indicando a presença de obstaculos: 0 se nenhum, positivo se a direita, negativo se a esquerda.
     */
    private float hasObstacles(Boid me) {
        for (Body body : me.eye.getObstaclesInSight()){
            PVector r = PVector.sub(body.getPos(), me.getPos());
            PVector vd = new PVector(me.getVel().y, -me.getVel().x);
            return PVector.dot(vd, r);
        }
        return 0;
    }
}
