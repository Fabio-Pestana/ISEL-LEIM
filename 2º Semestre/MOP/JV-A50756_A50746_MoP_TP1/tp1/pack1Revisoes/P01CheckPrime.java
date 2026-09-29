import java.util.Scanner;

public class P01CheckPrime {

    /**
     * Main, método de arranque da execução
     */
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduza um número inteiro (positivo): ");
        int n1 = scanner.nextInt();
        do
        {
            if(n1==0)
            {}
            else if (isPrime(n1))
            {
                System.out.println("O número " + n1 + " é primo, insira um novo número:");
                n1 = scanner.nextInt();
            }else if(n1<0)
            {
                System.out.println("O número " + n1 + " é negativo, insira um novo número:");
                n1 = scanner.nextInt();
            }else {
                System.out.println("O número " + n1 + " não é primo, insira um novo número:");
                n1 = scanner.nextInt();
            }
        }while (n1 !=0);
    }

    public static boolean isPrime(int number) {
        int aux=2;
        if(number==1)//pois o número 1 é primo
        {
            return true;
        }else{
            while(number!=aux)
            {
                if (number%aux==0)//um número primo só é dificil por ele próprio
                {                 //e por 1 logo testamos todos os outros casos
                    return false;
                }
                aux=aux+1;//incrementamos esta variável para testar os valores dentro do intervalo [2 a number-1]
            }
            return true;}
    }
}
