/**
 * @author Fabio Pestana - A50756
 * @author Miguel Alcobia - A50746
 * @author Joao Ramos - A50730
 * ISEL - LEIM 23/24
 * Redes de Computadores
 * Phase 1 project - Web Server
 */

// Imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


/**
 * Esta classe implementa um cliente web básico que pode fazer solicitações GET a um servidor.
 * Ele fornece um menu interativo para o user escolher entre diferentes opções, como fazer uma solicitação GET,
 * alterar o endereço do servidor e a porta, ou sair do programa.
 */
public class WebClient {
    private String serverAddress;   // Endereço do servidor
    private int port;               // Porta do servidor
    private Socket socket;          // Socket para comunicação com o servidor

    /**
     * Construtor do cliente web.
     * @param option Se verdadeiro, o cliente utilizará o endereço e a porta padrão.
     *               Caso contrário, permitirá ao user escolher.
     */
    public WebClient(boolean option) {
        setSocket(option);
    }

    /**
     * Define o endereço do servidor.
     * @param serverAddress endereço IP do servidor.
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Define a porta do servidor.
     * @param port número da porta do servidor.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Mostra ao user um menu de opções para este escolher
     * usar as configurações padrão (Ip -> 127.0.0.1 porta -> 80),
     * ou se deseja alterar esses valores.
     * @return Verdadeiro se o usuário escolher definir manualmente o
     *          endereço do servidor e a porta, falso caso contrário.
     */
    public static boolean optionsMenu(){
        Scanner scanner = new Scanner(System.in); // Cria um objeto Scanner para ler inputs do user na Consola

        String opcaoSN;             //variável para armazenar a opção do usuário (S/N)
        boolean continuar = false;  //variável booleana para controlar a execução do loop
        boolean op = true;          //variável booleana para retornar a escolha do usuário (S - true, N - false)

        do {
            System.out.println("-------------------");
            System.out.println("Web Client -> TP1 RCp");
            System.out.println("-------------------");
            System.out.println("Deseja escolher o IP address do server e a port? (S/N)");
            System.out.println("S - localhost -> 127.0.0.1 port -> 80");
            System.out.println("-------------------");

            opcaoSN = scanner.next(); // Lê a opção do usuário

            // Verifica se o user digitou S ou N, ignorando maiúsculas/minúsculas
            if (opcaoSN.equalsIgnoreCase("S") || opcaoSN.equalsIgnoreCase("N")){
                continuar = true; // Altera o valor da variavel para sair do loop
                if (opcaoSN.equalsIgnoreCase("N")) op = false; // Define a opção como falsa se o user escolher "N"
            }else
                System.out.println("Opção inválida");

        } while (!continuar);

        return op;
    }

