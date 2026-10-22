package pt.isel.leim.sbd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import pt.isel.leim.sbd.Stub;

public class Cliente {
	
	private String nif;
	private Stub stub;
	private List<String> unidadesMonetariasSuportadas; 
	private double tarifa;

	
	public Cliente(String nif) {
		this.nif = nif;
		this.stub = new Stub();
		this.tarifa = 0;
		
		// Inicializar a lista de unidades monetárias suportadas
	    this.unidadesMonetariasSuportadas = new ArrayList<>();
	    unidadesMonetariasSuportadas.add("EUR"); // Euro
	    unidadesMonetariasSuportadas.add("USD"); // Dólar
	    unidadesMonetariasSuportadas.add("GBP"); // Libra 
	    unidadesMonetariasSuportadas.add("BRL"); // Real
	    unidadesMonetariasSuportadas.add("JPY"); // Yen
	    unidadesMonetariasSuportadas.add("CHF"); // Franco Suíço
	    unidadesMonetariasSuportadas.add("CNY"); // Yuan
	}


	public String[][] listarModelosDisponiveis() {
	    String query = ""
	            + "SELECT DISTINCT Marca, Modelo "
	            + "FROM Veiculo "
	            + "WHERE Estado = 'disponível'";

	    String[][] results = stub.executeQuery(query, null);
		Auxiliar.printResults(results);
		return results;
	}
	
	public String[][] listarCategoriasVeiculosDisponiveis() {
	    String query = ""
	            + "SELECT DISTINCT Categoria "
	            + "FROM Veiculo "
	            + "WHERE Estado = 'disponível'";
	    
	    String[][] results = stub.executeQuery(query, null);
		Auxiliar.printResults(results);
		return results;
	}
    
    
    public void TarifaModelo(String modelo) {
	    String query = ""
	            + "SELECT T.Tarifa "
	            + "FROM Veiculo V "
	            + "JOIN TipoVeiculo T ON V.Categoria = T.Categoria "
	            + "WHERE V.Modelo = ?";
	    
	    Object[] params = {modelo};
	    String[][] results = stub.executeQuery(query, params);
	    
	    if (results.length > 0 && results[0].length > 0) {
            try {
            	this.tarifa = Double.parseDouble(results[0][0]);
            } catch (NumberFormatException e) {
                System.out.println("Erro ao converter a tarifa: " + e.getMessage());
            }
        }
	}

    
    public void TarifaTipoVeiculo(String categoria) {
	    String query = ""
	            + "SELECT Tarifa "
	            + "FROM TipoVeiculo "
	            + "WHERE Categoria = ?";
	    
	    Object[] params = {categoria};
	    String[][] results = stub.executeQuery(query, params);
	    
	    if (results.length > 0 && results[0].length > 0) {
            try {
            	this.tarifa = Double.parseDouble(results[0][0]);
            } catch (NumberFormatException e) {
                System.out.println("Erro ao converter a tarifa: " + e.getMessage());
            }
        }
	}
    
    // Método para converter a tarifa para a unidade monetária escolhida
    private double converterCusto(double custoFinal, String unidadeMonetaria) {
        double taxaConversao = 1.0;
        
        switch (unidadeMonetaria) {
            case "USD":
                taxaConversao = 1.04; //  USD
                break;
            case "GBP":
                taxaConversao = 0.83; //  GBP
                break;
            case "BRL":
                taxaConversao = 6.43; //  BRL
                break;
            case "JPY":
                taxaConversao = 164; //  JPY
                break;
            case "CHF":
                taxaConversao = 0.94; //  CHF
                break;
            case "CNY":
                taxaConversao = 7.59; // CNY
                break;
            case "EUR":
            default:
                taxaConversao = 1.0; //  EUR
                break;
        }

        return custoFinal * taxaConversao;
    }
    
