/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

package ac;

import processing.core.PApplet;

public class GameOfLife {

    private int linhas;
    private int colunas;
    Cell [][] cells;
    Cell [][] Newcells;
    private int cellAltura;
    private int cellLargura;

    private int raioVizinhanca = 1;

    private int RuleSet;

    public int isRuleSet() {
        return RuleSet;
    }

    public void setRuleSet(int ruleSet) {
        RuleSet = ruleSet;
    }

    public GameOfLife(PApplet p, int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        cells = new Cell[linhas][colunas];
        Newcells = new Cell[linhas][colunas];
        cellLargura = p.width/colunas;
        cellAltura = p.height/linhas;
        RuleSet=1;
        createCells();
        initRandom();
    }

    public int getCellAltura() {
        return cellAltura;
    }

    public void setCellAltura(int cellAltura) {
        this.cellAltura = cellAltura;
    }

    public int getCellLargura() {
        return cellLargura;
    }

    public void setCellLargura(int cellLargura) {
        this.cellLargura = cellLargura;
    }

    public void createCells(){
        for (int i=0; i<linhas;i++){
            for(int j=0;j<colunas;j++){
                cells[i][j] = new Cell(this, i, j);
            }
        }
        this.setVizinhanca();
    }

    public void setVizinhanca(){
        int nVizinhos = (int) Math.pow(2* raioVizinhanca +1,2);

        for (int i=0; i<linhas;i++){
            for(int j=0;j<colunas;j++){
                Cell[] vizinhosCell = new Cell[nVizinhos];
                int n=0;

                for (int k = -raioVizinhanca; k<= raioVizinhanca; k++) {
                    for (int l = -raioVizinhanca; l<= raioVizinhanca; l++) {

                        int linhaVizinho = (i + k + linhas) % linhas;
                        int colunaVizinho = (j + l + colunas) % colunas;

                        vizinhosCell[n++] = cells [linhaVizinho][colunaVizinho];
                    }
                }
                cells[i][j].setVizinhos(vizinhosCell);
            }
        }
    }

    public void initRandom(){
        int nStates = 2;
        for (int i=0; i<linhas;i++) {
            for (int j = 0; j < colunas; j++) {
                cells[i][j].setState((int) ((nStates) * Math.random()));
            }
        }
    }

    public void allCellStateZero(){
        for (int i=0; i<linhas;i++) {
            for (int j = 0; j < colunas; j++) {
                cells[i][j].setState(0);
            }
        }
    }

    public void update() {
        Cell[][] newCells = new Cell[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                int aliveNeighbors = cells[i][j].getAliveNeighbors();
                newCells[i][j] = new Cell(this, i, j);
                newCells[i][j].setState(rules(aliveNeighbors, cells[i][j]));
                newCells[i][j].setPreviousState(cells[i][j].getState());
            }
        }

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                cells[i][j] = newCells[i][j];
            }
        }
        this.setVizinhanca();
    }


    public int rules(int vizinhos, Cell cell){
        // se o int está a 1 faz a variante 23/3
        // se o int está a 2 faz a variante 23/36
        // se não aplica a regra da maioria

        if (this.isRuleSet()==1) { //variante 23/3
            if (vizinhos < 2 && cell.getState() == 1) return 0;
            if (vizinhos > 3 && cell.getState() == 1) return 0;
            if (vizinhos == 3 && cell.getState() == 0) return 1;
            return cell.getState();
        } else if (this.isRuleSet()==2) { //variante 23/36
            if ((vizinhos == 3 || vizinhos == 6) && cell.getState() == 0) return 1;
            if (vizinhos < 2 && cell.getState() == 1) return 0;
            if (vizinhos > 3 && cell.getState() == 1) return 0;
            return cell.getState();
        }
        //Regra da maioria
        int nVizinhos = (int) Math.pow(2* raioVizinhanca +1,2) -1;
        int diferenca = nVizinhos - vizinhos;
        if (diferenca > nVizinhos/2){ // Neste caso quando o valor de vizinhos vivos e mortos são iguais a cell nasce
            return 0;
        }else return 1;
    }
    public void display(PApplet p) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                cells[i][j].display(p);
            }
        }
    }

    public void setCell(int x, int y,Cell c){
        int row = y/cellAltura;
        int col = x/cellLargura;
        cells[row][col] = c;
        //System.out.println(row +" " + col);
    }

    public Cell pixel2Cell(int x, int y){
        int row = y/cellAltura;
        int col = x/cellLargura;
        if (row>=linhas) row = linhas-1;
        if (col>=colunas) col = colunas-1;
        //System.out.println(row +" " + col);
        return cells[row][col];
    }


}