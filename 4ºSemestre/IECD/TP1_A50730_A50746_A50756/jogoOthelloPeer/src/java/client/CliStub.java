package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import user.User;
import util.MyImage;
import util.XMLDoc;


public class CliStub implements AutoCloseable {


	private BufferedReader is = null; // ler mensagens da Socket
	
	private PrintWriter os = null; // Escrever mensagens na Socket
	
	// dados do jogador atual, conhecido após autenticação
    private Document registo=null;

    // Tamanho do jogo de tabuleiro  
    private final int BOARD_SIZE = 8;
	
    
    /**
     * Construtor da classe CliStub.
     * 
     * @param sk Socket utilizada para a comunicação com o servidor.
     * @throws IOException Exceção lançada em caso de erro de I/O.
     */
	public CliStub(Socket sk) throws IOException {
		// Cria um BufferedReader para ler do socket.
		is = new BufferedReader(new InputStreamReader(sk.getInputStream()));
		// Cria um PrintWriter para escrever no socket.
		os = new PrintWriter(sk.getOutputStream(), true);
	}
	
	/**
	 * Fecha a comunicação com o servidor
	 */
	@Override
	public void close() {
		
		// Fecha o BufferedReader.
		try {
			is.close();
		} catch (IOException e) {}
		
		// Fecha o PrintWriter.
		os.close();
	}

	/**
     * Converte o tabuleiro de jogo em uma representação de texto.
     * 
     * @param tabuleiro Elemento contendo o tabuleiro de jogo.
     * @return Representação em texto do tabuleiro de jogo.
     */
	public String tabuleiroToTXT(final Element tabuleiro) {
		String out = "---------------------\n";
		NodeList casas = tabuleiro.getElementsByTagName("casa");
		int k = 0;
		for (int i = 0; i < BOARD_SIZE; i++) {
			
			if (i == 0) {
                for (int l = 0; l < BOARD_SIZE; l++) {
                    if (l == 0) {
                        out+= ("    " + (l + 1));
                    } else {
                    	out+= (" " + (l + 1));
                    }
                }
    			out += "\n";
            }
            out+= ("  " + (i + 1) + " ");
			
			for (int j = 0; j < BOARD_SIZE; j++) {
				// Obtém o símbolo da casa atual.
				String simbolo = ((Element) casas.item(k++)).getAttribute("simbolo");
				out += simbolo;
			}
			// Adiciona uma nova linha após cada linha de casas.
			out += "\n";
		}
		out += "---------------------\n";
		return out;
	}

	/**
     * Converte o estado do jogo para uma mensagem de texto.
     * 
     * @param valor Abreviatura do estado do jogo.
     * @return Mensagem representando o estado do jogo.
     */
	public String estadoToTXT(final String valor) {
		switch (valor) {
		case "VO":
			return "Vitória do O!";
		case "VX":
			return "Vitória do X!";
		case "EM":
			return "Empate.";
		case "SJ":
			return "Sem Jogadas disponiveis!";
		case "IV":
			return "Jogada inválida!";
		default:
			return "";
		}
	}
	
	/**
     * Converte o estado final do jogo para um código.
     * 
     * @param valor Abreviatura do estado final do jogo.
     * @return Código representando o estado final do jogo.
     */
	public int estadoFinal(final String valor) {
		switch (valor) {
		case "VO":
			return 1;
		case "VX":
			return 2;
		case "EM":
			return 3;
		default:
			return 0;
		}
	}
	
	/**
     * Valida um documento XML contra um XSD.
     * 
     * @param doc Documento XML a ser validado.
     * @throws Exception Exceção lançada se a validação falhar.
     */
    private void validXSD(final Document doc) throws Exception {
        try {
            // Valida o documento contra o XSD.
            XMLDoc.validDocXSD(doc, XMLDoc.getContexto() + "othello-metodos-cli.xsd");
        } catch (SAXException | IOException e) {
            throw new Exception("Recebeu mensagem inválida: " + e.getLocalizedMessage());
        }
    }
    
    /**
     * Adiciona uma string a um arquivo de texto.
     * 
     * @param ficheiro Nome do arquivo de texto.
     * @param texto String a ser adicionada ao arquivo.
     */
    private static void adicionarStringFicheiro(String ficheiro, String texto) {
        try (
            // Cria um PrintWriter para escrever no ficheiro.
            PrintWriter escritor = new PrintWriter(new BufferedWriter(new FileWriter(ficheiro, true)))) {

            // Escreve a string no ficheiro.
            escritor.println(texto);
        } catch (IOException e) {}
    }
    
    
    /**
     * Registra um evento em um arquivo de log.
     * 
     * @param evento Evento a ser registrado.
     */
    private static void registaLog(String evento) {
    	adicionarStringFicheiro(XMLDoc.getContexto()+"protocolo.log", LocalDateTime.now()+" - "+evento.replaceAll("\n",""));
    }
    
    
    /**
     * Obtém um elemento do documento XML pelo nome da tag.
     * 
     * @param tag Nome da tag.
     * @return Elemento correspondente à tag, se encontrado; caso contrário, null.
     */
    private Element getElement(String tag) {
    	NodeList itens = registo.getElementsByTagName(tag);
    	if(itens.getLength()==1)
    		return (Element)itens.item(0);
    	return null;
    }
    
