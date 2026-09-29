package tp1.pack2Livros;

import java.util.Arrays;

/**
 * Classe que deverá suportar um livro
 */
public class Livro {

    // Título do livro
    private String titulo;

    // número de páginas
    private int numPaginas;

    // preço do livro
    private float preco;

    // array de autores, este array não deve ter nulls
    private String[] autores;

    /**
     * Deve criar um novo livro com os dados recebidos. O título não deve ser
     * null nem vazio.
     * O número de páginas não pode ser menor que 1.
     * O preço não pode ser negativo. ||
     * O array de autores não deve conter nem nulls e
     * deve conter pelo menos um autor válido.
     * Não pode haver repetições dos nomes
     * dos autores, considera-se os nomes sem os espaços extra (ver
     * removeExtraSpaces). Este método deve utilizar os métodos auxiliares
     * existentes. Em caso de nome inválido deve lançar uma excepção de
     * IllegalArgumentException com a indicação do erro ocorrido
     */
    public Livro(String titulo, int numPaginas, float preco, String[] autores) {

        // Condições para verificar se os requisitos da class são válidos.
        // para titulo
        if (titulo == null || titulo.length() == 0)
            throw new IllegalArgumentException("O titulo tem de ter pelo menos um caracter");
        this.titulo = titulo;
        //para nº pag
        if(numPaginas<1)
            throw new IllegalArgumentException("O livro tem de ter pelo menos uma página");
        this.numPaginas = numPaginas;
        //para preço
        if (preco<0)
            throw new IllegalArgumentException("O preço do livro não pode negativo");
        this.preco = preco;
        //para ver se há nomes repetidos
        if(!validarNomes(autores))
            throw new IllegalArgumentException("O livro tem nomes de autores repetidos");
        this.autores = autores;

    }

    /**
     * Devolve o título do livro
     */
    public String getTitulo() {
        return this.titulo;
    }

    /**
     * Devolve o número de páginas do livro
     */
    public int getNumPaginas() {
        return this.numPaginas;
    }

    /**
     * Devolve o preço do livro
     */
    public float getPreco() {
        return this.preco;
    }

    /**
     * Devolve uma cópia do array de autores do livro
     */
    public String[] getAutores() {
        return this.autores.clone();
    }

    /**
     * Deve devolver true se o array conter apenas nomes válidos. Um nome é
     * válido se conter pelo menos uma letra (Character.isLetter) e só conter
     * letras e espaços (Character.isWhitespace). Deve chamar validarNome.
     */
    public static boolean validarNomes(String[] nomes) {
        boolean aux = false; // criou-se uma variavel booleana auxiliar para verificar as condições
        if (haRepeticoes(nomes)) return false; // Se existir repetições é false
        else {
            for (int i=0;i< nomes.length;i++)
            {
                nomes[i] = removeExtraSpaces(nomes[i]); //remove os espaços a mais caso existam
                if (validarNome(nomes[i])) // valida o nome e aux passa a true
                    aux = true;
                else return false;
            }
            return aux;
        }
    }

    /**
     * Um nome válido se não for null e não conter pelo menos uma letra
     * (Character.isLetter) e só conter letras e espaços
     * (Character.isWhitespace)
     */
    public static boolean validarNome(String nome) {
        boolean check = false; // variavel auxiliar
        if(nome==null)
            return false; //nome não aceite
        else {
            for(int i=0; i< nome.length(); i++)
            {
                if(Character.isWhitespace(nome.charAt(i)) || Character.isLetter(nome.charAt(i))) //só aceita chars que sejam espaços ou letras
                    check = true;
                else return false; //existem caracteres n aceites na String
            }
            return check;
        }
    }


    /**
     * Recebe um nome já previamente validado, ou seja só com letras ou espaços.
     * Deve devolver o mesmo nome mas sem espaços (utilizar trim e
     * Character.isWhitespace) no início nem no fim e só com um espaço ' ' entre
     * cada nome. Deve utilizar um StringBuilder para ir contendo o nome já
     * corrigido
     */

