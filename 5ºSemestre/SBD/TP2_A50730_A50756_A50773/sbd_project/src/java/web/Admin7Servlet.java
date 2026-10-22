package pt.isel.leim.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Administrador;
import pt.isel.leim.sbd.Auxiliar;
	

@WebServlet("/Admin7Servlet")
public class Admin7Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
        String matricula = request.getParameter("Matricula");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        
        Administrador admin = new Administrador();
        String json = admin.exportarDadosParaJson(matricula, startDate, endDate);

        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=\"dados_veiculo.json\"");
        response.getWriter().write(json);
	}
}




