package pt.isel.leim.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Auxiliar;
import pt.isel.leim.sbd.Gerente;
	
@WebServlet("/Gerente3Servlet")
public class Gerente3Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	    
		String numeroParam = request.getParameter("entries");
		String semanasParam = request.getParameter("semanas");

		int numero = -1;
		int semanas = -1;
		if (numeroParam != null && !numeroParam.isEmpty()) {
			numero = Integer.parseInt(numeroParam);
		}
		
		if (semanasParam != null && !semanasParam.isEmpty()) {
			semanas = Integer.parseInt(semanasParam);
		}
		
		Gerente gerente = new Gerente();
		String[][] resultados = gerente.gerenteRankingMelhorAvaliacao(numero, semanas);
		String[] titulos = {"Marca", "Modelo"};
		String resultadoTabela = Auxiliar.generateHtmlTableWithRank(resultados, titulos);

		request.setAttribute("resultados", resultadoTabela);

        request.getRequestDispatcher("gerenteMelhorAval.jsp").forward(request, response);
    }
}