package client;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import user.User;
import util.XMLDoc;

/**
 * A classe Jogador representa o cliente do jogo Othello. Este cliente se comunica
 * com o servidor Othello para jogar o jogo e realizar operações como login, registro
 * de usuário, alteração de senha e alteração de foto de perfil.
 */
public class Jogador {

	
    private final static String DEFAULT_HOST = "localhost";


    private final static int DEFAULT_PORT = 5025;

    
    /**
     * Método para ler um número inteiro a partir da entrada do usuário.
     *
     * @param leitor O scanner utilizado para ler a entrada do usuário.
     * @return O número inteiro lido.
     */
    private static int readInt(Scanner leitor) {
        int numero = 0; // Variável para armazenar o número lido.
        // Loop para garantir que um número válido é lido.
        while (true) {
            // Verifica se o próximo token na entrada é um número.
            if (leitor.hasNextInt()) {
                // Lê o número curto.
                numero = leitor.nextInt();
                
                // Verifica se o número está entre 1 e 8.
                if (numero < 1 || numero > 8) {
                    // Mostra uma mensagem de erro.
                    System.out.println("Jogada inválida!");
                } else {
                    // Retorna o número lido.
                    return numero;
                }
            } else {
                // Ignora a linha atual da entrada.
                leitor.nextLine();
            }
        }
    }
    
    /**
     * Método para ler uma senha de forma segura.
     *
     * @param prompt A mensagem exibida para solicitar a senha.
     * @param s O scanner utilizado para ler a entrada do usuário.
     * @return A senha fornecida pelo usuário.
     */
  private static String leSenha(String prompt, Scanner s) {
      String senha=null;
      if(System.console() != null)
    	  senha = new String(System.console().readPassword(prompt, 5000));
      else {
    	  System.out.println(prompt);
    	  senha = s.nextLine();
      }
      return senha;
  }
  
  
  /**
   * Método para exibir o menu de login e registro de usuário.
   *
   * @param leitor O scanner utilizado para ler a entrada do usuário.
   * @param Stub O objeto CliStub usado para comunicação com o servidor.
   * @return O nome de usuário após o login ou registro bem-sucedido.
   * @throws IOException Exceção lançada em caso de erro de E/S.
   * @throws Exception Outras exceções podem ser lançadas.
   */
  private static String menuInicio(Scanner leitor,CliStub Stub) throws IOException, Exception {
	 int opcao = 0;
	String nome = null;
		
  	do {
  		System.out.println();
          System.out.println();
   	    System.out.println("*** Login ***");
           System.out.println("1 - Desejo Registar-me?");
           System.out.println("2 - Desejo Fazer login");

  		opcao = leitor.nextInt();
  		
  		 switch (opcao) {
           	case 1:
           		User user = User.read();
      			nome = user.getUsername();
      			Stub.newUser(user);
      			System.out.println("Novo User criado com sucesso!");
           		break;
           	case 2:
           		System.out.println("<<< ***** Indique o seu nome de utilizador:");
      			int t=0;
      			
      			do {
      				nome = leitor.nextLine();
      				if(t++==3)  // termina o jogador
      					return null;
      			}while(nome.isBlank());
      			//String nome = System.console().readLine(); 
      			// Lê a senha do utilizador.
      			String senha = leSenha("<<< ***** Indique a sua senha:",leitor);
      			if(Stub.login(nome, senha)) {
      				System.out.print("Login com sucesso");
      			}else {
      				System.out.print("Insucesso a fazer login");
      				opcao = 0;
      			}
           		break;
           	default:
           		System.out.println("Opção inválida, esolha uma opção do menu.");
           }
  	}while(opcao < 1 || opcao > 2);
	  return nome;
  }
  
