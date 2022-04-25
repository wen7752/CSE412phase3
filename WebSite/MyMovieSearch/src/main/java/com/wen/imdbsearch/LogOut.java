package com.wen.imdbsearch;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/userlogout")
public class LogOut  extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			
	HttpSession session;
	
	@Override
	public void init() throws ServletException {	
		super.init();
	

	}//end init()
		
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	IOException {
		
		response.setHeader("Cache-Control","no-cache"); 
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader ("Expires", -1);
		
	
		session = request.getSession();
		
		session.invalidate();
		
		request.getRequestDispatcher("home.jsp").forward(request, response);
		
		
	}//end doPost



	
}//end logIn
