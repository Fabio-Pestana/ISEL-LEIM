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

    public ParticleSystemControl(float[] velcontrol){
        this.control = velcontrol;
        setVelControl(velcontrol);
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

    public void setVelControl(float[] velcontrol){
        anguloMedio = velcontrol[0];
        anguloDispersao = velcontrol[1];
        velMin = velcontrol[2];
        velMax = velcontrol[3];
    }

    public PVector getRandomVel(){

        // "anguloDispersao/2" porque considerando anguloDispersao como a dispersao total,
        // deste modo temos a dispersao para cada lado
        float angle =  getRandom(anguloMedio - anguloDispersao/2, anguloMedio + anguloDispersao/2);
        PVector v = PVector.fromAngle(angle);

        return  v.mult(getRandom(velMin, velMax));
    }

    public static float getRandom(float min, float max){
        return min + (float) (Math.random() * (max - min));
    }
}
