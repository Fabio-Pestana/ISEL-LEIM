/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

package ac;

import processing.core.PApplet;
import setup.IProcessingApp;

public class TesteCA implements IProcessingApp {

    private int numLinhas = 50;
    private int numColunas = 50;

    private boolean check = true;

    private GameOfLife ca;

    @Override
    public void setup(PApplet parent) {
        ca = new GameOfLife(parent, numLinhas, numColunas);
        parent.frameRate(10);
    }

    @Override
    public void draw(PApplet parent, float dt) {
        if (check) {
            ca.update();
            ca.display(parent);
        }
    }

    @Override
    public void keyPressed(PApplet parent) {
        if (parent.key == ' '){
            if(check)
                check=false;
            else check = true;
        }
        if (parent.key == 'R' || parent.key =='r'){
            ca.initRandom();
        }
        if (parent.key == 'B' || parent.key =='b'){
            ca.allCellStateZero();
        }
        if (parent.key == '6'){
            System.out.println("23/36");
            ca.setRuleSet(2);
        }
        if (parent.key == '3'){
            System.out.println("23/3");
            ca.setRuleSet(1);
        }
        if (parent.key == 'M' || parent.key == 'm'){
            System.out.println("Regra da maioria");
            ca.setRuleSet(3);
        }
    }

    @Override
    public void mousePressed(PApplet p) {

        // Altera o estado da cell clicada
        Cell cell = ca.pixel2Cell(p.mouseX, p.mouseY);
        int currentState = cell.getState();
        int newState = (currentState == 0) ? 1 : 0;  //aletra estado

        cell.setState(newState);
        ca.setCell(p.mouseX, p.mouseY, cell);
        ca.display(p);
    }
}