    /**
     * Obtém o texto de um elemento do documento XML pelo nome da tag.
     * 
     * @param tag Nome da tag.
     * @return Texto do elemento correspondente à tag, se encontrado; caso contrário, uma string vazia.
     */
    private String getText(String tag) {
    	Element el = getElement(tag);
    	if(el==null)
    		return "";
    	else
    		return el.getTextContent();
    }
    
    /**
     * Mostra todos os dados do jogador autenticado
     */
    public void print() {
    	
    	if(registo==null) {
    		System.out.println("Não existe jogador autenticado!");
    		return;
    	}
    	System.out.println("----------------- Jogador '"+getElement("jogador").getAttribute("simbolo")+"' -----------------");
    	System.out.println("Nome de utilizador: " + getText("username"));
    	String birthdate=getText("birthdate");
	
    	if (!birthdate.isBlank()) {
    		System.out.println("Data de nascimento: " + birthdate);
    		System.out.println("Idade: " + getText("age"));
    	}
	
    	System.out.println("Nacionalidade: " + getText("nationality"));
    	System.out.println("Número de vitórias: " + getText("vitorias"));
    	System.out.println("Número de derrotas: " + getText("derrotas"));
    	System.out.println("Numero de jogos: " + getText("jogos"));
    	String tempo = millisConverted(Long.parseLong(getText("tempo")));
    	System.out.println("Tempo total gasto em jogos: " + tempo);
    	String tempoMedio = millisConverted(Long.parseLong(getText("timeAverageInGame")));
    	System.out.println("Tempo médio por jogo jogos: " + tempoMedio);
    	System.out.println("-------------------------------------------------------------------");
    }
    
    
    /**
     * Inicia a sessão de jogo com um determinado usuário.
     * 
     * @param user Nome do usuário.
     * @return Símbolo do jogador.
     * @throws IOException Exceção lançada em caso de erro de I/O.
     * @throws Exception Exceção genérica lançada em caso de erro.
     */
	public String iniciar(final String user) throws IOException, Exception {

		os.println("<metodo><iniciar nickname='" + user 
					   + "'/></metodo>");

		String resposta = is.readLine();
		registaLog("Cliente{"+resposta+"}");
		if(resposta==null)
			throw new Exception("Ligação ao servidor cancelada remotamente!");
		
		// Processa a resposta como um documento XML.
		registo = XMLDoc.parseString(resposta);
		validXSD(registo);
		
		NodeList jogadores = registo.getElementsByTagName("jogador");
	
		return ((Element)jogadores.item(0)).getAttribute("simbolo");
	}
	
	
	/**
     * Realiza o login de um usuário.
     * 
     * @param user Nome do usuário.
     * @param pass Senha do usuário.
     * @return Verdadeiro se o login for bem-sucedido; caso contrário, falso.
     * @throws IOException Exceção lançada em caso de erro de I/O.
     * @throws Exception Exceção genérica lançada em caso de erro.
     */
	public boolean login(final String user, final String pass) throws IOException, Exception {

		os.println("<metodo><login nickname='" + user 
					   + "' senha='" + pass + "'/></metodo>");

		String resposta = is.readLine();
		registaLog("Cliente{"+resposta+"}");
		if(resposta==null)
			throw new Exception("Ligação ao servidor cancelada remotamente!");
		
		Document d = XMLDoc.parseString(resposta);
		validXSD(d);
		
		Element value = (Element) d.getElementsByTagName("login").item(0);
		
		boolean sucessLog = Boolean.parseBoolean(value.getAttribute("value")); // "true" -> true 
	
		return sucessLog;
	}
	
