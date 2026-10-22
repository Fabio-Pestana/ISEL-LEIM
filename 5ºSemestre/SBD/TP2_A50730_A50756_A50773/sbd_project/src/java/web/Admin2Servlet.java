package pt.isel.leim.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Administrador;
import pt.isel.leim.sbd.Auxiliar;
	

@WebServlet("/Admin2Servlet")
public class Admin2Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String nif = request.getParameter("NIF");
		String cc = request.getParameter("CartaConducao");

		String campo = request.getParameter("fieldCondutor");
		String novoValor = request.getParameter("newValueCondutor");
		
		Administrador admin = new Administrador();
		if(campo.equals("Nome")) {
			novoValor = Auxiliar.normalizaTexto(novoValor);
		}
		
		boolean sucesso = admin.atualizarCondutor(campo, novoValor, nif, cc);
		
		if(sucesso) {
			request.setAttribute("mensagemCondutor", "Alteração do condutor com o nif " + nif + " e número da carta de condução "+ cc +" realizada com sucesso!");
		}else {
			request.setAttribute("mensagemCondutor", "Condutor não existe ou os dados não são válidos.");
		}
        request.getRequestDispatcher("administradorAtualizarClienteCondutor.jsp").forward(request, response);

    }
}



