package peer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import util.GeradorIP;
import util.XMLDoc;

/**
 * Implementa protocolo de descoberta de uma arquitetura peer-to-peer
 * O protocolo de descoberta é baseado em UDP
 * Permite fazer broadcast e registar os IPs que responderam também por UDP
 */
public class P2P {
    // Endereço IP de broadcast
    private static String broadcastAddress = "255.255.255.255";  //"192.168.1.255";
    // Porto usado para esperar mensagens de descoberta
    // Usado em UDP e TCP (não pode ser igual ao porto do servidor=5025)
    private static int portoDescoberta = 5026; 	
    // Porto UDP para receber respostas a mensagens de descoberta
    private static int portoUDPResposta = 5025; // só usado em UDP 
    private static String msgDescoberta = "<metodo><descobrir/></metodo>";

    // Cria um ArrayList com endereços de peers que vão ser acedidos diretamente.
    private static List<String> directPeers = new ArrayList<>(
    		// manter sempre o localhost para ligar sempre que ele responder
    	    Arrays.asList("pfenvy", "msi", "192.168.1.8", "anotherhost"));
    // Cria fila "First Out" para armazenar os peers que responderam.
    private static BlockingQueue<String> respondingPeers = new LinkedBlockingQueue<>();
    // Indica que está presente um servidor local
    private static boolean isPresentLocalServer=false;
        
    /**
     * @return a indicação se existe servior local
     */
    public static boolean isPresentLocalServer () {
    	return isPresentLocalServer;
    }
    
    /**
     * Percorre a lista dos sockets indicada em parametro 
     * envia diretamente por TCP uma mensagem de descoberta
     * para os endereços indicados na lista
     * @param direct Lista de peers a procurar
     */
    public static void sendDirect (List<String> direct) {
	System.out.println("TCP: procura os peers na lista:");
	if(direct.size()==0)
	    return;
	for (String IP : direct) {// lança uma tarefa para cada endereço
	    new Thread(() ->  { 
	    
	    System.out.println("À espera do peer '"+IP+"'...");
	    if(pingTCP(IP)) {
		if (!respondingPeers.contains(IP)) { // sem duplicados
		    try {
			if(!isPresentLocalServer && isLocal(InetAddress.getByName(IP).getHostAddress())) {
			    System.out.println("O peer "+IP+" é reconhecido como local!");
			    isPresentLocalServer=true;
			}
			respondingPeers.put(IP);
		    } catch (InterruptedException | UnknownHostException e) {
			e.printStackTrace();
		    }	
		    System.out.println("Acrescenta o peer '"+IP+"' na lista!");
		}
	    } 
	    
	    } ).start(); 
	}
    }
    
	/**
	 * Faz broadcast da mensagem de descoberta
	 */
    public static void sendBroadcast() {
    	try {
    	    sendMsgUDP(broadcastAddress, portoDescoberta, msgDescoberta);
	} catch (IOException e) {
		e.printStackTrace();
	}
    }
    
	/**
	 * Envia para o endereço IP a mensagem indicada em argumento
	 * @param IP   		Enderço IP da máquina de destino
	 * @param porto 	Porto da máquina de destino
	 * @param mensagem     	Mensagem que vai ser enviada
	 * @throws IOException  Em caso de erro
	 */
	public static void sendMsgUDP(String IP, int porto, String mensagem) throws IOException {
		try (DatagramSocket socket = new DatagramSocket()){
			byte[] outputBuffer=mensagem.getBytes();
			DatagramPacket pacote = 
				new DatagramPacket(outputBuffer, outputBuffer.length,
					InetAddress.getByName(IP), porto);
			socket.send(pacote);
		} 
	}
	
