import java.util.Random;
import java.util.Scanner;

public class TP2_05_Dados
{
    public static void main(String[] args)
    {
        Random random = new Random();
        int pc = random.nextInt(6)+1;
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("Adivinhe o valor do dado do pc!");
        System.out.print("Tentativa: ");
        int t = scanner.nextInt();
        System.out.print("");

        if (pc == t){
            System.out.println("Parabéns, acertou!");
        }
        else{
            System.out.println("Errou, tente novamente!");
        }

        System.out.print("Valor do dado do pc: " + pc);
        System.out.println("");
    }
}
