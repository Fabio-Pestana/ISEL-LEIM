package iasa_jogo.src.jogo.personagem;

import iasa_jogo.src.agente.Accao;
import iasa_jogo.src.agente.Controlo;
import iasa_jogo.src.agente.Percepcao;
import iasa_jogo.src.ambiente.Evento;
import iasa_jogo.src.jogo.ambiente.ComandoJogo;
import iasa_jogo.src.jogo.ambiente.EventoJogo;
import iasa_jogo.src.maqest.Estado;
import iasa_jogo.src.maqest.MaquinaEstados;

/**
 * Esta classe utiliza uma máquina de estados para
 * representar o comportamento de uma personagem num
 * ambiente virtual. Nesta classe apenas aplicamos os
 * vários estados e as suas transições para representarmos
 * a máquina de estados que na arquitetura
 * de apoio ao projeto fornecida pelos docentes é nos apresentada.
 *
 * Nesta classe é feito o processamento da informação do ambiente
 * (percepção do ambiente), e toma-se a decisão da ação adequada
 * segundo as condições no ambiente (evento atual), é nesta classe
 * que a personagem ou seja o nosso agente, dentro do jogo, se torna
 * inteligente seguindo o ciclo percepção-processamento-ação.
 */
public class ControloPersonagem implements Controlo {

    private MaquinaEstados maquinaEstados;

    /**
     * Método construtor do controlo da personagem.
     * Onde se define todos os estados possiveis da personagem, para cada
     * estado define-se os seus estados transitórios, utilizando uma máquina
     * de estados. Durante o jogo, esta máquina de estados estará em cargo de
     * controlar as transições entre estados atualizando o estado atual da máquina
     * dependendo do evento que ocorre no jogo (o que a personagem percepciona).
     */
    public ControloPersonagem() {
        // Definir estados
        Estado procura = new Estado("Procura");
        Estado inspeccao = new Estado("Inspecção");
        Estado observacao = new Estado("Observação");
        Estado registo = new Estado("Registo");

        // Definir acções
        Accao procurar = new Accao(ComandoJogo.PROCURAR);
        Accao aproximar = new Accao(ComandoJogo.APROXIMAR);
        Accao observar = new Accao(ComandoJogo.OBSERVAR);
        Accao fotografar = new Accao(ComandoJogo.FOTOGRAFAR);

        // Definir transições
        procura
                .transicao(EventoJogo.RUIDO, inspeccao, aproximar)
                .transicao(EventoJogo.ANIMAL, observacao, aproximar)
                .transicao(EventoJogo.SILENCIO, procura, procurar);

        inspeccao
                .transicao(EventoJogo.RUIDO, inspeccao, procurar)
                .transicao(EventoJogo.ANIMAL, observacao, aproximar)
                .transicao(EventoJogo.SILENCIO, procura);

        observacao
                .transicao(EventoJogo.ANIMAL, registo, observar)
                .transicao(EventoJogo.FUGA, inspeccao);

        registo
                .transicao(EventoJogo.FUGA, procura)
                .transicao(EventoJogo.ANIMAL, registo, fotografar)
                .transicao(EventoJogo.FOTOGRAFIA, procura);

        this.maquinaEstados = new MaquinaEstados(procura);
    }

    /**
     * Método que recebe a informação percepcionada pela personagem
     * no jogo e processa essa informação, para obter a acção que a
     * a personagem tem de tomar.
     *
     * @param percepcao informação percepcionada pela personagem
     * @return acção que a personagem toma
     */
    @Override
    public Accao processar(Percepcao percepcao) {
        Evento evento = percepcao.getEvento();
        Accao accao = maquinaEstados.processar(evento);
        mostrar();
        return accao;
    }

    /**
     * Método que mostra ao user, na consola, o estado atual da personagem.
     */
    private void mostrar(){
        System.out.printf("Estado: %s\n", getEstado().getNome());
    }

    /**
     * Usa-se delegação para chamar o estado da máquina de estados
     * criando redundância, ou seja, este método "pede" a maquina
     * de estados o seu estado atual.
     */
    public Estado getEstado(){
        return maquinaEstados.getEstado();
    }
}