	 /**
     * Cria um novo usuário.
     * 
     * @param user Objeto contendo os dados do novo usuário.
     * @throws IOException Exceção lançada em caso de erro de I/O.
     * @throws Exception Exceção genérica lançada em caso de erro.
     */
	public void newUser(final User user) throws IOException, Exception {
		
		os.println("<metodo><user username='" + user.getUsername() 
		+ "' password='"+ user.getOrgPassword() +
		 "' birthdate='"+ User.dateToXsd(user.getBirthdate()) +
		 "' profilePic='"+ user.getProfilePic().getBase64() +
		 "' nationality='"+ user.getNationality() +
				"'/></metodo>");

		
		String resposta = is.readLine();
		registaLog("Cliente{"+resposta+"}");
		
		if(resposta==null)
			throw new Exception("Ligação ao servidor cancelada remotamente!");
		
		registo = XMLDoc.parseString(resposta);
		validXSD(registo);
	}
	
	
	/**
     * Obtém o perfil de um usuário.
     * 
     * @param username Nome do usuário.
     * @return String contendo o perfil do usuário.
     * @throws IOException Exceção lançada em caso de erro de I/O.
     * @throws Exception Exceção genérica lançada em caso de erro.
     */
	public String seePerfil(String username) throws IOException, Exception {
		
		os.println("<metodo><perfil username='" + username + "'/></metodo>");

		String resposta = is.readLine();
		registaLog("Cliente{"+resposta+"}");
		
		if(resposta==null)
			throw new Exception("Ligação ao servidor cancelada remotamente!");
		
		Document d = XMLDoc.parseString(resposta);
		validXSD(d);
		
		Element perfilElem = (Element) d.getElementsByTagName("perfil").item(0);
		
		String perfil = PerfilString(perfilElem);
		
		return perfil;
	}

	/**
     * Altera a senha de um usuário.
     * 
     * @param username Nome do usuário.
     * @param oldPass Senha antiga do usuário.
     * @param newPass Nova senha do usuário.
     * @return Verdadeiro se a senha for alterada com sucesso; caso contrário, falso.
     * @throws IOException Exceção lançada em caso de erro de I/O.
     * @throws Exception Exceção genérica lançada em caso de erro.
     */
	public boolean changePass(String username, String oldPass, String newPass) throws IOException, Exception {
	
		os.println("<metodo><changePass username='" + username + "' oldPass='" + oldPass +
			"' newPass='" + newPass + "'/></metodo>");

		
		String resposta = is.readLine();
		registaLog("Cliente{"+resposta+"}");
	
		if(resposta==null)
			throw new Exception("Ligação ao servidor cancelada remotamente!");
		
	
		// Processa a resposta como um documento XML.
		Document d = XMLDoc.parseString(resposta);
		validXSD(d);
			
		Element value = (Element) d.getElementsByTagName("changePass").item(0);
			
		boolean sucessLog = Boolean.parseBoolean(value.getAttribute("value")); // "true" -> true 
		
		return sucessLog;
	}


	/**
     * Atualiza as informações de um usuário.
     * 
     * @param username Nome do usuário.
     * @param VDE Número de vitórias ou derrotas.
     * @param timeOfPlay Tempo total de jogo.
     * @return Verdadeiro se as informações do usuário forem atualizadas com sucesso; caso contrário, falso.
     * @throws IOException Exceção lançada em caso de erro de I/O.
     * @throws Exception Exceção genérica lançada em caso de erro.
     */
	public boolean updateUser(String username, int VDE, long timeOfPlay) throws IOException, Exception {
	
		os.println("<metodo><updateUser username='" + username + "' VDE='" + VDE +
			"' timeOfPlay='" + timeOfPlay + "'/></metodo>");

	
		String resposta = is.readLine();
		registaLog("Cliente{"+resposta+"}");
	
		if(resposta==null)
			throw new Exception("Ligação ao servidor cancelada remotamente!");
	
		Document d = XMLDoc.parseString(resposta);
		validXSD(d);
			
		
		Element value = (Element) d.getElementsByTagName("updateUser").item(0);
			
		boolean sucessLog = Boolean.parseBoolean(value.getAttribute("value")); // "true" -> true 
		
		return sucessLog;
	}

	/**
     * Altera a imagem de perfil de um usuário.
     * 
     * @param username Nome do usuário.
     * @param foto Nome do arquivo de imagem.
     * @return Verdadeiro se a imagem de perfil for alterada com sucesso; caso contrário, falso.
     * @throws IOException Exceção lançada em caso de erro de I/O.
     * @throws Exception Exceção genérica lançada em caso de erro.
     */
	public boolean changePic(String username, String foto) throws IOException, Exception {
		MyImage ft = new MyImage(XMLDoc.getContexto() + foto);
		String fotoBase64 = ft.getBase64();
	
		os.println("<metodo><changePic username='" + username + 
				"' picture='" + fotoBase64 + "'/></metodo>");

		
		// Recebe a resposta do servidor.
		String resposta = is.readLine();
		registaLog("Cliente{"+resposta+"}");
	
		if(resposta==null)
			throw new Exception("Ligação ao servidor cancelada remotamente!");
	
		// Processa a resposta como um documento XML.
		Document d = XMLDoc.parseString(resposta);
		validXSD(d);
			
		Element value = (Element) d.getElementsByTagName("changePic").item(0);
			
		boolean sucessLog = Boolean.parseBoolean(value.getAttribute("value")); // "true" -> true 
		
		return sucessLog;
	}


