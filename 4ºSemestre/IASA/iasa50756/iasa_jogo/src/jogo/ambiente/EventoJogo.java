package iasa_jogo.src.jogo.ambiente;

import iasa_jogo.src.ambiente.Evento;

/**
 * Esta classe tem uma relação de generalização com a
 * interface Evento (implementa esta interface), pois é
 * uma especialização de um Evento num jogo.
 * Como os eventos do jogo são um conjunto fixo, limitados e definidos
 * pelo programador, esta classe é do tipo JavaEnumeration (enum).
 */

public enum EventoJogo implements Evento {

    SILENCIO,
    RUIDO,
    ANIMAL,
    FUGA,
    FOTOGRAFIA,
    TERMINAR;

    /**
     * Método que mostra ao user o valor do Evento atual no jogo.
     */
    @Override
    public void mostrar() {
        System.out.printf("Percepção: %s\n", this);
    }
}
