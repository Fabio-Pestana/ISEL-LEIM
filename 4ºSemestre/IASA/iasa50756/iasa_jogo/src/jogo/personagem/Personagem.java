package iasa_jogo.src.jogo.personagem;

import iasa_jogo.src.agente.Agente;
import iasa_jogo.src.jogo.ambiente.AmbienteJogo;

/**
 * Esta classe tem uma relação de generalização com a
 * classe Agente, pois uma personagem é uma especialização de um agente
 * (agente do jogo), devido a isto no construtor desta classe é
 * necessário utilizar o super para chamar o construtor do agente. Esta
 * relação faz com que a classe personagem tenha uma dependência da classe
 * Agente, o que aumenta o nível de acoplamento.
 *
 * Esta classe também tem uma relação de dependência com as classes
 * AmbienteJogo e ControloPersonagem, sendo o AmbienteJogo um argumentos no
 * construtor e o ControloPersonagem é feita uma nova instância
 * para depois ambos serem utilizados pelo Agente. A relação entre estas
 * classes cria um maior acoplamento, no entanto este é necessário pois a
 * Personagem necessita de conhecer o Ambiente virtual onde se encontra e necessita
 * de um Controlo.
 *
 * Esta classe representa um agente inteligente dentro de um abiente virtual
 * (personagem de um jogo). Consider-se este agente inteligente pois ele
 * processa a informação que obtem do ambiente (um evento) e actua sobre esta
 * (um comando).
 */

public class Personagem extends Agente {


    /**
     * Método construtor da personagem
     * @param ambiente
     */
    public Personagem(AmbienteJogo ambiente) {
        super(ambiente, new ControloPersonagem());
    }
}
