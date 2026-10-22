package pt.isel.leim.sbd;


import java.sql.SQLException;

public class Gerente {
	
	private Stub stub;
	
	public Gerente() {
		this.stub = new Stub();
	}

	// 5.1 Histórico de um veiculo 
	public String[][] gerenteHistoricoVeiculo(String matricula, int nMeses, boolean todoHistorico) {

		String query = "";
		Object[] params = null;
		if (todoHistorico)
		{
			query = "" //
					+ "SELECT TipoIntervencao, AluguerID, DataHoraIntervencao, DataHoraFimIntervencao, Custo " //
					+ "FROM AlugueresEIntervencoes " //
					+ "WHERE Matricula = ?";
			params = new Object[]{matricula};

		}else {
			query = "" //
					+ "SELECT TipoIntervencao, AluguerID, DataHoraIntervencao, DataHoraFimIntervencao, Custo " //
					+ "FROM AlugueresEIntervencoes " //
					+ "WHERE Matricula = ?" //
					+ "AND DataHoraIntervencao >= DATE_SUB(CURRENT_DATE(), INTERVAL ? MONTH)" ;
			params = new Object[]{matricula, nMeses};
		}
		
	    System.out.println("Histórico do veículo com matrícula: " + matricula);
		String[][] results = stub.executeQuery(query, params);
		Auxiliar.printResults(results);
		return results;
	}
	
	// 5.2 Ranking das 3 marcas de veículos que geraram menor lucro
	public String[][] gerenteRankingMenorLucro (int numero) {
		String query = "";
		Object[] params = null;
		
		if (numero < 0) {
			query = ""
				+ "SELECT * "
				+ "FROM LucroTotalPorMarca";
			
		}else {
			query = "" //
				+ "SELECT * FROM LucroTotalPorMarca " //
				+ "limit ?"; 
			params = new Object[] {numero};
		}
		String[][] results = stub.executeQuery(query, params);
		Auxiliar.printResults(results);
		return results;
	}
	
	// 5.3 Ranking dos 5 modelos dos veículos com melhor avaliação na semana passada.
	public String[][] gerenteRankingMelhorAvaliacao (int numeroVeiculos, int semanas) {
		String query = null;
		Object[] params = null;
		if (numeroVeiculos <= 0) {
			query = "" //
					+ "SELECT V.Marca, V.Modelo " //
					+ "FROM Veiculo V " //
					+ "JOIN Aluguer A ON V.Matricula = A.Matricula " //
					+ "JOIN Reserva R ON A.IDReserva = R.ID " //
					+ "JOIN Avaliacao AV ON A.ID = AV.ID " //
					+ "WHERE R.DataHoraFinal >= CURDATE() - INTERVAL ? WEEK "
					+ "GROUP BY V.Marca, V.Modelo " //
					+ "ORDER BY AVG(CASE WHEN AV.Nota = 'Não vou voltar' THEN 1 " //
					+ "WHEN AV.Nota = 'Gostei' THEN 2 WHEN AV.Nota = 'Adorei' THEN 3 " //
					+ "END)";
			params = new Object[] {semanas};
		}else {
			query = "" //
				+ "SELECT V.Marca, V.Modelo " //
				+ "FROM Veiculo V " //
				+ "JOIN Aluguer A ON V.Matricula = A.Matricula " //
				+ "JOIN Reserva R ON A.IDReserva = R.ID " //
				+ "JOIN Avaliacao AV ON A.ID = AV.ID " //
				+ "WHERE R.DataHoraFinal >= CURDATE() - INTERVAL ? WEEK "
				+ "GROUP BY V.Marca, V.Modelo " //
				+ "ORDER BY AVG(CASE WHEN AV.Nota = 'Não vou voltar' THEN 1 " //
				+ "WHEN AV.Nota = 'Gostei' THEN 2 WHEN AV.Nota = 'Adorei' THEN 3 " //
				+ "END) DESC LIMIT ?";
			params = new Object[] {semanas, numeroVeiculos};
		}
		String[][] results = stub.executeQuery(query, params);
		Auxiliar.printResults(results);
		return results;
	}
	