  /**
   * Método para exibir o menu de operações do usuário.
   *
   * @param Stub O objeto CliStub usado para comunicação com o servidor.
   * @param nome O nome de usuário atual.
   * @throws IOException Exceção lançada em caso de erro de E/S.
   * @throws Exception Outras exceções podem ser lançadas.
   */
  private static void menuUtilizador(CliStub Stub, String nome) throws IOException, Exception {
		 int opcao = 0;
		 Scanner leitor = new Scanner(System.in);
			
	  	do {
	  		System.out.println();
	        System.out.println();
	        System.out.println("*** Menu User ***");
	       	System.out.println("1 - Ver Perfil");
	      	System.out.println("2 - Alterar senha");
	      	System.out.println("3 - Alterar foto");
	        System.out.println("4 - Jogar Othello");

	  		opcao = leitor.nextInt();
	  		
	  		 switch (opcao) {
	           	case 1:
	           		String perfil = Stub.seePerfil(nome);
	           		System.out.println(perfil);
	           		break;
	           	case 2:
	           		System.out.println("Insira a senha atual!");
	           		String OldPass = null;
	           		String NewPass = null;
	           		
	      			do {
	      				OldPass = leitor.nextLine();
	      			}while(OldPass.isBlank());
	           		
	      			NewPass = leSenha("<<< ***** Indique a nova senha:",leitor);
	           		
	           		if(Stub.changePass(nome, OldPass, NewPass)) {
	      				System.out.print("Sucesso a alterar a senha");
	      			}else {
	      				System.out.print("Insucesso a alterar a senha");
	      				opcao = 0;
	      			}
	           		break;
	           	case 3:
	           		System.out.println("Insira o nome da nova foto!");
	           		String foto = null;
	           		do {
	           			foto = leitor.nextLine();
	      			}while(foto.isBlank());
	           		
	           		if(Stub.changePic(nome, foto)) {
	      				System.out.print("Sucesso a alterar a foto");
	      			}else {
	      				System.out.print("Insucesso a alterar a foto");
	      				opcao = 0;
	      			}
	           		
	           		break;
	           	case 4:
	           		break;
	           	default:
	           		System.out.println("Opção inválida, esolha uma opção do menu.");
	           }
	  	}while(opcao != 4);
	  }
                                  
  /**
   * Método principal que inicia o cliente Othello.
   *
   * @param args Os argumentos da linha de comando, se fornecidos.
   */
    public static void main(String[] args) {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        
        // Lê os argumentos da linha de comando (se existirem).
        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }      
        try (   
            
            Socket socket = new Socket(host, port);
            CliStub Stub = new CliStub(socket);
            Scanner leitor = new Scanner(System.in)) {
        	
        	System.out.println("Cliente -> Ligação estabelecida: " + socket);
        	
        	String nome = menuInicio(leitor, Stub);
        		
        	menuUtilizador(Stub, nome);
        	String simbolo = Stub.iniciar(nome);
        	Stub.print();
        	
            if(simbolo.equals("O "))
            	System.out.println("À espera que o oponente jogue...");
            
            //Guarda a quantidade de tempo de todas as jogadas do jogador
            long timeOfPlay = 0;
            
            // Loop do jogo, enquanto não for o fim do jogo estado!="ND"
            for(;;) {
            	Element tab=Stub.obter(simbolo);
            	
            	// Mostra o tabuleiro atual.
                System.out.println(Stub.tabuleiroToTXT(tab));
                
                String estado=tab.getAttribute("estado");
                
                if(!estado.equals("ND")) {
                	System.out.println(Stub.estadoToTXT(estado));
                	
                	if(!estado.equals("IV") && !estado.equals("SJ")) {
                		int gameState = Stub.estadoFinal(estado);
                		Stub.updateUser(nome, gameState,  timeOfPlay);
                		break;
                	}
                }
                
                if(!estado.equals("SJ")) {
                	LocalDateTime inicio = LocalDateTime.now();
                	System.out.println("Joga " + simbolo + " primeiro linha e depois a coluna: ");

                	// Lê a jogada do jogador.
                	int linha = readInt(leitor);
                	int coluna = readInt(leitor);
                	
                	//Atualiza o cronometro com a duração entre o início da jogada e o momento em que termina
                	timeOfPlay += Duration.between(inicio, LocalDateTime.now()).toMillis();
                	
                	// Envia jogada para o servidor.
                	Stub.jogar(linha, coluna);
                }
            }
        } catch (Exception e) {
            System.err.println("Jogador: " + e.getLocalizedMessage());
            // e.printStackTrace();
        } finally {
            System.out.println("Jogador: terminou o jogo!");
        }
    } 
}