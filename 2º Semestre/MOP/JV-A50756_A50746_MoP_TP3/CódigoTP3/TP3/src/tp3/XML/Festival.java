package tp3.XML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Classe que representa um Evento do tipo Festival.
 *
 * @version 1.0
 * @author Docentes da Disciplina de Modelação e Programação, LEIM, Instituto Superior de Engenharia de Lisboa
 *
 */
public class Festival extends Evento {

	private static final int MAX_EVENTOS = 20;

	private final Evento[] eventos = new Evento[MAX_EVENTOS];
	private int numEventos = 0;

	public Festival(String nome) {
		super(nome);
	}

	/**
	 * Devolve todos os bilhetes existentes no Festival (somando e devolvendo todos os bilhetes dos seus Eventos).
	 * @return o número de bilhetes existentes no Festival.
	 */
	@Override
	public int getNumBilhetes() {
		int tickets=0;
		for (int i=0; i<numEventos; i++) {
			tickets = tickets + eventos[i].getNumBilhetes();
		}
		return tickets;
	}


	/**
	 * Retorna o número de actuaçõoes de um determinado artista.
	 * @param artista nome do artista.
	 * @return numero de actuacoes
	 * @Override
	 */
	@Override
	public int numActuacoes(String artista) {
		int contActuacoes = 0;
		for (int i=0; i<numEventos; i++) {
			contActuacoes = contActuacoes + this.eventos[i].numActuacoes(artista);
		}
		return contActuacoes;
	}

	/**
	 *  Devolve uma string representativa do Festival.
	 *  Nota: Ver o ficheiro OutputPretendido/OutputPretendido.txt
	 */
	@Override
	public String toString() {
		String string_final = "";
		string_final += "Festival " + this.getNome() + " com " + this.getNumBilhetes() + " bilhetes e com os artistas " + Arrays.toString(this.getArtistas());
		return string_final;
	}


	/**
	 * Devolve um array contendo todos, de forma não repetida, os nomes de todos os artistas quer irão
	 * actuar no Festival.
	 * @return um array contendo os nomes dos artistas.
	 */
	@Override
	public String[] getArtistas() {
		List<String> nomeArtista = new ArrayList<String>();
		for (int i = 0; i < numEventos; i++) {
			String[] artistas = eventos[i].getArtistas();
			for (int j = 0; j < artistas.length; j++) {
				if (!nomeArtista.contains(artistas[j]) && artistas[j]!=null) {
					nomeArtista.add(artistas[j]);
				}
			}
		}
		return nomeArtista.toArray(new String[nomeArtista.size()]);
	}

	/**
	 * Retorna a profundidade maxima da "árvore" que contém Festivais dentro de Festivais. O próprio Festival não conta.
	 * @return a profundidade máxima.
	 */
	public int getDeepFestival() {
		int prof = 0;
		for (int i = 0; i < eventos.length; i++) {
			if(eventos[i] != null && eventos[i] instanceof Festival f) {

				int prof_t= f.getDeepFestival() + 1;
				if (prof_t>prof)
					prof = prof_t;
			}
		}
		return prof;
	}

	/**
	 * Adiciona um novo Evento ao Festival, caso para nenhum dos artistas do novo evento se verifique que o seu número de atuações no
	 * novo evento (a adicionar) supere em mais de duas o número de atuações no festival corrente.
	 * @param evento
	 * @return verdadeiro, se o novo Evento foi adicionado.
	 */
	public boolean addEvento(Evento evento)
	{
		if (evento==null ||numEventos >= MAX_EVENTOS) //se já estiver cheio (20 eventos) nao se adiciona mais eventos
		{
			return false;
		}

		for (String artista : evento.getArtistas())
		{
			if (evento.numActuacoes(artista) > numActuacoes(artista) + 2)
			{
				return false;
			}
		}

		eventos[numEventos] = evento;
		numEventos++;
		return true;
	}

