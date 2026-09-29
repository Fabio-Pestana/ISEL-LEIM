package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fabio Pestana - A50756
 * ISEL - LEIM 22/23
 */
public class PersonalTrainer extends Employee {

    private int experiencia;
    private static final int MAX_CLIENTS = 15;
    private int numClientes;
    private static final int clienteBonus = 8;
    ArrayList<String> ptClients = new ArrayList<>();

    /**
     * Constrói um objeto PersonalTrainer com os atributos especificados.
     * @param nome O nome do pt.
     * @param idade A idade do pt.
     * @param number O numero de telemovel do pt.
     * @param password A password do pt.
     * @param genero O genero do pt.
     * @param dataContratacao A data de contratacao do pt.
     * @param yearsOfContract O numero de anos de contrato do pt.
     * @param experiencia A experiência do pt.
     */
    public PersonalTrainer(String nome, int idade, String number, String password, String genero, LocalDate dataContratacao, int yearsOfContract, int experiencia) {
        super(nome, idade, number, password, genero, "Instrutor", 900, dataContratacao, yearsOfContract);
        this.experiencia = experiencia;
    }

    /**
     * Obtém os clientes do pt.
     * @return Um array de strings com os nomes dos clientes do pt.
     */
    public String[] getClients() {
        List<String> nomeClient = new ArrayList<>();
        for (String client : ptClients) {
            if (!nomeClient.contains(client)) {
                nomeClient.add(client);
            }
        }
        return nomeClient.toArray(new String[numClientes]);
    }

    /**
     * Adiciona um cliente ao pt.
     * @param cliente O nome do cliente a ser adicionado, é obrigatório este ter o pack 2.
     * @return true se o cliente for adicionado com sucesso, false caso contrário.
     */
    public boolean addClient(String cliente)
    {
        int aux = Ginasio.getIndexOfGymM(cliente);
        if (cliente == null ||numClientes >= MAX_CLIENTS || Ginasio.GymMembers.get(aux).getPayment().getNumeroDoPack() != 2)
        {
            return false;
        }

        for (String Gclient : ptClients) {
            if (Gclient.contains(cliente))
                return false;
        }

        ptClients.add(cliente);
        numClientes++;
        return true;
    }

    /**
     * Remove um cliente do pt.
     * @param cliente O nome do cliente a ser removido.
     * @return true se o cliente for removido com sucesso, false caso contrário.
     */
    public boolean delClient(String cliente) {
        int index = getIndexOfGymM(cliente);
        if (index == -1)
            return false;

        ptClients.remove(cliente);
        numClientes--;
        return true;
    }

    /**
     * Retorna o índice do cliente na lista de clientes do pt.
     * @param name nome do cliente a ser procurado.
     * @return índice do cliente na lista ou -1 se não existir.
     */
    public int getIndexOfGymM(String name)
    {
        for(int i=0; i<ptClients.size(); i++)
        {
            if(ptClients.get(i) != null && ptClients.get(i).equals(name))
                return i;
        }
        return -1;
    }

    /**
     * Atualiza o salário do pt.
     * @return novo salário.
     */
    public double uptadeSalario()
    {
        return Bonus() + this.getSalario();
    }

    /**
     * Retorna a experiência do pt.
     * @return experiência.
     */
    public int getExperiencia() {
        return experiencia;
    }

    /**
     * Atualiza a experiência do pt.
     * @param experiencia nova experiência.
     */
    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    /**
     * Calcula o bônus do pt, caso ele tenha pelo menos 8 clientes.
     * @return valor do bônus.
     */
    public double Bonus(){
        Pack priceMonthsNp = new Pack(1);
        Pack clientesPack = new Pack(2);
        double bonus = 0;
        if (numClientes>=clienteBonus) {
            for (int i = 0; i < numClientes; i++)
            {
                bonus = bonus + clientesPack.setPrice() - priceMonthsNp.setPrice();
            }
            return bonus/3;
        }
        return 0;
    }

    /**
     * Cria um elemento XML com base nos dados do pt.
     * @param doc O documento XML.
     * @return O elemento criado.
     */
    public Element createElement(Document doc) {

        Element ptElement = doc.createElement("Instrutor");

        Element nomeElement = doc.createElement("Nome");
        nomeElement.setTextContent(this.getName());
        ptElement.appendChild(nomeElement);
        Element idadeElement = doc.createElement("Idade");
        idadeElement.setTextContent(Integer.toString(this.getAge()));
        ptElement.appendChild(idadeElement);
        Element numeroElement = doc.createElement("Numero");
        numeroElement.setTextContent(this.getNumber());
        ptElement.appendChild(numeroElement);
        Element passElement = doc.createElement("Password");
        passElement.setTextContent(this.getPassword());
        ptElement.appendChild(passElement);
        Element genreElement = doc.createElement("Genero");
        genreElement.setTextContent(this.getGenero());
        ptElement.appendChild(genreElement);

        Element dataElement = doc.createElement("DataContratacao");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");// Definir o formato da string desejada
        dataElement.setTextContent(this.getDataContratacao().format(formatter));// Converter LocalDate para String
        ptElement.appendChild(dataElement);

        Element YoCElement = doc.createElement("YearsOfContract");
        YoCElement.setTextContent(Integer.toString(this.getYearsOfContract()));
        ptElement.appendChild(YoCElement);

        Element eElement = doc.createElement("Experiencia");
        eElement.setTextContent(Integer.toString(this.getExperiencia()));
        ptElement.appendChild(eElement);


        Element PtClientesElement = doc.createElement("PtClientes");

        // adiciona cada cliente como um elemento filho de PtClientes
        for (int i = 0; i < ptClients.size(); i++) {
            Element clienteElement = doc.createElement("ClientNome");
            clienteElement.setTextContent(ptClients.get(i));
            PtClientesElement.appendChild(clienteElement);
        }
        // adiciona o elemento PtClientes como filho do nó ptElement
        ptElement.appendChild(PtClientesElement);

        return ptElement;
    }

    /**
     * Retorna uma string com os dados do pt.
     * @return string com os dados do objeto.
     */
    public String toString() {
        return this.getCargo() + " " + this.getName() +
                ", idade = " + this.getAge() +
                ", sexo = " + this.getGenero() +
                ", numero = " + this.getNumber() +
                " , salario = " + this.uptadeSalario() +
                " , experiencia = " + this.getExperiencia() +
                " , dataContratacao = " + this.getDataContratacao()+ '\n' +
                ", anos de contrato =" + this.getYearsOfContract() +
                " , data fim de Contrato = " + this.endContract()+ '\n' +
                "Clientes: " +  Arrays.toString(this.getClients());
    }
}

