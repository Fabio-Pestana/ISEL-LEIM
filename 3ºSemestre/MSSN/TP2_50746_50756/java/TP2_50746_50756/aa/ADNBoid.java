package aa;


public class ADNBoid {

    public float maxSpeed;
    public float maxForce;
    public float visionDistance;
    public float visionSafeDistance;
    public float visionAngle;
    public float deltaTPursuit;
    public float radiusArreive;
    public float deltaTWander;
    public float radiusWander;
    public float deltaPhiWander;

    public ADNBoid() {
        //Valores concordantes com a escala da window

        //Physics
        maxSpeed = random(3, 5);
        maxForce = random(4, 7);
        //Vision
        visionDistance = random(2, 4);
        visionSafeDistance = 0.25f * visionDistance;
        visionAngle = (float) Math.PI * 0.8f; //visão de 360
        //Pursuit
        deltaTPursuit = random(0.5f, 1f);
        //Arrive
        radiusArreive = random(3, 5);
        //Wander
        deltaTWander = random(1f, 1f);//0.5f, 0.7f
        radiusWander = random(3, 3); //2, 4
        deltaPhiWander = (float) (Math.PI/4); //(Math.PI/8)

    }

    public static float random(float min, float max) {
        return (float) (min + (max - min) * Math.random());
    }
}
