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
	
@WebServlet("/Funcionario6Servlet")
public class Funcionario6Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String matricula = request.getParameter("license-plate");
		String data = request.getParameter("date");
		System.out.println(data);

		matricula = matricula.toUpperCase();
		boolean check = Auxiliar.verificarMatricula(new Stub(), matricula);
		System.out.println(matricula);

		if(check) {
			Funcionario funcionario = new Funcionario();
			String[][] resultados = funcionario.funcionarioProcurarCondutorDeUmVeiculo(matricula, data);
			String[] titulos = {"Nome Condutor", "Carta de Condução", "Data de Nascimento", "Reputação"};
			String resultadoTabela = Auxiliar.generateHtmlTable(resultados, titulos);
			
			if(resultados.length == 0 || resultados[0].length ==0) {
				request.setAttribute("resultados", "Nenhum condutor foi encontrado para o veiculo " + matricula +", na data " + data + ".");
			}else {
				request.setAttribute("mensagem", "O condutor do veiculo " + matricula +", na data " + data + " é:");
				request.setAttribute("resultados", resultadoTabela);
			}
		}else {
			request.setAttribute("resultados", "Não existe veiculo com a matricula: " + matricula + ".");
		}
		
        request.getRequestDispatcher("funcionarioIdentificarCondutor.jsp").forward(request, response);
    }
}

