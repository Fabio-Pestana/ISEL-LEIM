package server;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import util.XMLDoc;


public class Servidor {

    public final static int DEFAULT_PORT = 5025;

    // Valor de timeout sem atividade termina o servidor
    private static int timeout = 0; // 1000*60*10;

    // Assinala que se pretende redirecionar o output
    private static boolean redirect = false;

    // Assinala que o servidor só vai facilitar um jogo
    // Modo do servidor jogo único facilitador do peer-to-peer
    private static boolean single = false;

    // Indica que o servidor tem um jogador ligado e está à espera de um 2º jogador
    private static boolean wait = false;

    // Assegura a exclusão no acesso ao atributo 'wait'
    private static final Semaphore semaphore = new Semaphore(1);

    
    /**
     * Afeta o atributo wait com acesso exclusivo
     * 
     * @param valor a afetar no atributo wait
     */
    private static void setWait(boolean valor) {
    	try {
    		semaphore.acquire();
    		wait = valor;
    	} catch (InterruptedException e) {
    	} finally {
    		semaphore.release();
    	}
    }

    /**
     * Devolve o atributo wait com acesso exclusivo
     * 
     * @return o valor do atributo wait
     */
    public static boolean getWait() {
	try {
	    semaphore.acquire();
	    return wait;
	} catch (InterruptedException e) {
	} finally {
	    semaphore.release();
	}
	return false;
    }

    /**
     * Método principal do programa Servidor.
     * 
     * @param args argumentos da linha de comando: 
     * <port> <single s/n> <redirect s/n> <timeout em milisegundos>
     */
    public static void main(String[] args) {
	int port = DEFAULT_PORT;
	// Acede ao parametro se existir
	if (args.length >= 1)
	    port = Integer.parseInt(args[0]);
	if (args.length >= 2)
	    single = args[1].toUpperCase().equals("S");
	if (args.length >= 3)
	    redirect = args[2].toUpperCase().equals("S");
	if (args.length >= 4)
	    timeout = Integer.parseInt(args[3]);
	if (single) 
	    System.out.println("Servidor em modo single-game (jogo único)!");
	else 
	    System.out.println("Servidor em modo de muli-game (vários jogos)!");
	if(timeout==0)
	    System.out.println("Servidor sem timeout no acesso!");
	else
	    System.out.println("Servidor com timeout no acesso: "+timeout+" milisegundos!");
	// Faz redirecionamento de output para ficheiro "server.log"
	if (redirect) {
	    System.out.println("O output vai ser redirecionado para o ficheiro server.log...");
	    PrintStream out = null;
	    try {
		FileOutputStream fileOutputStream = new FileOutputStream(XMLDoc.getContexto() + "server.log");
		out = new PrintStream(fileOutputStream, true, "UTF-8");
		System.setOut(out);
	    } catch (IOException e) {
     	         System.out.println("Não foi possivel redirecionar o output para o ficheiro: "+XMLDoc.getContexto()+"server.log");
		 // Ignora o erro, pode haver divergencia no caminho relativo/absoluto
	    }
	}
	
	// Lança a tarefa que responde ao protocolo de descoberta
	int p = peer.P2P.answerDiscovery(() -> getWait());
	System.out.println("Atende o protocolo de descoberta no porto: "+p+"...");
	
	// Contém sockets de jogadores autenticados
	FIFOJogador fIFOJogador = new Servidor().new FIFOJogador();
	
	// Tarefa para ir descarregando os jogadores autenticados da fila
	// Arranja oponente e lança uma tarefa para gerir os jogo
	new Thread(() ->
	{ for(;;) { 	// Consome jogadores da fila e lança jogos
	    Socket Sk1=null;
	    Socket Sk2=null;
	    
	    try {
	    	Sk1 = fIFOJogador.remove();
	    	setWait(true);
	    	Sk2 = fIFOJogador.remove();
	    	setWait(false);
	    } catch (InterruptedException e) {
	    	System.out.println("(remove) Interrupção da tarefa corrente!");
	    	// e.printStackTrace();
	    }
   
	    // Lança a tarefa para executar a interação com os jogadores
	    Thread jogo = new ServidorDedicado(Sk1, Sk2);
	    
	    jogo.start(); 
	    
	    // Se for modo jogo único, só faz um jogo, termina o servidor
	    if (single) { 
	    	try {
	    		jogo.join();
	    	} catch (InterruptedException e) {}
	    	
	    	System.out.println(">>>Saiu do servidor devido ao modo de jogo único!");
	    	System.exit(0); // Sai da aplicação
	    }	
		}} ).start();
	
		// lançar tarefa que processa os sockets com utilizadore autenticados
		// Cria um socket servidor na porta especificada
		try (ServerSocket serverSocket = new ServerSocket(port)) {
	    
			System.out.println("Servidor TCP concorrente iniciado...");
			System.out.println("Aguarda ligação no porto: " + port + "...");
			// Loop infinito para aguardar ligações de clientes
	    
			for (;;) {
				// Espera ligação do jogador
				System.out.println("Espera por um Jogador...");
				serverSocket.setSoTimeout(timeout); // tempo de inatividade sai
				Socket newSock = serverSocket.accept();
				System.out.println("Aceitou ligação do Jogador: "+newSock);
				
				try {
					fIFOJogador.add(newSock);
				} catch (InterruptedException e) {
					System.out.println("(add) Interrupção da tarefa corrente!");
				}    
			}
		} catch (IOException e) {
			System.err.println("Exceção no servidor: " + e.getLocalizedMessage());
		}
    } // fim main

    // classe para gerir fifo "First Out" ou "Primeiro a entrar, Primeiro a sair" 
    // com os jogadores autenticados que esperam por oponente
    private final class FIFOJogador {
      private final BlockingQueue<Socket> queue = new LinkedBlockingQueue<>();
      String simbolo="X ";

      public void add(Socket element) throws InterruptedException {

	  new Thread(() -> {
	      // Autentica e acrescenta na fila
	      // Em caso de falha fecha o socket

		  
	      try {
	    	  BufferedReader in = new BufferedReader(new InputStreamReader(element.getInputStream()));
	    	  String inputLine;
				for (;;) { 
					if ((inputLine = in.readLine()) != null) {
						System.out.println("Recebi -> " + inputLine);
						
						if(inputLine.startsWith("<metodo><user")) {
							SrvStub.runNewUser(element, inputLine);
						}
						else if (inputLine.startsWith("<metodo><iniciar")) {
							SrvStub.runIniciar(element, simbolo, inputLine);
							break;
						}else if (inputLine.startsWith("<metodo><login")) {
							SrvStub.runLogin(element, inputLine);
						}else if (inputLine.startsWith("<metodo><perfil")) {
							SrvStub.runPerfil(element, inputLine);
						}else if (inputLine.startsWith("<metodo><changePass")) {
							SrvStub.runChangePass(element, inputLine);
						}else if (inputLine.startsWith("<metodo><changePic")) {
							SrvStub.runChangePic(element, inputLine);
						}
					}
				}
	    	  
				queue.put(element);
				simbolo = ((simbolo.equals("X ")) ? "O " : "X ");
	      } catch (Exception e) {
	    	  System.out.println(e.getLocalizedMessage());
	    	  try {
	    		  element.close();
	    	  } catch (IOException e1) {}
	      }
	  }).start();

      }

      public Socket remove() throws InterruptedException {
        return queue.take();
      }
    }
} // end Servidor
