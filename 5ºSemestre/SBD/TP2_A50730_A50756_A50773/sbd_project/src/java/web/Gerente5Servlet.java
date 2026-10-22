package pt.isel.leim.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Auxiliar;
import pt.isel.leim.sbd.Gerente;
	
@WebServlet("/Gerente5Servlet")
public class Gerente5Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String freguesia = request.getParameter("freguesia");

		Gerente gerente = new Gerente();
		String[][] resultados = gerente.gerenteRankingClientes(freguesia);
		String[] titulos = {"NIF", "Nome do Cliente", "Reputação"};
		String resultadoTabela = Auxiliar.generateHtmlTableWithRank(resultados, titulos);

		request.setAttribute("resultados", resultadoTabela);
		request.setAttribute("extra", "Resultados para a freguesia: " + freguesia);

        request.getRequestDispatcher("gerenteClientes.jsp").forward(request, response);
    }
}