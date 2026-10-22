package pt.isel.leim.sbd;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Administrador {
	
	private Stub stub;
	
	public Administrador() {
		this.stub = new Stub();
	}
	
	
	public boolean atualizarCliente(String atributo, String novoValor, String nif) {
	    // Verificar se o atributo é válido
	    List<String> atributosValidos = Arrays.asList(
	        "Tipo", "Nome", "Distrito", "Concelho", "Freguesia", 
	        "Mail", "Telemovel", "CapitalSocial", "Lingua", "PreferenciaCultural"
	    );

	    if (!atributosValidos.contains(atributo)) {
	    	System.out.println("Atributo inválido: " + atributo);
	        return false; 
	    }
	    
	    if(!Auxiliar.verificarNIF(stub, nif)) {
	    	return false;
	    }
	    
	    if(atributo.equals("Mail")) {
	    	if(!Auxiliar.isValidEmailAddress(novoValor)) {
	    		return false;
	    	}
	    }
	    
	    if(atributo.equals("Telemovel")) {
	    	if(!Auxiliar.isNumberValid(novoValor)) {
	    		return false;
	    	}
	    }
	    
	    if(atributo.equals("Nome")) {
	    	if(!Auxiliar.isNameValid(novoValor)) {
	    		return false;
	    	}
	    }
	    
	    String query = ""
	    		+ "UPDATE Cliente "
	    		+ "SET " + atributo + " = ? "
	    		+ "WHERE NIF = ?";
	    
	    Object[] params = new Object[] {novoValor, nif};
	    		
	    return stub.executeInsertQuery(query, params);
	}
	
	
	public boolean atualizarCondutor(String atributo, String novoValor, String nif, String cc) {
	    // Verificar se o atributo é válido 
	    List<String> atributosValidos = Arrays.asList(
	        "Nome", "DataEmissaoCarta", "DataNascimento", 
	        "ValidadeCarta"
	    );

	    if (!atributosValidos.contains(atributo)) {
	    	System.out.println("Atributo inválido: " + atributo);
	        return false; 
	    }
	    
	    if(!Auxiliar.verificaCartaAssociada(stub, nif, cc)) {
	    	return false;
	    }
	    
	    if(atributo.equals("Nome")) {
	    	if(!Auxiliar.isNameValid(novoValor)) {
	    		return false;
	    	}
	    }
	    
	    String query = ""
	    		+ "UPDATE Cliente_Condutor "
	    		+ "SET " + atributo + " = ? "
	    		+ "WHERE NIF = ? AND "
	    		+ "NumeroCartaConducao = ?";
	    
	    Object[] params = new Object[] {novoValor, nif, cc};
	    		
	    return stub.executeInsertQuery(query, params);
	}
	
	
	public boolean atualizarVeiculo(String atributo, String novoValor, String matricula) {
	    // Verificar se o atributo é válido 
	    List<String> atributosValidos = Arrays.asList(
	        "Cor", "Marca", "Modelo", "NumeroLugares", "NumeroPortas",
	        "PotenciaMotor", "TipoMotor", "Estado", "RegistoKm", "Categoria"
	    );

	    if (!atributosValidos.contains(atributo)) {
	    	System.out.println("Atributo inválido: " + atributo);
	        return false; 
	    }
	    
	    if(!Auxiliar.verificarMatricula(stub, matricula)) {
	    	return false;
	    }
	    
	    if(atributo.equals("TipoMotor")) {
	    	if(!(novoValor.equals("gasolina") || novoValor.equals("gasóleo") || novoValor.equals("GPL") || novoValor.equals("elétrico") || novoValor.equals("híbrido"))) {
	    		return false;
	    	}
	    }
	    
	    if(atributo.equals("Estado")) {
	    	if(!(novoValor.equals("disponível") || novoValor.equals("alugado") || novoValor.equals("manutenção") || novoValor.equals("reservado"))) {
	    		return false;
	    	}
	    }
        
        
	    
	    String query = ""
	    		+ "UPDATE Veiculo "
	    		+ "SET " + atributo + " = ? "
	    		+ "WHERE Matricula = ?";
	    
	    Object[] params = new Object[] {novoValor, matricula};
	    		
	    return stub.executeInsertQuery(query, params);
	}
	
	
	public boolean criarCliente(String nif, String tipo, String nome, String distrito, String concelho, String freguesia, String mail, 
            String telemovel, int capitalSocial, String lingua, String preferenciaCultural) {
		String queryCriarCliente = "";
		Object[] params = null;
		
		nif = nif.trim();
		if(nif.length() < 12) {
			return false;
		}
		
		if(Auxiliar.verificarNIF(stub, nif)) {
	    	return false;
	    }
	    
		if(!Auxiliar.isValidEmailAddress(mail)) {
	    	return false;
	    }
	    
	   
	    if(!Auxiliar.isNumberValid(telemovel)) {
	    	return false;
	    }
	    
	    if(!Auxiliar.isNameValid(nome)) {
	    	return false;
	    }
	    
	    if(capitalSocial == 0 && distrito.equals("")) {
	    	queryCriarCliente = ""
	                + "INSERT INTO Cliente (NIF, Tipo, Nome, Distrito, Concelho, Freguesia, Mail, Telemovel, CapitalSocial, Lingua, PreferenciaCultural) "
	                + "VALUES (?, ?, ?, Null, Null, Null, ?, ?, Null, ?, ?)";
	    	
			params = new Object[] {nif, tipo, nome, mail, telemovel,  lingua,  preferenciaCultural};
	    }else if(capitalSocial == 0) {
			queryCriarCliente = ""
	                + "INSERT INTO Cliente (NIF, Tipo, Nome, Distrito, Concelho, Freguesia, Mail, Telemovel, CapitalSocial, Lingua, PreferenciaCultural) "
	                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, Null, ?, ?)";
	    	
			params = new Object[] {nif, tipo, nome, distrito, concelho, freguesia, mail, telemovel,  lingua,  preferenciaCultural};
		}else if(distrito.equals("")){
			queryCriarCliente = ""
	                + "INSERT INTO Cliente (NIF, Tipo, Nome, Distrito, Concelho, Freguesia, Mail, Telemovel, CapitalSocial, Lingua, PreferenciaCultural) "
	                + "VALUES (?, ?, ?, Null, Null, Null, ?, ?, ?, ?, ?)";
	    	
			params = new Object[] {nif, tipo, nome, mail, telemovel, capitalSocial, lingua,  preferenciaCultural};
		}else {
			queryCriarCliente = ""
	                + "INSERT INTO Cliente (NIF, Tipo, Nome, Distrito, Concelho, Freguesia, Mail, Telemovel, CapitalSocial, Lingua, PreferenciaCultural) "
	                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    	
			params = new Object[] {nif, tipo, nome, distrito, concelho, freguesia, mail, telemovel, capitalSocial, lingua,  preferenciaCultural};
		}
    	
    	return stub.executeInsertQuery(queryCriarCliente, params);
    }
	
	public boolean criarCondutor(String nif, String cc, String nome, LocalDate dataEmissaoCarta, LocalDate dataNascimento, LocalDate validadeCarta) {
		nif = nif.trim();
		if(nif.length() < 12) {
			return false;
		}
		
		cc = cc.trim();
		if(cc.length() < 9) {
			return false;
		}
		
		if(Auxiliar.verificaCartaAssociada(stub, nif, cc)) {
	    	return false;
	    }
	    
	    if(!Auxiliar.isNameValid(nome)) {
	    	return false;
	    }
	    
		String queryCriarCondutor = ""
                + "INSERT INTO Cliente_Condutor (NIF, NumeroCartaConducao, Nome, DataEmissaoCarta, DataNascimento, ValidadeCarta, Reputacao) "
                + "VALUES (?, ?, ?, ?, ?, ?, Null)";
    	
		Object[] params = new Object[] {nif, cc, nome, dataEmissaoCarta, dataNascimento, validadeCarta};
    	
    	return stub.executeInsertQuery(queryCriarCondutor, params);
    }
	
	
	public boolean criarCondutorWeb(String nif, String cc, String nome, String dataEmissaoCarta, String dataNascimento, String validadeCarta) {
		nif = nif.trim();
		if(nif.length() < 12) {
			return false;
		}
		
		cc = cc.trim();
		if(cc.length() < 9) {
			return false;
		}
		
		if(Auxiliar.verificaCartaAssociada(stub, nif, cc)) {
	    	return false;
	    }
		
		if(!Auxiliar.verificarNIF(stub, nif)) {
	    	return false;
	    }
	    
	    if(!Auxiliar.isNameValid(nome)) {
	    	return false;
	    }
	    
		String queryCriarCondutor = ""
                + "INSERT INTO Cliente_Condutor (NIF, NumeroCartaConducao, Nome, DataEmissaoCarta, DataNascimento, ValidadeCarta, Reputacao) "
                + "VALUES (?, ?, ?, ?, ?, ?, Null)";
    	
		Object[] params = new Object[] {nif, cc, nome, dataEmissaoCarta, dataNascimento, validadeCarta};
    	
    	return stub.executeInsertQuery(queryCriarCondutor, params);
    }
	
	public boolean criarVeiculo(String matricula, String cor, String marca, String modelo, int lugares, int portas, int potencia, String motor,
			String estado, double km, String categoria) {
		
		if(lugares <=0 || portas <=0 || potencia <=0 || km < 0)
			return false;
		
		if(Auxiliar.verificarMatricula(stub, matricula)) {
	    	return false;
	    }
		
		String queryCriarCondutor = ""
                + "INSERT INTO Veiculo (Matricula, Cor, Marca, Modelo, NumeroLugares, NumeroPortas, PotenciaMotor, TipoMotor, Estado, FotosVideos, RegistoKm, Categoria) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, Null, ?, ?)";
    	
		Object[] params = new Object[] {matricula, cor, marca, modelo, lugares, portas, potencia, motor, estado, km, categoria};
    	
    	return stub.executeInsertQuery(queryCriarCondutor, params);
    }
	
	
	public void exportarDadosParaJson(String matricula, String inicio, String fim, String caminhoFicheiro) {
		
        String queryVeiculo = ""
        		+ "SELECT * "
        		+ "FROM Veiculo "
        		+ "WHERE Matricula = ?";
        
        Object[] params = new Object[]{matricula};
        String[][] veiculoDados = stub.executeQuery(queryVeiculo, params);

        if (veiculoDados.length == 0) {
            System.out.println("Nenhum veículo encontrado com a matrícula: " + matricula);
            return;
        }

        String[] colunas = new String[]{"Matricula", "Cor", "Marca", "Modelo",
        		"NumeroLugares", "NumeroPortas", "PotenciaMotor", "TipoMotor", "Estado",
        		"FotosVideos", "RegistoKm", "Categoria"};
        String jsonString = gerarJsonString(veiculoDados, colunas, "veiculo", true, false);
        
        String queryAlugueres = ""
        		+ "SELECT * "
        		+ "FROM ReservasComAluguer "
        		+ "WHERE Matricula = ? AND "
        		+ "DataHoraInicio BETWEEN ? AND ?";
        
        
        Object[] paramsAR = new Object[]{matricula, inicio, fim};
        String[][] ARDados = stub.executeQuery(queryAlugueres, paramsAR);
        
        if (ARDados.length == 0) {
            System.out.println("Veiculo nao tem alugueres: " + matricula);
        }else {
        	String[] str = new String[]{"IDReserva", "Tarifa", "UnidadeMonetaria", "DataHoraInicio",
            		"DataHoraFinal", "CustoPrevisto", "NIF", "TipoVeiculo", "ModeloVeiculo",
            		"ParqueDoLevantamento", "IDAluguer", "CustoFinal", "AvaliacaoCondutor",
            		"Matricula", "KmPercorridos"};
        	jsonString = jsonString + gerarJsonString(ARDados, str, "AlugueresEReservas", false, false);
        }

        String queryIntervencoes = ""
        		+ "SELECT * "
        		+ "FROM Intervencao "
        		+ "WHERE Matricula = ? AND "
        		+ "DataHoraIntervencao BETWEEN ? AND ?";
        
        
        Object[] paramsI = new Object[]{matricula, inicio, fim};
        String[][] intervencoesDados = stub.executeQuery(queryIntervencoes, paramsI);
        
        if (intervencoesDados.length == 0) {
            System.out.println("Veiculo nao tem intervencoes: " + matricula);
        }else {
        	String[] strI = new String[]{"DataHoraIntervencao", "Custo", "Tipo", "Matricula"};
        	jsonString = jsonString + gerarJsonString(intervencoesDados, strI, "Intervencoes", false, true);
        }
        
        System.out.println(jsonString);
        JSONObject finalJson = new JSONObject(jsonString);

        // Gerar o arquivo JSON
        try (FileWriter file = new FileWriter(caminhoFicheiro)) {
            file.write(finalJson.toString(4));  // Escrever com indentação 
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Arquivo JSON exportado com sucesso para: " + caminhoFicheiro);
    }
	
	
	
	public String exportarDadosParaJson(String matricula, String inicio, String fim) {
		
        String queryVeiculo = ""
        		+ "SELECT * "
        		+ "FROM Veiculo "
        		+ "WHERE Matricula = ?";
        
        Object[] params = new Object[]{matricula};
        String[][] veiculoDados = stub.executeQuery(queryVeiculo, params);

        if (veiculoDados.length == 0) {
            System.out.println("Nenhum veículo encontrado com a matrícula: " + matricula);
            return "{}";
        }

        String[] colunas = new String[]{"Matricula", "Cor", "Marca", "Modelo",
        		"NumeroLugares", "NumeroPortas", "PotenciaMotor", "TipoMotor", "Estado",
        		"FotosVideos", "RegistoKm", "Categoria"};
        String jsonString = gerarJsonString(veiculoDados, colunas, "veiculo", true, false);
        
        String queryAlugueres = ""
        		+ "SELECT * "
        		+ "FROM ReservasComAluguer "
        		+ "WHERE Matricula = ? AND "
        		+ "DataHoraInicio BETWEEN ? AND ?";
        
        
        Object[] paramsAR = new Object[]{matricula, inicio, fim};
        String[][] ARDados = stub.executeQuery(queryAlugueres, paramsAR);
        
        if (ARDados.length == 0) {
            System.out.println("Veiculo nao tem alugueres: " + matricula);
        }else {
        	String[] str = new String[]{"IDReserva", "Tarifa", "UnidadeMonetaria", "DataHoraInicio",
            		"DataHoraFinal", "CustoPrevisto", "NIF", "TipoVeiculo", "ModeloVeiculo",
            		"ParqueDoLevantamento", "IDAluguer", "CustoFinal", "AvaliacaoCondutor",
            		"Matricula", "KmPercorridos"};
        	jsonString = jsonString + gerarJsonString(ARDados, str, "AlugueresEReservas", false, false);
        }

        String queryIntervencoes = ""
        		+ "SELECT * "
        		+ "FROM Intervencao "
        		+ "WHERE Matricula = ? AND "
        		+ "DataHoraIntervencao BETWEEN ? AND ?";
        
        
        Object[] paramsI = new Object[]{matricula, inicio, fim};
        String[][] intervencoesDados = stub.executeQuery(queryIntervencoes, paramsI);
        
        if (intervencoesDados.length == 0) {
            System.out.println("Veiculo nao tem intervencoes: " + matricula);
        }else {
        	String[] strI = new String[]{"DataHoraIntervencao", "Custo", "Tipo", "Matricula"};
        	jsonString = jsonString + gerarJsonString(intervencoesDados, strI, "Intervencoes", false, true);
        }
        
        System.out.println(jsonString);
        JSONObject finalJson = new JSONObject(jsonString);

        return finalJson.toString(4);
    }

	
	public String gerarJsonString(String[][] resultados, String[] colunas, String objeto, boolean inicio, boolean fim) {
	    StringBuilder jsonBuilder = new StringBuilder();
	    
	    // Começar a construção do JSON
	    if (inicio)
	    	jsonBuilder.append("{");
	    
	    jsonBuilder.append("\"").append(objeto).append("\": [");

	    for (int i = 0; i < resultados.length; i++) {
	        jsonBuilder.append("{");
	        for (int j = 0; j < colunas.length; j++) {
	            if (resultados[i][j] != null) {
	                jsonBuilder.append("\"").append(colunas[j]).append("\":\"")
	                        .append(resultados[i][j]).append("\"");
	            } else {
	                jsonBuilder.append("\"").append(colunas[j]).append("\":\"")
	                        .append("vazio").append("\"");
	            }

	            if (j < colunas.length - 1) {
	                jsonBuilder.append(", ");
	            }
	        }
	        jsonBuilder.append("}");

	        if (i < resultados.length - 1) {
	            jsonBuilder.append(", ");
	        }
	    }
	    
	    jsonBuilder.append("]");

	    // Fecha o JSON
	    if(fim)
	    	jsonBuilder.append("}");
	    else {
	    	jsonBuilder.append(",");
	    }
	    
	    return jsonBuilder.toString();
	}
	
	public boolean checkCategoria(String categoria, String[][] categorias) {
		for(String[] c: categorias) {
			System.out.println(c[0]);
			if(categoria.equals(c[0])) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean checkParque(String parque, String[][] parques) {
		for(String[] c: parques) {
			if(parque.equals(c[0])) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean processarJson(String jsonInput) {
        JSONObject jsonObj = new JSONObject(jsonInput);
        
        JSONArray veiculos = jsonObj.getJSONArray("veiculo");
        JSONObject veiculo = veiculos.getJSONObject(0);
        String registoKm = veiculo.getString("RegistoKm");
        String categoria = veiculo.getString("Categoria");
        String numeroPortas = veiculo.getString("NumeroPortas");
        String tipoMotor = veiculo.getString("TipoMotor");
        String marca = veiculo.getString("Marca");
        String modelo = veiculo.getString("Modelo");
        String matricula = veiculo.getString("Matricula");
        String cor = veiculo.getString("Cor");
        String numeroLugares = veiculo.getString("NumeroLugares");
        String potenciaMotor = veiculo.getString("PotenciaMotor");
        String estado = veiculo.getString("Estado");
        
        if(Auxiliar.verificarMatricula(stub, matricula)) {
        	System.out.println("Veiculo ja existe");
        	return false;
        }
        
        boolean CheckMotor = !(tipoMotor.equals("gasolina") || tipoMotor.equals("gasóleo") || tipoMotor.equals("GPL") || tipoMotor.equals("elétrico") || tipoMotor.equals("híbrido"));
        boolean CheckEstado = !(estado.equals("disponível") || estado.equals("alugado") || estado.equals("manutenção") || estado.equals("reservado"));
        
    	String[][] tiposVeiculos = Auxiliar.tiposVeiculos(stub);
        boolean CheckCategoria = !(checkCategoria(categoria, tiposVeiculos));
        
        if(CheckMotor || CheckEstado || CheckCategoria) {
        	return false;
        }
        
        boolean CheckVeiculo = criarVeiculo(matricula, cor, marca, modelo, Integer.parseInt(numeroLugares), Integer.parseInt(numeroPortas), Integer.parseInt(potenciaMotor), 
        		tipoMotor, estado, Double.valueOf(registoKm), categoria);
        
        if(!CheckVeiculo) {
        	System.out.println("Insucesso ao criar veiculo");
        	return false;
        }
        
        // Processar as intervenções
        JSONArray intervencoes = jsonObj.getJSONArray("Intervencoes");
        System.out.println("\nIntervenções:");
        Funcionario func = new Funcionario();
        
        for (int i = 0; i < intervencoes.length(); i++) {
            JSONObject intervencao = intervencoes.getJSONObject(i);
            String tipo = intervencao.getString("Tipo");
            String custo = intervencao.getString("Custo");
            String dataHoraIntervencao = intervencao.getString("DataHoraIntervencao");
            boolean CheckTipo = tipo.equals("reparação") || tipo.equals("manutenção");
            if(CheckTipo) {
            	func.funcionarioCriarIntervencao(dataHoraIntervencao, Double.valueOf(custo), tipo, matricula);
            }
        }

        // Processar os alugueres e reservas
        JSONArray alugueresEReservas = jsonObj.getJSONArray("AlugueresEReservas");
        System.out.println("\nAlugueres e Reservas:");
    	String[][] parques = Auxiliar.listarParques(stub);
        for (int i = 0; i < alugueresEReservas.length(); i++) {
            JSONObject aluguer = alugueresEReservas.getJSONObject(i);
            String modeloVeiculo = aluguer.getString("ModeloVeiculo");
            String nif = aluguer.getString("NIF");
            String idReserva = aluguer.getString("IDReserva");
            double custoPrevisto = Double.valueOf(aluguer.getString("CustoPrevisto"));
            double custoFinal = Double.valueOf(aluguer.getString("CustoFinal"));
            String parque = aluguer.getString("ParqueDoLevantamento");
            String dataHoraInicio = aluguer.getString("DataHoraInicio");
            String dataHoraFinal = aluguer.getString("DataHoraFinal");
            int tarifa = Integer.parseInt(aluguer.getString("Tarifa"));
            String moeda = aluguer.getString("UnidadeMonetaria");
            int avaliacaoCondutor = Integer.parseInt(aluguer.getString("AvaliacaoCondutor"));
            String tipoVeiculo = aluguer.getString("TipoVeiculo");
            String idAluguer = aluguer.getString("IDAluguer");
            double kmPercorridos = Double.valueOf(aluguer.getString("KmPercorridos"));
            
            if(tipoVeiculo.equals("vazio")) {
            	tipoVeiculo = null;
            }else if(modeloVeiculo.equals("vazio")) {
            	modeloVeiculo = null;
            }
            
            boolean reserva = reservaVeiculo(idReserva, tarifa, nif, custoPrevisto, moeda, dataHoraInicio, dataHoraFinal, tipoVeiculo, modeloVeiculo, parque, parques);
            
            if(reserva) {
            	criarAluguer(idAluguer, idReserva, matricula, custoFinal, avaliacaoCondutor, kmPercorridos);
            }
        }
        return true;
    }
	
	
	public boolean reservaVeiculo(String id, int tarifa, String nif, double custo, String moeda, String dataInicio, String dataFim, String categoria, String modelo, String parque, String[][] parques) {
    	boolean sucesso = false;
        boolean CheckMoeda = !(moeda.equals("EUR") || moeda.equals("USD") || moeda.equals("GBP") ||
        		moeda.equals("BRL") || moeda.equals("JPY") || moeda.equals("CHF") || moeda.equals("CNY"));
        
    	boolean checkId = false;
		try {
			checkId = Auxiliar.idReservaExiste(stub, id);
		} catch (SQLException e) {}
    	
        boolean CheckCategoria = !(checkParque(parque, parques));
        
        boolean checkNif = !(Auxiliar.verificarNIF(stub, nif));
    	
    	if(custo <= 0 || tarifa <= 0 || checkId || CheckMoeda || CheckCategoria || checkNif) {
    		return false;
    	}
    	    	
        String queryInserirReserva = "";
        String aux = "";
        
        if(categoria == null && modelo != null) {
        	queryInserirReserva = ""
                + "INSERT INTO Reserva (ID, Tarifa, UnidadeMonetaria, DataHoraInicio, DataHoraFinal, CustoFinalPrevisto, TipoVeiculo, Modelo, NIF, ParqueEstacionamento) "
                + "VALUES (?, ?, ?, ?, ?, ?, NULL, ?, ?, ?)";
        	
        	aux = modelo;
        }else if(categoria != null && modelo == null){
        	queryInserirReserva = ""
                    + "INSERT INTO Reserva (ID, Tarifa, UnidadeMonetaria, DataHoraInicio, DataHoraFinal, CustoFinalPrevisto, TipoVeiculo, Modelo, NIF, ParqueEstacionamento) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)";
        	
        	aux = categoria;
        }
        
        Object[] params = new Object[] {id, tarifa, moeda, dataInicio, dataFim, custo, aux, nif, parque};
		sucesso = stub.executeInsertQuery(queryInserirReserva, params);
		
		if(sucesso) {
            System.out.println("Reserva efetuada com sucesso! ID da reserva: " + id);
            System.out.printf("Custo final previsto: %.2f %s%n", custo, moeda);
		}else {
            System.out.println("Insucesso ao realizar reserva!");
		}
        
    	return sucesso;
    }
	
	
	public boolean criarAluguer(String idAluguer, String ReservaId, String matricula, double custo, int aval, double km) {		
		boolean sucesso = false;
		
		boolean CheckAval = !(aval >= 1 && aval <= 5);
        
    	boolean checkId = false;
		try {
			checkId = Auxiliar.idAluguerExiste(stub, idAluguer);
		} catch (SQLException e) {}
    	
        
    	
    	if(custo <= 0 || km <= 0 || checkId || CheckAval) {
    		return false;
    	}
    	
    	String queryInserirAluguer = ""
                + "INSERT INTO Aluguer (ID, IDReserva, CustoFinalEfetivo, AvaliacaoCondutor, Matricula, KmPercorridos) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
    	
		Object[] params = new Object[] {idAluguer, ReservaId, custo, aval, matricula, km};
		sucesso = stub.executeInsertQuery(queryInserirAluguer, params);
		if(sucesso) {
            System.out.println("Aluguer efetuada com sucesso! ID do Aluguer: " + idAluguer);
		}else {
            System.out.println("Insucesso ao criar aluguer! ID da reserva: " + ReservaId);
		}
    	
    	return sucesso;
    }
	
	
	public static String lerFicheiroJson(String caminhoArquivo) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(caminhoArquivo)))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

	
	public static void main(String[] args) {
        
		Administrador admin = new Administrador();
		/*
		String nif = "002345678901";
		String atributo = "Freguesia";
		String valor = "Alcântara";
		
		System.out.println(admin.atualizarCliente(atributo, valor, nif));
		
		String nif2 = "002345678901";
		String cc = "CN9099207484";
		String atributo2 = "DataNascimento";
		String valor2 = "1991-05-12";
        
		System.out.println(admin.atualizarCondutor(atributo2, valor2, nif2, cc));
		
		
		String matricula = "AA-01-AA";
		String atributo3 = "Cor";
		String valor3 = "Azul";
        
		System.out.println(admin.atualizarVeiculo(atributo3, valor3, matricula));
		
		
		boolean clienteCriado = admin.criarCliente(
	                "000006789012", // NIF
	                "pessoa", // Tipo
	                "MAZAD Silva", // Nome
	                "Lisboa", // Distrito
	                "Lisboa", // Concelho
	                "Benfica", // Freguesia
	                "mazad.silva@email.com", // Email
	                "912345678", // Telemóvel
	                0, // Capital Social
	                "Português", // Língua
	                "Cinema" // Preferência Cultural
	            );

	   if (clienteCriado) {
		   System.out.println("Cliente criado com sucesso!");
	   } else {
	       System.out.println("Falha ao criar o cliente.");
	   }

	   boolean condutorCriado = admin.criarCondutor(
		   "000006789012", // NIF
		   "CN1200007890", // Número do CC
		   "MAZAD Silva", // Nome
		   LocalDate.of(2020, 1, 15), // Data de Emissão da Carta
		   LocalDate.of(1990, 5, 20), // Data de Nascimento
		   LocalDate.of(2030, 1, 15) // Validade da Carta
		   );

	   if (condutorCriado) {
		   System.out.println("Condutor criado com sucesso!");
	   } else {
		   System.out.println("Falha ao criar o condutor.");
	   }

	   boolean veiculoCriado = admin.criarVeiculo(
			   "AA-12-BC", // Matrícula
			   "Preto", // Cor
			   "Toyota", // Marca
			   "Corolla", // Modelo
			   5, // Número de Lugares
			   4, // Número de Portas
			   130, // Potência do Motor
			   "gasolina", // Tipo de Motor
			   "disponível", // Estado
			   25000.0, // Quilometragem
			   "familiar" // Categoria
			   );

	   if (veiculoCriado) {
		   System.out.println("Veículo criado com sucesso!");
	   } else {
		   System.out.println("Falha ao criar o veículo.");
	   }
	   String matricula = "AA-01-AA";  // Exemplo de matrícula
        String inicio = "2024-01-01 00:00:00";  // Exemplo de data de início
        String fim = "2024-12-31 23:59:59";  // Exemplo de data de fim
        String caminhoFicheiro = "C:\\Users\\fabio\\OneDrive\\Ambiente de Trabalho\\licenciatura\\5ºSemestre\\SBD\\veiculo_dados3.json";  // Caminho onde o JSON será salvo
        admin.exportarDadosParaJson(matricula, inicio, fim, caminhoFicheiro);
	   */
		
        String caminhoFicheiro = "C:\\Users\\fabio\\OneDrive\\Ambiente de Trabalho\\licenciatura\\5ºSemestre\\SBD\\veiculo_dados3.json";  // Caminho onde o JSON será salvo

	
		String jsonInput = lerFicheiroJson(caminhoFicheiro);
        
        // Se o conteúdo do arquivo não for nulo, processa o JSON
        if (jsonInput != null) {
            admin.processarJson(jsonInput);
        } else {
            System.out.println("Erro ao ler o arquivo JSON.");
        }
    }

}
