package iasa_jogo.src.jogo.ambiente;

import iasa_jogo.src.ambiente.Comando;

/**
 * Esta classe tem uma relação de generalização com a
 * interface Comando (implementa esta interface), pois é
 * uma especialização de um Comando num jogo.
 * Como os comandos do jogo são um conjunto fixo, limitados e definidos
 * pelo programador, esta classe é do tipo JavaEnumeration (enum).
 *
 * Estes comandos representam as várias accões que um um agente inteligente
 * pode fazer depois de processar a informação do ambiente (virtual, o jogo).
 */

public enum ComandoJogo implements Comando {

    PROCURAR,
    APROXIMAR,
    OBSERVAR,
    FOTOGRAFAR;


    /**
     * A anotação @Override é usada para indicar que um
     * método está substituindo um método de uma interface (neste caso).
     * Embora seja opcional, é uma boa
     * prática utilizá-la sempre que se substitui
     * um método para garantir que não estamos a criar um novo
     * método por engano (engano no nome do método ou mudança da
     * assinatura do método, como por exemplo adicionar argumentos).
     *
     * Método que mostra ao user o valor do Comando atual no jogo.
     */
    @Override
    public void mostrar() {
        System.out.printf("Acção: %s\n", this);
    }
}
