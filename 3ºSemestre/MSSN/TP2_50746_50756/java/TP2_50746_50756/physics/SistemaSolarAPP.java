package physics;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

public class SistemaSolarAPP implements IProcessingApp {
    // sol, mercurio, venus, terra, marte, jupiter, saturno, urano , netuno
    private final float[] planetasDoSistemaMassa = {1.989e30f, 3.39e23f, 4.867e24f, 5.97e24f, 6.39e23f, 1.898e27f, 5.68e26f, 8.68e25f, 1.02e26f};
    private final double[] planetasRaio = {6.9634e9*2, 2.4397e9, 6.0518e9, 6.371e9, 3.3895e9, 6.9911e10, 5.8232e10, 2.5362e10*1.5, 2.4622e10*1.5};
    //valores reais em metros = {6.9634e8, 2.4397e6, 6.0518e6, 6.371e6, 3.3895e6, 6.9911e7, 5.8232e7, 2.5362e7, 2.4622e7};

    //mercurio, venus, terra, marte, jupiter, saturno, urano , netuno
    private final float[] planetasDoSistemaDist_Sol = {5.791e10f, 1.082e11f, 1.496e11f, 2.2794e11f, 7.7833e11f, 1.4294e12f, 2.97099e12f, 4.5043e12f};
    private final float[] planetasDoSistemaTranslacao_Sol = {4.787e4f, 3.502e4f, 3e4f, 2.4e4f, 1.3e4f, 9.6e3f, 6.8e3f, 5.45e3f};

    private final String[] planetsName= {"Sol", "Mercurio", "Venus", "Terra", "Marte", "Jupiter", "Saturno", "Urano" , "Netuno"};
    private ParticleSystem cinturaDeAsteroides1,cinturaDeAsteroides2, cinturaDeAsteroides3,cinturaDeAsteroides4, sunRaios;
    private float[] viewport = {0,0,1,1};
    private double[] window = {-1.2*netunoDist(), 1.2*netunoDist(), -1.2*netunoDist(), 1.2*netunoDist()};
    private SubPlot plt;
    private Body sun;
    private Body[] planets = new Body[8];
    private float speedUp = 60 * 60 * 24 * 15 ;
    // 1 segundo de simulação = mais ou menos 1/2 mes
    private float speedUp2 = 60 * 60 * 24 * 30 *12;
    // 1 segundo de simulação = mais ou menos 1 ano
    private  float[] velControl = {PApplet.radians(180), PApplet.radians(5), (float)2.25215e3, (float)2.25215e4};
    private  float[] velControlSun = {0, PApplet.radians(360), (float)2.25215e3, (float)2.25215e4};
    private int idx;
    private PImage backgroundImage;
    private ParticleSystemControl particleSystemControl,particleSystemControl2,particleSystemControl3,particleSystemControl4, particleSystemControlSun;

