package tp2.pack1ColeccoesComHeranca;

import java.util.Arrays;

class Coleccao extends Obra{
    // número máximo de obras de uma colecção
    private static int MAXOBRAS = 20;

    // prefixo
    public static final String GENERALPREFIX = "  ";

    private Livro[] livros = new Livro[MAXOBRAS];
    private Obra[] obras = new Obra[MAXOBRAS];

    // deverá conter o número de livros na colecção
    private int numObras = 0;

    // array de colecções, estas devem ocupar sempre os menores índices
    private Coleccao[] coleccoes = new Coleccao[MAXOBRAS];

    // Editores, tem as mesmas condicionantes que array de autores na classe
    private String[] editores;


    /**
     * Construtor da classe Coleccão
     * @param titulo Título do coleção
     * @param editores Array com os editores
     */
    public Coleccao(String titulo, String[] editores)  {
        super(titulo);
        // titulo
        if (titulo == null || titulo.length() == 0 || titulo.length() == 1 && Character.isWhitespace(titulo.charAt(0)))
            throw new IllegalArgumentException(
                    "O titulo tem de ter pelo menos um caracter");

        //editores
        for (int i=0;i<getNumLivros();i++)
        {
            if (!this.livros[i].validarNomes(editores)){
                throw new IllegalArgumentException("O livro tem nomes de editores inválidos");
            }
        }
        //editores
        if(editores.length < 1)
            throw new IllegalArgumentException("O array de editores tem de ter pelo menos um editor");
        this.editores = editores;
    }


    /**
     * Retorna o número de páginas do livro.
     * @return o número de páginas do livro
     */
    public int getNumPaginas() {
        // contamos todas as páginas de todos os livros e de todas as coleções.
        int cont=0;
        for (int i=0;i<numObras;i++){
            if (this.obras[i]!=null)
                cont=cont + this.obras[i].getNumPaginas();
        }
        return cont;
    }

    /**
     * As colecções com mais de 5000 páginas nos seus livros directos têm um
     * desconto de 20% nesses livros. As colecções em que o somatório de páginas
     * das suas subcolecções directas seja igual ou superior ao quádruplo do nº
     * de páginas da sua subcolecção directa com mais páginas deverão aplicar um
     * desconto de 10% sobre os preços das suas subcolecções
     */
    public float getPreco() {
        //consoantes as especificações dadas, calculou-se os preços desejados.
        float pt=0;
        int max=0;
        for (int i=0;i<numObras;i++)
        {
            float p=0;
            if (this.obras[i]!=null){
                if(this.obras[i].getNumPaginas()>5000)//5000 pags
                {
                    pt = (float) (this.obras[i].getPreco() - (this.obras[i].getPreco()*0.2));
                }else {
                    pt = p + pt + this.obras[i].getPreco();
                }
                if(this.obras[i].getNumPaginas()>max)
                {
                    max = this.obras[i].getNumPaginas(); //Valor máximo de páginas
                }
                if (max>=(this.obras[i].getNumPaginas()*4))//nº de páginas da subcolecção com mais páginas
                {
                    pt = (float) (this.obras[i].getPreco() - (this.obras[i].getPreco()*0.1));
                }
            }
        }
        return pt;
    }

    /**
     * Adiciona obra à coleção.
     * @param obra o nome da obra.
     * @return true se adicionar uma obra, false caso não seja possível
     */
    public boolean addObra(Obra obra){
        if (obra == null) { //verifica se a obra é null
            return false;
        }

        int indice = getIndexOfObra(obra.getTitulo()); //Verifica se a obra já existe
        if (indice >= 0 || numObras>=MAXOBRAS) {
            return false;
        }

        obras[numObras] = obra;
        numObras ++; //adiciona a obra à variavel numObra
        return true;
    }