    /**
     * Mostra ao user o menu principal permitindo ao user escolher entre diferentes opções.
     *      - Fazer um request do tipo GET ao server
     *      - Alterar o endereço ou porta do servidor
     *      - Sair do programa
     */
    public void menu(){
        Scanner scanner = new Scanner(System.in); // Cria um objeto Scanner para ler inputs do user na Consola

        int opcao;                  // Variável para armazenar a opção escolhida pelo user
        boolean continuar = false;  // Variável booleana para controlar a execução do loop

        do {
            System.out.println();
            System.out.println("-------------------");
            System.out.println("Web Client Menu");
            System.out.println("-------------------");
            System.out.println("1. Get ");
            System.out.println("2. Alterar serverAddress e port");
            System.out.println("3. Sair");
            System.out.println("-------------------");
            System.out.print("Digite a opção desejada: ");

            opcao = scanner.nextInt(); // Lê a opção escolhida pelo user

            switch (opcao) { // Verifica a opção escolhida pelo user
                case 1:
                    get(); // Chama o método get() para fazer uma solicitação GET ao servidor
                    break;
                case 2:
                    setSocket(false); // Chama o método setSocket() para alterar o endereço e a porta do servidor
                    break;
                case 3:
                    System.out.println("Saindo do programa...");
                    continuar = true; // Altera o valor da variável para sair do loop
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (!continuar);

        scanner.close(); // Fecha o objeto Scanner

        try {
            socket.close(); // Tenta fechar o socket
        }catch (IOException e){
            System.out.println("Erro ao fechar a socket");
        }
    }

    /**
     * Solicita ao user um endereço IP.
     * @return endereço IP válido.
     */
    public static String newIP() {
        Scanner scanner = new Scanner(System.in);   // Cria um objeto Scanner para ler inputs do user na Consola
        String ipAddress = null;                    // Declara uma variável para armazenar o endereço IP inserido pelo user
        boolean isValid = false;                    // Variável booleana para verificar se o endereço IP é válido

        while (!isValid) {
            System.out.println("Insira um novo endereço IP:");
            ipAddress = scanner.next(); // Lê o endereço IP inserido pelo usuário

            try {
                InetAddress inetAddress = InetAddress.getByName(ipAddress); // Tenta criar um objeto InetAddress com o endereço IP inserido

                // Verifica se o endereço IP inserido é válido
                if (inetAddress.getHostAddress().equals(ipAddress)) {
                    isValid = true; // Altera o valor da variável isValid se o endereço IP for válido
                } else {
                    System.out.println("Endereço IP inválido");
                }
            } catch (UnknownHostException e) {
                System.out.println("Endereço IP inválido");
            }
        }

        return ipAddress;
    }

    /**
     * Solicita ao user uma nova port.
     * @return port válida.
     */
    public static int newPort (){
        System.out.println("Insira um nova port:");
        Scanner scanner = new Scanner(System.in);   // Cria um objeto Scanner para ler inputs do user na Consola
        boolean isValid = false;                    // Variável booleana para verificar se a porta é válida
        int port = 80;                              // Variável para armazenar a porta inserida pelo user, inicializada com um valor padrão de 80 (porta default)

        while (!isValid){
            port = scanner.nextInt(); // Lê a porta inserida pelo user

            // Verifica se a porta está dentro do intervalo válido (0-65535)
            if (port >= 0 && port <= 65535){
               isValid = true; // Altera o valor da variável isValid se a porta for válida
            }else{
                System.out.println("Número da Port inválida");
            }
        }
        return port;
    }

    /**
     * Cria um novo socket com o endereço e a port do server.
     * @return Verdadeiro se o socket for criado com sucesso, falso caso contrário.
     */
    public boolean setNewSocket(){
        boolean socketSucess = false;  // Variável que indica se a criação do socket foi bem-sucedida
        try{
            socket = new Socket(serverAddress, port);   // Tenta criar um novo socket com o endereço e a porta do servidor
            socketSucess = true;                        // Altera o valor da variável socketSucess se a criação do socket for bem-sucedida
        } catch (IOException e) {
            System.out.println("Insucesso na ligação");
        }
        return socketSucess;
    }

    /**
     * Define o endereço do servidor e a porta com base na escolha do user.
     * @param option Verdadeiro, caso o cliente utilize o endereço e a porta padrão
     *               (127.0.0.1 -> localhost e a port 80). Caso contrário,
     *               permitirá ao usuário escolher.
     */
    public void setSocket(boolean option) {
        boolean success;    // Variável para indicar se a criação do socket foi bem-sucedida
        do {
            // Se a opção for verdadeira utiliza-se os valores standard (127.0.0.1 -> localhost e a port 80)
            if (option) {
                this.serverAddress = "127.0.0.1";   // Define o endereço do servidor como localhost (127.0.0.1)
                this.port = 80;                     // Define a porta do servidor como 80 (porta padrão HTTP)
            } else {
                String ip = newIP();    // Pede ao user um novo endereço IP
                setServerAddress(ip);   // Altera o endereço do servidor com o IP inserido pelo user
                int port = newPort();   // Pede ao user uma nova porta
                setPort(port);          // Altera a porta do servidor com a porta inserida pelo user
            }
            success = setNewSocket();   // Tenta criar um novo socket com o endereço e a porta do servidor
            option = false;             // Define a opção como false para garantir que em caso de insucesso na criação do socket, pede-se um novo endereço e porta ao user
        } while (!success);
    }

    /**
     * Realiza um pedido ao servidor do tipo GET.
     * Este método envia uma solicitação HTTP GET para o servidor e lê a sua resposta.
     * Caso receba respostas com estado 301 ou 302, ele redireciona o pedido segundo
     * a localização recebida.
     * Caso receba resposta de outro tipo ele espera 1 segundo e volta a enviar
     * uma solicitação HTTP GET para o servidor.
     * Só para de enviar solicitações HTTP GET para o servidor caso receba uma
     * resposta com estado 200 OK.
     */
    public void get(){
        String path = null;             // Caminho inicialmente definido como nulo
        String request;                 // String para armazenar o pedido HTTP GET
        boolean received200 = false;    // Variável para verificar se uma resposta 200 OK foi recebida

        while (!received200) {
            try {
                socket = new Socket(serverAddress, port);       // Cria um novo socket para se conectar ao servidor
                OutputStream out = socket.getOutputStream();    // Obtém o fluxo de saída do socket para enviar o pedido
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Obtém o fluxo de entrada do socket para ler a resposta do servidor

                // Monta o pedido HTTP GET
                if (path != null) {
                    request = "GET " + path + " HTTP/1.1\r\n" +
                            "Host: " + serverAddress + "\r\n" +
                            "Connection: close\r\n\r\n";
                }else{
                    request = "GET / HTTP/1.1\r\n" +
                            "Host: " + serverAddress + "\r\n" +
                            "Connection: close\r\n\r\n";
                }

                out.write(request.getBytes());  // Envia a requisição para o servidor
                out.flush();                    // Limpa o buffer de saída

                StringBuilder response = new StringBuilder();   // String para armazenar a resposta do servidor
                String line;                                    // Variável para armazenar cada linha da resposta
                String location = null;                         // Variável para armazenar a nova localização caso haja redirecionamento

                // Loop para ler cada linha da resposta
                while ((line = in.readLine()) != null) {
                    response.append(line).append("\n");         // Adiciona a linha à resposta
                    if (line.startsWith("HTTP/1.1 200 OK")) {   // Verifica se a resposta é um status 200 OK
                        received200 = true;                     // Define received200 como true para indicar que a resposta foi recebida com sucesso
                    } else if (line.startsWith("HTTP/1.1 3")) { // Verifica se a resposta é um status 3xx (redirecionamento)
                        if (line.startsWith("HTTP/1.1 301") || line.startsWith("HTTP/1.1 302")) { // Verifica se é um redirecionamento permanente ou temporário
                            // Loop para ler as linhas seguintes
                            while ((line = in.readLine()) != null) {
                                if (line.startsWith("Location: ")) {                            // Verifica se a linha contém a informação de localização do redirecionamento
                                    location = line.substring("Location: ".length());           // Extrai a URL de destino do redirecionamento
                                    location = location.replace("http://", ""); // Remove o prefixo "http://" da URL de destino

                                    break;  // Sai do loop interno após encontrar a informação de localização
                                }
                            }
                        }
                        break;  // Sai do loop externo após encontrar uma resposta de redirecionamento
                    }
                }

                // Exibe a resposta do servidor
                if (received200) {                                      // Se a resposta for 200 OK, exibe a resposta do servidor
                    System.out.println("HTTP Response:\n" + response);
                } else if (location != null) {                          // Se houver redirecionamento, exibe a resposta e a nova localização
                    System.out.println();
                    System.out.println("HTTP Response:\n" + response);
                    System.out.println("Redirected to: " + location + "\n");
                    path = location.substring(serverAddress.length());  // Atualiza o caminho com a nova localização
                } else {                                                // Se a resposta não for 200 OK, exibe uma mensagem de erro e espera antes de tentar novamente
                    System.out.println("Received non-200 response. Retrying...");
                    Thread.sleep(1000);                            // Espera-se 1 segundo antes de tentar novamente
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        boolean option = WebClient.optionsMenu();   // Chama o método estático optionsMenu() para permitir que o user escolha as configurações do cliente web
        WebClient client = new WebClient(option);   // Cria uma nova instância do WebClient com base na opção escolhida
        client.menu();                              // Chama o método menu() para exibir o menu principal e interagir com o user
    }
}
