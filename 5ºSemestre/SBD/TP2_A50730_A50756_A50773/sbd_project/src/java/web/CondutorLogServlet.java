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
	
@WebServlet("/CondutorLogServlet")
public class CondutorLogServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String nif = request.getParameter("NIF_search");
		String cc = request.getParameter("CC_search");

		boolean check = Auxiliar.verificaCartaAssociada(new Stub(), nif, cc);
				
		if(check){
			// Guardar NIF e CC na sessão
            HttpSession session = request.getSession();
            session.setAttribute("nif", nif);
            session.setAttribute("cc", cc);
	        request.getRequestDispatcher("Condutor.html").forward(request, response);
		}else {
			request.setAttribute("resultados", "NIf do cliente " + nif + " não está associado à carta de condução inserida (" + cc + ").");
	        request.getRequestDispatcher("condutorLogin.jsp").forward(request, response);
		}
    }
}