package ecosystem;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import setup.IProcessingApp;
import tools.SubPlot;
import processing.sound.*;

public class GameApp implements IProcessingApp {

    private float[] viewport = {.05f, .05f, .7f, .9f};

    private float[] viewport2 = { .04f, .04f, .72f, .92f};

    private SubPlot plt, moldura;

    private Terrain t;
    private Ecosystem population;

    private float positionImg;
    private boolean startMenu;
    private boolean game;
    private PImage menuImage;
    private PImage banner, zebra, lionF, lionM, hunter, player;
    private PImage retry, soundPlay, sound, soundStop, heart, noHeart, info;

    private PImage hunterStone, hunterPlant;
    private float xbanner, btnReset_y, btnPlay_y;
    private SoundFile fileMenu, fileGame;

    private boolean gameOver, infoPanel, creditsPanel;


    @Override
    public void setup(PApplet parent) {
        // Load the images
        menuImage = parent.loadImage("assets/menu.png");
        banner = parent.loadImage("assets/background3.png");
        zebra = parent.loadImage("assets/zebra.png");
        lionF = parent.loadImage("assets/lionFemale.png");
        lionM = parent.loadImage("assets/lionMale.png");
        hunter = parent.loadImage("assets/Hunter.png");
        hunterPlant = parent.loadImage("assets/hide_hunter.png");
        hunterStone = parent.loadImage("assets/hideStone_hunter.png");
        player = parent.loadImage("assets/player.png");
        retry =  parent.loadImage("assets/retry.png");
        heart =  parent.loadImage("assets/heartRed.png");
        noHeart =  parent.loadImage("assets/heartBlack.png");
        soundPlay = parent.loadImage("assets/sound_Play.png");
        soundStop = parent.loadImage("assets/sound_Stop.png");
        info = parent.loadImage("assets/info.png");
        sound = soundPlay;

        // Load the sounds
        fileMenu = new SoundFile(parent, "assets/menuSound.wav");
        fileGame = new SoundFile(parent, "assets/gameSound.wav");
        fileMenu.loop();
        fileMenu.amp(.3f);
        fileGame.amp(.15f);

        info.resize(parent.width, parent.height);
        banner.resize(parent.width, parent.height);
        menuImage.resize(parent.width, parent.height);
        parent.background(menuImage);

        xbanner = (float) (parent.width - (parent.width*0.25));
        btnReset_y = parent.height - parent.height/4;
        btnPlay_y = parent.height - parent.height/3;
        positionImg = parent.width * 0.2f;

        initialize(parent);

        startMenu = true;
        game = false;
        gameOver = false;
        infoPanel = false;
        creditsPanel = false;
    }

    @Override
    public void draw(PApplet parent, float dt) {

        if(startMenu){
            if (!infoPanel && !creditsPanel){
                startMenu(parent);
            }else if (infoPanel && !creditsPanel){
                infoMenu(parent);
            }else if (creditsPanel && !infoPanel){
                creditsMenu(parent);
            }
        }else {
            if (fileMenu.isPlaying()){
                fileMenu.stop();
                fileGame.loop();
            }
            parent.background(banner);

            float[] bb = moldura.getBoundingBox();
            parent.fill(0);
            parent.rect(bb[0], bb[1], bb[2], bb[3]);

            if (!gameOver){
                t.regenerate();
                population.update(dt, t);

                t.display(parent);
                population.display(parent, plt);
            }else{
                parent.fill(parent.color(255, 0, 0));
                PFont font = parent.createFont("MS GOTHIC", 50);
                parent.textFont(font);
                parent.text("Game Over", bb[0] + bb[2]/2, bb[1] + bb[3]/2);
            }


            parent.fill(parent.random(170, 200));
            PFont startFont = parent.createFont("MS GOTHIC", 21);
            parent.textFont(startFont);
            parent.text("Click in the figures\nto add more animals!", xbanner + 135, parent.height/4 + 50);

            parent.pushStyle();
            parent.image(zebra, xbanner + 20, parent.height/2 - 40, 40, 40);
            parent.image(lionM, xbanner + 85, parent.height/2 - 40, 40, 40);
            parent.image(lionF, xbanner + 115, parent.height/2 - 40, 40, 40);
            parent.image(hunter, xbanner + 190, parent.height/2 - 40, 30, 30);
            parent.image(sound, parent.width * 0.85f, parent.height - 100, 40, 30);

            parent.fill(255);
            parent.text(population.getNumPreys(), xbanner + 40, parent.height/2);
            parent.text(population.getNumPredators(), xbanner + 125, parent.height/2);
            parent.text(population.getNumHunters(), xbanner + 205, parent.height/2);

            HoverButton(parent, xbanner + 55, xbanner + 55 + 130, btnPlay_y, btnPlay_y+50);
            parent.rect(xbanner + 55, btnPlay_y,130, 50);

            HoverButton(parent, xbanner + 55, xbanner + 55 + 130, btnReset_y, btnReset_y+50);
            parent.rect(xbanner + 55, btnReset_y,130, 50);

            parent.fill(255);
            startFont = parent.createFont("MS GOTHIC", 16);
            parent.textFont(startFont);

            if (!isMouseOverButton(parent, xbanner + 55, xbanner + 55 + 130, btnPlay_y, btnPlay_y+50)){
                parent.text("Play Game", xbanner + 120, btnPlay_y + 25);
            }else{
                parent.image(player, xbanner + 105, btnPlay_y + 10, 30, 30);
            }

            if (!isMouseOverButton(parent, xbanner + 55, xbanner + 55 + 130, btnReset_y, btnReset_y + 50)){
                parent.text("Reset Ecosystem", xbanner + 120, btnReset_y + 25);
            }else{
                parent.image(retry, xbanner + 100, btnReset_y + 5, 40, 40);
            }

            parent.popStyle();

            if (game){
                runGame(parent);
            }
        }
    }

