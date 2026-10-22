import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet que devolve os dados limitados pelos parametros indicados
 */
@WebServlet("/GetHTML")
public class GetHTML extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public GetHTML() {
    	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// Obtendo o caminho absoluto do arquivo users.xml
        String filePath = getServletContext().getRealPath("/users.xml");	
        
        //System.out.println("PATH: " + filePath);
    
		db.DummyDB db = new db.DummyDB("users", filePath); // acesso a esta base de dados
	
		// Set the response message's MIME type.
		response.setContentType("text/html;charset=UTF-8");
		// Allocate a output writer to write the response message into the network
		// socket.
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
		List<String> items = db.getData(query, l, i);
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
