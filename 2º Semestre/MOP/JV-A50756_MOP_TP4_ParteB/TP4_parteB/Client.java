package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Fabio Pestana - A50756
 * ISEL - LEIM 22/23
 */

public class Client extends Person{
    private int peso;
    private int altura;
    private LocalDate dataInscricao;
    private int ClientPack;
    private Pack pack;

    private int taxaDeAtivFisica;

    /**
     * Constrói um objeto Client com os atributos especificados
     * @param nome O nome do cliente.
     * @param idade A idade do cliente.
     * @param number O numero de telemovel do cliente.
     * @param password A password do cliente.
     * @param genero O genero do cliente.
     * @param peso O cargo do cliente.
     * @param altura O salario do cliente.
     * @param ClientPack O numero do Pack do cliente.
     * @param dataInscricao A data de inscricao do cliente.
     * @param taxaDeAtivFisica A taxa de atividade fisica do cliente.
     *            (0 - Sedentario, 1 - Leve, 2 - Moderado, 3 - Intenso,  4 - Muito Intenso)
     */
    public Client(String nome, int idade, String number, String password, String genero, int peso, int altura, int ClientPack, LocalDate dataInscricao,int taxaDeAtivFisica) {
        super(nome, idade, number, password, genero);
        if (peso<=0)
            throw new IllegalArgumentException("Peso invalido");
        this.peso = peso;
        if (altura<=0)
            throw new IllegalArgumentException("Altura invalida");
        this.altura = altura;
        this.ClientPack = ClientPack;
        this.pack = new Pack(this.ClientPack);
        this.dataInscricao = dataInscricao;
        if (taxaDeAtivFisica>4 || taxaDeAtivFisica<0)
            throw new IllegalArgumentException("Taxa de Atividae Fisica invalida");
        this.taxaDeAtivFisica = taxaDeAtivFisica;
    }

    /**
     * Calcula as calorias com base na taxa de atividade física (utiliza-se a equação de Harris-Benedict)
     * @return O valor das calorias calculadas.
     */
    public int caloriesCalculator () //parametro a taxa de atividade fisica
    {
        double tmb = 0;
        double aux; //taxa de atividade física

        if (this.taxaDeAtivFisica == 0)        //Sedentario
            aux = 1.2;
        else if(this.taxaDeAtivFisica== 1)     //Leve
            aux = 1.375;
        else if(this.taxaDeAtivFisica== 2)     //Moderado
            aux = 1.55;
        else if(this.taxaDeAtivFisica== 3)     //Intenso
            aux = 1.725;
        else if (this.taxaDeAtivFisica ==4)    //Muito Intenso
            aux = 1.9;
        else
            return 0;

        // taxa metabólica basal
        if (getGenero().equalsIgnoreCase("masculino"))
        {
            tmb = aux * (88.36 + (13.4 * this.peso) + (4.8 * this.altura) - (5.7 * this.getAge()));
        }else {
            tmb = aux * (447.6 + (9.2 * this.peso) + (3.1 * this.altura) - (4.3 * this.getAge()));
        }
        return (int) tmb;
    }

    /**
     * Obtém o valor do défice calórico do cliente.
     * @return valor calórico.
     */
    public int getCaloricDeficit()
    {
        return caloriesCalculator () - 500;
    }

    /**
     * Obtém o valor de calorias para aumento de peso do cliente.
     * @return valor calórico.
     */
    public int getGainWeight()
    {
        return caloriesCalculator () + 500;
    }

    /**
     * Obtém a taxa de atividade fisica do cliente.
     * @return taxa de atividade fisica.
     */
    public int getTaxaDeAtivFisica() {return taxaDeAtivFisica;}

    /**
     * Atualiza a taxa de atividade fisica do cliente.
     * @param taxaDeAtivFisica A nova taxa.
     */
    public void setTaxaDeAtivFisica(int taxaDeAtivFisica) {this.taxaDeAtivFisica = taxaDeAtivFisica;}

