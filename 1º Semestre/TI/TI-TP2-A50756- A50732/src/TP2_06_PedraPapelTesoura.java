import java.util.Random;
import java.util.Scanner;

public class TP2_06_PedraPapelTesoura {
    public static void main(String[] args) {

        Random random = new Random();
        int value = random.nextInt(3);
        Scanner scanner = new Scanner(System.in);
        String jogada, t;

        //Atribuição da Jogada do PC
        if (value == 0) {
            jogada = "Papel";
        } else if (value == 1) {
            jogada = "Pedra";
        } else {
            jogada = "Tesoura";
        }

        System.out.println("");
        System.out.println("Jogo do Pedra, Papel ou Tesoura - Escreva a sua jogada (Pedra, Papel ou Tesoura, tal como aqui descrito)!");
        System.out.print("Tentativa do Utilizador: ");
        t = scanner.next();
        System.out.println("");
        //Jogada do PC = Papel
        if (jogada.equals("Papel")  && t.equals("Pedra") ) {
            System.out.println("Perdeu!");
        } else if (jogada.equals("Papel")  && t.equals("Tesoura") ) {
            System.out.println("Ganhou!");
        } else if (jogada.equals("Papel") && t.equals("Papel") ){
            System.out.println("Empate!");
        }

        //Jogada do PC = Pedra
        if (jogada.equals("Pedra") && t.equals("Tesoura")) {
            System.out.println("Perdeu!");
        } else if (jogada.equals("Pedra") && t.equals("Papel")) {
            System.out.println("Ganhou!");
        } else if (jogada.equals("Pedra") && t.equals("Pedra")){
            System.out.println("Empate!");
        }

        //Jogada do PC = Tesoura
        if (jogada.equals("Tesoura") && t.equals("Papel")) {
            System.out.println("Perdeu!");
        } else if (jogada.equals("Tesoura") && t.equals("Pedra")) {
            System.out.println("Ganhou!");
        } else if (jogada.equals("Tesoura") && t.equals("Tesoura")){
            System.out.println("Empate!");
        }

        System.out.println("Jogada do PC: " + jogada);
        System.out.print("");
    }
}