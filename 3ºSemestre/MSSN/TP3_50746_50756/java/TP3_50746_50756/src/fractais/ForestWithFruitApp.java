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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForestWithFruitApp implements IProcessingApp {

    private double[] window = {-15, 15, 0, 15};
    private float[] viewport = { 0, 0, 1, 1};
    private SubPlot plt;
    private List<Tree> forest;

    private final PVector[] pos = {new PVector(-8,4), new PVector(8,4), new PVector(-4,6), new PVector(4,6)};

    @Override
    public void setup(PApplet parent) {
        plt = new SubPlot(window, viewport, parent.width, parent.height);
        forest = new ArrayList<Tree>();
        initialize(parent);
    }

    @Override
    public void draw(PApplet parent, float dt) {
        float[] bb = plt.getBoundingBox();
        parent.fill(25, 150, 210);
        parent.rect(bb[0], bb[1], bb[2], bb[3]);

        float[] earth = plt.getPixelCoord(-15, 8);
        parent.fill(140, 80, 10);
        parent.rect(earth[0], earth[1], parent.width, parent.height);

        for (Tree tree: forest){
            tree.grow(dt);
            tree.display(parent, plt);
        }

        parent.fill(255);
        parent.textSize(18);
        parent.text("R -> Reset World", 10, 20);
        parent.text("Clique na Terra para plantar arvores", 10, 40);
        parent.text("Clique no Ceu para criar estrelas", 10, 60);
    }

    public void initialize(PApplet parent){
        Rule[] rules = new Rule[2];
        rules[0] = new Rule('F', "G[+F]-F");
        rules[1] = new Rule('G', "GG");

        for (int i = 0; i< pos.length; i++){
            Tree tree = new Tree("F", rules, pos[i], .8f, PApplet.radians(22.5f), 4, 0.5f, 2f, parent);
            tree.getTurtle().setFruitOrLeave(true);
            forest.add(tree);
        }
    }

    public boolean isMouseOverButton(PApplet p, float a, float b, float c, float d){
        return (p.mouseX>= a && p.mouseX<=b && p.mouseY>=c && p.mouseY<=d);
    }

    @Override
    public void keyPressed(PApplet parent) {
        if (parent.key=='r' || parent.key=='R'){
            Iterator<Tree> iterator = forest.iterator();
            while (iterator.hasNext()) {
                Tree tree = iterator.next();
                iterator.remove();
            }
            initialize(parent);
        }
    }

    @Override
    public void mousePressed(PApplet parent) {
        float[] earth = plt.getPixelCoord(-15, 8);
        double[] w =  plt.getWorldCoord(parent.mouseX, parent.mouseY);
        PVector pos = new PVector((float) w[0], (float) w[1]);
        Tree tree;
        if (isMouseOverButton(parent, earth[0] , parent.width, earth[1], parent.height)){
            Rule[] rules = new Rule[2];
            rules[0] = new Rule('F', "G[+F]-F");
            rules[1] = new Rule('G', "GG");
            tree = new Tree("F", rules, pos, parent.random(.2f, 1f), PApplet.radians(22.5f), 4, 0.5f, 1f, parent);
            tree.getTurtle().setFruitOrLeave(true);
            forest.add(tree);
        } else {
            Rule[] rules = new Rule[1];
            rules[0] = new Rule('F', "F-F++F-F");
            tree = new Tree("F++F++F", rules, pos, parent.random(.05f, .1f), PApplet.radians(60f), 2, 0.5f, 1f, parent);
            forest.add(tree);
        }
    }

    @Override
    public void mouseReleased(PApplet p) {
    }

    @Override
    public void mouseDragged(PApplet p) {
    }
}
