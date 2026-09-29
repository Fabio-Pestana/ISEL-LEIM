package tp1.pack1Revisoes;

public class P03WorkWithStrings {

    /**
     * Main, método de arranque da execução
     */
    public static void main(String[] args) {
        test_compareStrings(null, null); // result = 0
        test_compareStrings(null, ""); // result = -1
        test_compareStrings("", null); // result = 1
        test_compareStrings("a", ""); // result = 1
        test_compareStrings("", "a"); // result = -1
        test_compareStrings("a", "a"); // result = 0
        test_compareStrings("b", "a"); // result = 1
        test_compareStrings("a", "b"); // result = -1
        test_compareStrings("aa", "a"); // result = 2
        test_compareStrings("a", "aa"); // result = -2
        test_compareStrings("aa", "aa"); // result = 0
        test_compareStrings("ab", "aa"); // result = 2
        test_compareStrings("ab", "ab"); // result = 0
        test_compareStrings("abc", "abc"); // result = 0
        test_compareStrings("abc", "abd"); // result = -3
    }

    /**
     * Este método recebe duas Strings s1 e s2 e procede à sua comparação,
     * devolvendo um valor positivo se s1 for maior que s2, negativo se ao
     * contrário e 0 se iguais. A comparação deve ser feita primeiro em termos
     * lexicográficos caracter a caracter começando pelos caracteres de menor
     * peso ou em segundo lugar em termos de número de caracteres. Se diferentes
     * deve devolver o índice +1/-1 do caractere que faz a diferença. Ex.
     * s1="Bom", s2="Dia", deve devolver -1; s1="Boa", s2="Bom", deve devolver
     * -3; s1="Bom", s2="Bo", deve devolver 3. Uma String a null é considerada
     * menor que uma string não null.
     *
     * @param s1 string a comparar
     * @param s2 string a comparar
     * @return o resultado da comparação
     */
    private static int compareStrings(String s1, String s2) {
        if (s1 == null && s2 == null) { //casos das strings vazias
            return 0;
        } else if (s1 == null) {
            return -1;
        } else if (s2 == null) {
            return 1;
        }
        int len_s1 = s1.length(),len_s2 = s2.length(), lenMin=Math.min(len_s1,len_s2), contador = 0;
        for (int i = 0; i < lenMin; i++) { //lenMin é o comprimento da string mais pequena.
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if (c1>c2){ //compara-se os char´s apartir da tabela ASCII; sendo que [a,z]=[97,122] e [A,Z]=[65,90]
                contador = i + 1;
            }else if (c1==c2) //se o char for igual entre strings contador = 0
            {
                contador = 0;
            } else //se c2 é maior, o contador é negativo
            {
                contador = -(i + 1);

            }
        }
        if (len_s1 != len_s2) { //comprimento das strings for diferente, contador é igual a maior string
            contador =  Math.max(len_s1,len_s2);
        }
        if(len_s1<len_s2) //s2 é maior logo contador é negativo
        {
            return -(contador);
        }
        else {//s1 é maior logo contador é positivo
            return contador;
        }
    }

    /**
     * Auxiliary method that call compareStrings with two strings
     */
    private static void test_compareStrings(String s1, String s2) {
        try {
            System.out.print("compareStrings (" + s1 + ", " + s2 + ") = ");
            int res = compareStrings(s1, s2);
            System.out.println(res);

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}