    @Override
    public void keyPressed(PApplet parent) {

        // Quando a Space Bar e pressionada passamos do menu para o jogo em si
        // Se ja tivermos no jogo e se o player estiver vivo ao pressionar nesta tecla
        // matamos animais e cacadores se eles estiverem proximos
        if (parent.key == ' ') {
            startMenu = false;
            if (population.getPlayer()!= null){
                population.getPlayer().setKeyPressed(true);

            }
        }
    }

    @Override
    public void mousePressed(PApplet parent) {
        //panel da informacao
        if (isMouseOverButton(parent, parent.width/2 - 70, parent.width/2 + 70, parent.height * 0.9f - 20, parent.height * 0.9f +20)) {
            infoPanel = true;
        }
        //volta ao menu
        if (isMouseOverButton(parent, parent.width * 0.9f, parent.width * 0.9f + 40, parent.height * 0.9f, parent.height * 0.9f + 40)) {
            if (infoPanel)
                infoPanel = false;
            if (creditsPanel)
                creditsPanel=false;
        }
        //panel dos creditos
        if (isMouseOverButton(parent, parent.width/2 - 50, parent.width/2 + 70, parent.height * 0.95f - 20, parent.height * 0.95f +20)){
            creditsPanel = true;
        }

        // Reload do ecossistema sem o player e cacadores
        if (isMouseOverButton(parent, xbanner + 55, xbanner + 55 + 130, btnReset_y, btnReset_y + 50)) {
            initialize(parent);
            game = false;
            gameOver = false;
        }

        // Adiciona uma presa
        if (isMouseOverButton(parent, xbanner + 20, xbanner + 20 + 40, parent.height/2 - 40, parent.height/2)) {
            population.addPrey(parent, plt, t);
        }

        // Adiciona uma predador
        if (isMouseOverButton(parent, xbanner + 85, xbanner + 115 + 40, parent.height/2 - 40, parent.height/2)) {
            population.addPredator(parent, plt, t);
        }

        // Adiciona um cacador
        if (isMouseOverButton(parent, xbanner + 190, xbanner + 230, parent.height/2 - 40, parent.height/2)) {
            population.addHunter(parent, plt, t);
        }

        //inicia o jogo com o player e cacadores
        if (isMouseOverButton(parent, xbanner + 55, xbanner + 55 + 130, btnPlay_y, btnPlay_y+50)) {
            game = true;
            if (gameOver){
                initialize(parent);
                gameOver=false;
            }

            if (population.getNumHunters() == 0){
                population.initializeHunters(parent, plt, t);
            }

            if (population.getNumPlayer()!=1){
                population.initializePlayer(parent, plt);
            }
        }

        // Adiciona ou tira o som de fundo
        if (isMouseOverButton(parent, parent.width * 0.85f, (parent.width * 0.85f) + 40 , parent.height - 100, parent.height - 70)) {
            if (fileGame.isPlaying()){
                sound = soundStop;
                fileGame.pause();
            }else {
                sound = soundPlay;
                fileGame.loop();
            }
        }

        // Atualiza a posicao do body que o player faz seek
        if (population.getPlayer()!= null){
            population.getPlayer().playerMouse();
        }
    }

    @Override
    public void mouseReleased(PApplet p) {

    }

    @Override
    public void mouseDragged(PApplet p) {

    }

