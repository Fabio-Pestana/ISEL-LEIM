import java.util.Scanner;

public class TP3_01_SomaDigitos {
    public static int somaDigitos (int n){
        int soma = 0, comp = 0, i, initial_value;
        initial_value = n;
        while (n != 0){         //cálculo realizado para o comprimento do número inserido;
            n = n/10;
            comp = comp + 1;
        }

        for (i = 0; i<comp; i++){
            soma = soma + initial_value%10;       //cálculo para a soma dos dígitos do número inserido;
            initial_value = initial_value/10;
        }
        return soma;
    }

    public static void main(String[] args) {
        int num, soma_d = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.print("Introduza um valor para calcular a soma dos seus dígitos: ");
        num = scanner.nextInt();
        soma_d = somaDigitos(num);
        System.out.println("Soma dos dígitos de " + num + ": " + soma_d);
    }
}
