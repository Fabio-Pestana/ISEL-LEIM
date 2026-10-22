/**
 * IASA 23/24
 *
 * Trabalho realizado por:
 * Fabio Pestana A50756
 *
 */

package iasa_jogo.src.agente;

import iasa_jogo.src.ambiente.Comando;

/**
 * Esta classe conhece a Interface Comando, pois na arquitetura
 * de apoio ao projeto fornecida pelos docentes existe
 * uma seta unidirecional de associação do UML, o que
 * indica que esta classe tem um atributo (comando)
 * que é uma instância da classe Comando.
 */

public class Accao {

    private Comando comando;

    /**
     * Construtor da classe
     * @param comando
     */
    public Accao(Comando comando) {
        this.comando = comando;
    }

    /**
     * Como o atributo comando, segundo o UML é
     * "read only", este atributo privado tem apenas um get para permitir a leitura
     * e não tem set impedindo assim a alteração deste atributo.
     */
    public Comando getComando(){
        return this.comando;
    }
}
