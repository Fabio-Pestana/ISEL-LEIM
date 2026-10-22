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
	
@WebServlet("/Funcionario5Servlet")
public class Funcionario5Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		double custoFinal = Double.valueOf(request.getParameter("custoFinal"));
		int avaliacao = Integer.valueOf(request.getParameter("avaliacao"));
		double kmPercorridos = Double.valueOf(request.getParameter("kmPercorridos"));
		String matricula = request.getParameter("matriculaAluguer");
		String nif = request.getParameter("nif");
		String aluguer = request.getParameter("aluguer");
		if(custoFinal != 0 && avaliacao != 0 && kmPercorridos != 0) {
			Funcionario funcionario = new Funcionario();
			boolean sucesso = funcionario.funcionarioFinalizarAluguer(aluguer, custoFinal, avaliacao, kmPercorridos, nif, matricula);
			if(sucesso) {
				request.setAttribute("mensagem", "Aluguer " + aluguer + " finalizado com sucesso.");
			}else {
				request.setAttribute("mensagem", "Erro ao finalizar o aluguer: " + aluguer + ".");
			}
		}
		
        request.getRequestDispatcher("funcionarioEscolherAluguer.jsp").forward(request, response);
    }
}
