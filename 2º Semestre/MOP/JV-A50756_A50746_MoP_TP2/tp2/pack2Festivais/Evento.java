package tp2.pack2Festivais;


abstract class Evento {
     String nome;

    /**
     * Construtor da classe Evento.
     * @param nome O nome do evento.
     */
    public Evento(String nome)
    {
        this.nome = nome;
    }

    /**
     * Método abstrato que retorna o número de bilhetes disponíveis para o evento.
     * @return O número de bilhetes do evento.
     */
    public abstract int getNumBilhetes();

    /**
     * Método abstrato que retorna um array com os nomes dos artistas que irão atuar no evento.
     * @return Um array com os nomes dos artistas que irão atuar no evento.
     */
    public abstract String[] getArtistas();

    /**
     * Método abstrato que retorna o número de atuações que um determinado artista terá no evento.
     * @param artista O nome do artista a pesquisar.
     * @return O número de atuações que o artista terá no evento.
     */
    public abstract int numActuacoes(String artista);

    /**
     * Método que retorna uma string representando as informações do evento, como pedido no enunciado.
     * @return Uma string representando as informações do evento.
     */
    public String toString()
    {
        String string_final ="";
        string_final = this.nome + " com " + getNumBilhetes() + " e com os artista(s) " + String.join(", ", getArtistas());
        return string_final;
    }
}
