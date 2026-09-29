package tp1.pack3Coleccoes;

import java.util.Arrays;

import tp1.pack2Livros.Livro;

import java.util.Arrays;

/**
 * Classe Coleccao, deve conter a descrição de uma colecção, com título, os seus
 * livros, colecções e editores
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

    // array de colecções, estas devem ocupar sempre os menores índices
    private Coleccao[] coleccoes = new Coleccao[MAXOBRAS];

    // deverá conter sempre o número de colecções dentro da colecção
    private int numColeccoes = 0;

    // Editores, tem as mesmas condicionantes que array de autores na classe
    // livro
    private String[] editores;

    /**
     * Construtor; o título tem de ter pelo menos um caracter que não seja um
     * espaço (Character.isWhitespace); o array de editores devem ser pelo menos
     * um e têm as mesmas restrições que os autores dos livros;
     */
    public Coleccao(String titulo, String[] editores)  {
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
        //editores
        if(!Livro.validarNomes(editores))
            throw new IllegalArgumentException("Existem nomes inválidos");
        if(Livro.haRepeticoes(editores))
            throw new IllegalArgumentException("Existem editores repetidos");
        if(editores.length < 1)
            throw new IllegalArgumentException("O array de editores tem de ter pelo menos um editor");
        for(int i = 0; i < editores.length; i++) {
            editores[i] = Livro.removeExtraSpaces(editores[i]);
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
     * Obtem o número total de páginas da colecção, páginas dos livros e das
     * colecções
     */
    public int getNumPaginas() {
        // contamos todas as páginas de todos os livros e de todas as coleções.
        int cont=0;
        for (int i=0;i<numColeccoes;i++){
            cont=cont + this.coleccoes[i].getNumPaginas();
        }
        for (int j=0;j<numLivros;j++){
            cont = cont + this.livros[j].getNumPaginas();
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
        for (int i=0;i<numColeccoes;i++)
        {
            float p=0;
            if(this.coleccoes[i].getNumPaginas()>5000)//5000 pags
            {
                pt = (float) (this.coleccoes[i].getPreco() - (this.coleccoes[i].getPreco()*0.2));
            }else {
                pt = p + pt + this.coleccoes[i].getPreco();
            }
            if(this.coleccoes[i].getNumPaginas()>max)
            {
                max = this.coleccoes[i].getNumPaginas(); //Valor máximo de páginas
            }
            if (max>=(this.coleccoes[i].getNumPaginas()*4))//nº de páginas da subcolecção com mais páginas
            {
                pt = (float) (this.coleccoes[i].getPreco() - (this.coleccoes[i].getPreco()*0.1));
            }
        }
        for (int j=0;j<numLivros;j++) // processo igual ao das coleçoes mas agora para os livros das subcoleções
        {
            float p=0;
            if(this.livros[j].getNumPaginas()>5000)
            {
                pt = (float) (this.livros[j].getPreco() - (this.livros[j].getPreco()*0.2));
            }else {
                pt = p + pt + this.livros[j].getPreco();
            }
            if(this.livros[j].getNumPaginas()>max)
            {
                max = this.livros[j].getNumPaginas();
            }
            if (max>=(this.livros[j].getNumPaginas()*4))
            {
                pt = (float) (this.livros[j].getPreco() - (this.livros[j].getPreco()*0.1));
            }
        }
        return pt;
    }

    /**
     * Adiciona um livro à colecção se puder e este não seja null e a colecção
     * não ficar com livros iguais ao nível imediato da colecção. Deve utilzar o
     * método getIndexOfLivro e getIndexOfColeccao
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
     * Adiciona uma colecção à colecção se puder, esta não seja null e a
     * colecção não ficar com obras imediatas com títulos repetidos. Deve
     * utilizar o método getIndexOfLivro e getIndexOfColeccao
     */
    public boolean addColeccao(Coleccao col) {
        int indice = 0;
        //Mesmo processo que o addLivros, mas agora com coleções, recorrendo aos livros e às subscoleções
        if(numLivros + numColeccoes == MAXOBRAS || col.getTitulo() == null) {
            return false;
        }
        else if(this.getIndexOfColeccao(col.getTitulo()) != -1) {
            return false;
        }
        else {
            while(coleccoes[indice] != null) {
                indice ++;
            }
            coleccoes[indice] = col;
            numColeccoes++;
            return true;
        }
    }

    /**
     * Devolve o index no array de livros onde estiver o livro com o nome
     * pretendido. Devolve -1 caso não o encontre
     */
    private int getIndexOfLivro(String titulo) {
        //procura o livro na lista de livros e devolve o indice de onde está, se não dá -1
        for (int i = 0; i < numLivros; i++) {
            Livro livro = livros[i];
            if (livro != null && livro.getTitulo().equals(titulo)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Devolve o index no array de colecções onde estiver a colecção com o nome
     * pretendido. Devolve -1 caso não o encontre
     */
    private int getIndexOfColeccao(String titulo) {
        //procura a coleção na lista de coleções e devolve o indice de onde está, se não dá -1
        for (int i = 0; i < coleccoes.length; i++) {
            Coleccao col = coleccoes[i];
            if (col != null && col.getTitulo().equals(titulo) ) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Remove do array o livro com o título igual ao título recebido. Devolve o
     * livro removido ou null caso não tenha encontrado o livro. Deve-se
     * utilizar o método getIndexOfLivro. Recorda-se que os livros devem ocupar
     * sempre os menores índices, ou seja, não pode haver nulls entre os livros
     */
    public Livro remLivro(String titulo) {
        int indice = this.getIndexOfLivro(titulo);

        //Caso o livro n exista no array
        if (indice < 0) {
            return null;
        }
        Livro livroRemovido = livros[indice];

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
     * Remove do array de colecções a colecção com o título igual ao título
     * recebido. Devolve a colecção removida ou null caso não tenha encontrado.
     * Deve-se utilizar o método getIndexOfColeccao. Recorda-se que as colecções
     * devem ocupar sempre os menores índices, ou seja, não pode haver nulls
     * entre elas
     */
    public Coleccao remColeccao(String titulo) {
        int indice = this.getIndexOfColeccao(titulo);
        //Caso o livro n exista no array
        if (indice < 0) {
            return null;
        }
        Coleccao ColeccaoRemovido = coleccoes[indice];

        // Remove o livro
        Coleccao[] novaColeccao = new Coleccao[coleccoes.length - 1];   // cria-se o array com menos um indice que o original
        for (int i = 0; i < indice; i++) {                   // Este for serve para remover as colecoes à esquerda do que se remove
            novaColeccao[i] = coleccoes[i];
        }
        for (int i = indice + 1; i < coleccoes.length; i++) {   // Este Este for serve para "puxar" as colecoes à direita do removido para a esquerda
            novaColeccao[i - 1] = coleccoes[i];
        }
        coleccoes = novaColeccao;
        numColeccoes--;
        return ColeccaoRemovido;
    }

    /**
     * Devolve o nº de obras de uma pessoa. Cada colecção deve contabilizar-se
     * como uma obra para os editores.
     */
    public int getNumObrasFromPerson(String autorEditor) {
        int cont = 0;

        cont = cont + this.getLivrosComoAutor(autorEditor).length; //Quantidade de livros em que o autorEditor é autor

        for(int i = 0; i < editores.length; i++) //Quantidade de livros em que o autorEditor é editor
            if(editores[i].equals(autorEditor)) {
                cont++;
            }

        for(int j = 0; j < numColeccoes; j++) //Quantidade de livros em que o autorEditor está na coleção
            cont = cont + coleccoes[j].getNumObrasFromPerson(autorEditor);

        return cont;
    }

    /**
     * Devolver um novo array (sem nulls) com os livros de que a pessoa recebida
     * é autor. Não deve conter repetições, para excluir as repetições devem
     * utilizar o método mergeWithoutRepetitions
     */
    public Livro[] getLivrosComoAutor(String autorNome) {
        Livro[] autor_lista = new Livro[numLivros]; //lista para inserir os titulos do autor
        int contador = 0;

        for (int i = 0; i < numLivros; i++)
        {
            if (this.livros[i].getAutores().equals(autorNome))//verifica se o autornome é um autor dos livros.
            {
                autor_lista[contador]= livros[i];//acresenta o livro à lista do autor
                contador++;
            }
        }
        for (int j=0; j<numColeccoes;j++)
        {
            autor_lista = mergeWithoutRepetitions(autor_lista , coleccoes[j].getLivrosComoAutor(autorNome));//Une o autor lista a tudo o que diz respeito ao autor na coleção
        }
        return autor_lista;
    }

    /**
     * Deve devolver uma string compatível com os outputs desejados
     */
    public String toString() { //código para o output desejado
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
    public String[] getAutoresEditores() { //Une num array todos os autores e editores de uma coleção
        String [] aux_string = this.editores;
        for (int i=0; i<numLivros-1;i++)
        {
            aux_string = mergeWithoutRepetitions(aux_string,this.livros[i].getAutores());
        }
        for (int j=0;j<numColeccoes;j++)
        {
            aux_string = mergeWithoutRepetitions(aux_string, this.coleccoes[j].getAutoresEditores());
        }
        return aux_string;
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
     * Devolve true caso a colecção recebida tenha o mesmo título e a mesma
     * lista de editores. Para verificar verificar se os editores são os mesmos
     * devem utilizar o método mergeWithoutRepetitions
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

        // Outra colecção
        Livro l21 = new Livro("Viagem aos Himalaias 2", 340, 12.3f,
                new String[]{"João Mendonça", "Mário Andrade"});
        Livro l22 = new Livro("Viagem aos Pirinéus 2", 270, 11.5f,
                new String[]{"João Mendonça", "Júlio Pomar"});

        Coleccao cx2 = new Coleccao("Outono",
                new String[]{"João Mendonça", "Manuel Antunes"});
        cx2.addLivro(l21);
        cx2.addLivro(l22);
        System.out.println("cx2 -> " + cx2);
        cx2.print("");
        System.out.println();

        // adicioná-la a c1
        c1.addColeccao(cx2);
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
        System.out
                .println("Livros de " + nome + " -> " + Arrays.toString(obras));
        System.out.println();

        // rem livro
        String nomeLivro = "Viagem aos Himalaias";
        Livro l = c1.remLivro(nomeLivro);
        System.out.println("Remoção de " + nomeLivro + " -> " + l);
        c1.print("");
        System.out.println();
    }
}
g