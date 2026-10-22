package iasa_jogo.src.jogo.ambiente;

import iasa_jogo.src.ambiente.Ambiente;
import iasa_jogo.src.ambiente.Comando;
import iasa_jogo.src.ambiente.Evento;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Esta classe tem uma relação de generalização com a
 * interface Ambiente (implementa esta interface), pois é
 * uma especialização de um Ambiente (ambiente virtual, do jogo).
 *
 * Criou-se um dicionário utilizando um HashMap, para assim no
 * método gerarEvento() não utilizarmos um switch case, diminuindo assim
 * a complexidade do código. Esta abordagem demonstra ser mais
 * simples, o que ajuda numa melhor compreensão e facilidade
 * na leitura do código.
 *
 * E caso se queira adicionar mais Eventos ao ambiente torna-se mais fácil e flexível com a estrutura de HashMap.
 * Basta adicionar uma nova entrada ao mapa sem a necessidade de modificar o código que faz a seleção do evento.
 * Isso torna o código mais independente e extensível, ou seja, facilita a adição de novas funcionalidades
 * sem causar efeitos indesejados.
 */

public class AmbienteJogo implements Ambiente {

    private EventoJogo evento;
    private Map<String, EventoJogo> eventos;

    /**
     * Método construtor do ambiente virtual do Jogo.
     * Onde interage-se com o user para lhe pedir um evento
     * dentro de um dicionário, este dicionário serve para simular
     * um evento que ocorre no ambiente virtual.
     */
    public AmbienteJogo() {

        eventos = new HashMap<String, EventoJogo>();
        eventos.put("s", EventoJogo.SILENCIO);
        eventos.put("r", EventoJogo.RUIDO);
        eventos.put("a", EventoJogo.ANIMAL);
        eventos.put("f", EventoJogo.FUGA);
        eventos.put("o", EventoJogo.FOTOGRAFIA);
        eventos.put("t", EventoJogo.TERMINAR);

        //this.evento = this.gerarEvento();
    }

    /**
     * Método onde se volta a pedir ao user um evento,
     * para simular um evento que ocorre no ambiente virtual.
     */
    @Override
    public void evoluir() {
        this.evento = this.gerarEvento();
    }


    /**
     * Método que mostra ao user o evento atual no ambiente,
     * caso seja null não mostra.
     * @return evento Evento atual do jogo
     */
    @Override
    public Evento observar() {
        if (evento != null){
            evento.mostrar();
        }
        return this.evento;
    }

    /**
     * Método que mostra ao user o evento atual no ambiente.
     */
    @Override
    public void executar(Comando comando) {
        comando.mostrar();
    }

    /**
     * Método que pede ao user um evento, segundo as keys do dicionário.
     */
    private EventoJogo gerarEvento(){
        System.out.println("\n Evento?");

        Scanner scanner = new Scanner(System.in);
        String comando = scanner.next();

        return eventos.get(comando);
    }

    /**
     * Como o atributo evento, segundo o UML é
     * "read only", este atributo privado tem apenas um get para permitir a leitura
     * e não tem set impedindo assim a alteração deste atributo.
     */
    public EventoJogo getEventoJogo() {
        return this.evento;
    }
}
