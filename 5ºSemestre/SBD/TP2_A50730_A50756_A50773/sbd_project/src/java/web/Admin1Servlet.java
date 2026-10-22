package pt.isel.leim.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Administrador;
import pt.isel.leim.sbd.Auxiliar;
	

@WebServlet("/Admin1Servlet")
public class Admin1Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String nif = request.getParameter("NIF");
		String campo = request.getParameter("fieldCliente");
		String novoValor = request.getParameter("newValueCliente");
		
		Administrador admin = new Administrador();
		if(campo.equals("Nome") || campo.equals("Distrito") || campo.equals("Concelho") || campo.equals("Freguesia")
				|| campo.equals("Lingua") || campo.equals("PreferenciaCultural")) {
			novoValor = Auxiliar.normalizaTexto(novoValor);
		}
		
		boolean sucesso = admin.atualizarCliente(campo, novoValor, nif);
		
		if(sucesso) {
			request.setAttribute("mensagemCliente", "Alteração do cliente com o nif " + nif + " realizada com sucesso!");
		}else {
			request.setAttribute("mensagemCliente", "Cliente não existe ou os dados não são válidos.");
		}
        request.getRequestDispatcher("administradorAtualizarClienteCondutor.jsp").forward(request, response);

    }
}


