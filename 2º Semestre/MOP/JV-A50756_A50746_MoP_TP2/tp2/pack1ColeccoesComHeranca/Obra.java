package tp2.pack1ColeccoesComHeranca;

/**
 * Classe abstrata que representa uma obra.
 * Implementa a interface IObra.
 */

public abstract class Obra implements IObra {
    private String titulo;

    /**
     * Construtor da classe Obra.
     * @param titulo Título da obra.
     */

    public Obra(String titulo) {

        this.titulo = titulo;
    }

    /**
     * Retorna o título da obra.
     */
    public String getTitulo()
    {
        return this.titulo;
    }

    /**
     * Método estático que valida se há nomes repetidos num array de nomes.
     * @param nomes Array de nomes.
     * @returns Retorna true se não houver nomes repetidos, false caso contrário.
     */
    public static boolean validarNomes(String[] nomes) {
        boolean aux = false;

        // Verifica se há repetições no array
        if (haRepeticoes(nomes)) return false;
        else {
            // Valida cada nome do array de nomes
            for (int i=0;i< nomes.length;i++)
            {
                nomes[i] = removeExtraSpaces(nomes[i]);
                if (validarNome(nomes[i]))
                    aux = true;
                else return false;
            }
            return aux;
        }
    }

    /**
     * Método estático que verifica se há elementos repetidos num array.
     * @param elems Array de elementos.
     * @returns Retorna true se houver elementos repetidos, false caso contrário.
     */

    public static boolean haRepeticoes(String[] elems) {
        if (elems.length <= 1) {
            return false;
        }

        for (int i = 0; i < elems.length-1; i++)
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
     * Método estático que remove espaços extras de uma string.
     * @param nome Nome que será tratado.
     * @returns Retorna o nome sem espaços a mais.
     */
    public static String removeExtraSpaces(String nome) {
        nome = nome.trim();

        StringBuilder novonome = new StringBuilder();
        boolean space = false;
        for (int i = 0; i < nome.length(); i++) {
            if (!Character.isWhitespace(nome.charAt(i))) {
                novonome.append(nome.charAt(i));
                space = false;
            } else if (!space) {
                novonome.append(' ');
                space = true;
            }
        }
        return novonome.toString();
    }

    /**
     * Método que valida se um nome é composto apenas de letras e espaços.
     * @param nome Nome que será validado.
     * @returns Retorna true se o nome é válido, false caso contrário.
     */
    public static boolean validarNome(String nome) {
        boolean check = false;
        if(nome==null)
            return false;
        else {
            for(int i=0; i< nome.length(); i++)
            {
                if(Character.isWhitespace(nome.charAt(i)) || Character.isLetter(nome.charAt(i)))
                    check = true;
                else return false;
            }
            return check;
        }
    }

    /**
     * Método que compara duas obras e verifica se são ou não iguais.
     * @param o Nome Obra que será comparada.
     * @returns Retorna true se forem iguais, false caso contrário.
     */

    public boolean equals(Obra o) {
        if (this.getTitulo() == o.getTitulo()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método que invoca o toString próprio de cada classe filha.
     */
    public String toString(){
        return toString();
    }

    /**
     * Método que invoca o toString com um prefixo antes do resto do output.
     */

    public void print(String prefix) {
        System.out.println(prefix + toString());
    }
}