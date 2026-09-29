import java.util.Scanner;

public class TP2_07_ListaNumeros
{
    public static void main(String[] args)
    {
        int i=0;
        Scanner scanner= new Scanner(System.in);
        System.out.println("Insira dois números (primeiro o menor): ");
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        System.out.print("Insira um dos seguintes comandos: par/impar/todos ");
        String decisao = scanner.next();
        {
            if(decisao.equals("par"))
            {
                for(i=a;i<=b;i++)
                {
                    if((i%2)==0)
                    {
                        System.out.print(i + " ");
                    }
                }
            }else if(decisao.equals("impar"))
            {
                for(i=a;i<=b;i++)
                {
                    if((i%2)!=0)
                    {
                        System.out.print(i + " ");
                    }
                }
            } else if (decisao.equals("todos"))
            {
                for (i=a;i<=b;i++)
                {
                    System.out.print(i + " ");
                }
            }
            else
            {
                System.out.println("Erro x_x");
            }
        }
    }
}
