package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Gera e mostra endereços IP na rede local
 */
public class GeradorIP {

    /**
     * @param args 			Não usado
     * @throws UnknownHostException 	Em caso de erro
     */
    public static void main(String[] args) throws UnknownHostException {
	int inicioIntervalo = 1; 	// Valor do último byte do endereço de início do intervalo
	int fimIntervalo = 254; 	// Valor do último byte do endereço de fim do intervalo

	try {
	    listaEndercosIP(inicioIntervalo, fimIntervalo);
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @return Endereço base da rede ex: "192.168.1."
     * @throws UnknownHostException Em caso de erro
     */
    public static String obterEnderecoRedeBase() throws UnknownHostException {
	// Obter o endereço IP local
	InetAddress enderecoLocal = InetAddress.getLocalHost();
	byte[] enderecoBytes = enderecoLocal.getAddress();

	// Obter a máscara de sub-rede como um array de bytes
	InetAddress mascaraSubrede = InetAddress.getByName(obterMascaraSubrede());
	byte[] mascaraBytes = mascaraSubrede.getAddress();

	// Calcular o endereço base da rede
	byte[] enderecoRedeBytes = new byte[enderecoBytes.length];
	for (int i = 0; i < enderecoBytes.length; i++) {
	    enderecoRedeBytes[i] = (byte) (enderecoBytes[i] & mascaraBytes[i]);
	}

	// Converter o endereço base da rede para uma string
	InetAddress enderecoRede = InetAddress.getByAddress(enderecoRedeBytes);
	return enderecoRede.getHostAddress();
    }

    // Método auxiliar para obter a máscara de sub-rede como uma string
    private static String obterMascaraSubrede() {
	return "255.255.255.0"; // Ajuste conforme a máscara de sub-rede da sua rede
    }

    /**
     * Gera um endereço IP
     * 
     * @param indice 			Número do endereço IP
     * @return 				Endereço IP gerado
     * @throws UnknownHostException 	Em caso de erro
     */
    public static String gerarEnderecoIP(int indice) throws UnknownHostException {
	String endereco = obterEnderecoRedeBase() + indice;
	InetAddress inetAddress = InetAddress.getByName(endereco);
	return inetAddress.getHostAddress();
    }

    /**
     * Mostra os endereços IP no intervalo indicado
     * 
     * @param inicio 			Número de início do intervalo
     * @param fim    			Número de fim do intervalo
     * @throws UnknownHostException 	Em caso de erro
     */
    private static void listaEndercosIP(int inicio, int fim) throws UnknownHostException {
	for (int i = inicio; i <= fim; i++) {
	    System.out.println("Endereço IP: " + gerarEnderecoIP(i));
	}
    }
}
