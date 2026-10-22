/**
 * IASA 23/24
 *
 * Trabalho realizado por:
 * Fabio Pestana A50756
 *
 */

package iasa_jogo.src.agente;

/**
 * Criou-se a seguinte interface para assim criar um
 * contrato entre esta e as classes que a implementam
 * promovendo assim a organização do código de uma forma mais coesa,
 * através do encapsulamento. A utilização de interfaces também permite
 * uma melhor estruturação e manutenção do sistema, pois  classes que
 * implementam esta interface vão ter uma estrutura consistente e
 * atendam a um conjunto comum de requisitos (como o método processar()).
 *
 * A utilização de interfaces promove um acoplamento mais fraco,
 * pois as classes podem se conformar a "contratos" sem herdar
 * diretamente uma da outra.
 *
 * Esta interface tem uma relação de dependência com as
 * classes Percepcao e Accao, pois possui métodos
 * que usam instâncias destas classes (processar tem como
 * argumento uma Percepcao e retorna uma Accao).
 */

public interface Controlo {
    public Accao processar(Percepcao percepcao);
}
