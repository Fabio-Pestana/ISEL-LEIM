package aa;


public class DNA {
    public float maxSpeed;
    public float maxForce;
    public float visionDistance;
    public float visionSafeDistance;
    public float visionAngle;
    public float deltaTPursuit;
    public float radiusArrive;
    public float deltaTWander;
    public float radiusWander;
    public float deltaPhiWander;

    /**
     * Construtor da classe DNA.
     */
    public DNA() {
        //Valores concordantes com a escala da window

        //Physics
        maxSpeed = random(1f, 2.5f);
        maxForce = random(4f, 7f);
        //Vision
        visionDistance = random(1.5f, 3f);
        visionSafeDistance = 0.25f * visionDistance;
        visionAngle = (float)  Math.PI * 0.35f; //visão de 360
        //Pursuit
        deltaTPursuit = random(0.5f, 1f);
        //Arrive
        radiusArrive = random(3, 5);
        //Wander
        deltaTWander = random(.3f, .6f);//0.5f, 0.7f
        radiusWander = random(1f, 3f); //2, 4
        deltaPhiWander = (float) (Math.PI/8); //(Math.PI/8)
    }

    /**
     * Construtor da classe DNA utilizado para criar uma copia do DNA com a opcao de mutacao.
     * @param a      DNA original a ser copiado.
     * @param mutate boolean que indica se a mutacao deve ser aplicada a copia.
     */
    public DNA(DNA a, boolean mutate) {

        //Physics
        maxSpeed = a.maxSpeed;
        maxForce = a.maxForce;
        //Vision
        visionDistance = a.visionDistance;
        visionSafeDistance = a.visionSafeDistance;
        visionAngle = a.visionAngle; //visão de 360
        //Pursuit
        deltaTPursuit =a.deltaTPursuit;
        //Arrive
        radiusArrive = a.radiusArrive;
        //Wander
        deltaTWander = a.deltaTWander;//0.5f, 0.7f
        radiusWander = a.radiusWander; //2, 4
        deltaPhiWander = a.deltaPhiWander; //(Math.PI/8)

        if (mutate) mutate();
    }

    /**
     * Realiza a mutacao nos parametros geneticos, alterando aleatoriamente a velocidade maxima.
     */
    private void mutate(){
        maxSpeed += random(-0.2f, 0.2f);
        maxSpeed = Math.max(0, maxSpeed);
    }

    /**
     * Gera um numero aleatorio dentro do intervalo especificado.
     * @param min Valor minimo do intervalo.
     * @param max Valor maximo do intervalo.
     * @return Numero aleatorio dentro do intervalo especificado.
     */
    public static float random(float min, float max) {
        return (float) (min + (max - min) * Math.random());
    }

    public void setMaxSpeed(float maxSpeed){
        this.maxSpeed = maxSpeed;
    }
}