    public static String removeExtraSpaces(String nome) {
        //Remove os espaços no início e no final da string do nome com trim()
        nome = nome.trim();

        //Remove espaços a mais pelo meio
        StringBuilder novonome = new StringBuilder(); //Criação do objeto StringBuilder e o seu construtor
        boolean space = false; // indica se já encontrou um espaço em branco
        for (int i = 0; i < nome.length(); i++) {
            if (!Character.isWhitespace(nome.charAt(i))) { //Verifica se o char é ou não um espaço
                novonome.append(nome.charAt(i));            // Se não for acresenta-se esse char ao novonome
                space = false;                              // volta-se a assumir a variável space como false para reiniciar a procura
            } else if (!space) { // Caso exista um espaço adiciona apenas um espaço em branco ao novonome
                novonome.append(' ');
                space = true;   // Assume-se a variável space como true para reiniciar a procura
            }
        }
        return novonome.toString();
    }

    /**
     * Método que verifica se há elementos repetidos. O array recebido não
     * contém nulls.
     */
    public static boolean haRepeticoes(String[] elems) {
        //Se só houver um elemento, é impossivel existir repetições, logo é false
        if (elems.length <= 1) {
            return false;
        }

        for (int i = 0; i < elems.length-1; i++) //verifica-se o array inteiro e compara-se os indices
        {
            for (int j = i+1; j < elems.length ; j++)
            {
                if (elems[i].equals(elems[j]))
                    return true;
            }
        }
        return false;
    }

    /**
     * Devolve true se o autor recebido existe como autor do livro. O nome
     * recebido não contém espaços extra.
     */
    public boolean contemAutor(String autorNome) {
        String[] N = getAutores();
        for (int i=0;i<N.length;i++)
        {
            if (N[i].equals(autorNome)) //verifica-se se o argumento existe na lista de autores
                return true;
        }
        return false;
    }

    /**
     * Devolve uma string com a informação do livro (ver outputs desejados)
     */
    public String toString() { //Output como desejado pelo ficheiro .txt enviado
        return getTitulo() + ", " + getNumPaginas() + "p " +
                getPreco() + "€ " + Arrays.toString(getAutores());
    }

    /**
     * Deve mostrar na consola a informação do livro precedida do prefixo
     */
    public void print(String prefix) {
        System.out.println(prefix + toString());
    } //anexação do prefixo

    /**
     * O Livro recebido é igual se tiver o mesmo título que o título do livro
     * corrente
     */
    public boolean equals(Livro l) { //compara os títulos
        if (this.getTitulo() == l.getTitulo()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * main
     */
    public static void main(String[] args) {

        // constructor e toString
        Livro l = new Livro("Viagem aos Himalaias", 340, 12.3f, new String[]{"João Mendonça", "Mário Andrade"});
        System.out.println("Livro -> " + l);
        l.print("");
        l.print("-> ");
        System.out.println();

        // contém autor
        String autorNome = "Mário Andrade";
        System.out.println("Livro com o autor " + autorNome + "? -> " + l.contemAutor(autorNome));
        autorNome = "Mário Zambujal";
        System.out.println("Livro com o autor " + autorNome + "? -> " + l.contemAutor(autorNome));
        System.out.println();

        // equals
        System.out.println("Livro: " + l);
        System.out.println("equals Livro: " + l);
        System.out.println(" -> " + l.equals(l));

        Livro l2 = new Livro("Viagem aos Himalaias", 100, 10.3f, new String[]{"Vitor Záspara"});
        System.out.println("Livro: " + l);
        System.out.println("equals Livro: " + l2);
        System.out.println(" -> " + l.equals(l2));
        System.out.println();

        // testes que dão excepção - mostra-se a excepção

        // livro lx1
        System.out.println("Livro lx1: ");
        try {
            Livro lx1 = new Livro("Viagem aos Himalaias", -1, 12.3f, new String[]{"João Mendonça", "Mário Andrade"});
            System.out.println("Livro lx1: " + lx1);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        System.out.println();

        // livro lx2
        System.out.println("Livro lx2: ");
        try {
            Livro lx2 = new Livro("Viagem aos Himalaias", 200, -12.3f, new String[]{"João Mendonça", "Mário Andrade"});
            System.out.println("Livro lx2: " + lx2);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        System.out.println();

        // livro lx3
        System.out.println("Livro lx3: ");
        try {
            Livro lx3 = new Livro(null, 200, -12.3f, new String[]{"João Mendonça", "Mário Andrade"});
            System.out.println("Livro lx3: " + lx3);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        System.out.println();

        // livro lx4
        System.out.println("Livro lx4: ");
        try {
            Livro lx4 = new Livro("Viagem aos Himalaias", 200, 12.3f, new String[]{"João Mendonça", "Mário Andrade", "João Mendonça"});
            System.out.println("Livro lx4: " + lx4);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }
}