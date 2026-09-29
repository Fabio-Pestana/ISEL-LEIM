package ac;

import tools.Histogram;

public class MajorityCell extends Cell{

    private Histogram hist;

    /**
     * Construtor da classe MajorityCell
     * @param ca    Automato celular ao qual a cell pertence.
     * @param linha Linha da cell.
     * @param coluna Coluna da cell.
     */
    public MajorityCell(CellularAutomata ca, int linha, int coluna) {
        super(ca, linha, coluna);
    }

    /**
     * Calcula o histograma dos estados dos vizinhos.
     */
    public void computeHistogram(){
        Cell[] vizinhos = getVizinhos();
        int[] data = new int[vizinhos.length];
        for (int i=0; i<vizinhos.length; i++){
            data[i] = vizinhos[i].getState();
        }
        hist = new Histogram(data, ca.nStates);
    }

    /**
     * Aplica a regra da maioria, atualizando o estado da cell se necessario.
     * @return true se o estado da celula foi alterado, false caso contrario.
     */
    public boolean applyMajorityRule(){
        int mode = hist.getMode(0);
        boolean changed = false;
        if (getState() != mode){
            setState(mode);
            setCellImg();
            changed = true;
        }
        return changed;
    }
}
