package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabio Pestana - A50756
 * ISEL - LEIM 22/23
 */

/**
 * Classe responsável pela interface de um ginasio (gestão).
 */
public class MenuInterface {
    public static JFrame frame = new JFrame("Ginasio A50756");
    public static JPanel menuGestor;
    public static JPanel panel;
    public static JPanel menuPt;
    public static JPanel menuAl;
    public static JPanel login;
    public static JPanel menuClient;
    public static Person person;
    public static Person auxPerson;
    public static boolean checkButton;

    /**
     * Metodo principal da aplicação, abre e lê os dados do ficheiro XML
     * Criando os varios objetos e faz add destes aos seus respetivos ArrayList´s na classe Ginasio
     */
    public static void main(String[] args) {

        try {
            String filePath = "Gymdados.xml";
            File inputFile = new File(filePath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            Element ginasioBD = (Element) doc.getElementsByTagName("Ginasio").item(0);
            Element persons = (Element) ginasioBD.getElementsByTagName("Persons").item(0);
            Element machines = (Element) ginasioBD.getElementsByTagName("Machines").item(0);

            java.util.List<Element> personElements = new ArrayList<>();
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
                        Ginasio.addGymMember((Client) person);
                        break;
                    case "employee":
                        person = Build_Classes.buildE(personElement);
                        Ginasio.addEmployee((Employee) person);
                        break;
                    case "instrutor":
                        person = Build_Classes.buildPt(personElement);
                        Ginasio.addEmployee((PersonalTrainer) person);
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
                Ginasio.addMachine(equipamento);
            }
            login = new Login();
            ImageIcon icone = new ImageIcon("Icon.png");
            frame.setIconImage(icone.getImage());
            login.setLayout(null);
            frame.add(login);
            frame.setSize(1024, 640);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Painel para efetuar o login.
     * Caso o user da app não tenha conta ele pode tornar-se membro do ginásio
     */
    public static class Login extends JPanel {

        JPasswordField password;
        JTextField name;
        JLabel lPass, lName;
        JCheckBox sPass;

        Login() {
            this.setLayout(null);

            person = null;

            lName = new JLabel("Name:");
            lName.setBounds(415, 265, 64, 25);
            lName.setFont(new Font("Open Sans", Font.BOLD, 16));
            lName.setForeground(Color.WHITE);

            name = new JTextField();
            name.setBounds(478, 259, 138, 38);
            name.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lPass = new JLabel("Password:");
            lPass.setBounds(385, 326, 102, 25);
            lPass.setFont(new Font("Open Sans", Font.BOLD, 16));
            lPass.setForeground(Color.WHITE);

            password = new JPasswordField();
            password.setBounds(478, 320, 138, 38);
            password.setFont(new Font("Open Sans", Font.PLAIN, 14));

            JButton botao = new JButton("Login");
            botao.setBounds(547, 370, 67, 20);
            botao.setFont(new Font("Open Sans", Font.BOLD, 12));
            botao.setForeground(Color.BLACK);
            botao.setBackground(Color.LIGHT_GRAY);

            sPass = new JCheckBox("Show");
            sPass.setBounds(478, 370, 60, 20);
            sPass.setFont(new Font("Open Sans", Font.ITALIC, 12));
            sPass.setForeground(Color.BLACK);
            sPass.setBackground(Color.LIGHT_GRAY);

            JButton botaoClient = new JButton("Torne-se cliente");
            botaoClient.setBounds(428, 552, 169, 25);
            botaoClient.setFont(new Font("Open Sans", Font.BOLD, 12));
            botaoClient.setForeground(Color.WHITE);
            botaoClient.setBackground(Color.BLUE);

            //Password visivel ou nao
            sPass.addActionListener(e ->
            {
                if (sPass.isSelected()) {
                    password.setEchoChar((char) 0);
                } else {
                    password.setEchoChar('*');
                }
            });

            botaoClient.addActionListener(e -> {
                frame.remove(login);
                panel = new bMember();
                frame.add(panel);
                frame.setVisible(true);
            });

            //Login ou erro
            botao.addActionListener(e -> {
                String n = name.getText();
                String p = String.valueOf(password.getPassword());
                if (Ginasio.checkUser(n, p)) {
                    person = Ginasio.getPerson(n, p);
                    decision(login,person);
                } else {
                    JOptionPane.showMessageDialog(this, "Dados incorretos");
                    name.setText("");
                    password.setText("");
                }
            });

            this.add(lName);
            this.add(name);
            this.add(lPass);
            this.add(password);
            this.add(botao);
            this.add(sPass);
            this.add(botaoClient);

            JLabel image = new JLabel(new ImageIcon("login.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }


    /**
     * Painel para se criar um cliente,
     */
    public static class bMember extends JPanel {

        public static JTextField name, password,idade,numero,genero, altura, CPack, peso, tda;
        public static JLabel lPass, lName, lIdade, lNumero, lGenero, lAltura, lCPack, lPeso, lTda;

        bMember() {
            this.setLayout(null);

            lName = new JLabel("Nome:");
            lName.setBounds(64, 167, 64, 25);
            lName.setFont(new Font("Open Sans", Font.BOLD, 16));
            lName.setForeground(Color.WHITE);

            name = new JTextField();
            name.setBounds(147, 162, 192, 35);
            name.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lIdade = new JLabel("Idade:");
            lIdade.setBounds(68, 251, 60, 25);
            lIdade.setFont(new Font("Open Sans", Font.BOLD, 16));
            lIdade.setForeground(Color.WHITE);

            idade = new JTextField();
            idade.setBounds(147, 245, 192, 35);
            idade.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lNumero = new JLabel("Numero:");
            lNumero.setBounds(45, 334, 88, 25);
            lNumero.setFont(new Font("Open Sans", Font.BOLD, 16));
            lNumero.setForeground(Color.WHITE);

            numero = new JTextField();
            numero.setBounds(147, 329, 192, 35);
            numero.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lPass = new JLabel("Password:");
            lPass.setBounds(30, 417, 102, 25);
            lPass.setFont(new Font("Open Sans", Font.BOLD, 16));
            lPass.setForeground(Color.WHITE);

            password = new JTextField();
            password.setBounds(147, 412, 192, 35);
            password.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lGenero = new JLabel("Genero:");
            lGenero.setBounds(50, 501, 78, 25);
            lGenero.setFont(new Font("Open Sans", Font.BOLD, 16));
            lGenero.setForeground(Color.WHITE);

            genero = new JTextField();
            genero.setBounds(147, 495, 192, 35);
            genero.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lPeso = new JLabel("Peso(kg):");
            lPeso.setBounds(572, 215, 95, 25);
            lPeso.setFont(new Font("Open Sans", Font.BOLD, 16));
            lPeso.setForeground(Color.WHITE);

            peso = new JTextField();
            peso.setBounds(680, 210, 192, 35);
            peso.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lAltura = new JLabel("Altura (cm):");
            lAltura.setBounds(554,299 , 114, 25);
            lAltura.setFont(new Font("Open Sans", Font.BOLD, 16));
            lAltura.setForeground(Color.WHITE);

            altura = new JTextField();
            altura.setBounds(680, 293, 192, 35);
            altura.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lCPack = new JLabel("Gym Pack:");
            lCPack.setBounds(559, 382, 105, 25);
            lCPack.setFont(new Font("Open Sans", Font.BOLD, 16));
            lCPack.setForeground(Color.WHITE);

            CPack = new JTextField();
            CPack.setBounds(680, 377, 192, 35);
            CPack.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lTda = new JLabel("Taxa de Atv. Fisica:");
            lTda.setBounds(495,453 , 195, 25);
            lTda.setFont(new Font("Open Sans", Font.BOLD, 16));
            lTda.setForeground(Color.WHITE);

            tda = new JTextField();
            tda.setBounds(680, 447, 192, 35);
            tda.setFont(new Font("Open Sans", Font.PLAIN, 14));

            JButton Cbotao = new JButton("Done");
            Cbotao.setBounds(464, 558, 96, 35);
            Cbotao.setFont(new Font("Open Sans", Font.BOLD, 12));
            Cbotao.setForeground(Color.BLACK);
            Cbotao.setBackground(Color.LIGHT_GRAY);

            JButton InfoButton = new JButton("Ler Info");
            InfoButton.setBounds(100, 558, 152, 35);
            InfoButton.setFont(new Font("Open Sans", Font.BOLD, 12));
            InfoButton.setForeground(Color.BLACK);
            InfoButton.setBackground(Color.LIGHT_GRAY);

            JButton Back = new JButton();
            if(person==null){
                Back.setText("Back to the Login");
            }else{
                Back.setText("Back to the Menu");
            }
            Back.setBounds(784, 558, 153, 35);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            Back.addActionListener(e -> {
                if (person instanceof Employee) {
                    frame.remove(panel);
                    panel = new AddMenu();
                    frame.add(panel);
                    frame.setVisible(true);
                }else
                    decision(panel, person);
            });

            //Login ou erro
            Cbotao.addActionListener(e -> {
                Client sub = createClient();;

                if (person==null){
                    person = sub;
                }else{
                    auxPerson = sub;
                }

                if (Ginasio.addGymMember(sub)) {
                    decision(panel,person);
                } else {
                    JOptionPane.showMessageDialog(this, "Ja existe uma conta com estes dados.");
                    name.setText("");
                    password.setText("");
                }
            });

            InfoButton.addActionListener(e -> {
                    JOptionPane.showMessageDialog(this, "Password (4 numeros, 4 letras minusculas e uma maiscula) \n" +
                            "Taxa de Atividade Fisica: 0 - Sedentario, 1 - Leve, 2 - Moderado, 3 - Intenso, 4 - Muito Intenso \n" +
                            "Pack do Ginasio: \n" +
                            "\t 1 - Acesso às instalações do ginásio e uma aula de introdução com um instrutor. Valor: 25 euros mensais \n" +
                            "\t 2 - Acesso às instalações do ginásio e a um instrutor que lhe dará treinos personalizados. Valor: 35 euros mensais.");
            });


            this.add(lName);
            this.add(name);
            this.add(lIdade);
            this.add(idade);
            this.add(lNumero);
            this.add(numero);
            this.add(lPass);
            this.add(password);
            this.add(lGenero);
            this.add(genero);
            this.add(lPeso);
            this.add(peso);
            this.add(lAltura);
            this.add(altura);
            this.add(lCPack);
            this.add(CPack);
            this.add(lTda);
            this.add(tda);
            this.add(Cbotao);
            this.add(InfoButton);
            this.add(Back);

            JLabel image = new JLabel(new ImageIcon("ClientRegistration.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Cria um novo objeto Client com base nas informações fornecidas pelo user no painel bMember.
     * @return Client criado
     */
    public static Client createClient()
    {

        String nome = bMember.name.getText();
        String num = bMember.numero.getText();
        String pass = bMember.password.getText();
        String genre = bMember.genero.getText();
        int age = Integer.parseInt(bMember.idade.getText());
        int weight = Integer.parseInt(bMember.peso.getText());
        int height = Integer.parseInt(bMember.altura.getText());
        int taf = Integer.parseInt(bMember.tda.getText());
        int clientPack = Integer.parseInt(bMember.CPack.getText());

        return new Client(nome, age, num, pass, genre, weight, height, clientPack, LocalDate.now(), taf);
    }

    /**
     * Método que recebe um painel e um objeto Person, é responsável por tomar decisões com base no tipo de person passada como argumento.
     * @param panel O painel atual a ser removido do frame.
     * @param person pessoa para quem se vai apresentar o menu.
     */
    public static void decision(JPanel panel, Person person)
    {
        if (person == null){
            frame.remove(panel);
            login = new Login();
            frame.add(login);
            frame.setVisible(true);
        } else if (person instanceof Client) {
            frame.remove(panel);
            menuClient = new ClientMenu();
            frame.add(menuClient);
            frame.setVisible(true);
        }
        else {
            Employee employee = (Employee) person;
            if (employee.getCargo().equals("Instrutor")) {
                frame.remove(panel);
                menuPt = new PtMenu();
                frame.add(menuPt);
                frame.setVisible(true);
            }else if (employee.getCargo().equals("Gestor")) {
                frame.remove(panel);
                menuGestor = new GestorMenu();
                frame.add(menuGestor);
                frame.setVisible(true);
            } else {
                frame.remove(panel);
                menuAl = new ALMenu();
                frame.add(menuAl);
                frame.setVisible(true);
            }
        }
    }

    /**
     * Método responsável por efetuar o logout do user.
     * @param panel O painel atual que será removido do frame.
     */
    public static void logOut(JPanel panel)
    {
        int r = JOptionPane.showConfirmDialog(null, "Antes de fazer Log out, deseja guardar as ações executadas no XML?", "Save XML", JOptionPane.YES_NO_CANCEL_OPTION);
        if (r==JOptionPane.YES_OPTION)
        {
            Ginasio.saveGymToXML();
            frame.remove(panel);
            login = new Login();
            frame.add(login);
            frame.setVisible(true);
        }else if (r==JOptionPane.NO_OPTION)
        {
            frame.remove(panel);
            login = new Login();
            frame.add(login);
            frame.setVisible(true);
        }
    }

    /**
     * Painel para o Menu do Cliente
     */
    public static class ClientMenu extends JPanel
    {
        Client client = (Client) person;
        public static JLabel lMenu;

        ClientMenu() {
            this.setLayout(null);

            lMenu = new JLabel("Menu Cliente");
            lMenu.setBounds(74, 104, 221, 39);
            lMenu.setFont(new Font("Open Sans", Font.BOLD, 30));
            lMenu.setForeground(Color.WHITE);

            JButton Perfil = new JButton("Ver Perfil");
            Perfil.setBounds(158, 165, 241, 39);
            Perfil.setFont(new Font("Open Sans", Font.BOLD, 12));
            Perfil.setForeground(Color.BLACK);
            Perfil.setBackground(Color.LIGHT_GRAY);

            JButton Niveis = new JButton("Niveis Calóricos");
            Niveis.setBounds(158, 265, 242, 39);
            Niveis.setFont(new Font("Open Sans", Font.BOLD, 12));
            Niveis.setForeground(Color.BLACK);
            Niveis.setBackground(Color.LIGHT_GRAY);

            JButton ShowE = new JButton("Ver Funcionários");
            ShowE.setBounds(158, 364, 241, 39);
            ShowE.setFont(new Font("Open Sans", Font.BOLD, 12));
            ShowE.setForeground(Color.BLACK);
            ShowE.setBackground(Color.LIGHT_GRAY);

            JButton ChoosePt = new JButton("Escolher PT");
            ChoosePt.setBounds(158, 464, 242, 39);
            ChoosePt.setFont(new Font("Open Sans", Font.BOLD, 12));
            ChoosePt.setForeground(Color.BLACK);
            ChoosePt.setBackground(Color.LIGHT_GRAY);

            JButton Logout = new JButton("Log out");
            Logout.setBounds(708, 165, 241, 39);
            Logout.setFont(new Font("Open Sans", Font.BOLD, 12));
            Logout.setForeground(Color.BLACK);
            Logout.setBackground(Color.LIGHT_GRAY);

            Logout.addActionListener(e -> {
                logOut(menuClient);
            });

            Perfil.addActionListener(e -> {
                frame.remove(menuClient);
                panel = new Perfil();
                frame.add(panel);
                frame.setVisible(true);
            });

            Niveis.addActionListener(e -> {
                frame.remove(menuClient);
                panel = new Calories();
                frame.add(panel);
                frame.setVisible(true);
            });

            ShowE.addActionListener(e -> {
                frame.remove(menuClient);
                panel = new VerEmployees();
                frame.add(panel);
                frame.setVisible(true);
                Ginasio.employeesTable();
            });

            ChoosePt.addActionListener(e -> {
                if (client.getPayment().getNumeroDoPack()==2){
                    frame.remove(menuClient);
                    panel = new ChoosePt();
                    frame.add(panel);
                    frame.setVisible(true);
                    Ginasio.ptTable();
                }else
                {
                    JOptionPane.showMessageDialog(this, "Opção inválida para o seu pack do ginsásio \n" +
                            "Para aceder a esta opção faça upgrade para o pacote 2.");
                }
            });

            this.add(lMenu);
            this.add(Perfil);
            this.add(Niveis);
            this.add(ShowE);
            this.add(ChoosePt);
            this.add(Logout);

            JLabel image = new JLabel(new ImageIcon("Menu.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel para o Perfil do Cliente
     * Caso o user estiver a ver o seu Perfil, ele pode alterar grande parte dos seus dados.
     * Se o user for um Funcionario do Ginásio ele não consegue alterar os dados do cliente nem ver a sua password.
     */
    public static class Perfil extends JPanel {
        public static JLabel lPass, lName, lIdade, lNumero, lGenero, lAltura, lCPack, lPeso, lTda, lData;

        Client client;
        Perfil() {
            this.setLayout(null);

            if (person instanceof Employee ){
                client = (Client) auxPerson;
            }else
                client = (Client) person;

            lName = new JLabel("Nome: " + client.getName());
            lName.setBounds(64, 167, 200, 25);
            lName.setFont(new Font("Open Sans", Font.BOLD, 16));
            lName.setForeground(Color.WHITE);

            lIdade = new JLabel("Idade: " + client.getAge() + " anos");
            lIdade.setBounds(64, 251, 200, 25);
            lIdade.setFont(new Font("Open Sans", Font.BOLD, 16));
            lIdade.setForeground(Color.WHITE);

            lNumero = new JLabel("Numero: " + client.getNumber());
            lNumero.setBounds(64, 334, 200, 25);
            lNumero.setFont(new Font("Open Sans", Font.BOLD, 16));
            lNumero.setForeground(Color.WHITE);

            lPass = new JLabel("Password: " + client.getPassword());
            lPass.setBounds(64, 417, 300, 25);
            lPass.setFont(new Font("Open Sans", Font.BOLD, 16));
            lPass.setForeground(Color.WHITE);

            lGenero = new JLabel("Genero: " + client.getGenero());
            lGenero.setBounds(64, 501, 200, 25);
            lGenero.setFont(new Font("Open Sans", Font.BOLD, 16));
            lGenero.setForeground(Color.WHITE);

            lPeso = new JLabel("Peso(kg): " + client.getPeso() + " kg");
            lPeso.setBounds(572, 167, 200, 25);
            lPeso.setFont(new Font("Open Sans", Font.BOLD, 16));
            lPeso.setForeground(Color.WHITE);

            lAltura = new JLabel("Altura (cm): " + client.getAltura() + " cm");
            lAltura.setBounds(572, 251, 200, 25);
            lAltura.setFont(new Font("Open Sans", Font.BOLD, 16));
            lAltura.setForeground(Color.WHITE);

            lCPack = new JLabel("Gym Pack: " + client.getPayment().getNumeroDoPack());
            lCPack.setBounds(572, 334, 200, 25);
            lCPack.setFont(new Font("Open Sans", Font.BOLD, 16));
            lCPack.setForeground(Color.WHITE);


            lTda = new JLabel("Taxa de Atv. Fisica: " + client.getTaxaDeAtivFisica());
            lTda.setBounds(572, 417, 300, 25);
            lTda.setFont(new Font("Open Sans", Font.BOLD, 16));
            lTda.setForeground(Color.WHITE);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            lData = new JLabel("Data de Inscrição: "+ client.getDataInscricao().format(formatter));
            lData.setBounds(572, 501, 300, 25);
            lData.setFont(new Font("Open Sans", Font.BOLD, 16));
            lData.setForeground(Color.WHITE);

            JButton goBack = new JButton("Back to the Menu");
            goBack.setBounds(832, 557, 153, 39);
            goBack.setFont(new Font("Open Sans", Font.BOLD, 12));
            goBack.setForeground(Color.BLACK);
            goBack.setBackground(Color.LIGHT_GRAY);

            JButton changeInfo = new JButton("Change Info");
            changeInfo.setBounds(423, 557, 173, 39);
            changeInfo.setFont(new Font("Open Sans", Font.BOLD, 12));
            changeInfo.setForeground(Color.BLACK);
            changeInfo.setBackground(Color.LIGHT_GRAY);

            goBack.addActionListener(e -> {
                decision(panel,person);
            });

            changeInfo.addActionListener(e -> {
                JDialog.setDefaultLookAndFeelDecorated(true);
                Object[] selectionValues = {"Password","Idade","Numero","Altura","Peso","Taxa de Atv.Fisica", "Numero do Pack"};
                String inicial = "Password";
                JFrame frame1 =new JFrame();
                Object selection = JOptionPane.showInputDialog(null, "Que dado deseja alterar?",
                        "Change Info", JOptionPane.QUESTION_MESSAGE, null, selectionValues, inicial);
                String nD0 = JOptionPane.showInputDialog(frame1, "Insira os novos dados:");
                if ((selection).equals("Numero") && Person.isNumberValid(nD0)) {
                    client.setNumber(nD0);
                }else if (isStringNumber(nD0)) {
                    int nD1 = Integer.parseInt(nD0);
                    if ((selection).equals("Idade") && nD1 >16) {
                        client.setIdade(nD1);
                    }else if ((selection).equals("Peso")&& nD1 >0){
                        client.setPeso(nD1);
                    }
                    else if ((selection).equals("Taxa de Atv.Fisica") && nD1<=4 && nD1>=0){
                        client.setTaxaDeAtivFisica(nD1);
                    }
                    else if ((selection).equals("Numero do Pack")&& nD1<3 && nD1>0){
                        client.getPayment().setNumeroDoPack(nD1);
                    }
                    else if ((selection).equals("Altura")&& nD1 >0){
                        client.setAltura(nD1);
                    }else {
                        JOptionPane.showMessageDialog(this, "Dados inválidos");
                    }
                }else {
                    if ((selection).equals("Password") && Person.isPasswordValid(nD0)) {
                        client.setPassword(nD0);
                    } else {
                        JOptionPane.showMessageDialog(this, "Dados inválidos");
                    }
                }
                frame.remove(panel);
                panel = new Perfil();
                frame.add(panel);
                frame.setVisible(true);
            });

            this.add(lName);
            this.add(lIdade);
            this.add(lNumero);
            this.add(lGenero);
            this.add(lPeso);
            this.add(lAltura);
            this.add(lCPack);
            this.add(lTda);
            this.add(lData);
            this.add(goBack);

            if (person instanceof Client)
                this.add(changeInfo);
            else
                lPass.setText("Password: só visivel para o cliente");
            this.add(lPass);

            JLabel image = new JLabel(new ImageIcon("Perfil.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Método que vê se uma String é um numero.
     * @param s String a testar.
     * @return true se for um numero, ou false se nao for
     */
    public static boolean isStringNumber(String s)
    {
        for (int i=0; i<s.length(); i++)
        {
            if (!Character.isDigit(s.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Painel com os niveis calóricos do Cliente
     */
    public static class Calories extends JPanel
    {
        public static JLabel lCalories, lMantain, lGain, lDeficit;

        Client client = (Client) person;

        Calories() {
            this.setLayout(null);

            lCalories = new JLabel("Menu Calórico");
            lCalories.setBounds(31, 35, 350, 57);
            lCalories.setFont(new Font("Open Sans", Font.BOLD, 36));
            lCalories.setForeground(Color.WHITE);

            lMantain = new JLabel("Manter Peso: " + client.caloriesCalculator());
            lMantain.setBounds(140, 181, 240, 39);
            lMantain.setFont(new Font("Open Sans", Font.BOLD, 25));
            lMantain.setForeground(Color.WHITE);

            lGain = new JLabel("Perde Peso: " + client.getCaloricDeficit());
            lGain.setBounds(140, 307, 240, 39);
            lGain.setFont(new Font("Open Sans", Font.BOLD, 25));
            lGain.setForeground(Color.WHITE);

            lDeficit = new JLabel("Ganhar Peso: " + client.getGainWeight());
            lDeficit.setBounds(140, 434, 240, 39);
            lDeficit.setFont(new Font("Open Sans", Font.BOLD, 25));
            lDeficit.setForeground(Color.WHITE);


            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(708, 165, 241, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            Back.addActionListener(e -> {
                frame.remove(panel);
                menuClient = new ClientMenu();
                frame.add(menuClient);
                frame.setVisible(true);
            });

            this.add(lCalories);
            this.add(lMantain);
            this.add(lGain);
            this.add(lDeficit);
            this.add(Back);

            JLabel image = new JLabel(new ImageIcon("Base_Desing.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel com uma tabela com todos os funcionários do Ginásio, qualquer pessoa pode visualizar o perfil destes funcionários,
     * No entanto, só os gestores é que tem uma opcção de remover funcionários.
     */
    public static class VerEmployees extends JPanel
    {
        public static JTable tabelaF;
        public static DefaultTableModel tableModel;

        VerEmployees() {
            this.setLayout(null);

            JLabel title = new JLabel("Funcionários");
            title.setBounds(31, 35, 350, 57);
            title.setFont(new Font("Open Sans", Font.BOLD, 36));
            title.setForeground(Color.WHITE);

            String[] colunas = {"Nome","Género", "Idade", "Número", "Cargo", "Data de Contratação"};
            tableModel = new DefaultTableModel(colunas, 0);

            tabelaF = new JTable(tableModel);

            JTableHeader tableHeader = tabelaF.getTableHeader();
            tableHeader.setBackground(Color.darkGray);
            tableHeader.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(tabelaF);
            scrollPane.setBounds(64, 145, 890, 335);

            JLabel text = new JLabel("Nome do Funcionário:");
            text.setFont(new Font("Open Sans", Font.BOLD, 15));
            text.setForeground(Color.WHITE);

            JTextField nome = new JTextField();
            nome.setFont(new Font("Open Sans", Font.PLAIN, 14));

            JButton showP = new JButton("Show");
            showP.setFont(new Font("Open Sans", Font.BOLD, 12));
            showP.setForeground(Color.BLACK);
            showP.setBackground(Color.LIGHT_GRAY);

            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(824, 528, 153, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            Back.addActionListener(e -> {
                decision(panel, person);
            });

            showP.addActionListener(e -> {
                String em = nome.getText();
                int ind = Ginasio.getIndexOfEmployee(em);
                if (ind>=0)
                {
                    auxPerson = Ginasio.employees.get(ind);

                    frame.remove(panel);
                    panel = new PerfilEmployee();
                    frame.add(panel);
                    frame.setVisible(true);
                }
                else{
                    JOptionPane.showMessageDialog(this, "Funcionário não existe");
                    nome.setText("");
                }
            });

            if (person instanceof Employee employee){
                if (employee.getCargo().equalsIgnoreCase("Gestor"))
                {
                    JButton delete = new JButton("Remove");
                    delete.setFont(new Font("Open Sans", Font.BOLD, 12));
                    delete.setForeground(Color.BLACK);
                    delete.setBackground(Color.LIGHT_GRAY);
                    delete.addActionListener(e -> {
                        String em = nome.getText();
                        int ind = Ginasio.getIndexOfEmployee(em);
                        if (ind>=0)
                        {
                            Ginasio.delEmployee(Ginasio.employees.get(ind));

                            frame.remove(panel);
                            panel = new VerEmployees();
                            frame.add(panel);
                            frame.setVisible(true);
                            Ginasio.employeesTable();

                        }
                        else{
                            JOptionPane.showMessageDialog(this, "Funcionário não existe");
                            nome.setText("");
                        }
                    });
                    delete.setBounds(603, 528, 93, 39);
                    text.setBounds(185, 535, 209, 24);
                    nome.setBounds(360, 528, 153, 39);
                    showP.setBounds(523, 528, 73, 39);
                    this.add(delete);
                }else{
                    text.setBounds(220, 535, 209, 24);
                    nome.setBounds(404, 528, 153, 39);
                    showP.setBounds(573, 528, 73, 39);
                }
            } else {
                text.setBounds(220, 535, 209, 24);
                nome.setBounds(404, 528, 153, 39);
                showP.setBounds(573, 528, 73, 39);
            }

            this.add(title);
            this.add(Back);
            this.add(scrollPane);
            this.add(showP);
            this.add(nome);
            this.add(text);

            JLabel image = new JLabel(new ImageIcon("BaseDesing2.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel com uma tabela com todos os instrutores do Ginásio.
     * Neste painel os clientes podem escolher um Pt para os treinar.
     */
    public static class ChoosePt extends JPanel
    {
        public static JTable tabelaF;
        public static DefaultTableModel tableModel;
        Client client = (Client) person;
        ChoosePt() {
            this.setLayout(null);

            JLabel title = new JLabel("Escolher Personal Trainer");
            title.setBounds(31, 35, 550, 57);
            title.setFont(new Font("Open Sans", Font.BOLD, 36));
            title.setForeground(Color.WHITE);

            String[] colunas = {"Nome","Género", "Idade", "Número", "Experiência", "Data de Contratação", "Numero de Clientes "};
            tableModel = new DefaultTableModel(colunas, 0);

            tabelaF = new JTable(tableModel);

            JTableHeader tableHeader = tabelaF.getTableHeader();
            tableHeader.setBackground(Color.darkGray);
            tableHeader.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(tabelaF);
            scrollPane.setBounds(64, 145, 890, 335);

            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(824, 528, 153, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            JLabel text = new JLabel("Escolha um Instrutor:");
            text.setBounds(230, 535, 209, 24);
            text.setFont(new Font("Open Sans", Font.BOLD, 15));
            text.setForeground(Color.WHITE);

            JTextField nome = new JTextField();
            nome.setBounds(404, 528, 153, 39);
            nome.setFont(new Font("Open Sans", Font.PLAIN, 14));

            JButton add = new JButton("Add");
            add.setBounds(573, 528, 73, 39);
            add.setFont(new Font("Open Sans", Font.BOLD, 12));
            add.setForeground(Color.BLACK);
            add.setBackground(Color.LIGHT_GRAY);

            Back.addActionListener(e -> {
                frame.remove(panel);
                menuClient = new ClientMenu();
                frame.add(menuClient);
                frame.setVisible(true);
            });

            add.addActionListener(e -> {
                String pt = nome.getText();
                int ind = Ginasio.getIndexOfEmployee(pt);
                if (ind>=0)
                {
                    PersonalTrainer employee = (PersonalTrainer) Ginasio.employees.get(ind);
                    if (employee.addClient(client.getName())){
                        JOptionPane.showMessageDialog(this, "Adicionado com sucesso");
                        Ginasio.ptTable();}
                    else
                        JOptionPane.showMessageDialog(this, "Já existe");
                }
                else{
                    JOptionPane.showMessageDialog(this, "Instrutor não existe");
                    nome.setText("");
                }
            });

            this.add(title);
            this.add(text);
            this.add(nome);
            this.add(Back);
            this.add(add);
            this.add(scrollPane);

            JLabel image = new JLabel(new ImageIcon("BaseDesing2.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel onde se visualiza o perfil de um Funcionário,
     * sendo que os gestores podem alterar os dados dos funcionários (não podem atualizar o seu próprio contrato)
     */
    public static class PerfilEmployee extends JPanel {
        public static JLabel lName, lIdade, lGenero, lNumero, lSalario, lExp, lYears, lDataB, lDataE;
        public static JLabel lCargo;
        Employee employee = (Employee) auxPerson;
        PerfilEmployee() {
            this.setLayout(null);

            lName = new JLabel("Nome: " + employee.getName());
            lName.setBounds(64, 167, 200, 25);
            lName.setFont(new Font("Open Sans", Font.BOLD, 16));
            lName.setForeground(Color.WHITE);

            lIdade = new JLabel("Idade: " + employee.getAge() + " anos");
            lIdade.setBounds(64, 251, 200, 25);
            lIdade.setFont(new Font("Open Sans", Font.BOLD, 16));
            lIdade.setForeground(Color.WHITE);

            lNumero = new JLabel("Numero: " + employee.getNumber());
            lNumero.setBounds(64, 334, 200, 25);
            lNumero.setFont(new Font("Open Sans", Font.BOLD, 16));
            lNumero.setForeground(Color.WHITE);

            lGenero = new JLabel("Genero: " + employee.getGenero());
            lGenero.setBounds(64, 417, 200, 25);
            lGenero.setFont(new Font("Open Sans", Font.BOLD, 16));
            lGenero.setForeground(Color.WHITE);
            if (employee instanceof PersonalTrainer pt)
            {
                lSalario = new JLabel("Salario: " + String.format("%.2f", pt.uptadeSalario()));
                lSalario.setBounds(64, 501, 200, 25);
                lSalario.setFont(new Font("Open Sans", Font.BOLD, 16));
                lSalario.setForeground(Color.WHITE);

                lExp = new JLabel("Experiencia: " + pt.getExperiencia());
                lExp.setBounds(572, 334, 200, 25);
                lExp.setFont(new Font("Open Sans", Font.BOLD, 16));
                lExp.setForeground(Color.WHITE);

                JButton VerClientes = new JButton("PT clientes");
                VerClientes.setBounds(425, 557, 173, 39);
                if (person instanceof Employee && ((Employee)person).getCargo().equalsIgnoreCase("Gestor"))
                    VerClientes.setBounds(549, 557, 173, 39);
                VerClientes.setFont(new Font("Open Sans", Font.BOLD, 12));
                VerClientes.setForeground(Color.BLACK);
                VerClientes.setBackground(Color.LIGHT_GRAY);

                VerClientes.addActionListener(e -> {
                    frame.remove(panel);
                    panel = new TabelaPtClientes();
                    frame.add(panel);
                    frame.setVisible(true);
                    Ginasio.tabela(pt.getName());
                });

                this.add(VerClientes);
                this.add(lExp);

            }else {
                lSalario = new JLabel("Salario: " + String.format("%.2f", employee.getSalario()));
                lSalario.setBounds(64, 501, 200, 25);
                lSalario.setFont(new Font("Open Sans", Font.BOLD, 16));
                lSalario.setForeground(Color.WHITE);

                lCargo = new JLabel("Cargo: " + employee.getCargo());
                lCargo.setBounds(572, 334, 250, 25);
                lCargo.setFont(new Font("Open Sans", Font.BOLD, 16));
                lCargo.setForeground(Color.WHITE);

                this.add(lCargo);
            }

            lYears = new JLabel("Anos de Contrato: " + employee.getYearsOfContract() + " anos");
            lYears.setBounds(572, 167, 200, 25);
            lYears.setFont(new Font("Open Sans", Font.BOLD, 16));
            lYears.setForeground(Color.WHITE);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            lDataB = new JLabel("Data de Contratação: "+ employee.getDataContratacao().format(formatter));
            lDataB.setBounds(572, 251, 300, 25);
            lDataB.setFont(new Font("Open Sans", Font.BOLD, 16));
            lDataB.setForeground(Color.WHITE);

            lDataE = new JLabel("Fim de Contrato: "+ employee.endContract().format(formatter));
            lDataE.setBounds(572, 417, 300, 25);
            lDataE.setFont(new Font("Open Sans", Font.BOLD, 16));
            lDataE.setForeground(Color.WHITE);

            JButton goBack = new JButton("Back to the Menu");
            goBack.setBounds(832, 557, 153, 39);
            goBack.setFont(new Font("Open Sans", Font.BOLD, 12));
            goBack.setForeground(Color.BLACK);
            goBack.setBackground(Color.LIGHT_GRAY);

            goBack.addActionListener(e -> {
                decision(panel,person);
            });


            if (person instanceof Employee)
            {
                if (((Employee)person).getCargo().equalsIgnoreCase("Gestor"))
                {
                    JButton changeInfo = new JButton("Change Info");
                    if (auxPerson instanceof PersonalTrainer && !(person instanceof PersonalTrainer)) {
                        changeInfo.setBounds(270, 557, 173, 39);
                    }else {
                        changeInfo.setBounds(423, 557, 173, 39);
                    }
                    changeInfo.setFont(new Font("Open Sans", Font.BOLD, 12));
                    changeInfo.setForeground(Color.BLACK);
                    changeInfo.setBackground(Color.LIGHT_GRAY);

                    changeInfo.addActionListener(e -> {
                        JDialog.setDefaultLookAndFeelDecorated(true);
                        Object[] selectionValues = {"Password", "Idade", "Numero", "Novo Contrato"};
                        String inicial = "Password";
                        JFrame frame1 = new JFrame();
                        Object selection = JOptionPane.showInputDialog(null, "Que dado deseja alterar?",
                                "Change Info", JOptionPane.QUESTION_MESSAGE, null, selectionValues, inicial);
                        String nD0 = "contrato";
                        if (!(selection).equals("Novo Contrato"))
                            nD0 = JOptionPane.showInputDialog(frame1, "Insira os novos dados:");
                        else if (!person.getName().equalsIgnoreCase(auxPerson.getName())) {
                            employee.newContract();
                        }else {
                            JOptionPane.showMessageDialog(this, "Não tem autorização para alterar o seu contrato.");
                        }

                        if ((selection).equals("Numero") && Person.isNumberValid(nD0)) {
                            employee.setNumber(nD0);
                        } else if (isStringNumber(nD0))
                        {
                            int nD1 = Integer.parseInt(nD0);
                            if ((selection).equals("Idade") && nD1 > 16) {
                                employee.setIdade(nD1);
                            } else{
                                JOptionPane.showMessageDialog(this, "Dados inválidos");
                            }
                        } else {
                            if ((selection).equals("Password") && Person.isPasswordValid(nD0)) {
                                employee.setPassword(nD0);
                            } else if (!(selection).equals("Novo Contrato")){
                                JOptionPane.showMessageDialog(this, "Dados inválidos.");
                            }
                        }
                        frame.remove(panel);
                        panel = new PerfilEmployee();
                        frame.add(panel);
                        frame.setVisible(true);
                    });
                    this.add(changeInfo);
                }
            }

            this.add(lName);
            this.add(lIdade);
            this.add(lNumero);
            this.add(lGenero);
            this.add(lSalario);
            this.add(lYears);
            this.add(lDataB);
            this.add(lDataE);
            this.add(goBack);

            JLabel image = new JLabel(new ImageIcon("Perfil.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel com uma tabela com todos os clientes de um Instrutor.
     * Neste painel o Instrutor pode remover os seus clientes e o Gestor pode remover qualquer cliente de qualquer Pt.
     */
    public static class TabelaPtClientes extends JPanel
    {
        public static JTable tabelaF;
        public static DefaultTableModel tableModel;
        public static JLabel title;
        TabelaPtClientes() {
            this.setLayout(null);
            PersonalTrainer pt;
            if (person instanceof Client)
            {
                pt = (PersonalTrainer) auxPerson;
            }else if ((person instanceof PersonalTrainer && auxPerson instanceof PersonalTrainer) || ((Employee)person).getCargo().equalsIgnoreCase("Gestor"))
            {
                pt = (PersonalTrainer) auxPerson;
            }else if (person instanceof PersonalTrainer){
                pt = (PersonalTrainer) person;
            }else
                pt = (PersonalTrainer) auxPerson;
            title = new JLabel("Clientes do " + pt.getName());
            title.setBounds(31, 35, 550, 57);
            title.setFont(new Font("Open Sans", Font.BOLD, 36));
            title.setForeground(Color.WHITE);

            String[] colunas = {"Nome", "Idade", "Numero", "Genero"};

            tableModel = new DefaultTableModel(colunas, 0);

            tabelaF = new JTable(tableModel);

            JTableHeader tableHeader = tabelaF.getTableHeader();
            tableHeader.setBackground(Color.darkGray);
            tableHeader.setForeground(Color.WHITE);

            JLabel text = new JLabel("Nome do Cliente:");
            text.setBounds(240, 535, 209, 24);
            text.setFont(new Font("Open Sans", Font.BOLD, 15));
            text.setForeground(Color.WHITE);

            JTextField nome = new JTextField();
            nome.setBounds(404, 528, 153, 39);
            nome.setFont(new Font("Open Sans", Font.PLAIN, 14));

            JScrollPane scrollPane = new JScrollPane(tabelaF);
            scrollPane.setBounds(64, 145, 890, 335);

            JButton Back = new JButton("Back to the perfil");
            Back.setBounds(824, 528, 153, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);


            Back.addActionListener(e -> {
                if (person instanceof PersonalTrainer && !(auxPerson instanceof PersonalTrainer)){
                    frame.remove(panel);
                    panel = new perfilEditE();
                    frame.add(panel);
                    frame.setVisible(true);
                }else{
                    frame.remove(panel);
                    panel = new PerfilEmployee();
                    frame.add(panel);
                    frame.setVisible(true);
                }
            });

            if (person instanceof Employee employee)
            {
                if ((employee.getCargo().equalsIgnoreCase("Gestor") || person instanceof PersonalTrainer))
                {
                    if (!(person instanceof PersonalTrainer && auxPerson instanceof PersonalTrainer)) //Um pt nao pode remover clientes de outro
                    {
                        JButton delete = new JButton("Remove");
                        delete.setFont(new Font("Open Sans", Font.BOLD, 12));
                        delete.setBounds(573, 528, 90, 39);
                        delete.setForeground(Color.BLACK);
                        delete.setBackground(Color.LIGHT_GRAY);
                        delete.addActionListener(e -> {
                            String em = nome.getText();
                            int ind = pt.getIndexOfGymM(em);
                            if (ind>=0 )
                            {
                                pt.delClient(pt.ptClients.get(ind));
                                frame.remove(panel);
                                panel = new TabelaPtClientes();
                                frame.add(panel);
                                frame.setVisible(true);
                                Ginasio.tabela(pt.getName());
                            }
                            else {
                                JOptionPane.showMessageDialog(this, "Cliente não existe.");
                                nome.setText("");
                            }
                        });
                        this.add(delete);
                        this.add(nome);
                        this.add(text);
                    }
                }
            }

            this.add(title);
            this.add(Back);
            this.add(scrollPane);

            JLabel image = new JLabel(new ImageIcon("BaseDesing2.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel para o Menu do Gestor
     */
    public static class GestorMenu extends JPanel
    {
        public static JLabel lMenu;
        GestorMenu() {
            this.setLayout(null);

            lMenu = new JLabel("Menu Gestor");
            lMenu.setBounds(74, 104, 221, 39);
            lMenu.setFont(new Font("Open Sans", Font.BOLD, 30));
            lMenu.setForeground(Color.WHITE);

            JButton Perfil = new JButton("Ver Perfil");
            Perfil.setBounds(158, 165, 241, 39);
            Perfil.setFont(new Font("Open Sans", Font.BOLD, 12));
            Perfil.setForeground(Color.BLACK);
            Perfil.setBackground(Color.LIGHT_GRAY);

            JButton Colegas = new JButton("Ver colegas");
            Colegas.setBounds(158, 245, 242, 39);
            Colegas.setFont(new Font("Open Sans", Font.BOLD, 12));
            Colegas.setForeground(Color.BLACK);
            Colegas.setBackground(Color.LIGHT_GRAY);

            JButton showC = new JButton("Ver Clientes");
            showC.setBounds(158, 324, 241, 39);
            showC.setFont(new Font("Open Sans", Font.BOLD, 12));
            showC.setForeground(Color.BLACK);
            showC.setBackground(Color.LIGHT_GRAY);

            JButton showE = new JButton("Ver Máquinas");
            showE.setBounds(158, 406, 241, 39);
            showE.setFont(new Font("Open Sans", Font.BOLD, 12));
            showE.setForeground(Color.BLACK);
            showE.setBackground(Color.LIGHT_GRAY);

            JButton addPM = new JButton("Add People/Machines");
            addPM.setBounds(158, 487, 242, 39);
            addPM.setFont(new Font("Open Sans", Font.BOLD, 12));
            addPM.setForeground(Color.BLACK);
            addPM.setBackground(Color.LIGHT_GRAY);

            JButton financas = new JButton("Finanças do Ginásio");
            financas.setBounds(708, 245, 241, 39);
            financas.setFont(new Font("Open Sans", Font.BOLD, 12));
            financas.setForeground(Color.BLACK);
            financas.setBackground(Color.LIGHT_GRAY);

            JButton Logout = new JButton("Log out");
            Logout.setBounds(708, 165, 241, 39);
            Logout.setFont(new Font("Open Sans", Font.BOLD, 12));
            Logout.setForeground(Color.BLACK);
            Logout.setBackground(Color.LIGHT_GRAY);

            Perfil.addActionListener(e -> {
                frame.remove(menuGestor);
                panel = new perfilEditE();
                frame.add(panel);
                frame.setVisible(true);
            });

            Colegas.addActionListener(e -> {
                frame.remove(menuGestor);
                panel = new VerEmployees();
                frame.add(panel);
                frame.setVisible(true);
                Ginasio.employeesTable();
            });

            showC.addActionListener(e -> {
                frame.remove(menuGestor);
                panel = new tabelaClientes();
                frame.add(panel);
                frame.setVisible(true);
                Ginasio.clientTable();
            });

            showE.addActionListener(e -> {
                frame.remove(menuGestor);
                panel = new tabelaEquipamento();
                frame.add(panel);
                frame.setVisible(true);
                Ginasio.equipamentoTable();
            });

            financas.addActionListener(e -> {
                frame.remove(menuGestor);
                panel = new Financas();
                frame.add(panel);
                frame.setVisible(true);
            });

            addPM.addActionListener(e -> {
                frame.remove(menuGestor);
                panel = new AddMenu();
                frame.add(panel);
                frame.setVisible(true);
            });

            Logout.addActionListener(e -> {
                logOut(menuGestor);
            });

            this.add(lMenu);
            this.add(Perfil);
            this.add(Colegas);
            this.add(showC);
            this.add(addPM);
            this.add(showE);
            this.add(financas);
            this.add(Logout);

            JLabel image = new JLabel(new ImageIcon("Menu.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel com o Perfil do Funcionário, onde este pode alterar alguns dos seus dados.
     */
    public static class perfilEditE extends JPanel {
        perfilEditE() {
            this.setLayout(null);
            auxPerson=null;
            Employee employee = (Employee) person;

            JLabel lName = new JLabel("Nome: " + employee.getName());
            lName.setBounds(64, 167, 200, 25);
            lName.setFont(new Font("Open Sans", Font.BOLD, 16));
            lName.setForeground(Color.WHITE);

            JLabel lIdade = new JLabel("Idade: " + employee.getAge() + " anos");
            lIdade.setBounds(64, 251, 200, 25);
            lIdade.setFont(new Font("Open Sans", Font.BOLD, 16));
            lIdade.setForeground(Color.WHITE);

            JLabel lNumero = new JLabel("Numero: " + employee.getNumber());
            lNumero.setBounds(64, 334, 200, 25);
            lNumero.setFont(new Font("Open Sans", Font.BOLD, 16));
            lNumero.setForeground(Color.WHITE);

            JLabel lGenero = new JLabel("Genero: " + employee.getGenero());
            lGenero.setBounds(64, 417, 200, 25);
            lGenero.setFont(new Font("Open Sans", Font.BOLD, 16));
            lGenero.setForeground(Color.WHITE);
            if (employee instanceof PersonalTrainer pt)
            {
                JLabel lSalario = new JLabel("Salario: " + String.format("%.2f", pt.uptadeSalario()));
                lSalario.setBounds(64, 501, 200, 25);
                lSalario.setFont(new Font("Open Sans", Font.BOLD, 16));
                lSalario.setForeground(Color.WHITE);

                JLabel lExp = new JLabel("Experiencia: " + pt.getExperiencia());
                lExp.setBounds(572, 334, 200, 25);
                lExp.setFont(new Font("Open Sans", Font.BOLD, 16));
                lExp.setForeground(Color.WHITE);

                JButton VerClientes = new JButton("PT clientes");
                VerClientes.setBounds(549, 557, 173, 39);
                VerClientes.setFont(new Font("Open Sans", Font.BOLD, 12));
                VerClientes.setForeground(Color.BLACK);
                VerClientes.setBackground(Color.LIGHT_GRAY);

                VerClientes.addActionListener(e -> {
                    frame.remove(panel);
                    panel = new TabelaPtClientes();
                    frame.add(panel);
                    frame.setVisible(true);
                    Ginasio.tabela(pt.getName());
                });
                this.add(VerClientes);
                this.add(lExp);

            }else {
                JLabel lSalario = new JLabel("Salario: " + String.format("%.2f", employee.getSalario()));
                lSalario.setBounds(64, 501, 200, 25);
                lSalario.setFont(new Font("Open Sans", Font.BOLD, 16));
                lSalario.setForeground(Color.WHITE);

                JLabel lCargo = new JLabel("Cargo: " + employee.getCargo());
                lCargo.setBounds(572, 334, 250, 25);
                lCargo.setFont(new Font("Open Sans", Font.BOLD, 16));
                lCargo.setForeground(Color.WHITE);

                this.add(lCargo);
                this.add(lSalario);
            }

            JLabel lYears = new JLabel("Anos de Contrato: " + employee.getYearsOfContract() + " anos");
            lYears.setBounds(572, 167, 200, 25);
            lYears.setFont(new Font("Open Sans", Font.BOLD, 16));
            lYears.setForeground(Color.WHITE);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            JLabel lDataB = new JLabel("Data de Contratação: "+ employee.getDataContratacao().format(formatter));
            lDataB.setBounds(572, 251, 300, 25);
            lDataB.setFont(new Font("Open Sans", Font.BOLD, 16));
            lDataB.setForeground(Color.WHITE);

            JLabel lDataE = new JLabel("Fim de Contrato: "+ employee.endContract().format(formatter));
            lDataE.setBounds(572, 417, 300, 25);
            lDataE.setFont(new Font("Open Sans", Font.BOLD, 16));
            lDataE.setForeground(Color.WHITE);

            JButton goBack = new JButton("Back to the Menu");
            goBack.setBounds(832, 557, 153, 39);
            goBack.setFont(new Font("Open Sans", Font.BOLD, 12));
            goBack.setForeground(Color.BLACK);
            goBack.setBackground(Color.LIGHT_GRAY);

            goBack.addActionListener(e -> {
                decision(panel,person);
            });
            JButton changeInfo = new JButton("Change Info");
            if (person instanceof PersonalTrainer)
                changeInfo.setBounds(270, 557, 173, 39);
            else
                changeInfo.setBounds(423, 557, 173, 39);
            changeInfo.setFont(new Font("Open Sans", Font.BOLD, 12));
            changeInfo.setForeground(Color.BLACK);
            changeInfo.setBackground(Color.LIGHT_GRAY);

            changeInfo.addActionListener(e -> {
                JDialog.setDefaultLookAndFeelDecorated(true);
                Object[] selectionValues = {"Password", "Idade", "Numero"};
                String inicial = "Password";
                JFrame frame1 = new JFrame();
                Object selection = JOptionPane.showInputDialog(null, "Que dado deseja alterar?",
                            "Change Info", JOptionPane.QUESTION_MESSAGE, null, selectionValues, inicial);
                String nD0 = JOptionPane.showInputDialog(frame1, "Insira os novos dados:");
                if ((selection).equals("Numero") && Person.isNumberValid(nD0)) {
                    employee.setNumber(nD0);
                } else if (isStringNumber(nD0))
                {
                    int nD1 = Integer.parseInt(nD0);
                    if ((selection).equals("Idade") && nD1 > 16) {
                        employee.setIdade(nD1);
                    } else{
                        JOptionPane.showMessageDialog(this, "Dados inválidos");
                    }
                } else {
                    if ((selection).equals("Password") && Person.isPasswordValid(nD0)) {
                        employee.setPassword(nD0);
                    } else {
                        JOptionPane.showMessageDialog(this, "Dados inválidos");
                    }
                }
                frame.remove(panel);
                panel = new perfilEditE();
                frame.add(panel);
                frame.setVisible(true);
            });

            this.add(changeInfo);
            this.add(lName);
            this.add(lIdade);
            this.add(lNumero);
            this.add(lGenero);
            this.add(lYears);
            this.add(lDataB);
            this.add(lDataE);
            this.add(goBack);

            JLabel image = new JLabel(new ImageIcon("Perfil.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel com uma tabela com todos os clientes do Ginasio, todos os users com acesso a este painel podem aceder aos perfis dos clientes.
     * Caso o user seja um Gestor, ele tem a opcao de remover clientes.
     */
    public static class tabelaClientes extends JPanel
    {
        public static JTable tabelaF;
        public static DefaultTableModel tableModel;
        public static JLabel title;


        tabelaClientes() {
            this.setLayout(null);

            Employee employee = (Employee) person;

            title = new JLabel("Clientes do Ginásio");
            title.setBounds(31, 35, 550, 57);
            title.setFont(new Font("Open Sans", Font.BOLD, 36));
            title.setForeground(Color.WHITE);

            String[] colunas = {"Nome", "Idade", "Numero", "Genero", "Peso", "Altura", "Inscrição", "Expiração pack", "Gym Pack", "Despesas"};

            tableModel = new DefaultTableModel(colunas, 0);

            tabelaF = new JTable(tableModel);

            JTableHeader tableHeader = tabelaF.getTableHeader();
            tableHeader.setBackground(Color.darkGray);
            tableHeader.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(tabelaF);
            scrollPane.setBounds(64, 145, 890, 335);

            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(824, 528, 153, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            JLabel text = new JLabel("Nome do Cliente:");
            text.setFont(new Font("Open Sans", Font.BOLD, 15));
            text.setForeground(Color.WHITE);

            JTextField nome = new JTextField();
            nome.setFont(new Font("Open Sans", Font.PLAIN, 14));

            JButton showP = new JButton("Show");
            showP.setFont(new Font("Open Sans", Font.BOLD, 12));
            showP.setForeground(Color.BLACK);
            showP.setBackground(Color.LIGHT_GRAY);
            showP.addActionListener(e -> {
                String em = nome.getText();
                int ind = Ginasio.getIndexOfGymM(em);
                if (ind>=0)
                {
                    auxPerson = Ginasio.GymMembers.get(ind);

                    frame.remove(panel);
                    panel = new Perfil();
                    frame.add(panel);
                    frame.setVisible(true);

                }
                else{
                    JOptionPane.showMessageDialog(this, "Cliente não existe");
                    nome.setText("");
                }
            });

            Back.addActionListener(e -> {
                decision(panel,person);
            });

            if (employee.getCargo().equalsIgnoreCase("Gestor")){
                JButton delete = new JButton("Remove");
                delete.setFont(new Font("Open Sans", Font.BOLD, 12));
                delete.setForeground(Color.BLACK);
                delete.setBackground(Color.LIGHT_GRAY);
                delete.addActionListener(e -> {
                    String em = nome.getText();
                    int ind = Ginasio.getIndexOfGymM(em);
                    if (ind>=0)
                    {
                        Ginasio.delClient(Ginasio.GymMembers.get(ind));

                        frame.remove(panel);
                        panel = new tabelaClientes();
                        frame.add(panel);
                        frame.setVisible(true);
                        Ginasio.clientTable();

                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Cliente não existe");
                        nome.setText("");
                    }
                });
                delete.setBounds(603, 528, 93, 39);
                text.setBounds(220, 535, 209, 24);
                nome.setBounds(360, 528, 153, 39);
                showP.setBounds(523, 528, 73, 39);
                this.add(delete);
            }else {
                text.setBounds(250, 535, 209, 24);
                nome.setBounds(404, 528, 153, 39);
                showP.setBounds(573, 528, 73, 39);
            }

            this.add(title);
            this.add(Back);
            this.add(nome);
            this.add(text);
            this.add(showP);
            this.add(scrollPane);

            JLabel image = new JLabel(new ImageIcon("BaseDesing2.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel onde o Gestor pode visualizar e eliminar equipamentos do Ginasio.
     */
    public static class tabelaEquipamento extends JPanel
    {
        public static JTable tabelaF;
        public static DefaultTableModel tableModel;
        public static JLabel title;


        tabelaEquipamento() {
            this.setLayout(null);

            title = new JLabel("Equipamento do Ginásio");
            title.setBounds(31, 35, 550, 57);
            title.setFont(new Font("Open Sans", Font.BOLD, 36));
            title.setForeground(Color.WHITE);

            String[] colunas = {"Nome", "Data de Compra", "Manutenção", "Custo", "Precisa de Manutencao", "Preço Manutenção"};

            tableModel = new DefaultTableModel(colunas, 0);

            tabelaF = new JTable(tableModel);

            JTableHeader tableHeader = tabelaF.getTableHeader();
            tableHeader.setBackground(Color.darkGray);
            tableHeader.setForeground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(tabelaF);
            scrollPane.setBounds(64, 145, 890, 335);

            JLabel text = new JLabel("Nome do Equipamento:");
            text.setBounds(210, 535, 209, 24);
            text.setFont(new Font("Open Sans", Font.BOLD, 15));
            text.setForeground(Color.WHITE);

            JTextField nome = new JTextField();
            nome.setBounds(404, 528, 153, 39);
            nome.setFont(new Font("Open Sans", Font.PLAIN, 14));

            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(824, 528, 153, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            JButton delete = new JButton("Remove");
            delete.setFont(new Font("Open Sans", Font.BOLD, 12));
            delete.setBounds(573, 528, 90, 39);
            delete.setForeground(Color.BLACK);
            delete.setBackground(Color.LIGHT_GRAY);

            Back.addActionListener(e -> {
                decision(panel,person);
            });

            delete.addActionListener(e -> {
                String em = nome.getText();
                int ind = Ginasio.getIndexOfMachines(em);
                if (ind>=0)
                {
                    Ginasio.delMachine(Ginasio.machines.get(ind));

                    frame.remove(panel);
                    panel = new tabelaEquipamento();
                    frame.add(panel);
                    frame.setVisible(true);
                    Ginasio.equipamentoTable();
                }else{
                    JOptionPane.showMessageDialog(this, "Equipamento não existe");
                    nome.setText("");
                }
            });

            this.add(title);
            this.add(text);
            this.add(nome);
            this.add(Back);
            this.add(delete);
            this.add(scrollPane);

            JLabel image = new JLabel(new ImageIcon("BaseDesing2.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel onde o Gestor pode visualizar as financas do Ginasio.
     */
    public static class Financas extends JPanel
    {
        Financas() {
            this.setLayout(null);

            JLabel lT = new JLabel("Finanças do Ginásio");
            lT.setBounds(31, 35, 450, 57);
            lT.setFont(new Font("Open Sans", Font.BOLD, 36));
            lT.setForeground(Color.WHITE);

            JLabel lLucos = new JLabel("Lucros: " + String.format("%.2f", Ginasio.gymLucros()) + " euros");
            lLucos.setBounds(140, 181, 400, 39);
            lLucos.setFont(new Font("Open Sans", Font.BOLD, 25));
            lLucos.setForeground(Color.WHITE);

            JLabel lEntradas = new JLabel("Entradas: " + String.format("%.2f", Ginasio.gymEntradas()) + " euros");
            lEntradas.setBounds(140, 307, 400, 39);
            lEntradas.setFont(new Font("Open Sans", Font.BOLD, 25));
            lEntradas.setForeground(Color.WHITE);

            JLabel lDespesas = new JLabel("Despesas: " + String.format("%.2f", Ginasio.gymDespesas())  + " euros");
            lDespesas.setBounds(140, 434, 400, 39);
            lDespesas.setFont(new Font("Open Sans", Font.BOLD, 25));
            lDespesas.setForeground(Color.WHITE);

            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(708, 165, 240, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            Back.addActionListener(e -> {
                decision(panel, person);
            });

            this.add(lT);
            this.add(lLucos);
            this.add(lEntradas);
            this.add(lDespesas);
            this.add(Back);

            JLabel image = new JLabel(new ImageIcon("Base_Desing.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel onde o Gestor pode escolher registrar pessoas e equipamentos.
     */
    public static class AddMenu extends JPanel
    {
        AddMenu() {
            this.setLayout(null);

            JLabel lT = new JLabel("Add Menu");
            lT.setBounds(31, 35, 350, 57);
            lT.setFont(new Font("Open Sans", Font.BOLD, 36));
            lT.setForeground(Color.WHITE);

            JButton cClients = new JButton("Criar e addicionar clientes");
            cClients.setBounds(140, 165, 240, 39);
            cClients.setFont(new Font("Open Sans", Font.BOLD, 12));
            cClients.setForeground(Color.BLACK);
            cClients.setBackground(Color.LIGHT_GRAY);

            JButton cEmployees = new JButton("Criar e addicionar funcionários");
            cEmployees.setBounds(140, 265, 240, 39);
            cEmployees.setFont(new Font("Open Sans", Font.BOLD, 12));
            cEmployees.setForeground(Color.BLACK);
            cEmployees.setBackground(Color.LIGHT_GRAY);

            JButton cPT = new JButton("Criar e addicionar instrutores");
            cPT.setBounds(140, 365, 240, 39);
            cPT.setFont(new Font("Open Sans", Font.BOLD, 12));
            cPT.setForeground(Color.BLACK);
            cPT.setBackground(Color.LIGHT_GRAY);

            JButton cEquip = new JButton("Criar e addicionar maquinas");
            cEquip.setBounds(140, 465, 240, 39);
            cEquip.setFont(new Font("Open Sans", Font.BOLD, 12));
            cEquip.setForeground(Color.BLACK);
            cEquip.setBackground(Color.LIGHT_GRAY);

            JButton addCtoPT = new JButton("Adicionar Clientes a Instrutores");
            addCtoPT.setBounds(708, 265, 240, 39);
            addCtoPT.setFont(new Font("Open Sans", Font.BOLD, 12));
            addCtoPT.setForeground(Color.BLACK);
            addCtoPT.setBackground(Color.LIGHT_GRAY);

            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(708, 165, 241, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            Back.addActionListener(e -> {
                decision(panel, person);
            });

            cClients.addActionListener(e -> {
                frame.remove(panel);
                panel =new bMember();
                frame.add(panel);
                frame.setVisible(true);
            });

            cEmployees.addActionListener(e -> {
                checkButton=true;
                frame.remove(panel);
                panel =new employRegis();
                frame.add(panel);
                frame.setVisible(true);
            });

            cPT.addActionListener(e -> {
                checkButton=false;
                frame.remove(panel);
                panel =new employRegis();
                frame.add(panel);
                frame.setVisible(true);
            });

            cEquip.addActionListener(e -> {
                frame.remove(panel);
                panel =new equipRegis();
                frame.add(panel);
                frame.setVisible(true);
            });

            addCtoPT.addActionListener(e -> {
                frame.remove(panel);
                panel =new addClientToPt();
                frame.add(panel);
                frame.setVisible(true);
            });


            this.add(cClients);
            this.add(cEmployees);
            this.add(cPT);
            this.add(cEquip);
            this.add(addCtoPT);
            this.add(lT);
            this.add(Back);

            JLabel image = new JLabel(new ImageIcon("Base_Desing.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel para se registrar e criar um Employee ou PersonalTrainer (dependendo do estado do checkButton).
     */
    public static class employRegis extends JPanel {
        public static JTextField name, password,idade,numero,genero, cargo, data, dC;
        public static JLabel lPass, lName, lIdade, lNumero, lGenero, lCargo, lData, lDC;

        employRegis() {
            this.setLayout(null);

            lName = new JLabel("Nome:");
            lName.setBounds(64, 167, 64, 25);
            lName.setFont(new Font("Open Sans", Font.BOLD, 16));
            lName.setForeground(Color.WHITE);

            name = new JTextField();
            name.setBounds(147, 162, 192, 35);
            name.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lIdade = new JLabel("Idade:");
            lIdade.setBounds(68, 251, 60, 25);
            lIdade.setFont(new Font("Open Sans", Font.BOLD, 16));
            lIdade.setForeground(Color.WHITE);

            idade = new JTextField();
            idade.setBounds(147, 245, 192, 35);
            idade.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lNumero = new JLabel("Numero:");
            lNumero.setBounds(50, 334, 88, 25);
            lNumero.setFont(new Font("Open Sans", Font.BOLD, 16));
            lNumero.setForeground(Color.WHITE);

            numero = new JTextField();
            numero.setBounds(147, 329, 192, 35);
            numero.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lPass = new JLabel("Password:");
            lPass.setBounds(36, 417, 102, 25);
            lPass.setFont(new Font("Open Sans", Font.BOLD, 16));
            lPass.setForeground(Color.WHITE);

            password = new JTextField();
            password.setBounds(147, 412, 192, 35);
            password.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lGenero = new JLabel("Genero:");
            lGenero.setBounds(50, 501, 78, 25);
            lGenero.setFont(new Font("Open Sans", Font.BOLD, 16));
            lGenero.setForeground(Color.WHITE);

            genero = new JTextField();
            genero.setBounds(147, 495, 192, 35);
            genero.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lDC = new JLabel("Duraçao do Contrato:");
            lDC.setBounds(480, 215, 180, 25);
            lDC.setFont(new Font("Open Sans", Font.BOLD, 16));
            lDC.setForeground(Color.WHITE);

            dC = new JTextField();
            dC.setBounds(680, 210, 192, 35);
            dC.setFont(new Font("Open Sans", Font.PLAIN, 14));

            if (checkButton){
                lCargo = new JLabel("Cargo:");
                lCargo.setBounds(600, 382, 95, 25);
                lCargo.setFont(new Font("Open Sans", Font.BOLD, 16));
                lCargo.setForeground(Color.WHITE);

                cargo = new JTextField();
                cargo.setBounds(680, 377, 192, 35);
                cargo.setFont(new Font("Open Sans", Font.PLAIN, 14));

                lData = new JLabel("Salario:");
                lData.setBounds(590,299 , 114, 25);
                lData.setFont(new Font("Open Sans", Font.BOLD, 16));
                lData.setForeground(Color.WHITE);

                data = new JTextField();
                data.setBounds(680, 293, 192, 35);
                data.setFont(new Font("Open Sans", Font.PLAIN, 14));

                this.add(lCargo);
                this.add(cargo);
            } else{
                lData = new JLabel("Anos de Experiência:");
                lData.setBounds(485,299 , 180, 25);
                lData.setFont(new Font("Open Sans", Font.BOLD, 16));
                lData.setForeground(Color.WHITE);

                data = new JTextField();
                data.setBounds(680, 293, 192, 35);
                data.setFont(new Font("Open Sans", Font.PLAIN, 14));

            }

            JButton Cbotao = new JButton("Done");
            Cbotao.setBounds(464, 558, 96, 35);
            Cbotao.setFont(new Font("Open Sans", Font.BOLD, 12));
            Cbotao.setForeground(Color.BLACK);
            Cbotao.setBackground(Color.LIGHT_GRAY);


            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(784, 558, 153, 35);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            Back.addActionListener(e -> {
                checkButton=false;
                frame.remove(panel);
                panel =new AddMenu();
                frame.add(panel);
                frame.setVisible(true);
            });

            //Login ou erro
            Cbotao.addActionListener(e -> {
                Employee sub = createEmployee();
                if (Ginasio.addEmployee(sub)) {
                    decision(panel,person);
                } else {
                    JOptionPane.showMessageDialog(this, "Ja existe um funcionário com estes dados.");
                    name.setText("");
                    password.setText("");
                }
            });

            this.add(lName);
            this.add(name);
            this.add(lIdade);
            this.add(idade);
            this.add(lNumero);
            this.add(numero);
            this.add(lPass);
            this.add(password);
            this.add(lGenero);
            this.add(genero);
            this.add(lData);
            this.add(data);
            this.add(lDC);
            this.add(dC);
            this.add(Cbotao);
            this.add(Back);

            JLabel image = new JLabel(new ImageIcon("EmployeeRegistration.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Cria um novo objeto Employee ou PersonalTrainer dependendo do valor do checkButton.
     * @return Employee ou PersonalTrainer criado
     */
    public static Employee createEmployee()
    {
        String nome = employRegis.name.getText();
        String num = employRegis.numero.getText();
        String pass = employRegis.password.getText();
        String genre = employRegis.genero.getText();
        int age = Integer.parseInt(employRegis.idade.getText());
        int anosdeC = Integer.parseInt(employRegis.dC.getText());
        int data = Integer.parseInt(employRegis.data.getText());
        if (checkButton) {
            String cargo = employRegis.cargo.getText();
            return new Employee(nome, age, num, pass, genre, cargo, data, LocalDate.now(), anosdeC);
        }else{
            return new PersonalTrainer(nome,age,num,pass,genre,LocalDate.now(),anosdeC,data);
        }
    }

    /**
     * Painel para se registrar e criar um equipamento.
     */
    public static class equipRegis extends JPanel {
        public static JTextField name, custo;
        public static JLabel lName, lCusto;

        equipRegis() {
            this.setLayout(null);

            lName = new JLabel("Nome:");
            lName.setBounds(100, 165, 64, 25);
            lName.setFont(new Font("Open Sans", Font.BOLD, 16));
            lName.setForeground(Color.WHITE);

            name = new JTextField();
            name.setBounds(196, 165, 192, 35);
            name.setFont(new Font("Open Sans", Font.PLAIN, 14));

            lCusto = new JLabel("Custo:");
            lCusto.setBounds(100, 329, 60, 25);
            lCusto.setFont(new Font("Open Sans", Font.BOLD, 16));
            lCusto.setForeground(Color.WHITE);

            custo = new JTextField();
            custo.setBounds(196, 320, 192, 35);
            custo.setFont(new Font("Open Sans", Font.PLAIN, 14));

            JButton Cbotao = new JButton("Done");
            Cbotao.setBounds(754, 329, 150, 35);
            Cbotao.setFont(new Font("Open Sans", Font.BOLD, 12));
            Cbotao.setForeground(Color.BLACK);
            Cbotao.setBackground(Color.LIGHT_GRAY);

            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(754, 165, 150, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            Back.addActionListener(e -> {
                frame.remove(panel);
                panel =new AddMenu();
                frame.add(panel);
                frame.setVisible(true);
            });


            Cbotao.addActionListener(e -> {
                Equipamento sub = createEquip();
                if (Ginasio.addMachine(sub)) {
                    decision(panel,person);
                } else {
                    JOptionPane.showMessageDialog(this, "Ja existe um equipamento com este nome.");
                    name.setText("");
                    custo.setText("");
                }
            });

            this.add(lName);
            this.add(name);
            this.add(lCusto);
            this.add(custo);
            this.add(Cbotao);
            this.add(Back);

            JLabel image = new JLabel(new ImageIcon("EquipRegistration.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Cria um novo objeto Equipamento com base nas informações fornecidas pelo user.
     * @return Equipamento criado
     */
    public static Equipamento createEquip()
    {
        String nome = equipRegis.name.getText();
        int preco = Integer.parseInt(equipRegis.custo.getText());
        return new Equipamento(nome, LocalDate.now(), preco);
    }

    /**
     * Painel onde o Pt pode adicionar um cliente, para o treinar, ou onde o Gestor pode adicionar um Cliente a um Pt.
     */
    public static class addClientToPt extends JPanel {
        public static JTextField nameC, namePT;
        public static JLabel lNameC, lNamePT;

        addClientToPt() {
            this.setLayout(null);

            JButton Cbotao = new JButton("Done");
            Cbotao.setBounds(754, 329, 150, 35);
            Cbotao.setFont(new Font("Open Sans", Font.BOLD, 12));
            Cbotao.setForeground(Color.BLACK);
            Cbotao.setBackground(Color.LIGHT_GRAY);

            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(754, 165, 150, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);


            lNameC = new JLabel("Cliente:");
            lNameC.setBounds(100, 165, 64, 25);
            lNameC.setFont(new Font("Open Sans", Font.BOLD, 16));
            lNameC.setForeground(Color.WHITE);

            nameC = new JTextField();
            nameC.setBounds(196, 165, 192, 35);
            nameC.setFont(new Font("Open Sans", Font.PLAIN, 14));



            if (person instanceof PersonalTrainer)
            {
                lNameC.setText("Insira o nome do cliente:");
                lNameC.setBounds(70, 168, 300, 25);
                nameC.setBounds(280, 165, 192, 35);
                Cbotao.setBounds(754, 300, 150, 35);

            }else
            {
                lNamePT = new JLabel("Instrutor:");
                lNamePT.setBounds(80, 329, 100, 25);
                lNamePT.setFont(new Font("Open Sans", Font.BOLD, 16));
                lNamePT.setForeground(Color.WHITE);

                namePT = new JTextField();
                namePT.setBounds(196, 329, 192, 35);
                namePT.setFont(new Font("Open Sans", Font.PLAIN, 14));
                this.add(namePT);
                this.add(lNamePT);
            }

            Back.addActionListener(e -> {
                if (person instanceof PersonalTrainer)
                {
                    decision(panel, person);
                }else {
                    frame.remove(panel);
                    panel =new AddMenu();
                    frame.add(panel);
                    frame.setVisible(true);
                }
            });


            Cbotao.addActionListener(e -> {
                String nC = nameC.getText();
                String nPt;
                if (person instanceof PersonalTrainer)
                    nPt = person.getName();
                else
                    nPt = namePT.getText();
                int i = Ginasio.getIndexOfGymM(nC);
                int j = Ginasio.getIndexOfEmployee(nPt);
                if (i>=0 && j>=0) {
                    if (Ginasio.employees.get(j) instanceof PersonalTrainer pt)
                    {
                        if (pt.addClient(Ginasio.GymMembers.get(i).getName())) {
                            JOptionPane.showMessageDialog(this, "Adição feita com sucesso.");
                            if (person instanceof PersonalTrainer)
                                nameC.setText("");
                            else {
                                nameC.setText("");
                                namePT.setText("");
                            }
                        }else
                            JOptionPane.showMessageDialog(this, "Cliente já é aluno do Pt ou tem pack 1.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Nome do cliente ou/e instrutor inválido(s)");
                    if (person instanceof PersonalTrainer)
                        nameC.setText("");
                    else {
                        nameC.setText("");
                        namePT.setText("");
                    }
                }
            });

            this.add(lNameC);
            this.add(nameC);
            this.add(Cbotao);
            this.add(Back);

            JLabel image = new JLabel(new ImageIcon("addCToPt.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel para o Menu do Personal Trainer
     */
    public static class PtMenu extends JPanel
    {
        public static JLabel lMenu;

        PtMenu() {
            this.setLayout(null);

            lMenu = new JLabel("Menu Personal Trainer");
            lMenu.setBounds(74, 104, 350, 39);
            lMenu.setFont(new Font("Open Sans", Font.BOLD, 30));
            lMenu.setForeground(Color.WHITE);

            JButton Perfil = new JButton("Ver Perfil");
            Perfil.setBounds(158, 165, 241, 39);
            Perfil.setFont(new Font("Open Sans", Font.BOLD, 12));
            Perfil.setForeground(Color.BLACK);
            Perfil.setBackground(Color.LIGHT_GRAY);

            JButton Colegas = new JButton("Ver colegas");
            Colegas.setBounds(158, 245, 242, 39);
            Colegas.setFont(new Font("Open Sans", Font.BOLD, 12));
            Colegas.setForeground(Color.BLACK);
            Colegas.setBackground(Color.LIGHT_GRAY);

            JButton showC = new JButton("Ver Clientes do Ginasio");
            showC.setBounds(158, 324, 241, 39);
            showC.setFont(new Font("Open Sans", Font.BOLD, 12));
            showC.setForeground(Color.BLACK);
            showC.setBackground(Color.LIGHT_GRAY);

            JButton addC = new JButton("Adicionar clientes");
            addC.setBounds(158, 406, 241, 39);
            addC.setFont(new Font("Open Sans", Font.BOLD, 12));
            addC.setForeground(Color.BLACK);
            addC.setBackground(Color.LIGHT_GRAY);

            JButton showB = new JButton("Ver Bónus");
            showB.setBounds(158, 487, 242, 39);
            showB.setFont(new Font("Open Sans", Font.BOLD, 12));
            showB.setForeground(Color.BLACK);
            showB.setBackground(Color.LIGHT_GRAY);

            JButton Logout = new JButton("Log out");
            Logout.setBounds(708, 165, 241, 39);
            Logout.setFont(new Font("Open Sans", Font.BOLD, 12));
            Logout.setForeground(Color.BLACK);
            Logout.setBackground(Color.LIGHT_GRAY);

            Perfil.addActionListener(e -> {
                frame.remove(menuPt);
                panel = new perfilEditE();
                frame.add(panel);
                frame.setVisible(true);
            });

            Colegas.addActionListener(e -> {
                frame.remove(menuPt);
                panel = new VerEmployees();
                frame.add(panel);
                frame.setVisible(true);
                Ginasio.employeesTable();
            });

            showC.addActionListener(e -> {
                frame.remove(menuPt);
                panel = new tabelaClientes();
                frame.add(panel);
                frame.setVisible(true);
                Ginasio.clientTable();
            });

            addC.addActionListener(e -> {
                frame.remove(menuPt);
                panel =new addClientToPt();
                frame.add(panel);
                frame.setVisible(true);
            });

            showB.addActionListener(e -> {
                frame.remove(menuPt);
                panel = new bonus();
                frame.add(panel);
                frame.setVisible(true);
            });

            Logout.addActionListener(e -> {
                logOut(menuPt);
            });

            this.add(lMenu);
            this.add(Perfil);
            this.add(Colegas);
            this.add(showC);
            this.add(addC);
            this.add(showB);
            this.add(Logout);

            JLabel image = new JLabel(new ImageIcon("Menu.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel onde o Pt pode visualizar o seu salário e seu Bonus (caso tenha pelo menos 8 clientes)
     */
    public static class bonus extends JPanel
    {
        bonus() {
            this.setLayout(null);
            PersonalTrainer pt = (PersonalTrainer) person;

            JLabel lT = new JLabel("Salário");
            lT.setBounds(31, 35, 350, 57);
            lT.setFont(new Font("Open Sans", Font.BOLD, 36));
            lT.setForeground(Color.WHITE);

            JLabel lsalario = new JLabel("Salario (normal): " + String.format("%.2f", pt.getSalario()) + " euros");
            lsalario.setBounds(140, 181, 400, 39);
            lsalario.setFont(new Font("Open Sans", Font.BOLD, 25));
            lsalario.setForeground(Color.WHITE);

            JLabel lbonus = new JLabel("Bónus: " + String.format("%.2f", pt.Bonus()) + " euros");
            lbonus.setBounds(140, 307, 400, 39);
            lbonus.setFont(new Font("Open Sans", Font.BOLD, 25));
            lbonus.setForeground(Color.WHITE);

            JLabel lfinal = new JLabel("Salario final: " + String.format("%.2f", pt.uptadeSalario()) + " euros");
            lfinal.setBounds(140, 434, 400, 39);
            lfinal.setFont(new Font("Open Sans", Font.BOLD, 25));
            lfinal.setForeground(Color.WHITE);

            JButton Back = new JButton("Back to the Menu");
            Back.setBounds(708, 165, 241, 39);
            Back.setFont(new Font("Open Sans", Font.BOLD, 12));
            Back.setForeground(Color.BLACK);
            Back.setBackground(Color.LIGHT_GRAY);

            Back.addActionListener(e -> {
                decision(panel, person);
            });

            this.add(lT);
            this.add(lsalario);
            this.add(lbonus);
            this.add(lfinal);
            this.add(Back);

            JLabel image = new JLabel(new ImageIcon("Base_Desing.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

    /**
     * Painel para o Menu do Auxiliar de Limpeza
     */
    public static class ALMenu extends JPanel
    {
        public static JLabel lMenu;

        ALMenu() {
            this.setLayout(null);

            lMenu = new JLabel("Menu Auxiliar de limpeza");
            lMenu.setBounds(74, 104, 400, 39);
            lMenu.setFont(new Font("Open Sans", Font.BOLD, 30));
            lMenu.setForeground(Color.WHITE);

            JButton Perfil = new JButton("Ver Perfil");
            Perfil.setBounds(158, 165, 241, 39);
            Perfil.setFont(new Font("Open Sans", Font.BOLD, 12));
            Perfil.setForeground(Color.BLACK);
            Perfil.setBackground(Color.LIGHT_GRAY);

            JButton Colegas = new JButton("Ver colegas");
            Colegas.setBounds(158, 245, 242, 39);
            Colegas.setFont(new Font("Open Sans", Font.BOLD, 12));
            Colegas.setForeground(Color.BLACK);
            Colegas.setBackground(Color.LIGHT_GRAY);

            JButton showC = new JButton("Ver Clientes do Ginasio");
            showC.setBounds(158, 324, 241, 39);
            showC.setFont(new Font("Open Sans", Font.BOLD, 12));
            showC.setForeground(Color.BLACK);
            showC.setBackground(Color.LIGHT_GRAY);

            JButton Logout = new JButton("Log out");
            Logout.setBounds(708, 165, 241, 39);
            Logout.setFont(new Font("Open Sans", Font.BOLD, 12));
            Logout.setForeground(Color.BLACK);
            Logout.setBackground(Color.LIGHT_GRAY);

            Perfil.addActionListener(e -> {
                frame.remove(menuAl);
                panel = new perfilEditE();
                frame.add(panel);
                frame.setVisible(true);
            });

            Colegas.addActionListener(e -> {
                frame.remove(menuAl);
                panel = new VerEmployees();
                frame.add(panel);
                frame.setVisible(true);
                Ginasio.employeesTable();
            });

            showC.addActionListener(e -> {
                frame.remove(menuAl);
                panel = new tabelaClientes();
                frame.add(panel);
                frame.setVisible(true);
                Ginasio.clientTable();
            });

            Logout.addActionListener(e -> {
                logOut(menuAl);
            });

            this.add(lMenu);
            this.add(Perfil);
            this.add(Colegas);
            this.add(showC);
            this.add(Logout);

            JLabel image = new JLabel(new ImageIcon("Menu.png"));
            image.setBounds(0, 0, 1024, 640);
            this.add(image);

            this.setVisible(true);
        }
    }

}