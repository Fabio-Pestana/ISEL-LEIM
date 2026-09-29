import java.util.Scanner;

public class TP2_10_Divisao
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduza dois números positivos para fazer uma divisao ");
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        while (x<=0 || y<=0)
        {
            System.out.println("insira dois números POSITIVOS: ");
            x = scanner.nextInt();
            y = scanner.nextInt();
        }
        int resto=x;
        int divisao=0;
        while ((resto-y)>=0)
        {
            resto = resto - y;
            divisao=divisao+1;
        }
        System.out.println("A divisão é: " + divisao);
        System.out.println("O resto da divisão é: " + resto);
    }
}

