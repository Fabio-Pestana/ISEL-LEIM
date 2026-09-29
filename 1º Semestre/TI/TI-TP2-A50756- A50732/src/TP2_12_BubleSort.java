import java.util.Random;
import java.util.Scanner;

public class TP2_12_BubleSort
{
    public static void main(String[] args)
    {
        System.out.print("Insira um numero para o comprimento da lista: ");
        Scanner scanner = new Scanner(System.in);
        int x= scanner.nextInt();
        int [] lista = new int[x];
        int cres=0;
        boolean executar= true;
        System.out.println(" ");
        System.out.print("A lista é a seguinte: ");
        for (int i=0; i<x; i++)
        {
            Random random = new Random();
            int rand = random.nextInt(101);//0 tambem conta
            lista[i] = rand;
            System.out.print(lista[i] + " ");
        }
        while (executar==true)
        {
            executar=false;
            for (int i=0; i<(x-1); i++)
            {

                if (lista[i]>lista[i+1])
                {
                    cres= lista[i+1];
                    lista[i+1]=lista[i];
                    lista[i]=cres;
                    executar=true;
                }
            }
        }
        System.out.println(" ");
        System.out.print("Lista ordenada: ");
        for (int i=0; i<x; i++)
        {
            System.out.print(lista[i]+ " ");
        }
    }
}
