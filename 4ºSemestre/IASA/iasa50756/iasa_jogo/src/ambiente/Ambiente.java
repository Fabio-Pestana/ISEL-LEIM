/**
 * IASA 23/24
 *
 * Trabalho realizado por:
 * Fabio Pestana A50756
 *
 */

package iasa_jogo.src.ambiente;

/**
 * Esta interface tem uma relação de dependência com as
 * classes Evento e Comando, pois possui métodos
 * que usam instâncias destas classes (observar retorna um Evento e
 * executar tem como argumento um Comando).
 */

public interface Ambiente {
    public void evoluir();
    public Evento observar();
    public void executar(Comando comando);
}