package pt.isel.leim.sbd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Condutor {
	
	private Stub stub;
	
	public Condutor() {
		this.stub = new Stub();
	}
	
	
	public String[][] consultarReservasPendentes(String nif) {
		String query = "" //
				+ "SELECT IDAluguer, Matricula, Marca, Modelo, Cor, MoradaParque, Piso, Fila, Numero " //
				+ "FROM AlugueresIncompletos "
				+ "WHERE NIFCliente = ?";


	    Object[] params = {nif};
		String[][] results = stub.executeQuery(query, params);
		Auxiliar.printResults(results);
		return results;
	}
	
	
	public String[] validarMatriculaReservasPendentes(String nif, String matricula) {
	    // Consulta as reservas pendentes do cliente
	    String[][] reservasPendentes = consultarReservasPendentes(nif);
	    
	    // Verifica se a matrícula fornecida está entre as reservas pendentes
	    for (String[] reserva : reservasPendentes) {
	        String matriculaReserva = reserva[1];  
	        if (matricula.equals(matriculaReserva)) {
	            return reserva;  
	        }
	    }
	    
	    return null;
	}
	
	private boolean isVeiculo(String matricula, String checkEstado) {		
		Connection conn = stub.getConnection();
	    String query = ""
	    		+ "SELECT Estado "
	    		+ "FROM Veiculo "
	    		+ "WHERE Matricula = ?";

	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {

	        pstmt.setString(1, matricula);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                String estado = rs.getString("Estado");
	                return checkEstado.equalsIgnoreCase(estado);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }finally {
	    	stub.closeConnection(conn);
	    }
	    
	    return false;
	}

	public boolean levantarVeiculo(String nif, String matricula) {
		boolean sucesso = isVeiculo(matricula, "reservado");
		String[] parque = validarMatriculaReservasPendentes(nif, matricula);
		
		if(!sucesso || parque == null) {
			return false;
		}
		
        String veiculoEstadoQuery = ""
        		+ "UPDATE Veiculo "
        		+ "SET Estado = 'alugado' "
        		+ "WHERE Matricula = ?";
        
        String queryAtualizarEstado = "" //
	            + "UPDATE LugarEstacionamento " //
	            + "SET Estado = 'vago' " //
	            + "WHERE Morada = ? AND Piso = ? AND Fila = ? AND Numero = ?";
        
        String deleteEstacionamentoQuery = ""
        		+ "DELETE FROM LugarEstacionamento_Veiculo "
        		+ "WHERE Matricula = ?";

        Object[] params = new Object[] {matricula};
		sucesso = stub.executeInsertQuery(veiculoEstadoQuery, params);
		
		if(sucesso) {
			Object[] paramsUpdate = new Object[] {parque[5], parque[6], parque[7], parque[8]};
			sucesso = stub.executeInsertQuery(queryAtualizarEstado, paramsUpdate);

			Object[] paramsDelete = new Object[] {matricula};
			sucesso = stub.executeInsertQuery(deleteEstacionamentoQuery, paramsDelete);
		}
		
		return sucesso;
	}
	
	
	public String[][] lugaresDisponiveis(String morada) {
		String query = "" //
				+ "SELECT Piso, Fila, Numero " //
				+ "FROM LugarEstacionamento "
				+ "WHERE Morada = ? AND Estado = 'vago'";


	    Object[] params = {morada};
		String[][] results = stub.executeQuery(query, params);
		System.out.println(morada);
		Auxiliar.printResults(results);
		return results;
	}
	
	public String[] validarLugar(String piso, String fila, String numero, String morada) {
	    String[][] lugaresDisponiveis = lugaresDisponiveis(morada);
	    
	    for (String[] lugar : lugaresDisponiveis) {
	    	boolean checkP = lugar[0].equals(piso);
	    	boolean checkF = lugar[1].equals(fila);
	    	boolean checkN = lugar[2].equals(numero);
	    	
	        if (checkP && checkF && checkN) {
	            return lugar;  
	        }
	    }
	    
	    return null;
	}
	
	public String matriculaCondutor(String nif) {
		String query = "" //
				+ "SELECT Matricula " //
				+ "FROM AlugueresIncompletos "
				+ "WHERE NIFCliente = ?";


	    Object[] params = {nif};
		String[][] results = stub.executeQuery(query, params);

		if(results.length != 0 && results[0].length != 0)
			return results[0][0];

		return null;
	}
	
	public boolean entregarVeiculo(String nif, String piso, String fila, String numero, String morada) {
		String[] lugar = validarLugar(piso, fila, numero, morada);
		String matricula = matriculaCondutor(nif);
		
		boolean sucesso = isVeiculo(matricula, "alugado");
		
		if(!sucesso || lugar == null|| matricula == null) {
			return false;
		}

        String veiculoEstadoQuery = ""
        		+ "UPDATE Veiculo "
        		+ "SET Estado = 'disponível' "
        		+ "WHERE Matricula = ?";
        
        String queryAtualizarEstado = "" //
	            + "UPDATE LugarEstacionamento " //
	            + "SET Estado = 'ocupado' " //
	            + "WHERE Morada = ? AND Piso = ? AND Fila = ? AND Numero = ?";
        
        String queryInserirAluguer = ""
                + "INSERT INTO LugarEstacionamento_Veiculo (Morada, Piso, Fila, Numero, Matricula) "
                + "VALUES (?, ?, ?, ?, ?)";

        Object[] params = new Object[] {matricula};
		sucesso = stub.executeInsertQuery(veiculoEstadoQuery, params);
		
		if(sucesso) {
			Object[] paramsUpdate = new Object[] {morada, lugar[0], lugar[1], lugar[2]};
			sucesso = stub.executeInsertQuery(queryAtualizarEstado, paramsUpdate);

			Object[] paramsInsert = new Object[] {morada, lugar[0], lugar[1], lugar[2], matricula};
			sucesso = stub.executeInsertQuery(queryInserirAluguer, paramsInsert);
		}
		
		return sucesso;
	}
	
	public String aluguerIdCondutor(String nif) {
		String query = "" //
				+ "SELECT IDAluguer " //
				+ "FROM AlugueresIncompletos "
				+ "WHERE NIFCliente = ?";


	    Object[] params = {nif};
		String[][] results = stub.executeQuery(query, params);

		if(results.length != 0 && results[0].length != 0)
			return results[0][0];

		return null;
	}
	
	
	public boolean carrosAlugados(String nif) {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		String query = "" //
				+ "SELECT V.Matricula "
				+ "FROM Veiculo V " //
				+ "JOIN Aluguer A ON V.Matricula = A.Matricula "
				+ "JOIN Reserva R ON A.IDReserva = R.ID "
				+ "WHERE R.NIF = ? AND V.Estado = 'alugado'";


	    Object[] params = {nif};
		String[][] results = stub.executeQuery(query, params);
		return results.length != 0 && results[0].length != 0;
	}
	
	public boolean avaliarServico(String nif, String avaliacao, String comentario) {
		String aluguer = aluguerIdCondutor(nif);
		comentario = comentario.trim().replaceAll(" +", " ");
		
		if(aluguer == null) {
			return false;
		}
		
		String queryInserirAvalicao = null;
		Object[] params = null;
		
		if(comentario.equals("")) {
			queryInserirAvalicao = ""
	                + "INSERT INTO Avaliacao (ID, Nota, Comentario) "
	                + "VALUES (?, ?, Null)";
			
			params = new Object[] {aluguer, avaliacao};
		}else {
			queryInserirAvalicao = ""
	                + "INSERT INTO Avaliacao (ID, Nota, Comentario) "
	                + "VALUES (?, ?, ?)";
			
			params = new Object[] {aluguer, avaliacao, comentario};
		}
		
		return stub.executeInsertQuery(queryInserirAvalicao, params);
	}
	
	
	public boolean avaliarServicoWeb(String nif, int aval, String comentario) {
		String aluguer = aluguerIdCondutor(nif);
		comentario = comentario.trim().replaceAll(" +", " ");
		
		if(aluguer == null) {
			return false;
		}
		String avaliacao = "";
		if(aval == 1) {
			avaliacao = "Não vou voltar";
		}else if(aval == 2) {
			avaliacao = "Gostei";
		}else if(aval == 3) {
			avaliacao = "Adorei";
		}else {
			return false;
		}
		
		String queryInserirAvalicao = null;
		Object[] params = null;
		
		if(comentario.equals("")) {
			queryInserirAvalicao = ""
	                + "INSERT INTO Avaliacao (ID, Nota, Comentario) "
	                + "VALUES (?, ?, Null)";
			
			params = new Object[] {aluguer, avaliacao};
		}else {
			queryInserirAvalicao = ""
	                + "INSERT INTO Avaliacao (ID, Nota, Comentario) "
	                + "VALUES (?, ?, ?)";
			
			params = new Object[] {aluguer, avaliacao, comentario};
		}
		
		return stub.executeInsertQuery(queryInserirAvalicao, params);
	}
	
	
	
	
	public static void main(String[] args) throws SQLException {
		Condutor condutor = new Condutor();
		String nif = "123456789012";
		String cc = "CN1234567890";
		 
		condutor.consultarReservasPendentes(nif);
		
		System.out.println();
		
		System.out.println(condutor.levantarVeiculo(nif, "AA-05-AA"));
		
		
		
	}
}
