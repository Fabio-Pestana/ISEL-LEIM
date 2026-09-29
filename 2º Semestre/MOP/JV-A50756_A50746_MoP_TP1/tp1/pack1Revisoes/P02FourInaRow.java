//package tp1.pack1Revisoes;

import java.util.Scanner;

public class P02FourInaRow {

    private static void showboard(char[][] board) {
        String outside_board = "+-------------+";
        System.out.println(outside_board);
        for (int i =0;i<board.length;i++) //print do board
        {
            System.out.print("| ");
            for (int j=0; j< board[0].length;j++)
            {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println(outside_board);
    }

    /**
     * Puts one piece for the received player. First asks the user to choose one
     * column, then validates it and repeat it until a valid column is chosen.
     * Finally, puts the player character on top of selected column.
     *
     * @param player   The player: 'A' or 'B'. Put this character on the board
     * @param board    The board
     * @param keyboard The keyboard Scanner
     * @return The column selected by the user.
     */
    private static int play(char player, char[][] board, Scanner keyboard) {
        int c;
        do{
            System.out.print("Choose a columm (Player "+ player+ " ): ");
            c = keyboard.nextInt()-1;//para assim obter o valor desejado pelo player

        } while(c>=board[0].length || c<0 || board[0][c] != '0');//impede que o player coloque valores fora do indice pretendido do board
                                                                 //ou que coloque informaçao numa coluna que se encontra cheia
        for (int i = board.length-1; i>=0; i--)
        {
            if (board[i][c] == '0')
            {
                board[i][c]=player;
                break; //para apenas mudar um '0'
            }
        }
        return c;
    }

    /**
     * Checks if the player, with the character on top on the received column, won
     * the game or not. It will get the top move on that column, and check if there
     * are 4 pieces in a row, in relation to that piece and from the same player.
     * Returns true is yes, false is not.
     *
     * @param board The board
     * @param col   The last played column
     * @return True is that player won the game, or false if not.
     */
    private static boolean lastPlayerWon(char[][] board, int col)
    {
        //Vertical
        for (int i = board.length-1; i>=3; i--)//O i só vai até metade da linha,
            // pois quando o indice é 3 ele já irá testar as restantes linhas
        {
            if ((board[i][col]=='A' && board[i-1][col]=='A' && board[i-2][col]=='A' && board[i-3][col]=='A')||(board[i][col]=='B' && board[i-1][col]=='B' && board[i-2][col]=='B' && board[i-3][col]=='B'))
                return true;

        }

        //Horizontal
        for (int i = 0; i < board.length; i++) {
            //vai se verificar se existe uma sequência de 4 letras em cada linha.
            for (int c=0; c<board[0].length-3; c++){
                if (board[i][c] == 'A' && board[i][c+1] == 'A' && board[i][c+2] == 'A' && board[i][c+3] == 'A' ||
                        board[i][c] == 'B' && board[i][c+1] == 'B' && board[i][c+2] == 'B' && board[i][c+3] == 'B'){
                    return true;
                }
            }
        }

        // Dividiu-se o metodo da diagonal em dois para maior facilidade. Desta forma verificaremos metade da diagonal
        // para cima onde subimos uma coluna e uma casa p/ a direita. Depois faremos o mesmo, mas para da matade da
        // linha para baixo.

        //Diagonal Direita -> Esq /
        for (int i=3; i<board.length; i++) {
            for (int c = 0; c < board[0].length-3; c++) {
                if (board[i][c] == 'A' && board[i - 1][c + 1] == 'A' && board[i - 2][c + 2] == 'A' && board[i - 3][c + 3] == 'A' ||
                        board[i][c] == 'B' && board[i - 1][c + 1] == 'B' && board[i - 2][c + 2] == 'B' && board[i - 3][c + 3] == 'B') {
                    return true;
                }
            }
        }

        //Diagonal Esq -> Direita \
        for (int i=0; i<board.length-3; i++) {
            for (int c = 0; c < board[0].length-3; c++) {
                if (board[i][c] == 'A' && board[i + 1][c + 1] == 'A' && board[i + 2][c + 2] == 'A' && board[i + 3][c + 3] == 'A' ||
                        board[i][c] == 'B' && board[i + 1][c + 1] == 'B' && board[i + 2][c + 2] == 'B' && board[i + 3][c + 3] == 'B') {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if there are at least one free position on board.
     *
     * @param board The board
     * @return True if there is, at least, one free position on board
     */
    private static boolean existsFreePlaces(char[][] board) {
        for (int i = 0; i<board.length; i++)
        {
            for (int j = 0; j<board[0].length ; j++)
            {
                if(board[i][j]=='0') // o 0 é um espaço livre, logo se existir e devolvido true
                    return true;
            }
        }
        return false;
    }

    /**
     * Main method - this method should not be changed
     */
    public static void main(String[] args) {
        final int NCOLs = 7;
        final int NROWS = 6;

        // program variables
        Scanner keyboard = new Scanner(System.in);
        char[][] board = new char[NCOLs][NROWS];
        for (int i = 0; i< board.length;i++) //Adicinou-se este ciclo for para adicionar os 0´s ao tabuleiro.
        {
            for (int j = 0; j<board[0].length;j++)
            {
                board [i][j]='0';
            }
        }
        char winner = ' ';

        // show empty board
        showboard(board);

        // game cycle
        do {
            int col = play('A', board, keyboard);
            showboard(board);
            if (lastPlayerWon(board, col)) {
                winner = 'A';
                break;
            }
            if (!existsFreePlaces(board))
                break;

            col = play('B', board, keyboard);
            showboard(board);
            if (lastPlayerWon(board, col)) {
                winner = 'B';
                break;
            }

        } while (existsFreePlaces(board));

        // show final result
        switch (winner) {
            case ' ':
                System.out.println("We have a draw....");
                break;
            case 'A':
                System.out.println("Winner: Player A. Congratulations...");
                break;
            case 'B':
                System.out.println("Winner: Player B. Congratulations...");
                break;
        }

        // close keyboard
        keyboard.close();
    }
}