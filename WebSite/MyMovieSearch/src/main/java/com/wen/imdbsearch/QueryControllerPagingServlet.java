package com.wen.imdbsearch;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/querycontrollerPaging")
public class QueryControllerPagingServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	Connection connection;
	private Statement stmt;
	private Statement stmt_director;
	private Statement stmt_cast;
	@SuppressWarnings("unused")
	private Statement stmt_count;
	
	HttpServletRequest req;
	HttpServletResponse res;
	private String sqlQueryOriginal;
	HttpSession session ;
	
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
		
		
		
		setReq(request);
		setRes(response);
		
		session = request.getSession();
		
		//get Hidden Field
		int movieid_next = Integer.valueOf(request.getParameter("movieid_next"));
		int currentPage = Integer.valueOf(request.getParameter("currentPage"))+1;
		
		sqlQueryOriginal = request.getParameter("pquery");
		
		String sqlQuery=null;
		
		sqlQuery = request.getParameter("pquery").replace("a limit 10", "a where a.sno>"+movieid_next+" limit 10");
		
		
		String rowcount = request.getParameter("rowcount");
		String totalPage = request.getParameter("totalPage");
		
		showCount(getReq(), getRes(), rowcount,currentPage,totalPage, sqlQuery);
		
		//runSelectQuery(sqlQuery);
	}//end doPost
	
	
	public ArrayList<MovieList> runSelectQuery(String query) throws ServletException, IOException{
		ResultSet rs=null;
		ArrayList<MovieList> result=null;
		try {
			
			stmt=connection.createStatement();
			//System.err.println(query); 
			rs = stmt.executeQuery(query);
		
			 result = new ArrayList<MovieList>();
			
			while(rs.next())
			{
				MovieList movieList = new MovieList();
				movieList.setSno(rs.getInt("sno"));					
				movieList.setMovieid(rs.getString("movieid"));
				movieList.setCover(rs.getString("cover"));
				movieList.setPrimary_title(rs.getString("primary_title"));
				movieList.setOriginal_title(rs.getString("original_title"));
				movieList.setRelease_year(rs.getString("release_year"));				
				movieList.setRating(rs.getString("rating"));
				movieList.setRuntime_duration(rs.getString("runtime_duration"));
				movieList.setGenre(rs.getString("genre"));
				
				String directorName = getDirectorNames(rs.getString("movieid"));
				movieList.setDirector_name(directorName);
				
				String casts = getCast(rs.getString("movieid"));
				movieList.setCasts(casts);
				
				result.add(movieList);
			}//end while
			
			rs.close();
			stmt.close();
			//connection.close();
			//return result;
		//	showResult(getReq(), getRes(), result);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return result;
		
	}//end runSelectQuery

	
	public String getDirectorNames(String movieid) throws SQLException
	{ 	
		String directorNames="";
		ArrayList<String> directors = new ArrayList<String>();
		ResultSet rs_director=null;
		String query_director = "SELECT p.name FROM movie_crew_cast mc join person p on (p.personid = mc.personid) WHERE mc.movieid = '"+movieid+"' and mc.professionid = 15";
		
		stmt_director=connection.createStatement();
		rs_director = stmt_director.executeQuery(query_director);
		
		while(rs_director.next())
		{
			directors.add(rs_director.getString("name"));
		}
		
		rs_director.close();
		stmt_director.close();
	
		for(int i=0;i<directors.size();i++)
		{
			if(directors.get(i).length()>0)
				directorNames +=directors.get(i);
			
			if((i+1)<directors.size())
				directorNames +=", ";
		}
		
				
		if(directorNames.length()==0)
			directorNames="0";
		
		return directorNames;
	}//end director
	
	public String getCast(String movieid) throws SQLException
	{ 	
		String persons="";
		ArrayList<String> casts = new ArrayList<String>();
		ResultSet rs_casts=null;
		String query_director = "SELECT p.name FROM movie_crew_cast mc join person p on (p.personid = mc.personid) WHERE mc.movieid = '"+movieid+"' and mc.professionid  in (1,2)";
		
		stmt_cast=connection.createStatement();
		rs_casts = stmt_cast.executeQuery(query_director);
		
		while(rs_casts.next())
		{
			casts.add(rs_casts.getString("name"));
		}
		
		rs_casts.close();
		stmt_cast.close();
	
		for(int i=0;i<casts.size();i++)
		{
			if(casts.get(i).length()>0)
				persons +=casts.get(i);
			
			if((i+1)<casts.size())
				persons +=", ";
		}
		
			
		if(persons.length()==0)
			persons="0";
		
		return persons;
	}//end cast
	
	
	public void showCount(HttpServletRequest request, HttpServletResponse response, String counts, int currentPage,String totalPage,String sqlQuery) 
			throws ServletException, IOException {
		
		
				System.err.println(sqlQuery);
		ArrayList<MovieList> result = runSelectQuery(sqlQuery);
		
		@SuppressWarnings("unused")
		int arraySize = result.size();
		int maxIndex = result.size()-1;
		int movieid_next = (((MovieList)result.get(maxIndex)).getSno());
		
		Collections.sort(result, new SortMovieList());
		
		ArrayList<PlayList> playlist_result = new ArrayList<PlayList>();
		
		if(session.getAttribute("userid")!=null) {
			String userid = String.valueOf(session.getAttribute("userid"));
			playlist_result = getMyPlayList(userid);
		}
		
		request.setAttribute("rowcount", counts);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("movieid_next", movieid_next);
		request.setAttribute("result", result);
		request.setAttribute("plresult", playlist_result);
		request.setAttribute("plresultcount", playlist_result.size());
		request.setAttribute("pquery", sqlQueryOriginal);
		
		
		
		request.getRequestDispatcher("movielist.jsp").forward(request, response);
	}
	
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

	public ArrayList<PlayList> getMyPlayList(String userid) throws ServletException, IOException{
		ResultSet rs=null;
		ArrayList<PlayList> result=null;
		try {
			
			stmt=connection.createStatement();
			
			rs = stmt.executeQuery("select playlistid,playlist_name,userid from playlist where userid="+userid);
		
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
			stmt.close();
			//connection.close();
			//return result;
		//	showResult(getReq(), getRes(), result);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return result;
		
	}//end getMyPlayList
	
}//end class
