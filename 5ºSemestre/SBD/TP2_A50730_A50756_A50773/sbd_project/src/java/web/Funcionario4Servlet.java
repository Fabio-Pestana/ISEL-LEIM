package pt.isel.leim.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Auxiliar;
import pt.isel.leim.sbd.Stub;
import pt.isel.leim.sbd.Funcionario;
	
@WebServlet("/Funcionario4Servlet")
public class Funcionario4Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String nome = request.getParameter("client-search");
		
		if(nome != null) {
			nome = Auxiliar.normalizaTexto(nome);

			Funcionario funcionario = new Funcionario();
			String[][] resultados = funcionario.funcionarioProcurarCliente(nome);
			if(resultados.length>0 && resultados[0].length>0) {
				String resultadoHtml = Auxiliar.gerarFichaClienteHTML(resultados);
				request.setAttribute("resultados", resultadoHtml);
			}else {
				request.setAttribute("resultados", "Nenhum cliente encontrado");
			}
		}
		
        request.getRequestDispatcher("funcionarioProcura.jsp").forward(request, response);
    }
}
