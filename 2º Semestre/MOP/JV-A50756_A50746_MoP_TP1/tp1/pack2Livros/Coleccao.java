package tp1.pack2Livros;

import java.util.Arrays;

/**
 * Classe Colecca, deve conter a descrição de uma colecção, com título, seus
 * livros e editores
 */
public class Coleccao {

    // número máximo de obras de uma colecção
    private static int MAXOBRAS = 20;

    // prefixo usual
    public static final String GENERALPREFIX = "  ";

    // título da colecção
    private String titulo;

    // Array de livros, em que estas encontram-se sempre nos menores índices e
    // pela ordem de registo
    private Livro[] livros = new Livro[MAXOBRAS];

    // deverá conter sempre o número de livros na colecção
    private int numLivros = 0;

    // Editores, tem as mesmas condicionantes que array de autores na classe
    // livro
    private String[] editores;

    /**
     * Construtor; o título tem de ter pelo menos um caracter que não seja um
     * espaço (Character.isWhitespace);
     * o array de editores devem ser pelo menos
     * um e têm as mesmas restrições que os autores dos livros; o array de
     * livros deve conter os mesmos sempre nos menores índices
     */
    public Coleccao(String titulo, String[] editores) {
        // Condições para verificar se os requisitos da class são válidos.
        // titulo
        if (titulo == null || titulo.length() == 0 || titulo.length() == 1 && Character.isWhitespace(titulo.charAt(0)))
            throw new IllegalArgumentException(
                    "O titulo tem de ter pelo menos um caracter");
        this.titulo = titulo;

        //editores
        for (int i=0;i<numLivros;i++)
        {
            if (this.livros[i].validarNomes(editores)==false){
                throw new IllegalArgumentException("O livro tem nomes de editores repetidos");
            }

        }
        this.editores = editores;
    }

    /**
     *
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtem o número total de páginas da colecção
     */
    public int getNumPaginas() { // soma as páginas dos livros pertencentes à coleção
        int cont=0;
        for (int i=0;i<numLivros;i++)
        {
            cont = cont + this.livros[i].getNumPaginas();
        }
        return cont;
    }

    /**
     * Devolve o preço da colecção tendo em conta que as colecções com 4 ou mais
     * livros têm um desconto de 20% nos livros que custam pelo menos 10 euros e
     * que têm mais de 200 páginas
     */
    public float getPreco() { //Calculo do preço tendo em conta os requisitos pedidos
        float pt=0;
        for (int i=0;i<numLivros;i++)
        {
            float p=0;
            if(numLivros>=4 && this.livros[i].getPreco()>=10 && this.livros[i].getNumPaginas()>200)
            {
                pt = (float) (this.livros[i].getPreco() - (this.livros[i].getPreco()*0.2));
            }else {
                pt = p + pt + this.livros[i].getPreco();
            }
        }
        return pt;
    }

    /**
     * Adiciona um livro se puder e este não seja null e a colecção não ficar
     * com livros iguais. Deve utilzar o método getIndexOfLivro.
     */
    public boolean addLivro(Livro livro) {
        if (livro == null) { //verifica se o livro é null
            return false;
        }

        int indice = getIndexOfLivro(livro.getTitulo()); //Verifica se o livro já existe na coleção
        if (indice >= 0) {
            return false;
        }

        if (numLivros>=MAXOBRAS){   // Vê se a coleção está cheia
            return false;
        }

        livros[numLivros] = livro;
        numLivros ++; //adiciona livro à variavel numLivro
        return true;
    }

    /**
     * Devolve o index no array de livros onde estiver o livro com o nome
     * pretendido. Devolve -1 caso não o encontre
     */
    private int getIndexOfLivro(String titulo) {
        for (int i = 0; i < numLivros; i++) { // procura  o título do argumento na lista de livros
            Livro livro = livros[i];
            if (livro != null && livro.getTitulo().equals(titulo)) {
                return i; // retorna o i caso encontre o livro
            }
        }
        return -1; //retorna -1 caso n encontre o livro
    }

