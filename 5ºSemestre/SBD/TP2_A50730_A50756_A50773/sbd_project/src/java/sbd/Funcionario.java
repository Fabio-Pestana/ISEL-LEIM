package pt.isel.leim.sbd;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Funcionario {
	
	private Stub stub;
	
	public Funcionario() {
		this.stub = new Stub();
	}
	
	public String[][] consultarReservasPendentes() {
		String query = "" //
				+ "SELECT R.* FROM Reserva R " //
				+ "LEFT JOIN Aluguer A ON R.ID = A.IDReserva "
				+ "WHERE A.ID IS NULL " //
				+ "ORDER BY R.DataHoraInicio ASC";

		String[][] results = stub.executeQuery(query, null);
		Auxiliar.printResults(results);
		return results;
	}
	
	
	public String[][] consultarReservasPendentesWeb() {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String query = "" //
				+ "SELECT R.ID, R.DataHoraInicio, "
				+ "R.TipoVeiculo, R.Modelo, R.ParqueEstacionamento "
				+ "FROM Reserva R " //
				+ "LEFT JOIN Aluguer A ON R.ID = A.IDReserva "
				+ "WHERE A.ID IS NULL " //
				+ "ORDER BY R.DataHoraInicio ASC";

		String[][] results = stub.executeQuery(query, null);
		Auxiliar.printResults(results);
		return results;
	}
	
	
	public boolean verificarReservaPendente(String reservaId) {
		String[][] reservasPendentes = consultarReservasPendentes();
	    // Verifica se a reserva está na lista de pendentes
	    if (reservasPendentes != null) {
	        for (String[] reserva : reservasPendentes) {
	            if (reservaId.equals(reserva[0])) { 
	                return true;
	            }
	        }
	    }
	    return false; // A reserva não está entre as pendentes
	}

	public String[][] consultarVeiculosDisponiveis(String ReservaId) {
	    String[][] results = null; // Resultado final que será retornado
        String[] resultado = new String[2];  // Índice 0 para TipoVeiculo, índice 1 para Modelo

		String query = "" //
				+ "SELECT R.TipoVeiculo, R.Modelo " //
				+ "FROM Reserva R "
				+ "WHERE R.ID = ?";
		
	    Object[] params = {ReservaId};
	    String[][] reservaResults = stub.executeQuery(query, params);
	    
	    if (reservaResults.length > 0 && reservaResults[0].length > 0) {
	        resultado[0] = reservaResults[0][0]; // TipoVeiculo
	        resultado[1] = reservaResults[0][1]; // Modelo
	        System.out.println("TipoVeiculo: " + resultado[0] + ", Modelo: " + resultado[1]);
	    }

		String aux = null;
		
		if(resultado[0] == null && resultado[1] != null) {
			query = "" //
					+ "SELECT V.Matricula, V.Marca, V.Modelo " //
					+ "FROM Veiculo V "
					+ "WHERE V.Modelo = ? " //
					+ "AND V.Estado = 'disponível'";
			
			aux = resultado[1];
		}else if(resultado[0] != null && resultado[1] == null) {
			query = "" //
					+ "SELECT V.Matricula, V.Marca, V.Modelo " //
					+ "FROM Veiculo V "
					+ "JOIN TipoVeiculo T ON V.Categoria = T.Categoria " //
					+ "WHERE V.Estado = 'disponível' "
					+ "AND T.Categoria = ? "
					+ "ORDER BY V.Marca, V.Modelo";
			aux = resultado[0];
		}
		
		if (aux != null) {
	        params = new Object[]{aux};
	        results = stub.executeQuery(query, params);
			Auxiliar.printResults(results);
	    }
		return results;
	}
	
	// 4.1 Atribuir Veiculo a uma reserva - Criar Aluguer
	public boolean funcionarioCriarAluguer(String ReservaId, String matricula) throws SQLException {
		boolean check = Auxiliar.verificarMatriculaDisponivel(this.stub, matricula);
		boolean check2 = verificarReservaPendente(ReservaId);
		
		boolean sucesso = false;
    	
		if(!check || !check2) {
            System.out.println("Insucesso ao criar aluguer! ID da reserva: " + ReservaId);
	        return sucesso;
		}
		
    	String idAluguer = gerarIdAluguer();
    	
    	String queryInserirAluguer = ""
                + "INSERT INTO Aluguer (ID, IDReserva, CustoFinalEfetivo, AvaliacaoCondutor, Matricula, KmPercorridos) "
                + "VALUES (?, ?, NULL, NULL, ?, NULL)";
    	
		Object[] params = new Object[] {idAluguer, ReservaId, matricula};
		sucesso = stub.executeInsertQuery(queryInserirAluguer, params);
		if(sucesso) {
            System.out.println("Aluguer efetuada com sucesso! ID do Aluguer: " + idAluguer);
		}else {
            System.out.println("Insucesso ao criar aluguer! ID da reserva: " + ReservaId);
		}
    	
    	return sucesso;
    }
	
	// 4.1 Atribuir Veiculo a uma reserva - Alterar o estado do veiculo
	public boolean alterarEstadoVeiculo(String matricula) {
		boolean sucesso = false;

		String query = ""
                + "UPDATE Veiculo "
                + "SET Estado = 'reservado' "
                + "WHERE Matricula = ?";
		
		Object[] params = new Object[] {matricula};
		sucesso = stub.executeInsertQuery(query, params);
		
    	return sucesso;
	}
	
	
	private String gerarIdAluguer() throws SQLException {
		String idAluguer = stub.gerarId("ALUG");
		
        while (Auxiliar.idAluguerExiste(stub, idAluguer)) {
        	idAluguer = stub.gerarId("ALUG"); 
        }
        
        return idAluguer;
	}
	
	
	// 4.2 Localizar um determinado veiculo - Funcionário (DONE)
	public String[][] funcionarioLocalizarVeiculo(String matricula) {
		String query = "" //
				+ "SELECT Morada, Piso, Fila, Numero " //
				+ "FROM LugarEstacionamento_Veiculo " //
				+ "WHERE Matricula = ?";
		Object[] params = new Object[] {matricula};
		String[][] results = stub.executeQuery(query, params);
		System.out.println("A localizar o veículo com matrícula: " + matricula + "\n");
		Auxiliar.printResults(results);
		return results;
	}
	
	
	public String funcionarioLocalizarVeiculoWeb(String matricula) {
		String query = "" //
				+ "SELECT Morada, Piso, Fila, Numero " //
				+ "FROM LugarEstacionamento_Veiculo " //
				+ "WHERE Matricula = ?";
		
		Object[] params = new Object[] {matricula};
		String[][] results = stub.executeQuery(query, params);
		
		if (results != null && results.length > 0) {
	        // Formata a mensagem com os dados do resultado
	        return String.format(
	            "O veículo com matrícula %s está localizado no Parque: %s, Piso: %s, Fila: %s, Numero: %s.",
	            matricula,
	            results[0][0], // Parque
	            results[0][1], // Piso
	            results[0][2], // Fila
	            results[0][3]  // Numero
	        );
	    } else {
	        // veículo não encontrado
	        return String.format("O veículo com matrícula %s não foi encontrado.", matricula);
	    }
	}
	
	// 4.3 Registar Intervenção
	public boolean funcionarioCriarIntervencao(String dataHoraIntervencao, double custo, String tipoIntervencao , String matricula) {
		
		boolean sucesso = false;
    	
		if(custo <=0) {
	        return sucesso;
		}
				    	
    	String query = ""
                + "INSERT INTO Intervencao (DataHoraIntervencao, Custo, Tipo, Matricula) "
                + "VALUES (?, ?, ?, ?)";
    	
    	
    	Object[] params = new Object[] {dataHoraIntervencao, custo, tipoIntervencao, matricula};
		sucesso = stub.executeInsertQuery(query, params);
		if(sucesso) {
            System.out.println("Intervenção registrada com sucesso!");
		}else {
            System.out.println("Falha ao registrar a intervenção.");
		}

    	return sucesso;
    }
	
	// 4.4 Procurar ficha de um cliente através do seu nome - Funcionário
	public String[][] funcionarioProcurarCliente(String nomeCliente) {
		String query = "" //
				+ "SELECT * " //
				+ "FROM Cliente " //
				+ "WHERE Nome = ?";
		
		Object[] params = new Object[] {nomeCliente};
		String[][] results = stub.executeQuery(query, params);
		System.out.println("A Localizar cliente com o nome: " + nomeCliente + "\n");
		Auxiliar.printResults(results);
		return results;
	}
	
	// auxilio ao 4.4
	public List<String> funcionarioAutoCompleteCliente() {
		List<String> nomes = new ArrayList<String>();
		String query = "" //
				+ "SELECT Nome " //
				+ "FROM Cliente ";
		
		String[][] results = stub.executeQuery(query, null);
		System.out.println("Nomes dos Clientes: \n");
		Auxiliar.printResults(results);
		
		for (String[] row : results) {
			nomes.add(row[0]);
	    }
		return nomes;
	}
	
	public List<String> getData(ArrayList<String> lista, String query, int nLetters, int nItems) {
		List<String> matched = new ArrayList<String>();
		if(query==null || query.length()<nLetters) // numero minimo de letras
			return matched;
		query = query.toLowerCase();
		for(int i=0; i<lista.size(); i++) {
			String item = lista.get(i).toLowerCase();
			if( item.toLowerCase().startsWith(query)) { 
				matched.add(lista.get(i));
			if( matched.size()>nItems)  // limita o numero de respostas
				break;
			}
		}
		return matched;
	}
	
	public String[][] alugueresAFinalizar() {
		String query = "" //
				+ "SELECT ID, Matricula " //
				+ "FROM Aluguer "
				+ "WHERE KmPercorridos IS NULL";
		
		String[][] results = stub.executeQuery(query, null);
		System.out.println("Alugueres a finalizar: \n");
		Auxiliar.printResults(results);
		return results;
	}
	
	
	public String[][] alugueresAFinalizarWeb() {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		String query = "" //
				+ "SELECT ID, Matricula " //
				+ "FROM Aluguer "
				+ "WHERE KmPercorridos IS NULL";
		
		String[][] results = stub.executeQuery(query, null);
		System.out.println("Alugueres a finalizar: \n");
		Auxiliar.printResults(results);
		return results;
	}
	
	public Object[] verDetalhesAluguerCondutor(String aluguer) {
		Connection conn = stub.getConnection();

		String query = "" //
				+ "SELECT * " //
				+ "FROM DetalhesAlugueresCondutor "
				+ "WHERE IDAluguer = ?";
		
	    Object[] detalhes = null; 


		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, aluguer);
			try (ResultSet rs = pstmt.executeQuery()) {
		            if (rs.next()) {
		            	detalhes = new Object[] {
		            			rs.getString("Matricula"),
		                        rs.getString("NIF"),       
		                        rs.getFloat("CustoPrevistoReserva"), 
		                        rs.getString("NomeCondutor"),       
		                        rs.getFloat("ReputacaoCondutor"),  
		                 };
		               } else {
		               System.out.println("Nenhum aluguer encontrado com o ID: " + aluguer);
		           }
		       }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        stub.closeConnection(conn);
	    }
		
		return detalhes;
	}
	
	//maior q 4.5 da 10% se for maior q 4.0 e menor q 4,5 da-se 5%
	public boolean funcionarioFinalizarAluguer(String aluguer, double custoFinal, int avalicaoCondutor, double kmPercorridos, String nif, String matricula) {
		boolean sucesso = false;
				
		String query = "" //
	            + "UPDATE Aluguer " //
	            + "SET CustoFinalEfetivo = ?, " //
	            + "    AvaliacaoCondutor = ?, " //
	            + "    KmPercorridos = ? " //
	            + "WHERE ID = ?";
	    
	    Object[] params = new Object[] {custoFinal, avalicaoCondutor, kmPercorridos, aluguer};
		sucesso = stub.executeInsertQuery(query, params);
		if(sucesso) {
            System.out.println("Aluguer atualizado com sucesso!");
            atualizarQuilometragemVeiculo(matricula, kmPercorridos);
	    	atualizarReputacaoCondutor(nif);
		}else {
            System.out.println("Nenhum aluguer encontrado com o ID fornecido.");
		}
	    	
		return sucesso;
	}
	
	
	private boolean atualizarReputacaoCondutor(String nif) {
	    Connection conn = stub.getConnection();

	    String queryMediaAvaliacoes = "" //
	            + "SELECT AVG(A.AvaliacaoCondutor) AS MediaAvaliacoes, CC.NumeroCartaConducao " //
	            + "FROM Reserva R " //
	            + "JOIN Aluguer A ON R.ID = A.IDReserva " //
	            + "JOIN Cliente_Condutor CC ON R.NIF = CC.NIF " //
	            + "WHERE R.NIF = ? " //
	            + "GROUP BY CC.NumeroCartaConducao";

	    String queryAtualizarReputacao = "" //
	            + "UPDATE Cliente_Condutor " //
	            + "SET Reputacao = ? " //
	            + "WHERE NIF = ? AND NumeroCartaConducao = ?";

	    boolean atualizado = false;

	    try (PreparedStatement pstmtMedia = conn.prepareStatement(queryMediaAvaliacoes);
	         PreparedStatement pstmtAtualizar = conn.prepareStatement(queryAtualizarReputacao)) {

	        pstmtMedia.setString(1, nif);


	        try (ResultSet rs = pstmtMedia.executeQuery()) {
	            while (rs.next()) {

	            	float mediaAvaliacoes = rs.getFloat("MediaAvaliacoes");
	                String numeroCartaConducao = rs.getString("NumeroCartaConducao");

	                pstmtAtualizar.setFloat(1, mediaAvaliacoes);
	                pstmtAtualizar.setString(2, nif);
	                pstmtAtualizar.setString(3, numeroCartaConducao);

	                atualizado = stub.sucessoInsert(pstmtAtualizar);
	                if (atualizado) {
	                    System.out.println("Reputação do condutor com a carta " + numeroCartaConducao + " atualizada com sucesso!");
	                }
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        stub.closeConnection(conn);
	    }

	    return atualizado; 
	}
	
	
	private boolean atualizarQuilometragemVeiculo(String matricula, double kmAdicional) {

	    String queryAtualizarKm = "" //
	            + "UPDATE Veiculo " //
	            + "SET RegistoKm = RegistoKm + ? " //
	            + "WHERE Matricula = ?";
	    
	    
	    Object[] params = new Object[] {kmAdicional, matricula};
	    boolean atualizado = stub.executeInsertQuery(queryAtualizarKm, params);
		if(atualizado) {
            System.out.println("Quilometragem do veículo com matrícula " + matricula + " atualizada com sucesso!");
		}else {
            System.out.println("Veículo com matrícula " + matricula + " não encontrado.");
		}

	    return atualizado; // Retorna true se a atualização foi bem-sucedida, false caso contrário
	}

	
	// 4.6 Procurar Condutor de um veiculo para uma data - Funcionário (DONE)
	public String[][] funcionarioProcurarCondutorDeUmVeiculo(String matricula, String dataHora) {

	    String query = "" //
	            + "SELECT C.Nome, C.NumeroCartaConducao, C.DataNascimento, C.Reputacao " //
	            + "FROM Aluguer A "  //
	            + "JOIN Reserva R ON A.IDReserva = R.ID " //
	            + "JOIN Cliente_Condutor C ON R.NIF = C.NIF " //
	            + "JOIN Veiculo V ON A.Matricula = V.Matricula "  //
	            + "WHERE V.Matricula = ? AND ? BETWEEN R.DataHoraInicio AND R.DataHoraFinal";
	    
	    Object[] params = new Object[] {matricula, dataHora};
		String[][] results = stub.executeQuery(query, params);
        System.out.println("Procurar o condutor do veículo com matrícula: " + matricula + " na data: " + dataHora + "\n");
		Auxiliar.printResults(results);
		return results;
	}
	
	
	public boolean verificarDisponibilidadeLugar(String morada, int piso, int fila, int numero) {
	    Connection conn = stub.getConnection();

	    String query = "" //
	            + "SELECT Estado " //
	            + "FROM LugarEstacionamento " //
	            + "WHERE Morada = ? AND Piso = ? AND Fila = ? AND Numero = ?";

	    boolean disponibilidade = false; // Assume que o lugar está ocupado (false)

	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {

	        pstmt.setString(1, morada);
	        pstmt.setInt(2, piso);
	        pstmt.setInt(3, fila);
	        pstmt.setInt(4, numero);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                // Verifica o estado do lugar de estacionamento
	                String estado = rs.getString("Estado");

	                // Se o estado for 'vago', o lugar está disponível
	                if ("vago".equalsIgnoreCase(estado)) {
	                    disponibilidade = true;
	                }
	            } else {
	                System.out.println("Lugar de estacionamento não encontrado.");
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    stub.closeConnection(conn);

	    return disponibilidade; // Retorna true se o lugar estiver disponível, false caso contrário
	}
	
	public Stub getStub() {
    	return this.stub;
    }
	
	public boolean atualizarLugarEstacionamento(String matricula, String morada, int piso, int fila, int numero) {
		
		boolean check = verificarDisponibilidadeLugar(morada, piso, fila, numero);
		if (!check) {
			return false;
		}
		
		vagoLugarEstacionamento(matricula);
		
	    // Query para atualizar o estado do lugar de estacionamento para 'ocupado'
	    String queryEstado = "" //
	            + "UPDATE LugarEstacionamento " //
	            + "SET Estado = 'ocupado' " //
	            + "WHERE Morada = ? AND Piso = ? AND Fila = ? AND Numero = ?";

	    // Query para atualizar a associação do veículo com o lugar de estacionamento
	    String queryLugarEst = "" //
	            + "UPDATE LugarEstacionamento_Veiculo " //
	            + "SET Morada = ?, Piso = ?, Fila = ?, Numero = ? " //
	            + "WHERE Matricula = ?";
	    
	    Object[] params = new Object[] {morada, piso, fila, numero};
	    boolean sucesso = stub.executeInsertQuery(queryEstado, params);
	    
		if(sucesso) {
            Object[] params2 = new Object[] {morada, piso, fila, numero, matricula};
    	    sucesso = stub.executeInsertQuery(queryLugarEst, params2);
		}else {
            System.out.println("Falha ao atualizar o estado do lugar de estacionamento.");
		}

	    return sucesso; 
	}


	public boolean vagoLugarEstacionamento(String matricula) {
	    Connection conn = stub.getConnection();

	    String queryProcurarLugar = "" //
	            + "SELECT Morada, Piso, Fila, Numero " //
	            + "FROM LugarEstacionamento_Veiculo " //
	            + "WHERE Matricula = ?";

	    String queryAtualizarEstado = "" //
	            + "UPDATE LugarEstacionamento " //
	            + "SET Estado = 'vago' " //
	            + "WHERE Morada = ? AND Piso = ? AND Fila = ? AND Numero = ?";

	    boolean sucesso = false;

	    try (PreparedStatement pstmtLugar = conn.prepareStatement(queryProcurarLugar);
	         PreparedStatement pstmtEstado = conn.prepareStatement(queryAtualizarEstado)) {

	    	pstmtLugar.setString(1, matricula);

	        try (ResultSet rs = pstmtLugar.executeQuery()) {
	            if (rs.next()) {
	                String morada = rs.getString("Morada");
	                int piso = rs.getInt("Piso");
	                int fila = rs.getInt("Fila");
	                int numero = rs.getInt("Numero");

	                pstmtEstado.setString(1, morada);
	                pstmtEstado.setInt(2, piso);
	                pstmtEstado.setInt(3, fila);
	                pstmtEstado.setInt(4, numero);
	                
		            sucesso = stub.sucessoInsert(pstmtEstado);
	            } else {
	                System.out.println("Nenhum lugar de estacionamento encontrado para a matrícula fornecida.");
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        stub.closeConnection(conn);
	    }

	    return sucesso;
	}



	public static void main(String[] args) throws SQLException {
		
		Funcionario funcionario = new Funcionario();
		
		System.out.println("Indique a matricula do veiculo que deseja localizar");
		String matriculaString = "AA-01-AA";
		funcionario.funcionarioLocalizarVeiculo(matriculaString);
					
		System.out.println();
		System.out.println();

		System.out.println("Histórico do veiculo: " + matriculaString);

		funcionario.funcionarioAutoCompleteCliente();
		System.out.println();
		System.out.println();

		System.out.println("\n Indique o nome do cliente que deseja visualizar");
		String nomeString = "Ricardo Mendes";
		
		funcionario.funcionarioProcurarCliente(nomeString);
		System.out.println();
		System.out.println();

		funcionario.funcionarioProcurarCondutorDeUmVeiculo("AA-01-AA", "2018-11-02 10:12:00");
		funcionario.funcionarioProcurarCondutorDeUmVeiculo("AA-01-AA", "2018-11-12 10:12:00");
		
		/*
		funcionario.consultarReservasPendentes();
		
		String matricula = "FF-07-FF";
		String reserva = "RESE00025";
				
		funcionario.consultarVeiculosDisponiveis(reserva);	
		
		boolean aux = funcionario.funcionarioCriarAluguer(reserva, matricula);
		
		
		if(aux) {
			System.out.println(funcionario.alterarEstadoVeiculo(matricula));
		} 
		
		
		String aluguer = "ALUG78277";
		
		funcionario.alugueresAFinalizar();
		Object[] resultados = funcionario.verDetalhesAluguerCondutor(aluguer);
		String matriculaAluguer = (String) resultados[0];
		String nif = (String) resultados[1];
		float custoPrevisto = (float) resultados[2];
		String Nome = (String) resultados[3];
		float reputacao = (float) resultados[4];
		System.out.println(matriculaAluguer);
		System.out.println(nif);
		System.out.println(custoPrevisto);
		System.out.println(Nome);
		System.out.println(reputacao);
		
		System.out.println("maior q 4.5 da 10% se for maior q 4.0 e menor q 4,5 da-se 5%");

		float desconto = (float) (custoPrevisto * 10 / 100.0);
        float custoFinal = custoPrevisto - desconto;
        
        int avalicao = 4;
        double km = 350;
		
		funcionario.funcionarioFinalizarAluguer( aluguer, custoFinal, avalicao, km, nif, matriculaAluguer);
		
		funcionario.funcionarioCriarIntervencao("2020-05-20 14:00", 390.5, "manutenção", "FF-07-FF");
		
        Auxiliar.listarParques(funcionario.getStub());

        String matricula = "AA-08-AA"; 
        String morada = "Rua Elias Garcia, Amadora, 2700-327"; 
        int piso = 2; 
        int fila = 2; 
        int numero = 7; 

        boolean sucesso = funcionario.atualizarLugarEstacionamento(matricula, morada, piso, fila, numero);

        if (sucesso) {
            System.out.println("O lugar de estacionamento foi atualizado com sucesso.");
        } else {
            System.out.println("Falha ao atualizar o lugar de estacionamento.");
        }
        */
	}
}
