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
						}else if (inputLine.startsWith("<metodo><ranking")) {
							SrvStub.runRanking(element, inputLine);
						}else if (inputLine.startsWith("<metodo><favColor")) {
							SrvStub.runGetFavoriteColor(element, inputLine);
						}else if (inputLine.startsWith("<metodo><changeColor")) {
							SrvStub.runChangeColor(element, inputLine);
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
