import java.util.Random;
import java.util.Scanner;

public class TP2_11_RandomArray
{
    public static void main(String[] args)
    {
        System.out.print("Insira um numero para o comprimento da lista: ");
        Scanner scanner = new Scanner(System.in);
        int x= scanner.nextInt();
        int [] lista = new int[x];
        int soma=0;
        System.out.println(" ");
        System.out.print("A lista é a seguinte: ");
        for (int i=0; i<x; i++)
        {
            Random random = new Random();
            int rand= random.nextInt(101);//0 tambem conta
            lista[i]=rand;
            System.out.print(lista[i]+ " ");
            soma = soma + rand;
        }
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("A soma é "+ soma + " ");
    }
}