    private final float buttonWidth = 80;
    private final float buttonHeight = 30;
    private final float margin = 10;
    private final float buttonx = buttonWidth + 2*margin;
    @Override
    public void setup(PApplet p) {
        plt = new SubPlot(window, viewport, p.width, p.height);

        idx= planets.length-1;

        sun = new Body(new PVector(), new PVector(), planetasDoSistemaMassa[0], (float) planetasRaio[0], p.color(255,200,0));
        sun.setName(planetsName[0]);

        particleSystemControlSun = new ParticleSystemControl(velControlSun);

        sunRaios = new ParticleSystem(new PVector(), new PVector(), planetasDoSistemaMassa[0], (float) (planetasRaio[0]*0.2), p.color(255,200,0), 1000000f, particleSystemControlSun);

        int[] planetColors = {
                p.color(139, 69, 19),  // Mercurio
                p.color(255, 255, 200),  // Venus
                p.color(0,200,120),    // Terra
                p.color(193, 68, 14), // Marte
                p.color(216, 202, 157),  // Jupiter
                p.color(227,224,192), // Saturno
                p.color(70, 130, 180), // Urano
                p.color(0, 0, 255)     // Netuno
        };

        for (int i=0; i<planets.length; i++){
            planets[i] = new Body(new PVector(0,planetasDoSistemaDist_Sol[i]), new PVector(planetasDoSistemaTranslacao_Sol[i],0), planetasDoSistemaMassa[i+1], (float) planetasRaio[i+1], planetColors[i]);
            planets[i].setName(planetsName[i+1]);
        }

        particleSystemControl = new ParticleSystemControl(velControl);
        particleSystemControl2 = new ParticleSystemControl(velControl);
        particleSystemControl3 = new ParticleSystemControl(velControl);
        particleSystemControl4 = new ParticleSystemControl(velControl);

        cinturaDeAsteroides1 = new ParticleSystem(new PVector(0,(float) (cinturadeAteroidesDist()*0.8)), new PVector(cinturadeAteroidesTranslacao_Sol(),0), cinturadeAteroidesMassa(),  (float) (planetasRaio[4]*1.5), p.color(255), 12000000f, particleSystemControl);
        cinturaDeAsteroides2 = new ParticleSystem(new PVector(0, (float) (-1*cinturadeAteroidesDist()*0.8)), new PVector(-1*cinturadeAteroidesTranslacao_Sol(),0),  cinturadeAteroidesMassa(),  (float)  (planetasRaio[4]*1.5), p.color(255), 12000000f, particleSystemControl2);
        cinturaDeAsteroides3 = new ParticleSystem(new PVector((float) (cinturadeAteroidesDist()*0.8),0), new PVector(0,-1*cinturadeAteroidesTranslacao_Sol()), cinturadeAteroidesMassa(),  (float)  (planetasRaio[4]*1.5), p.color(255), 12000000f, particleSystemControl3);
        cinturaDeAsteroides4 = new ParticleSystem(new PVector((float) (-1*cinturadeAteroidesDist()*0.8),0), new PVector(0,cinturadeAteroidesTranslacao_Sol()),  cinturadeAteroidesMassa(),  (float)  (planetasRaio[4]*1.5), p.color(255), 12000000f, particleSystemControl4);

        backgroundImage = p.loadImage("background.jpg");

        backgroundImage.resize(p.width, p.height);

    }

    @Override
    public void draw(PApplet parent, float dt) {
        parent.background(backgroundImage);
        float[] coordenadasSub = plt.getBoundingBox();
        parent.fill(10, 120);
        parent.rect(coordenadasSub[0], coordenadasSub[1], coordenadasSub[2], coordenadasSub[3]);

        sunRaios.move(dt * speedUp);
        sunRaios.display(parent, plt);
        sun.display(parent, plt);

        for (int i = 0; i < 4; i++) {
            orbitPlanet(planets[i], dt, parent, plt, speedUp);
        }
        for (int i = 4; i < planets.length; i++) {
            orbitPlanet(planets[i], dt, parent, plt, speedUp2);
        }

        orbitPlanet(cinturaDeAsteroides1, dt, parent, plt, speedUp * 4);
        cinturaDeAsteroides1.changeAngleOfSP(parent, sun);
        orbitPlanet(cinturaDeAsteroides2, dt, parent, plt, speedUp * 4);
        cinturaDeAsteroides2.changeAngleOfSP(parent, sun);
        orbitPlanet(cinturaDeAsteroides3, dt, parent, plt, speedUp * 4);
        cinturaDeAsteroides3.changeAngleOfSP(parent, sun);
        orbitPlanet(cinturaDeAsteroides4, dt, parent, plt, speedUp * 4);
        cinturaDeAsteroides4.changeAngleOfSP(parent, sun);
        if (idx <= 6) {
            displayText(plt, parent, "Cintura de asteroides", cinturadeAteroidesTranslacao_Sol(), cinturadeAteroidesDist());
        }

        // Botao de aumentar G
        parent.fill(100);
        parent.rect(margin, margin, buttonWidth, buttonHeight);
        parent.fill(255);
        parent.text("Aumentar G", margin + 5, margin + 20);

        // Botao de diminuir G
        parent.fill(100);
        parent.rect(buttonx, margin, buttonWidth, buttonHeight);
        parent.fill(255);
        parent.text("Diminuir G", buttonx + 10, margin + 20);

        parent.fill(255, 255, 0);
        parent.text("R -> reset Sistema Solar", 10, parent.height - 80);
        parent.text("+ -> zoom in no Sistema Solar", 10, parent.height - 60);
        parent.text("- -> zoom out no Sistema Solar", 10, parent.height - 40);
        parent.text("Mouse sobre planetas -> info sobre eles", 10, parent.height - 20);
    }

