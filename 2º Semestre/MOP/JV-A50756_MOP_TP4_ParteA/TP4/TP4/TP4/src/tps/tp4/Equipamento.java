package tps.tp4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Fabio Pestana - A50756
 * ISEL - LEIM 22/23
 */

public class Equipamento {
    private String nome;
    private LocalDate dataDeCompra;
    LocalDate Manutencao;
    private int custo;
    boolean precisaDeManutencao;

    /**
     * Constrói um objeto Equipamento com os atributos especificados..
     * @param nome nome do equipamento.
     * @param dataDeCompra data de compra do equipamento.
     * @param custo custo do equipamento.
     */
    public Equipamento(String nome, LocalDate dataDeCompra, int custo) {
        this.nome = nome;
        this.dataDeCompra = dataDeCompra;
        this.Manutencao = dataDeCompra;
        this.custo = custo;
    }

    /**
     * Retorna o nome do equipamento.
     * @return nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Altera o nome do equipamento.
     * @param nome O novo nome a ser definida.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna a data de compra do equipamento.
     * @return data.
     */
    public LocalDate getDataDeCompra() {
        return this.dataDeCompra;
    }

    /**
     * Atualiza a data de compra do equipamento.
     * @param newData nova data.
     */
    public void setDataDeCompra(LocalDate newData){this.dataDeCompra = newData;}

    /**
     * Retorna o custo do equipamento.
     * @return custo.
     */
    public int getCusto() {
        return this.custo;
    }

    /**
     * Verifica se o equipamento precisa de manutenção (se tiver mais de dois anos).
     * @return true se o equipamento precisa de manutenção, false caso contrário.
     */
    public boolean isPrecisaDeManutencao() {
        int ano = dataDeCompra.getYear();
        LocalDate dataAtual = LocalDate.now();
        int anoAtual = dataAtual.getYear();
        if (anoAtual-ano >= 2)
        {
            precisaDeManutencao = true;
        } else
            precisaDeManutencao = false;
        return precisaDeManutencao;
    }

    /**
     * Atualiza a data de manutenção do equipamento.
     * @return nova data.
     */
    public LocalDate manutencaoUptade(){
        if (isPrecisaDeManutencao()) {
            this.Manutencao = LocalDate.now();
            precisaDeManutencao = false;
        }
        else
            this.Manutencao = this.getDataDeCompra();
        return this.Manutencao;
    }

    /**
     * Retorna o preço da manutenção do equipamento (5 por cento do custo do equipamento).
     * @return preço.
     */
    public double precoManutencao()
    {
        return this.getCusto()*0.05;
    }

    /**
     * Cria um elemento XML com base nos dados do equipamento.
     * @param doc O documento XML.
     * @return O elemento criado.
     */
    public Element createElement(Document doc) {

        Element equipElement = doc.createElement("Machine");

        Element nomeElement = doc.createElement("Nome");
        nomeElement.setTextContent(this.getNome());
        equipElement.appendChild(nomeElement);

        Element dataElement = doc.createElement("DataDeCompra");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");// Definir o formato da string desejada
        dataElement.setTextContent(this.getDataDeCompra().format(formatter));// Converter LocalDate para String
        equipElement.appendChild(dataElement);

        Element custoElement = doc.createElement("Custo");
        custoElement.setTextContent(Integer.toString(this.getCusto()));
        equipElement.appendChild(custoElement);


        return equipElement;
    }

    /**
     * Retorna uma string com os dados do equipamento.
     * @return string com os dados do objeto.
     */
    public String toString() {
        return "Equipamento = " + this.getNome() +
                ", dataDeCompra = " + this.getDataDeCompra() +
                ", Manutencao = " + this.manutencaoUptade() +
                ", custo = " + this.getCusto() +
                ", precisaDeManutencao = " + this.precisaDeManutencao +
                ", preco de manutencao = " + this.precoManutencao();
    }
}

