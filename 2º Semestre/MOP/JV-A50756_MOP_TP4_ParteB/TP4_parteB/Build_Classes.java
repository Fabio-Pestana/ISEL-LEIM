package tps.tp4;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Fabio Pestana - A50756
 * ISEL - LEIM 22/23
 */


/**
 * Classe responsável por construir diferentes tipos de objetos com base em elementos XML.
 */
public class    Build_Classes
{

    /**
     * Constrói e retorna um objeto Client com base num nó XML.
     * @param nNode nó XML contendo os atributos do cliente
     * @return Client construído
     */
    public static Person buildC(Node nNode) {

        //transforma o node num Element
        Element eElement = (Element) nNode;

        //procura no node os atributos necessarios para criar um Espectaculo
        String nome = eElement.getElementsByTagName("Nome").item(0).getTextContent();
        int idade = Integer.parseInt(eElement.getElementsByTagName("Idade").item(0).getTextContent());
        String numero = eElement.getElementsByTagName("Numero").item(0).getTextContent();
        String password = eElement.getElementsByTagName("Password").item(0).getTextContent();
        String genero = eElement.getElementsByTagName("Genero").item(0).getTextContent();
        int peso = Integer.parseInt(eElement.getElementsByTagName("Peso").item(0).getTextContent());
        int altura = Integer.parseInt(eElement.getElementsByTagName("Altura").item(0).getTextContent());
        int pack = Integer.parseInt(eElement.getElementsByTagName("ClientPack").item(0).getTextContent());
        String dataInscricaoStr = eElement.getElementsByTagName("DataInscricao").item(0).getTextContent();
        LocalDate dataInscricao = LocalDate.parse(dataInscricaoStr, DateTimeFormatter.ISO_LOCAL_DATE);
        int TFA = Integer.parseInt(eElement.getElementsByTagName("TaxaDeAtividadeFisica").item(0).getTextContent());

        Client client = new Client(nome, idade, numero, password,genero,peso,altura,pack,dataInscricao,TFA);
        return client;
    }

    /**
     * Constrói e retorna um objeto Employee com base num nó XML.
     * @param nNode nó XML contendo os atributos do funcionário
     * @return Employee construído
     */
    public static Person buildE(Node nNode) {
        //transforma o node num Element
        Element eElement = (Element) nNode;
        //procura no node os atributos necessarios para criar um Espectaculo
        String nome = eElement.getElementsByTagName("Nome").item(0).getTextContent();
        int idade = Integer.parseInt(eElement.getElementsByTagName("Idade").item(0).getTextContent());
        String numero = eElement.getElementsByTagName("Numero").item(0).getTextContent();
        String password = eElement.getElementsByTagName("Password").item(0).getTextContent();
        String genero = eElement.getElementsByTagName("Genero").item(0).getTextContent();
        String cargo = eElement.getElementsByTagName("Cargo").item(0).getTextContent();
        int salario = Integer.parseInt(eElement.getElementsByTagName("Salario").item(0).getTextContent());
        String DataContratacaoStr = eElement.getElementsByTagName("DataContratacao").item(0).getTextContent();
        LocalDate DataContratacao = LocalDate.parse(DataContratacaoStr, DateTimeFormatter.ISO_LOCAL_DATE);
        int YearsOfContract = Integer.parseInt(eElement.getElementsByTagName("YearsOfContract").item(0).getTextContent());

        Employee employee = new Employee(nome, idade, numero, password, genero, cargo, salario, DataContratacao, YearsOfContract);

        return employee;
    }

    /**
     * Constrói e retorna um objeto PersonalTrainer com base num nó XML.
     * @param nNode nó XML contendo os atributos do PersonalTrainer
     * @return PersonalTrainer construído
     */
    public static Person buildPt(Node nNode) {
        //transforma o node num Element
        Element eElement = (Element) nNode;
        //procura no node os atributos necessarios para criar um Espectaculo
        String nome = eElement.getElementsByTagName("Nome").item(0).getTextContent();
        int idade = Integer.parseInt(eElement.getElementsByTagName("Idade").item(0).getTextContent());
        String numero = eElement.getElementsByTagName("Numero").item(0).getTextContent();
        String password = eElement.getElementsByTagName("Password").item(0).getTextContent();
        String genero = eElement.getElementsByTagName("Genero").item(0).getTextContent();
        String DataContratacaoStr = eElement.getElementsByTagName("DataContratacao").item(0).getTextContent();
        LocalDate DataContratacao = LocalDate.parse(DataContratacaoStr, DateTimeFormatter.ISO_LOCAL_DATE);
        int YearsOfContract = Integer.parseInt(eElement.getElementsByTagName("YearsOfContract").item(0).getTextContent());
        int exp = Integer.parseInt(eElement.getElementsByTagName("Experiencia").item(0).getTextContent());

        PersonalTrainer pt = new PersonalTrainer(nome, idade, numero, password, genero, DataContratacao, YearsOfContract, exp);

        NodeList clientes = eElement.getElementsByTagName("ClientNome");
        for (int i = 0; i < clientes.getLength(); i++) {
            Node clienteN = clientes.item(i);
            String cliente = clienteN.getTextContent();
            pt.addClient(cliente);
        }
        return pt;
    }

    /**
     * Constrói e retorna um objeto Equipamento com base num nó XML.
     * @param nNode nó XML contendo os atributos do Equipamento
     * @return Equipamento construído
     */
    public static Equipamento buildM(Node nNode) {
        //transforma o node num Element
        Element eElement = (Element) nNode;
        //procura no node os atributos necessarios para criar um Espectaculo
        String nome = eElement.getElementsByTagName("Nome").item(0).getTextContent();
        String DataDeCompraStr = eElement.getElementsByTagName("DataDeCompra").item(0).getTextContent();
        LocalDate DataDeCompra = LocalDate.parse(DataDeCompraStr, DateTimeFormatter.ISO_LOCAL_DATE);
        int custo = Integer.parseInt(eElement.getElementsByTagName("Custo").item(0).getTextContent());

        Equipamento equipamento = new Equipamento(nome, DataDeCompra, custo);

        return equipamento;
    }
}
