/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

import processing.core.PApplet;

import static processing.core.PApplet.map;

public class Ex_B_2 implements IProcessingApp{
    @Override
    public void setup(PApplet parent) {
        parent.background(150);
    }

    @Override
    public void draw(PApplet parent, float dt) {
        float mouseX = parent.mouseX;
        float mouseY = parent.mouseY;

        //enquanto o cursor sobe na janela,o background obtem valores mais amarelados
        float yColorRed = map(mouseY, parent.height, 0, 0, 250);
        float yColorGreen = map(mouseY, parent.height, 0, 0, 200);

        //enquanto o cursor desce na janela,o background fica azul
        float yColorBlue = map(mouseY, 0, parent.height, 0, 240);

        parent.background(parent.color(yColorRed, yColorGreen, yColorBlue));

        //corpo da estrela
        parent.fill(parent.color(255, 255, 0));
        parent.beginShape();
        parent.vertex(497,401);
        parent.vertex(574, 141);
        parent.vertex(663, 401);
        parent.vertex(887, 412);
        parent.vertex(702, 572);
        parent.vertex(768, 832);
        parent.vertex(575,682);
        parent.vertex(380,832);
        parent.vertex(449, 572);
        parent.vertex(261, 410);
        parent.vertex(497,401);
        parent.endShape();
        parent.stroke(0);

        //olhos
        parent.fill(255);
        parent.ellipse(497,401, 100, 100);
        parent.ellipse(663, 401, 100, 100);

        //pupila que segue o rato
        float xLeft = map(mouseX, 0, parent.width, 467, 527);
        float xRight = map(mouseX, 0, parent.width, 633, 693);
        float yEyes = map(mouseY, 0, parent.height, 376, 440);

        parent.fill(0);
        parent.ellipse(xLeft, yEyes, 20, 20);//497, 401
        parent.ellipse(xRight, yEyes, 20, 20);//663 401

        //sobrancelhas que sobem ou descem dependendo da posicao do rato em y
        float ytriLeft2 = map(mouseY, 0, parent.width, 310, 330);
        float ytriRight2 = map(mouseY, 0, parent.width, 310, 330);
        float ytriLeft1 = map(mouseY, 0, parent.width, 323, 343);
        float ytriRight1 = map(mouseY, 0, parent.width, 317, 327);
        float ytriLeft3 = map(mouseY, 0, parent.width, 340, 360);
        float ytriRight3 = map(mouseY, 0, parent.width, 331, 351);

        parent.fill(0);
        parent.triangle(548,(int)ytriLeft1, 489, (int)ytriLeft2, 428, (int)ytriLeft3);
        parent.triangle(611,(int)ytriRight1,672,(int)ytriRight2,708,(int)ytriRight3);

        //Sorri quando o cursor está na parte superior da janela
        //Perde o sorriso na parte inferior e fica triste
        int mudanca = parent.height/2;

        if (mouseY<=mudanca){

            //boca
            parent.fill(0);
            parent.beginShape();
            parent.vertex(616, 590);
            parent.bezierVertex(568, 583, 536, 569, 497, 550);
            parent.vertex(497, 550);//l2 l1
            parent.bezierVertex(511, 628, 538, 625, 616, 590);
            parent.stroke(0);
            parent.endShape();

            //dentes
            parent.fill(255);
            parent.beginShape();
            parent.vertex(492, 514);
            parent.bezierVertex(544, 547, 576, 561, 673, 560);
            parent.vertex(616, 590);
            parent.bezierVertex(568, 583, 536, 569, 497, 550);
            parent.vertex(492, 514);
            parent.stroke(0);
            parent.endShape();

            //lingua
            parent.fill(parent.color(255, 0, 0));
            parent.beginShape();
            parent.vertex(508, 588);
            parent.bezierVertex(526, 590, 540, 598, 538, 612);
            parent.vertex(538, 612);
            parent.bezierVertex(525, 614, 516, 603, 509, 588);
            parent.endShape();
        }else {

            //boca
            float posBoca = map(mouseY, 0, parent.width, 550, 610);
            parent.fill(0);
            parent.beginShape();
            parent.vertex(500, posBoca);
            parent.bezierVertex(520, 550, 560, 550, 580, posBoca);
            parent.vertex(580, posBoca);
            parent.vertex(500, posBoca);
            parent.endShape();
        }
    }

    @Override
    public void keyPressed(PApplet parent) {
    }

    @Override
    public void mousePressed(PApplet parent) {
    }
}
