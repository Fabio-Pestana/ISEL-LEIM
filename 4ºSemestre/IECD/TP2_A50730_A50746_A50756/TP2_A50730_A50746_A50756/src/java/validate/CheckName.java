package validate;

import java.io.IOException;

import client.Jogador;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Classe utilitária para validação e recuperação de informações de sessão e contexto.
 */
public class CheckName {
	
	 /**
     * Verifica se o utilizador está autenticado e retorna o nome do utilizador.
     *
     * @param request  O objeto HttpServletRequest.
     * @param response O objeto HttpServletResponse.
     * @return O nome do utilizador se autenticado, caso contrário redireciona para a página de login.
     * @throws ServletException Se ocorrer um erro no processamento da servlet.
     * @throws IOException      Se ocorrer um erro de I/O.
     */
	public static String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(false);
		if(session!=null) {
			String name=(String)session.getAttribute("name");
			if(name!=null)
			  return name;
			else
			  session.invalidate();
		}
		response.sendRedirect("login.html");  
		return null;
	}
	
	/**
     * Recupera o objeto Jogador da sessão do utilizador.
     *
     * @param request  O objeto HttpServletRequest.
     * @param response O objeto HttpServletResponse.
     * @return O objeto Jogador se encontrado, caso contrário redireciona para a página de login.
     * @throws ServletException Se ocorrer um erro no processamento da servlet.
     * @throws IOException      Se ocorrer um erro de I/O.
     */
	public static Jogador getJogador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(false);
		if(session!=null) {
			ServletContext context = request.getServletContext();
			String uid = (String) session.getAttribute("uid");
			Jogador jogador = (Jogador) context.getAttribute(uid);

			if(jogador!=null)
			  return jogador;
			else
			  session.invalidate();
		}
		response.sendRedirect("login.html");  
		return null;
	}
	
	  /**
     * Adiciona tempo de jogo ao contexto do servlet.
     *
     * @param request  O objeto HttpServletRequest.
     * @param response O objeto HttpServletResponse.
     * @param time     O tempo a ser adicionado.
     * @throws ServletException Se ocorrer um erro no processamento da servlet.
     * @throws IOException      Se ocorrer um erro de I/O.
     */
	public static void addTime(HttpServletRequest request, HttpServletResponse response, long time) throws ServletException, IOException {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        ServletContext context = request.getServletContext();
	        String uid = (String) session.getAttribute("uid_Time");
	        long timeOfPlay = (long) context.getAttribute(uid); // Obtenha o tempo armazenado como Long
	        
	        long newTime = timeOfPlay + time;
	        context.setAttribute(uid, newTime);
	    }
	}
	
	/**
     * Recupera o tempo de jogo do contexto do servlet.
     *
     * @param request  O objeto HttpServletRequest.
     * @param response O objeto HttpServletResponse.
     * @return O tempo de jogo, ou 0L se não houver tempo armazenado ou se a sessão for inválida.
     * @throws ServletException Se ocorrer um erro no processamento da servlet.
     * @throws IOException      Se ocorrer um erro de I/O.
     */
	public static long getTime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        ServletContext context = request.getServletContext();
	        String uid = (String) session.getAttribute("uid_Time");
	        long timeOfPlay = (long) context.getAttribute(uid); // Obtenha o tempo armazenado como Long
	        if (timeOfPlay == 0L) {
	        	session.invalidate();
	        	response.sendRedirect("login.html");  
	    		return 0L;
	        }
	        return timeOfPlay;
	    }
    	response.sendRedirect("login.html");  
		return 0L;
	}
	
	/**
     * Recupera a cor preferida do utilizador da sessão.
     *
     * @param request  O objeto HttpServletRequest.
     * @param response O objeto HttpServletResponse.
     * @return A cor preferida do utilizador no formato hexadecimal, ou redireciona para a página de login se a sessão for inválida.
     * @throws ServletException Se ocorrer um erro no processamento da servlet.
     * @throws IOException      Se ocorrer um erro de I/O.
     */
	public static String getColor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(false);
		if(session!=null) {
			String color=(String)session.getAttribute("color");
			if(color!=null) {
				color = "#" + color;
				return color;
			}else
			  session.invalidate();
		}
		response.sendRedirect("login.html");  
		return null;
	}
}