	// 5.4 Ranking dos 10 veículos que percorreram menos km no último trimestre
	public String[][] gerenteRankingMenoskm (int numero) {
		String query = null;
		Object[] params = null;
		if (numero <= 0) {
			query = "" //
					+ "SELECT V.Marca, V.Modelo, V.Matricula, ROUND(SUM(A.KmPercorridos), 2) AS KmTotal " //
					+ "FROM Veiculo V " //
					+ "JOIN Aluguer A ON V.Matricula = A.Matricula " //
					+ "JOIN Reserva R ON A.IDReserva = R.ID " //
					+ "WHERE R.DataHoraInicio >= CURDATE() - INTERVAL 3 MONTH " //
					+ "GROUP BY V.Matricula " //
					+ "ORDER BY KmTotal ASC";
		}else {
			query = "" //
				+ "SELECT V.Marca, V.Modelo, V.Matricula, ROUND(SUM(A.KmPercorridos), 2) AS KmTotal " //
				+ "FROM Veiculo V " //
				+ "JOIN Aluguer A ON V.Matricula = A.Matricula " //
				+ "JOIN Reserva R ON A.IDReserva = R.ID " //
				+ "WHERE R.DataHoraInicio >= CURDATE() - INTERVAL 3 MONTH " //
				+ "GROUP BY V.Matricula " //
				+ "ORDER BY KmTotal ASC " //
				+ "limit ?"; 
			params = new Object[] {numero};
		}
		String[][] results = stub.executeQuery(query, params);
		Auxiliar.printResults(results);
		return results;
	}

	// 5.5 Ranking dos 100 clientes ordenados por reputação e filtrados por freguesia de morada
	public String[][] gerenteRankingClientes (String freguesia) {
		String query = "" //
				+ "SELECT C.NIF, C.Nome, CC.Reputacao " //
				+ "FROM Cliente_Condutor CC " //
				+ "JOIN Cliente C ON CC.NIF = C.NIF " //
				+ "WHERE C.Freguesia = ? " //
				+ "ORDER BY CC.Reputacao DESC " //
				+ "LIMIT 100";

		Object[] params = new Object[] {freguesia};
		String[][] results = stub.executeQuery(query, params);
		System.out.println("Ranking dos 100 clientes ordenados por reputação e filtrados por freguesia " + freguesia + ":\n");
		Auxiliar.printResults(results);
		return results;
	}
	
	public void listarFreguesias() {
	    String query = ""
	            + "SELECT DISTINCT Freguesia "
	            + "FROM Cliente "
	            + "WHERE Freguesia IS NOT NULL";

		String[][] results = stub.executeQuery(query, null);
		Auxiliar.printResults(results);
	}
	
	
	public String listarFreguesiasWeb() {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query = ""
	            + "SELECT DISTINCT Freguesia "
	            + "FROM Cliente "
	            + "WHERE Freguesia IS NOT NULL";
		
	    String[][] results = stub.executeQuery(query, null);

	    // Converte o resultado em uma String de opções para a lista
	    StringBuilder options = new StringBuilder();
	    for (String[] row : results) {
	        String freguesia = row[0];
	        options.append("<option value=\"").append(freguesia).append("\">").append(freguesia).append("</option>");
	    }

	    return options.toString(); // Converte StringBuilder para String
	}


	public static void main(String[] args) throws SQLException {
		Gerente gerente = new Gerente();

		gerente.gerenteHistoricoVeiculo("AA-01-AA", 6, true);
		
		System.out.println();
		System.out.println();
		
		gerente.gerenteRankingMenorLucro(3);
		
		System.out.println();
		System.out.println();
		
		gerente.gerenteRankingMelhorAvaliacao(5, 1);
		
		System.out.println();
		System.out.println();
		
		gerente.gerenteRankingMenoskm(10);
		
		System.out.println();
		System.out.println();

		
		gerente.listarFreguesias();

		System.out.println();
		System.out.println();
		
		gerente.gerenteRankingClientes("Benfica");
	}
}
