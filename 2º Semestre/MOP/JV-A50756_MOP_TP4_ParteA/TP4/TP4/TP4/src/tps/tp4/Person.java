package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Fabio Pestana - A50756
 * ISEL - LEIM 22/23
 */

public abstract class Person {
    private String nome;
    private int idade;
    private String number;
    private String password;
    private String genero;

    /**
     * Constrói um objeto Person com os atributos especificados.
     * @param nome O nome da pessoa.
     * @param idade A idade da pessoa.
     * @param number O número de telemovel da pessoa.
     * @param password A password da pessoa.
     * @param genero O genero da pessoa.
     * @throws IllegalArgumentException se algum dos valores de entrada for inválido.
     */
    public Person(String nome, int idade, String number, String password, String genero)
    {
        if(!isNameValid(nome))
            throw new IllegalArgumentException("Nome invalido");
        this.nome = removeExtraSpaces(nome);
        if (idade<16)
            throw new IllegalArgumentException("Idade invalida");
        this.idade = idade;
        if(!isNumberValid(number))
            throw new IllegalArgumentException("Numero invalido");
        this.number = number;
        if (!isPasswordValid(password))
            throw new IllegalArgumentException("Password invalida");
        this.password = password;
        if (!(genero.equalsIgnoreCase("masculino") || genero.equalsIgnoreCase("feminino")))
            throw new IllegalArgumentException("Genero invalido");
        this.genero = genero;
    }

    /**
     * Retorna o nome da pessoa.
     * @return nome.
     */
    public String getName() {return this.nome;}

    /**
     * Retorna a idade da pessoa.
     * @return idade.
     */
    public int getAge() {return this.idade;}

    /**
     * Retorna o número de telemovel da pessoa.
     * @return número.
     */
    public String getNumber() { return this.number;}

    /**
     * Retorna a password da pessoa.
     * @return password.
     */
    public String getPassword() { return this.password;}

    /**
     * Retorna o genero da pessoa.
     * @return genero.
     */
    public String getGenero() { return this.genero;}

    /**
     * Valida se o nome da pessoa é válido.
     * @param name .
     * @return true se o nome for válido, false caso contrário.
     */
    public static boolean isNameValid(String name)
    {
        boolean check = false;
        if (name==null)
            return false;
        else {
            for(int i=0; i< name.length(); i++)
            {
                if(Character.isWhitespace(name.charAt(i)) || Character.isLetter(name.charAt(i)))
                    check = true;
                else
                    return false;
            }
            return check;
        }
    }

    /**
     * Remove espaços extras no nome da pessoa.
     * @param name .
     * @return O nome sem espaços extras.
     */
    public static String removeExtraSpaces(String name) {
        name = name.trim();

        StringBuilder newName = new StringBuilder();

        boolean aux = false;

        for (int i = 0; i < name.length(); i++) {
            if (!Character.isWhitespace(name.charAt(i))) {
                newName.append(name.charAt(i));
                aux = false;
            } else if (!aux) {
                newName.append(' ');
                aux = true;
            }
        }
        return newName.toString();
    }

    /**
     * Valida se o número de telemovel fornecido é válido.
     * @param number .
     * @return true se o número de telemovel for válido, false caso contrário.
     */
    public static boolean isNumberValid(String number)
    {
        boolean check = false;
        if (number ==null || !(number.length()<15 && number.length()>=8)) //tamanho maximo de um numero de telefone
            return false;
        else {
            for(int i = 0; i< number.length(); i++)
            {
                if(Character.isDigit(number.charAt(i)))
                    check = true;
                else
                    return false;
            }
            return check;
        }
    }

    /**
     * Valida se a password fornecida é válida (pelo menos 4 numeros, 4 letras minusculas e uma maiscula).
     * @param password .
     * @return true se a password for válida, false caso contrário.
     */
    public static boolean isPasswordValid (String password)
    {
        int cont_letrasm = 0;
        int cont_letrasM = 0;
        int cont_digitos = 0;

        for (int i=0; i<password.length(); i++)
        {
            if (Character.isLetter(password.charAt(i)))
            {
                if (password.charAt(i)>64 && password.charAt(i)<91)
                    cont_letrasM++;
                else
                    cont_letrasm++;
            }
            if (Character.isDigit(password.charAt(i)))
            {
                cont_digitos++;
            }
        }
        if (cont_letrasm >=4 && cont_digitos >=4 && cont_letrasM>=1) //pelo menos 4 numeros 4 letras minusculas e uma maiscula
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Altera a idade da pessoa.
     * @param NovaIdade A nova idade a ser definida.
     */
    public void setIdade(int NovaIdade)
    {
        if (NovaIdade>=16)
            this.idade = NovaIdade;
    }

    /**
     * Altera a password da pessoa.
     * @param NewPassword A nova password a ser definida.
     */
    public void setPassword(String NewPassword)
    {
        if (isPasswordValid (NewPassword))
            this.password = NewPassword;
    }

    /**
     * Altera o número de telemovel da pessoa.
     * @param newNumber O novo número de telemovel a ser definido.
     */
    public void setNumber(String newNumber)
    {
        if (isNumberValid(newNumber))
            this.number = newNumber;
    }

    /**
     * Valida as credenciais de login da pessoa.
     * @param Name
     * @param Password
     * @return true se as credenciais de login forem válidas, false caso contrário.
     */
    public boolean login(String Name, String Password)
    {
        Name = removeExtraSpaces(Name);
        if (Name.equalsIgnoreCase(this.getName()) && Password.equals(this.getPassword()))
        {
            return true;
        }
        return false;
    }

    /**
     * Metodo abstrado, utilizado para criar um elemento em XML
     * @param doc O documento XML.
     */
    public abstract Element createElement(Document doc);
}