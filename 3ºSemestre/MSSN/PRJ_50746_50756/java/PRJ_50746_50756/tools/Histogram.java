package tools;

public class Histogram {
    int[] hist;
    int nbins;

    /**
     * Construtor da classe Histogram
     *
     * @param data   Um array do conjunto de dados para o qual o histograma sera construido.
     * @param nbins  O numero de intervalos desejados para o histograma.
     */
    public Histogram(int[] data, int nbins){
        this.nbins = nbins;
        hist = new int[nbins];
        for (int i = 0; i<data.length;i++){
            hist[data[i]]++;
        }
    }

    /**
     * Obtem a moda do histograma.
     *
     * @param preference Um valor inteiro representando a preferencia para a moda.
     *                   Se a preferencia tiver a mesma contagem que a moda, esta e retornada.
     *                   Caso contrario, a moda do histograma e retornada.
     * @return A moda do histograma.
     */
    public int getMode(int preference){
        int maxVal = 0;
        int mode = 0;

        // Encontra o valor mais frequente no histograma.
        for (int i = 0; i < nbins; i++){
            if (hist[i] > maxVal){
                maxVal = hist[i];
                mode = i;
            }
        }

        if (hist[preference] == hist[mode])
            return preference;
        return mode;
    }
}
