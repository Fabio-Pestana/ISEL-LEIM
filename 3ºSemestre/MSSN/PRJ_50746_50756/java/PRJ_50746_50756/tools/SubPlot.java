package tools;

/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * ISEL - LEIM 23/24
 */

public class SubPlot {
    private double[] window;
    private float[] viewport;
    private double mx;
    private double bx;
    private double my;
    private double by;

    /**
     * Construtor da classe SubPlot
     * @param window   Um array de doubles representando as coordenadas da janela [xmin, xmax, ymin, ymax].
     * @param viewport Um array de floats representando as coordenadas da visualizacao [vxmin, vxmax, vymin, vymax].
     * @param fullwidth A largura da janela.
     * @param fullheight A altura da janela.
     */
    public SubPlot(double[] window, float[] viewport, float fullwidth, float fullheight) {
        this.window = window;
        this.viewport = viewport;
        mx = viewport[2] * fullwidth / (window[1]-window[0]);
        bx = viewport[0] * fullwidth;
        my = -viewport[3] * fullheight / (window[3]-window[2]);
        by = (1 - viewport[1]) * fullheight;
    }

    /**
     * Obtem as coordenadas de pixel correspondentes as coordenadas do mundo fornecidas.
     * @param x A coordenada x no mundo.
     * @param y A coordenada y no mundo.
     * @return Um array de floats contendo as coordenadas do pixel [x, y].
     */
    public float[] getPixelCoord(double x, double y){

        float[] coord = new float[2];

        coord[0] = (float) (bx + mx * (x - window[0]));
        coord[1] = (float) (by + my * (y - window[2]));

        return coord;
    }

    /**
     * Obtem as coordenadas do mundo correspondentes as coordenadas de pixel fornecidas.
     * @param xx A coordenada x em pixels.
     * @param yy A coordenada y em pixels.
     * @return Um array de double contendo as coordenadas do mundo [x, y].
     */
    public double[] getWorldCoord(float xx, float yy){
        double[] coord = new double[2];

        coord[0] = window[0] + (xx - bx) / mx;
        coord[1] = window[2] + (yy - by) / my;

        return coord;
    }

    /**
     * Obtem a caixa delimitadora do subplot em coordenadas de pixel.
     * @return Um array de floats contendo a bounding box [xmin, ymax, largura, altura].
     */
    public float[] getBoundingBox(){
        float[] c1 = getPixelCoord(window[0], window[2]);
        float[] c2 = getPixelCoord(window[1], window[3]);
        float[] box = {c1[0], c2[1], c2[0] - c1[0], c1[1] - c2[1]};

        return box;
    }

    /**
     * Obtem as coordenadas do vetor correspondentes as mudancas nas coordenadas do mundo fornecidas.
     * @param dx A mudança na coordenada x do mundo.
     * @param dy A mudança na coordenada y do mundo.
     * @return Um array de floats contendo as coordenadas do vetor [dx, dy].
     */
    public float[] getVectorCoord(double dx, double dy){
        float[] v = new float[2];
        v[0] = (float) (dx*mx);
        v[1] = (float) (-dy*my);
        return v;
    }

    public double[] getWindow() {
        return window;
    }

    public float[] getViewport(){
        return viewport;
    }
}
