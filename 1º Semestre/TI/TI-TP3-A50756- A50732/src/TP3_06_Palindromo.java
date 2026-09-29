import java.util.Scanner;

public class TP3_06_Palindromo
{
    public static boolean palindromo(String str)
    {
        boolean validade;
        str = str.toLowerCase();
        char letra_i = str.charAt(0);
        char letra_f = str.charAt(str.length()-1);
        String newstr;
        if (letra_i==letra_f)
        {
            validade = true;
        }else
        {
            validade = false;
        }
        if (str.length()>2)
        {
            newstr= str.substring(1,str.length()-1);
        }else
        {
            return validade;
        }
        validade = validade && palindromo(newstr);
        return validade;
    }
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira uma palavra:");
        String word = scanner.next();
        if (palindromo(word)==true)
        {
            System.out.println(word + " é um palindromo!");
        } else
        {
            System.out.println(word + " não é um palindromo!");
        }
    }
}
