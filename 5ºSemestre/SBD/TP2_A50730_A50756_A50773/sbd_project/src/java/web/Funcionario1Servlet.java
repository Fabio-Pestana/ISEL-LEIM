package pt.isel.leim.web;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Auxiliar;
import pt.isel.leim.sbd.Funcionario;
	

@WebServlet("/Funcionario1Servlet")
public class Funcionario1Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String matricula = request.getParameter("matricula");
		String reserva = request.getParameter("reserva");
		
		Funcionario funcionario = new Funcionario();
		boolean aux;
		try {
			aux = funcionario.funcionarioCriarAluguer(reserva, matricula);
			if(aux) {
				funcionario.alterarEstadoVeiculo(matricula);
				String msg = "Sucesso na atribuição do veiculo " + matricula + " na reserva: " + reserva;
				request.setAttribute("mensagem", msg);
			}else {
				String msg = "Insucesso na atribuição do veiculo " + matricula + " na reserva: " + reserva;
				request.setAttribute("mensagem", msg);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		


        request.getRequestDispatcher("ReservasPendentes.jsp").forward(request, response);
    }
}
