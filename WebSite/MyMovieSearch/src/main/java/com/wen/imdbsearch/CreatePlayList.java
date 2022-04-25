package com.wen.imdbsearch;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/creatplaylist")
public class CreatePlayList extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Connection connection;
	private CallableStatement stmt;
		
	HttpServletRequest req;
	HttpServletResponse res;
	HttpSession session;
	
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	IOException {
		
		response.setHeader("Cache-Control","no-cache"); 
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader ("Expires", -1);
		
		setReq(request);
		setRes(response);
		
		
		
		session = request.getSession();
		
		String namepl = request.getParameter("namepl");
		int userid = (int)session.getAttribute("userid");
		String currentPage = request.getParameter("currentWebPageLogin");
				
		String sqlQuery="CALL insert_palylist(?,?,?)";
		
		System.err.println("IN");
		
		try {
			showCount(getReq(), getRes(), sqlQuery,currentPage,namepl,userid);
		} catch (ServletException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}//end doPost

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

	public void showCount(HttpServletRequest request, HttpServletResponse response, String sqlQuery,String currentPage,String namepl,int userid) 
			throws ServletException, IOException, SQLException {
		
		
		stmt=connection.prepareCall(sqlQuery);
		stmt.setString(1, namepl.toUpperCase());
		stmt.setInt(2, userid);		
		stmt.registerOutParameter(3, Types.VARCHAR);
	
		stmt.execute();
		
		String result = stmt.getString(3);
		
		System.err.println(result);
		System.err.println(userid+" >> "+namepl.toUpperCase());
		boolean checknum = isNumeric(result);
		
		String msg="";
		
		if(checknum) 
		{
			if (Integer.valueOf(result)==1)
			{
				msg = "New Playlist has been created successfully; you may need to add movies in it.";
				
			}
			else {
				msg = "ERROR: Playlist Name already exists; try again";
			}
			
			
		}//end checknum
		else 
		{
			msg = "ERROR: "+result;
		}
				
		
		stmt.close();
		
		
		request.setAttribute("alertmsg", msg);
		
		if(currentPage.equals("home"))
		request.getRequestDispatcher("home.jsp").forward(request, response);
		else
		request.getRequestDispatcher("movielist.jsp").forward(request, response);
	}

public boolean isNumeric(String str) {
		
		if(str==null) 
			return false;
		
		try 
		{
			@SuppressWarnings("unused")
			int n = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}//end isNumeric
}//end creatplaylist

