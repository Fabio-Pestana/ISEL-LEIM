package ecosystem;

import ac.Cell;
import ac.MajorityCA;
import physics.Body;
import processing.core.PApplet;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Terrain extends MajorityCA {

    /**
     * Construtor da classe Terrain.
     * @param p   Referencia ao objeto PApplet, usado para renderizaçao grafica.
     * @param plt Referencia ao subplot que define as dimensoes e a posicao do terreno.
     */
    public Terrain(PApplet p, SubPlot plt) {
        super(p, plt, WorldConstants.LINHAS, WorldConstants.COLUNAS, WorldConstants.NSTATES, 1);
    }

    /**
     * Cria as celulas do terreno, sendo algumas do tipo Patch com tempo de regeneracao aleatorio.
     */
    @Override
    protected void createCells(){
        int minRT = (int) (WorldConstants.REGENERATION_TIME[0] * 1000);
        int maxRT = (int) (WorldConstants.REGENERATION_TIME[1] * 1000);
        for (int i=0; i<linhas;i++){
            for(int j=0;j<colunas;j++){
                int timeToGrow = (int) (minRT + (maxRT - minRT) * Math.random());
                cells[i][j] = new Patch(this, i, j, timeToGrow);
            }
        }
        setVizinhanca();
    }

    /**
     * Regenera todas as celulas do tipo Patch no terreno.
     */
    public void regenerate(){
        for (int i=0; i<linhas;i++){
            for(int j=0;j<colunas;j++){
                ((Patch)cells[i][j]).regenerate();
            }
        }
    }

    /**
     * Obtem uma lista de corpos representando os obstaculos no terreno.
     * @return Lista de corpos representando os obstaculos no terreno.
     */
    public List<Body> getObstacles(){
        List<Body> bodies = new ArrayList<Body>();
        for (int i=0; i<linhas;i++){
            for(int j=0;j<colunas;j++){
               if (cells[i][j].getState() == WorldConstants.PatchType.OBSTACLE.ordinal()){
                   Body b = new Body(this.getCenterCell(i,j));
                   bodies.add(b);
               }
            }
        }
        return bodies;
    }
}
