package fractais;
/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

public class Rule {

    private char simbolo;

    private String sequencia;

    public Rule(char simbolo, String sequencia) {
        this.simbolo = simbolo;
        this.sequencia = sequencia;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public String getSequencia() {
        return sequencia;
    }
}
