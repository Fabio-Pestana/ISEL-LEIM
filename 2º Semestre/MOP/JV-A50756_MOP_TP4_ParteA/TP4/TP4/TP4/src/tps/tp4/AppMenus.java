package tps.tp4;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Fabio Pestana - A50756
 * ISEL - LEIM 22/23
 */

/**
 * Classe responsável pelos Menus de uma gestao de um ginasio.
 */
public class AppMenus {

    /**
     * Cria e retorna um Scanner para entrada de dados do sistema.
     * @return Scanner
     */
    public static Scanner scanner() {return new Scanner(System.in);}

    /**
     * Realiza o processo de login e mostra as opções disponíveis para o user.
     */
    public static void login() {
        Scanner scanner = scanner();
        System.out.println("\t Bem vindo ao Ginásio!");
        System.out.println("[1] -> Para iniciar sessão");
        System.out.println("[2] -> Para criar uma conta cliente");
        System.out.println("[3] -> Fechar app");
        int opcao = scanner.nextInt();
        System.out.println("----------------------------------------");

        switch (opcao) {
            case 1:
                Scanner scanner2 = scanner();
                System.out.println("Name: ");
                String name = scanner2.nextLine();
                System.out.println("Password:");
                String password = scanner2.nextLine();
                if(Ginasio.checkUser(name, password))
                {
                    Person person = Ginasio.getPerson(name,password);
                    decision(person);
                }else {
                    System.out.println("Dados incorretos");
                    login();
                }
                break;

            case 2:
                Client newClient = createClient();
                Ginasio.addGymMember(newClient);
                decision(newClient);
                break;
            case 3:
                System.out.println("App fechada");
                Ginasio.saveGymToXML();
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida");
                break;
        }
    }


    /**
     * Mostra as opções disponíveis para o cliente e executa a opcao desejada.
     */
    public static void menuClient(Client client) {
        Scanner scanner =scanner();
        System.out.println("\t Menu Cliente:");
        System.out.println(client.getName() + " neste menu voce pode  ver e alterar certos elementos do seu perfil, visualizar os funcionarios \n" +
                "do nosso estabelicemento e caso seja um membro com o pack 2 podera tambem escolher um dos nossos instrutores para o" +
                " treinar. ");
        System.out.println("[1] -> Para visualizar a calculadora dos seus niveis caloricos");
        System.out.println("[2] -> Para alterar a sua idade");
        System.out.println("[3] -> Para alterar a sua altura");
        System.out.println("[4] -> Para alterar o seu peso");
        System.out.println("[5] -> Para alterar a sua taxa de atividade fisica");
        System.out.println("[6] -> Para alterar a sua password");
        System.out.println("[7] -> Para alterar o seu numero");
        System.out.println("[8] -> Para visualizar os funcionarios do ginasio");
        System.out.println("[9] -> Para alterar o  seu Pack");
        System.out.println("[10] -> Para escolher um dos instrutores para lhe treinar");
        System.out.println("[11] -> Para vizualizar o seu perfil");
        System.out.println("[12] -> Log out");
        System.out.println("[13] -> Fechar app");
        int opcao = scanner.nextInt();
        System.out.println("----------------------------------------\n");
        switch (opcao) {
            case 1:
                System.out.println("Aqui está uma estimativa do valor calorico:");
                System.out.println(client.caloriesCalculator());
                System.out.println("Aqui está uma estimativa do valor calorico, para perder peso:");
                System.out.println(client.getCaloricDeficit());
                System.out.println("Aqui está uma estimativa do valor calorico, para ganhar peso:");
                System.out.println(client.getGainWeight());
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 2:
                System.out.println("Esta e a sua idade atual " + client.getAge());
                if (change())
                {
                    System.out.println("Nova idade: ");
                    int idade = scanner.nextInt();
                    client.setIdade(idade);
                }
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 3:
                System.out.println("Esta e a sua altura atual " + client.getAltura());
                if (change())
                {
                    System.out.println("Nova altura: ");
                    int altura = scanner.nextInt();
                    client.setAltura(altura);
                }
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 4:
                System.out.println("Este e o seu peso atual " + client.getPeso());
                if (change())
                {
                    System.out.println("Novo peso: ");
                    int peso = scanner.nextInt();
                    client.setPeso(peso);
                }
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 5:
                System.out.println("Esta e a sua taxa de atividade fisica atual " + client.getTaxaDeAtivFisica());
                if (change())
                {
                    System.out.println("Nova TFA: ");
                    int TFA = scanner.nextInt();
                    client.setTaxaDeAtivFisica(TFA);
                }
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 6:
                System.out.println("Esta e a sua password atual " + client.getPassword());
                if (change())
                {
                    System.out.println("Nova password (pelo menos 4 numeros, 4 letras minusculas e uma maiscula): ");
                    scanner = scanner();
                    String pass = scanner.nextLine();
                    client.setPassword(pass);
                }
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 7:
                System.out.println("Este e o seu numero atual " + client.getNumber());
                if (change())
                {
                    System.out.println("Nova numero: ");
                    scanner = scanner();
                    String numero = scanner.nextLine();
                    client.setNumber(numero);
                }
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 8:
                System.out.println(Arrays.toString(Ginasio.getEmployees()));
                System.out.println("Deseja visualizar o perfil de um dos funcionarios? yes/no");
                Scanner scanner1 = scanner();
                String r = scanner1.nextLine();
                if (r.equals("yes"))
                {
                    System.out.println("Escreva o nome do funcionario:");
                    String n = scanner1.nextLine();
                    int ind = Ginasio.getIndexOfEmployee(n);
                    if (ind>=0)
                    {
                        System.out.println(Ginasio.employees.get(ind));
                    }
                }
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 9:
                System.out.println("Este e o seu pack atual " + client.getPayment().getNumeroDoPack() +
                        ", e o seu preco " + client.getPayment().setPrice());
                if (change())
                {
                    System.out.println("Nova Pack: ");
                    scanner = scanner();
                    int pack = scanner.nextInt();
                    client.getPayment().setNumeroDoPack(pack);
                }
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 10:
                if (client.getPayment().getNumeroDoPack() == 2)
                {
                    Scanner scanner3 = scanner();
                    System.out.println(Arrays.toString(Ginasio.getEmployees()));
                    System.out.println("Escreva o nome do instrutor:");
                    String n = scanner3.nextLine();
                    int ind = Ginasio.getIndexOfEmployee(n);
                    if (ind>=0)
                    {
                        PersonalTrainer employee = (PersonalTrainer) Ginasio.employees.get(ind);
                        if (employee.addClient(client.getName()))
                            System.out.println("Adicao com sucesso");
                        else
                            System.out.println("Sem sucesso");
                    }
                }else System.out.println("Parametro Invalido");
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 11:
                System.out.println(client);
                if (Continue())
                    menuClient(client);
                else Ginasio.saveGymToXML();
                break;
            case 12:
                System.out.println("Log out feito com sucesso");
                login();
                break;
            case 13:
                System.out.println("App fechada");
                Ginasio.saveGymToXML();
                System.exit(0);
                break;
            default:
                System.out.print("\n Escolha uma opção válida ");
                menuClient(client);
        }
    }

