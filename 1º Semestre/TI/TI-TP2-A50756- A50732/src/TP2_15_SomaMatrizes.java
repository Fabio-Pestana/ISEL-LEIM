import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TP2_15_SomaMatrizes {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int i, j;
        System.out.print("Insira um valor n para definir o número de colunas e linhas das matrizes: ");
        int n = scanner.nextInt();
        System.out.println("");
        int[][] mat1 = new int[n][n];
        int[][] mat2 = new int[n][n];

        //preencher matriz1
        for (i = 0; i < mat1.length; i++) {
            for (j = 0; j < mat1[i].length; j++) {
                mat1[i][j] = random.nextInt(10);
            }
        }

        //preencher matriz2
        for (i = 0; i < mat2.length; i++) {
            for (j = 0; j < mat2[i].length; j++) {
                mat2[i][j] = random.nextInt(10);
            }
        }

        System.out.println("Matriz 1: " + Arrays.deepToString(mat1));
        System.out.println("Matriz 2: " + Arrays.deepToString(mat2));
        System.out.println("");

        int[][] soma = new int[n][n];

        for (i = 0; i < soma.length; i++) {
            for (j = 0; j < soma[i].length; j++) {
                soma[i][j] = mat1[i][j] + mat2[i][j];
            }
        }

        System.out.println("Soma das Matrizes: " + Arrays.deepToString(soma));
    }
}

