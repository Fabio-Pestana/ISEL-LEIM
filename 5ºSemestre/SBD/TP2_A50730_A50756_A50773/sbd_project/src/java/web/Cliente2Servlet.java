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
	

@WebServlet("/Cliente2Servlet")
public class Cliente2Servlet extends HttpServlet {

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
	    
		String parque = request.getParameter("parque");
		String veiculo = request.getParameter("categoria");
		String modelo = request.getParameter("modelo");
		String inicio = request.getParameter("inicio");
		String fim = request.getParameter("fim");
		String unidade = request.getParameter("unidade");
		String codigo = request.getParameter("discountCode");

		double custo = Double.valueOf(request.getParameter("custo"));
		double tarifa = Double.valueOf(request.getParameter("tarifa"));

		Cliente cliente = new Cliente(nif);
        try {
            custo = cliente.custoComDesconto(codigo, custo);
			boolean sucesso = cliente.clienteReservarVeiculo(custo, unidade, inicio, fim, veiculo, modelo, parque, codigo, tarifa);
			if(sucesso) {
				request.setAttribute("mensagem", "A reserva realizou-se com sucesso, valor final: " + custo + " em "+ unidade+ ".");
			}else {
				request.setAttribute("mensagem", "Ocorreu um erro ao processar a reserva.");
			}
        
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        request.getRequestDispatcher("clienteReservar.jsp").forward(request, response);
    }
}


