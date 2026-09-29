package ecosystem;

import ac.CellularAutomata;
import ac.MajorityCell;

public class Patch extends MajorityCell {

    private long eatenTime;

    private int timeToGrow;

    /**
     * Construtor da classe Patch.
     * @param terrain Referencia ao terreno ao qual a celula pertence.
     * @param linha   Linha da celula.
     * @param coluna  Coluna da celula.
     * @param timeG   Tempo necessario para regeneracao da celula.
     */
    public Patch(Terrain terrain, int linha, int coluna, int timeG) {
        super(terrain, linha, coluna);
        this.timeToGrow = timeG;
        eatenTime = System.currentTimeMillis();
    }

    /**
     * Atualiza o estado da celula para FERTILE e redefine a imagem associada.
     */
    public void setFertile(){
        state = WorldConstants.PatchType.FERTILE.ordinal();
        setCellImg();
        eatenTime = System.currentTimeMillis();
    }

    /**
     * Regenera a celula se ela estiver no estado FERTILE e o tempo de regeneracao tiver passado.
     * Atualiza o estado da celula para FOOD e redefine a imagem associada.
     */
    public void regenerate(){
        if (state == WorldConstants.PatchType.FERTILE.ordinal() && System.currentTimeMillis() > (eatenTime + timeToGrow)){
            state = WorldConstants.PatchType.FOOD.ordinal();
            setCellImg();
        }
    }
}
