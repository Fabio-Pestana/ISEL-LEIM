import java.util.Arrays;
import java.util.Scanner;

public class TP3_07_MaioresQue {

    public static int maioresQue(int[] array, int val){
        int contador;
        if(array.length == 0){
            return 0;
        } else {
            if (array[0]>val){
                contador = 1;
                array = Arrays.copyOfRange(array, 1, array.length);
                return contador + maioresQue(array, val);
            } else {
                contador = 0;
                array = Arrays.copyOfRange(array, 1 , array.length);
                return contador + maioresQue(array, val);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int valor;
        int[] vetor = new int[]{1,2,3,4,5,6,7,8,9,10};
        System.out.println("");
        System.out.print("Introduza um valor: ");
        valor = scanner.nextInt();
        System.out.println("Para o vetor {1,2,3,4,5,6,7,8,9,10} existem " + maioresQue(vetor, valor) + " valores maiores do que " + valor + ".");
    }
}
