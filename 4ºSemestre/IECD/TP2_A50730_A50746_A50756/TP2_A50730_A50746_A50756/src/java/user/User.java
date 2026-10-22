package user;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.MyImage;
import util.XMLDoc;
import util.XMLDoc2;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import java.awt.Menu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class User {
    // Nome de ficheiro associado aos utilizadores
    private static String file = "users";
    // Path da pasta xml
    private static String xmlPath = "src/main/webapp/";

    // Carrega o ficheiro com os utilizadores
    private static Document doc = null;

    private String username;
    private String password;
    private String orgPassword;
    private MyImage profilePic;
    private String nationality;
    private LocalDate birthdate;
	private int vitorias = 0;
    private int derrotas = 0;
    private long gameTime = 0;
    private int games = 0;
    private long timeAverageInGame = 0;
    private String colorCode;
    private final String DeffaultColor = "676e7c";

    private static Scanner scanner;

    static {
        // Este bloco só executado quando a classe é carregada
        _load();
    }
    
    public User(String username, int vitorias, long time) throws NoSuchAlgorithmException, XPathExpressionException {
        setUsername(username);
        setVitorias(vitorias);
        setTimeAverageInGame(time);
    }

    public User(String username, String password, LocalDate birthdate, String nationality) throws NoSuchAlgorithmException, XPathExpressionException {
        setUsername(username);
        setPassword(password);
        setBirthdate(birthdate);
        setNationality(nationality);
    }

    // Construtor por defeito (User Teste)
    public User() {
        this.username = "userteste";
        this.password = "password123";
        this.nationality = "inexistente";
        this.birthdate = LocalDate.now();
    }
    
    public String getColor() {
        return colorCode;
    }

    public void setColor(String colorCode) {
        this.colorCode = colorCode;
    }
    
    public static boolean changeColor(String username, String newColorCode) throws XPathExpressionException, NoSuchAlgorithmException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        _load();
        if (newColorCode == null)
            return false;

        NodeList us = XMLDoc.getXPath("/users/user[username/text()='" + username + "']/colorCode", doc);

        if (us.getLength() != 1) {
            System.out.println("User não encontrado");
            return false;
        }

        us.item(0).setTextContent(newColorCode);
        _save();
        return true;
    }
    
    public static List<User> readUsersFromFile() {
        List<User> users = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlPath+"users.xml");
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("user");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String username = element.getElementsByTagName("username").item(0).getTextContent();
                    int victories = Integer.parseInt(element.getElementsByTagName("victories").item(0).getTextContent());
                    long timeAverageInGame = Long.parseLong(element.getElementsByTagName("timeAverageInGame").item(0).getTextContent());
                    User user = new User(username, victories, timeAverageInGame);
                    NodeList nl = element.getElementsByTagName("profilePic");
                    user.setPhotography((nl.getLength() == 1) ? nl.item(0).getTextContent() : null);
                    nl = element.getElementsByTagName("nationality");
                    user.setNationality((nl.getLength() == 1) ? nl.item(0).getTextContent() : null);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public static void saveRankingFile() throws Exception {
        String xmlContent = toXMLStringForRanking();
        
        // Path para "webapp/ranking_backups"
        Path webAppPath = Paths.get("src/main/webapp/ranking_backups");
        
        // Verificar se já existe algum arquivo no diretório
        File[] files = webAppPath.toFile().listFiles((dir, name) -> name.startsWith("rankingTable_") && name.endsWith(".xml"));
        
        if (files != null && files.length > 0) {
            // Encontrar o arquivo mais recente
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
            File mostRecentFile = files[0];
            
            // Ler o arquivo mais recente
            StringBuilder mostRecentContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(mostRecentFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    mostRecentContent.append(line);
                }
            }
            
            // Comparar com os dados atuais
            if (xmlContent.equals(mostRecentContent.toString())) {
                //System.out.println("Os dados atuais são idênticos ao arquivo de backup mais recente. Não foi criado nenhum arquivo de backup.");
                return;
            }
        }
        
        // Formatar a data e hora conforme o padrão especificado
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy--HH-mm-ss-SSS-nnnnnnnnn");
        String timestamp = now.format(formatter);
        
        // Criar o nome do arquivo
        String fileName = "rankingTable_" + timestamp + ".xml";
        String filePath = webAppPath.resolve(fileName).toString();
        
        // Salvar o conteúdo XML no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(xmlContent);
            System.out.println("Backup File guardado como: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getTop5Users() {
        List<User> users = readUsersFromFile();
        users.sort((u1, u2) -> {
            if (u1.getVitorias() != u2.getVitorias()) {
                return u2.getVitorias() - u1.getVitorias();  // Ordena por número de vitórias (decrescente)
            } else {
                return Long.compare(u1.getTimeAverageInGame(), u2.getTimeAverageInGame());  // Ordena por tempo médio de jogo (crescente)
            }
        });

        List<User> top5Usernames = new ArrayList<>();
        for (int i = 0; i < Math.min(5, users.size()); i++) {
            top5Usernames.add(users.get(i));
        }
        return top5Usernames;
    }
    
    public static String toXMLStringForRanking() throws Exception {
    	_load();
    	String ret = "";
    	List<User> users = User.getTop5Users();
    	for(User user : users) {
    		ret += "<user username='"+user.getUsername()+"' ";
            if(user.getProfilePic()!=null)
                ret+="profilePic='"+user.getProfilePic().getBase64()+"' ";
            if(user.getNationality()!=null) {
            	 ret+="nationality='"+user.getNationality()+"' ";
            	 ret+="flag='"+user.getBandeiraBase64(user.getNationality())+"' ";
            }
            
            ret+= "vitorias='"+user.getVitorias()+"' " +
            	"timeAverageInGame='"+user.getTimeAverageInGame()+"' />";
    	}
        
        return ret; //.replaceAll("[\\s]", "");
    }

    /**
     * Carrega e valida o documento util que tem os dados dos utilizadores
     */
    public static void _load() {
    	XMLDoc2.RemoveExtraLinesFromXML();
    	
        Document d = XMLDoc.parseFile(xmlPath+ file + ".xml");
        //XMLDoc.validDocXSD(d, "src/" + file + ".xsd");
        if(XMLDoc2.validDocXSD(XMLDoc.parseFile(xmlPath+"paises.xml"), xmlPath+"paises.xsd")){

            doc = d; // atualiza o documento na classe
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean setUsername(String username) {
        if (!validarUserName(username) && doesNameExists(username)){
            return false;
        }
        this.username = username;
        return true;
    }
    
    public boolean setUsernameNoXML(String username) {
        if (!validarUserName(username)){
            return false;
        }
        this.username = username;
        return true;
    }

    public static boolean validarUserName(String username) {

        // Tamanho mínimo e máximo do username
        final int TAMANHO_MINIMO = 4;
        final int TAMANHO_MAXIMO = 10;

        // Regex para validar caracteres permitidos no username
        // A expressão regular valida sequências de carateres, com tamanho entre 4 e 10,
        // que sejam apenas letras minúsculas, maiúsculas, números, underline e hífen,
        // desde que não estejam vazias (deve ter pelo menos um caractere) e
        // não contenham nenhum outro tipo de caractere especial
        final String REGEX_CARACTERES_VALIDOS = "^[a-zA-Z0-9_-]{" + TAMANHO_MINIMO + "," + TAMANHO_MAXIMO + "}$";

        // Verifica se o username está vazio
        if (username.isEmpty()) {
            return false;
        }

        // Verifica se o username contém apenas caracteres válidos
        if (!username.matches(REGEX_CARACTERES_VALIDOS)) {
            return false;
        }

        // Username é válido
        return true;
    }
    
    
    
    public static boolean doesNameExists(String name) {
    	if (name!=null){
        	return XMLDoc2.searchElementValue(file, "username",name);
        }
        return false;
    }
    

    public void setNationalityNoVerification(String nationality) {
        this.nationality = nationality;
    }
    
    public static boolean isNationalityValid(String nationality) {
        if (nationality!=null){
        	return XMLDoc2.searchElementValue(xmlPath+"paises","nacionalidade", nationality);
        }
        return false;
    }
    
    public String getBandeiraBase64(String nacionalidade) throws Exception {
        // Caminho para o arquivo XML
        String xmlFilePath = xmlPath + "bandeiras.xml";

        // Cria um objeto DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Cria um objeto DocumentBuilder
        DocumentBuilder builder = factory.newDocumentBuilder();
        // Parseia o XML e obtém o documento
        Document document = builder.parse(xmlFilePath);

        // Normaliza o documento XML
        document.getDocumentElement().normalize();

        // Obtém todas as tags "nacionalidade" no documento
        NodeList nacionalidadeList = document.getElementsByTagName("nacionalidade");

        // Itera sobre cada elemento "nacionalidade"
        for (int i = 0; i < nacionalidadeList.getLength(); i++) {
            Node node = nacionalidadeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String nome = element.getElementsByTagName("nome").item(0).getTextContent();
                String bandeira = element.getElementsByTagName("bandeira").item(0).getTextContent();
                // Verifica se a nacionalidade é a desejada
                if (nome.equalsIgnoreCase(nacionalidade)) {
                    MyImage p = new MyImage();
                    p.setBase64(bandeira);
                    return bandeira;
                }
            }
        }
        return null;
    }

    public String getPassword() {
        return password;
    }
    
    public String getOrgPassword() {
        return orgPassword;
    }
    
    public long getTimeAverageInGame() {
		return timeAverageInGame;
	}

	public void setTimeAverageInGame(long timeAverageInGame) {
		this.timeAverageInGame = timeAverageInGame;
	}

    public boolean setPassword(String password) throws NoSuchAlgorithmException {
    	this.orgPassword = password;
        this.password = XMLDoc.SHA256(password);
        return true;
    }

    public MyImage getProfilePic() {
        return profilePic;
    }

    /**
     * @param photo em base64
     * @return false se houver erro
     */
    public boolean setPhotography(String photo) {
        if (photo != null) {
            MyImage i = new MyImage();
            i.setBase64(photo.replaceAll("[\\s]", "")); // linhas e brancos a mais
            return setPhotography(i);
        }
        this.profilePic = null;
        return true;
    }

    /**
     * @param photo objeto que contém a fotografia
     * @return sucesso
     */
    public boolean setPhotography(MyImage photo) {
        this.profilePic = photo;
        return true;
    }
    
    public String getNationality() {
        return nationality;
    }

    public boolean setNationality(String nationality) {
        if (nationality!=null){
            if(XMLDoc2.searchElementValue(xmlPath+"paises","nacionalidade", nationality)){
                this.nationality = nationality;
                return true;
            }
        }
        return false;
    }
    
    

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public boolean setBirthdate(LocalDate birthdate) {
        // Validação da data de nascimento
        this.birthdate = birthdate;
        return true;
    }

    public int getAge() {
        // Calcula a idade a partir da data de nascimento
        LocalDate now = LocalDate.now();
        return Period.between(birthdate, now).getYears();
    }

    public int getVitorias() {
        return vitorias;
    }

    public void setVitorias(int viorias) {
        this.vitorias = viorias;
    }
    
    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public long getGameTime() {
        return gameTime;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }
    
    public void setDeffaultColor() {
    	setColor(DeffaultColor);
    }

    // Cria/Regista um User
    public static User read() throws NoSuchAlgorithmException, XPathExpressionException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        scanner = new Scanner(System.in);
        User user = new User();
        user.setDeffaultColor();
        do {
            System.out.print("Nome do utilizador: ");
            String username = scanner.nextLine();
            if (user.setUsername(username))
                break;
            System.out.print("Nome de utilizador inválido! ");
        } while (true);

        do {
            System.out.print("Senha para autenticação: ");
            String password = scanner.nextLine();
            try {
                if (user.setPassword(password)) {
                	break;
                }
                    
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            System.out.print("Senha inválida! ");
        } while (true);

        do {
            try {
                System.out.print("Ano de nascimento: ");
                int year = scanner.nextInt();
                System.out.print("Mês de nascimento: ");
                int month = scanner.nextInt();
                System.out.print("Dia de nascimento: ");
                int day = scanner.nextInt();
                System.out.println();
                user.setBirthdate(LocalDate.of(year, month, day));
                scanner.nextLine();
                if (user.getBirthdate() != null)
                    break;
            } catch (Exception e) {}
        } while (true);

        do {
            System.out.print("Nacionalidade do utilizador: ");
            String nationality = scanner.nextLine();
            if (user.setNationality(nationality))
                break;
            System.out.print("Nacionalidade do utilizador inválido! ");
        } while (true);

        changePicture(user);

        return user;
    }

	/**
     * Mostra os dados do utilizador na consola
     *
     * @throws Exception em caso de erro
     */
    public void print() throws Exception {
        System.out.println("----- Dados do Utilizador -----");
        /*if (blocked)
            System.out.println("Está bloqueado!");
        else
            System.out.println("Não está bloqueado!");*/
        System.out.println("Nome de utilizador: " + username);
        // Evitar mostrar a senha por motivos de segurança
        System.out.println("Senha: " + password);
        System.out.println("Idade: " + getAge());
        System.out.println("Nacionalidade: " + getNationality());

        System.out.println("---------------------------------------------------");
    }

    
    /**
     * Lógica que permite escolher/trocar a foto que o user quer como sua.
     * @param user
     * @throws XPathExpressionException 
     * @throws TransformerException 
     * @throws TransformerFactoryConfigurationError 
     * @throws IOException 
     * @throws SAXException 
     */
    public static void changePicture(User user){
        Scanner scanner1 = new Scanner(System.in);
        do {
            System.out.print("Indique o caminho para o ficheiro que contém a fotografia desejada tipo pass: ");
            String foto = scanner1.nextLine();
            if (foto == null)
                break;
            // Fotografia do utilizador (tipo passaporte)
            MyImage ft = new MyImage(XMLDoc.getContexto() + "photos/" + foto);
            ft.show();
            if (user.setPhotography(ft)) {
                break;
            }
        } while (true);
    }
    
    public static void changePicture(String username) throws XPathExpressionException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException{
        Scanner scanner1 = new Scanner(System.in);
        do {
        	// Procura o utilizador no DOM com o nome de utilizador e senha fornecidos
            NodeList us = XMLDoc.getXPath("/users/user[username/text()='" + username + "']/profilePic", doc);
        	// Verifica se foi encontrado um único utilizador com fotografia
            if (us.getLength() != 1) {
                System.out.println("Utilizador não encontrado ou credenciais inválidas");
                break;
            }

            System.out.print("Indique o caminho para o ficheiro que contém a fotografia desejada tipo pass: ");
            String foto = scanner1.nextLine();
            if (foto == null)
                break;
            MyImage ft = new MyImage(XMLDoc.getContexto() + foto);

            us.item(0).setTextContent(ft.getBase64());
            
            System.out.println("A sua fotografia foi atualizada com sucesso.");
            _save();
            _load();
            break;
        } while (true);
    }
    
    public static boolean changePicture(String username, String foto) throws XPathExpressionException, NoSuchAlgorithmException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
    	_load();
    	
    	NodeList us = XMLDoc.getXPath("/users/user[username/text()='" + username + "']/profilePic", doc);
    	
    	if (us.getLength() != 1) {
            System.out.println("Utilizador não encontrado ou credenciais inválidas");
            return false;
        }

        if (foto == null)
            return false;
        
        us.item(0).setTextContent(foto);
        
        _save();
        return true;
    }
    
    public static boolean changePassword(String username, String oldPass, String newPass) throws XPathExpressionException, NoSuchAlgorithmException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
    	_load();
    	if (newPass == null)
            return false;
    	
    	NodeList us = XMLDoc.getXPath("/users/user[username/text()='" + username + "' and password/text()='"
                 + XMLDoc.SHA256(oldPass) + "']/password", doc);
    	
    	if (us.getLength() != 1) {
            System.out.println("User não encontrado");
            return false;
        }
    	
    	us.item(0).setTextContent(XMLDoc.SHA256(newPass));
        _save();
        return true;
    }
    
    public static boolean updateUser(String username, boolean win, long time, boolean empate) throws XPathExpressionException, NoSuchAlgorithmException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
    	_load();
    	
    	if(time < 0)
    		return false;
    	
    	if(win && !empate) {
    		NodeList nodeVitorias = XMLDoc.getXPath("/users/user[username/text()='" + username + "']/victories", doc);
    	
    		if (nodeVitorias.getLength() != 1) {
    			System.out.println("User não encontrado");
    			return false;
    		}
    		
    		String v = nodeVitorias.item(0).getTextContent();
    		int Victories = Integer.parseInt(v);
    		Victories = Victories + 1;
    		nodeVitorias.item(0).setTextContent(Integer.toString(Victories));
    		
    	}else if(!win && !empate){
    		NodeList nodeDerrotas = XMLDoc.getXPath("/users/user[username/text()='" + username + "']/defeats", doc);
        	
        	if (nodeDerrotas.getLength() != 1) {
                System.out.println("User não encontrado");
                return false;
            }
        	String v = nodeDerrotas.item(0).getTextContent();
        	int defeats = Integer.parseInt(v);
        	defeats = defeats + 1;
        	
        	nodeDerrotas.item(0).setTextContent(Integer.toString(defeats));
    	}
    	

    	NodeList us = XMLDoc.getXPath("/users/user[username/text()='" + username + "']/gameTime", doc);
    	
    	if (us.getLength() != 1) {
            System.out.println("User não encontrado");
            return false;
        }
    	
    	String content = us.item(0).getTextContent();
    	
    	long gametime = Long.parseLong(content);
    	
    	gametime = gametime + time;
    	
    	us.item(0).setTextContent(String.valueOf(gametime));
    	
    	
    	NodeList games = XMLDoc.getXPath("/users/user[username/text()='" + username + "']/games", doc);
    	
    	if (games.getLength() != 1) {
            System.out.println("User não encontrado");
            return false;
        }
    	
    	String contentGames = games.item(0).getTextContent();
    	
    	int numberGames = Integer.parseInt(contentGames);
    	numberGames = numberGames + 1;
    	
    	games.item(0).setTextContent(Integer.toString(numberGames));
    	
    	NodeList timeAverage = XMLDoc.getXPath("/users/user[username/text()='" + username + "']/timeAverageInGame", doc);
    	
    	if (timeAverage.getLength() != 1) {
            System.out.println("User não encontrado");
            return false;
        }
    	
    	long tempoMedioEmJogos = gametime / numberGames;
    	
    	timeAverage.item(0).setTextContent(String.valueOf(tempoMedioEmJogos));
    	
    	_save();
		return true;
    }

    /**
     * Lógica que permite escolher/trocar para uma nova password.
     * @param user
     * @throws XPathExpressionException 
     * @throws TransformerException 
     * @throws TransformerFactoryConfigurationError 
     * @throws IOException 
     * @throws SAXException 
     */
    public static void changePassword(String username, String password) throws NoSuchAlgorithmException, XPathExpressionException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        Scanner scanner1 = new Scanner(System.in);
        do {
            System.out.print("Digite a nova password: ");
            String newPass = scanner1.nextLine();
            if (newPass == null)
                break;
            
            NodeList us = XMLDoc.getXPath("/users/user[username/text()='" + username + "' and password/text()='"
                    + XMLDoc.SHA256(password) + "']/password", doc);
            
            if (us.getLength() != 1) {
                System.out.println("User não encontrado");
                break;
            }
            us.item(0).setTextContent(XMLDoc.SHA256(newPass));
            _save();
            _load();
            System.out.println("A sua password foi alterado com sucesso!");
            break;
        } while (true);
    }

    /**
     * Converte um user para um ficheiro XML
     */
    public void user2XML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Verifica se o arquivo users.xml já existe
            File f = new File(xmlPath+file+".xml");
            Document doc;
            Element rootElement;

            if (f.exists()) {
                // Se o arquivo já existe, carrega o documento XML
                doc = builder.parse(f);
                rootElement = doc.getDocumentElement();
            } else {
                // Se o arquivo não existe, cria um documento XML
                doc = builder.newDocument();
                rootElement = doc.createElement("users");
                doc.appendChild(rootElement);
            }

            // Cria um novo elemento <user>
            Element userElement = doc.createElement("user");

            // Adiciona os elementos filho ao elemento <user>
            userElement.appendChild(XMLDoc2.createElement(doc, "username", getUsername()));
            userElement.appendChild(XMLDoc2.createElement(doc, "password", getPassword()));
            userElement.appendChild(XMLDoc2.createElement(doc, "profilePic", getProfilePic().getBase64()));
            userElement.appendChild(XMLDoc2.createElement(doc, "nationality", getNationality()));
            userElement.appendChild(XMLDoc2.createElement(doc, "birthdate", getBirthdate().toString()));
            userElement.appendChild(XMLDoc2.createElement(doc, "victories", String.valueOf(getVitorias())));
            userElement.appendChild(XMLDoc2.createElement(doc, "defeats", String.valueOf(getDerrotas())));
            userElement.appendChild(XMLDoc2.createElement(doc, "gameTime", String.valueOf(getGameTime())));
            userElement.appendChild(XMLDoc2.createElement(doc, "games", String.valueOf(getGames())));
            userElement.appendChild(XMLDoc2.createElement(doc, "timeAverageInGame", String.valueOf(getTimeAverageInGame())));
            userElement.appendChild(XMLDoc2.createElement(doc, "colorCode", getColor()));

            // Adiciona o elemento <user> ao elemento root <users>
            rootElement.appendChild(userElement);

            // Escreve o conteúdo do documento XML no arquivo users.xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            // Se o arquivo não existia antes, cria o mesmo
            if (!f.exists()) {
                f.createNewFile();
            }

            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);
            System.out.println("Utilizador adicionado com sucesso ao arquivo "+ file +".xml.");
            _load();
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Autenticar um utilizador.
     *
     * @param username Nome de utilizador
     * @param password Senha
     * @return Instância da classe User com informações do utilizador se
     *         autenticação for bem-sucedida, null caso contrário
     * @throws Exception em caso de erro
     */
    public static User _authenticate(String username, String password) throws Exception {
        // Procura o utilizadornno ficheiro XML com o nome de utilizador e senha fornecidos
    	_load();
        NodeList us = XMLDoc.getXPath("/users/user[username/text()='" + username +
                "' and password/text()='" + XMLDoc.SHA256(password) + "']", doc);

        // Verifica se um único utilizador desbloqueado foi encontrado com as credenciais
        if (us.getLength() != 1) {
            return null; // Utilizador não encontrado ou credenciais inválidas
        }

        // Retorna os dados do utilizador que se autenticou
        return getByUserName(username);
    }

    /**
     * Procura o utilizador com o username indicado
     * @param username 		Nome de utilizador
     * @return 			Utilizador encontrado
     * @throws Exception em 	Em caso de erro
     */
    public static User getByUserName(String username) throws Exception {
        return getUser("/users/user[username/text()='" + username + "']");
    }

    /**
     * Retorna um único utilizador (ou null) que respeite a expressão em argumento
     * @param xpath Expressão XPATH
     * @return	o utilizador encontrado
     * @throws Exception em caso de erro
     */
    public static User getUser(String xpath) throws Exception {
    	_load();
        NodeList l = XMLDoc.getXPath(xpath, doc);
        if (l.getLength() == 1) {
            User us = new User();
            us.fromElement((Element) l.item(0));
            us.print();
            return us;
        }
        System.out.println("Não encontrou!");
        return null;
    }

    /**
     * Atualiza o utilizdor atual a partir do elemento indicado em parametro
     *
     * @param userElement elemento que representa um utilizador no DOM
     * @throws Exception em caso de erro
     */
    public void fromElement(Element userElement) throws Exception {
        // Extract and set the user data from the XML element
        setUsername(userElement.getElementsByTagName("username").item(0).getTextContent());
        setPassword(userElement.getElementsByTagName("password").item(0).getTextContent());
        NodeList nl = userElement.getElementsByTagName("profilePic");
        setPhotography((nl.getLength() == 1) ? nl.item(0).getTextContent() : null);
        nl = userElement.getElementsByTagName("nationality");
        setNationality((nl.getLength() == 1) ? nl.item(0).getTextContent() : null);
        nl = userElement.getElementsByTagName("birthdate");
        setBirthdate((nl.getLength() == 1) ? xsdToLocalDate(nl.item(0).getTextContent()) : null);
        
        String vitorias = userElement.getElementsByTagName("victories").item(0).getTextContent();
        setVitorias(Integer.parseInt(vitorias));
        
        String derrotas = userElement.getElementsByTagName("defeats").item(0).getTextContent();
        setDerrotas(Integer.parseInt(derrotas));
        
        String gameTime = userElement.getElementsByTagName("gameTime").item(0).getTextContent();
        setGameTime(Long.parseLong(gameTime));
        
        String games = userElement.getElementsByTagName("games").item(0).getTextContent();
        setGames(Integer.parseInt(games));
        
        String time = userElement.getElementsByTagName("timeAverageInGame").item(0).getTextContent();
        setTimeAverageInGame(Long.parseLong(time));
        
        String color = userElement.getElementsByTagName("colorCode").item(0).getTextContent();
        setColor(color);
    }

    /**
     * @param xsdDate data no formato do XSD
     * @return data no Java
     */
    public static LocalDate xsdToLocalDate(String xsdDate) {
        if(xsdDate==null || xsdDate.isBlank())
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(xsdDate, formatter);
    }

    public String toXMLString(char simbolo) throws ParserConfigurationException {
        String ret = "<jogador simbolo='"+simbolo+"'>"
                + "<username>"+getUsername()+"</username>";
        if(getBirthdate()!=null)
            ret+="<birthdate>"+dateToXsd(getBirthdate())+"</birthdate>";
        if(getProfilePic()!=null)
            ret+="<profilePic>"+getProfilePic().getBase64()+"</profilePic>";
        if(getNationality()!=null)
            ret+="<nationality>"+getNationality()+"</nationality>";
        if(getAge()!=-1)
            ret+="<age>"+getAge()+"</age>";
        ret += "</jogador>";
        return ret; //.replaceAll("[\\s]", "");
    }
    
    public String toXMLString(String simbolo) throws ParserConfigurationException {
        String ret = "<jogador simbolo='"+simbolo+"'>"
                + "<username>"+getUsername()+"</username>";
        if(getBirthdate()!=null)
            ret+="<birthdate>"+dateToXsd(getBirthdate())+"</birthdate>";
        if(getProfilePic()!=null)
            ret+="<profilePic>"+getProfilePic().getBase64()+"</profilePic>";
        if(getNationality()!=null)
            ret+="<nationality>"+getNationality()+"</nationality>";
        if(getAge()!=-1)
            ret+="<age>"+getAge()+"</age>";
        
        ret+= "<vitorias>"+getVitorias()+"</vitorias> " +
        		"<derrotas>"+getDerrotas()+"</derrotas> "+
        		"<tempo>"+getGameTime()+"</tempo> " + 
        		"<jogos>"+getGames()+"</jogos> " + 
        		"<timeAverageInGame>"+getTimeAverageInGame()+"</timeAverageInGame></jogador>";
        return ret; //.replaceAll("[\\s]", "");
    }
    
    
    public String toXMLString() throws Exception {
    	_load();
    	
        String ret = " username='"+getUsername()+"' ";
        if(getBirthdate()!=null)
            ret+="birthdate='"+dateToXsd(getBirthdate())+"' ";
        if(getProfilePic()!=null)
            ret+="profilePic='"+getProfilePic().getBase64()+"' ";
        if(getNationality()!=null) {
        	 ret+="nationality='"+getNationality()+"' ";
        	 ret+="flag='"+getBandeiraBase64(getNationality())+"' ";
        }
           
        if(getAge()!=-1)
            ret+="age='"+getAge()+"' ";
        
        ret+= "vitorias='"+getVitorias()+"' " +
        		"derrotas='"+getDerrotas()+"' "+
        		"tempo='"+getGameTime()+"' " + 
        		"jogos='"+getGames()+"' " + 
        		"timeAverageInGame='"+getTimeAverageInGame()+"' " +
        		"colorCode='" + getColor() + "'";
        
        return ret; //.replaceAll("[\\s]", "");
    }
    
    


    /**
     * @param localDate data no Java
     * @return string que representa data no formato do XSD
     */
    public static String dateToXsd(LocalDate localDate) {
        if(localDate==null)
            return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }
    
    public static void menu() {
        char op;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println();
    	    System.out.println("*** Utilizador ***");
            System.out.println("1 - Consultar utilizador");
            System.out.println("2 - Alterar fotografia");
            System.out.println("3 - Alterar senha");
            System.out.println("4 - Simular autenticação/login");
            System.out.println("5 - Criar utilizador");
            System.out.println("0 - Terminar!");
            String str = null;
            do {
            str = sc.nextLine();
            } while (str == null || str.length() == 0);
            op = str.charAt(0);
            
            switch (op) {
                case '1':
                try {
                    consultarUser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
                case '2':
                try {
                    alterarFoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
                case '3':
                try {
                    alterarSenha();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
                case '4':
                try {
                    simLogin();
                } catch (Exception e) {
                e.printStackTrace();
                }
                break;
                case '5':
                try {
                    criaUser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
                default:
                System.out.println("Opção inválida, esolha uma opção do menu.");
            }
        } while (op != '0');
        System.out.println("Terminou a execução.");
        System.exit(0);
    }
    
    /**
     * Valida o documento XML, garante backup e grava as alterações.
     * 
     * @throws SAXException                         Se ocorrer um erro ao analisar o
     *                                              documento XML.
     * @throws IOException                          Se ocorrer um erro ao ler ou
     *                                              escrever arquivos.
     * @throws TransformerFactoryConfigurationError Se houver um problema na
     *                                              configuração da fábrica de
     *                                              transformação XML.
     * @throws TransformerException                 Se ocorrer um erro ao
     *                                              transformar o documento XML.
     */
    public static void _save() throws SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {

		// 1. Valida o documento XML contra o esquema XSD para garantir sua conformidade
		// com a estrutura esperada
		// --> Prevenir a gravação de dados inválidos ou inconsistentes.
		XMLDoc.validDocXSD(doc, XMLDoc.getContexto() + file + ".xsd");
	
		// 2. Cria o caminho completo para o ficheiro XML de utilizadores
		String docXML = XMLDoc.getContexto() + file + ".xml";
	
		// 3. Gera um nome de ficheiro para guardar um backup do ficheiro original
		String backup = XMLDoc.gerarNomeFBackupVersao(docXML);
	
		// 4. Grava as alterações feitas no ficheiro XML de utilizadores
		// --> Usando um mecanismo de bloqueio para evitar problemas de concorrência.
		// --> Renomeia o ficheiro original antes de o alterar.
		XMLDoc.gravarLock(doc, docXML, backup);
    }
    
    public static void consultarUser() throws Exception {
        Scanner scu = new Scanner(System.in);
        System.out.println("Insira o username a consultar:");
        String username = scu.nextLine();
        
        getByUserName(username);
    }

    public static void alterarFoto() throws Exception {
        Scanner scu = new Scanner(System.in);
        System.out.println("Insira o username a que vai alterar a foto:");
        String username = scu.nextLine();

        changePicture(username);
    }
    
    public static void alterarSenha() throws Exception {
        Scanner scu = new Scanner(System.in);
        System.out.println("Insira o username a que vai alterar a password:");
        String username = scu.nextLine();
        System.out.println("Insira a password atual para ser alterada:");
        String password = scu.nextLine();

        changePassword(username, password);
    }
    
    public static void simLogin() throws Exception {
        Scanner scu = new Scanner(System.in);
    	// Lê o nome do utilizador.
        System.out.println("Indique o seu username: ");
        String nome = scu.nextLine();
        if(nome==null || nome.isBlank())
            return;
    	// Lê a senha do utilizador.
        System.out.println("Indique a password do utilizador: ");
        String senha = null;
        do {
            senha = scu.nextLine();
        } while (senha == null || senha.length() == 0);

    	// Inicia a sessão.
        User login = _authenticate(nome, senha);
        if (login != null) {
            System.out.println("login realizada com sucesso!");
    	    // Mostra dados do utilizador que fez login
            login.print();
        } else
            System.out.println("Falhou o processo de Login!");
    }
    
    public static void criaUser() throws Exception {
        User user = read();
        user.user2XML();
    }
    
    // Main para testes
    public static void main(String[] args) throws Exception {
        //menu();
    	User user = User._authenticate("hello", "12345");
    	System.out.print(user.toXMLString());
    }
}