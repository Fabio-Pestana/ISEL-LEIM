package tp2.pack2Festivais;

/**
 * Classe que representa um Espetáculo, que é um tipo de Evento.
 * Extende a classe abstrata Evento.
 */

import java.util.Arrays;

public class Espetaculo extends Evento
{
    private String[] artistas = new String[10];
    private int nArtistas;
    private int numBilhetes;
    private String localidade;

    /**
     * Construtor da classe Espetaculo.
     * Recebe como parâmetros o nome do espetáculo, o número de bilhetes e a localidade.
     * Chama o construtor da classe mãe Evento e inicializa as variáveis da classe.
     * @param nome o nome do espetáculo
     * @param numBilhetes o número de bilhetes disponíveis para o espetáculo
     * @param localidade a localidade do espetáculo
     */
    public Espetaculo(String nome, int numBilhetes, String localidade) {
        super(nome);
        this.numBilhetes = numBilhetes;
        this.localidade = localidade;
    }

    /**
     * Método que retorna o número de atuações de um artista no espetáculo.
     * Recebe como parâmetro o nome do artista.
     * Percorre o array de artistas e retorna 1 se o artista já pertencer ao espetáculo.
     * @param artista o nome do artista
     * @return o número de atuações do artista (0 ou 1)
     */
    @Override
    public int numActuacoes(String artista) {
        for (int i=0; i<nArtistas; i++)
        {
            if (this.artistas[i].equals(artista))
                return 1;
        }
        return 0;
    }

    /**
     * Método que adiciona um artista ao espetáculo.
     * Recebe como parâmetro o nome do artista.
     * Verifica se o nome do artista não é null, se ainda há espaço no array de artistas e se o nome do artista tem mais de 1 caracter.
     * Verifica também se o artista já pertence ao espetáculo.
     * Se todas as condições são satisfeitas, adiciona o artista ao array de artistas e incrementa o número de artistas.
     * @param artista o nome do artista
     * @return true se o artista foi adicionado com sucesso, false caso contrário
     */
    public boolean addArtista(String artista) {

        if (artista == null || nArtistas>=10 || artista.length()<=1 ) { // Vê se o numero de artista já atingiu o máximo (10)
            return false;
        }

        for (int i=0; i<nArtistas;i++)
        {
            if (this.artistas[i].equalsIgnoreCase(artista)) //verifica se o artista já pertence ao espetaculo
                return false;
        }

        artistas[nArtistas]=artista;
        nArtistas ++; //adiciona +1 artista para o numero de artistas
        return true;
    }

    /**
     * Método que retorna o número de bilhetes disponíveis para o espetáculo.
     * @return O número de bilhetes do espetáculo.
     */
    @Override
    public int getNumBilhetes() {
        return numBilhetes;
    }

    /**
     * Método que retorna um array com os nomes dos artistas que irão atuar no espetáculo.
     * @return Um array com os nomes dos artistas do espetáculo.
     */
    @Override
    public String[] getArtistas() {
        return Arrays.copyOf(artistas, nArtistas);
    }

    /**
     * Método que retorna uma string representando as informações do espetaculo, como pedido no enunciado.
     * @return Uma string representando as informações do espetaculo.
     */
    @Override
    public String toString()
    {
        String string_final ="";
        string_final = this.nome + " com " + getNumBilhetes() + " e com os artista(s) ";
        for (int i=0; i<nArtistas;i++){
            string_final = string_final + ", " + getArtistas()[i];
        }
        string_final = string_final +  ", em " + this.localidade;
        return string_final;
    }

    /**
     * Main, para testar o código
     */
    public static void main(String[] args)
    {
        Espetaculo NM = new Espetaculo("Noite Mágica", 200, "Diney, Paris");
        String a1 = "Baltazar";
        String a2 = "Mogli";
        String a3 = null;
        String a4 = "A";
        System.out.println("Artista " + a1 + " adicionado com exito -->"+ NM.addArtista(a1));
        System.out.println("Artista " + a2 + " adicionado com exito -->"+ NM.addArtista(a2));
        System.out.println("Artista " + a3 + " adicionado com exito -->"+ NM.addArtista(a3));
        System.out.println("Artista " + a4 + " adicionado com exito -->"+ NM.addArtista(a4));
        System.out.println( a1 + " numero de atuacoes ---> " + NM.numActuacoes(a1));
        System.out.println( "Olaf numero de atuacoes ---> " + NM.numActuacoes("Olaf"));
        System.out.println(NM);
    }

}