    /**
     * Remove do array o livro com o título igual ao título recebido. Devolve o
     * livro removido ou null caso não tenha encontrado o livro. Deve-se
     * utilizar o método getIndexOfLivro. Recorda-se que os livros devem ocupar
     * sempre os menores índices, ou seja, não pode haver nulls entre os livros
     */
    public Livro remLivro(String titulo) {
        int indice = getIndexOfLivro(titulo);

        //Caso o livro n exista no array
        if (indice < 0) {
            return null;
        }

        Livro livroRemovido = livros[indice]; // cria-se uma novo livro

        // Remove o livro
        Livro[] newLivros = new Livro[livros.length - 1];   // cria-se o array com menos um indice que o original
        for (int i = 0; i < indice; i++) {                   // Este for serve para remover os livros à esquerda do que se remove
            newLivros[i] = livros[i];
        }
        for (int i = indice + 1; i < livros.length; i++) {   // Este Este for serve para "puxar" os livros à direita do removido para a esquerda
            newLivros[i - 1] = livros[i];
        }
        livros = newLivros;
        numLivros--;
        return livroRemovido;
    }

    /**
     * Devolve o nº de obras de uma pessoa. A colecção deve contabilizar-se como
     * uma obra para os editores.
     */
    public int getNumObrasFromPerson(String autorEditor) {
        int cont=getLivrosComoAutor(autorEditor).length; //utilizando o metodo getLivrosComoAutor e a .length,
        //obtemos todos os livros de um autor, sendo esse valor armazenado no cont
        for (int j=0; j<this.editores.length; j++) //este ciclo e condiçao servem para procurar o argumento do metodo entre
        { // o array dos editores, sempre que encontre um ele incrementa o contador.
            if (this.editores[j].equals(autorEditor))
            {
                cont++;
            }
        }
        return cont;
    }

    /**
     * Devolver um novo array (sem nulls) com os livros de que a pessoa recebida
     * é autor
     */
    public Livro[] getLivrosComoAutor(String autorNome) {
        Livro[] autor_lista = new Livro[numLivros]; //cria-se uma lista de livros com o comprimento de livros existentes.
        int contador = 0;
        for (int i = 0; i < numLivros; i++) {
            Livro livro = livros[i]; //criação de livro
            String[] autores = livro.getAutores();//array com todos os autores
            for (int j = 0; j < autores.length; j++) {
                if (autores[j].equals(autorNome)) { //verifica se o autor é o mesmo
                    autor_lista[contador] = livro; //adicona o livro ao autor_lista
                    contador++;
                }
            }
        }
        return Arrays.copyOf(autor_lista, contador);//copia o array autor_lista apenas com o comprimento existente no contador.
    }


    /**
     * Deve devolver uma string compatível com os outputs desejados
     */
    public String toString() { //Output como desejado pelo ficheiro .txt enviado
        String p = "Coleção " + getTitulo() + ", editores " +Arrays.toString(this.editores) + ", "+ numLivros + " livros, "+
                getNumPaginas() + "p, " + getPreco() + "€ \n";

        for (int i=0; i<numLivros;i++){
            p = p + "\t" + livros[i].toString() + "\n";
        }
        return p;
    }

    /**
     * Deve devolver um array, sem nulls, com todos os autores e editores
     * existentes na colecção. O resultado não deve conter repetições. Deve
     * utilizar o método mergeWithoutRepetitions
     */
    public String[] getAutoresEditores() {
        String [] aux_string={}; //array vazio
        for (int i=0; i<numLivros-1;i++){
            aux_string = mergeWithoutRepetitions(this.livros[i].getAutores(), this.livros[i+1].getAutores()); // une o array dos autores de todos os livros da coleção
        }
        return mergeWithoutRepetitions(aux_string,this.editores); //une os autores e os editores
    }

