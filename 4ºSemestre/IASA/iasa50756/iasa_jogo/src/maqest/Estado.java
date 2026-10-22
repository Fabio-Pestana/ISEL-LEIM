package iasa_jogo.src.maqest;

import iasa_jogo.src.agente.Accao;
import iasa_jogo.src.ambiente.Evento;
import iasa_jogo.src.jogo.ambiente.EventoJogo;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta classe representa um estado numa Máquina de estados.
 * Onde temos um atributo onde guardamos o seu nome e temos
 * um Map onde guardamos as transições entre este estado e outros.
 * Nestes Map´s guardamos como key o evento que faz a alteração entre
 * estados e como value temos um objeto Transição, ou seja um objeto
 * que guarda informação sobre o estado Sucessor e a sua ação
 * (função de saída).
 *
 * Esta classe tem um método transicao() com 2 assinaturas,
 * o metodo com 2 parâmetros representa uma Mudança de estados
 * (representa o delta) e o com 3 parâmetros representa a função
 * de saída (representa o lambda).
 */

public class Estado{

    private String nome;
    private Map<Evento, Transicao> transicoes;

    /**
     * Método construtor de um estado de uma máquina de estados.
     *
     * @param nome nome do estado
     */
    public Estado(String nome) {
        this.nome = nome;
        transicoes = new HashMap<Evento, Transicao>();
    }

    /**
     * Método que retorna uma transição segundo o seu evento.
     *
     * @param evento evento que pode ocorrer no ambiente.
     * @return a transição de um evento.
     */
    public Transicao processar(Evento evento){
        return transicoes.get(evento);
    }

    /**
     * Usando a Delegação usa-se o método transição com a assinatura
     * com 3 parâmetros, onde passamos no último argumento um null.
     * Método que permite adicionar uma nova transicao ao estado.
     *
     * @param evento evento que pode ocorrer no ambiente.
     * @param estadoSucessor futuro estado caso o evento ocorra no ambiente
     */
    public Estado transicao(Evento evento, Estado estadoSucessor){
        return transicao(evento, estadoSucessor, null);
    }

    /**
     * Método que permite adicionar uma nova transicao ao estado.
     *
     * @param evento evento que pode ocorrer no ambiente.
     * @param estadoSucessor futuro estado caso o evento ocorra no ambiente
     * @param accao ação que o agente toma na transição
     * @return  o próprio estado
     */
    public Estado transicao(Evento evento, Estado estadoSucessor, Accao accao){
        transicoes.put(evento, new Transicao(estadoSucessor, accao));
        return this;
    }

    /**
     * Método que retorna o nome do estado.
     * Como o atributo público aumenta o acuplamento, decidiu-se
     * alterar o atributo para private, permitindo apenas leituras
     * deixando o nome "read only".
     *
     * @return nome do estado
     */
    public String getNome() {
        return nome;
    }
}
