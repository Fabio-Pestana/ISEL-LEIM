package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * A classe Othello implementa a lógica do jogo Othello (também conhecido como Reversi).
 * Este jogo de tabuleiro é jogado em um tabuleiro 8x8, onde os jogadores alternam entre
 * peças pretas e brancas, tentando capturar as peças do oponente ao colocar suas próprias
 * peças de forma que uma linha, coluna ou diagonal de peças do oponente seja encurralada
 * por peças do jogador atual.
 */
public class Othello implements BoardGame {
    protected final int BOARD_SIZE = 8;
    protected String[][] board;
    protected final String pecasPretas = "X ";
    protected final String pecasBrancas = "O ";

    /**
     * Construtor da classe Othello. Inicializa o tabuleiro com as peças iniciais.
     */
    public Othello() {
        board = new String[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = "  ";
            }
        }

       
        board[3][3] = pecasBrancas;
        board[3][4] = pecasPretas;
        board[4][3] = pecasPretas;
        board[4][4] = pecasBrancas;
        
       /*
        // exemplo de uma situacao de um jogador sem jogadas validas
        board[0][0] = pecasPretas;
        board[0][1] = pecasPretas;
        board[0][2] = pecasPretas;
        board[0][3] = pecasPretas;
        board[0][4] = pecasPretas;
        board[0][5] = pecasPretas;
        board[0][6] = pecasPretas;
        board[0][7] = pecasPretas;
        board[1][3] = pecasPretas;
        board[1][4] = pecasPretas;
        board[1][5] = pecasPretas;
        board[1][6] = pecasPretas;
        board[1][7] = pecasPretas;
        board[1][0] = pecasPretas;
        
        board[1][1] = pecasBrancas;
        board[2][1] = pecasBrancas;
        board[3][0] = pecasBrancas;	
        */
    }

    
    /**
     * Realiza uma jogada no tabuleiro nas coordenadas especificadas pelo jogador
     * com o símbolo fornecido.
     *
     * @param lin    A linha onde a jogada será feita.
     * @param col    A coluna onde a jogada será feita.
     * @param symbol O símbolo do jogador fazendo a jogada.
     * @return true se a jogada for válida e bem-sucedida, false caso contrário.
     */
    @Override
    public boolean joga(int lin, int col, String symbol) {

        if (getValidMoves(symbol).length == 0){
            return false;
        }

        if ((lin > BOARD_SIZE || lin < 1) || (col > BOARD_SIZE || col < 1))
            return false;

        int row = lin - 1;
        int column = col - 1;

        if (!board[row][column].equals("  "))
            return false;

        boolean moveIsValid = false;

        // Check em todas as direções por jogadas válidas
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) // exclui a propria celula
                    continue;

                if (isValidDirection(row, column, i, j, symbol)) {
                    moveIsValid = true;
                    flipPieces(row, column, i, j, symbol);
                }
            }
        }

        if (moveIsValid) {
            board[row][column] = symbol;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se uma determinada direção a partir de uma posição específica é válida para
     * capturar peças do oponente.
     *
     * @param row    A linha da posição inicial.
     * @param col    A coluna da posição inicial.
     * @param dRow   O deslocamento na direção das linhas (pode ser -1, 0 ou 1).
     * @param dCol   O deslocamento na direção das colunas (pode ser -1, 0 ou 1).
     * @param symbol O símbolo do jogador atual.
     * @return true se a direção é válida para capturar peças do oponente, false caso contrário.
     */
    private boolean isValidDirection(int row, int col, int dRow, int dCol, String symbol) {
        int newRow = row + dRow;
        int newCol = col + dCol;

        if (newRow < 0 || newRow >= BOARD_SIZE || newCol < 0 || newCol >= BOARD_SIZE || board[newRow][newCol].equals("  ") || board[newRow][newCol].equals(symbol))
            return false;

        while (true) {
            newRow += dRow;
            newCol += dCol;

            if (newRow < 0 || newRow >= BOARD_SIZE || newCol < 0 || newCol >= BOARD_SIZE)
                return false;

            if (board[newRow][newCol].equals(symbol))
                return true;

            if (board[newRow][newCol].equals("  "))
                return false;
        }
    }

    /**
     * Inverte as peças do oponente capturadas em uma determinada direção a partir de uma posição específica.
     *
     * @param row    A linha da posição inicial.
     * @param col    A coluna da posição inicial.
     * @param dRow   O deslocamento na direção das linhas (pode ser -1, 0 ou 1).
     * @param dCol   O deslocamento na direção das colunas (pode ser -1, 0 ou 1).
     * @param symbol O símbolo do jogador atual.
     */
    private void flipPieces(int row, int col, int dRow, int dCol, String symbol) {
        int newRow = row + dRow;
        int newCol = col + dCol;

        while (!board[newRow][newCol].equals(symbol)) {
            board[newRow][newCol] = symbol;
            newRow += dRow;
            newCol += dCol;
        }
    }

    /**
     * Verifica se o jogador com o símbolo fornecido venceu o jogo.
     *
     * @param symbol O símbolo do jogador a ser verificado.
     * @return true se o jogador venceu o jogo, false caso contrário.
     */
    @Override
    public boolean vitoria(String symbol) {
        int countPlayer = countP(symbol);
        String opponentSymbol = symbol.equals(pecasPretas) ? pecasBrancas : pecasPretas;
        int countOpponent = countP(opponentSymbol);

        return countPlayer > countOpponent;
    }

    /**
     * Verifica se o jogo terminou em empate.
     *
     * @return true se o jogo terminou em empate, false caso contrário.
     */
    @Override
    public boolean empate() {
        return countP(pecasPretas) == countP(pecasBrancas);
    }

    /**
     * Verifica se o jogo terminou (ou seja, se houve um vencedor ou empate).
     *
     * @return true se o jogo terminou, false caso contrário.
     */
    @Override
    public boolean terminou() {
        return (getValidMoves(pecasPretas).length == 0 && getValidMoves(pecasBrancas).length == 0);
    }

    /**
     * Imprime o tabuleiro atual do jogo Othello no console.
     */
    public void printBoard() {
        System.out.println("---------------------");
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i == 0) {
                for (int k = 0; k < BOARD_SIZE; k++) {
                    if (k == 0) {
                        System.out.print("    " + (k + 1));
                    } else {
                        System.out.print(" " + (k + 1));
                    }
                }
                System.out.println();
            }
            System.out.print("  " + (i + 1) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println("---------------------");
        System.out.println(pecasPretas + "counter -> " + countP(pecasPretas));
        System.out.println(pecasBrancas + "counter -> " + countP(pecasBrancas));
    }

    /**
     * Imprime o tabuleiro atual do jogo Othello no console, destacando as jogadas válidas para o jogador atual.
     *
     * @param symbol O símbolo do jogador atual.
     */
    public void printBoard(String symbol) {
        System.out.println("---------------------");

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i == 0) {
                for (int k = 0; k < BOARD_SIZE; k++) {
                    if (k == 0) {
                        System.out.print("    " + (k + 1));
                    } else {
                        System.out.print(" " + (k + 1));
                    }
                }
                System.out.println();
            }
            System.out.print("  " + (i + 1) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].equals("  ")) {
                    if (isValidMove(i, j, symbol)) {
                        // Se a célula estiver vazia e for uma jogada válida para o jogador atual, marca como "p"
                        System.out.print("p ");
                    } else {
                        System.out.print(board[i][j]);
                    }
                } else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println("---------------------");
        System.out.println(pecasPretas + "counter -> " + countP(pecasPretas));
        System.out.println(pecasBrancas + "counter -> " + countP(pecasBrancas));
        String[] validMoves = getValidMoves(symbol);
        System.out.println("Tem " + validMoves.length + " jogadas válidas (casas com o p)");
    }

    /**
     * Obtém uma lista de coordenadas das jogadas válidas para o jogador atual.
     *
     * @param symbol O símbolo do jogador atual.
     * @return Um array de Strings contendo as coordenadas das jogadas válidas.
     */
    public String[] getValidMoves(String symbol) {
        List<String> validMoves = new ArrayList<>();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // Verifica se a célula está vazia
                if (board[row][col].equals("  ")) {
                    // Verifica se o movimento é válido para esta célula
                    if (isValidMove(row, col, symbol)) {
                        // Adiciona a jogada válida à lista de jogadas válidas
                        validMoves.add("(" + (row + 1) + ", " + (col + 1) + ")");
                    }
                }
            }
        }

        // Converte a lista de jogadas válidas para um array de Strings
        return validMoves.toArray(new String[0]);
    }


    /**
     * Verifica se uma determinada jogada é válida para o jogador atual.
     *
     * @param row    A linha onde a jogada está sendo feita.
     * @param col    A coluna onde a jogada está sendo feita.
     * @param symbol O símbolo do jogador atual.
     * @return true se a jogada é válida, false caso contrário.
     */
    protected boolean isValidMove(int row, int col, String symbol) {
        // Verifica se a célula está vazia
        if (!board[row][col].equals("  "))
            return false;

        // Verifica em todas as oito direções
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                // Ignora a direção (0, 0) que representa a célula atual
                if (dRow == 0 && dCol == 0)
                    continue;

                // Verifica se é possível capturar peças na direção atual
                if (isValidDirection(row, col, dRow, dCol, symbol))
                    return true;
            }
        }

        // Se nenhum movimento válido foi encontrado em todas as direções, retorna falso
        return false;
    }

    /**
     * Conta o número de peças do jogador com o símbolo fornecido no tabuleiro.
     *
     * @param playerP O símbolo do jogador.
     * @return O número de peças do jogador.
     */
    public int countP(String playerP) {
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].equals(playerP)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Inicia e executa o jogo Othello, permitindo que os jogadores façam suas jogadas alternadamente
     * até que o jogo termine.
     */
    public void playGame(){
        Scanner scanner = new Scanner(System.in);

        String currentPlayer = pecasPretas;
        boolean gameOver = false;

        while (!gameOver) {
            System.out.println();
            System.out.println("É a vez do jogador " + currentPlayer);

            if (getValidMoves(currentPlayer).length == 0) {
                System.out.println("Não tem jogadas válidas.");
                currentPlayer = currentPlayer.equals(pecasPretas) ? pecasBrancas : pecasPretas;
                if (terminou()) gameOver = true;
            }else {
                printBoard(currentPlayer);
                // Obtém a entrada do jogador
                System.out.print("Digite a linha e a coluna (ex: 1 1): ");
                int row = scanner.nextInt();
                int col = scanner.nextInt();

                // Verifica se o movimento é válido e faz o movimento se for válido
                if (joga(row, col, currentPlayer)) {
                    // Verifica se o jogo terminou após o movimento
                    if (terminou()) {
                        gameOver = true;
                    } else {
                        // Alterna para o próximo jogador
                        currentPlayer = currentPlayer.equals(pecasPretas) ? pecasBrancas : pecasPretas;
                    }
                }else {
                    System.out.println("Movimento inválido. Tente novamente.");
                }
            }
        }
        System.out.println();
        // Verifica o resultado do jogo

        if (empate()){
            System.out.println("O jogo terminou em empate!");
        }else if(vitoria(pecasPretas)) {
            System.out.println("O jogador "+ pecasPretas +" venceu!");
        } else if (vitoria(pecasBrancas)) {
            System.out.println("O jogador "+ pecasBrancas +" venceu!");
        }


        System.out.println("Board final: ");
        printBoard();

        scanner.close();
    }

    public static void main(String[] args) {
            Othello othello = new Othello();
            othello.playGame();
    }
}