    /**
     * Mostra as opções disponíveis para o personal trainer e executa a opcao desejada.
     */
    public static void menuPt(PersonalTrainer pt) {
        Scanner scanner = scanner();
        System.out.println("\t Menu Personal trainer:");
        System.out.println(pt.getName() + " neste menu voce pode  ver e alterar certos elementos do seu perfil, visualizar os seus colegas \n" +
                "adicionar clientes e remove-los");
        System.out.println("[1] -> Para visualizar os seus colegas");
        System.out.println("[2] -> Para alterar a sua idade");
        System.out.println("[3] -> Para alterar a sua password");
        System.out.println("[4] -> Para alterar o seu numero");
        System.out.println("[5] -> Para adicionar clientes");
        System.out.println("[6] -> Para remover clientes");
        System.out.println("[7] -> Para ver os seus clientes");
        System.out.println("[8] -> Para ver o salario e bonus (caso exista)");
        System.out.println("[9] -> Para visualizar data de contratacao, fim do contrato e anos de experiencia");
        System.out.println("[10] -> Para vizualizar o seu perfil");
        System.out.println("[11] -> Log out");
        System.out.println("[12] -> Fechar app");
        int opcao = scanner.nextInt();
        System.out.println("----------------------------------------\n");
        switch (opcao) {
            case 1:
                System.out.println(Arrays.toString(Ginasio.getEmployees()));
                System.out.println("Deseja visualizar o perfil de um dos seus colegas? yes/no");
                Scanner scanner1 = scanner();
                String r = scanner1.nextLine();
                if (r.equals("yes"))
                {
                    System.out.println("Escreva o nome do perfil que deseja ver:");
                    String n = scanner1.nextLine();
                    int ind = Ginasio.getIndexOfEmployee(n);
                    if (ind>=0)
                    {
                        System.out.println(Ginasio.employees.get(ind));
                    }
                }
                if (Continue())
                    menuPt(pt);
                else Ginasio.saveGymToXML();
                break;
            case 2:
                System.out.println("Esta e a sua idade atual " + pt.getAge());
                if (change())
                {
                    System.out.println("Nova idade: ");
                    scanner = scanner();
                    int idade = scanner.nextInt();
                    pt.setIdade(idade);
                }
                if (Continue())
                    menuPt(pt);
                else Ginasio.saveGymToXML();
                break;
            case 3:
                System.out.println("Esta e a sua password atual " + pt.getPassword());
                if (change())
                {
                    System.out.println("Nova password (pelo menos 4 numeros, 4 letras minusculas e uma maiscula): ");
                    scanner = scanner();
                    String pass = scanner.nextLine();
                    pt.setPassword(pass);
                }
                if (Continue())
                    menuPt(pt);
                else Ginasio.saveGymToXML();
                break;
            case 4:
                System.out.println("Este e o seu numero atual " + pt.getNumber());
                if (change())
                {
                    System.out.println("Nova numero: ");
                    scanner = scanner();
                    String numero = scanner.nextLine();
                    pt.setNumber(numero);
                }
                if (Continue())
                    menuPt(pt);
                else Ginasio.saveGymToXML();
                break;
            case 5:
                Scanner scanner2 = scanner();
                System.out.println(Arrays.toString(Ginasio.getMembers()));
                System.out.println("Deseja adicionar um cliente? yes/no");
                String r2 = scanner2.nextLine();
                if (r2.equals("yes")) {
                    System.out.println("Escreva o nome de um cliente:");
                    String n = scanner2.nextLine();
                    int ind = Ginasio.getIndexOfGymM(n);
                    if (ind >= 0)
                    {
                        if (pt.addClient(Ginasio.GymMembers.get(ind).getName()))
                            System.out.println("Adicao com sucesso");
                        else
                            System.out.println("Sem sucesso");
                    } else
                        System.out.println("Cliente nao encontrado");
                }
                if (Continue())
                    menuPt(pt);
                else Ginasio.saveGymToXML();
                break;
            case 6:
                Scanner scanner3 = scanner();
                System.out.println(Arrays.toString(pt.getClients()));
                System.out.println("Deseja remover um cliente? yes/no");
                String r3 = scanner3.nextLine();
                if (r3.equals("yes")) {
                    System.out.println("Escreva o nome de um cliente:");
                    String n2 = scanner3.nextLine();
                    int index = pt.getIndexOfGymM(n2);
                    if (index >= 0)
                    {
                        if (pt.delClient(pt.ptClients.get(index)))
                            System.out.println("Cliente removido com sucesso");
                        else
                            System.out.println("Sem sucesso");
                    } else
                        System.out.println("Cliente nao encontrado");
                }
                if (Continue())
                    menuPt(pt);
                else Ginasio.saveGymToXML();
                break;
            case 7:
                System.out.println("Estes sao os seus clientes: \n " + Arrays.toString(pt.getClients()));
                if (Continue())
                    menuPt(pt);
                else Ginasio.saveGymToXML();
                break;
            case 8:
                if (pt.Bonus()>0)
                    System.out.println("Este é o seu bonus: " + pt.Bonus());
                System.out.println("Este é o seu salario (bonus já incrementado, caso exista): " + pt.uptadeSalario());
                if (Continue())
                    menuPt(pt);
                else Ginasio.saveGymToXML();
                break;
            case 9:
                System.out.println("Esta é a sua data de contratacao: " + pt.getDataContratacao());
                System.out.println("O seu contrato termina: " + pt.endContract());
                System.out.println("Voce tem "  + pt.getExperiencia() + " anos de experiencia.");
                if (Continue())
                    menuPt(pt);
                else Ginasio.saveGymToXML();
                break;
            case 10:
                System.out.println(pt);
                if (Continue())
                    menuPt(pt);
                else Ginasio.saveGymToXML();
                break;
            case 11:
                System.out.println("Log out feito com sucesso");
                login();
                break;
            case 12:
                System.out.println("App fechada");
                Ginasio.saveGymToXML();
                System.exit(0);
                break;
            default:
                System.out.print("\n Escolha uma opção válida ");
                menuPt(pt);
        }
    }

