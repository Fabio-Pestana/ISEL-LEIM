import java.io.IOException;

import client.Jogador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import validate.CheckName;

/**
 * Servlet que suporta a login/autenticação dos utilizadores
 */
@WebServlet("/PictureServlet")
public class PictureServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nome = CheckName.login(request, response);
        String photoBase64 = request.getParameter("pictureBase64");
	      
		if(nome!=null) {
			Jogador jogador = CheckName.getJogador(request, response);
			try {
					if(jogador.getStub().changePicWeb(nome, photoBase64)) {			
						request.getRequestDispatcher("/changePic.jsp").forward(request,response);
					}else {
						jogador.fechar();
						request.getRequestDispatcher("/login.html").forward(request, response);
					}
			} catch (Exception e) {}
		}else {
			request.getRequestDispatcher("/login.html").forward(request, response);
		}
	}
}