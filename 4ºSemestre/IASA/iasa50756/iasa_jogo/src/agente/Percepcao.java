/**
 * IASA 23/24
 *
 * Trabalho realizado por:
 * Fabio Pestana A50756
 *
 */

package iasa_jogo.src.agente;

import iasa_jogo.src.ambiente.Evento;

/**
 * Esta classe conhece a Interface Evento, pois na arquitetura
 * de apoio ao projeto fornecida pelos docentes existe
 * uma seta unidirecional de associação do UML, o que
 * indica que esta classe tem um atributo (evento)
 * que é uma instância da classe Evento.
 */
public class Percepcao {

    private Evento evento;

    /**
     * Construtor da classe
     * @param evento
     */
    public Percepcao(Evento evento) {
        this.evento = evento;
    }

    /**
     * Como o atributo evento, segundo o UML é
     * "read only", este atributo privado tem apenas um get para permitir a leitura
     * e não tem set impedindo assim a alteração deste atributo.
     */
    public Evento getEvento(){
        return this.evento;
    }
}
