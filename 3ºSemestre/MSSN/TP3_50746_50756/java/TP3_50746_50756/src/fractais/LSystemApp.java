package fractais;

/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;
import tools.SubPlot;

public class LSystemApp implements IProcessingApp {

    private LSystem lSystem1, lSystem2;
    private double[] window = {-15, 15, 0, 15};
    private float[] viewport = { 0, 0, 1, 1};
    private PVector startingPosTree1, startingPosTree2;
    private SubPlot plt;
    private Turtle turtle1, turtle2;
    private final float buttonWidth = 120;
    private final float buttonHeight = 30;
    private final float marginY = 50;
    private float marginX;
    private float buttonx, buttonx2;

    @Override
    public void setup(PApplet parent) {
        marginX = (float) parent.width/4;
        buttonx2 = parent.width - marginX;
        buttonx = buttonx2 - buttonWidth;
        plt = new SubPlot(window, viewport, parent.width, parent.height);
        initialize();
    }

    @Override
    public void draw(PApplet parent, float dt) {
        float[] bb = plt.getBoundingBox();
        parent.fill(25, 150, 210);
        parent.rect(bb[0], bb[1], bb[2], bb[3]);

        float[] earth = plt.getPixelCoord(-15, 2);
        parent.fill(140, 80, 10);
        parent.rect(earth[0], earth[1], parent.width, parent.height);

        parent.pushMatrix();
        turtle1.setPose(startingPosTree1, PApplet.radians(90), plt, parent);
        turtle1.render(lSystem1, plt, parent);
        parent.popMatrix();

        parent.pushMatrix();
        turtle2.setPose(startingPosTree2, PApplet.radians(90), plt, parent);
        turtle2.render(lSystem2, plt, parent);
        parent.popMatrix();

        parent.fill(120);
        parent.rect(marginX, marginY, buttonWidth, buttonHeight);
        parent.rect(buttonx, marginY, buttonWidth, buttonHeight);

        parent.fill(255);
        parent.textSize(11);
        parent.text("Left Generation++", marginX + 10, marginY + 20);
        parent.text("Right Generation++", buttonx + 10, marginY + 20);
        parent.textSize(20);
        parent.text("R -> Reset turtles", 30, parent.height - 40);
    }

    public boolean isMouseOverButton(PApplet p, float a, float b, float c, float d){
        return (p.mouseX>= a && p.mouseX<=b && p.mouseY>=c && p.mouseY<=d);
    }

    public void initialize(){
        startingPosTree1 = new PVector(-8,2);
        startingPosTree2 = new PVector(8,2);

        Rule[] rules1 = new Rule[1];
        rules1[0] = new Rule('F', "FF+[+F-F-F]-[-F+F+F]");

        lSystem1 = new LSystem("F", rules1);
        turtle1 = new Turtle(5, PApplet.radians(22.5f));

        Rule[] rules2 = new Rule[2];
        rules2[0] = new Rule('X', "F[+X][-X]FX");
        rules2[1] = new Rule('F', "FF");

        lSystem2 = new LSystem("X", rules2);
        turtle2 = new Turtle(8, PApplet.radians(25.7f));
    }

    @Override
    public void keyPressed(PApplet parent) {
        if (parent.key=='r' ||parent.key=='R'){
            initialize();
        }
    }

    @Override
    public void mousePressed(PApplet parent) {
        if (isMouseOverButton(parent, marginX, marginX+buttonWidth, marginY, marginY +buttonHeight)) {
            lSystem1.nextGeneration();
            turtle1.scaling(0.5f);
        }
        if (isMouseOverButton(parent, buttonx, buttonx2, marginY, marginY +buttonHeight)) {
            lSystem2.nextGeneration();
            turtle2.scaling(0.5f);
        }

    }

    @Override
    public void mouseReleased(PApplet p) {

    }

    @Override
    public void mouseDragged(PApplet p) {

    }
}
