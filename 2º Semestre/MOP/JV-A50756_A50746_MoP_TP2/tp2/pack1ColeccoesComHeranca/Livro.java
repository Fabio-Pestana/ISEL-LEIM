package tp2.pack1ColeccoesComHeranca;

import java.util.Arrays;

class Livro extends Obra {

    /**
     * Construtor da classe Livro
     * @param titulo Título do livro
     * @param numPaginas Número de páginas do livro
     * @param preco Preço do livro
     * @param autores Array com os nomes dos autores do livro
     */
    public Livro(String titulo, int numPaginas, float preco, String[] autores) {
        super(titulo);

        //para o titulo
        if (titulo == null || titulo.length() == 0)
            throw new IllegalArgumentException("O titulo tem de ter pelo menos um caracter");
        //para nº pag
        if(numPaginas<1)
            throw new IllegalArgumentException("O livro tem de ter pelo menos uma página");
        this.numPaginas = numPaginas;
        //para preço
        if (preco<0)
            throw new IllegalArgumentException("O preço do livro não pode negativo");
        this.preco = preco;
        //para ver se os nomes dos autores são válidos
        if(!validarNomes(autores))
            throw new IllegalArgumentException("O livro tem nomes de autores inválidos");
        this.autores = autores;

    }

    /**
     * O número de páginas do livro.
     */
    private int numPaginas;

    /**
     * O preço do livro.
     */
    private float preco;

    /**
     * Os nomes dos autores do livro.
     */
    private String [] autores;

    /**
     * Retorna o número de páginas do livro.
     * @return o número de páginas do livro
     */
    @Override
    public int getNumPaginas() {
        return numPaginas;
    }

    /**
     * Retorna o preço do livro.
     * @return o preço do livro
     */
    @Override
    public float getPreco() {
        return this.preco;
    }

    /**
     * Retorna uma string com informações sobre o livro.
     * @return  uma string com o título, número de páginas, preço e autores do livro
     */
    public String toString() {
        return getTitulo() + ", " + getNumPaginas() + "p, " +
                getPreco() + "€ " + Arrays.toString(autores);
    }

    /**
     * Retorna uma cópia do array com os nomes dos autores do livro.
     * @return um array com os nomes dos autores do livro
     */
    public String[] getAutores() {
        return this.autores.clone();
    }

    /**
     * Verifica se o livro foi escrito por um determinado autor.
     * @param autorNome o nome do autor
     * @return true se o livro foi escrito pelo autor, false caso contrário
     */
    public boolean contemAutor(String autorNome) {
        String[] N = getAutores();
        for (int i=0;i<N.length;i++)
        {
            if (N[i].equals(autorNome))
                return true;
        }
        return false;
    }

    /**
     * Verifica se o livro foi escrito por um determinado autor.
     * @param l o livro a ser comparado
     * @return true se os livros forem iguais, false caso contrário
     */
    public boolean equals(Livro l) {
        if (this.getTitulo().equals(l.getTitulo())) {
            return true;
        }
        return false;
    }

    /**
     * main
     */
    public static void main(String[] args)
    {
        Livro l1 = new Livro("O Hobbit",310, 11.99f, new String[]{"JRR Tolkien"});
        System.out.println("Livro -> " + l1);
        l1.print("");
        l1.print("-> ");
        System.out.println();

        Livro l2 = new Livro("Frankenstein",280, 7.99f, new String[]{"Mary Shelley"});
        System.out.println("Livro -> " + l2);
        l2.print("");
        l2.print("-> ");
        System.out.println();

        Livro l3 = new Livro("Os Maias",925, 9.99f, new String[]{"Eça de Queiroz"});
        System.out.println("Livro -> " + l3);
        l3.print("");
        l3.print("-> ");
        System.out.println();

        Livro l4 = new Livro("Eu, Robô",253, 13.99f, new String[]{"Isaac Asimov"});
        System.out.println("Livro -> " + l4);
        l4.print("");
        l4.print("-> ");
        System.out.println();

        Livro l5 = new Livro("À boleia pela galáxia",450, 15.59f, new String[]{"Douglas Adams"});
        System.out.println("Livro -> " + l5);
        l5.print("");
        l5.print("-> ");
        System.out.println();

        Livro l6 = new Livro("Os Três Mosqueteiros",683, 19.59f, new String[]{"Alexandre Dumas"});
        System.out.println("Livro -> " + l6);
        l6.print("");
        l6.print("-> ");
        System.out.println();

        Livro l7 = new Livro("Harry Potter e a Pedra Filosfal",380, 13.30f, new String[]{"JK Rowling"});
        System.out.println("Livro -> " + l7);
        l7.print("");
        l7.print("-> ");
        System.out.println();

        Livro l8 = new Livro("O Conde de Monte Cristo - Parte 1",650, 19.59f, new String[]{"Alexandre Dumas"});
        System.out.println("Livro -> " + l8);
        l8.print("");
        l8.print("-> ");
        System.out.println();

        Livro l9 = new Livro("O Conde de Monte Cristo - Parte 2",730, 19.59f, new String[]{"Alexandre Dumas"});
        System.out.println("Livro -> " + l9);
        l9.print("");
        l9.print("-> ");
        System.out.println();;

        Livro l10 = new Livro("O Silmarillion",310, 11.99f, new String[]{"JRR Tolkien", "Christopher Tolkien"});
        System.out.println("Livro -> " + l10);
        l10.print("");
        l10.print("-> ");
        System.out.println();

        // contém autor
        String autorNome = "JRR Tolkien";
        System.out.println("Livro com o autor " + autorNome + "? -> " + l1.contemAutor(autorNome));
        autorNome = "Sophia de Mello Breyner Andersen";
        System.out.println("Livro com o autor " + autorNome + "? -> " + l1.contemAutor(autorNome));
        autorNome = "Christopher Tolkien";
        System.out.println("Livro com o autor " + autorNome + "? -> " + l10.contemAutor(autorNome));
        System.out.println();

        // equals
        System.out.println("Livro: " + l1);
        System.out.println("equals Livro: " + l1);
        System.out.println(" -> " + l1.equals(l1));

        System.out.println("Livro: " + l9);
        System.out.println("equals Livro: " + l10);
        System.out.println(" -> " + l9.equals(l10));

        //comentado estao os vários testes dos erros segundo as condições impostas no Construtor
/*
        Livro l11 = new Livro ("O ano da morte de Ricardo Reis", -493, 15, new String[] {"José Saramago"});
        System.out.println("Livro -> " + l11);
        l11.print("");
        l11.print("-> ");
        System.out.println();

        Livro l11 = new Livro ("", 493, 15, new String[] {"José Saramago"});
        System.out.println("Livro -> " + l11);
        l11.print("");
        l11.print("-> ");
        System.out.println();

        Livro l11 = new Livro ("O ano da morte de Ricardo Reis", 493, -19.99f, new String[] {"José Saramago"});
        System.out.println("Livro -> " + l11);
        l11.print("");
        l11.print("-> ");
        System.out.println();

        Livro l11 = new Livro ("O ano da morte de Ricardo Reis", 493, 19.99f, new String[] {"José Saramago 1998"});
        System.out.println("Livro -> " + l11);
        l11.print("");
        l11.print("-> ");
        System.out.println();
        */
    }

}
