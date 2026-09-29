package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Fabio Pestana - A50756
 * ISEL - LEIM 22/23
 */

/**
 * Classe responsavel pela gestao de um ginasio.
 */
public class Ginasio
{
    private static final int MAX_MEMBERS = 250;
    private static final int MAX_EMPLOYEES = 10;
    private static final int MAX_MACHINES = 40;
    static int numMembers;
    static int numEmployees;
    static int numMachines;
    static ArrayList<Client> GymMembers = new ArrayList<>();
    static ArrayList<Employee> employees = new ArrayList<>();
    static ArrayList<Equipamento> machines = new ArrayList<>();


    /**
     * Retorna um array com os nomes dos membros do ginásio.
     * @return os nomes dos membros do ginásio
     */
    public static String[] getMembers() {
        List<String> nomeClient = new ArrayList<>();
        for (Client gymMember : GymMembers) {
            String clienteN = gymMember.getName();
            if (!nomeClient.contains(clienteN)) {
                nomeClient.add(clienteN);
            }
        }
        return nomeClient.toArray(new String[numMembers]);
    }

    /**
     * Retorna um array com os nomes dos funcionários do ginásio.
     * @return os nomes dos funcionários do ginásio (adiciona "Treinador" aos PersonalTrainer´s)
     */
    public static String[] getEmployees() {
        List<String> nomeFuncionario = new ArrayList<>();
            for (Employee employee : employees) {
            String empregadoN = employee.getName();
            if (!nomeFuncionario.contains(empregadoN)) {
                if (employee instanceof PersonalTrainer)
                    nomeFuncionario.add("Treinador " + empregadoN);
                else
                    nomeFuncionario.add(empregadoN);
            }
        }

        return nomeFuncionario.toArray(new String[numEmployees]);
    }

    /**
     * Retorna um array com os nomes dos equipamentos do ginásio.
     * @return os nomes dos equipamentos do ginásio
     */
    public static String[] getEquipamento() {
        List<String> nomeEquipamento = new ArrayList<>();
        for (Equipamento machine : machines) {
            String maquinaN = machine.getNome();
            if (!nomeEquipamento.contains(maquinaN)) {
                nomeEquipamento.add(maquinaN);
            }
        }
        return nomeEquipamento.toArray(new String[numMachines]);
    }

    /**
     * Adiciona um membro ao ginásio (confirma se já se atingiu o valor maximo de clientes e se o cliente já existe).
     * @param cliente objeto Client a ser adicionado
     * @return true se o cliente for adicionado com sucesso, false caso contrário
     */
    public static boolean addGymMember(Client cliente)
    {
        if (cliente == null ||numMembers >= MAX_MEMBERS)
        {
            return false;
        }

        for (Client gymMember : GymMembers) {
            if (gymMember.getName().contains(cliente.getName()))
                return false;
        }

        GymMembers.add(cliente);
        numMembers++;
        return true;
    }

    /**
     * Adiciona um funcionario ao ginásio (confirma se já se atingiu o valor maximo de funcionarios e se o funcionario já existe).
     * @param newEmployee objeto Employee a ser adicionado
     * @return true se o funcionario for adicionado com sucesso, false caso contrário
     */
    public static boolean addEmployee(Employee newEmployee)
    {
        if (newEmployee == null ||numEmployees >= MAX_EMPLOYEES)
        {
            return false;
        }

        for (Employee employee : employees) {
            if (employee.getName().contains(newEmployee.getName()))
                return false;
        }

        employees.add(newEmployee);
        numEmployees++;
        return true;
    }

    /**
     * Adiciona uma maquina ao ginásio (confirma se já se atingiu o valor maximo de maquinas e se a maquina já existe).
     * @param maquina objeto Equipamento a ser adicionado
     * @return true se a maquina for adicionada com sucesso, false caso contrário
     */
    public static boolean addMachine(Equipamento maquina)
    {
        if (maquina == null ||numMachines >= MAX_MACHINES)
        {
            return false;
        }

        for (Equipamento machine : machines) {
            if (machine.getNome().contains(maquina.getNome()))
                return false;
        }

        machines.add(maquina);
        numMachines++;
        return true;
    }

    /**
     * Remove um cliente do ginásio (utiliza o metodo getIndexOfGymM() para obter o indice do cliente).
     * @param cliente o objeto Cliente a ser removido
     * @return true se o cliente for removido com sucesso, false caso contrário
     */
    public static boolean delClient(Client cliente) {
        int index = getIndexOfGymM(cliente.getName());
        if (index == -1)
            return false;

        GymMembers.remove(cliente);
        numMembers--;
        return true;
    }

