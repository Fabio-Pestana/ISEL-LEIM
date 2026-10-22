package pt.isel.leim.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pt.isel.leim.sbd.Auxiliar;
import pt.isel.leim.sbd.Stub;
	
@WebServlet("/ClienteLogServlet")
public class ClienteLogServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String nif = request.getParameter("NIF_search");

		boolean check = Auxiliar.verificarNIF(new Stub(), nif);
				
		if(check){
			// Guardar NIF na sessão
            HttpSession session = request.getSession();
            session.setAttribute("nif", nif);
	        request.getRequestDispatcher("Cliente.html").forward(request, response);
		}else {
			request.setAttribute("resultados", "Cliente com nif " + nif + " não existe.");
	        request.getRequestDispatcher("clienteLogin.jsp").forward(request, response);
		}
    }
}
