package pt.isel.leim.web;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pt.isel.leim.sbd.Cliente;
	

@WebServlet("/Cliente1Servlet")
public class Cliente1Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	    HttpSession session = request.getSession();
	    String nif = (String) session.getAttribute("nif");
	    
		String parque = request.getParameter("Parque");
		String veiculo = request.getParameter("TipoVeiculo");
		String modelo = request.getParameter("Modelo");
		String inicio = request.getParameter("DataInicio");
		String fim = request.getParameter("DataFim");
		String unidade = request.getParameter("UnidadeMonetaria");

		LocalDate dataInicio = LocalDate.parse(inicio);
		LocalDate dataFim = LocalDate.parse(fim);
		

		Cliente cliente = new Cliente(nif);
		
		try {
			double custo = cliente.calcularCustoPrevisto(dataInicio, dataFim, veiculo, modelo, unidade );
			if(custo>0) {
				if(veiculo == null) {
					veiculo = "";
				}else if(modelo == null) {
					modelo = "";
				}
				request.setAttribute("Parque", parque);
				request.setAttribute("TipoVeiculo", veiculo);
				request.setAttribute("Modelo", modelo);
				request.setAttribute("DataInicio", inicio);
				request.setAttribute("DataFim", fim);
				request.setAttribute("UnidadeMonetaria", unidade);
				request.setAttribute("Custo", custo);
				request.setAttribute("Tarifa", cliente.getTarifa());
		        request.getRequestDispatcher("clienteFinalizarReserva.jsp").forward(request, response);
			}else {
				request.setAttribute("mensagem", "Ocorreu um erro ao processar a reserva.");
		        request.getRequestDispatcher("clienteReservar.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
}


