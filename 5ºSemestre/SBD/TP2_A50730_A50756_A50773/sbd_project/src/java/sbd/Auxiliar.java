package pt.isel.leim.sbd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Auxiliar {
	
	public static boolean isNameValid(String name)
    {
        boolean check = false;
        
        for(int i=0; i< name.length(); i++)
        {
        	if(Character.isWhitespace(name.charAt(i)) || Character.isLetter(name.charAt(i)))
                check = true;
            else
                return false;
        }
            return check;
    }
	
	public static String normalizaTexto(String texto) {
        String[] palavras = texto.split("\\s+");
        StringBuilder textoNormalizado = new StringBuilder();

        for (String palavra : palavras) {
            if (palavra.length() > 0) {
                String palavraNormalizada = palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase();
                textoNormalizado.append(palavraNormalizada).append(" ");
            }
        }

        return textoNormalizado.toString().trim();
    }
	
	public static boolean isNumberValid(String number)
    {
        boolean check = false;
        if (number ==null || !(number.length()<15 && number.length()>=8))
            return false;
        else {
            for(int i = 0; i< number.length(); i++)
            {
                if(Character.isDigit(number.charAt(i)))
                    check = true;
                else
                    return false;
            }
            return check;
        }
    }
	
	public static boolean isValidEmailAddress(String email) {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
	}
	
	public static boolean verificarMatricula(Stub stub, String matricula) {
		Connection conn = stub.getConnection();
        String resultado = null;  
    	
    	String query = "" //
				+ "SELECT * " //
				+ "FROM Veiculo "
				+ "WHERE Matricula = ?";
    	
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, matricula);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
	                resultado = rs.getString("Matricula");
	            }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        stub.closeConnection(conn);
        
        return resultado != null;
	}
	
	public static boolean verificarMatriculaDisponivel(Stub stub, String matricula) {
		Connection conn = stub.getConnection();
        String resultado = null;  
    	
    	String query = "" //
				+ "SELECT * " //
				+ "FROM Veiculo "
				+ "WHERE Matricula = ? And Estado = 'disponivel'";
    	
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, matricula);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
	                resultado = rs.getString("Matricula");
	            }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        stub.closeConnection(conn);
        
        return resultado != null;
	}
	
	public static boolean verificarNIF(Stub stub, String nif) {
		
		Connection conn = stub.getConnection();
        String resultado = null;  
    	
    	String query = "" //
				+ "SELECT * " //
				+ "FROM Cliente "
				+ "WHERE NIF = ?";
    	
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, nif);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
	                resultado = rs.getString("NIF");
	            }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        stub.closeConnection(conn);
        
        return resultado != null;
	}
	
	public static boolean verificarCartaConducao(Stub stub, String cc) {
		Connection conn = stub.getConnection();
        String resultado = null;  
    	
    	String query = "" //
				+ "SELECT * " //
				+ "FROM Cliente_Condutor "
				+ "WHERE NumeroCartaConducao = ?";
    	
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, cc);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
	                resultado = rs.getString("NumeroCartaConducao");
	            }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        stub.closeConnection(conn);
        return resultado != null;
	}
	
	public static String[][] listarParques(Stub stub) {

		String query = "" //
				+ "SELECT Morada " //
				+ "FROM ParqueEstacionamento ";

		String[][] results = stub.executeQuery(query, null);
		return results;
	}
	
	public static String[][] listarParquesWeb(Stub stub) {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		String query = "" //
				+ "SELECT Morada " //
				+ "FROM ParqueEstacionamento ";

		String[][] results = stub.executeQuery(query, null);
		return results;
	}
	
	public static String[][] listarCategoriasWeb(Stub stub) {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		String query = "" //
				+ "SELECT Categoria " //
				+ "FROM TipoVeiculo ";

		String[][] results = stub.executeQuery(query, null);
		return results;
	}
	
	
	public static String[][] listarParquesDadosWeb(Stub stub) {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		String query = "" //
				+ "SELECT Morada, Latitude, Longitude " //
				+ "FROM ParqueEstacionamento ";

		String[][] results = stub.executeQuery(query, null);
		return results;
	}
	
	public static String[][] tiposVeiculos(Stub stub){
		
		String query = "" //
				+ "SELECT Categoria " //
				+ "FROM TipoVeiculo";

		String[][] results = stub.executeQuery(query, null);
		return results;
	}
	
	
	public static String gerarIdReserva(Stub stub) throws SQLException {
		String idReserva = stub.gerarId("RESE");
		
		// Verificar se o ID já existe na base de dados
        while (idReservaExiste(stub, idReserva)) {
            idReserva = stub.gerarId("RESE"); // Gerar novo ID se já existir
        }
        
        return idReserva;
	}
	
	
	public static boolean idReservaExiste(Stub stub, String idReserva) throws SQLException {
    	Connection conn = stub.getConnection();
        String sql = "SELECT COUNT(*) FROM Reserva WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idReserva);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true se o ID já existe
            }
        }
		stub.closeConnection(conn);
        return false; // Retorna false se o ID não existe
    }
	
	public static boolean idAluguerExiste(Stub stub, String idAluguer) throws SQLException {
    	Connection conn = stub.getConnection();
        String sql = ""
        		+ "SELECT COUNT(*) "
        		+ "FROM Aluguer "
        		+ "WHERE ID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idAluguer);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true se o ID já existe
            }
        }
        stub.closeConnection(conn);
        return false; // Retorna false se o ID não existe
    }
	
	
	
	public static void printResults(String[][] results) {
		if(results.length != 0)
		{
			for (String[] row : results) {
		        for (String cell : row) {
		            System.out.print(cell + " | ");
		        }
		        System.out.println();
		    }
		}else {
			System.out.println("Nao foram encontrados resultados");
		}
	}
	
	public static boolean verificaCartaAssociada(Stub stub, String nif, String numeroCarta){
		Connection conn = stub.getConnection();
	    String query = "SELECT COUNT(*) AS Total " +
	                   "FROM Cliente_Condutor " +
	                   "WHERE NIF = ? AND NumeroCartaConducao = ?";
	    
	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nif);
            stmt.setString(2, numeroCarta);

            try(ResultSet rs = stmt.executeQuery()){
            	if (rs.next()) {
                    return rs.getInt(1) > 0; 
                }
            }
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        stub.closeConnection(conn);
	    }
        return false; 
	}
	
	
	public static String generateHtmlTable(String[][] data, String[] columnTitles) {
	    StringBuilder htmlTable = new StringBuilder();
	    htmlTable.append("<table class='table'>");

	    // Adiciona a linha de cabeçalho
	    if (columnTitles != null && columnTitles.length > 0) {
	        htmlTable.append("<thead><tr>"); 
	        for (String title : columnTitles) {
	            htmlTable.append("<th>").append(title).append("</th>");
	        }
	        htmlTable.append("</tr></thead>"); 
	    }

	    // Adiciona o corpo da tabela
	    htmlTable.append("<tbody>");
	    for (int i = 0; i < data.length; i++) {
	        htmlTable.append("<tr>"); 

	        // Itera pelas colunas da linha atual
	        for (int j = 0; j < data[i].length; j++) {
	            htmlTable.append("<td>")
	                     .append(data[i][j] != null ? data[i][j] : "") 
	                     .append("</td>"); 
	        }
	        htmlTable.append("</tr>"); 
	    }
	    htmlTable.append("</tbody>"); // Fecha o corpo da tabela

	    htmlTable.append("</table>");
	    return htmlTable.toString();
	}
	
	
	public static String generateHtmlTableWithRank(String[][] data, String[] columnTitles) {
	    // Inicia o código HTML com a tag de abertura da tabela
	    StringBuilder htmlTable = new StringBuilder();
	    htmlTable.append("<table class='table'>");

	    // Adiciona a linha de cabeçalho
	    if (columnTitles != null && columnTitles.length > 0) {
	        htmlTable.append("<thead><tr>"); 
	        htmlTable.append("<th>Posição</th>");
	        for (String title : columnTitles) {
	            htmlTable.append("<th>").append(title).append("</th>"); 
	        }
	        htmlTable.append("</tr></thead>"); 
	    }

	    // Adiciona o corpo da tabela
	    htmlTable.append("<tbody>");
	    for (int i = 0; i < data.length; i++) {
	        htmlTable.append("<tr>"); 

	        htmlTable.append("<td>").append(i + 1).append("º</td>");

	        // Itera pelas colunas da linha atual
	        for (int j = 0; j < data[i].length; j++) {
	            htmlTable.append("<td>")
	                     .append(data[i][j] != null ? data[i][j] : "") 
	                     .append("</td>");
	        }
	        htmlTable.append("</tr>"); 
	    }
	    htmlTable.append("</tbody>"); 

	    htmlTable.append("</table>");
	    return htmlTable.toString();
	}
	
	
	public static String gerarFichaClienteHTML(String[][] resultados) {
		
	    if (resultados == null || resultados.length == 0) {
	        return "<p>Cliente não encontrado.</p>";
	    }
	    
	    StringBuilder html = new StringBuilder();

	    for (String[] cliente : resultados) {
		    // Extrair os valores do cliente
		    String nif = cliente[0];
		    String tipo = cliente[1];
		    String nome = cliente[2];
		    String distrito = cliente[3];
		    String concelho = cliente[4];
		    String freguesia = cliente[5];
		    String email = cliente[6];
		    String telemovel = cliente[7];
		    String capitalSocial = tipo.equalsIgnoreCase("empresa") ? cliente[8] : null;
		    String lingua = cliente[9];
		    String preferenciaCultural = cliente[10];

		    // Construir o HTML da ficha
		    html.append("<h2>Ficha do Cliente</h2>\n")
		        .append("<p><strong>NIF:</strong> ").append(nif).append("</p>\n")
		        .append("<p><strong>Tipo:</strong> ").append(tipo).append("</p>\n")
		        .append("<p><strong>Nome:</strong> ").append(nome).append("</p>\n");

		    if (distrito != null && !distrito.isEmpty()) {
		        html.append("<p><strong>Distrito:</strong> ").append(distrito).append("</p>\n");
		    }
		    if (concelho != null && !concelho.isEmpty()) {
		        html.append("<p><strong>Concelho:</strong> ").append(concelho).append("</p>\n");
		    }
		    if (freguesia != null && !freguesia.isEmpty()) {
		        html.append("<p><strong>Freguesia:</strong> ").append(freguesia).append("</p>\n");
		    }

		    html.append("<p><strong>Email:</strong> ").append(email).append("</p>\n")
		        .append("<p><strong>Telemóvel:</strong> ").append(telemovel).append("</p>\n");

		    if (tipo.equalsIgnoreCase("empresa") && capitalSocial != null) {
		        html.append("<p><strong>Capital Social (€):</strong> ").append(capitalSocial).append("</p>\n");
		    }

		    html.append("<p><strong>Língua:</strong> ").append(lingua).append("</p>\n");

		    if (preferenciaCultural != null && !preferenciaCultural.isEmpty()) {
		        html.append("<p><strong>Preferência Cultural:</strong> ").append(preferenciaCultural).append("</p>\n");
		    }
	    }

	    return html.toString();
	}
	
	
	public static String gerarOpcoes(String[][] resultados, int idx) {
		StringBuilder options = new StringBuilder();
		for (String[] row : resultados) {
		    String aux = row[idx];
		    options.append("<option value=\"").append(aux).append("\">").append(aux).append("</option>");
		}
		return options.toString();
	}
	
	public static void main(String[] args) throws SQLException {
		Stub stub = new Stub();
		System.out.println(Auxiliar.verificarNIF(stub, "123456789012"));
		System.out.println(Auxiliar.verificarCartaConducao(stub, "CN1234567890"));
		System.out.println(Auxiliar.verificarMatricula(stub, "FF-02-FF"));
		System.out.println(Auxiliar.verificarNIF(stub, "aa"));
		System.out.println(Auxiliar.verificarCartaConducao(stub, "sd"));
		System.out.println(Auxiliar.verificarMatricula(stub, "FF-02-FdsaF"));
		
		System.out.println(Auxiliar.verificarMatriculaDisponivel(stub, "AA-01-AA"));
		System.out.println(Auxiliar.verificarMatriculaDisponivel(stub, "AA-03-AA"));

		
		Auxiliar.printResults(Auxiliar.listarParques(stub));
	}

}
