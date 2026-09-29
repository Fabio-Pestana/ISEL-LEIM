package tp2.pack2Festivais;

/**
 * Classe que representa um Festival, que é um tipo de Evento.
 * Extende a classe abstrata Evento.
 */


import java.util.ArrayList;
import java.util.List;

public class Festival extends Evento
{
    Evento[] eventos = new Evento[20];

    int numEventos;

    /**
     * Construtor da classe Festival.
     * Chama o construtor da classe mãe Evento e inicializa as variáveis da classe.
     * @param nome o nome do festival
     */
    public Festival(String nome) {
        super(nome);
    }

    /**
     * Obtém o número total de bilhetes disponíveis para o festival, somando o número de bilhetes de cada evento.
     * @return o número total de bilhetes disponíveis para o festival
     */
    @Override
    public int getNumBilhetes() {
        int tickets=0;
        for (int i=0; i<numEventos; i++)
        {
            tickets = tickets + eventos[i].getNumBilhetes();
        }
        return tickets;
    }

    /**
     * Obtém o número de atuações de um artista no festival, somando o número de atuações deste em cada evento.
     * @param artista o nome do artista
     * @return o número total de atuações do artista no festival
     */
    @Override
    public int numActuacoes(String artista) {
        int contActuacoes = 0;
        for (int i=0; i<numEventos; i++)
        {
            contActuacoes = contActuacoes + this.eventos[i].numActuacoes(artista);

        }
        return contActuacoes;
    }

    /**
     * Obtém uma lista de todos os artistas que vão atuar no festival, sem repetições.
     * @return um array de strings com o nome de cada artista que vai atuar no festival
     */
    @Override
    public String[] getArtistas()
    {
        List<String> nomeArtista = new ArrayList<String>();
        for (int i = 0; i < numEventos; i++) {
            String[] artistas = eventos[i].getArtistas();
            for (int j = 0; j < artistas.length; j++) {
                if (!nomeArtista.contains(artistas[j])) {
                    nomeArtista.add(artistas[j]);
                }
            }
        }
        return nomeArtista.toArray(new String[nomeArtista.size()]);
    }

    /**
     * Método que retorna uma string representando as informações do festival, como pedido no enunciado.
     * @return Uma string representando as informações do festival.
     */
    public String toString()
    {
        String string_final = this.nome + "\n";
        for (int i=0; i<eventos.length; i++){
                if (eventos[i]!=null){
                    string_final = string_final + "\t" + eventos[i].toString() + "\n \n";
                }
        }
        return string_final;
    }

    /**
     * Obtém a profundidade do festival, que é o número máximo de festivais dentro do festival atual, sendo que o proprio não conta.
     * @return a profundidade do festival
     */
    public int getDeepFestival()
    {
        int prof = 0;

        for (int i = 0; i < eventos.length; i++) {
            if(eventos[i] != null && eventos[i] instanceof Festival) {

                Festival f = (Festival) eventos[i];
                prof = prof + f.getDeepFestival() + 1;
            }
        }
        return prof;
    }

    /**
     * Adiciona um novo evento ao festival.
     * @param evento O evento a ser adicionado.
     * @return true se o evento foi adicionado com sucesso, false caso contrário (se o festival já contém 20 eventos ou se verifique que o número de atuações no novo evento
     * supere em mais de duas o número de atuações no festival corrente).
     */
    public boolean addEvento(Evento evento)
    {
        if (numEventos >= eventos.length) //se já estiver cheio (20 eventos) nao se adiciona mais eventos
        {
            return false;
        }

        for (String artista : evento.getArtistas())
        {
            if (evento.numActuacoes(artista) > numActuacoes(artista) + 2)
            {
                return false;
            }
        }

        eventos[numEventos] = evento;
        numEventos++;
        return true;
    }

