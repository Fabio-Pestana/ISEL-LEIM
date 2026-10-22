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
	
@WebServlet("/Funcionario7Servlet")
public class Funcionario7Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String matricula = request.getParameter("license-plate");
		matricula = matricula.toUpperCase();
		String morada = request.getParameter("parque");
		int piso = Integer.parseInt(request.getParameter("piso"));
		int fila = Integer.parseInt(request.getParameter("fila"));
		int numero = Integer.parseInt(request.getParameter("numero"));

		boolean check = Auxiliar.verificarMatricula(new Stub(), matricula);
		
		
		if(check) {
			Funcionario funcionario = new Funcionario();
			boolean resultado = funcionario. atualizarLugarEstacionamento(matricula, morada, piso, fila, numero);
			if(resultado) {
				request.setAttribute("mensagem", "Veiculo " + matricula + " atualizado com sucesso no parque: "
						+ morada + ", no piso: " + piso
						+ ", na fila: " + fila
						+ ", no número: " + numero + ".");
			}else {
				request.setAttribute("mensagem", "Erro ao atualizar o veiculo " + matricula + " no parque: "
						+ morada + ", no piso: " + piso
						+ ", na fila: " + fila
						+ ", no número: " + numero + ", o lugar marcado está indisponivel.");
			}
		}else {
			request.setAttribute("mensagem", "Matricula inserida não corresponde a nenhum veiculo (" + matricula +").");
		}
		
        request.getRequestDispatcher("funcionarioAlterarLugarVeiculo.jsp").forward(request, response);
    }
}

