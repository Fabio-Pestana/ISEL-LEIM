package tp3.XML;

import java.util.Arrays;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classe que representa um Evento do tipo Espetaculo.
 * @version 1.0 
 * @author Docentes da Disciplina de Modelação e Programação, LEIM, Instituto Superior de Engenharia de Lisboa
 */
public class Espetaculo extends Evento {

	private static final int MAX_ARTISTAS = 10;
	private String[] artistas = new String[MAX_ARTISTAS];
	private int nArtistas = 0;
	private int numBilhetes;
	private String localidade;

	/**
	 * Constroi um novo Espetaculo
	 * @param nome nome do Espetaculo
	 * @param localidade a localidade do Espetaculo
	 * @param numBilhetes o número de bilhetes disponíveis
	 */
	public Espetaculo(String nome, String localidade, int numBilhetes) {
		super(nome);
		this.numBilhetes = numBilhetes;
		this.localidade = localidade;
	}

	/**
	 * Informa se um determinado artista irá actuar no Espetaculo.
	 * @return 1, se actuar e 0 caso contrário.
	 * @Override
	 */
	@Override
	public int numActuacoes(String artista) {
		for (int i=0; i<nArtistas; i++) {
			if (this.artistas[i].equals(artista))
				return 1;
		}
		return 0;
	}

	/**
	 * Permite adicionar un novo artista ao Espetaculo se o artista ainda
	 * não tem actuações e se o número máximo de artistas ainda não foi ultrapassado.
	 * @param artista representa o novo artista
	 * @return verdadeiro, caso o artista tenha sido adicionado e falso caso contrário.
	 */
	public boolean addArtista(String artista) {

		if (artista == null || nArtistas>=MAX_ARTISTAS || artista.length()<=1 ) { // Vê se o numero de artista já atingiu o máximo (10)
			return false;
		}

		for (int i=0; i<nArtistas;i++) {
			if (this.artistas[i].equalsIgnoreCase(artista)) //verifica se o artista já pertence ao espetaculo
				return false;
		}

		artistas[nArtistas]=artista;
		nArtistas ++; //adiciona +1 artista para o numero de artistas
		return true;
	}

	/**
	 * Devolve o número de bilhetes do Espetaculo.
	 * @return o número de bilhetes.
	 * @Override
	 */
	@Override
	public int getNumBilhetes() {
		return this.numBilhetes;
	}

	/**
	 * Devolve uma cópia dos artistas que actuam no Espetaculo.
	 * @return o array.
	 * @Override
	 */
	@Override
	public String[] getArtistas() {
		return Arrays.copyOf(artistas, nArtistas);
	}

	/**
	 * Devolve a localidade do Espetaculo
	 * @return a localidade.
	 */
	public String getLocalidade() {
		return this.localidade;
	}

	/**
	 * Devolve uma String a representar o Espetaculo.
	 * Nota: Ver o ficheiro OutputPretendido.txt
     * @Override
	 */
	@Override
	public String toString()
	{
		String string_final ="";
		string_final = this.getNome() + " com " + getNumBilhetes() + " bilhetes e com os artista(s) " + String.join(", ", Arrays.toString(getArtistas()) +  ", em " + this.localidade);
		return string_final;
	}

	/**
	 * Constroi um novo Evento a partir do objecto Node passado como parâmetro.
	 * @param nNode
	 * @return um novo Evento
	 */
	public static Evento build(Node nNode) {
			//transforma o node num Element
			Element eElement = (Element) nNode;

			//procura no node os atributos necessarios para criar um Espectaculo
			String nome = eElement.getElementsByTagName("Nome").item(0).getTextContent();
			String localidade = eElement.getElementsByTagName("Localidade").item(0).getTextContent();
			int numBilhetes = Integer.parseInt(eElement.getAttribute("numBilhetes"));

			//cria-se um Espectaculo
			Espetaculo espetaculo = new Espetaculo(nome, localidade, numBilhetes);

			//procura no node os artistas para adicionar no Espectaculo
		 	NodeList artistasNodes = eElement.getElementsByTagName("Artista");
			for (int i = 0; i < artistasNodes.getLength(); i++) {
				Node artistaNode = artistasNodes.item(i);
				String artista = artistaNode.getTextContent();
				espetaculo.addArtista(artista);
			}
			return espetaculo;
	}


	/**
	 *  Constroi um novo Element a partir do Espectaculo actual.
	 *  @param doc - o documento que irá gerar o novo Element
	 *  @return o elemento com as caracteristicas do Espectaculo
	 */
	public Element createElement(Document doc) {
			// Criar elemento para representar o Espetaculo
			Element espetaculoElement = doc.createElement("Espetaculo");

			// Definir atributos do Espetaculo
			espetaculoElement.setAttribute("Nome", this.getNome());
			espetaculoElement.setAttribute("Localidade", this.getLocalidade());
			espetaculoElement.setAttribute("numBilhetes", Integer.toString(this.getNumBilhetes()));
			Element artistasElement = doc.createElement("Artistas");

			// adiciona cada artista como um elemento filho de Artistas
			for (int i = 0; i < nArtistas; i++) {
				Element artistaElement = doc.createElement("Artista");
				artistaElement.setTextContent(artistas[i]);
				artistasElement.appendChild(artistaElement);
			}
			// adiciona o elemento Artistas como filho do nó espetaculoElement
			espetaculoElement.appendChild(artistasElement);

			return espetaculoElement;
		}
	}