	/**
     * Converte o perfil de um usuário em uma string formatada.
     * 
     * @param perfil Elemento contendo o perfil do usuário.
     * @return String formatada contendo o perfil do usuário.
     */
	public String PerfilString(final Element perfil) {
			String out = "---------------------\n\n";
			out += "Nome do Jogador: " + perfil.getAttribute("username");
			out += "\nData de Nascimento: " + perfil.getAttribute("birthdate");
			out += "\nNacionalidade: " + perfil.getAttribute("nationality");
			out += "\nIdade: " + perfil.getAttribute("age");
			out += "\nNúmero de vitórias: " + perfil.getAttribute("vitorias");
			out += "\nNúmero de derrotas: " + perfil.getAttribute("derrotas");
			out += "\nNumero de jogos: " + perfil.getAttribute("jogos");
			String tempo = millisConverted(Long.parseLong(perfil.getAttribute("tempo")));
			out += "\nTempo total gasto em jogos: " + tempo;
			String tempoMedio = millisConverted(Long.parseLong(perfil.getAttribute("timeAverageInGame")));
			out += "\nTempo médio por jogo jogos: " + tempoMedio;

	 
			out += "\n\n---------------------\n";
		
			String photo = perfil.getAttribute("profilePic");
		
			MyImage p = new MyImage();
			p.setBase64(photo);
			try {
				p.view();
			} catch (Exception e) {}

			return out;
	}

	/**
     * Converte um intervalo de tempo em milissegundos para uma string formatada.
     * 
     * @param time Intervalo de tempo em milissegundos.
     * @return String formatada representando o intervalo de tempo.
     */
	private String millisConverted(long time) {
		String total = "";
		
		long dias = time / (1000 * 60 * 60 * 24);
		if(dias != 0)
			total += dias + " dias, ";
		
		long horas = (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		if(horas != 0)
			total += horas + " horas, ";
		
		long minutos = (time % (1000 * 60 * 60)) / (1000 * 60);
		if(minutos != 0)
			total += minutos + " minutos, ";
		
		long segundos = (time % (1000 * 60)) / 1000;
		if(segundos != 0)
			total += segundos + " segundos, ";
		
		long milissegundos = (time % 1000);
		if(milissegundos != 0)
			total += milissegundos + " milissegundos.";
		
		
		return total;
	}
	

	/**
     * Obtém o estado atual do jogo do servidor.
     * 
     * @param symbol Símbolo do jogador.
     * @return Elemento contendo o estado atual do jogo.
     * @throws IOException Exceção lançada em caso de erro de I/O.
     * @throws Exception Exceção genérica lançada em caso de erro.
     */
	public Element obter(String symbol) throws IOException, Exception {
		// Envia a mensagem "obter" para o servidor.
		os.println("<metodo><obter/></metodo>");
		
		// Recebe a resposta do servidor e processa o XML.
		String resposta=is.readLine();
		
		registaLog("Cliente{"+resposta+"}");
		
		if(resposta==null)
			throw new Exception("Ligação ao servidor cancelada remotamente!");
		
		Document d = XMLDoc.parseString(resposta);
		validXSD(d);
		
		// Obtém o elemento "tabuleiro" da resposta.
		Element tabuleiro = (Element) d.getElementsByTagName("tabuleiro").item(0);
		
		// Retorna o elemento "tabuleiro".
		return tabuleiro;
	}

	 /**
     * Realiza uma jogada no jogo.
     * 
     * @param linha Número da linha da jogada.
     * @param coluna Número da coluna da jogada.
     * @throws IOException Exceção lançada em caso de erro de I/O.
     * @throws Exception Exceção genérica lançada em caso de erro.
     */
	public void jogar(int linha, int coluna) throws IOException, Exception {
		// Verifica se o número da casa do jogo é válido.
		if (linha<1 || linha>BOARD_SIZE || coluna<1 || coluna>BOARD_SIZE) 
			throw new Exception("Número da casa do jogo inválido!");
		
		// Envia a mensagem com a jogada para o servidor.
		os.println("<metodo><jogar linha='" + linha + "' coluna='"+ coluna + "'/></metodo>");
		
		// Recebe a resposta do servidor e retorna o estado.
		String resposta=is.readLine();
		registaLog("Cliente{"+resposta+"}");
		
		if(resposta==null)
			throw new Exception("Ligação ao servidor cancelada remotamente!");
		
		Document d = XMLDoc.parseString(resposta);
		validXSD(d);
	}
}
