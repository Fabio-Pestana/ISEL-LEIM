package tp3.XML;

import java.util.Arrays;

import org.w3c.dom.*;

/**
 * Classe abstracta que representa um Evento.
 * 
 * @version 1.0 
 * @author Docentes da Disciplina de Modelação e Programação, LEIM, Instituto Superior de Engenharia de Lisboa
 *
 */
public abstract class Evento {

	private String nome;

	public Evento(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public abstract int getNumBilhetes();

	public abstract String[] getArtistas();

	public abstract int numActuacoes(String artista);

	/**
	 * Cria um novo Elemento contendo todas as informações deste Evento.
	 * @param doc o documento que irá gerar o novo Elemento.
	 * @return um Elemento que represnta o Evento na arvore XML
	 */
	public abstract Element createElement(Document doc);

	/**
	 * Retorna uma String contendo informações sobre o Evento.
	 * Nota: Ver o ficheiro OutputPretendido.txt
	 */
	public String toString() {
		String string_final = "";
		string_final = getNome() + " com " + getNumBilhetes() + " e com os artistas " + String.join(", ", Arrays.toString(getArtistas()));
		return string_final;
	}

	/**
	 * Escreve, para a consola, o prefixo seguido da String representativa do Evento.
	 * @param prefix - Um prefixo para gerar a identação apropriada de acordo com a "profundidade".
	 */
	public void print(String prefix) {
		System.out.println(prefix + toString());
	}

	/**
	 * Constroi um novo Evento (Espetáculo ou Festival) a partir do nome no nó
	 * nNode que foi lido na arvore XML.
	 * @param nNode o nó/elemento contendo a informação do Evento.
	 * @return o novo Evento associado a esse nNode.
	 */
	public static Evento build(Node nNode) {
		if (nNode.getNodeName().equals("Festival")) {
			return Festival.build(nNode);
		} else if (nNode.getNodeName().equals("Espetaculo")) {
			return Espetaculo.build(nNode);
		} else {
			return null;
		}
	}
}