    /**
     * Adiciona obra à coleção.
     * @param titulo titulo da obra.
     * @return i (indice) caso encontre o livro e -1 caso não o encontre.
     */
    public int getIndexOfObra(String titulo){
        //procura a obra na lista de obras e devolve o indice de onde ela está, se não dá -1
        for (int i = 0; i < numObras; i++) {
            Obra obra = obras[i];
            if (obra != null && obra.getTitulo().equals(titulo)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Remove uma obra da coleção.
     * @param titulo titulo da obra a remover.
     * @return null caso o livro não exista e ObrasRemovido, que é um array sem a obra escolhida.
     */
    public Obra remObra(String titulo) {
        int indice = getIndexOfObra(titulo);
        if (indice < 0) {
            return null;
        }
        Obra ObrasRemovido = obras[indice];

        // Remove o livro
        Obra[] novaObra = new Obra[obras.length - 1];   // cria-se o array com menos um indice que o original
        for (int i = 0; i < indice; i++) {                   // Este for serve para remover as colecoes à esquerda do que se remove
            novaObra[i] = obras[i];
        }
        for (int i = indice + 1; i < obras.length; i++) {   // Este Este for serve para "puxar" as colecoes à direita do removido para a esquerda
            novaObra[i - 1] = obras[i];
        }
        obras = novaObra;
        numObras--;

        return ObrasRemovido;
    }

    /**
     * Remove todas as obras com o ơtulo igual ao ơtulo recebido, ignorando as
     * diferenças entre maiúsculas e minúsculas (ignore case), mesmo que estas obras se encontrem
     * dentro de subcolecções e em qualquer nível de profundidade.
     * @param titulo titulo da obra a remover.
     * @return true true caso tenha removido pelo menos uma obra, false caso contrário.
     */
    public boolean remAllObras (String titulo){
        boolean check = false;
        for (int i = 0; i < numObras; i++) {
            if (obras[i] != null && obras[i].getTitulo().equalsIgnoreCase(titulo)) {
                remObra(titulo);
                check = true;
            }
            if (obras[i] instanceof Coleccao) {
                Coleccao c = (Coleccao) obras[i];
                if (c.remAllObras(titulo)) {
                    check = true;
                }
            }
        }
        return check;
    }

    /**
     * Calcula o nº de obras que uma pessoa tem em seu nome.
     * @param autorEditor nome da pessoa que se quer o nº de obras.
     * @return o nº de obras atribuidas à pessoa.
     */
    public int getNumObrasFromPerson(String autorEditor) {

        int autorObras = 0;

        //Incrementa caso o autorEditor esteja no array editores
        if(editores != null)
            for(int i = 0; i < editores.length; i ++) {
                if(editores[i].equals(autorEditor)) {
                    autorObras++;
                }
            }

        for(int j = 0; j < obras.length; j++) {
            if(obras[j] != null) {
                if(obras[j] instanceof Livro && ((Livro) obras[j]).contemAutor(autorEditor)) {
                    autorObras++;
                }else {
                    Coleccao subCol = (Coleccao) obras[j];
                    if(subCol != null) {
                        for(int t = 0; t < subCol.obras.length; t++) {
                            if(subCol.obras[t] != null && subCol.obras[t] instanceof Livro) {
                                if(((Livro) subCol.obras[t]).contemAutor(autorEditor)) {
                                    autorObras++;
                                }
                                if(subCol.editores[t].equals(autorEditor)) {
                                    autorObras++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return autorObras;
    }

    /**
     * Devovle um array com os livros de um determindado autor.
     * @param autorNome nome da pessoa que se quer array de livros.
     * @return um array com os livros de um determindado autor.
     */
    public Livro[] getLivrosComoAutor(String autorNome) {
        Livro[] autor_lista = new Livro[numObras]; // lista para inserir os títulos do autor
        int contador = 0;

        for (int i = 0; i < numObras; i++) {
            if (obras[i] instanceof Livro && ((Livro) obras[i]).contemAutor(autorNome)) {
                autor_lista[contador] = (Livro) obras[i]; // adiciona o livro à lista do autor
                contador++;
            }
        }

        for (int j = 0; j < numObras; j++) {
            for (int h=0;h<getProfundidade();h++){
                if (obras[j] instanceof Coleccao) {
                    autor_lista = mergeWithoutRepetitions(autor_lista, ((Coleccao) obras[j]).getLivrosComoAutor(autorNome));
                    numObras = autor_lista.length; // atualiza o tamanho do array
                }
            }

        }
        return autor_lista;
    }


    /**
     * Devolve uma string compatível com os outputs desejados
     */
    public String toString() { //código para o output desejado
        String p = "Coleção " + getTitulo() + ", editores " +Arrays.toString(editores) + ", "+ getNumLivros() + " livros, "+
                getNumPaginas() + "p, " + getPreco() + "€ \n";

        for (int i=0; i<numObras;i++){
            if (obras[i]!=null){
                p = p + "\t" + obras[i].toString() + "\n";
            }
        }
        return p;
    }

    /**
     * Devolve um array, sem nulls, com todos os autores e editores
     * existentes na colecção. O resultado não deve conter repetições.ions
     */
    public String[] getAutoresEditores() {
        String[] autores_lista = editores;

        for (int i = 0; i < obras.length; i++) {

            if(obras[i] != null) {
                if(obras[i] instanceof Livro) {
                    autores_lista = mergeWithoutRepetitions(autores_lista, ((Livro) obras[i]).getAutores());

                }else {
                    autores_lista = mergeWithoutRepetitions(autores_lista, ((Coleccao) obras[i]).getAutoresEditores());
                }
            }
        }
        return autores_lista;
    }

    /**
     * Método que recebendo dois arrays sem repetições devolve um novo array com
     * todos os elementos dos arrays recebidos mas sem repetições
     */
    private static String[] mergeWithoutRepetitions(String[] a1, String[] a2) { //Une dois arrays
        String[] at = new String[a1.length + a2.length]; //Array que tem como comprimento a soma de a1 e a2
        int indice = 0;
        boolean rep; //Variavel para verificar repetições
        for (int i = 0; i < a1.length; i++) {
            rep = false;
            for (int j = 0; j < indice; j++) {
                if (a1[i].equals(at[j])) { // Se já existe no at (array auxiliar) o valor referido no indice
                    rep = true;
                    break;
                }
            }

            if (!rep) { //Se não houver repetição o valor de a1 em i é atribuido a at no indice, deixando esse incrementado se tal ocorrer
                at[indice] = a1[i];
                indice++;
            }
        }

        //mesmo metodo para o a2
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

        //cria-se o array af com o comprimento indice para lhe ser atribuido os valores de at
        String[] af = new String[indice];
        for (int i = 0; i < indice; i++) {
            af[i] = at[i];
        }

        return af;
    }

    /**
     * Método idêntico ao método anterior mas agora com arrays de livros
     */
    private static Livro[] mergeWithoutRepetitions(Livro[] a1, Livro[] a2) {
        Livro[] l_final = new Livro[a1.length+a2.length+1]; //último elemento será sempre null

        for(int i = 0; i < a1.length; i++){
            l_final[i] = a1[i];
        }
        for(int i = 0; i < a2.length; i++){
            l_final[a1.length+i] = a2[i];
        }

        //remover elementos repetidos
        for(int i = 0; i < l_final.length-1; i++) {
            for(int j = 0; j < l_final.length-1; j++) {
                if(!(l_final[i] == null || l_final[j] == null) && i!=j && l_final[i].equals(l_final[j])) {
                    for(int k = j; k < l_final.length-1; k++) {
                        l_final[k] = l_final[k+1];
                    }
                    j--;
                }
            }
        }

        for(int i = 0; i < l_final.length; i++) {
            if(l_final[i] == null) l_final = Arrays.copyOf(l_final, i);
        }

        return l_final;
    }

    /**
     * Método o nº de livros da coleção
     */
    public int getNumLivros() {
        int cont = 0;
        for (Obra obra : obras) {
            if (obra instanceof Livro) {
                cont++;
            }
        }
        return cont;
    }

    /**
     * Método o nº de coleções
     */
    public int getNumColeccoes() {
        int cont = 0;
        for (Obra obra : obras) {
            if (obra instanceof Coleccao) {
                cont++;
            }
        }
        return cont;
    }

    /**
     * Método que devolve o comprimento máximo das cadeias de coleções
     * com coleções dentro. Uma colecção, por si, deve devolve uma profundidade de 1.
     */
    public int getProfundidade() {
        int prof = 1;
        for (Obra obra : obras) {
            if (obra instanceof Coleccao) {
                int subProf = ((Coleccao) obra).getProfundidade() + 1;
                if (subProf > prof) {
                    prof = subProf;
                }
            }
        }
        return prof;
    }


    /**
     * Verifica se a coleção foi escrito por um determinado autor.
     * @param c a coleção a ser comparado
     * @return true se as coleções forem iguais, false caso contrário
     */
    public boolean equals(Coleccao c) {
        if (!this.getTitulo().equals(c.getTitulo())) {
            return false;
        }

        String[] merge = mergeWithoutRepetitions(this.editores, c.editores);
        if(merge.length != this.editores.length || merge.length != c.editores.length){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Mostra uma colecção segundo os outputs desejados
     */
    public void print(String prefix) { //print c/ prefixo
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
                new String[]{"João Mendonça", "Manuel Alfazema"});

        boolean res;

        res = c1.addObra(l1);
        res = c1.addObra(l2);
        System.out.println("c1 -> " + c1);
        c1.print("");
        System.out.println();

        // adicionar um livro com nome de outro já existente
        res = c1.addObra(l2);
        System.out.println("adição novamente de Viagem aos Pirinéus a c1 -> " + res);
        System.out.println("c1 -> " + c1);
        System.out.println();

        // Outra colecção
        Livro l21 = new Livro("Viagem aos Himalaias 2", 340, 12.3f,
                new String[]{"João Mendonça", "Mário Andrade"});
        Livro l22 = new Livro("Viagem aos Pirinéus 2", 270, 11.5f,
                new String[]{"João Mendonça", "Júlio Pomar"});

        Coleccao cx2 = new Coleccao("Outono",
                new String[]{"João Mendonça", "Manuel Antunes"});
        cx2.addObra(l21);
        cx2.addObra(l22);
        System.out.println("cx2 -> " + cx2);
        cx2.print("");
        System.out.println();

        // adicioná-la a c1
        c1.addObra(cx2);
        System.out.println("c1 após adição da colecção cx2 -> " + c1);
        c1.print("");
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
        System.out.println("Livros de " + nome + " -> " + Arrays.toString(obras));
        System.out.println();

        // remallObra
        String nomeLivro = "Viagem aos Himalaias";
        Boolean rao = c1.remAllObras(nomeLivro);
        System.out.println("Remoção de " + nomeLivro + " (remAllobra)-> " + rao);
        c1.print("");
        System.out.println();

        //equals
        System.out.println("equals de c1 com c1 -> "+ c1.equals(c1));
        System.out.println("equals de c1 com cx2 -> "+ c1.equals(cx2));

    }
}
