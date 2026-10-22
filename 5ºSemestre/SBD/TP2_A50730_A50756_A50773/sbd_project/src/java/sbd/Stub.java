package pt.isel.leim.sbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.PreparedStatement;

public class Stub {

	private final String USER = "root";
	private final String PASSWORD = "mysql123";
	private final String DATABASE = "SBD";
	private final String SERVER_NAME = "localhost:3306";
	private final String URL = "jdbc:mysql://" + SERVER_NAME + "/" + DATABASE;
	
	public Connection getConnection() {
		return getConnection(URL, USER, PASSWORD);
	}

	private Connection getConnection(String url, String user, String password) {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("Erro ao conectar com a base de dados: " + e.getMessage());
			return null;
		}
	}
	
	
	public boolean sucessoInsert(PreparedStatement pstmt) throws SQLException {
		int linhasAfetadas = pstmt.executeUpdate();
		return linhasAfetadas > 0;
	}
	
	
	public boolean executeInsertQuery(String query, Object[] params) {
	    Connection conn = getConnection();
	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	        if (params != null) {
	            for (int i = 0; i < params.length; i++) {
	                pstmt.setObject(i + 1, params[i]);
	            }
	        }
	        return sucessoInsert(pstmt);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Retornar array vazio em caso de erro
	    } finally {
	        closeConnection(conn);
	    }
	}
	
	public void closeConnection(Connection conn) {
	    if (conn != null) { // Verifica se a conexão não é nula
	        try {
	            conn.close();
	        } catch (SQLException e) {
	            System.out.println("Erro ao fechar a conexão: " + e.getMessage());
	        }
	    } else {
	        System.out.println("Conexão já está nula.");
	    }
	}

	
	public void printResults(PreparedStatement pstmt) throws SQLException {
		try (ResultSet rs = pstmt.executeQuery()) {
			int ncols = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= ncols; ++i) {
					Object val = rs.getObject(i);
					System.out.print(val);
					if (i < ncols)
						System.out.print(" - ");
				}
				System.out.println();
			}
		}
	}
	
	public String[][] executeQuery(String query, Object[] params) {
	    Connection conn = getConnection();
	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	        // Substituir os parâmetros
	        if (params != null) {
	            for (int i = 0; i < params.length; i++) {
	                pstmt.setObject(i + 1, params[i]);
	            }
	        }
	        return getResults(pstmt);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return new String[0][0]; // Retornar array vazio em caso de erro
	    } finally {
	        closeConnection(conn);
	    }
	}
	
	public String[][] getResults(PreparedStatement pstmt) throws SQLException {
	    try (ResultSet rs = pstmt.executeQuery()) {
	        // Obter o número de colunas
	        int ncols = rs.getMetaData().getColumnCount();

	        // Armazena as linhas em uma lista temporária
	        List<String[]> rows = new ArrayList<>();

	        // Iterar sobre os resultados
	        while (rs.next()) {
	            String[] row = new String[ncols]; // Cada linha é um array de strings
	            for (int i = 0; i < ncols; ++i) {
	                Object val = rs.getObject(i + 1); // Colunas começam em 1
	                row[i] = (val != null) ? val.toString() : null; // Evitar NullPointerException
	            }
	            rows.add(row); // Adicionar a linha à lista
	        }

	        // Converter a lista em um array de arrays
	        return rows.toArray(new String[0][0]);
	    }
	}

	
	public String gerarId(String inicio) {
        // Gerar número aleatório de 10000 a 99999
        int numeroAleatorio = (int) (Math.random() * 90000) + 10000;
        return inicio + numeroAleatorio;
    }
}