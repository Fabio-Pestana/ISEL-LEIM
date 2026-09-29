package physics;

import processing.core.PVector;

public class ParticleSystemControl {

    private float anguloMedio;
    private float anguloDispersao;
    private float velMin;
    private float velMax;

    private float[] control;

    public float[] getControl() {
        return control;
    }

    /**
     * Construtor da classe ParticleSystemControl
     * @param velcontrol Um array de floats representando os controles [angulo medio, dispersao angular, velMin, velMax].
     */
    public ParticleSystemControl(float[] velcontrol){
        this.control = velcontrol;
        setVelControl(velcontrol);
    }

    /**
     * Configura os controles com base no array fornecido.
     * @param velcontrol Um array de floats representando os controles [angulo medio, dispersao angular, velMin, velMax].
     */
    public void setVelControl(float[] velcontrol){
        anguloMedio = velcontrol[0];
        anguloDispersao = velcontrol[1];
        velMin = velcontrol[2];
        velMax = velcontrol[3];
    }

    /**
     * Gera e retorna um vetor de velocidade aleatoria com base nos controles atuais.
     * @return Um vetor de velocidade aleatoria.
     */
    public PVector getRandomVel(){

        // "anguloDispersao/2" porque considerando anguloDispersao como a dispersao total,
        // deste modo temos a dispersao para cada lado
        float angle =  getRandom(anguloMedio - anguloDispersao/2, anguloMedio + anguloDispersao/2);
        PVector v = PVector.fromAngle(angle);

        return  v.mult(getRandom(velMin, velMax));
    }

    /**
     * Gera e retorna um numero aleatorio no intervalo fornecido.
     * @param min O valor minimo do intervalo.
     * @param max O valor maximo do intervalo.
     * @return Um numero aleatorio no intervalo [min, max].
     */
    public static float getRandom(float min, float max){
        return min + (float) (Math.random() * (max - min));
    }

    public float getAnguloMedio() {
        return anguloMedio;
    }

    public void setAnguloMedio(float anguloMedio) {
        this.anguloMedio = anguloMedio;
    }

    public float getAnguloDispersao() {
        return anguloDispersao;
    }

    public void setAnguloDispersao(float anguloDispersao) {
        this.anguloDispersao = anguloDispersao;
    }
}
