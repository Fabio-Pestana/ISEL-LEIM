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
import java.util.List;

public class ForestApp implements IProcessingApp {
    private double[] window = {-15, 15, 0, 15};
    private float[] viewport = { 0, 0, 1, 1};
    private SubPlot plt;
    private List<Tree> forest;

    @Override
    public void setup(PApplet parent) {
        plt = new SubPlot(window, viewport, parent.width, parent.height);
        forest = new ArrayList<Tree>();
    }

    @Override
    public void draw(PApplet parent, float dt) {
        float[] bb = plt.getBoundingBox();
        parent.rect(bb[0], bb[1], bb[2], bb[3]);

        for (Tree tree: forest){
            tree.grow(dt);
            tree.display(parent, plt);
        }
    }


    @Override
    public void mousePressed(PApplet parent) {
        double[] w =  plt.getWorldCoord(parent.mouseX, parent.mouseY);
        PVector pos = new PVector((float) w[0], (float) w[1]);
        Tree tree;
        if (parent.random(100) < 50){
            Rule[] rules = new Rule[1];
            rules[0] = new Rule('F', "FF+[+F-F-F]-[-F+F+F]");
            tree = new Tree("F", rules, pos, .4f, PApplet.radians(22.5f), 3, 0.5f, 2f, parent);
        } else {
            Rule[] rules = new Rule[2];
            rules[0] = new Rule('X', "F+[[X]-X]-F[-FX]+X");
            rules[1] = new Rule('F', "FF");
            tree = new Tree("X", rules, pos, .4f, PApplet.radians(25), 5, 0.5f, 2f, parent);
        }
        forest.add(tree);
    }

    @Override
    public void keyPressed(PApplet parent) {
    }

    @Override
    public void mouseReleased(PApplet p) {

    }

    @Override
    public void mouseDragged(PApplet p) {

    }
}
