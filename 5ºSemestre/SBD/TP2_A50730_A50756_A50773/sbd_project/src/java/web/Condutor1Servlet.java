package pt.isel.leim.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pt.isel.leim.sbd.Condutor;
	

@WebServlet("/Condutor1Servlet")
public class Condutor1Servlet extends HttpServlet {

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
		String matricula = request.getParameter("matricula");
		System.out.println(nif);
		
		Condutor condutor = new Condutor();
		boolean sucesso = condutor.levantarVeiculo(nif, matricula);
		if(sucesso) {
			request.setAttribute("mensagem", "Levantou o veículo " + matricula + " com sucesso!" );
		}else {
			request.setAttribute("mensagem", "O veículo " + matricula + " já foi levantado!");
		}

        request.getRequestDispatcher("condutorConsultarReservas.jsp").forward(request, response);
    }
}