    public boolean isMouseOverButton(PApplet p, float a, float b, float c, float d){
        return (p.mouseX>= a && p.mouseX<=b && p.mouseY>=c && p.mouseY<=d);
    }

    @Override
    public void keyPressed(PApplet parent) {
        if (parent.key == '-'){
            if (idx!=planets.length-1){
                idx++;
                double[] w = {-1.2*planetasDoSistemaDist_Sol[idx], 1.2*planetasDoSistemaDist_Sol[idx], -1.2*planetasDoSistemaDist_Sol[idx], 1.2*planetasDoSistemaDist_Sol[idx]};
                plt.setSubPlot(w, viewport, parent.width, parent.height);
            }
        }
        if (parent.key == '+'){
            if (idx!=0){
                idx--;
                double[] w = {-1.1*planetasDoSistemaDist_Sol[idx], 1.1*planetasDoSistemaDist_Sol[idx], -1.1*planetasDoSistemaDist_Sol[idx], 1.1*planetasDoSistemaDist_Sol[idx]};
                plt.setSubPlot(w, viewport, parent.width, parent.height);
            }
        }
        if (parent.key == 'R' || parent.key == 'r'){
            sun.resetG();
            for (int i=0; i<planets.length; i++){
                planets[i].setBody(new PVector(0,planetasDoSistemaDist_Sol[i]), new PVector(planetasDoSistemaTranslacao_Sol[i],0));
            }
            cinturaDeAsteroides1.setParticleSystem(new PVector(0,(float) (cinturadeAteroidesDist()*0.8)), new PVector(cinturadeAteroidesTranslacao_Sol(),0));
            cinturaDeAsteroides2.setParticleSystem(new PVector(0, (float) (-1*cinturadeAteroidesDist()*0.8)), new PVector(-1*cinturadeAteroidesTranslacao_Sol(),0));
            cinturaDeAsteroides3.setParticleSystem(new PVector((float) (cinturadeAteroidesDist()*0.8),0), new PVector(0,-1*cinturadeAteroidesTranslacao_Sol()));
            cinturaDeAsteroides4.setParticleSystem(new PVector((float) (-1*cinturadeAteroidesDist()*0.8),0), new PVector(0,cinturadeAteroidesTranslacao_Sol()));
        }
    }

    @Override
    public void mousePressed(PApplet parent) {
        if (isMouseOverButton(parent, margin, buttonWidth, margin, margin+buttonHeight)) {
            sun.setG(sun.getG() * 1.2);
        }
        if (isMouseOverButton(parent, buttonx, buttonx+buttonWidth, margin, margin+buttonHeight)) {
            sun.setG(sun.getG() * 0.8);
        }
    }



    public void orbitPlanet(Mover p, float dt, PApplet parent, SubPlot plt, float s){
        PVector f = sun.attraction(p);
        p.applyForce(f);

        p.move(dt * s);
        p.display(parent, plt);
    }
    public float netunoDist(){
        return planetasDoSistemaDist_Sol[7];
    }


    private float cinturadeAteroidesDist() {
        return planetasDoSistemaDist_Sol[3] * 2;
    }

    private float cinturadeAteroidesTranslacao_Sol() {
        return (planetasDoSistemaTranslacao_Sol[3] +planetasDoSistemaTranslacao_Sol[4])/2;
    }

    private float cinturadeAteroidesMassa() {
        return ((planetasDoSistemaMassa[4] + planetasDoSistemaMassa[5]) );
    }

    private void displayText(SubPlot plt,PApplet p, String s, float x, float y){
        float[] pp = plt.getPixelCoord(x, y);
        p.pushStyle();
        p.fill(255);
        p.text(s,pp[0] - pp[0]/10, pp[1]);
        p.popStyle();
    }
}
