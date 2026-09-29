import java.util.Scanner;

public class TP2_03_Votar
{
    public static void main(String[] args)
    {
        Scanner scanner= new Scanner(System.in);
        System.out.print("Introduza o seu nome: ");
        String nome = scanner.next();
        System.out.print("Insira o seu ano de nascimento: ");
        int ano_nasci= scanner.nextInt();
        int idade= 2022-ano_nasci;
        if(idade>=18)
        {
            System.out.println(" O/A " + nome + " pode votar! ");
        }else
        {
            System.out.println(" O/A " + nome + " não pode votar! ");
        }
    }
}
