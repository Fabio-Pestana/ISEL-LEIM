import java.util.Scanner;

public class TP3_10_Normalizar
{
    public static String normalizar(String str)
    {
        String aux;
        String final_str="";
        char space = ' ';
        if (str.length()==0)
        {
            return final_str;
        }
        if (str.charAt(0) == space)
        {
            int l = str.charAt(1);
            l = l-32;
            char L = (char) l;
            aux = str.substring(2);
            final_str = final_str + str.charAt(0) + L + normalizar(aux);
            return final_str;
        }else
        {
            aux=str.substring(1);
            final_str = final_str + str.charAt(0) + normalizar(aux);
            return final_str;
        }
    }
    public static String removeEspacosConsecutivos (String str) {
        String str_no_ex_spaces = "";
        String aux;
        char space = ' ';
        if (str.length() == 0) {
            return str_no_ex_spaces;
        }
        if (str.charAt(0) == space && str.charAt(1) == space) {
            aux = str.substring(1);
            str_no_ex_spaces = str_no_ex_spaces + removeEspacosConsecutivos(aux);
        } else {
            aux = str.substring(1);
            str_no_ex_spaces = str_no_ex_spaces + str.charAt(0) + removeEspacosConsecutivos(aux);
        }
        return str_no_ex_spaces;
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print(" Insira uma string para normalizar: ");
        String phrase = scanner.nextLine();
        phrase=removeEspacosConsecutivos(phrase);
        phrase= ' '+ phrase.toLowerCase();
        System.out.println(normalizar(phrase));
    }
}