	/**
	 * Envia para a máquina com o endereço IP a mensagem indicada em argumento
	 * @param IP   		Enderço IP da máquina de destino
	 * @return 		Indica se no destino está oponente à espera
	 */
	public static boolean pingTCP(String IP) {
		BufferedReader is = null;
		PrintWriter os = null;
		Socket socket = null;
		try { // Cria um socket para se ligar ao servidor
			socket = new Socket(IP, portoDescoberta);
			// Cria um stream para escrever dados no socket
			os = new PrintWriter(socket.getOutputStream(), true);
			// Cria um stream para ler dados do socket
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
   		        os.println(msgDescoberta);
   		        // montar o timeout
   		        
		        String msgIn = is.readLine();
		        registaLog(socket.getRemoteSocketAddress()+"<-{"+msgIn+"}");
			Document m = XMLDoc.parseString(msgIn);
			validXSD(m);
		        boolean wait=((Element) m.getElementsByTagName("descobrir").item(0)).getAttribute("wait").equals("true");
		        System.out.println("<< TCP: o peer '"+IP+"' responde wait='"+wait+"'.");
		        if(!wait)
		            System.out.println("<< TCP: o peer '"+IP+"' não tem oponente à espera!");
			else
			    System.out.println("<< TCP: o peer '"+IP+"' tem oponente à espera!");
		        return wait;  // existe oponente à espera
		} catch (Exception e) {
		    // sem problema, o peer pode não estar disponínvel
		    return false;
		} finally {
		    try {
			if(socket!=null) {
			    socket.close();
			    is.close();
			    os.close();
			}
		    } catch (IOException e) {
		    }

		}
	}
    /**
     * @param ficheiro  	Destino
     * @param texto		A adicionar o ficheiro
     * @throws IOException 	Se houver erro
     */
    private static void adicionarStringFicheiro(String ficheiro, String texto) throws IOException {
        try (
            // Cria um PrintWriter para escrever no ficheiro.
            PrintWriter escritor = new PrintWriter(new BufferedWriter(new FileWriter(ficheiro, true)))) {

            // Escreve a string no ficheiro.
            escritor.println(texto);
        }
    }
    
    /**
     * @param evento 		mensagem a resgistar
     * @throws IOException	em caso de erro
     */
    private static void registaLog(String evento) throws IOException {
    	adicionarStringFicheiro(XMLDoc.getContexto()+"protocolo.log", LocalDateTime.now()+" - "+evento.replaceAll("\n",""));
    }
    
    /**
     * Valida se o documento XML recebido representa um método válido.
     *
     * @param doc Documento XML a ser validado.
     * @throws Exception Se o documento não for válido.
     */
    private static void validXSD(final Document doc) throws Exception {
        try {
            // Valida o documento contra o XSD "metodos.xsd".
            XMLDoc.validDocXSD(doc, XMLDoc.getContexto() + "discover.xsd");
        } catch (SAXException | IOException e) {
            throw new Exception("Recebeu mensagem inválida: " + e.getLocalizedMessage());
        }
    }
    
	/**
	 * Aceita mensagens de resposta ao protocolo de descoberta
	 *  e regista os peers que responderam em 'respondingPeers'
	 * 
	 * @param timeout milisegundos de espera entre respotas
	 */
	public static void listen(int timeout){
	    // Está presente um servidor local.
	    isPresentLocalServer=false;
	    int n=0;
	    while (true) 
	        try (
	            // Cria um DatagramSocket para receber mensagens de resposta.
	            DatagramSocket socket = new DatagramSocket(portoUDPResposta); 
		    // Cria um DatagramSocket para enviar a resposta.
		    DatagramSocket responseSocket = new DatagramSocket();	
	            ){

	    	    // Cria um buffer para armazenar a mensagem recebida.
	    	    byte[] request = new byte[1024];
	            
	    	    // Recebe resposta a uma mensagem de descoberta.
	            DatagramPacket packet = new DatagramPacket(request, request.length);
	    	    
	            // Define o tempo limite para a operação de recepção.
	    	    socket.setSoTimeout(timeout);
	            
	    	    System.out.println("UDP: à espera de respostas...");
	            socket.receive(packet);

	            // Obtém o endereço IP e o porto do remetente.
	            InetAddress senderAddress = packet.getAddress();

	            // Converte a mensagem recebida em String.
		    String message = new String(packet.getData(), 0, packet.getLength());
		    String host=senderAddress.getHostName();
		    boolean wait=false;
		    try {
		         	registaLog(host+"<-{"+message+"}");
		         	Document m = XMLDoc.parseString(message);
		         	validXSD(m);
		         	wait=((Element) m.getElementsByTagName("descobrir").item(0)).getAttribute("wait").equals("true");
			} catch (Exception e) {
			    	System.out.println("Erro no protocolo de descoberta: "+e.getLocalizedMessage());
			}
		    if(!isPresentLocalServer && isLocal(InetAddress.getByName(host).getHostAddress())) {
		        	System.out.println("O peer "+host+" é reconhecido como local!");
		        	isPresentLocalServer=true;
		    }
		    if(wait) {
		           
			    if (!respondingPeers.contains(host)) { // sem duplicados
				 System.out.println("UDP: acrescenta o peer "+host+" na lista!");
				 try {
				    respondingPeers.put(host);
				} catch (InterruptedException e) {
				    e.printStackTrace();
				}	
        		         n++;
			    }
		     } 
		     System.out.println("<< UDP: o peer '"+host+"' responde wait='"+wait+"'.");
		     if(!wait)
			 System.out.println("<< UDP: o peer '"+host+"' não tem oponente à espera!");
		     else
			 System.out.println("<< UDP: o peer '"+host+"' tem oponente à espera!");
	           } catch (SocketTimeoutException e) {
	            // Timeout expirado, interrompe o loop.
	        	if(n==0)
	        	    System.out.println("UDP: não foi encontrado nenhum oponente em espera!");
	        	else
	        	    if(n==1)
	        		System.out.println("UDP: foi encontrado um oponente!");
	        	    else
	        		System.out.println("UDP: foram encontrados '"+n+"' oponentes!");
	        		
	            break;
	        } catch (IOException e) {
	            // Erro na comunicação, ignora e continua.
	            e.printStackTrace();
	        }
	}

