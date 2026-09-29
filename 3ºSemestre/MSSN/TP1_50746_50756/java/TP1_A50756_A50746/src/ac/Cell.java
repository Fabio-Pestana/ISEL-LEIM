/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

package ac;

import processing.core.PApplet;

public class Cell {
    private int linha;
    private int coluna;
    private int state;
    private int previousState;
    private Cell[] vizinhos;
    private int raioVizinhanca = 1;

    private GameOfLife ca;
    public Cell(GameOfLife ca, int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
        this.state = 0; // Initial state (0: Dead, 1: Alive)
        this.previousState = 0;
        this.vizinhos = new Cell[(int) Math.pow(2* raioVizinhanca +1,2)];
        this.ca = ca;
    }

    public int getState() {
        return state;
    }

    public void setPreviousState(int previousState) {
        this.previousState = previousState;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Cell[] getVizinhos() {
        return vizinhos;
    }

    public void setVizinhos(Cell[] vizinhos) {
        this.vizinhos = vizinhos;
    }

    public void display(PApplet p) {
        p.stroke(25);
        if (this.state==1 && this.previousState==0){  //Quando nascem ficam brancas
            p.fill(255);
        } else if (this.state==1 && this.previousState==1){ //Cells vivas ficam verdes
            p.fill(p.color(0,255,0));
        } else if (this.state==0 && this.previousState==1){ //Quando morrem ficam vermelhas
            p.fill(p.color(255,0,0));
        } else p.fill(0); //Cells que continuam mortas continuam pretas

        p.rect(coluna * ca.getCellLargura(), linha * ca.getCellAltura(), ca.getCellLargura(), ca.getCellAltura());
    }

    public int getAliveNeighbors() {
        int aliveNeighbors = 0;
        for (Cell neighbor : vizinhos) {
            if (neighbor!=null) {
                if (neighbor.getState() == 1) aliveNeighbors++;
            }
        }

        if (this.getState() == 1) {
            aliveNeighbors = aliveNeighbors - 1;
        }
        return aliveNeighbors;
    }
}