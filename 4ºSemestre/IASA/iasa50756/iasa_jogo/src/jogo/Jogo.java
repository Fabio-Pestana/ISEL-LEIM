package iasa_jogo.src.jogo;

import iasa_jogo.src.jogo.ambiente.AmbienteJogo;
import iasa_jogo.src.jogo.ambiente.EventoJogo;
import iasa_jogo.src.jogo.personagem.Personagem;

/**
 * Esta classe tem uma relação de composição com as classes
 * Personagem e AmbienteJogo para assim simular o ambiente virtual
 * tal como a sua interacao com uma personagem.
 */

public class Jogo {

    static Personagem personagem;

    static AmbienteJogo ambiente;

    /**
     * Método é privado pois é apenas usado
     * "internamente" no método main. Utilizando assim o encapsulamento
     * para diminuir o acoplamento (diminuindo o grau de dependência).
     *
     * Método que simula um jogo através de inputs de user (simula eventos no
     * ambiente virtual), através do método evoluir do ambiente e a personagem
     * através do seu ciclo percepção-processamento-acção, irá percepcionar o
     * Evento que ocorre no ambiente e tomar a acção segundo ele, através do
     * seu método executar.
     */
    private static void executar(){
        do {
            ambiente.evoluir();
            personagem.executar();
        }while (ambiente.getEventoJogo() != EventoJogo.TERMINAR);
    }


    public static void main(String[] args) {
        ambiente = new AmbienteJogo();
        personagem = new Personagem(ambiente);
        executar();
    }
}
