package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Fabio Pestana - A50756
 * ISEL - LEIM 22/23
 */

public class Employee extends Person{
    private String cargo;
    private int salario;
    private LocalDate dataContratacao;
    private int yearsOfContract;

    /**
     * Constrói um objeto Employee com os atributos especificados
     * @param nome O nome do funcionario.
     * @param idade A idade do funcionario.
     * @param number O numero de telemovel do funcionario.
     * @param password A password do funcionario.
     * @param genero O genero do funcionario.
     * @param cargo O cargo do funcionario.
     * @param salario O salario do funcionario.
     * @param dataContratacao A data de contratacao do funcionario.
     * @param yearsOfContract O numero de anos de contrato do funcionario.
     */
    public Employee(String nome, int idade, String number, String password, String genero, String cargo, int salario, LocalDate dataContratacao, int yearsOfContract) {
        super(nome, idade, number, password, genero);
        if (!isCargoValid(cargo))
            throw new IllegalArgumentException("Cargo invalido");
        this.cargo = cargo;
        this.salario = salario;
        this.dataContratacao = dataContratacao;
        this.yearsOfContract = yearsOfContract;
    }

    /**
     * Verifica se o cargo fornecido é válido (só sao aceites: Instrutor, Gestor e Auxiliar de limpeza).
     * @param cargo .
     * @return true se o cargo for válido, false caso contrário.
     */
    public boolean isCargoValid(String cargo)
    {
        String i = "Instrutor";
        String g = "Gestor";
        String l = "Auxiliar de limpeza";
        return (cargo.equalsIgnoreCase(i) || cargo.equalsIgnoreCase(g)|| cargo.equalsIgnoreCase(l));
    }

    /**
     * Obtém o cargo do funcionario.
     * @return cargo.
     */
    public String getCargo() {return cargo;}

    /**
     * Atualiza o salario do funcionario.
     * @param newSalary o novo salario.
     */
    public void Raise(int newSalary){ this.salario=newSalary;}

    /**
     * Retorna o salario do funcionario.
     * @return salario.
     */
    public double getSalario() {return salario;}

    /**
     * Retorna a data de contratacao do funcionario.
     * @return data.
     */
    public LocalDate getDataContratacao() {return dataContratacao;}

    /**
     * Retorna o numero de anos de contrato do funcionario.
     * @return número de anos.
     */
    public int getYearsOfContract() {return yearsOfContract;}

    /**
     * Atualiza o numero de anos de contrato do funcionario.
     * @param yearsOfContract O novo numero.
     */
    public void setYearsOfContract(int yearsOfContract) {this.yearsOfContract = yearsOfContract;}

    /**
     * Calcula a data de finalizacao do contrato.
     * @return data.
     */
    public LocalDate endContract() {return this.dataContratacao.plusYears(this.yearsOfContract);}

    /**
     * Atualiza a data de contratação para a data atual.
     */
    public void newContract(){ this.dataContratacao = LocalDate.now();}

    /**
     * Cria um elemento XML com base nos dados do funcionário.
     * @param doc O documento XML.
     * @return O elemento criado.
     */
    public Element createElement(Document doc) {

        Element employeeElement = doc.createElement("Employee");

        Element nomeElement = doc.createElement("Nome");
        nomeElement.setTextContent(this.getName());
        employeeElement.appendChild(nomeElement);

        Element idadeElement = doc.createElement("Idade");
        idadeElement.setTextContent(Integer.toString(this.getAge()));
        employeeElement.appendChild(idadeElement);

        Element numeroElement = doc.createElement("Numero");
        numeroElement.setTextContent(this.getNumber());
        employeeElement.appendChild(numeroElement);

        Element passElement = doc.createElement("Password");
        passElement.setTextContent(this.getPassword());
        employeeElement.appendChild(passElement);

        Element genreElement = doc.createElement("Genero");
        genreElement.setTextContent(this.getGenero());
        employeeElement.appendChild(genreElement);

        Element cargoElement = doc.createElement("Cargo");
        cargoElement.setTextContent(this.getCargo());
        employeeElement.appendChild(cargoElement);

        Element salarioElement = doc.createElement("Salario");
        salarioElement.setTextContent(Double.toString(this.getSalario()));
        employeeElement.appendChild(salarioElement);

        Element dataElement = doc.createElement("DataContratacao");

        // Definir o formato da string desejada
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Converte LocalDate para String
        dataElement.setTextContent(this.getDataContratacao().format(formatter));
        employeeElement.appendChild(dataElement);

        Element YoCElement = doc.createElement("YearsOfContract");
        YoCElement.setTextContent(Integer.toString(this.getYearsOfContract()));
        employeeElement.appendChild(YoCElement);

        return employeeElement;
    }

    /**
     * Retorna uma string com os dados do funcionario.
     * @return string com os dados do objeto.
     */
    public String toString() {
        return "nome =" + this.getName() +
                ", idade = " + this.getAge() +
                ", sexo = " + this.getGenero() +
                ", numero = " + this.getNumber() +
                ", cargo =" + this.getCargo() +
                ", salario =" + this.getSalario() +
                ", dataContratacao =" + this.getDataContratacao() +
                ", anos de contrato =" + this.getYearsOfContract() +
                ", fim do contrato =" + this.endContract() ;
    }
}