    /**
     * Remove um evento do festival.
     * @param nomeEvento O nome do evento a ser removido.
     * @return true se o evento foi removido com sucesso, false caso contrário (se não foi encontrado um evento com o nome especificado).
     */
    public boolean delEvento(String nomeEvento)
    {
        for (int i = 0; i < numEventos; i++) {
            if (this.eventos[i].nome.equals(nomeEvento)) {
                eventos[i] = null;
                numEventos--;
                return true;
            } else if (eventos[i] instanceof Festival) {
                Festival subFestival = (Festival) eventos[i];
                if (subFestival.delEvento(nomeEvento)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Main, para testar o código
     */
    public static void main(String[] args)
    {
        Festival RockInRio = new Festival("Rock in Rio");
        Espetaculo BEP = new Espetaculo("Black Eyed Peas", 4000, "Lisboa");
        String a1 = "Will.I.Am";
        String a2 = "Fergie";
        String a3 = "Taboo";
        BEP.addArtista(a1);
        BEP.addArtista(a2);
        BEP.addArtista(a3);
        RockInRio.addEvento(BEP);

        Espetaculo UB40 = new Espetaculo("UB40", 5000 , "Rio de Janeiro");
        String a4 = "Ali Campbell";
        String a5 = "Earl Falconer";
        String a6 = "Brian Travers";
        UB40.addArtista(a4);
        UB40.addArtista(a5);
        UB40.addArtista(a6);
        RockInRio.addEvento(UB40);

        Espetaculo XeP = new Espetaculo("Xutos e Pontapés", 3500 , "Lisboa");
        String a7 = "Zé Pedro";
        String a8 = "Tim";
        String a9 = "Joao";
        String a10 = "Kalú";
        XeP.addArtista(a7);
        XeP.addArtista(a8);
        XeP.addArtista(a9);
        XeP.addArtista(a10);
        RockInRio.addEvento(XeP);


        System.out.println(RockInRio);
        System.out.println("Numero de bilhetes do Rock in Rio: " + RockInRio.getNumBilhetes());
        System.out.println("Numero de atuações do Joao: " + RockInRio.numActuacoes("Joao"));
        System.out.println("Profundidade do festival Rock in Rio: " + RockInRio.getDeepFestival());


        Festival RollingLoud = new Festival("Rouling Loud");
        Espetaculo T = new Espetaculo("The Scotts", 4000, "Portimao");
        String c1 = "Travis";
        String c2 = "Scott";
        T.addArtista(c1);
        T.addArtista(c2);
        RollingLoud.addEvento(T);

        Espetaculo R = new Espetaculo("Rockstar", 3500 , "Porto");
        String c3 = "Da Baby";
        String c4 = "Lil Uzi Vert";
        R.addArtista(c3);
        R.addArtista(c4);
        RollingLoud.addEvento(R);

        Espetaculo  U = new Espetaculo("Watch this", 4000 , "Lisboa");
        String c5 = "Lil Uzi Vert";
        String c6 = "Polo G";
        String c7 = "Joao";
        U.addArtista(c5);
        U.addArtista(c6);
        U.addArtista(c7);
        RollingLoud.addEvento(U);

        System.out.println(RollingLoud);
        System.out.println("Numero de bilhetes do Rolling Loud: " + RollingLoud.getNumBilhetes());
        System.out.println("Numero de atuações do Lil Uzi Vert: " + RollingLoud.numActuacoes("Lil Uzi Vert"));
        System.out.println("Profundidade do festival Rolling Loud: " + RollingLoud.getDeepFestival());

        RockInRio.addEvento(RollingLoud);
        System.out.println(RockInRio);
        System.out.println("Profundidade do festival Rock in Rio: " + RockInRio.getDeepFestival());

        Festival Disney = new Festival("Disney");
        Espetaculo I = new Espetaculo("The Incredibles", 10000, "Orlando");
        String d1 = "Mr.Incredible";
        String d2 = "Ice";
        String d3 = "Elastigirl";
        I.addArtista(d1);
        I.addArtista(d2);
        I.addArtista(d3);
        Disney.addEvento(I);

        Espetaculo C = new Espetaculo("Cars", 7500 , "Orlando");
        String d4 = "Lightning McQueen";
        String d5 = "The King";
        String d6 = "Chick Hicks";
        C.addArtista(d4);
        C.addArtista(d5);
        C.addArtista(d6);
        Disney.addEvento(C);

        RockInRio.addEvento(Disney);

        System.out.println(RockInRio);
        System.out.println("Numero de bilhetes do Rock in Rio: " + RockInRio.getNumBilhetes());
        System.out.println("Numero de atuações do Joao: " + RockInRio.numActuacoes("Joao"));
        System.out.println("Profundidade do festival Rock in Rio: " + RockInRio.getDeepFestival());
        System.out.println("delete evento Watch this--->" + RockInRio.delEvento("Watch this"));
        System.out.println("delete evento Watch--->" + RockInRio.delEvento("Watch"));
        System.out.println(RockInRio);
        System.out.println("delete festival Rouling Loud--->" + RockInRio.delEvento("Rouling Loud"));
        System.out.println(RockInRio);
        System.out.println("Profundidade do festival Rock in Rio: " + RockInRio.getDeepFestival());
        System.out.println("Profundidade do festival Disney: " + Disney.getDeepFestival());

    }
}
