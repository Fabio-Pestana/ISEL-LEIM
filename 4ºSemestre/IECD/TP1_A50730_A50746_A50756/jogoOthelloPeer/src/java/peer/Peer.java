package peer;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Classe Peer, simula um cliente junto com um servidor.
 *
 */
public class Peer {
	/**
	 * Porta padrão do servidor.
	 */
	private final static int DEFAULT_PORT = 5025;
	
	// Valor de timeout sem atividade termina o servidor
	// Tempo máximo de espera por um oponente 90 segundos
	private static int timeout = 1000*90; 
	
	// Para ler o teclado
	private static Scanner tecla = new Scanner(System.in);

	// Executa o servidor como processo {true} ou tarefa {false}
	private static void runServer(boolean processo) {
		if(processo)
			runServerProcess();
		else
			runServerThread();
	} 
	
	// Executa o cliente que se liga ao servidor indicado em parametro
	private static void runClient(String host) {
		System.out.println("Peer: lança o cliente...");
		new Thread(() ->
				client.Jogador.main(new String[] {host==null?"localhost":host, Integer.toString(DEFAULT_PORT) })
		).start();
	}
	
	// Executa o servidor como tarefa
	private static void runServerThread() {
		System.out.println("Peer: lança a tarefa com servidor em modo de jogo único.");
		// <port> <single s/n> <redirect s/n> <timeout em milisegundos>
		new Thread(() ->
			server.Servidor.main(new String[] 
		{Integer.toString(DEFAULT_PORT),"S", "N","N",Integer.toString(timeout)})).start();
		
	}
		
	// Executa o servidor como processo
	private static void runServerProcess() {
		System.out.println("Peer: lança o processo com servidor em modo de jogo único.");
		try {
			File file = new File(".");
			String absolutePath = file.getAbsolutePath();
			absolutePath = absolutePath.substring(0, absolutePath.length() - 1); // remove o ponto
			// Definir a diretoria de trabalho no ambito do projeto eclipse
			String workingDir =  absolutePath + File.separator + "build" + File.separator + "classes"+ File.separator;
			System.out.println("Peer: diretoria de trabalho: "+workingDir);
			// Criar um ProcessBuilder
			System.setProperty("PROCESS_NAME", "Servidor do Jogo do Galo");
			
			// formato do comando de linha: <port> <single s/n> <redirect s/n> <timeout em milisegundos>
	        ProcessBuilder pb = new ProcessBuilder("java","-cp",workingDir,"server.Servidor", 
	        Integer.toString(DEFAULT_PORT), "S","S",Integer.toString(timeout));
	        
	        //pb.directory(new File(workingDir));
	        // Redireciona 'stdin', "stdout" e stderr para o processo que o lançou
		    // pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
	        	// pode já ter sido redirecionado para ficheiro
	        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);  
	        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
	        
		    // Iniciar o processo
		    pb.start();
		    System.out.println("Peer: lançou o processo servidor.");
		} catch (IOException e) {
			System.out.println("Peer: ocorreu exceção no lançamento do processo servidor!");
		    e.printStackTrace();
		}
	}
	/**
	 * Método principal do programa.
	 * 
	 * @param args argumentos da linha de comando (não utilizado)
	 */
	public static void main(String[] args) {
		
		P2P.findHosts();	// Atualiza a fila de peers
        	String host = P2P.getFirst();
        	if(host!=null) 
        	    System.out.println(">> O primeiro peer é: "+host); 
        	P2P.printPeers();	     // Mostra outros peers responderam
		if(P2P.isPresentLocalServer())
		    System.out.println("Peer: existe um servidor local presente/ativo...");
		
		// Se não existir nenhum peer remoto, lança local
		if(!P2P.isPresentLocalServer() && host==null)
			runServer(true);  // lança processo {true}/tarefa {false} como servidor
		else
			System.out.println("Peer: vai ligar ao servidor: "+(host==null?"localhost":host));
		try {
			TimeUnit.SECONDS.sleep(1);   // Pausa de 1 segundo para o servidor ficar ok.
						     // Fazer pausa não é um mecanismo de sincronização
						     // Este código tem de ser revisto
		} catch (InterruptedException e) {

		} 
		// lança o cliente do peer
		System.out.println(">>>>Prima qualquer tecla para lançar o cliente...");
		tecla.nextLine();

		runClient((host==null)?"localhost":host);
	} // fim do método main
} // fim da classe Peer
