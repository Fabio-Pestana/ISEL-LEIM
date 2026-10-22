package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import user.User;
import util.XMLDoc;


public class SrvStub {
    
	/**
     * Valida um documento XML em relação a um esquema XSD.
     * 
     * @param doc O documento XML a ser validado.
     * @throws Exception Se ocorrer um erro durante a validação.
     */
    private static void validXSD(final Document doc) throws Exception {
        try {
            // Valida o documento contra o XSD "metodos.xsd".
            XMLDoc.validDocXSD(doc, XMLDoc.getContexto() + "othello-metodos-srv.xsd");
        } catch (SAXException | IOException e) {
            throw new Exception("Recebeu mensagem inválida: " + e.getLocalizedMessage());
        }
    }
    
    /**
     * Obtém o próximo documento XML da entrada fornecida.
     * 
     * @param is O BufferedReader da qual ler a entrada.
     * @return O documento XML lido.
     * @throws Exception Se ocorrer um erro durante a leitura ou validação do XML.
     */
    private static Document getNext(BufferedReader is) throws Exception {
    	// Lê a linha que contém a mensagem.
    	String line=is.readLine();
    	registaLog("Servidor{"+line+"}");
        Document d = XMLDoc.parseString(line); 
        // Valida o schema XSD da mensagem.
        validXSD(d);
        return d;
    }
    
    /**
     * Obtém o próximo documento XML da entrada fornecida.
     * 
     * @param line A linha contendo o XML.
     * @return O documento XML lido.
     * @throws Exception Se ocorrer um erro durante a validação do XML.
     */
    private static Document getNext(String line) throws Exception {
    	// Lê a linha que contém a mensagem.
        
        	registaLog("Servidor{"+line+"}");
            Document d = XMLDoc.parseString(line); 
            // Valida o schema XSD da mensagem.
            validXSD(d);
            return d;
        }
    
	
    /**
     * Executa a operação de iniciar jogo para um jogador.
     * 
     * @param sk O socket do cliente.
     * @param simbolo O símbolo do jogador.
     * @param line A linha contendo a solicitação XML.
     * @throws Exception Se ocorrer um erro durante a execução.
     */
    public static void runIniciar(Socket sk, String simbolo, String line) throws Exception {
	
    	PrintWriter os = new PrintWriter(sk.getOutputStream(), true);
    
		System.out.println("   Jogador '"+simbolo+"': "+sk);
        Document x = getNext(line);
        
        // Trata o jogador
        // Extrai o nome e senha do jogador X.
        String Nome 	= 	getMethod(x,"iniciar").getAttribute("nickname");
        System.out.println("   Jogador '"+simbolo+"': " + Nome);
        
        User jg = User.getByUserName(Nome);
        
        if(jg == null)
            throw new Exception("Falhou a autenticação do utilizador '"+Nome+"'!");
        
        System.out.println("Autenticação/login do utilizador '"+Nome+"' realizado com sucesso!");
        
        Document d = XMLDoc.parseString(jg.toXMLString(simbolo));
        
        Node jogador = d.getElementsByTagName("jogador").item(0);
        Node cloneElement = x.importNode(jogador, true);
        
        x.getElementsByTagName("iniciar").item(0).appendChild(cloneElement);
        // Envia a mensagem de "iniciar" para o jogador, 
        // com o seu símbolo que confirma o login bem sucedido.
        String msg=XMLDoc.documentToString(x);
        os.println(msg);
    }
    
    /**
     * Executa a operação de criar um novo usuário.
     * 
     * @param sk O socket do cliente.
     * @param line A linha contendo a solicitação XML.
     * @throws Exception Se ocorrer um erro durante a execução.
     */
    public static void runNewUser(Socket sk, String line) throws Exception {   	
    		
    		PrintWriter os = new PrintWriter(sk.getOutputStream(), true);
        
            Document x = getNext(line);
            
            if(getMethodIfExists(x,"user")!= null) {
            	String Nome = getMethod(x,"user").getAttribute("username");
            	String Senha = getMethod(x,"user").getAttribute("password");
            	String Data = getMethod(x,"user").getAttribute("birthdate");
            	String Foto = getMethod(x,"user").getAttribute("profilePic");
            	String Nacionalidade = getMethod(x,"user").getAttribute("nationality");
            
            	LocalDate data = LocalDate.parse(Data);
            	User user = new User(Nome, Senha, data, Nacionalidade);
            	user.setPhotography(Foto);
            	user.user2XML();
            
            	System.out.println("User criado com sucesso!");
            
            	os.println("<metodo><user/></metodo>");
            }
        }
    
