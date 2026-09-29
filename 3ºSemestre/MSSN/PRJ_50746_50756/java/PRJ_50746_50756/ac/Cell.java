/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

package ac;

import processing.core.PApplet;
import processing.core.PImage;

public class Cell {
    private int linha;
    private int coluna;
    protected int state;
    private Cell[] vizinhos;
    private int raioVizinhanca = 1;

    protected CellularAutomata ca;

    private PImage cellImg;

    /**
     * Construtor da classe Cell
     * @param ca    Automato celular ao qual a cell pertence.
     * @param linha Linha da cell.
     * @param coluna Coluna da cell.
     */
    public Cell(CellularAutomata ca, int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
        this.state = 0;
        this.vizinhos = new Cell[(int) Math.pow(2* raioVizinhanca +1,2)];
        this.ca = ca;
        this.cellImg = null;
    }

    /**
     * Exibe a representacao grafica da celula.
     * @param p Objeto PApplet usado para desenhar.
     */
    public void display(PApplet p) {
        p.pushStyle();
        p.noStroke();
        if (cellImg == null){
            setCellImg();
        }
        p.image(cellImg, ca.xmin + coluna * ca.cellLargura, ca.ymin +  linha * ca.cellAltura, ca.cellLargura, ca.cellAltura);
        p.popStyle();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCellImg() {
        this.cellImg = ca.p.loadImage(ca.getImageSrcs()[state]);
    }

    public Cell[] getVizinhos() {
        return vizinhos;
    }

    public void setVizinhos(Cell[] vizinhos) {
        this.vizinhos = vizinhos;
    }
}