    /**
     * Remove um funcionario do ginásio (utiliza o metodo getIndexOfEmployee() para obter o indice do funcionario).
     * @param employee o objeto Employee a ser removido
     * @return true se o funcionario for removido com sucesso, false caso contrário
     */
    public static boolean delEmployee(Employee employee) {
        int index = getIndexOfEmployee(employee.getName());
        if (index == -1)
            return false;

        employees.remove(employee);
        numEmployees--;
        return true;
    }

    /**
     * Remove um equipamento do ginásio (utiliza o metodo getIndexOfMachines() para obter o indice do equipamento).
     * @param maquina o objeto Equipamento a ser removido
     * @return true se o equipamento for removido com sucesso, false caso contrário
     */
    public static boolean delMachine(Equipamento maquina) {
        int index = getIndexOfMachines(maquina.getNome());
        if (index == -1)
            return false;

        machines.remove(maquina);
        numMachines--;
        return true;
    }

    /**
     * Retorna o índice do cliente no ArrayList GymMembers com base no seu nome.
     * @param name nome a ser procurado
     * @return índice do cliente no ArrayList GymMembers, ou -1 se este não for encontrado
     */
    public static int getIndexOfGymM(String name)
    {
        for(int i=0; i<GymMembers.size(); i++)
        {
            if(GymMembers.get(i) != null && GymMembers.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }

    /**
     * Retorna o índice do funcionario no ArrayList employees com base no seu nome.
     * @param name nome a ser procurado
     * @return índice do funcionario no ArrayList employees, ou -1 se este não for encontrado
     */
    public static int getIndexOfEmployee(String name)
    {
        for(int i=0; i<employees.size(); i++)
        {
            if(employees.get(i) != null && employees.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }

    /**
     * Retorna o índice do equipamento no ArrayList machines com base no seu nome.
     * @param nome nome a ser procurado
     * @return índice do equipamento no ArrayList machines, ou -1 se este não for encontrado
     */
    public static int getIndexOfMachines(String nome)
    {
        for(int i=0; i<machines.size(); i++)
        {
            if(machines.get(i) != null && machines.get(i).getNome().equals(nome))
                return i;
        }
        return -1;
    }

    /**
     * Calcula as despesas totais do ginásio.
     * @return valor das despesas
     */
    public static double gymDespesas()
    {
        double despesas = 0;
        for (Employee employee : employees) {
            if (employee instanceof PersonalTrainer)
                despesas = despesas + ((PersonalTrainer) employee).uptadeSalario();
            else
                despesas = despesas + employee.getSalario();
        }
        for (Equipamento machine : machines) {
            if (machine.isPrecisaDeManutencao()) {
                despesas = despesas + machine.precoManutencao();
            } else if (machine.getDataDeCompra().getMonth() == LocalDate.now().getMonth() && machine.getDataDeCompra().getYear() == LocalDate.now().getYear()) {
                despesas = despesas + machine.getCusto();
            }
        }
        return despesas;
    }

    /**
     * Calcula as entradas totais do ginásio.
     * @return valor das entradas
     */
    public static double gymEntradas()
    {
        double valor = 0;

        for (Client gymMember : GymMembers) {
            valor = valor + gymMember.getPayment().setPrice();
        }
        return valor;
    }

    /**
     * Calcula o lucro do ginásio (entradas - despesas).
     * @return valor do lucro
     */
    public static double gymLucros()
    {
        return gymEntradas()-gymDespesas();
    }


    /**
     * Verifica se existe um user com o nome e password fornecidos.
     * @param name o nome do user
     * @param pass a senha do user
     * @return true se o user existe, false caso contrário
     */
    public static boolean checkUser(String name, String pass)
    {
        for (Client client : GymMembers)
        {
            if (client.login(name,pass))
                return true;
        }
        for (Employee employee : employees)
        {
            if (employee.login(name,pass))
                return true;
        }
        return false;
    }

    /**
     * Retorna um cliente ou funcionario, dependendo do nome e password fornecidos.
     * @param name o nome da pessoa
     * @param pass a password da pessoa
     * @return person desejada
     */
    public static Person getPerson(String name, String pass)
    {
        for (Client client : GymMembers)
        {
            if (client.login(name,pass))
                return client;
        }
        for (Employee employee : employees)
        {
            if (employee.login(name,pass))
                return employee;
        }
        return null;
    }

    /**
     * Cria um elemento XML com base nos dados do ginasio
     * Utiliza os createElement dos outros objetos (Client, Employee, PersonalTrainer, Equipamento) para criar os seus elementos.
     * @param doc O documento XML.
     * @return O elemento criado.
     */
    public static Element createElement(Document doc) {
        Element ginasio = doc.createElement("Ginasio");

        Element persons = doc.createElement("Persons");
        ginasio.appendChild(persons);

        Element clientes = doc.createElement("Clientes");
        persons.appendChild(clientes);

        for (int i = 0; i < numMembers; i++) {
            Element cliente = GymMembers.get(i).createElement(doc);
            clientes.appendChild(cliente);
        }

        Element employees = doc.createElement("Employees");
        persons.appendChild(employees);

        for (int i = 0; i < numEmployees; i++) {
            Element employee = Ginasio.employees.get(i).createElement(doc);
            employees.appendChild(employee);
        }

        Element machines = doc.createElement("Machines");
        ginasio.appendChild(machines);

        for (int i = 0; i < numMachines; i++) {
            Element machine = Ginasio.machines.get(i).createElement(doc);
            machines.appendChild(machine);
        }

        return ginasio;
    }


    /**
     * Salva as informações do ginásio (clientes, funcionários e equipamentos) em um arquivo XML.
     */
    public static void saveGymToXML() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Criação do documento XML
            Document doc = docBuilder.newDocument();

            // Criação do elemento root "Ginasio"
            Element ginasio = createElement(doc);
            doc.appendChild(ginasio);

            // Grava o documento XML em um arquivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            DOMSource source = new DOMSource(doc);

            // Especifica o caminho para o arquivo de saída
            String filePath = "tp4/GinasioBd.xml";
            StreamResult result = new StreamResult(new File(filePath));

            // Salva o documento XML no arquivo
            transformer.transform(source, result);

            System.out.println("Ginasio salvo no XML com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo principal da aplicação, abre e lê os dados do ficheiro XML
     * Criando os varios objetos e faz add destes aos seus respetivos ArrayList´s
     */
    public static void main(String[] args) {
            try {

                File inputFile = new File("tp4/Gymdados.xml");

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputFile);

                Element ginasioBD = (Element) doc.getElementsByTagName("Ginasio").item(0);
                Element persons = (Element) ginasioBD.getElementsByTagName("Persons").item(0);
                Element machines = (Element) ginasioBD.getElementsByTagName("Machines").item(0);

                List<Element> personElements = new ArrayList<>();
                NodeList Clients = persons.getElementsByTagName("Client");
                NodeList Pts = persons.getElementsByTagName("Instrutor");
                NodeList Employee = persons.getElementsByTagName("Employee");

                for (int i = 0; i < Clients.getLength(); i++) {
                    personElements.add((Element) Clients.item(i));
                }
                for (int i = 0; i < Pts.getLength(); i++) {
                    personElements.add((Element) Pts.item(i));
                }
                for (int i = 0; i < Employee.getLength(); i++) {
                    personElements.add((Element) Employee.item(i));
                }


                // Loop através dos elementos de usuário
                for (Element personElement : personElements) {
                    String p = personElement.getElementsByTagName("person").item(0).getTextContent();

                    // Constrói o usuário apropriado conforme a categoria
                    Person person;
                    switch (p) {
                        case "cliente":
                            person = Build_Classes.buildC(personElement);
                            addGymMember((Client) person);
                            break;
                        case "employee":
                            person = Build_Classes.buildE(personElement);
                            addEmployee((Employee) person);
                            break;
                        case "instrutor":
                            person = Build_Classes.buildPt(personElement);
                            addEmployee((PersonalTrainer) person);
                            break;
                        default:
                            break;
                    }

                }
                // Cria uma lista com os elementos equipamentos
                List<Element> machineElement = new ArrayList<>();
                NodeList machineList = machines.getElementsByTagName("Machine");
                for (int i = 0; i < machineList.getLength(); i++) {
                    machineElement.add((Element) machineList.item(i));
                }

                for (Element equipElement : machineElement) {
                    Equipamento equipamento;
                    equipamento = Build_Classes.buildM(equipElement);
                    addMachine(equipamento);
                }
                AppMenus.login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}