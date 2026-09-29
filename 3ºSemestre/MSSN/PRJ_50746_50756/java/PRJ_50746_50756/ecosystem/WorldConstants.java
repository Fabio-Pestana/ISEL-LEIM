package ecosystem;

import processing.core.PApplet;

public class WorldConstants {

    //Mundo
    public final static double[] WINDOW = {-10, 10, -10, 10};

    //Terreno
    public final static int LINHAS = 20;
    public final static int COLUNAS = 20;

    public static enum PatchType{
        EMPTY, OBSTACLE, FERTILE, FOOD
    }

    public final static double[] PATCH_TYPE_PROB = {.25f, .15f, .1f, .5f}; // 1 -> 100
    public final static int NSTATES = PatchType.values().length;
    public static int [][] TERRAIN_COLORS = {
            {200 + 50, 200, 60}, {160, 30, 70}, {200, 200, 60}, {40, 200, 20},  {0, 0, 200}
    };

    public static String[] TERRAIN_IMAGES = {
            "assets/empty_terrain.png", "assets/stone.png", "assets/terrain.png", "assets/food.png"
    };

    public final static float[] REGENERATION_TIME = {10.f, 20.f}; //seconds

    //Prey Population

    public final static float PREY_SIZE = .4f;
    public final static float PREY_MASS = 1f;
    public final static int INI_PREY_POPULATION = 2;
    public final static float INI_PREY_ENERGY = 15f;
    public final static float ENERGY_FROM_PLANT = 4f;
    public final static float PREY_ENERGY_TO_REPRODUCE = 25f;
    public static int[] PREY_COLOR = {80, 100, 220};
    public static int PREY_OBSTACLE_EFFECT = 50;
    public static String PREY_SRC = "assets/zebra.svg";


    //Predator Population

    public final static float PREDATOR_SIZE = .5f;
    public final static float PREDATOR_MASS = 1.5f;
    public final static int INI_PREDATOR_POPULATION = 10;
    public final static float INI_PREDATOR_ENERGY = 50f;
    public final static float ENERGY_FROM_PREY = 12f;
    public final static float ENERGY_LION_HUNTER = 15f;

    public final static float PREDATOR_ENERGY_TO_REPRODUCE = 65f;
    public static int[] PREDATOR_COLOR = {200, 50, 50};
    public static double PREDATOR_GENDER_PROBABILITY = 0.5;
    public static int PREDATOR_OBSTACLE_EFFECT = 20;
    public static String PREDATOR_MALE_SRC = "assets/lion_male.svg";
    public static String PREDATOR_FEMALE_SRC = "assets/lion_female.svg";

    //Hunters
    public static String HUNTER_SRC = "assets/hunter.svg";
    public static String HUNTER_PLANT_SRC = "assets/hunter_plant.svg";
    public static String HUNTER_STONE_SRC = "assets/hunter_stone.svg";

    public final static float HUNTER_SIZE = .5f;
    public final static float HUNTER_MASS = 2f;
    public final static int INI_HUNTER_POPULATION = 5;
    public final static float INI_HUNTER_ENERGY = 60f;
    public static double KILL_LION_PROBABILITY = 0.6;
    public static double KILL_ZEBRA_PROBABILITY = 0.5;
    public final static float ENERGY_FROM_LION = 20f;

    public static float[] DEATH_CONTROL = {PApplet.radians(90), PApplet.radians(40), .2f, 2};

    public final static float TIME_PS = 8f;

    public static int[] HUNTER_COLOR = {200, 150, 80};

    //Player

    public static String PLAYER_SRC = "assets/hero.svg";

    public final static float PLAYER_SIZE = .5f;
    public final static float PLAYER_MASS = 1.7f;
    public final static float INI_PLAYER_ENERGY = 100f;
    public final static float PLAYER_SPEED = 2.5f;
    public final static float ENERGY_FROM_HUNTER = 40f;
    public static int[] PLAYER_COLOR = {255, 0, 0};
}