    /**
     * Obtem as cores correspondentes aos estados do terreno no formato de array de inteiros.
     * As cores sao obtidas a partir das constantes definidas em {@link WorldConstants}.
     * @param p O objeto PApplet.
     * @return Um array de inteiros contendo as cores correspondentes aos estados do terreno.
     */
    private int[] getColors(PApplet p){
        int[] colors = new int[WorldConstants.NSTATES];
        for (int i = 0; i<WorldConstants.NSTATES; i++){
            colors[i] = p.color(WorldConstants.TERRAIN_COLORS[i][0], WorldConstants.TERRAIN_COLORS[i][1], WorldConstants.TERRAIN_COLORS[i][2]);
        }
        return colors;
    }

    /**
     * Obtem os caminhos das imagens correspondentes aos estados do terreno no formato de array de strings.
     * Os caminhos sao obtidos a partir das constantes definidas em {@link WorldConstants}.
     * @return Um array de strings contendo os caminhos das imagens correspondentes aos estados do terreno.
     */
    private String[] getImages(){
        String[] img = new String[WorldConstants.NSTATES];
        for (int i = 0; i<WorldConstants.NSTATES; i++){
            img[i] = WorldConstants.TERRAIN_IMAGES[i];
        }
        return img;
    }

    /**
     * Inicializa os elementos essenciais do jogo, como sons, populacao e terreno e os subplots.
     * @param parent O objeto PApplet.
     */
    public void initialize(PApplet parent){
        plt = new SubPlot(WorldConstants.WINDOW, viewport, parent.width, parent.height);
        moldura = new SubPlot(WorldConstants.WINDOW, viewport2, parent.width, parent.height);

        t = new Terrain(parent, plt);
        t.setStateColors(getColors(parent));
        t.setImageSrcs(getImages());
        t.initRandomCustom(WorldConstants.PATCH_TYPE_PROB);
        for (int i=0;i<2;i++){
            t.majorityRule();
        }
        population = new Ecosystem(parent, plt, t);
    }

    /**
     * Verifica se o mouse esta sobre um determinado retangulo.
     * @param p O objeto PApplet.
     * @param a A coordenada x do canto superior esquerdo do retangulo.
     * @param b A coordenada x do canto inferior direito do retangulo.
     * @param c A coordenada y do canto superior esquerdo do retangulo.
     * @param d A coordenada y do canto inferior direito do retangulo.
     * @return true se o mouse estiver sobre o retangulo, false caso contrario.
     */
    public boolean isMouseOverButton(PApplet p, float a, float b, float c, float d){
        return (p.mouseX >= a && p.mouseX <= b && p.mouseY >= c && p.mouseY <= d);
    }

    /**
     * Altera a cor de preenchimento quando o mouse está sobre um retangulo..
     * @param p O objeto PApplet.
     * @param a A coordenada x do canto superior esquerdo do retangulo.
     * @param b A coordenada x do canto inferior direito do retangulo.
     * @param c A coordenada y do canto superior esquerdo do retangulo.
     * @param d A coordenada y do canto inferior direito do retangulo.
     * @return true se o mouse estiver sobre o retangulo, false caso contrario.
     */
    public void HoverButton(PApplet p, float a, float b, float c, float d){
        p.fill(120);
        if (isMouseOverButton(p, a, b, c, d)) {
            p.fill(50);
        }
    }

    /**
     * Executa as operacoes necessarias durante o jogo, como exibicao de vidas do player, energia e outros elementos do jogo.
     * @param parent O objeto PApplet.
     */
    public void runGame(PApplet parent){
        for (int i=0; i<3; i++)
        {
            parent.image(noHeart, xbanner + 20 + 45*i, parent.height*0.15f, 40, 40);
        }
        parent.pushStyle();
        parent.fill(255);
        PFont startFont = parent.createFont("MS GOTHIC", 16);
        parent.textFont(startFont);
        parent.text("Energy Levels:",xbanner + 75, parent.height*0.08f);
        parent.fill(0);
        parent.rect(xbanner + 20, parent.height*0.1f, 200, 20);

        if (population.getNumHunters() == 0){
            population.initializeHunters(parent, plt, t);
        }

        if (population.getPlayer()!= null){
            int aux = population.getPlayer().getLifes();
            float energy = population.getPlayer().energy;
            for (int i=0; i<aux; i++){
                parent.image(heart, xbanner + 20 + 45*i, parent.height*0.15f, 40, 40);
            }
            float rectBar = PApplet.map(energy, 0, 200, 0, 100);

            if (!gameOver){
                parent.fill(parent.color(255, 0, 0));
                parent.rect(xbanner + 20, parent.height*0.1f, rectBar, 20);
            }

            if (population.getNumAnimals() == 0){
                population.initializePopulations(parent, plt, t);
            }

            if (aux <= 0){
                gameOver = true;
            }
        }else{
            gameOver = true;
        }
        parent.popStyle();
    }

