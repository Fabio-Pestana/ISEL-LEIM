package server;

/**
 * Classe que representa uma versão do jogo Othello com funcionalidades adicionais para manipulação de XML.
 * Herda da classe Othello.
 */
public class OthelloXML extends Othello{
	
	/**
     * Estado do jogo após a última jogada.
     * Possíveis valores:
     * - "ND": Nada a registar, continua o jogo.
     * - "IV": Jogada inválida.
     * - "VX": Vitória do X.
     * - "VO": Vitória do O.
     * - "SJ": Sem Jogadas
     * - "EM": Empate.
     */
	private String estado = "ND";
	
	
	/**
     * Converte o tabuleiro atual para uma representação em XML, incluindo o estado do jogo.
     *
     * @param symbol O símbolo do jogador atual.
     * @return Uma string representando o tabuleiro em XML.
     */
	public String tabuleiroToXML(String symbol) {
		atualizarEstadoTabuleiro(symbol);
        String tab = "<tabuleiro simbolo ='"+ symbol
        		+"' estado='" + estado + "'>";
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].equals("  ")) {
                    if (isValidMove(i, j, symbol)) {
                        tab += "<casa simbolo='p '/>";
                    } else {
                        tab += "<casa simbolo='" + board[i][j] + "'/>";
                    }
                } else {
                    tab += "<casa simbolo='" + board[i][j] + "'/>";
                }
            }
        }
        return tab += "</tabuleiro>";
    }
	
	/**
     * Realiza uma jogada no tabuleiro e atualiza o estado do jogo.
     *
     * @param lin    A linha onde a jogada será feita.
     * @param col    A coluna onde a jogada será feita.
     * @param symbol O símbolo do jogador que está realizando a jogada.
     * @return true se a jogada foi válida e false caso contrário.
     */
	public boolean joga(int lin, int col, String symbol) {
		this.estado = "ND"; // Nada a registar, continua o jogo.

		// Verifica se a jogada é válida no jogo base.
		if (!super.joga(lin, col, symbol)) {
			if(getValidMoves(symbol).length == 0) {
				estado = "SJ"; // Sem Jogadas
			}else {
				estado = "IV"; // Jogada inválida.
			}
			return false;
		}

		if(terminou()) {
			// Verifica se há vitória.
			if (vitoria(pecasPretas))
				estado = "VX"; 	// Vitória do X.
			else if (vitoria(pecasBrancas))
				estado = "VO"; 	// Vitória do O.
			else if (empate())	// Verifica se há empate.
				estado = "EM"; 	// Empate.
		}
		return true;
	}
	
	/**
     * Atualiza o estado do jogo de acordo com as condições atuais do tabuleiro.
     *
     * @param symbol O símbolo do jogador atual.
     */
	private void atualizarEstadoTabuleiro(String symbol) {
		
		if(getValidMoves(symbol).length == 0) {
			this.estado = "SJ"; // Sem Jogadas
		}else {
			this.estado = "ND";
		}
		
		
		if(terminou()) {
			// Verifica se há vitória.
			if (vitoria(pecasPretas))
				this.estado = "VX"; 	// Vitória do X.
			else if (vitoria(pecasBrancas))
				this.estado = "VO"; 	// Vitória do O.
			else if (empate())	// Verifica se há empate.
				this.estado = "EM"; 	// Empate.
		}
	}
}
