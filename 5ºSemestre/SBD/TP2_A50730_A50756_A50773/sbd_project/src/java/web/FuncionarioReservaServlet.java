package pt.isel.leim.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Auxiliar;
import pt.isel.leim.sbd.Funcionario;
	

@WebServlet("/FuncionarioReservaServlet")
public class FuncionarioReservaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String reserva = request.getParameter("reserva");
		
		Funcionario funcionario = new Funcionario();
		String[][] resultados = funcionario.consultarVeiculosDisponiveis(reserva);
		String[] titulos = {"Matricula", "Marca", "Modelo"};
		String resultadoTabela = Auxiliar.generateHtmlTable(resultados, titulos);
		request.setAttribute("resultados", resultadoTabela);
		request.setAttribute("reserva", reserva);
		
		// Converte o resultado em uma String de opções para a lista
		StringBuilder opcoes = new StringBuilder();
		for (String[] row : resultados) {
		    String matricula = row[0];
		    opcoes.append("<option value=\"").append(matricula).append("\">").append(matricula).append("</option>");
		}

		request.setAttribute("opcoes", opcoes.toString());

        request.getRequestDispatcher("funcionarioAtribuirVeiculo.jsp").forward(request, response);
    }
}
