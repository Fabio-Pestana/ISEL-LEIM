import java.io.IOException;
import client.Jogador;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import validate.CheckName;

	
	/**
	 * Jogador jogador = CheckName.getJogador(request, response);
	 * 
	 * Servlet que suporta a login/autenticação dos utilizadores
	 */
@WebServlet("/PassServlet")
public class PassServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String old_pass = request.getParameter("old-password");
		String new_pass = request.getParameter("new-password"); 
		String confirm_pass = request.getParameter("confirm-password"); 
			
		if (old_pass == null || new_pass == null || confirm_pass==null) {
			request.getRequestDispatcher("/changePass.html").forward(request, response);
			return;
		}
		      
		if(new_pass.equals(confirm_pass)) {
			Jogador jogador = CheckName.getJogador(request, response);
			String nome = CheckName.login(request, response);
			try {
				if(jogador.getStub().changePassWeb(nome, old_pass, new_pass)) {		
					request.getRequestDispatcher("/menu.jsp").forward(request,response);
				}else {
					request.getRequestDispatcher("/changePass.html").forward(request, response);
				}
			} catch (Exception e) {
				jogador.fechar();
				request.getRequestDispatcher("/login.html").forward(request, response);
			}
		}else {
			request.getRequestDispatcher("/changePass.html").forward(request, response);
		}
	}
}
