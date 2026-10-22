package pt.isel.leim.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Administrador;
import pt.isel.leim.sbd.Auxiliar;
	

@WebServlet("/Admin3Servlet")
public class Admin3Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String matricula = request.getParameter("Matricula");
		matricula = matricula.toUpperCase();

		String campo = request.getParameter("field");
		String novoValor = request.getParameter("newValue");
		
		Administrador admin = new Administrador();
		if(campo.equals("Cor") || campo.equals("Marca") || campo.equals("Modelo")) {
			novoValor = Auxiliar.normalizaTexto(novoValor);
		}
		
		boolean sucesso = admin.atualizarVeiculo(campo, novoValor, matricula);
		
		if(sucesso) {
			request.setAttribute("mensagem", "Alteração do veículo com a matricula " + matricula +" realizada com sucesso!");
		}else {
			request.setAttribute("mensagem", "Véiculo não existe ou os dados não são válidos.");
		}
        request.getRequestDispatcher("adminAtualizarVeiculos.jsp").forward(request, response);

    }
}




