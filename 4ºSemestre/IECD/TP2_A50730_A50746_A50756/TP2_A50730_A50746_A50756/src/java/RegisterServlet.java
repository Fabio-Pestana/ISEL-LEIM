import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Scanner;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import client.Jogador;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import user.User;
import util.MyImage;
import util.XMLDoc;

/**
 * Servlet que suporta a login/autenticação dos utilizadores
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = java.util.UUID.randomUUID().toString();
		String uidTime = java.util.UUID.randomUUID().toString();
		String nome = request.getParameter("username");
		String senha = request.getParameter("password");
		String birthdateStr = request.getParameter("birthdate");
        String nationality = request.getParameter("nationality");
        String photoBase64 = request.getParameter("pictureBase64");
        long time = 0L;
        
        LocalDate birthdateDate = null;
        try {
        	birthdateDate = LocalDate.parse(birthdateStr);
		} catch (Exception e) {}
		
	      
		if(nome!=null && senha!=null) {
			try {
		        User user = new User();
		        
		        boolean name = user.setUsernameNoXML(nome);
		        boolean pass = user.setPassword(senha);
		        boolean date = user.setBirthdate(birthdateDate);
		        user.setNationalityNoVerification(nationality);
		        boolean foto = user.setPhotography(photoBase64);
		        
		        if(name && pass && date && foto) {
		        	Jogador jogador = new Jogador();
					if(jogador.getStub().newUserWeb(user)) {
						String color = jogador.getStub().getFavColorWeb(nome);
						HttpSession session = request.getSession();
						ServletContext context = request.getServletContext();
						context.setAttribute(uidTime, time);
						context.setAttribute(uid, jogador);
						session.setAttribute("uid", uid);
						session.setAttribute("uid_Time", uidTime);
						session.setAttribute("name",nome);
						session.setAttribute("simbolo", null);
						session.setAttribute("color",color);
						request.getRequestDispatcher("/menu.jsp").forward(request,response);
					}else {
						jogador.fechar();
						request.getRequestDispatcher("/register.html").forward(request, response);
					}
		        }else{
		        	request.getRequestDispatcher("/register.html").forward(request, response);
					return;
		        }
			} catch (Exception e) {}
		}else {
			request.getRequestDispatcher("/login.html").forward(request, response);
		}
	}
}