    /**
     * Com base no tipo de person recebido (cliente ou funcionário), chama o seu menu apropriado.
     * @param person pessoa para quem se vai apresentar o menu.
     */
    public static void decision(Person person)
    {
        if (person instanceof Client)
            menuClient((Client) person);
        else {
            Employee employee = (Employee) person;
            if (employee.getCargo().equals("Instrutor")) {
                menuPt((PersonalTrainer) employee);
            }else if (employee.getCargo().equals("Gestor")) {
                menuGestor(employee);
            } else {
                menuAL(employee);
            }
        }
    }

    /**
     * Mostra as opções disponíveis para o auxiliar de limpezas e executa a opcao desejada.
     */
    public static void menuAL(Employee employee) {
        Scanner scanner = scanner();
        System.out.println("\t Menu Auxiliar de limpeza:");
        System.out.println(employee.getName() + " neste menu voce pode  ver e alterar certos elementos do seu perfil,\n " +
                "visualizar os seus colegas e seus perfis.");
        System.out.println("[1] -> Para visualizar os seus colegas");
        System.out.println("[2] ->  Para alterar a sua idade");
        System.out.println("[3] -> Para alterar a sua password");
        System.out.println("[4] -> Para alterar o seu numero");
        System.out.println("[5] -> Para vizualizar o seu perfil");
        System.out.println("[6] -> Log out");
        System.out.println("[7] -> Fechar app");
        int opcao = scanner.nextInt();
        System.out.println("----------------------------------------\n");
        switch (opcao) {
            case 1:
                System.out.println(Arrays.toString(Ginasio.getEmployees()));
                System.out.println("Deseja visualizar o perfil de um dos seus colegas? yes/no");
                Scanner scanner1 = scanner();
                String r = scanner1.nextLine();
                if (r.equals("yes"))
                {
                    System.out.println("Escreva o nome do perfil que deseja ver::");
                    String n = scanner1.nextLine();
                    int ind = Ginasio.getIndexOfEmployee(n);
                    if (ind>=0)
                    {
                        System.out.println(Ginasio.employees.get(ind));
                    }else
                        System.out.println("Funcionario nao encontrado");
                }
                if (Continue())
                    menuAL(employee);
                else Ginasio.saveGymToXML();
                break;
            case 2:
                System.out.println("Esta e a sua idade atual " + employee.getAge());
                if (change())
                {
                    System.out.println("Nova idade: ");
                    scanner = scanner();
                    int idade = scanner.nextInt();
                    employee.setIdade(idade);
                }
                if (Continue())
                    menuAL(employee);
                else Ginasio.saveGymToXML();
                break;
            case 3:
                System.out.println("Esta e a sua password atual " + employee.getPassword());
                if (change())
                {
                    System.out.println("Nova password (pelo menos 4 numeros, 4 letras minusculas e uma maiscula): ");
                    scanner = scanner();
                    String pass = scanner.nextLine();
                    employee.setPassword(pass);
                }
                if (Continue())
                    menuAL(employee);
                else Ginasio.saveGymToXML();
                break;
            case 4:
                System.out.println("Este e o seu numero atual " + employee.getNumber());
                if (change())
                {
                    System.out.println("Nova numero: ");
                    scanner = scanner();
                    String numero = scanner.nextLine();
                    employee.setNumber(numero);
                }
                if (Continue())
                    menuAL(employee);
                else Ginasio.saveGymToXML();
                break;
            case 5:
                System.out.println(employee);
                if (Continue())
                    menuAL(employee);
                else Ginasio.saveGymToXML();
                break;
            case 6:
                System.out.println("Log out feito com sucesso \n");
                login();
                break;
            case 7:
                System.out.println("App fechada");
                Ginasio.saveGymToXML();
                System.exit(0);
                break;
            default:
                System.out.print("\n Escolha uma opção válida ");
                menuAL(employee);
        }
    }