    /**
     * Executa a operação de login de um usuário.
     * 
     * @param sk O socket do cliente.
     * @param line A linha contendo a solicitação XML.
     * @throws Exception Se ocorrer um erro durante a execução.
     */
    public static void runLogin(Socket sk, String line) throws Exception {
    	boolean sucessLogin = true;
    	
    	PrintWriter os = new PrintWriter(sk.getOutputStream(), true);
    
        Document x = getNext(line);
        
        String Nome 	= 	getMethod(x,"login").getAttribute("nickname");
        String Senha 	= 	getMethod(x,"login").getAttribute("senha");
        
        User jg = User._authenticate(Nome, Senha);
        
        if(jg == null)
            sucessLogin = false;
        
    	os.println("<metodo><login value='" + sucessLogin + "'/></metodo>");
    }
    
    /**
     * Executa a operação de alterar a senha de um usuário.
     * 
     * @param sk O socket do cliente.
     * @param line A linha contendo a solicitação XML.
     * @throws Exception Se ocorrer um erro durante a execução.
     */
    public static void runChangePass(Socket sk, String line) throws Exception {
    	
    	PrintWriter os = new PrintWriter(sk.getOutputStream(), true);
    
        Document x = getNext(line);
        
        String Nome 	= 	getMethod(x,"changePass").getAttribute("username");
        String Senha 	= 	getMethod(x,"changePass").getAttribute("oldPass");
        String NovaSenha 	= 	getMethod(x,"changePass").getAttribute("newPass");
        
        boolean sucess = User.changePassword(Nome, Senha, NovaSenha);
        
    	os.println("<metodo><changePass value='" + sucess + "'/></metodo>");
    }
    

    /**
     * Executa a operação de atualizar os stats do jogador
     * 
     * @param is       O BufferedReader para ler os dados de entrada.
     * @param os       O PrintWriter para enviar a saída.
     * @param simbolo  O símbolo do usuário no jogo.
     * @param sk       O socket associado à conexão.
     * @throws Exception Se ocorrer um erro durante a atualização do usuário.
     */
	public static void runUpdateUser(BufferedReader is, PrintWriter os, String simbolo,  Socket sk) throws Exception {
	    Document updateUser = getNext(is);
	    
	    boolean sucessUpdate;
	
	    Element update = getMethod(updateUser, "updateUser");
	
	    String nome = update.getAttribute("username");
	    String gameResult = update.getAttribute("VDE");
	    int VDE = Integer.parseInt(gameResult);
	    String time = update.getAttribute("timeOfPlay");
	    long timeOfPlay = Long.parseLong(time);
	
	    if(VDE == 1) {
	    	if(simbolo.equals("X ")) {
	    		sucessUpdate = User.updateUser(nome, false, timeOfPlay, false);
	    	}else {
	    		sucessUpdate = User.updateUser(nome, true, timeOfPlay, false);
	    	}
	    }else if(VDE == 2) {
	    	if(simbolo.equals("X ")) {
	    		sucessUpdate = User.updateUser(nome, true, timeOfPlay, false);
	    	}else {
	    		sucessUpdate = User.updateUser(nome, false, timeOfPlay, false);
	    	}
	    }else {
	    	sucessUpdate = User.updateUser(nome, false, timeOfPlay, true);
	    }
	    
		os.println("<metodo><updateUser value='" + sucessUpdate + "'/></metodo>");
	}
    
	/**
	 * Executa a mudança de imagem de perfil do usuário.
	 * 
	 * @param sk    O socket associado à conexão.
	 * @param line  A linha recebida contendo os dados da solicitação.
	 * @throws Exception Se ocorrer um erro durante a mudança de imagem.
	 */
	public static void runChangePic(Socket sk, String line) throws Exception {
    	
    	PrintWriter os = new PrintWriter(sk.getOutputStream(), true);
    
        Document x = getNext(line);
        
        String Nome 	= 	getMethod(x,"changePic").getAttribute("username");
        String Foto 	= 	getMethod(x,"changePic").getAttribute("picture");
        
        boolean sucess = User.changePicture(Nome, Foto);
        
    	os.println("<metodo><changePic value='" + sucess + "'/></metodo>");
    }
    
