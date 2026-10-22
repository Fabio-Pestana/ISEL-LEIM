package util;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Manipula imagens
 * 
 * @author Engº Porfírio Filipe
 * 
 */
@SuppressWarnings("serial")
public class MyImage implements Serializable {
    String path = null; 	// caminho
    String mime = null; 	// tipo
    byte[] content = null; 	// conteudo
    /**
     * Caminho para a raiz onde estão as imagens
     */
    public static final String contexto = "src/main/webapp/";

    /**
     * construtor por omissao
     */
    public MyImage() {
    }

    /**
     * @param p caminho
     */
    public MyImage(final String p) {
	path = p;
	mime = p.substring(p.lastIndexOf('.') + 1);
	load();
    }
    
    /**
     * @param p caminho
     * @param m tipo
     */
    public MyImage(final String p, final String m) {
	path = p;
	mime = m;
	load();
    }

    /**
     * Carrega a imagem a partir do ficheiro
     * 
     * @return sucesso
     */
    public boolean load() {
	if (path != null) {
	    File fi = new File(path);
	    if (fi.exists())
		try {
		    content = Files.readAllBytes(fi.toPath());
		    return true;
		} catch (final IOException e) {
		    // e.printStackTrace();
		    System.err.println("Falhou a abertura do ficheiro com o nome: '" + path + "'!");
		    return false;
		}
	    else
		System.err.println("Não encontrou o fihciero com o nome: '" + path + "'!");
	}
	return false;
    }

    /**
     * Copia a imagem passada em argumento
     * @param f imagem que vai ser copiada
     */
    public void copia(MyImage f) {
	path = f.path;
	mime = f.mime;
	content = f.content;
    }

