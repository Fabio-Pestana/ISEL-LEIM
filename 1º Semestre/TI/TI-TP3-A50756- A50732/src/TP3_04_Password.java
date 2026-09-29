import java.util.Scanner;

public class TP3_04_Password
{
    public static boolean isLetter (char ch)
    {
        if((ch>=65 && ch<=90) || (ch>=97 && ch<=122))
        {
            return true;
        }else
        {
            return false;
        }
    }
    public static boolean isDigit (char ch)
    {
        if(ch>=48 && ch<=57)
        {
            return true;
        } else
        {
            return false;
        }
    }
    public static boolean isValid (String password) //2 numeros 3 letras ---> 10 no total (min)
    {
        int cont_letras = 0;
        int cont_digitos = 0;
        for (int i=0; i<password.length(); i++)
        {
            if (isLetter(password.charAt(i))==true)
            {
                cont_letras = cont_letras + 1;
            }
            if (isDigit(password.charAt(i))==true)
            {
                cont_digitos = cont_digitos + 1;
            }
        }
        if (cont_letras >=3 && cont_digitos >=2 && password.length()>=10)
        {
            return true;
        } else
        {
            return false;
        }
    }

    public static void main(String[] args)
    {
        Scanner scanner= new Scanner(System.in);
        System.out.print("Insira uma password:");
        String password= scanner.next();
        if (isValid(password)==true)
        {
            System.out.println("Password válida");
        }else
        {
            System.out.println("Password inválida");
        }
    }
}
