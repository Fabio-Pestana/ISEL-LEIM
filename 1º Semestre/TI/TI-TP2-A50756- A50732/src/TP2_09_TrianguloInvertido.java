import java.util.Scanner;

public class TP2_09_TrianguloInvertido
{
    public static void main(String[] args)
    {
        Scanner scanner= new Scanner(System.in);
        System.out.println("Insira o número de linhas: ");
        int linhas= scanner.nextInt();
        int i=0, cardcalc=0;
        String c= "#";
        String e= " ";
        for(i=linhas;i>0;i--)
        {
            cardcalc=1+(i-1)*2; // calculo dos #--> feito através da formula da progressão aritmética
            int esp=linhas-i;
            System.out.println(e.repeat(esp)+c.repeat(cardcalc));
            //repeat é uma função que recebe uma string e a repete um n número de vezes
        }
    }
}
