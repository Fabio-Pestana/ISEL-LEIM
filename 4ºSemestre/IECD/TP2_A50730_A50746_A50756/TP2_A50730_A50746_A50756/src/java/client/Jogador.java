package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;


import org.w3c.dom.Element;

import user.User;

/**
 * A classe Jogador representa o cliente do jogo Othello. Este cliente comunica
 * com o servidor Othello para jogar o jogo e realizar operações como login, registo
 * de user, alteração de senha, alteração de foto de perfil e ver o ranking do top 5
 * dos jogadores.
 */
public class Jogador {

    private final static String DEFAULT_HOST = "localhost";

    private final static int DEFAULT_PORT = 5025;
    
    private Socket socket = null;
	private CliStub Stub = null;

	/**
     * Cria um novo jogador e estabelece uma ligação com o servidor.
     *
     * @throws UnknownHostException Se o endereço do host não puder ser determinado.
     * @throws IOException          Se ocorrer um erro de E/S ao criar a ligação.
     */
	public Jogador() throws UnknownHostException, IOException {
		open();
	}
	
	/**
     * Estabelece uma ligação com o servidor.
     *
     * @throws UnknownHostException Se o endereço do host não puder ser determinado.
     * @throws IOException          Se ocorrer um erro de E/S ao criar a ligação.
     */
	public void open() throws UnknownHostException, IOException {
		socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
		Stub = new CliStub(socket);

		// Mostrar os parametros da ligação
		System.out.println("Ligação: " + socket);
	}
            
	/**
     * Obtém o socket utilizado para a ligação com o servidor.
     *
     * @return O socket utilizado para a ligação.
     */
	public Socket getSocket() {
		return socket;
	}

	/**
     * Obtém o stub do cliente utilizado para interagir com o servidor.
     *
     * @return O stub do cliente.
     */
	public CliStub getStub() {
		return Stub;
	}

    
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
  private String menuInicio(Scanner leitor) throws IOException, Exception {
	 int opcao = 0;
	String nome = null;
		
  	do {
  		System.out.println();
          System.out.println();
   	    System.out.println("*** Login ***");
           System.out.println("1 - Desejo Registar-me");
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
  private boolean menuUtilizador(String nome) throws IOException, Exception {
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
	        System.out.println("5 - Ranking");
	        System.out.println("6 - Log Out");

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
	           	case 5:
	           		String ranking = Stub.ranking();
	           		System.out.println(ranking);
	           		break;
	           	case 6:
	           		return false;
	           	default:
	           		System.out.println("Opção inválida, esolha uma opção do menu.");
	           }
	  	}while(opcao != 4);
	  	return true;
	  }
  
  /**
   * Menu para perguntar ao jogador se deseja jogar novamente ou voltar ao menu de utilizador.
   *
   * @param leitor O scanner para a entrada do jogador.
   * @return true se o jogo deve ser reiniciado, false se o jogador optar por voltar ao menu de utilizador ou se ocorrer um erro.
   * @throws IOException Se ocorrer um erro de E/S ao comunicar com o servidor.
   * @throws Exception   Se ocorrer um erro ao processar a opção do jogador.
   */
  private boolean menuGame(Scanner leitor) throws IOException, Exception {
		 int opcao = 0;
		 boolean resetGame = false;
	  	do {
	  		System.out.println();
	          System.out.println();
	   	    System.out.println("*** Play Again? ***");
	           System.out.println("1 - Deseja jogar outra vez?");
	           System.out.println("2 - Ou deseja voltar ao menu de user?");
	           System.out.println("Nota-> mesmo que escolha 1 se o outro player recusar, volta para o menu.");
	  		opcao = leitor.nextInt();
	  		
	  		 switch (opcao) {
	           	case 1:
	           		resetGame = Stub.playAgain(true);
	           		if(resetGame) {
	           			System.out.println("Reset Game");
	           		}else {
	           			System.out.println("Player Recusou Pedido, voltando ao Menu");
	           		}
	           		break;
	           	case 2:
	           		resetGame = Stub.playAgain(false);
	           		if(!resetGame) {
	           			System.out.println("Player Recusou Pedido, voltando ao Menu");
	           		}
	           		break;
	           	default:
	           		System.out.println("Opção inválida, esolha uma opção do menu.");
	           }
	  	}while(opcao < 1 || opcao > 2);
		  return resetGame;
	  }
  
  /**
   * Fecha a ligação com o servidor.
   */
	public void fechar() {
		// No fim, fechar os streams e o socket
		Stub.fechar();
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			// if an I/O error occurs when closing this socket
		}
	}

/**
   * Método principal que inicia o cliente Othello.
   *
   * @param args Os argumentos da linha de comando, se fornecidos.
   */
    public static void main(String[] args) {  
    	Jogador jogador = null;
    	boolean backToMenu = false;
    	boolean replay = false;
        try (Scanner leitor = new Scanner(System.in)) 
        {
        	jogador = new Jogador();
        	
        	String nome = jogador.menuInicio(leitor);
        	
        	do {
        		
        		backToMenu = jogador.menuUtilizador(nome);
        		if(!backToMenu) {
        			break;
        		}
	        	String simbolo = jogador.getStub().iniciar(nome);
	        	jogador.getStub().print();
	        	
	            if(simbolo.equals("O "))
	            	System.out.println("À espera que o oponente jogue...");
	            else
	            	System.out.println("X joga primeiro");
	            
	            //Guarda a quantidade de tempo de todas as jogadas do jogador
	            long timeOfPlay = 0;
	            do {
	            // Loop do jogo, enquanto não for o fim do jogo estado!="ND"
	            for(;;) {
	            	Element tab=jogador.getStub().obter(simbolo);
	            	
	            	// Mostra o tabuleiro atual.
	                System.out.println(jogador.getStub().tabuleiroToTXT(tab));
	                
	                String Symbol = tab.getAttribute("simbolo");
	                
	                if(Symbol.equals(simbolo)) {
		                String estado=tab.getAttribute("estado");
		                
		                if(!estado.equals("ND")) {
		                	System.out.println(jogador.getStub().estadoToTXT(estado));
		                	
		                	if(!estado.equals("IV") && !estado.equals("SJ")) {
		                		int gameState = jogador.getStub().estadoFinal(estado);
		                		jogador.getStub().updateUser(nome, gameState,  timeOfPlay);
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
		                	jogador.getStub().jogar(linha, coluna);
		                }
	                }else {
	                	System.out.println("À espera que o oponente " + Symbol + " jogue...");
	                }
	            }
	            	replay = jogador.menuGame(leitor);
	            }while(replay);
	            jogador.fechar();
	            jogador.open();
        	}while(!replay);
        } catch (Exception e) {
            System.err.println("Jogador: " + e.getLocalizedMessage());
        } finally {
            System.out.println("Jogador: terminou o jogo!");
            jogador.fechar();
        }
    } 
}