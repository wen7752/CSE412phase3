package com.wen.imdbsearch;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/removemoviepl")
public class RemovePlaylist extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Connection connection;
	HttpSession session =null;
	HttpServletRequest req;
	HttpServletResponse res;
	private Statement stmt;	
	
	@Override
	public void init() throws ServletException {	
		super.init();
		
		
		try {
			Singleton singleton = Singleton.getInstance();
			
			boolean r = openDBConnection(singleton.jdbcDriver, singleton.connURL, singleton.username, singleton.pwd);
			System.out.println("DataConnectionOpen => "+r);
			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//end init()
	
	public boolean openDBConnection(String jdbcDriver, String dbURL,String username, String password) 
			throws ClassNotFoundException, SQLException
	{
		Class.forName(jdbcDriver);
		
		connection = DriverManager.getConnection(dbURL,username,password);
		
		if (connection!=null) {
			
			return true;
		}else {
			return false;
		}
		
		
	}//end openDBConnection
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Cache-Control","no-cache"); 
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader ("Expires", -1);
		
		session = request.getSession();
		
		setReq(request);
		setRes(response);
		
		
		String pid = request.getParameter("playlistid");
		
		
		String sqlQuery=null;
	
		
		String movieid = request.getParameter("movieid");
				
			
		
		sqlQuery = "delete from movie_playlist where movieid='"+movieid+"' and playlistid="+pid;
				
		runSelectQuery(sqlQuery,pid);
		
	}//end doPost
	
	public void runSelectQuery(String query,String pid) throws ServletException, IOException{
		
		try {
			
			
			stmt=connection.createStatement();
					
			@SuppressWarnings("unused")
			int i = stmt.executeUpdate(query);
			stmt.close();
			
			System.err.println(query);
			
			System.err.println(i);
			//getReq().getRequestDispatcher("ViewPlaylist").forward(getReq(), getRes());
			if(i>0) {
			RequestDispatcher rd = getReq().getRequestDispatcher("/viewplaylist?userplaylists="+pid);
			rd.forward(getReq(), getRes());
			}
			
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}//end runSelectQuery
	
	public HttpServletRequest getReq() {
		return req;
	}
	public void setReq(HttpServletRequest req) {
		this.req = req;
	}
	public HttpServletResponse getRes() {
		return res;
	}
	public void setRes(HttpServletResponse res) {
		this.res = res;
	}
	
	
}//end class
