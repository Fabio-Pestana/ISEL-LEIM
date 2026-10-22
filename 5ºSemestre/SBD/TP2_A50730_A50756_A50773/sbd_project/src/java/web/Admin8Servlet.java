package pt.isel.leim.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Administrador;
	

@WebServlet("/Admin8Servlet")
public class Admin8Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	    String jsonData = request.getParameter("jsonData");

		Administrador admin = new Administrador();
		
		boolean sucesso = admin.processarJson(jsonData);
		
		if(sucesso) {
			request.setAttribute("mensagem", "Sucesso ao processar o json.");
		}else {
			request.setAttribute("mensagem", "Erro ao importar ficheiro, os dados no json correspondem a um veiculo já existente ou a formato está incorreto.");
		}
        request.getRequestDispatcher("adminImportar.jsp").forward(request, response);
    }
}




