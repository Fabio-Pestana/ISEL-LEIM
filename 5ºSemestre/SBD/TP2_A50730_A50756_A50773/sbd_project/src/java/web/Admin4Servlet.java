package pt.isel.leim.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Administrador;
import pt.isel.leim.sbd.Auxiliar;
	

@WebServlet("/Admin4Servlet")
public class Admin4Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String nif = request.getParameter("NIF");
		String nome = request.getParameter("Nome");
		String tipo = request.getParameter("Tipo");
		String distrito = request.getParameter("Distrito");
		String concelho = request.getParameter("Concelho");
		String freguesia = request.getParameter("Freguesia");
		String telemovel = request.getParameter("Telemovel");
		String mail = request.getParameter("Mail");
		String lingua = request.getParameter("Lingua");
		String capial = request.getParameter("CapitalSocial");
		String preferencia = request.getParameter("PrefCultural");
		
		if(distrito == null) {
			distrito = "";
		}else if(concelho == null) {
			concelho = "";
		}else if(freguesia == null) {
			freguesia = "";
		}else if(capial == null) {
			capial = "0";
		}else if(preferencia == null) {
			preferencia = "";
		}
		
		if(!distrito.equals("")) {
			distrito = Auxiliar.normalizaTexto(distrito);
			concelho = Auxiliar.normalizaTexto(concelho);
			freguesia = Auxiliar.normalizaTexto(freguesia);
		}else if(!preferencia.equals("")) {
			preferencia = Auxiliar.normalizaTexto(preferencia);
		}
		int capitalSocial = Integer.valueOf(capial);
		nome = Auxiliar.normalizaTexto(nome);
		lingua = Auxiliar.normalizaTexto(lingua);
		
		Administrador admin = new Administrador();
		
		boolean sucesso = admin.criarCliente(nif, tipo, nome, distrito, concelho, freguesia, mail, telemovel, capitalSocial, lingua, preferencia);
		
		if(sucesso) {
			request.setAttribute("mensagemCliente", "Sucesso ao criar o cliente com o nif " + nif + ".");
		}else {
			request.setAttribute("mensagemCliente", "Cliente já existe ou os dados introduzidos não são válidos.");
		}
        request.getRequestDispatcher("adminCriarClienteCondutor.jsp").forward(request, response);

    }
}