    /**
     * Mostra as opções disponíveis para o gestor e executa a opcao desejada.
     * Gestor é o empregado com maior poder nesta app, podendo manipular mais dados.
     */
    public static void menuGestor(Employee employee) {
        Scanner scanner = scanner();
        System.out.println("\t Menu Gestor:");
        System.out.println(employee.getName() + " neste menu voce pode  ver e alterar informacoes pessoais e do ginasio, tambem pode \n" +
                "adicionar clientes/funcionarios/maquinas e remove-los");
        System.out.println("[1] -> Para visualizar os seus colegas"); 
        System.out.println("[2] -> Para alterar a sua idade ou de outro funcionario");
        System.out.println("[3] -> Para alterar a sua password");
        System.out.println("[4] -> Para alterar o seu numero");
        System.out.println("[5] -> Para visualizar a sua data de contratacao, fim do contrato ou de outro funcionario");
        System.out.println("[6] -> Para criar e adicionar clientes");
        System.out.println("[7] -> Para remover clientes");
        System.out.println("[8] -> Para criar e adicionar funcionarios");
        System.out.println("[9] -> Para remover funcionarios");
        System.out.println("[10] -> Para adicionar clientes a instrutores");
        System.out.println("[11] -> Para remover clientes a instrutores");
        System.out.println("[12] -> Para criar e adicionar maquinas");
        System.out.println("[13] -> Para remover maquinas");
        System.out.println("[14] -> Para ver as maquinas do ginasio");
        System.out.println("[15] -> Para ver os clientes do ginasio");
        System.out.println("[16] -> Para criar novos contratos para os funcionarios");
        System.out.println("[17] -> Ver Finanças do ginasio");
        System.out.println("[18] -> Para vizualizar o seu perfil");
        System.out.println("[19] -> Log out");
        System.out.println("[20] -> Fechar app");
        int opcao = scanner.nextInt();
        System.out.println("----------------------------------------\n");
        switch (opcao) {
            case 1:
                System.out.println(Arrays.toString(Ginasio.getEmployees()));
                System.out.println("Deseja visualizar o perfil de um dos seus colegas? yes/no");
                Scanner scanner1 = scanner();
                String r = scanner1.nextLine();
                if (r.equals("yes"))
                {
                    System.out.println("Escreva o nome do perfil que deseja ver:");
                    String n = scanner1.nextLine();
                    int ind = Ginasio.getIndexOfEmployee(n);
                    if (ind>=0)
                    {
                        System.out.println(Ginasio.employees.get(ind));
                    }
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 2:
                if (dadoPessoal()) {
                    System.out.println("Esta e a sua idade atual " + employee.getAge());
                    if (change())
                    {
                        System.out.println("Nova idade: ");
                        scanner = scanner();
                        int idade = scanner.nextInt();
                        employee.setIdade(idade);
                    }
                }else {
                    System.out.println(Arrays.toString(Ginasio.getEmployees()));
                    System.out.println("Deseja visualizar o perfil de um dos seus colegas? yes/no");
                    Scanner scanner2 = scanner();
                    String res = scanner2.nextLine();
                    if (res.equals("yes"))
                    {
                        System.out.println("Escreva o nome do funcionário:");
                        String n = scanner2.nextLine();
                        int ind = Ginasio.getIndexOfEmployee(n);
                        if (ind>=0)
                        {
                            System.out.println(Ginasio.employees.get(ind));
                            if (change())
                            {
                                System.out.println("Nova idade: ");
                                int idade = scanner.nextInt();
                                Ginasio.employees.get(ind).setIdade(idade);
                            }
                        }
                    }
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 3:
                System.out.println("Esta e a sua password atual " + employee.getPassword());
                if (change())
                {
                    System.out.println("Nova password (pelo menos 4 numeros, 4 letras minusculas e uma maiscula): ");
                    scanner = scanner();
                    String pass = scanner.nextLine();
                    employee.setPassword(pass);
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 4:
                System.out.println("Este e o seu numero atual " + employee.getNumber());
                if (change())
                {
                    System.out.println("Nova numero: ");
                    scanner = scanner();
                    String numero = scanner.nextLine();
                    employee.setNumber(numero);
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 5:
                if (dadoPessoal()) {
                    System.out.println("Esta é a sua data de contratacao: " + employee.getDataContratacao());
                    System.out.println("O seu contrato termina: " + employee.endContract());
                } else {
                    System.out.println(Arrays.toString(Ginasio.getEmployees()));
                    System.out.println("Deseja visualizar a informacao de um dos seus colegas? yes/no");
                    Scanner scanner2 = scanner();
                    String res = scanner2.nextLine();
                    if (res.equals("yes"))
                    {
                        System.out.println("Escreva o nome do funcionario:");
                        String n = scanner2.nextLine();
                        int ind = Ginasio.getIndexOfEmployee(n);
                        if (ind>=0)
                        {
                            System.out.println("Esta é a sua data de contratacao: " + Ginasio.employees.get(ind).getDataContratacao());
                            System.out.println("O seu contrato termina: " + Ginasio.employees.get(ind).endContract());
                        }
                    }
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 6:
                Client newClient = createClient();
                Ginasio.addGymMember(newClient);
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 7:
                System.out.println("Estes sao os clientes do ginasio: \n " + Arrays.toString(Ginasio.getMembers()));
                System.out.println("Deseja remover um dos clientes? yes/no");
                Scanner scanner2 = scanner();
                String res = scanner2.nextLine();
                if (res.equals("yes"))
                {
                    System.out.println("Escreva o nome do cliente:");
                    String n = scanner2.nextLine();
                    int ind = Ginasio.getIndexOfGymM(n);
                    if (ind >= 0)
                    {
                        if (Ginasio.delClient(Ginasio.GymMembers.get(ind)))
                            System.out.println("Cliente removido com sucesso");
                        else
                            System.out.println("Sem sucesso");
                    } else
                        System.out.println("Cliente nao encontrado");
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 8:
                Employee newEmployee = createEmployee();
                Ginasio.addEmployee(newEmployee);
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 9:
                System.out.println("Estes sao os funcionarios do ginasio: \n " + Arrays.toString(Ginasio.getEmployees()));
                System.out.println("Deseja remover um dos funcionarios? yes/no");
                Scanner scanner4 = scanner();
                String resp = scanner4.nextLine();
                if (resp.equals("yes"))
                {
                    System.out.println("Escreva o nome do funcionario:");
                    String nf = scanner4.nextLine();
                    int j = Ginasio.getIndexOfEmployee(nf);
                    if (j >= 0)
                    {
                        if (Ginasio.delEmployee(Ginasio.employees.get(j)))
                            System.out.println("Funcionario removido com sucesso");
                        else
                            System.out.println("Sem sucesso");
                    } else
                        System.out.println("Funcionario nao encontrado");
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 10:
                Scanner scanner3 = scanner();
                System.out.println(Arrays.toString(Ginasio.getMembers()));
                System.out.println(Arrays.toString(Ginasio.getEmployees()));
                System.out.println("Deseja adicionar um cliente a um instrutor? yes/no");
                String r2 = scanner3.nextLine();
                if (r2.equals("yes")) {
                    System.out.println("Escreva o nome de um cliente:");
                    String n = scanner3.nextLine();
                    int ind = Ginasio.getIndexOfGymM(n);
                    System.out.println("Escreva o nome de um instrutor:");
                    String s = scanner3.nextLine();
                    int i = Ginasio.getIndexOfEmployee(s);
                    if (ind >= 0 && i>=0)
                    {
                        if (Ginasio.employees.get(i) instanceof PersonalTrainer)
                        {
                            PersonalTrainer pt = (PersonalTrainer) Ginasio.employees.get(i);
                            if (pt.addClient(Ginasio.GymMembers.get(ind).getName()))
                                System.out.println("Adicao com sucesso");
                            else
                                System.out.println("Sem sucesso");

                        }else
                            System.out.println("Instrutor nao encontrado");

                    } else
                        System.out.println("Cliente ou Instrutor nao encontrado");
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 11:
                Scanner scanner5 = scanner();
                System.out.println(Arrays.toString(Ginasio.getMembers()));
                System.out.println(Arrays.toString(Ginasio.getEmployees()));
                System.out.println("Deseja remover um cliente a um instrutor? yes/no");
                String r3 = scanner5.nextLine();
                if (r3.equals("yes")) {
                    System.out.println("Escreva o nome de um instrutor:");
                    String s = scanner5.nextLine();
                    int i = Ginasio.getIndexOfEmployee(s);
                    if (Ginasio.employees.get(i) instanceof PersonalTrainer) {
                        PersonalTrainer pt = (PersonalTrainer) Ginasio.employees.get(i);
                        System.out.println(Arrays.toString(pt.getClients()));
                        System.out.println("Escreva o nome de um cliente:");
                        String n = scanner5.nextLine();
                        int ind = pt.getIndexOfGymM(n);
                        if (ind >= 0 && i >= 0) {
                            if (pt.delClient(pt.ptClients.get(ind)))
                                System.out.println("Cliente Removido com sucesso");
                            else
                                System.out.println("Sem sucesso");
                        }
                    } else
                        System.out.println("Instrutor nao encontrado");
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 12:
                Equipamento newMachine = createEquipamento();
                Ginasio.addMachine(newMachine);
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 13:
                Scanner scanner6 = scanner();
                System.out.println(Arrays.toString(Ginasio.getEquipamento()));
                System.out.println("Deseja remover uma maquina? yes/no");
                String r4 = scanner6.nextLine();
                if (r4.equals("yes")) {
                    System.out.println("Escreva o nome da maquina:");
                    String n2 = scanner6.nextLine();
                    int index = Ginasio.getIndexOfMachines(n2);
                    if (index >= 0)
                    {
                        if (Ginasio.delMachine(Ginasio.machines.get(index)))
                            System.out.println("Maquina removida com sucesso");
                        else
                            System.out.println("Sem sucesso");
                    } else
                        System.out.println("Maquina nao encontrada");
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 14:
                System.out.println(Arrays.toString(Ginasio.getEquipamento()));
                System.out.println("Deseja visualizar os dados de um dos equipamentos? yes/no");
                Scanner scanner0 = scanner();
                String res0 = scanner0.nextLine();
                if (res0.equals("yes"))
                {
                    System.out.println("Escreva o nome do equipamento:");
                    String n = scanner0.nextLine();
                    int ind = Ginasio.getIndexOfMachines(n);
                    if (ind>=0)
                        System.out.println(Ginasio.machines.get(ind));
                    else System.out.println("Equipamento nao encontrado");
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 15:
                System.out.println(Arrays.toString(Ginasio.getMembers()));
                System.out.println("Deseja visualizar os dados de um dos clientes? yes/no");
                Scanner scan = scanner();
                String cli = scan.nextLine();
                if (cli.equals("yes"))
                {
                    System.out.println("Escreva o nome do cliente:");
                    String n = scan.nextLine();
                    int ind = Ginasio.getIndexOfGymM(n);
                    if (ind>=0)
                        System.out.println(Ginasio.GymMembers.get(ind));
                    else System.out.println("Cliente nao encontrado");
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 16:
                Scanner scanner7 = scanner();
                System.out.println(Arrays.toString(Ginasio.getEmployees()));
                System.out.println("Deseja criar novos contratos para empregado? yes/no");
                String r5 = scanner7.nextLine();
                if (r5.equals("yes")) {
                    System.out.println("Escreva o nome de um empregado:");
                    String s = scanner7.nextLine();
                    int i = Ginasio.getIndexOfEmployee(s);
                    if (i>=0 && Ginasio.getIndexOfEmployee(employee.getName()) != i)
                    {
                        Ginasio.employees.get(i).newContract();
                        System.out.println("Insira um novo salario ou insire um numero negativo para manter o anterior");
                        int x = scanner7.nextInt();
                        if (x>0)
                            Ginasio.employees.get(i).Raise(x);
                        System.out.println("Novo contrato feito");
                    }
                }
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 17:
                System.out.println("Lucros: " + Ginasio.gymLucros());
                System.out.println("Entradas: " + Ginasio.gymEntradas());
                System.out.println("Despesas: " + Ginasio.gymDespesas());
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 18:
                System.out.println(employee);
                if (Continue())
                    menuGestor(employee);
                else Ginasio.saveGymToXML();
                break;
            case 19:
                System.out.println("Log out feito com sucesso");
                login();
                break;
            case 20:
                System.out.println("App fechada");
                Ginasio.saveGymToXML();
                System.exit(0);
                break;
            default:
                System.out.print("\n Escolha uma opção válida ");
                menuGestor(employee);
        }
    }

    /**
     * Cria um novo objeto Client com base nas informações fornecidas pelo user.
     * @return Client criado
     */
    public static Client createClient()
    {
        Scanner scanner0 = scanner();
        System.out.println("Nome: ");
        String nome = scanner0.nextLine();
        System.out.println("Idade: ");
        int idade = scanner0.nextInt();
        scanner0.nextLine(); // Consumir a nova linha pendente após a leitura do número
        System.out.println("Numero: ");
        String numero = scanner0.nextLine();
        System.out.println("Password (pelo menos 4 numeros, 4 letras minusculas e uma maiscula): ");
        String pass = scanner0.nextLine();
        System.out.println("Genero: ");
        String genero = scanner0.nextLine();
        System.out.println("Peso (em kg): ");
        int peso = scanner0.nextInt();
        System.out.println("Altura (em cm): ");
        int altura = scanner0.nextInt();
        System.out.println("Taxa de atividade fisica");
        System.out.println("0 - Sedentario");
        System.out.println("1 - Leve");
        System.out.println("2 - Moderado");
        System.out.println("3 - Intenso");
        System.out.println("4 - Muito Intenso");
        int taf = scanner0.nextInt();
        System.out.println("Packs do ginasio");
        System.out.println("1 - Tem acesso às instalações do ginásio e a uma aula de introdução com um instrutor. Valor do pack: 25 euros mensais.");
        System.out.println("2 - Tem acesso às instalações do ginásio e a um instrutor que lhe dará treinos personalizados e acompanhará o seu desenvolvimento. Valor do pack: 35 euros mensais.");
        int clientPack = scanner0.nextInt();
        Client newClient = new Client(nome, idade, numero, pass, genero, peso, altura, clientPack, LocalDate.now(), taf);
        return newClient;
    }

    /**
     * Cria um novo objeto Employee ou PersonalTrainer com base nas informações fornecidas pelo user.
     * @return Employee ou PersonalTrainer criado
     */
    public static Employee createEmployee()
    {
        Scanner scanner01 = scanner();
        System.out.println("Criar Empregado:");
        System.out.println("[1] -> Criar Gestor ou Auxiliar de limpeza");
        System.out.println("[2] -> Criar Instrutor");
        int decisao = scanner01.nextInt();
        if (decisao==1)
        {
            Scanner scanner21 = scanner();
            System.out.println("Nome: ");
            String nome = scanner21.nextLine();
            System.out.println("Idade: ");
            int idade = scanner21.nextInt();
            scanner21.nextLine(); // Consumir a nova linha pendente após a leitura do número
            System.out.println("Numero: ");
            String numero = scanner21.nextLine();
            System.out.println("Password (pelo menos 4 numeros, 4 letras minusculas e uma maiscula): ");
            String pass = scanner21.nextLine();
            System.out.println("Genero: ");
            String genero = scanner21.nextLine();
            System.out.println("Cargo: ");
            String cargo = scanner21.nextLine();
            System.out.println("Salario: ");
            int salario = scanner21.nextInt();
            System.out.println("Anos de contrato: ");
            int contrato = scanner21.nextInt();
            Employee newEmployee = new Employee(nome, idade, numero, pass, genero, cargo, salario,  LocalDate.now(), contrato);
            return newEmployee;
        }
        Scanner s = scanner();
        System.out.println("Nome: ");
        String nome = s.nextLine();
        System.out.println("Idade: ");
        int idade = s.nextInt();
        s.nextLine(); // Consumir a nova linha pendente após a leitura do número
        System.out.println("Numero: ");
        String numero = s.nextLine();
        System.out.println("Password (pelo menos 4 numeros, 4 letras minusculas e uma maiscula): ");
        String pass = s.nextLine();
        System.out.println("Genero: ");
        String genero = s.nextLine();
        System.out.println("Anos de contrato: ");
        int contrato = s.nextInt();
        System.out.println("Anos de experiencia: ");
        int exp = s.nextInt();
        PersonalTrainer newPt = new PersonalTrainer(nome, idade, numero, pass, genero,  LocalDate.now(), contrato, exp);
        return newPt;
    }

    /**
     * Cria um novo objeto Equipamento com base nas informações fornecidas pelo user.
     * @return Equipamento criado
     */
    public static Equipamento createEquipamento()
    {
        Scanner scanner3 = scanner();
        System.out.println("Nome: ");
        String nome = scanner3.nextLine();
        System.out.println("Custo: ");
        int custo = scanner3.nextInt();
        Equipamento newE = new Equipamento(nome, LocalDate.now(), custo);
        return newE;
    }

    /**
     * Pergunta ao user se deseja visualizar/alterar os seus próprios dados ou os de outro funcionário.
     * @return true se deseja visualizar/alterar os seus próprios dados, false se deseja visualizar/alterar os dados de outro funcionário
     */
    public static boolean dadoPessoal()
    {
        System.out.println("Deseja alterar/visualizar os seus dados ou de outro funcionario?");
        System.out.println("[1] -> Dados Pessoais");
        System.out.println("[2] -> De outro funcionario");
        Scanner scanner = scanner();
        int r = scanner.nextInt();
        return r == 1;
    }

    /**
     * Pergunta ao user se deseja alterar os dados.
     * @return true se deseja alterar os dados, false caso contrário
     */
    public static boolean change()
    {
        System.out.println("Deseja alterar os dados? yes/no");
        Scanner scanner = scanner();
        String c = scanner.nextLine();
        return c.equalsIgnoreCase("yes");
    }

    /**
     * Pergunta ao user se deseja voltar ao seu respetivo menu.
     * @return true se deseja voltar, false caso contrário
     */
    public static boolean Continue()
    {
        System.out.println("Deseja voltar ao menu? yes/no");
        Scanner scanner = scanner();
        String c = scanner.nextLine();
        return c.equalsIgnoreCase("yes");
    }
}
