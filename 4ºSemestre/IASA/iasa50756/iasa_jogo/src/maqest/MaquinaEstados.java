package iasa_jogo.src.maqest;

import iasa_jogo.src.agente.Accao;
import iasa_jogo.src.ambiente.Evento;

/**
 * Esta classe representa uma Máquina de estados. Estas
 * máquinas são utilizadas para representar um sistema
 * computacional. Nesta classe "criamos" uma máquina
 * de estados com um estado inicial.
 * A dinâmica de um sistema é criada através dos vários
 * estados que o sistema pode ter e a forma como ele
 * evolui ao longo do tempo, nesta classe a atualização
 * do estado é feita no método processar(), onde o estado é
 * atualizado através do valor do estado atual e
 * pela valor da entrada (Evento que ocorre no ambiente).
 *
 * Utilizando a factorização e modularização colocou-se
 * grande parte da complexidade do sistema nesta classe.
 * A desvantagem desta implementação é que não aceita guardas
 * nas transições de estados.
 */

public class MaquinaEstados {
    private Estado estado;

    /**
     * Método construtor da máquina de estados.
     *
     * @param estadoInicial estado inicial da máquina de estados
     */
    public MaquinaEstados(Estado estadoInicial) {
        this.estado = estadoInicial;
    }

    /**
     * Método que processa o evento que ocorreu no ambiente,
     * atualiza o estado da máquina de estados, caso exista uma transição
     * e retorna a acção que o agente deve tomar.
     *
     * @param evento evento que ocorreu no ambiente.
     * @return ação a tomar pelo agente (null caso não haja uma transição)
     */
    public Accao processar(Evento evento){
        Transicao transicao = this.estado.processar(evento);
        if (transicao != null) //Guarda
        {
            this.estado = transicao.getEstadoSucessor();
            return transicao.getAccao();
        }else {
            return null;
        }
    }

    public Estado getEstado() {
        return estado;
    }
}