    /**
     * Método que recebendo dois arrays sem repetições devolve um novo array com
     * todos os elementos dos arrays recebidos mas sem repetições
     */
    private static String[] mergeWithoutRepetitions(String[] a1, String[] a2) {
        String[] at = new String[a1.length + a2.length]; // cria um array de comprimento igual à soma do comprimento dos dois arrays
        int indice = 0; // variavel indice do array at (array  auxiliar)
        boolean rep; // variavel booleana para ver repetições
        for (int i = 0; i < a1.length; i++) { //percorre o primeiro array completo
            rep = false;
            for (int j = 0; j < indice; j++) {
                if (a1[i].equals(at[j])) { //verifica se já existe
                    rep = true; //se sim passa para o próximo
                    break;
                }
            }

            if (!rep) {
                at[indice] = a1[i]; //se não acresenta ao array auxiliar
                indice++; //incrementa o indice
            }
        }

        //Repetiu-se os passos anterirores para o segundo array

        for (int i = 0; i < a2.length; i++) {
            rep = false;
            for (int j = 0; j < indice; j++) {
                if (a2[i].equals(at[j])) {
                    rep = true;
                    break;
                }
            }
            if (!rep) {
                at[indice] = a2[i];
                indice++;
            }
        }

        //cria-se o arrya final com o comprimento de indice
        String[] af = new String[indice];
        for (int i = 0; i < indice; i++) {
            af[i] = at[i]; //atribui-se os valores de at para af.
        }

        return af;
    }


    /**
     * Devolve true caso a colecção recebida tenha o mesmo título e a mesma
     * lista de editores. Para verificar verificar se os editores são os mesmos
     * devem utilizar o método mergeWithoutRepetitions
     */
    public boolean equals(Coleccao c)
    {
        if (!this.getTitulo().equals(c.getTitulo())) { //verifica-se se os titulos são iguais
            return false;
        }

        String[] mergedEditores = mergeWithoutRepetitions(this.editores, c.editores); //cria-se um array com os editores da coleção

        return Arrays.equals(mergedEditores, this.editores) && Arrays.equals(mergedEditores, c.editores); // verifica-se se os arrays são iguais
    }

    /**
     * Mostra uma colecção segundo os outputs desejados
     */
    public void print(String prefix)
    {
        System.out.println(prefix + toString());
    }

    /**
     * main
     */
    public static void main(String[] args) {
        Livro l1 = new Livro("Viagem aos Himalaias", 340, 12.3f,
                new String[]{"João Mendonça", "Mário Andrade"});
        Livro l2 = new Livro("Viagem aos Pirinéus", 270, 11.5f,
                new String[]{"João Mendonça", "Júlio Pomar"});

        Coleccao c1 = new Coleccao("Primavera",
                new String[]{"João Mendonça","Manuel Alfazema"});

        boolean res;

        res = c1.addLivro(l1);
        res = c1.addLivro(l2);
        System.out.println("c1 -> " + c1);
        c1.print("");
        System.out.println();

        // adicionar um livro com nome de outro já existente
        res = c1.addLivro(l2);
        System.out.println(
                "adição novamente de Viagem aos Pirinéus a c1 -> " + res);
        System.out.println("c1 -> " + c1);
        System.out.println();

        // get editores autores
        String[] ae = c1.getAutoresEditores();
        System.out.println("Autores editores of c1 -> " + Arrays.toString(ae));
        System.out.println();

        // getNumObrasFromPerson
        String nome = "João Mendonça";
        int n = c1.getNumObrasFromPerson(nome);
        System.out.println("Nº de obras de " + nome + " -> " + n);
        System.out.println();

        // getLivrosComoAutor
        nome = "João Mendonça";
        Livro[] obras = c1.getLivrosComoAutor(nome);
        System.out
                .println("Livros de " + nome + " -> " + Arrays.toString(obras));
        System.out.println();

        // rem livro
        String nomeLivro = "Viagem aos Himalaias";
        Livro l = c1.remLivro(nomeLivro);
        System.out.println("Remoção de " + nomeLivro + " -> " + l);
        c1.print("");
        System.out.println();

        // equals
        Coleccao c2 = new Coleccao("Primavera",
                new String[]{"João Mendonça", "Manuel Alfazema"});
        boolean same = c1.equals(c2);
        System.out.println("c2:");
        c2.print("");
        System.out.println("c1.equals(c2) -> " + same);
        System.out.println();

        Coleccao c3 = new Coleccao("Primavera",
                new String[]{"João Mendonça"});
        same = c1.equals(c3);
        System.out.println("c3:");
        c3.print("");
        System.out.println("c1.equals(c3) -> " + same);
    }
}