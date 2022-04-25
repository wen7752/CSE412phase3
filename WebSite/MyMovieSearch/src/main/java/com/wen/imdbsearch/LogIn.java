package com.wen.imdbsearch;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/userlogin")
public class LogIn  extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection connection;
	private CallableStatement stmt;
	private Statement stmt2;
		
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
		
		String email = request.getParameter("emaillogin");
		String password = request.getParameter("passwordlogin");		
		String currentPage = request.getParameter("currentWebPageLogin");
		String pquery = request.getParameter("pquery");
		
		String sqlQuery="CALL check_login(?,?,?,?)";
		
		
		
		try {
			showCount(getReq(), getRes(), sqlQuery,currentPage,email,password,pquery);
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

	public void showCount(HttpServletRequest request, HttpServletResponse response, String sqlQuery,String currentPage,String email,String password,String pquery) 
			throws ServletException, IOException, SQLException {
		
		
		stmt=connection.prepareCall(sqlQuery);
		stmt.setString(1, email.toLowerCase());
		stmt.setString(2, password);		
		stmt.registerOutParameter(3, Types.INTEGER); //ouserid
		stmt.setNull(3, Types.INTEGER);
		stmt.registerOutParameter(4, Types.VARCHAR); //oemail
		stmt.setNull(4, Types.VARCHAR);
		
		stmt.execute();
		
		int ouserid = stmt.getInt(3);
		String oemail = stmt.getString(4);
		
		String msg="";
		
		if (ouserid==0)
		{
			msg = "user does not exists; Try Again!!";
		}		
		else if (ouserid == -1)
		{
			msg = "ERROR: "+oemail;
		}
		else {
			session.setAttribute("userid", ouserid);
			session.setAttribute("email", oemail);
		}
		
		
		stmt.close();
		
		
	
		
		ArrayList<PlayList> playlist_result = new ArrayList<PlayList>();
		
		if(session.getAttribute("userid")!=null) {
			String userid = String.valueOf(session.getAttribute("userid"));
			playlist_result = getMyPlayList(userid);
		}
		
		request.setAttribute("alertmsg", msg);
		request.setAttribute("oemail", oemail);
		request.setAttribute("ouserid", ouserid);
		request.setAttribute("plresult", playlist_result);
		request.setAttribute("plresultcount", playlist_result.size());
		
		
		if(currentPage.equals("home"))
		{request.getRequestDispatcher("home.jsp").forward(request, response);}
		else
		{
		
			request.getRequestDispatcher("movielist.jsp").forward(request, response);
		System.err.println(pquery);
		}
	}

	
	public ArrayList<PlayList> getMyPlayList(String userid) throws ServletException, IOException{
		ResultSet rs=null;
		ArrayList<PlayList> result=null;
		try {
			
			stmt2=connection.createStatement();
			
			rs = stmt2.executeQuery("select playlistid,playlist_name,userid from playlist where userid="+userid);
		
			 result = new ArrayList<PlayList>();
			
			while(rs.next())
			{
				PlayList playlist = new PlayList();
				playlist.setPlaylistid(rs.getInt("playlistid"));								
				playlist.setPlaylist_name(rs.getString("playlist_name"));
				playlist.setUserid(rs.getInt("userid"));	
				
				result.add(playlist);
			}//end while
			
			rs.close();
			stmt2.close();
			//connection.close();
			//return result;
		//	showResult(getReq(), getRes(), result);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return result;
		
	}//end getMyPlayList
	
}//end logIn
