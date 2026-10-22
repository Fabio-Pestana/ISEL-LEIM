package pt.isel.leim.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Funcionario;
	
@WebServlet("/FuncionarioAutocomplete")
public class FuncionarioAutocomplete extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
    public FuncionarioAutocomplete() {
    	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	
    	Funcionario funcionario = new Funcionario();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String tag = request.getParameter("tag");	// tag a ser usada na lista
		String query = request.getParameter("query");   // String que é comparado com o início
		String nlt = request.getParameter("nlt");	// Quantidade de letras minimo exigido para preencher a lista
		String nit = request.getParameter("nit");	// Quantidade de sugestões devolvidas

		if (query == null)
		    query = "";
		if (tag == null)
		    tag = "option";
		int l = 0;
		if (nlt != null)
		    l = Integer.parseInt(nlt);
		int i = 10;
		if (nit != null)
		    i = Integer.parseInt(nit);

		List<String> nomes = funcionario.funcionarioAutoCompleteCliente();
		List<String> items = funcionario.getData((ArrayList<String>) nomes, query, l, i);
		Iterator<String> iterator = items.iterator();
		while (iterator.hasNext())
		    if (tag.compareToIgnoreCase("br") == 0)
			out.write((String) iterator.next() + "<br>");
		    else
			out.write("<" + tag + ">" + (String) iterator.next() + "</" + tag + ">");
	    }
	
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException {
		doGet(request, response);
	    }

}
