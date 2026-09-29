import java.util.Scanner;
public class TP3_05_Silabas {

    public static boolean isVogal (char ch){
        if(ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u'){
            return true;
        } else{
            return false;
        }
    }

    public static int countVogais (String str){
        int num_vog = 0;
        for (int i = 0; i<str.length()-1; i++){
            if(isVogal(str.charAt(i)) == true){
                num_vog++;
            }
        }
        return num_vog;
    }

    public static int countParesVogais (String str){
        int num_par = 0;
        for (int i = 0; i<str.length()-1; i++){
            if((isVogal(str.charAt(i)) == true) && (isVogal(str.charAt(i+1)) == true)){
                num_par++;
            }
        }
        return num_par;
    }

    public static int silabas(String str){
        int num_silabas;
        if(countParesVogais(str)==0)
        {
            num_silabas = countVogais(str);
            return num_silabas;
        } else
        {
            num_silabas = countVogais(str) - countParesVogais(str);
            return num_silabas + 1;
        }

    }

    public static void main(String[] args) {
        String palavra;
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.print("Introduza a palavra para calcular o seu número de sílabas: ");
        palavra = scanner.next();
        System.out.println("A palavra " + palavra + " tem " + silabas(palavra) + " sílabas.");
    }
}
