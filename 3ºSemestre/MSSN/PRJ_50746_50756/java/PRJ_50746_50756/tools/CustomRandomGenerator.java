package tools;

public class CustomRandomGenerator {

    private double[] cdf;

    /**
     * Construtor da classe CustomRandomGenerator
     *
     * @param pmf A Função de Massa de Probabilidade representando as probabilidades de diferentes elementos.
     *            A soma de todos os elementos na PMF deve ser igual a 1.
     */
    public CustomRandomGenerator(double[] pmf){
        cdf = new double[pmf.length];
        cdf[0] = pmf[0];
        // Calcula a Função de Distribuição a partir da PMF fornecida.
        for (int i = 1; i < pmf.length ; i++){
            cdf[i] = cdf[i-1] + pmf[i];
        }
    }

    /**
     * Gera um elemento aleatorio com base na PMF previamente fornecida.
     *
     * @return Um inteiro representando o elemento aleatorio.
     */
    public int getRandomClass(){
        double r = Math.random();
        int val = 0;
        for (int i = 0; i < cdf.length; i++){
            if (r > cdf[i]) {
                val++;
            }else break;
        }
        return val;
    }
}
