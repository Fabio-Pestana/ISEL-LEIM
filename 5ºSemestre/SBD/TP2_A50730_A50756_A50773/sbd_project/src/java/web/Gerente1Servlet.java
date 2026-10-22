package pt.isel.leim.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Auxiliar;
import pt.isel.leim.sbd.Gerente;
import pt.isel.leim.sbd.Stub;
	
@WebServlet("/Gerente1Servlet")
public class Gerente1Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		String matricula = request.getParameter("license-plate");
		String numeroParam = request.getParameter("months");
        String fullHistory = request.getParameter("full-history"); // A checkbox
        
        boolean historico = fullHistory != null;
		int meses = -1;
		if (numeroParam != null && !numeroParam.isEmpty()) {
			meses = Integer.parseInt(numeroParam);
		}
		
		boolean checkMatricula = Auxiliar.verificarMatricula(new Stub(), matricula);
		
		if(checkMatricula) {
			Gerente gerente = new Gerente();
			String[][] resultados = gerente.gerenteHistoricoVeiculo(matricula, meses, historico);
			String[] titulos = {"Tipo Intervenção", "Id", "Data e hora da Intervencao",
					"Data e hora fim da Intervencao", "Custo"};
			String resultadoTabela = Auxiliar.generateHtmlTable(resultados, titulos);
			request.setAttribute("resultados", resultadoTabela);

		}else {
			request.setAttribute("erro", "Matricula "+ matricula + " nao encontrada");
		}

        request.getRequestDispatcher("gerenteHistorico.jsp").forward(request, response);
    }
}