    public double calcularCustoPrevisto(LocalDate dataInicio, LocalDate dataFim, String categoria, String modelo, String unidadeMonetaria) throws SQLException {
        long diasDeDiferenca = ChronoUnit.DAYS.between(dataInicio, dataFim);
        
        if (diasDeDiferenca > 14) {
            return 0;
        }
        
        if(categoria == null || categoria == "") {
        	this.TarifaModelo(modelo);
        }else {
        	this.TarifaTipoVeiculo(categoria);
        }
        
        if (tarifa == 0) {
        	return 0;
        }
        double tarifaFinal = tarifa * diasDeDiferenca;
        
        return converterCusto(tarifaFinal, unidadeMonetaria);
    }
    
    
    // 2.3 Consultar Descontos
    public List<String> verificarDescontosDisponiveis() throws SQLException {
    	Connection conn = stub.getConnection();

        List<String> descontosDisponiveis = new ArrayList<>();
        String sql = ""
        		+ "SELECT Codigo " //
        		+ "FROM Desconto WHERE " //
        		+ "DataExpiracao >= CURDATE() " //
        		+ "AND NumeroUtilizacoes = -1 ";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                descontosDisponiveis.add(rs.getString("Codigo"));
            }
        }
		stub.closeConnection(conn);

        return descontosDisponiveis;
    }
    
    
    public int obterValorPercentualDesconto(String codigoDesconto) throws SQLException {
        String query = ""
        		+ "SELECT ValorPercentual "
        		+ "FROM Desconto "
        		+ "WHERE Codigo = ?";
        
        Object[] params = {codigoDesconto};
	    String[][] results = stub.executeQuery(query, params);
	    
	    if (results.length > 0 && results[0].length > 0) {
            return Integer.parseInt(results[0][0]);
        }
        
	    return -1;
    }

    
    public double custoComDesconto(String codigo, double custo) throws SQLException {
    	
    	int valorPercentual = obterValorPercentualDesconto(codigo);
    	
    	if (valorPercentual == -1 || custo == 0) {
            return custo; // Retorna o custo inicial sem aplicar desconto
            
        }
    	
        double desconto = custo * valorPercentual / 100.0;
        double custoFinal = custo - desconto;

    	return custoFinal;
    }
    
    // 2.1 Reservar Veiculo
    public boolean clienteReservarVeiculo(double custo, String unidadeMonetaria, LocalDate dataInicio, LocalDate dataFim, String categoria, String modelo, String parque, String codigo) {
    	boolean sucesso = false;
    	if(custo == 0 || (categoria == null && modelo == null) || this.tarifa == 0) {
    		return false;
    	}
    	
    	String idReserva = "";
		try {
			idReserva = Auxiliar.gerarIdReserva(stub);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        String queryInserirReserva = "";
        String aux = "";
        
        if(categoria == null) {
        	queryInserirReserva = ""
                + "INSERT INTO Reserva (ID, Tarifa, UnidadeMonetaria, DataHoraInicio, DataHoraFinal, CustoFinalPrevisto, TipoVeiculo, Modelo, NIF, ParqueEstacionamento) "
                + "VALUES (?, ?, ?, ?, ?, ?, NULL, ?, ?, ?)";
        	
        	aux = modelo;
        }else{
        	queryInserirReserva = ""
                    + "INSERT INTO Reserva (ID, Tarifa, UnidadeMonetaria, DataHoraInicio, DataHoraFinal, CustoFinalPrevisto, TipoVeiculo, Modelo, NIF, ParqueEstacionamento) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)";
        	
        	aux = categoria;
        }
        
        Object[] params = new Object[] {idReserva, this.tarifa, unidadeMonetaria, Date.valueOf(dataInicio), Date.valueOf(dataFim), custo, aux, this.nif, parque};
		sucesso = stub.executeInsertQuery(queryInserirReserva, params);
		
		if(sucesso) {
            System.out.println("Reserva efetuada com sucesso! ID da reserva: " + idReserva);
            System.out.printf("Custo final previsto: %.2f %s%n", custo, unidadeMonetaria);
            if(codigo != null) {
            	criarDescontoReserva(idReserva, codigo);
            }
		}else {
            System.out.println("Insucesso ao realizar reserva!");
		}
        
    	return sucesso;
    }
    
    
    public boolean clienteReservarVeiculo(double custo, String unidadeMonetaria, String dataInicio, String dataFim, String categoria, String modelo, String parque, String codigo, double tarifa) throws SQLException {
    	boolean sucesso = false;
    	if(custo == 0 || (categoria == null && modelo == null) || tarifa == 0) {
    		return false;
    	}
    	
    	String idReserva = Auxiliar.gerarIdReserva(stub);
    	
        String queryInserirReserva = "";
        String aux = "";
        
        if(categoria == null) {
        	queryInserirReserva = ""
                + "INSERT INTO Reserva (ID, Tarifa, UnidadeMonetaria, DataHoraInicio, DataHoraFinal, CustoFinalPrevisto, TipoVeiculo, Modelo, NIF, ParqueEstacionamento) "
                + "VALUES (?, ?, ?, ?, ?, ?, NULL, ?, ?, ?)";
        	
        	aux = modelo;
        }else{
        	queryInserirReserva = ""
                    + "INSERT INTO Reserva (ID, Tarifa, UnidadeMonetaria, DataHoraInicio, DataHoraFinal, CustoFinalPrevisto, TipoVeiculo, Modelo, NIF, ParqueEstacionamento) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)";
        	
        	aux = categoria;
        }
        
        Object[] params = new Object[] {idReserva, tarifa, unidadeMonetaria, Date.valueOf(dataInicio), Date.valueOf(dataFim), custo, aux, this.nif, parque};
		sucesso = stub.executeInsertQuery(queryInserirReserva, params);
		
		if(sucesso) {
            System.out.println("Reserva efetuada com sucesso! ID da reserva: " + idReserva);
            System.out.printf("Custo final previsto: %.2f %s%n", custo, unidadeMonetaria);
            if(codigo != null) {
            	criarDescontoReserva(idReserva, codigo);
            }
		}else {
            System.out.println("Insucesso ao realizar reserva!");
		}
        
    	return sucesso;
    }
    
    public double getTarifa() {
    	return this.tarifa;
    }
    
    public boolean criarDescontoReserva(String id, String desconto) {
    	
        String queryInserirReserva = ""
                + "INSERT INTO Desconto_Reserva (ID, Codigo) "
                + "VALUES (?, ?)";
       
        Object[] params = new Object[] {id, desconto};
        return stub.executeInsertQuery(queryInserirReserva, params);
    }
    
    // 2.2 Consultar estado das reservas
    public String[][] clienteConsultarReservas() {
		String query = "" //
				+ "SELECT DataHoraInicio, DataHoraFinal, "
				+ "EstadoReserva, CustoFinalPrevisto, " //
				+ "CustoFinalEfetivo, UnidadeMonetaria, AvaliacaoCondutor, " //
				+ "MarcaVeiculo, ModeloVeiculo "
				+ "FROM EstadoReservas "
				+ "WHERE NIF = ?";
		
		Object[] params = new Object[] {this.nif};
		String[][] results = stub.executeQuery(query, params);
		Auxiliar.printResults(results);
		return results;
	}
    
    
    // 2.3 Consultar Reputacao
    public String[][] clienteConsultarReputacao() {
		String query = "" //
				+ "SELECT CC.Nome, CC.Reputacao "
				+ "FROM Cliente_Condutor CC " //
				+ "JOIN Reserva R ON CC.NIF = R.NIF " //
				+ "WHERE R.NIF = ? " //
				+ "limit 1";
		
		Object[] params = new Object[] {this.nif};
		String[][] results = stub.executeQuery(query, params);
		Auxiliar.printResults(results);
		return results;
	}
    
    public String[][] verDescontos() {
		
		String query = ""
        		+ "SELECT Codigo, DataExpiracao, ValorPercentual " //
        		+ "FROM Desconto WHERE " //
        		+ "DataExpiracao >= CURDATE() " //
        		+ "AND NumeroUtilizacoes = -1 ";
		
		String[][] results = stub.executeQuery(query, null);
		Auxiliar.printResults(results);
		return results;
	}
    
    public Stub getStub() {
    	return this.stub;
    }

	
	public static void main(String[] args) throws SQLException {

		Cliente cliente = new Cliente("123456789012");
        
        Auxiliar.listarParques(cliente.getStub());

        System.out.println("\nLista de todos os modelos disponíveis:");
        cliente.listarModelosDisponiveis();
        
        System.out.println("\nLista de todas as categorias disponíveis:");
        cliente.listarCategoriasVeiculosDisponiveis();
      
        LocalDate dataInicio = LocalDate.of(2024, 12, 29);
        LocalDate dataFim = LocalDate.of(2025, 1, 03);
        String tipoOuModelo = "Corolla"; 
        String parque = "Avenida Marginal, Cascais, 2750-334";
        String unidadeMonetaria = "EUR";
        String codigoDesconto = "NATAL2024";
        
        double custo = cliente.calcularCustoPrevisto(dataInicio, dataFim, null, tipoOuModelo, unidadeMonetaria);
        
        System.out.println("\nCusto: " + custo);
        
        System.out.println("\nDescontos Disponiveis: " + cliente.verificarDescontosDisponiveis());
        custo = cliente.custoComDesconto(codigoDesconto, custo);
        System.out.println("\nCusto com desconto: " + custo);
        
        System.out.println("\n Deseja realizar a reserva? Sim");
        
        cliente.clienteReservarVeiculo(custo, unidadeMonetaria, dataInicio, dataFim, null, tipoOuModelo, parque, codigoDesconto);
        
        System.out.println("\n Consultar Reservas:");
        cliente.clienteConsultarReservas();
        
        System.out.println("\n Consultar Reputação:");
        cliente.clienteConsultarReputacao();
    }
}
