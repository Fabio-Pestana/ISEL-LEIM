import java.util.Scanner;

public class TP2_14_SomaElementos
{
    public static void main(String[] args)
    {
        int [] lista = new int [] {1, 1, 2, 3, 4, 5, 6, 6, 8, 10 };
        System.out.print(" Insira um número para adquirir uma lista de valores que fazem a sua soma: ");
        Scanner scanner= new Scanner(System.in);
        int valor = scanner.nextInt();
        int l=lista.length;
        System.out.print(" A lista é: { ");
        for (int i=0;i<l;i++)
        {
            for (int j=1; j<l;j++)
            {
                if(lista[i]+lista[j]==valor && i<j)
                {
                    System.out.print("[ " + lista[i] + " + "+ lista[j] + " ] ");
                }
            }
        }
        System.out.print("}");
    }
}
