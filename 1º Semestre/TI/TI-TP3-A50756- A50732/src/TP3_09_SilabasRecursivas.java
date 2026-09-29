import java.util.Scanner;

public class TP3_09_SilabasRecursivas {
    public static boolean isVogal(char ch){
        if(ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u'){
            return true;
        } else {
            return false;
        }
    }
    public static int silabas(String str)
    {
        int contador;
        if (str.length() == 0)
        {
            contador = 0;
            return contador;
        } else if (str.length() == 1 && (isVogal(str.charAt(0)))) // ==true
        {
            contador = 1;
            return contador;
        } else if ((isVogal(str.charAt(0))) && (isVogal(str.charAt(1)))) // ==true
        {
            contador = 1;
            return contador + silabas(str.substring(2));
        } else if ((isVogal(str.charAt(0)))) // ==true
        {
            contador = 1;
            return contador + silabas(str.substring(1));
        }else
        {
            return silabas(str.substring(1));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String palavra, palavra_aux;
        int num_silabas;
        System.out.println("");
        System.out.print("Introduza uma palavra para calcular o número de sílabas: ");
        palavra = scanner.next();
        palavra_aux = palavra.toLowerCase();
        num_silabas = silabas(palavra_aux);
        System.out.println("A palavra " + palavra + " tem " + num_silabas + " sílabas.");
    }
}


