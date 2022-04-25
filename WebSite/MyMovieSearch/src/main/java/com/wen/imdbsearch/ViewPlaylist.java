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

@WebServlet("/viewplaylist")
public class ViewPlaylist extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Connection connection;
	private Statement stmt;
	private Statement stmt_director;
	private Statement stmt_cast;
	private Statement stmt_count;
	private Statement stmt_playlistname;
	
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Cache-Control","no-cache"); 
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader ("Expires", -1);
		
		session = request.getSession();
		
		setReq(request);
		setRes(response);
		
		String selectedPL = request.getParameter("userplaylists");
		
		String userid = String.valueOf(session.getAttribute("userid"));
		
		String sqlQuery=null;
		String sqlCountQuery = null;
		String sqlplname = null;
		
		String title = null;
		String pid = null;
		
				
		
		if(selectedPL==null )
		{
		sqlQuery = "SELECT row_number() over(order by m.movieid) sno, m.movieid,m.cover,m.primary_title,m.original_title,m.release_year,m.rating,m.runtime_duration,m.genre FROM movie m WHERE m.movieid in (select movieid from movie_playlist where playlistid in (select min(playlistid) from playlist where userid="+userid+")) order by m.movieid";
		
		sqlCountQuery = "SELECT count(m.movieid) as counts FROM movie m WHERE m.movieid in (select movieid from movie_playlist where playlistid in (select min(playlistid) from playlist where userid="+userid+"))";
		}
		else {
			sqlQuery = "SELECT row_number() over(order by m.movieid) sno, m.movieid,m.cover,m.primary_title,m.original_title,m.release_year,m.rating,m.runtime_duration,m.genre FROM movie m WHERE m.movieid in (select movieid from movie_playlist where playlistid = "+selectedPL+") order by m.movieid";			
			sqlCountQuery = "SELECT count(m.movieid) as counts FROM movie m WHERE m.movieid in (select movieid from movie_playlist where playlistid = "+selectedPL+")";
			sqlplname = "select playlist_name,playlistid from playlist where playlistid="+selectedPL;
			
			String[] p = getPlaylistName(sqlplname); 
			title = p[0];
			pid = 	p[1];
		}
			
		int RowCounts = getCountRows(sqlCountQuery);
		
		showCount(getReq(), getRes(), RowCounts, sqlQuery,title,pid);
		
		
		//runSelectQuery(sqlQuery);
	}//end doPost

	public void showCount(HttpServletRequest request, HttpServletResponse response, int counts,String sqlQuery,String title,String pid) 
			throws ServletException, IOException {
		int currentPage = 0;
		int totalPage = 0;
		
		if(counts>10) {
			totalPage = counts/10;
			if(request.getParameter("currentPage")!=null)
				currentPage = Integer.valueOf(request.getParameter("currentPage"))+1;
			else
				currentPage = 1;
		}
		else
		{
			currentPage=1;
			totalPage = 1;
		}
		
		ArrayList<MovieList> result = runSelectQuery(sqlQuery);
		
		
		Collections.sort(result, new SortMovieList());
		
		
		
		ArrayList<PlayList> playlist_result = new ArrayList<PlayList>();
		
		if(session.getAttribute("userid")!=null) {
			String userid = String.valueOf(session.getAttribute("userid"));
			playlist_result = getMyPlayList(userid);
		}
		
		if(title==null) {
			request.setAttribute("playlistname", (playlist_result.get(0)).getPlaylist_name());
			request.setAttribute("playlistid", (playlist_result.get(0)).getPlaylistid());
		}else {
			request.setAttribute("playlistname", title);
			request.setAttribute("playlistid", pid);
		}
		
		
		request.setAttribute("rowcount", counts);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPage", totalPage);		
		request.setAttribute("result", result);
		request.setAttribute("plresult", playlist_result);
		request.setAttribute("plresultcount", playlist_result.size());
		session.setAttribute("pquery", sqlQuery);
		
		request.getRequestDispatcher("viewplaylist.jsp").forward(request, response);
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
	
	public int getCountRows(String query)
	{
		System.err.println(query);
		int count=0;
		try {
			stmt_count=connection.createStatement();
			ResultSet rs_count = stmt_count.executeQuery(query);
			
				while(rs_count.next())
				{
				count = rs_count.getInt("counts");
				}
			
			} catch (SQLException e) 
				{
					e.printStackTrace();
				}
		return count;
	}//end getCountRows

	public String[] getPlaylistName(String query)
	{
		String plname=null;
		String plid = null;
		
		try {
			stmt_playlistname=connection.createStatement();
			ResultSet rs_name = stmt_playlistname.executeQuery(query);
			
				while(rs_name.next())
				{
					plname = rs_name.getString("playlist_name");
					plid  = rs_name.getString("playlistid");
				}
			
			} catch (SQLException e) 
				{
					e.printStackTrace();
				}
		return new String[]{plname,plid};
	}//end getPlaylistName
	
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
	
	
	public ArrayList<MovieList> runSelectQuery(String query) throws ServletException, IOException{
		ResultSet rs=null;
		ArrayList<MovieList> result=null;
		try {
			
			stmt=connection.createStatement();
			System.err.println(query); 
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
	
}//end class