	/**
	 * Mostra os endereços IP que responderam
	 */
	public static void printPeers() {
		if(respondingPeers.size()==0) {
		    System.out.println("Lista de peers com oponentes em espera vazia.");
			return;
		}
		System.out.println("Lista de peers que responderam: ");
		int linha=0;
	    for (String IP : respondingPeers) 
	        // Exibe as informações do socket.
	        System.out.println(++linha+" - " + IP);
	    System.out.println("Fim da lista.");
	}
	
	// Constroí a mensagem de resposta ao peotocolo de descoberta
	private static String msgOut(BooleanSupplier getWait) {
		boolean wait=getWait.getAsBoolean();
		return "<metodo><descobrir wait='" + wait + "'/></metodo>";
	}
	/**
	 * Para ser usado uma vez num servidor.
	 * Responde continuamente ao protocolo de descoberta por UDP e TCP.
	 * @param getWait 	Metodo  que indica se o servidor está á espera de 2º Jogador
	 * @return 		Porto usado no protocolo de descoberta
	 */
	public static int answerDiscovery(BooleanSupplier getWait) {
	    discoveryUDP(getWait);
	    discoveryTCP(getWait);
	    return portoDescoberta;
	}
	
	/**
	 * Responde continuamente ao protocolo de descoberta por UDP
	 * @param getWait metodo que indica se o servidor está á espera de 2º Jogador
	 */
	private static void discoveryUDP(BooleanSupplier getWait) {
		new Thread(() -> {
		int erro=0;
		while (true) {
			try (// Cria um DatagramSocket para receber mensagens de broadcast.
				DatagramSocket socket = new DatagramSocket(portoDescoberta);
				// Cria um DatagramSocket para enviar a resposta.
				DatagramSocket responseSocket = new DatagramSocket()) {
				// Cria um buffer para armazenar a mensagem recebida.
				byte[] request = new byte[1024];
				// Recebe uma mensagem de broadcast.
				DatagramPacket requestPacket = new DatagramPacket(request, request.length);
				System.out.println("Descoberta: espera por mensagens UDP no porto: "+portoDescoberta);
				socket.receive(requestPacket);

				// Converte a mensagem recebida em String.
				String msgIn = new String(requestPacket.getData(), 0, requestPacket.getLength());
				System.out.println("UDP: recebeu mensagem de descoberta de:"+requestPacket.getSocketAddress());
				
				// Regista a mensagem recebida com dados do remetente
				registaLog(requestPacket.getAddress().getHostName()+"<-{"+msgIn+"}");
				// System.out.println("recebeu: "+msgIn);
				Document m;
				try {
					m = XMLDoc.parseString(msgIn);
					// Valida mensagem recebida
					validXSD(m);
					if(m.getElementsByTagName("descobrir").getLength()==0)
						System.err.println("Descoberta: mensagem sem 'descobrir': "+msgIn);
				} catch (Exception e) {
					System.err.println("Descoberta: mensagem não reconhecida: "+msgIn);
					continue;
					// e.printStackTrace();
				}

				// Envia uma resposta para o remetente.
				byte[] response = msgOut(getWait).getBytes();
				DatagramPacket responsePacket = 
				new DatagramPacket(response, response.length, 
					requestPacket.getAddress(), portoUDPResposta);
				// System.out.println("Envia resposta ao protocolo de descoberta: "+msgIn);
				responseSocket.send(responsePacket);
				erro=0;
			} catch (IOException e) {
				// Erro na comunicação
				e.printStackTrace();
				if(++erro==10)
				    break;
				else
				    continue;  // podia entrar em loop
			}
		}
		}).start();
	}

