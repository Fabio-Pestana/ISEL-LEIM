package iasa_jogo.src.maqest;

import iasa_jogo.src.agente.Accao;

/**
 * Esta classe representa uma transição entre 2 estados. O
 * que permite a máquina de estados evoluir de um estado para
 * outro. Esta classe tem 2 atributos, o estado sucessor para
 * permitir a atualização do estado na classe MaquinaEstados e
 * tambem tem uma acão que será executada na transição entre estados.
 */

public class Transicao {
    private Accao accao;

    private Estado estadoSucessor;

    /**
     * Método construtor da Transição
     * @param estadoSucessor o estado sucessor para permitir a atualização do estado na classe MaquinaEstados
     * @param accao acão que será executada na transição entre estados
     */
    public Transicao(Estado estadoSucessor, Accao accao) {
        this.estadoSucessor = estadoSucessor;
        this.accao = accao;
    }

    public Accao getAccao() {
        return accao;
    }

    public Estado getEstadoSucessor() {
        return estadoSucessor;
    }
}