	/**
	 * Executa a operação de obtenção do perfil de usuário.
	 * 
	 * @param sk    O socket associado à conexão.
	 * @param line  A linha recebida contendo os dados da solicitação.
	 * @throws Exception Se o usuário não for encontrado ou ocorrer um erro durante a operação.
	 */
    public static void runPerfil(Socket sk, String line) throws Exception {
    	boolean sucessLogin = true;
    	
    	PrintWriter os = new PrintWriter(sk.getOutputStream(), true);
    
        Document x = getNext(line);
        
        String Nome = getMethod(x,"perfil").getAttribute("username");
        User user = User.getByUserName(Nome);
        
        if(user == null)
            throw new Exception("User nao encontrado '"+Nome+"'!");
        
    	os.println("<metodo><perfil" + user.toXMLString() + "/></metodo>");
    }
    
    /**
     * Executa a operação de obtenção do estado atual do jogo (tabuleiro).
     * 
     * @param is        O BufferedReader para ler os dados de entrada.
     * @param os        O PrintWriter para enviar a saída.
     * @param simbolo   O símbolo do usuário no jogo.
     * @param sk        O socket associado à conexão.
     * @param jogo      O jogo.
     * @throws Exception Se ocorrer um erro durante a operação.
     */
    public static void runObter(BufferedReader is, PrintWriter os, String simbolo, Socket sk, OthelloXML jogo) throws Exception {
        Document obter = getNext(is);
        
        getMethod(obter, "obter");

        os.println("<metodo><obter>" + jogo.tabuleiroToXML(simbolo) + "</obter></metodo>");
    }
	
    
    /**
     * Executa a jogada recebida pelo cliente.
     * 
     * @param is        O BufferedReader para ler os dados de entrada.
     * @param os        O PrintWriter para enviar a saída.
     * @param simbolo   O símbolo do usuário no jogo.
     * @param sk        O socket associado à conexão.
     * @param jogo      O estado atual do jogo.
     * @return O estado atualizado do jogo após a jogada.
     * @throws Exception Se ocorrer um erro durante a operação.
     */
    public static OthelloXML runJogar(BufferedReader is, PrintWriter os, String simbolo, Socket sk, OthelloXML jogo) throws Exception {
        Document jogar = getNext(is);

        Element jogada = getMethod(jogar, "jogar");

        String linhaStr = jogada.getAttribute("linha");
        int linhaNum = Integer.parseInt(linhaStr);
        
        String colunaStr = jogada.getAttribute("coluna");
        int colunaNum = Integer.parseInt(colunaStr);

        jogo.joga(linhaNum, colunaNum, simbolo);

        os.println(XMLDoc.documentToString(jogar));   	
    	return jogo;
    }
    
    /**
     * Obtém o elemento XML associado a um método específico de um documento XML.
     * 
     * @param doc     O documento XML.
     * @param Method  O nome do método XML.
     * @return O elemento XML associado ao método.
     * @throws Exception Se o método não for encontrado ou houver mais de um método com o mesmo nome.
     */
    private static Element getMethod(final Document doc, final String Method) throws Exception {
        NodeList items = doc.getElementsByTagName(Method);
        if (items.getLength() != 1) {
            throw new Exception("Erro de lógica: espera método '" + Method + "'!");
        }
        return ((Element) doc.getElementsByTagName(Method).item(0));
    }
    
    /**
     * Obtém o elemento XML associado a um método específico de um documento XML, se existir.
     * 
     * @param doc     O documento XML.
     * @param Method  O nome do método XML.
     * @return O elemento XML associado ao método, ou null se não encontrado.
     * @throws Exception Se houver mais de um método com o mesmo nome.
     */
    private static Element getMethodIfExists(final Document doc, final String Method) throws Exception {
        NodeList items = doc.getElementsByTagName(Method);
        if (items.getLength() != 1) {
            return null;
        }
        return ((Element) doc.getElementsByTagName(Method).item(0));
    }
    
    /**
     * Adiciona uma string a um arquivo especificado.
     * 
     * @param ficheiro  O caminho do arquivo.
     * @param texto     A string a ser adicionada ao arquivo.
     * @throws IOException Se ocorrer um erro de E/S ao escrever no arquivo.
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
     * Registra um evento no arquivo de log.
     * 
     * @param evento O evento a ser registrado.
     * @throws IOException Se ocorrer um erro de E/S ao escrever no arquivo de log.
     */
    private static void registaLog(String evento) throws IOException {
    	adicionarStringFicheiro(XMLDoc.getContexto()+"protocolo.log", LocalDateTime.now()+" - "+evento.replaceAll("\n",""));
    }
}
