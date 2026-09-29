package fractais;

/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

public class LSystem {

    private String sequence;

    private Rule[] ruleset;

    private int generation;

    public LSystem(String axiom, Rule[] ruleset) {
        this.sequence = axiom;
        this.ruleset = ruleset;
        this.generation = 0;
    }

    public String getSequence() {
        return sequence;
    }

    public int getGeneration() {
        return generation;
    }

    public void nextGeneration(){
        generation++;

        String nextGeneration = "";

        for (int i = 0; i<sequence.length(); i++){
            char c = sequence.charAt(i);
            String replace = "" + c;
            for (int j = 0; j< ruleset.length; j++){
                if (c == ruleset[j].getSimbolo()){
                    replace = ruleset[j].getSequencia();
                    break;
                }
            }
            nextGeneration = nextGeneration + replace;
        }
        this.sequence = nextGeneration;
    }
}
