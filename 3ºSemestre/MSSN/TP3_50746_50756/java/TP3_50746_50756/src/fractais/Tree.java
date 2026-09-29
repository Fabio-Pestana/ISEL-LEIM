package fractais;

/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

public class Tree {
    private LSystem ls;
    private Turtle turtle;
    private PVector position;
    private float length;
    private float growthRate;
    private int numberOfSeasonsToGrow;
    private float scalingFactor;
    private float intervalBetweenSeasons;
    private float now;
    private float nextSeasonTime;
    private PApplet p;

    public Tree(String axiom, Rule[] rules, PVector position, float referenceLength, float angle, int nIterations, float scalingFactor, float interval, PApplet p) {
        this.ls = new LSystem(axiom, rules);
        this.turtle = new Turtle(0, angle);

        this.length = 0;
        growthRate = referenceLength/interval;

        this.position = position;

        this.numberOfSeasonsToGrow = nIterations;
        this.scalingFactor = scalingFactor;
        this.intervalBetweenSeasons = interval;
        this.now = p.millis()/1000f;
        this.nextSeasonTime = now + intervalBetweenSeasons;
        this.p = p;
    }

    public void grow(float dt) {
        now += dt;
        if (now < nextSeasonTime) {
            length += growthRate * dt;
            turtle.setLen(length);
        } else if (ls.getGeneration() < numberOfSeasonsToGrow) {
            ls.nextGeneration();
            length *= scalingFactor;
            growthRate *= scalingFactor;
            turtle.setLen(length);
            nextSeasonTime = now + intervalBetweenSeasons;
        }
    }

    public LSystem getLs() {
        return ls;
    }

    public Turtle getTurtle() {
        return turtle;
    }

    public void display(PApplet p, SubPlot plt){
        p.pushMatrix();
        turtle.setPose(position, (float) Math.PI/2, plt, p);
        turtle.render(ls, plt, p);
        p.popMatrix();
    }
}
