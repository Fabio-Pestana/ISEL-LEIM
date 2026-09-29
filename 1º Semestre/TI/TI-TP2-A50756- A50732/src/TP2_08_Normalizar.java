import java.util.Scanner;

public class TP2_08_Normalizar {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("Programa que normaliza um nome completo - Insira um nome completo para o normalizar.");
        System.out.print("Nome do Indivíduo: ");
        String nome_i = scanner.nextLine();
        String nome_f = "";
        int comp = nome_i.length(), l1, i=1;
        l1 = nome_i.charAt(0);

        //Primeira letra do Nome:
        if ((l1 >= 97) && (l1 <= 122 )){
            l1 = l1 - 32;
            nome_f = nome_f + (char) l1;
        }
        else if ((l1 > 64) && (l1 < 91 )){
            l1 = l1;
            nome_f = nome_f + (char) l1;
        }

        //Resto das Palavras:
        while(i<comp){
            int an = nome_i.charAt(i);
            if(an > 96 && an < 123){
                nome_f = nome_f + (char) an;
                i = i+1;
            } else if (an > 64 && an < 91) {
                an = an + 32;
                nome_f = nome_f + (char) an;
                i = i+1;
            }else if (an == 32){
                nome_f = nome_f + (char) an;
                int ls = nome_i.charAt(i+1);
                if((nome_i.charAt(i+1) == 'd' || nome_i.charAt(i+1) == 'D' ) && (nome_i.charAt(i+2) == 'o' || nome_i.charAt(i+2) == 'O') && (nome_i.charAt(i+3) == 's' || nome_i.charAt(i+3) == 'S'))
                {
                    nome_f = nome_f + 'd' + 'o' + 's';
                    i=i+4;
                } else if ((nome_i.charAt(i+1) == 'd' || nome_i.charAt(i+1) == 'D' ) && (nome_i.charAt(i+2) == 'a' || nome_i.charAt(i+2) == 'A') && (nome_i.charAt(i+3) == 's' || nome_i.charAt(i+3) == 'S'))
                {
                    nome_f = nome_f + 'd' + 'a' + 's';
                    i=i+4;
                } else if ((nome_i.charAt(i+1) == 'd' || nome_i.charAt(i+1) == 'D' ) && (nome_i.charAt(i+2) == 'o' || nome_i.charAt(i+2) == 'O'))
                {
                    nome_f = nome_f + 'd' + 'o';
                    i = i + 3;
                } else if ((nome_i.charAt(i+1) == 'd' || nome_i.charAt(i+1) == 'D' ) && (nome_i.charAt(i+2) == 'A' || nome_i.charAt(i+2) == 'a'))
                {
                    nome_f = nome_f + 'd' + 'a';
                    i = i + 3;
                } else if ((nome_i.charAt(i+1) == 'd' || nome_i.charAt(i+1) == 'D' ) && (nome_i.charAt(i+2) == 'e' || nome_i.charAt(i+2) == 'E'))
                {
                    nome_f = nome_f + 'd' + 'e';
                    i = i + 3;
                } else if ((nome_i.charAt(i+1) == 'e' || nome_i.charAt(i+1) == 'E' ))
                {
                    nome_f = nome_f + 'e';
                    i = i + 2;
                } else if (ls > 64 && ls < 91) { //Letras Maiúsculas do Meio
                    nome_f = nome_f + (char) ls;
                    i = i + 2;
                } else
                {
                    ls = ls - 32;
                    nome_f = nome_f + (char) ls;
                    i = i + 2;
                }
            }
        }
        System.out.println("Nome final: " + nome_f);
    }
}