	/**
	 * Remove um evento em qualquer profundidade do Festival corrente.
	 * @param nomeEvento nome do Evento a remover.
	 * @return verdadeiro, se o Evento foi removido.
	 */
	public boolean delEvento(String nomeEvento) {
		for (int i = 0; i < numEventos; i++) {
			if (this.eventos[i].getNome().equals(nomeEvento)) {
				eventos[i] = null;
				numEventos--;
				return true;
			} else if (eventos[i] instanceof Festival) {
				Festival subfestival = (Festival) eventos[i];
				if (subfestival.delEvento(nomeEvento)) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * Imprime na consola informações sobre o Festival.
	 * Nota: Ver o output pretendido em OutputPretendido/OutputPretendido.txt.
	 * @param prefix prefixo para identar o Festival de acordo com a sua profundidade.
	 */
	public void print(String prefix) {
		System.out.println(prefix + toString());
		for(int i = 0; i<numEventos;i++){
			eventos[i].print(prefix+ " ");
		}
	}


	/**
	 * Constroi um novo Festival a partir de um nó contendo as infomações lidas do documento XML.
	 * @param nNode o nó associado ao Festival
	 * @return um novo Festival
	 */
	public static Festival build(Node nNode) {
		//transforma o node num Element
		Element eFest = (Element) nNode;

		//procura no node os atributos necessarios para criar um Festival
		String nome = eFest.getElementsByTagName("Nome").item(0).getTextContent();
		NodeList evento = eFest.getElementsByTagName("Eventos").item(0).getChildNodes();

		//cria-se um Festival
		Festival festival = new Festival(nome);

		//procura no node os eventos a adicionar ao Festival
		for (int i = 0; i < evento.getLength(); i++) {
			festival.addEvento(Evento.build(evento.item(i)));
		}
		return festival;
	}

	/**
	 * Cria um novo Elemento quer irá representar, no documento XML, o Festival associado ao Festival corrente.
	 * @param doc o Documento que irá ser usado para gerar o novo Element.
	 */
	public Element createElement(Document doc) {
		// Criar elemento para representar o Festival
		Element festival = doc.createElement("Festival");

		// Definir atributos do Festival
		Element nome = doc.createElement("Nome");
		nome.setTextContent(getNome());
		festival.appendChild(nome);

		//adiciona cada evento como um elemento filho do Festival
		Element evento = doc.createElement("Eventos");
		for (int i = 0; i < this.numEventos; i++) {
			if (this.eventos[i] instanceof Espetaculo) {
				evento.appendChild(this.eventos[i].createElement(doc));
			}
			if (this.eventos[i] instanceof Festival) {
				evento.appendChild(this.eventos[i].createElement(doc));
			}
		}
		festival.appendChild(evento);
		return festival;
	}


	/**
	 * Método main que gera no output o que está no ficheiro OutputPretendido/OutputPretendido.txt e cria um novo
	 * documento XML/Eventos.xml, com a mesma estrutura que o documento OutputPretendido/Eventos.xml.
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			File inputFile = new File("BaseDados.xml");

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "/BaseDados/Eventos/*";
			NodeList nList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);

			Node nNode = nList.item(0);
			Evento evento = Evento.build(nNode);
			if(evento != null) evento.print("");


			Festival fNovo = new Festival("Bollywood Music Festival");

			Espetaculo e1_1 = new Espetaculo("Suna Hai", "Sines", 500);
			e1_1.addArtista("Suna Hai");
			fNovo.addEvento(e1_1);

			Espetaculo e1_2 = new Espetaculo("Rait Zara", "Sines", 400);
			e1_2.addArtista("Rait Zara");
			fNovo.addEvento(e1_2);

			if(evento instanceof Festival festival) {

				festival.addEvento(fNovo);

				// root elements
				Document newDoc = dBuilder.newDocument();
				Element rootElement = newDoc.createElement("Eventos");

				rootElement.appendChild(festival.createElement(newDoc));

				newDoc.appendChild(rootElement);

				FileOutputStream output = new FileOutputStream("Eventos.xml");
				writeXml(newDoc, output);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//XPath


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