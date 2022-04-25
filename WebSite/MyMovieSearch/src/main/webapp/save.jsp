<%@ page import="java.sql.*"%>
<%@ page import="com.wen.imdbsearch.*"  %>
<%@ page errorPage="exception.jsp" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", -1); 
%>

<%
	try{
		
	

	String playlistid=request.getParameter("playlistid");
	String movieid=request.getParameter("addmovieid");
	 
													
	Singleton singleton = Singleton.getInstance();
	Class.forName(singleton.jdbcDriver);
	Connection con = DriverManager.getConnection(singleton.connURL,singleton.username,singleton.pwd);
	String query = "CALL insert_movieplaylist(?,?,?)";
	CallableStatement stmt = con.prepareCall(query);
	
	stmt.setString(1, movieid);
	stmt.setInt(2, Integer.valueOf(playlistid));		
	stmt.registerOutParameter(3, Types.VARCHAR);
	
	stmt.execute();
	
	String result = stmt.getString(3);
	
	boolean checknum=false;
	
	if(result==null){
		checknum = false;
	}
	else{
		try{
			int n = Integer.parseInt(result);
		}
		catch (NumberFormatException e) {
			checknum= false;
		}
		checknum = true;
	}

	String msg="";
	
	if(checknum)
	{
		
		if (Integer.valueOf(result)==1)
		{
			msg = "true";
			
		}
		else {
			msg = "false";
		}
	}else{
		msg = "ERROR: "+result;
	}
	
	stmt.close();
	
	response.getWriter().write(msg);
	
	
	
	}catch(Exception e){
		e.printStackTrace();
		response.getWriter().write(e.getMessage());
	}


%>