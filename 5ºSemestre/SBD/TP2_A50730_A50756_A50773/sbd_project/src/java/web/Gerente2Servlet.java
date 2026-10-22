package pt.isel.leim.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Auxiliar;
import pt.isel.leim.sbd.Gerente;
	
@WebServlet("/Gerente2Servlet")
public class Gerente2Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	    
		String numeroParam = request.getParameter("num-entries");
		int numero = -1;
		if (numeroParam != null && !numeroParam.isEmpty()) {
			numero = Integer.parseInt(numeroParam);
		}
		
		Gerente gerente = new Gerente();
		
		String[][] resultados = gerente.gerenteRankingMenorLucro(numero);
		String[] titulos = {"Marcas", "Lucro em euros"};
		String resultadoTabela = Auxiliar.generateHtmlTableWithRank(resultados, titulos);

		request.setAttribute("resultados", resultadoTabela);

        request.getRequestDispatcher("gerenteRankingLucro.jsp").forward(request, response);
    }
}