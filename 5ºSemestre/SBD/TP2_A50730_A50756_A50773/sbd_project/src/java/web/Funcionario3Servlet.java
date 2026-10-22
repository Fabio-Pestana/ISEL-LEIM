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
	
@WebServlet("/Funcionario3Servlet")
public class Funcionario3Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String matricula = request.getParameter("license-plate");
		String dataHoraIntervencao = request.getParameter("datetime");
		double custo = Double.parseDouble(request.getParameter("cost"));
		String intervencao = request.getParameter("type");

		boolean check = Auxiliar.verificarMatricula(new Stub(), matricula);
				
		if(check) {
			Funcionario funcionario = new Funcionario();
			boolean resultado = funcionario.funcionarioCriarIntervencao(dataHoraIntervencao, custo, intervencao, matricula);
			if(resultado) {
				request.setAttribute("resultados", "Intervenção criada com sucesso para o veiculo: " + matricula);
			}else {
				request.setAttribute("resultados", "Erro ao criar intervenção para o veiculo: " + matricula);
			}
		}else {
			request.setAttribute("resultados", "Matricula inserida não corresponde a nenhum veiculo (" + matricula +").");
		}
		
        request.getRequestDispatcher("funcionarioIntervencoes.jsp").forward(request, response);
    }
}
