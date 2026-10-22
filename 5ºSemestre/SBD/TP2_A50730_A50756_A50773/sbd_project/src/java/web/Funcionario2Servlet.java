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
	
@WebServlet("/Funcionario2Servlet")
public class Funcionario2Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String matricula = request.getParameter("license-plate");
		boolean check = Auxiliar.verificarMatricula(new Stub(), matricula);
		
		if(check) {
			Funcionario funcionario = new Funcionario();
			String resultado = funcionario.funcionarioLocalizarVeiculoWeb(matricula);
			request.setAttribute("resultados", resultado);
		}else {
			request.setAttribute("extra", "Matricula inserida não corresponde a nenhum veiculo (" + matricula +").");
		}
		
        request.getRequestDispatcher("funcionarioLocalizarVeiculo.jsp").forward(request, response);
    }
}
