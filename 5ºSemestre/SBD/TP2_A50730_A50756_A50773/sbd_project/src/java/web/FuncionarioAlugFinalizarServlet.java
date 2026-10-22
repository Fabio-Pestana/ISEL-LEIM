package pt.isel.leim.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pt.isel.leim.sbd.Auxiliar;
import pt.isel.leim.sbd.Funcionario;
	

@WebServlet("/FuncionarioAlugFinalizarServlet")
public class FuncionarioAlugFinalizarServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

	    try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String aluguer = request.getParameter("aluguer");
		
		Funcionario funcionario = new Funcionario();
		Object[] objetos =  funcionario.verDetalhesAluguerCondutor(aluguer);
		
		String matriculaAluguer = (String) objetos[0];
		String nif = (String) objetos[1];
		float custoPrevisto = (float) objetos[2];
		float reputacao = (float) objetos[4];
		System.out.println(custoPrevisto);
		
		request.setAttribute("aluguer", aluguer);
		request.setAttribute("matricula", matriculaAluguer);
		request.setAttribute("nif", nif);
		request.setAttribute("custoPrevisto", custoPrevisto);
		request.setAttribute("reputacao", reputacao);
		String opcoes = gerarFichaAluguerHTML(aluguer, objetos);

		request.setAttribute("opcoes", opcoes);

        request.getRequestDispatcher("funcionarioFinalizarAluguer.jsp").forward(request, response);
    }
	
	public String gerarFichaAluguerHTML(String aluguerId, Object[] resultados) {
	    
	    if (resultados == null || resultados.length == 0) {
	        return "<p>Aluguer não encontrado.</p>";
	    }	   
	    
	    String matricula = (String) resultados[0]; 
	    String nif = (String) resultados[1];       
	    float custoPrevisto = (float) resultados[2]; 
	    String nome = (String) resultados[3];      
	    float reputacao = (float) resultados[4];   

	    StringBuilder html = new StringBuilder();
	    html.append("<h3>Informações do Aluguer ").append(aluguerId).append("</h3>\n")
	        .append("<p><strong>NIF do cliente:</strong> ").append(nif).append("</p>\n")
	        .append("<p><strong>Nome:</strong> ").append(nome).append("</p>\n")
	        .append("<p><strong>Matricula do veiculo:</strong> ").append(matricula).append("</p>\n")
	        .append("<p><strong>Reputação do Condutor:</strong> ").append(reputacao).append("</p>\n")
	        .append("<p><strong>Custo previsto do aluguer:</strong> ").append(custoPrevisto).append("</p>\n");

	    return html.toString();
	}
}


