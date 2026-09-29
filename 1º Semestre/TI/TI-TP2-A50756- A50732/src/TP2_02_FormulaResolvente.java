import java.util.Scanner;

public class TP2_02_FormulaResolvente {
    public static void main(String[] args) {

        double a, b, c, res1, res2, delta, raiz;
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("Calculadora para a Fórmula Resolvente - Indique valores para a, b e c da seguinte expressão: ax^2+bx+c.");
        System.out.print("a = ");
        a = scanner.nextDouble();
        System.out.print("b = ");
        b = scanner.nextDouble();
        System.out.print("c = ");
        c = scanner.nextDouble();

        delta = ((b*b) - (4*a*c));
        raiz = Math.sqrt(delta);
        res1 = ((-b) + raiz)/(2*a);
        res2 = ((-b) - raiz)/(2*a);

        if (delta < 0){
            System.out.println("Não tem soluções.");
        }
        else {
            System.out.println("Zeros da função que indicou: " + res1 + " e " + res2);
        }
        System.out.print("");
    }
}
