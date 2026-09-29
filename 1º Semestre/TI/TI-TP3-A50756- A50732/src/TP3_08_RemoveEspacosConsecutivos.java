import java.util.Scanner;

public class TP3_08_RemoveEspacosConsecutivos
{
    public static String removeEspacosConsecutivos (String str)
    {
        String str_no_ex_spaces = "";
        String aux;
        char space = ' ';
        if(str.length()==0)
        {
            return str_no_ex_spaces;
        }
        if (str.charAt(0)== space && str.charAt(1)== space)
        {
            aux = str.substring(1);
            str_no_ex_spaces = str_no_ex_spaces + removeEspacosConsecutivos(aux);
        } else {
            aux = str.substring(1);
            str_no_ex_spaces = str_no_ex_spaces + str.charAt(0)+ removeEspacosConsecutivos(aux);
        }
        return str_no_ex_spaces;
    }
    public static void main(String[] args)
    {
        System.out.print("Insira uma frase:");
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        System.out.println("Palavra/frase sem espaços a mais--->" + removeEspacosConsecutivos(word));
    }
}