package pt.isel.leim.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Administrador;
import pt.isel.leim.sbd.Auxiliar;
	

@WebServlet("/Admin5Servlet")
public class Admin5Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String nif = request.getParameter("NIF");
		String nome = request.getParameter("NomeCondutor");
		String dataNascimento = request.getParameter("DataNascimentoCondutor");
		String cc = request.getParameter("CartaConducao");
		String emissaoCarta = request.getParameter("DataEmissaoCarta");
		String validadeCarta = request.getParameter("ValidadeCarta");
		
		nome = Auxiliar.normalizaTexto(nome);
		
		Administrador admin = new Administrador();
		
		boolean sucesso = admin.criarCondutorWeb(nif, cc, nome, emissaoCarta, dataNascimento, validadeCarta);
		
		if(sucesso) {
			request.setAttribute("mensagemCondutor", "Sucesso ao criar o condutor com o nif " + nif + "e carta de condução" + cc + ".");
		}else {
			request.setAttribute("mensagemCondutor", "Condutor já existe ou os dados introduzidos não são válidos.");
		}
        request.getRequestDispatcher("adminCriarClienteCondutor.jsp").forward(request, response);

    }
}


