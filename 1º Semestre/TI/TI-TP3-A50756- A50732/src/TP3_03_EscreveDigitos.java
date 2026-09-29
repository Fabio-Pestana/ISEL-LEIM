import java.util.Scanner;

public class TP3_03_EscreveDigitos {

    public static String getDigitoEmString(int digito){
        String digito_str = "";
        if (digito == 1){
            digito_str = "um";
        } else if(digito == 2){
            digito_str = "dois";
        } else if(digito == 3){
            digito_str = "três";
        } else if (digito == 4){
            digito_str = "quatro";
        } else if (digito == 5) {
            digito_str = "cinco";
        } else if (digito == 6) {
            digito_str = "seis";
        } else if (digito == 7) {
            digito_str = "sete";
        } else if (digito == 8) {
            digito_str = "oito";
        } else if (digito == 9) {
            digito_str = "nove";
        } else if (digito == 0) {
            digito_str = "zero";
        }
        return digito_str;
    }

    public static  int getDigito(int n, int i){
        int digito = 0;
        for (int j=0; j<i+1; j++) {
            digito=n%10;
            n=n/10;
        }
        return digito;
    }

    public static int getNumDigitos(int n){
        int num_digitos = 0;
        while (n != 0){
            n = n/10;
            num_digitos++;
        }
        return num_digitos;
    }

    public static String mostraDigitos(int n){
        String digito = "";
        for(int i = 0; i<getNumDigitos(n); i++){
            digito = getDigitoEmString(getDigito(n, i)) + "\n" + digito;
        }
        return digito;
    }

    public static void main(String[] args) {
        int numero;
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.print("Introduza um valor para colocar a leitura dos seus dígitos por extenso: ");
        numero = scanner.nextInt();
        System.out.println("");
        System.out.println(mostraDigitos(numero));
    }
}