    /**
     * Guarda a imagem no ficheiro indicado em argumento 
     * Fazendo backup/rename do ficheiro
     * @param dataFile ficheiro onde imagem vai ser guardada
     * @return sucesso
     */
    public boolean save(String dataFile) {
	if (dataFile == null)
	    dataFile = path;
	String bkFile = dataFile.substring(0, dataFile.lastIndexOf('.')) 
		+ "-"+new Date().getTime() + "."+dataFile.substring(dataFile.lastIndexOf('.') + 1);
	final File file = new File(dataFile);
	if (file.exists()) {
	    if (!file.renameTo(new File(bkFile))) {
		System.err.println("Falhou a alteração do nome '" + dataFile + "' do ficheiro para '" + bkFile + "'!");
		return false;
	    } else 
	       System.out.println("Foi mantido ficheiro de backup em: '" + bkFile+"'!");
	}

	try {
	    File f = new File(dataFile);
	    Path path = Paths.get(f.getAbsolutePath());
	    Files.write(path, content);

	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * @return existe
     */
    public boolean isOk() {
	return path != null;
    }

    /**
     * @return mime
     */
    public String getMime() {
	return mime;
    }

    /**
     * @return caminho
     */
    public String getPath() {
	return path;
    }

    /**
     * @return o conteudo da imagem em bytes
     */
    public byte[] getContent() {
	return content;
    }

    /**
     * Modifica o conteudo da imagem
     * @param c conteudo em bytes
     */
    public void setContent(byte[] c) {
	content = c;
    }

    /**
     * Conversão de bytes para base64
     * 
     * @return conteudo
     */
    public String getBase64() {
	if (content != null)
	    return Base64.getEncoder().encodeToString(content);
	return null;
    }

    /**
     * Conversão de base64 para bytes
     * @param base64 conteudo em base64
     */
    public void setBase64(String base64) {
	if (base64 != null)
	    content=Base64.getDecoder().decode(base64);
    }

    /**
     * Visualiza a imagem
     */
    public void show() {
	if (path != null) {  // pode ter só o conteudo
	    System.out.println(" Imagem:");
	    System.out.println("  Path = " + path);
	    System.out.println("  Mime = " + mime);
	}
	if (content == null)
	    System.out.println("Imagem: Não existe conteudo em memória!");
	else {
	    System.out.println("Imagem: Conteudo em memória! (" + content.length + ")");
	    try {
		view();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Seria a imagem para um ficheiro
     * @param fileOut fluxo de escrita
     * @return sucesso
     */
    public boolean seriar(FileOutputStream fileOut) {
	try {
	    ObjectOutputStream outputStream = new ObjectOutputStream(fileOut);
	    outputStream.writeObject(this);
	    outputStream.close();
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return false;
    }

    /**
     * Seria a imagem para um socket
     * @param socket 		Para definir o fluxo de escrita
     * @throws IOException 	Em caso de erro
     */
    public void seriar(Socket socket) throws IOException {
	try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());) {
	    outputStream.writeObject(this);
	}
    }

    /**
     * Deseria a imagem de um ficheiro
     * @param fileIn 	Fluxo para leitura
     * @return 		Sucesso
     */
    public boolean deseriar(FileInputStream fileIn) {
	try {
	    ObjectInputStream inputStream = new ObjectInputStream(fileIn);
	    copia((MyImage) inputStream.readObject());
	    inputStream.close();
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException c) {
	    c.printStackTrace();
	}
	return false;
    }

    /**
     * Deseria a imagem de um socket
     * @param socket para definir o fluxo de leitura
     * 
     * @throws IOException		Em caso de erro
     * @throws ClassNotFoundException	Em caso de erro
     * 
     */
    public void deseriar(Socket socket) throws ClassNotFoundException, IOException {
	try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());) {
	    copia((MyImage) inputStream.readObject());

	}
    }

    /**
     * Simula o acesso como cliente TCP
     * 
     * @param host			Endereço IP de destino
     * @param port			Porto de destino
     * @throws UnknownHostException 	Em caso de erro
     * @throws IOException 		Em caso de erro
     */
    public void clientTCP(String host, int port) throws UnknownHostException, IOException {
	try (Socket socket = new Socket(host, port);) {
	    seriar(socket);
	}
    }

    /**
     * Simula o acesso como servidor TCP
     * @param port			Porto onde espera ligação
     * @throws IOException		Em caso de erro
     * @throws ClassNotFoundException 	Em caso de erro
     */
    public void serverTCP(int port) throws IOException, ClassNotFoundException {
	try (

		ServerSocket serverSocket = new ServerSocket(port);
		Socket socket = serverSocket.accept();)

	{
	    deseriar(socket);
	}
    }

    /**
     * Descarrega uma imagem de um URL especificado para um diretório de destino.
     *
     * @param sourceUrl 		O URL da imagem a descarregar.
     * @param targetDirectory 		O caminho do diretório de destino onde a imagem será gravada.
     * @return 				O caminho completo do ficheiro da imagem descarregada.
     * @throws MalformedURLException 	Se a URL fornecida estiver incorreta.
     * @throws IOException 		Se ocorrer um erro durante a leitura ou escrita da imagem.
     * @throws FileNotFoundException 	Se o diretório de destino não existir ou não for possível criar o ficheiro.
     */
    public static String descarregar(String sourceUrl, String targetDirectory)
            throws MalformedURLException, IOException, FileNotFoundException {

        // Cria um objeto URL a partir do URL da imagem
        URL imageUrl = new URL(sourceUrl);

        // Extrai o nome do ficheiro a partir do URL
        String strPath = imageUrl.getFile();

        // Remove parâmetros do URL (se existirem)
        if (strPath.lastIndexOf('?') == -1) {
            strPath = strPath.substring(strPath.lastIndexOf('/') + 1);
        } else {
            strPath = strPath.substring(strPath.lastIndexOf('/') + 1, strPath.lastIndexOf('?'));
        }

        // Abre um fluxo de entrada para ler a imagem do URL
        try (InputStream imageReader = new BufferedInputStream(imageUrl.openStream());
                // Abre um fluxo de saída para gravar a imagem no ficheiro de destino
                OutputStream imageWriter = new BufferedOutputStream(
                        new FileOutputStream(targetDirectory + strPath))) {

            // Lê a imagem do URL byte a byte e escreve no ficheiro
            int readByte;
            while ((readByte = imageReader.read()) != -1) {
                imageWriter.write(readByte);
            }

            // Devolve o caminho completo do ficheiro da imagem descarregada
            return targetDirectory + strPath;
        }
    }
    // Visualizar a imagem
    private static void exemplo1() {
	MyImage f10 = new MyImage(contexto + "mars.jpg", "jpg");
	f10.show();
    }
    
    // Acede ao ficheiro que mostra e salva mantendo backup
    private static void exemplo2() {

	MyImage f10 = new MyImage(contexto + "pt.png", "png");
	f10.show();
	
	// obtém o conteudo em base64;
	String xml = f10.getBase64(); 
	System.out.println("Texto em base 64: " + xml.length());

	MyImage f3 = new MyImage();
	
	// define a nova imagem a partir de string em base64;
	f3.setBase64(xml); 
	
	// guarda no ficheiro
	f3.save(contexto + "pt.png"); 
	System.out.println("Terminou a manipulação da imagem 'pt.png'!");
    }

    // Teste do servidor que usa seriação 
    private static void exemplo3() {
	MyImage f10 = new MyImage();
	try {
	    f10.serverTCP(5025);
	    f10.save(MyImage.contexto + "isel-seriado.png");
	    f10.show();
	} catch (ClassNotFoundException | IOException e) {
	    e.printStackTrace();
	}
    }

    // Exemplifica como pode ser descarregada imagem via com http ou https
    private static void exemplo5() {
	try {
	    // raiz: https://www.isel.pt/servicos/servico-de-comunicacao-e-imagem/identidade-institucional/
	    String fich = descarregar("https://www.isel.pt/sites/default/files/001_imagens_isel/Logotipos/logo_ISEL_principal_RGB_PNG.png", contexto);
	    MyImage f2 = new MyImage(fich, fich.substring(fich.lastIndexOf('.') + 1));
	    f2.show();
	    System.out.println("Descarregou a imagem!");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    // Teste do cliente que usa seriação
    private static void exemplo4() {
	MyImage f10 = new MyImage(MyImage.contexto + "isel.png", "png");
	try {
	    f10.clientTCP("localHost", 5025);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Menu para demonstrar como se pode usar esta classe
     */
    private static void menu() {
	char op;
	Scanner sc = new Scanner(System.in);
	do {
	    System.out.println();
	    System.out.println();
	    System.out.println("*** Menu ***");
	    System.out.println("1 – Visualizar imagem.");
	    System.out.println("2 - Testar conversão base64.");
	    System.out.println("3 – Lança servidor para receber imagem.");
	    System.out.println("4 – Lança cliente para enviar imagem.");
	    System.out.println("5 – Descarrega imagem de URL.");
	    System.out.println("0 - Terminar!");
	    String str = sc.nextLine();
	    if (str != null && str.length() > 0)
		op = str.charAt(0);
	    else
		op = ' ';
	    switch (op) {
	    case '1':
		exemplo1();
		break;
	    case '2':
		exemplo2();
		break;
	    case '3':
		exemplo3();
		break;
	    case '4':
		exemplo4();
		break;
	    case '5':
		exemplo5();
		break;
	    case '0':
		break;
	    default:
		System.out.println("Opção inválida, esolha uma opção do menu.");
	    }
	} while (op != '0');
	sc.close();
	System.out.println("Terminou a execução.");
	System.exit(0);
    }
    
    /**
     * Visualiza a imagem a partir do array de bytes. 
     * 
     * @throws Exception 	Exceção genérica caso ocorra algum erro.
     */
    public void view() throws Exception {
	if(content==null)
	    return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame editorFrame = new JFrame("Visualização");
                editorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // WindowConstants.EXIT_ON_CLOSE

                ByteArrayInputStream bais = new ByteArrayInputStream(content);
                BufferedImage image=null;
                try {
                    image=ImageIO.read(bais);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                
                ImageIcon imageIcon = new ImageIcon(image);
                JLabel jLabel = new JLabel();
                jLabel.setIcon(imageIcon);
                editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

                editorFrame.pack();
                editorFrame.setLocationRelativeTo(null);
                editorFrame.setVisible(true);
            }
        });
    }
    /**
     * Para demonstrar o funcionamento desta classe
     * @param args não usado
     */
    public static void main(final String[] args) {

    	menu();

    }
}
