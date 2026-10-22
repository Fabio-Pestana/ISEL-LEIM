package server;

/**
 * A interface BoardGame define o contrato para um jogo de tabuleiro genérico.
 * Um jogo de tabuleiro deve implementar esta interface para fornecer funcionalidades
 * como fazer uma jogada, verificar vitória, verificar empate e verificar se o jogo
 * terminou.
 */
public interface BoardGame {

	 /**
     * Realiza uma jogada no tabuleiro nas coordenadas especificadas pelo jogador
     * com o símbolo fornecido.
     *
     * @param lin    A linha onde a jogada será feita.
     * @param col    A coluna onde a jogada será feita.
     * @param symbol O símbolo do jogador fazendo a jogada.
     * @return true se a jogada for válida e bem-sucedida, false caso contrário.
     */
    boolean joga(int lin, int col, String symbol);
    
    /**
     * Verifica se o jogador com o símbolo fornecido venceu o jogo.
     *
     * @param symbol O símbolo do jogador a ser verificado.
     * @return true se o jogador venceu o jogo, false caso contrário.
     */
    boolean vitoria(String symbol);
    
    /**
     * Verifica se o jogo terminou em empate.
     *
     * @return true se o jogo terminou em empate, false caso contrário.
     */
    boolean empate();
    
    /**
     * Verifica se o jogo terminou (ou seja, se houve um vencedor ou empate).
     *
     * @return true se o jogo terminou, false caso contrário.
     */
    boolean terminou();
}
