/**
 * IASA 23/24
 *
 * Trabalho realizado por:
 * Fabio Pestana A50756
 *
 */

package iasa_jogo.src.agente;

import iasa_jogo.src.ambiente.Ambiente;
import iasa_jogo.src.ambiente.Evento;

/**
 * Esta classe conhece a Interface Ambiente, pois na arquitetura
 * de apoio ao projeto fornecida pelos docentes existe
 * uma seta unidirecional de associação do UML, o que
 * indica que esta classe tem um atributo (ambiente)
 * que é uma instância da classe Ambiente.
 * Esta classe também é composta por um atributo do tipo Controlo
 * (controlo), devido a seta de composição do UML fornecido.
 */

public class Agente {
    private Ambiente ambiente;
    private Controlo controlo;

    /**
     * Método construtor do agente
     * @param ambiente
     * @param control
     */
    public Agente(Ambiente ambiente, Controlo control) {
        this.ambiente = ambiente;
        this.controlo = control;
    }

    /**
     * Método que aplica o modelo do agente fornecido pelos docentes.
     */
    public void executar(){
        Percepcao percepcao = percepcionar();
        Accao accao = this.controlo.processar(percepcao);
        actuar(accao);
    }

    /**
     * Método que simula a interpretação da informação (observa o ambiente)
     * para assim compreender a informação no ambiente (virtual)
     * @return new Percepcao(evento)
     */
    protected Percepcao percepcionar(){
        Evento evento = this.ambiente.observar();
        return new Percepcao(evento);
    }

    /**
     * Método que simula o agente executar uma acção
     */
    protected void actuar(Accao accao){
        if (accao != null){
            ambiente.executar(accao.getComando());
        }
    }
}
