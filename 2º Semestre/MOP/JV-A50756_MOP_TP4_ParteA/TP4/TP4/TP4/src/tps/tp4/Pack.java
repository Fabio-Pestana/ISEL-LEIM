package tps.tp4;

import java.time.LocalDate;

/**
 * @author Fabio Pestana - A50756
 * ISEL - LEIM 22/23
 */

public class Pack {

    private int numeroDoPack;

    double price;

    /**
     * Constrói um objeto Pack com os atributos especificados.
     * @param numeroDoPack O número do pacote a ser criado.
     * @throws IllegalArgumentException se o número do pacote fornecido for inválido.
     */
    public Pack(int numeroDoPack) {
        if (numeroDoPack>2 || numeroDoPack<1) //só existem dois metodos (1 e 2)
            throw new IllegalArgumentException("Metodo de pagamento invalido");
        this.numeroDoPack = numeroDoPack;
    }

    /**
     * Obtém o número do pacote.
     * @return número.
     */
    public int getNumeroDoPack() {
        return numeroDoPack;
    }


    /**
     * Atualiza o número do pacote.
     * @param newDoPack novo número.
     */
    public void setNumeroDoPack(int newDoPack) {
        if (newDoPack <= 2 && newDoPack >= 1)
            this.numeroDoPack = newDoPack;
    }

    /**
     * Define o preço do pacote com base no número do pacote atual.
     * numeroDoPack = 1, preco base do ginasio (sem acesso a Personal Trainer)
     * numeroDoPack = 2, preco com aulas personalizadas (com Personal Trainer)
     */
    public double setPrice() {
        if (this.numeroDoPack == 1)
        {
            this.price = 25;
        }else if (this.numeroDoPack == 2)
        {
            this.price = 35;
        }
        return this.price;
    }

    /**
     * Obtém a data de expiração do contrato com base na data de inscrição (adiciona um mês).
     * @param dataInscricao data de inscrição.
     * @return data de expiração.
     */
    public LocalDate getDataExpiracaoContrato(LocalDate dataInscricao) {
        return dataInscricao.plusMonths(1);
    }
}