    /**
     * Exibe informacoes sobre os diferentes elementos do ecossistema no menu de informacoes.
     * @param parent O objeto PApplet.
     */
    public void infoMenu(PApplet parent){
        parent.background(info);
        parent.image(zebra, positionImg, parent.height * 0.2f, 40, 40);
        parent.image(lionM, positionImg - 25, parent.height* 0.4f, 40, 40);
        parent.image(lionF, positionImg + 25, parent.height* 0.4f, 40, 40);
        parent.image(hunter, positionImg, parent.height* 0.6f, 40, 40);
        parent.image(hunterPlant, positionImg - 50, parent.height* 0.6f, 40, 40);
        parent.image(hunterStone, positionImg + 50, parent.height* 0.6f, 40, 40);
        parent.image(player, positionImg,  parent.height* 0.8f, 40, 40);

        PFont startFont = parent.createFont("MS GOTHIC", 20);
        parent.textFont(startFont);
        parent.fill(255);
        parent.text("Prey that runs away from predators, evades obstacles \n and eats the grass on the ground", parent.width * 0.6f, parent.height * 0.2f + 10);
        parent.text("Predator that hunts preys and evades obstacles", parent.width * 0.6f, parent.height * 0.4f + 10);
        parent.text("Hunters hunt the animals, can be killed by lions or \n the player and \"can be one with the habitat\"", parent.width * 0.6f, parent.height * 0.6f + 10);
        parent.text("Protector of the savanna, save animals from hunters and \n if you kill animals you lose a life. \n" +
                "Be careful, you only have 3 lives! \n The player moves by pressing the mouse and \n kills with the space bar.", parent.width * 0.6f, parent.height * 0.8f + 30);

        parent.image(retry, parent.width * 0.9f, parent.height * 0.9f, 40, 40);
    }

    /**
     * Exibe os creditos do projeto (nomes dos autores do projeto).
     * @param parent O objeto PApplet.
     */
    public void creditsMenu(PApplet parent){
        parent.background(info);
        parent.image(zebra, parent.width/2 - 205, parent.height * 0.3f, 50, 50);
        parent.image(lionM, parent.width/2 - 145, parent.height* 0.3f, 50, 50);
        parent.image(lionF, parent.width/2 - 85, parent.height* 0.3f, 50, 50);
        parent.image(hunter, parent.width/2 + 95, parent.height* 0.3f, 50, 50);
        parent.image(hunterPlant, parent.width/2 + 155, parent.height* 0.3f, 50, 50);
        parent.image(hunterStone, parent.width/2 + 35, parent.height* 0.3f, 50, 50);
        parent.image(player, parent.width/2 - 25,  parent.height* 0.3f, 50, 50);

        PFont startFont = parent.createFont("MS GOTHIC", 40);
        parent.textFont(startFont);

        parent.fill(255);
        parent.text("Credits", parent.width * 0.5f, parent.height * 0.25f);

        startFont = parent.createFont("MS GOTHIC", 30);
        parent.textFont(startFont);
        parent.text("Made by ISEL students:", parent.width * 0.5f, parent.height * 0.42f);

        startFont = parent.createFont("MS GOTHIC", 24);
        parent.textFont(startFont);

        parent.text("Fabio Pestana - A50756", parent.width * 0.5f, parent.height * 0.5f );
        parent.text("Miguel Alcobia - A50746", parent.width * 0.5f, parent.height * 0.5f + 50);

        parent.image(retry, parent.width * 0.9f, parent.height * 0.9f, 40, 40);
    }

    /**
     * Exibe o menu inicial do jogo.
     * @param p O objeto PApplet.
     */
    public void startMenu(PApplet p) {

        p.background(menuImage);

        PFont titleFont = p.createFont("MS GOTHIC", 40);
        p.textFont(titleFont);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);

        p.fill(0);

        String title = "Protector of the Savanna";
        float titleX = p.width / 2;
        float titleY = p.height / 2 + p.height / 4;
        p.text(title, titleX, titleY);  // Move title up

        PFont startFont = p.createFont("MS GOTHIC", 24);
        p.textFont(startFont);

        p.fill(0);

        String startText = "Press Space to Start";
        float startX = p.width / 2;
        float startY = p.height / 2 + p.height / 3;  // Move text down
        p.text(startText, startX, startY);

        p.pushStyle();
        if (isMouseOverButton(p, startX - 70, startX + 70, p.height * 0.9f - 20, p.height * 0.9f +20)) {
            p.fill(125);
        }else {
            p.fill(0);
        }
        p.text("Information", startX, p.height * 0.9f);
        p.popStyle();

        p.pushStyle();
        if (isMouseOverButton(p, startX - 50, startX + 70, p.height * 0.95f - 20, p.height * 0.95f +20)) {
            p.fill(125);
        }else {
            p.fill(0);
        }
        p.text("Credits", startX, p.height * 0.95f);
        p.popStyle();
    }
}
