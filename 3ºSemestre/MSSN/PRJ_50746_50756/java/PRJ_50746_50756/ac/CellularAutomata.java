package ac;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.CustomRandomGenerator;
import tools.SubPlot;

public class CellularAutomata {
    protected int linhas;
    protected int colunas;
    protected int nStates;
    private int raioVizinhanca;
    protected Cell [][] cells;
    private int[] colors;

    private String[] imageSrcs;
    protected float cellAltura;
    protected float cellLargura;
    protected float xmin, ymin;
    private SubPlot plt;

    protected PApplet p;

    /**
     * Construtor da classe CellularAutomata.
     * @param p       Objeto PApplet usado para desenhar.
     * @param plt     Subplot associado ao automato celular.
     * @param row     Numero de linhas.
     * @param col     Numero de colunas.
     * @param nStates Numero de estados possiveis para cada celula.
     * @param raio    Raio da vizinhança de cada celula.
     */
    public CellularAutomata(PApplet p, SubPlot plt, int row, int col, int nStates, int raio) {
        this.linhas = row;
        this.colunas = col;
        this.nStates = nStates;
        this.raioVizinhanca = raio;
        float[] bb = plt.getBoundingBox();
        this.xmin = bb[0];
        this.ymin = bb[1];
        cells = new Cell[linhas][colunas];
        colors = new int[nStates];
        imageSrcs = new String[nStates];
        cellLargura = bb[2]/colunas;
        cellAltura =  bb[3]/linhas;
        this.plt = plt;
        this.p = p;
        createCells();
        setStateColors(p);
        initRandom();
    }

    /**
     * Inicializa as cores associadas aos estados do automato com valores aleatorios.
     * @param p Objeto PApplet usado para desenhar.
     */
    public void setStateColors(PApplet p){
        for (int i = 0; i<nStates; i++){
            colors[i] = p.color(p.random(255), p.random(255), p.random(255));
        }
    }

    public void setStateColors(int[] colors){
        this.colors = colors;
    }

    public int[] getStateColors() {
        return colors;
    }

    public String[] getImageSrcs() {
        return imageSrcs;
    }

    public void setImageSrcs(String[] imageSrcs) {
        this.imageSrcs = imageSrcs;
    }

    /**
     * Cria as celulas no automato.
     */
    protected void createCells(){
        for (int i=0; i<linhas;i++){
            for(int j=0;j<colunas;j++){
                cells[i][j] = new Cell(this, i, j);
            }
        }
        this.setVizinhanca();
    }

    /**
     * Inicializa os estados das celulas com valores aleatorios.
     */
    public void initRandom(){
        for (int i=0; i<linhas;i++) {
            for (int j = 0; j < colunas; j++) {
                cells[i][j].setState((int) (this.nStates * Math.random()));
            }
        }
    }

    /**
     * Inicializa os estados das celulas com base em uma distribuicao de probabilidade customizada.
     * @param pmf Um array de probabilidades.
     */
    public void initRandomCustom(double[] pmf){
        CustomRandomGenerator crg = new CustomRandomGenerator(pmf);
        for (int i=0; i<linhas;i++) {
            for (int j = 0; j < colunas; j++) {
                cells[i][j].setState(crg.getRandomClass());
            }
        }
    }

    /**
     * Define a vizinhança de cada celula no automato.
     */
    protected void setVizinhanca(){
        int nVizinhos = (int) Math.pow(2* raioVizinhanca +1,2);

        for (int i=0; i<linhas;i++){
            for(int j=0;j<colunas;j++){
                Cell[] vizinhosCell = new Cell[nVizinhos];
                int n=0;

                for (int k = -raioVizinhanca; k<= raioVizinhanca; k++) {
                    int linhaVizinho = (i + k + linhas) % linhas;
                    for (int l = -raioVizinhanca; l<= raioVizinhanca; l++) {
                        int colunaVizinho = (j + l + colunas) % colunas;
                        vizinhosCell[n++] = cells [linhaVizinho][colunaVizinho];
                    }
                }
                cells[i][j].setVizinhos(vizinhosCell);
            }
        }
    }

    /**
     * Exibe a representacao grafica do automato celular.
     * @param p Objeto PApplet usado para desenhar.
     */
    public void display(PApplet p) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                cells[i][j].display(p);
            }
        }
    }

    /**
     * Obtem o centro de uma cell especifica no AC.
     * @param linha  Linha da celula.
     * @param coluna Coluna da celula.
     * @return Um vetor PVector representando as coordenadas do centro da celula no mundo.
     */
    public PVector getCenterCell(int linha, int coluna){
        float x = (linha + 0.5f) * cellLargura;
        float y = (coluna + 0.5f) * cellLargura;
        double[] w = plt.getWorldCoord(x,y);
        return new PVector((float) w[0], (float) w[1]);
    }

    /**
     * Converte coordenadas do mundo para uma cell especifica no AC.
     * @param x Coordenada x no mundo.
     * @param y Coordenada y no mundo.
     * @return A cell correspondente.
     */
    public Cell world2Cell(double x, double y){
        float[] xy = plt.getPixelCoord(x, y);
        return pixel2Cell(xy[0], xy[1]);
    }

    /**
     * Converte coordenadas de pixels para uma cell especifica no AC.
     * @param x Coordenada x em pixels.
     * @param y Coordenada y em pixels.
     * @return A cell correspondente.
     */
    public Cell pixel2Cell(float x, float y){
        int row = (int)((y-ymin)/cellAltura);
        int col = (int)((x - xmin)/cellLargura);
        if (row >= linhas) row = linhas-1;
        if (col >= colunas) col = colunas-1;
        if (row < 0) row = 0;
        if (col < 0) col = 0;
        return cells[row][col];
    }
}