	/**
	 * Responde continuamente ao protocolo de descoberta por TCP.
	 * @param getWait metodo que indica se o servidor está á espera de 2º Jogador
	 */
	private static void discoveryTCP(BooleanSupplier getWait) {
		new Thread(() -> {
		    
		try (	// Cria um Socket servidor para esperar mensagem de descoberta.
			ServerSocket serverSocket = new ServerSocket(portoDescoberta)){  		    
		int erro=0;
		for(;;) {
		    	System.out.println("Descoberta: espera por mensagens TCP no porto:"+portoDescoberta);
			try (

				// Estabelecer circuito virtual com o outro peer
				Socket newSock= serverSocket.accept();
				// Cria streams para leitura e escrita de dados no socket
				BufferedReader is = new BufferedReader(new InputStreamReader(newSock.getInputStream()));
				PrintWriter os = new PrintWriter(newSock.getOutputStream(), true)
			    )
			    {			    
				String msgIn = is.readLine();
				System.out.println("TCP: recebeu mensagem de descoberta de:"+newSock.getRemoteSocketAddress());
				// Regista a mensagem recebida com endereço IP dp remetente
				registaLog(newSock.getRemoteSocketAddress()+"<-{"+msgIn+"}");
				Document m;
				try {
					m = XMLDoc.parseString(msgIn);
					// Valida a mensagem recebida
					validXSD(m);
					if(m.getElementsByTagName("descobrir").getLength()==0)
						System.err.println("Descoberta: mensagem sem 'descobrir': "+msgIn);
				} catch (Exception e) {
					System.err.println("Descoberta: mensagem não reconhecida: "+msgIn);
					continue;
				}
				os.println(msgOut(getWait));
				erro=0;
			} catch (IOException e) {
				// Erro na comunicação
				e.printStackTrace();			
				if(++erro==10)
				    break;
				else
				    continue;  // podia entrar em loop
			}
		}// termina o for
    		} catch (IOException e) {
    		    e.printStackTrace();
    		}
		System.out.println("Saiu: discoveryTCP");
		}).start();
	}
	
	/**
	 * Lança tarefas que vão popular a fila respondingPeers
	 */
	public static void findHosts() {
	// Limpar a fila de peers que responderm.
	respondingPeers.clear();
	// Se for true não lança servidor local
	isPresentLocalServer=false;
	// Preenche a lista de procura com todos os endereços da rede
	// Pode ser problematico!
	for(int i=0; i<254; i++)
	    try {
		directPeers.add(GeradorIP.gerarEnderecoIP(i));
	    } catch (UnknownHostException e) {
		e.printStackTrace();
	    }
	// 2 segundos de espera entre a receção de respostas
	// não tem limite de respostas, 
	// 	mas como é na rede local não são demasiadas!
        Thread listenBroadcast 	= new Thread(() ->  listen(1000*2));  
	listenBroadcast.start();  	// atualiza a fila: respondingPeers;
	
        Thread sendDirect1 	= new Thread(() ->  sendDirect(directPeers));
	sendDirect1.start(); 		// atualiza a fila: respondingPeers;
	
        Thread sendBroadcast 	= new Thread(() ->  sendBroadcast());
	sendBroadcast.start();
		
	}
	
    /**
     * @param ip    	Endereço de IP que vai ser testado
     * @return		Devolve true se for IP da máquina local
     */
    public static boolean isLocal(String ip) {
		try {
		     String host=InetAddress.getLocalHost().getHostName();
		     InetAddress[] addresses = InetAddress.getAllByName(host);
    	             for (int i = 0; i < addresses.length; ++i) 
    	               if (addresses[i].getHostAddress().equals(ip))
    	          	 return true;
		} catch (UnknownHostException e) {
			return false;
		}
        return false;
    }
    
    
    /**
     * @return o primeiro peer da fila
     */
    public static String getFirst() {
	try {
	    // return respondingPeers.take();
	    return respondingPeers.poll(5, TimeUnit.SECONDS);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	return null;
    }
    /**
     * @param args				Não é usado
     * @throws IOException			Em caso de erro
     * @throws InterruptedException 		Em caso de erro
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        	findHosts();
        	String host = P2P.getFirst();
        	if(host!=null) 
        	    System.out.println(">> O primeiro peer é: "+host);
        	P2P.printPeers();	     // Mostra outros peers responderam

    }
}