    /**
     * Obtém o peso do cliente.
     * @return peso.
     */
    public int getPeso() {return peso;}

    /**
     * Obtém a altura do cliente.
     * @return altura.
     */
    public int getAltura() {return altura;}

    /**
     * Obtém o ClientPack do cliente.
     * @return ClientPack.
     */
    public int getClientPack(){return ClientPack;}

    /**
     * Atualiza o peso do cliente.
     * @param NovoPeso O novo peso.
     */
    public void setPeso(int NovoPeso)
    {
        this.peso = NovoPeso;
    }

    /**
     * Atualiza a altura do cliente.
     * @param NovaAltura A nova altura.
     */
    public void setAltura(int NovaAltura)
    {
        this.altura = NovaAltura;
    }

    /**
     * Obtém a data de inscricao do cliente.
     * @return data.
     */
    public LocalDate getDataInscricao() {
        return dataInscricao;
    }

    /**
     * Atualiza a data de inscricao do cliente.
     * @param dataInscricao A nova data.
     */
    public void setDataInscricao(LocalDate dataInscricao) {this.dataInscricao = dataInscricao;}

    /**
     * Obtém o pack do cliente.
     * @return pack.
     */
    public Pack getPayment(){return this.pack;}

    /**
     * Cria um elemento XML com base nos dados do cliente.
     * @param doc O documento XML.
     * @return O elemento criado.
     */
    public Element createElement(Document doc) {

        Element clientElement = doc.createElement("Client");

        Element nomeElement = doc.createElement("Nome");
        nomeElement.setTextContent(this.getName());
        clientElement.appendChild(nomeElement);
        Element idadeElement = doc.createElement("Idade");
        idadeElement.setTextContent(Integer.toString(this.getAge()));
        clientElement.appendChild(idadeElement);
        Element numeroElement = doc.createElement("Numero");
        numeroElement.setTextContent(this.getNumber());
        clientElement.appendChild(numeroElement);
        Element passElement = doc.createElement("Password");
        passElement.setTextContent(this.getPassword());
        clientElement.appendChild(passElement);
        Element genreElement = doc.createElement("Genero");
        genreElement.setTextContent(this.getGenero());
        clientElement.appendChild(genreElement);
        Element pesoElement = doc.createElement("Peso");
        pesoElement.setTextContent(Integer.toString(this.getPeso()));
        clientElement.appendChild(pesoElement);
        Element alturaElement = doc.createElement("Altura");
        alturaElement.setTextContent(Integer.toString(this.getAltura()));
        clientElement.appendChild(alturaElement);
        Element cpElement = doc.createElement("ClientPack");
        cpElement.setTextContent(Integer.toString(this.getClientPack()));
        clientElement.appendChild(cpElement);

        Element dataElement = doc.createElement("DataInscricao");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");// Definir o formato da string desejada
        dataElement.setTextContent(this.getDataInscricao().format(formatter));// Converter LocalDate para String
        clientElement.appendChild(dataElement);

        Element tfaElement = doc.createElement("TaxaDeAtividadeFisica");
        tfaElement.setTextContent(Integer.toString(this.getTaxaDeAtivFisica()));
        clientElement.appendChild(tfaElement);


        return clientElement;
    }

    /**
     * Retorna uma string com os dados do cliente.
     * @return string com os dados do objeto.
     */
    public String toString() {
        return "Cliente: " +
                "nome = " + this.getName() +
                ", idade = " + this.getAge() +
                ", sexo = " + this.getGenero() +
                ", numero = " + this.getNumber() +
                ", peso = " + this.getPeso() +
                ", altura = " + this.getAltura() +
                ", taxa de atividade fisica = " + this.getTaxaDeAtivFisica() +
                ", dataInscricao = " +  this.getDataInscricao() +
                ", Gym pack = " + getPayment().getNumeroDoPack() +
                ", despesas = " + getPayment().setPrice() +
                ", data da expiracao do pack = " + getPayment().getDataExpiracaoContrato(this.getDataInscricao());
    }
}