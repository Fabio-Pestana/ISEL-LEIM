import java.util.Scanner;

public class TP2_01_Idade
{
    public static void main(String[] args)
    {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Em que ano você nasceu? ");
        int ano_nasci = scanner.nextInt();
        int idade= 2022 - ano_nasci;
        System.out.println("Nasceu em " + ano_nasci + " e em 2022 tem " + idade +" anos!");
    }
}