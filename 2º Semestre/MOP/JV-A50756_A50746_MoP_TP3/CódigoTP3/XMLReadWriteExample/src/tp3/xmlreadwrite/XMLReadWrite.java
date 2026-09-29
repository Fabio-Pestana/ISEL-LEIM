
package tp3.xmlreadwrite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Esta aplicação serve para exemplificar a utilização de uma API disponibilizada nos packages javax.xml e org.w3.dom para ler
 * um ficheiro XML, editar os dados XML carregados, e voltar a gravar esses dados num ficheiro novo.
 * 
 * @version 1.0 
 * @author Docentes da Disciplina de Modelação e Programação, LEIM, Instituto Superior de Engenharia de Lisboa
 *
 */
public class XMLReadWrite {
	
	public static void main(String[] args) {

		try {
	 	 
			File inputFile = new File("XML/BaseDados.xml");
    	 
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         
			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "/catalog/*";
			NodeList nBooks = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
    
			//Mostrar na consola todos os Elementos "book"
			showAllBooks(nBooks);
			
			//Adicionar um novo "book" à lista nList de "book"s carregados da base de dados (BaseDados.xml)
			Element newBook = createNewBook(doc, "New Book", "003", "Mr Zen", "Fantasy", 5.0f);
				
			expression = "/catalog";
			NodeList nList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
    
			Node nCatalog = nList.item(0);
			
			nCatalog.appendChild(newBook);
			
			//Escrever, para o documento NewDB, o novo documento actualizado
			 FileOutputStream output = new FileOutputStream("XML/NewDB.xml");
	         writeXml(doc, output);
			
			
	  } catch (Exception e) {
	         e.printStackTrace();
	  }
		
		
	}

	/**
	 * Mostra na consola informações sobre todos os elementos com o nome "book" passados no argumento. 
	 * @param nBooks uma lista de nós contendo os elementos com nome "book".
	 */
	public static void showAllBooks(NodeList nBooks) {
		
		System.out.println("Books in Catalog :");
		for(int index = 0; index < nBooks.getLength(); index++) {
			
			Node nBook = nBooks.item(index);
			if (nBook.getNodeType() == Node.ELEMENT_NODE) {
				Element eBook = (Element) nBook;
				String id = eBook.getAttribute("id");
				String title  =  eBook.getElementsByTagName("title").item(0).getTextContent();
				String author =  eBook.getElementsByTagName("author").item(0).getTextContent();		
				String price  =  eBook.getElementsByTagName("price").item(0).getTextContent();
				System.out.println("id:" + id + " Title: " + title + ", Autor: " + author + " , price: " + price);
			}
		}
		
	} 
	
	/**
	 * Cria um novo Elemento "book".
	 * 
	 * @param doc o Documento que irá gerar os novos Elementos
	 * @param title o título do "book"
	 * @param id  o attributo ID do "book"
	 * @param author o autor 
	 * @param genre o género 
	 * @param price o preço
	 * @return um novo Element com nome "book" contendo os dados/subelementos passados como argumento.
	 */
	public static Element createNewBook(Document doc, String title, String id, String author, String genre, float price) {
	
		Element eBook   = doc.createElement("book");
		eBook.setAttribute("id", id);
		
		Element eAuthor   = doc.createElement("author");
		eAuthor.appendChild(doc.createTextNode(author));		
		eBook.appendChild(eAuthor);
		
		Element eTitle   = doc.createElement("title");
		eTitle.appendChild(doc.createTextNode(title));		
		eBook.appendChild(eTitle);
		
		Element eGenre   = doc.createElement("genre");
		eGenre.appendChild(doc.createTextNode(genre));		
		eBook.appendChild(eGenre);
		
		Element ePrice = doc.createElement("price");
		ePrice.appendChild(doc.createTextNode(Float.toString(price)));		
		eBook.appendChild(ePrice);
		
		Element eDate = doc.createElement("publish_date");
		eDate.appendChild(doc.createTextNode(LocalDate.now().toString()));		
		eBook.appendChild(eDate);
		
		Element eDesc = doc.createElement("description");
		eDesc.appendChild(doc.createTextNode("No Description"));		
		eBook.appendChild(eDesc);
		
		return eBook;
	}
	
	/**
	 * Escreve, para o OutputStream, o documento doc.
	 * @param doc o documento contendo os Elementos a gravar on ficheiro output
	 * @param output o ficheiro de saída.
	 */
    private static void writeXml(Document doc, OutputStream output) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        
        // pretty print XML
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }
	
	
}

