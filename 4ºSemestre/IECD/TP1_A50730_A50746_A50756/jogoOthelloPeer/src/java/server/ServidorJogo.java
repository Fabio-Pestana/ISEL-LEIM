package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


class ServidorDedicado extends Thread {

    // **Atributos:**
	
	// Tempo máximo para jogar
	final int timeout = 1000*900;  // 0 - sem timeout

    /**
     * Socket da ligação com o jogador X.
     */
    private Socket connectionX = null; 

    /**
     * Socket da ligação com o jogador O.
     */
    private Socket connectionO = null; 

    // **Construtor:**

    /**
     * Manipula os circuitos virtuais dos jogadores.
     *
     * @param connection1 Socket do jogador X.
     * @param connection2 Socket do jogador O.
     */
    public ServidorDedicado(Socket connection1, Socket connection2) {
        this.connectionX = connection1;
        this.connectionO = connection2;
    }

    /**
     * Método executado pela thread para gerir um jogo.
     * 
     * Este método é responsável por:
     * * Criar streams de leitura e escrita para os sockets.
     * * Iniciar o jogo.
     * * Gerir o ciclo de jogadas entre os jogadores.
     * * Fechar os sockets quando o jogo termina.
     */
    public void run() {

        try (
            // Cria streams para leitura e escrita de dados nos sockets
        	
        	// **Socket X:**
            // Stream para ler dados do socket X.
            BufferedReader isX = new BufferedReader(new InputStreamReader(connectionX.getInputStream()));
            
        	// Stream para escrever dados no socket X.
            PrintWriter osX = new PrintWriter(connectionX.getOutputStream(), true);
        	
        	// **Socket O:**
            // Stream para ler dados do socket O.
            BufferedReader isO = new BufferedReader(new InputStreamReader(connectionO.getInputStream()));
            
        	// Stream para escrever dados no socket X.
            PrintWriter osO = new PrintWriter(connectionO.getOutputStream(), true);
        ) {
        	// Define timeout para inatvidade
        	connectionX.setSoTimeout(timeout);
        	connectionO.setSoTimeout(timeout);
        	
            // **Informação sobre a thread**
            System.out.println("Iniciou a Thread ("+ this.getId()+") do servidor dedicado:");

            // **Criação do jogo**

            // Cria uma nova instância do jogo.
            OthelloXML jogo = new OthelloXML();

            // Ciclo para gerir a interação entre jogadores

            for(;;) {

                // ** Jogador X **
            	// **Obter tabuleiro:**
                // Atualiza o tabuleiro e o estado do jogo do jogador X.
            	SrvStub.runObter(isX, osX, "X ", connectionX, jogo);
            	
            	// **Processar jogada:**
                // Se o jogo não terminou e se X tem jogadas válidas, concretiza a jogada do jogador X.Se o jogo já terminou atualiza o user
                if(!jogo.terminou() && jogo.getValidMoves("X ").length != 0) {
                	jogo = SrvStub.runJogar(isX, osX, "X ", connectionX, jogo);
                }else if(jogo.terminou()){
                	SrvStub.runUpdateUser(isX, osX, "X ", connectionX);
                }

                // ** Jogador O **
                // **Obter tabuleiro:**
                // Atualiza o tabuleiro e o estado do jogo do jogador O.
                SrvStub.runObter(isO, osO, "O ", connectionO, jogo);
                
                // **Processar jogada:**
                //  Se o jogo não terminou e se O tem jogadas válidas, concretiza a jogada do jogador O.Se o jogo já terminou atualiza o user
                if(!jogo.terminou()  && jogo.getValidMoves("O ").length != 0) {
                	jogo = SrvStub.runJogar(isO, osO, "O ", connectionO, jogo);
                }else if(jogo.terminou()){
                	SrvStub.runUpdateUser(isO, osO, "O ", connectionO);
                }
                
            }
		} catch (Exception e) {
			System.out.println("Servidor dedicado: terminou o jogo ("+e.getLocalizedMessage()+")!");
			// e.printStackTrace();
		} finally {
			// Garante que os sockets são fechados, mesmo em caso de exceção
			try {
				connectionX.close();
				connectionO.close();
			} catch (IOException e) {
				// Ignora a exceção caso ocorra algum erro ao fechar
			}
		}
		System.out.println("Servidor dedicado: terminou a Thread ("+ this.getId()+") do servidor dedicado!");
	} 
} 