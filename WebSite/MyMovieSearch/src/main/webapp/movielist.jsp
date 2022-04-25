<%@ page import="java.util.ArrayList"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.wen.imdbsearch.*"  %>
<%@ page errorPage="exception.jsp" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
    
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", -1); 
%>
    
<!DOCTYPE html>

<html lang="en" class="no-js">

<!-- movielist07:38-->
<head>
	<!-- Basic need -->
	<title>My Movie Search Portal</title>
	<meta charset="UTF-8">
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta name="author" content="">
	<link rel="profile" href="#">

    <!--Google Font-->
    <link rel="stylesheet" href='http://fonts.googleapis.com/css?family=Dosis:400,700,500|Nunito:300,400,600' />
	<!-- Mobile specific meta -->
	<meta name=viewport content="width=device-width, initial-scale=1">
	<meta name="format-detection" content="telephone-no">

	<!-- CSS files -->
	<link rel="stylesheet" href="css/plugins.css">
	<link rel="stylesheet" href="css/style.css">


</head>
<body>
<!--preloading-->
<div id="preloader" >
    <img class="logo" src="images/logo4.png" alt="" width="119" height="58">
    <div id="status">
        <span></span>
<!--         <iframe width="420" height="315" -->
<!-- 			src="https://www.youtube.com/embed/tgbNymZ7vqY" style='display: none;'> -->
<!-- 		</iframe> -->
        <span></span>
    </div>
	
</div>
<!--end of preloading-->

<c:if test="${not empty alertmsg}">
	<script>
        var message = "${alertmsg}";
        alert(message);
    </script>
</c:if>

<!--addPlaylist form popup-->
<div class="login-wrapper" id="cpladd-content">
    <div class="login-content form-style-1">
        <a href="#" class="close" style="display:block;">x</a>
        <h3>Add to My Playlist</h3>
      
        	<div class="row">
        	  <input type="hidden" name="addmovieid" id="addmovieid"  />               
        	</div>
        	
        	<div class="row group-ip">
        	<c:choose>
        	<c:when test="${plresultcount>0}">
        	
        	  <select name="userplaylists" id="userplaylists" onchange="getSelected();"  style="cursor: pointer;font-size:12pt;">        	  
				<c:forEach var="fetch" items="${plresult}">
				<option value='<c:out value="${fetch.getPlaylistid()}"/>'><c:out value="${fetch.getPlaylist_name()}"/></option>
				</c:forEach>        	  
        	  </select>
        	  
        	</c:when>
        	<c:otherwise>
        	<h6>No Playlist Exists; you need to create Playlist</h6>
        	</c:otherwise>
        	</c:choose>
        	
        	</div>
         	<div class="row">
         	&nbsp;
         	</div>
            <div class="row">
             <button type="button" onclick="addPL(document.getElementById('addmovieid').value)" >Add</button>             
           </div>
           
       
       
    </div>
</div>
<!--end of login form popup-->

<!--createPlaylist form popup-->
<div class="login-wrapper" id="cpl-content">
    <div class="login-content">
        <a href="#" class="close" style="display:block;">x</a>
        <h3>Create Playlist</h3>
       <form method="post" action="creatplaylist" onsubmit="return validateCPL(this)">
        	<div class="row">
        		 <label for="Playlist Name">
                    Playlist Name:
                    <input type="text" name="namepl" id="namepl" placeholder="enter playlist name"  required="required" />
                </label>
        	</div>
         
            <div class="row">
             <button type="submit" onclick="loading6()" >Create</button>
             <img alt="" style="visibility:collapse;" id='page_loader6' src="images/fancybox_loading@2x.gif"  />
           </div>
           <div class="row">
           <input name="currentWebPageLogin" value="home" type="hidden"/>
           </div>
           
       </form>
       
    </div>
</div>
<!--end of login form popup-->

<!--login form popup-->
<div class="login-wrapper" id="login-content">
    <div class="login-content">
        <a href="#" class="close">x</a>
        <h3>Login</h3>
       <form method="post" action="userlogin" onsubmit="return validateLoginForm(this)">
        	<div class="row">
        		 <label for="username">
                    Email:
                    <input type="text" name="emaillogin" id="emaillogin" placeholder="xyz@gmail.com" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" style="text-transform: lowercase;" required="required" />
                </label>
        	</div>
           
            <div class="row">
            	<label for="password">
                    Password:
                    <input type="password" name="passwordlogin" id="passwordlogin" placeholder="******" pattern="(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$" required="required" />
                </label>
            </div>
        
            <div class="row">
             <button type="submit" >Login</button>
             <img alt="" style="visibility:collapse;" id='page_loader4' src="images/fancybox_loading@2x.gif"  />
           </div>
           <div class="row">
           <input name="currentWebPageLogin" value="movielist" type="hidden"/>
           </div>
           
       </form>
       
    </div>
</div>
<!--end of login form popup-->

<!--signup form popup-->
<div class="login-wrapper"  id="signup-content">
    <div class="login-content">
        <a href="#" class="close">x</a>
        <h3>sign up</h3>
        <form method="post" action="usersignup" onsubmit="return validateSignupForm(this)">
            <div class="row">
                 <label for="username-2">
                    Username:
                    <input type="text" name="username" id="username" placeholder="Hugh Jackman"  required="required" style="text-transform: capitalize;"/>
        		</label>
            </div>
           
            <div class="row">
                <label for="email-2">
                    your email:
                    <input type="text" name="email" id="email" placeholder="xyz@gmail.com" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" style="text-transform: lowercase;" required="required" />
                </label>
            </div>
             <div class="row">
                <label for="password-1">
                    Password:
                    <span style="font-size:8pt;">Must contain at least one  number and one uppercase and lowercase letter, and at least 8 or more characters</span>
                    <input type="password" name="password1" id="password1" placeholder="" pattern="(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$" title="Must contain at least one  number and one uppercase and lowercase letter, and at least 8 or more characters" required="required" />
                </label>
            </div>
             <div class="row">
                <label for="repassword-2">
                    re-type Password:
                    <input type="password" name="password2" id="password2" placeholder="" pattern="(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$" title="Must contain at least one  number and one uppercase and lowercase letter, and at least 8 or more characters" required="required" onkeyup="checkPassword()" />
                    <img alt="" style="visibility:collapse;width:16px;height:16px;" id='error1' src="images/wrong.png"   />
                    <img alt="" style="visibility:collapse;width:16px;height:16px;" id='right1' src="images/right.png"   />
                </label>
            </div>
           <div class="row">
             <button type="submit" onclick="loading5()">sign up</button>
             <img alt="" style="visibility:collapse;" id='page_loader5' src="images/fancybox_loading@2x.gif"  />
           </div>
           <div class="row">
           <input name="currentWebPage" value="movielist" type="hidden"/>
           </div>
        </form>
    </div>
</div>
<!--end of signup form popup-->

<!-- BEGIN | Header -->
<header class="ht-header">
	<div class="container">
		<nav class="navbar navbar-default navbar-custom" style="padding:0;">
				<!-- Brand and toggle get grouped for better mobile display -->
				<div class="navbar-header logo">
				    <div class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
					    <span class="sr-only">Toggle navigation</span>
					    <div id="nav-icon1">
							<span></span>
							<span></span>
							<span></span>
						</div>
				    </div>
				    <a href="#"><img class="logo text-center" src="images/logo4.png" alt="" width="100" height="58" ></a>
				    
			    </div>
				<!-- Collect the nav links, forms, and other content for toggling -->
				<div class="collapse navbar-collapse flex-parent" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav flex-child-menu menu-left">
						<li class="hidden">
							<a href="#page-top"></a>
						</li>						
						<li ><a href="home.jsp">Home</a></li>
					</ul>
					
					<ul class="nav navbar-nav flex-child-menu menu-right">						
					<c:choose>
						<c:when test="${sessionScope.userid>0}">
						<li >
						<div class="row">
						<div class="col-md-4" style="border-bottom:1px solid red;">
						<a href="#">
						<img alt="" style="width:24px;height:24px;" src="images/login.png"  />&nbsp;Welcome: <c:out value="${sessionScope.email}"/>
						</a>
						</div>
						<div class="col-md-3" style="border-bottom:1px solid red;padding-bottom:2px;">						
						<a href="#" class="cplaylist">Create PlayList</a>
						</div>	
						<div class="col-md-2" style="border-bottom:1px solid red;padding-bottom:2px;">
						<a href="#" onclick="viewplaylist()"> View PlayList
						</a>
						</div>						
						</div>
						</li>&nbsp;&nbsp;
						
						<li ><a href="#" onclick="logout()">log out</a></li>&nbsp;&nbsp;
						
						</c:when>
						<c:otherwise>
						<li class="loginLink"><a href="#">LOG In</a></li>
						</c:otherwise>						
						</c:choose>				
						<li class="btn signupLink"><a href="#">sign up</a></li>
					</ul>
				</div>
			<!-- /.navbar-collapse -->
	    </nav>
	    <form method="post" action="userlogout" class="form-style-1" name="logoutform" id="logoutform" style="display:none;" >
						</form>
						
		<form method="post" action="viewplaylist" class="form-style-1" name="viewpl" id="viewpl" style="display:none;" >
						</form>
	    <!-- top search form -->
	  
	</div>
</header>
<!-- END | Header -->

<div class="hero common-hero">
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="hero-ct">
					<h1> movie search - result</h1>
					<ul class="breadcumb">
						<li class="active"><a href="home.jsp">Home</a></li>
						<li> <span class="ion-ios-arrow-right"></span> movie listing</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="page-single movie_list">
	<div class="container">
		<div class="row ipad-width2">
			<div class="col-md-8 col-sm-12 col-xs-12">
				<div class="topbar-filter">
					<p>Found <span style="color:yellow;">${rowcount} movies</span> in total</p>
					<div class="pagination2">
						<span id="xofy1">Page ${currentPage} of ${totalPage}:</span>
						
						<c:choose>
						<c:when test="${totalPage>1}">
						
												
						<a href="#xofy1" onclick="mySubmit2()" style="font-size:12pt;">next <i class="ion-arrow-right-b" style="font-size:12pt;"></i></a>		
						
						
						<img alt="" style="visibility:collapse;" id='page_loader2' src="images/fancybox_loading@2x.gif"  />
						
						
						</c:when>
						<c:otherwise>
						<span></span>
						</c:otherwise>
						</c:choose>
					</div>
				</div>
				<c:forEach var="fetch" items="${result}">
				<div class="movie-item-style-2">
										
					<c:choose>
					<c:when test="${empty fetch.getCover()}">
					<img src="images/uploads/blankimdb.jpg"  style="border:1px solid white;width:170px;height:261px;"  alt="">   
					</c:when>
					<c:otherwise>
					<img src='<c:out value="${fetch.getCover()}"/>'  title='<c:out value="${fetch.getMovieid() }"/>' style="border:1px solid white;width:170px;height:261px;">                		             	
                	</c:otherwise>
					</c:choose>
					
					<div class="mv-item-infor">
						<h6 sytle="font-size:13pt;"><a href="#"><c:out value="${fetch.getPrimary_title()}"/> <span>(<c:out value="${fetch.getRelease_year()}"/>)</span></a></h6> 
						<p class="rate"><i class="ion-android-star"></i><span><c:out value="${fetch.getRating()}" /></span> /10</p>
						<p class="">Genre: <a href="#"><c:out value="${fetch.getGenre() }"/></a></p>
						<p class="describe">Original Title: <a href="#"><c:out value="${fetch.getOriginal_title() }"/></a></p>
						<p class="run-time"> Run Time: <c:out value="${fetch.getRuntime_duration() }"/></p>
						<p>Director: <a href="#"><c:out value="${fetch.getDirector_name()}" /></a></p>
						<c:choose>
						<c:when test='${fetch.getCasts()!="0"}'>
						<p>Stars: <a href="#"><c:out value="${fetch.getCasts()}"/></a> </p>
						</c:when>
						<c:otherwise>
						<p>Stars: <a href="#">N/A</a> </p>
						</c:otherwise>
						</c:choose>
						<c:choose>
						<c:when test="${sessionScope.userid>0}">
						<input type="hidden" name="movieid" value='<c:out value="${fetch.getMovieid() }"/>'/>
							<p><a href="#" id="<c:out value="${fetch.getMovieid() }"/>" class="cplaylistadd" onclick="javascript:document.getElementById('addmovieid').value='<c:out value="${fetch.getMovieid() }"/>'"><img  src="images/playlist.png" alt="Add to Playlist" title="Add to Playlist" style="width:20px;height:20px;"/><span style="color:yellow;">Add to Playlist</span></a></p>
						</c:when>
						<c:otherwise>
						<p style="color:yellow;">Login to add it in playlist</p>
						</c:otherwise>
						</c:choose>
						
					</div>
				</div>
				</c:forEach>
				<form method="post" action="querycontrollerPaging" id="formPaging"  >
				<div class="topbar-filter">					
					<div class="pagination2">
						<span id="xofy">Page ${currentPage} of ${totalPage}:</span>
						
						<c:choose>
						<c:when test="${totalPage>1}">
						
						
						<input name="movieid_next" value="${movieid_next}" type="hidden"/>
						<input name="currentPage" value="${currentPage}" type="hidden"/>
						<input name="pquery" value="${pquery}" type="hidden"/>
						<input name="rowcount" value="${rowcount}" type="hidden"/>
						<input name="totalPage" value="${totalPage}" type="hidden"/>		
											
						<a href="#xofy" onclick="mySubmit3()" style="font-size:12pt;">next <i class="ion-arrow-right-b" style="font-size:12pt;"></i></a>		
						
						
						<img alt="" style="visibility:collapse;" id='page_loader3' src="images/fancybox_loading@2x.gif"  />
						</c:when>
						<c:otherwise>
						<span></span>
						</c:otherwise>
						</c:choose>
					</div>
				</div>
				</form>
			</div>
			<div class="col-md-4 col-sm-12 col-xs-12">
				<div class="sidebar">
					<div class="searh-form">
						<h4 class="sb-title">Search for movie</h4>						
						<form method="post" action="querycontroller" class="form-style-1" name="searchForm" onsubmit="return validateForm(this)" required> 
							<div class="row">
								<div class="col-md-12 form-it">
									<label>Movie name</label>
									<input type="text" name="movietext" placeholder="Enter keywords">
								</div>
								<div class="col-md-12 form-it">
									<label>Genres</label>
									<div class="group-ip">
										<select name="genre" id="genre" style="cursor: pointer;">
											
											<option value="0">Select</option>
											
											<%
												Singleton singleton = Singleton.getInstance();
												Class.forName(singleton.jdbcDriver);
												Connection con = DriverManager.getConnection(singleton.connURL,singleton.username,singleton.pwd);
												Statement stmt = con.createStatement();
												String query = "select g.\"genreName\" from genre g order by g.\"genreName\"";
												ResultSet rs = stmt.executeQuery(query);
												
												while(rs.next()){
													%>													
													<option><%=rs.getString("genreName")%></option>
													<%
													}//end while
												rs.close();
												stmt.close();
												%>
										</select>
										
										
									</div>
									
								</div>
								<div class="col-md-12 form-it">
									<label>Rating Range</label>
									
									 <select name="rating" style="cursor: pointer;">
									 
									 <option value="0">Select</option>
										<%  stmt = con.createStatement();
										 query = "select distinct rating from movie where rating is not null order by rating desc";
										  rs = stmt.executeQuery(query);
												
												while(rs.next()){
													%>
													<option ><%=rs.getString("rating") %></option>
													<%
												}//end while
												rs.close();
												stmt.close();
											%>
									</select>
									
								</div>
								<div class="col-md-12 form-it">
									<label>Release Year</label>
									<div class="row">
										<div class="col-md-6">
											<select name="releaseyear_from" style="cursor: pointer;">
												<option value="0">From</option>
												<%  stmt = con.createStatement();
										 			query = "select distinct release_year from movie where release_year is not null order by release_year desc";
													  rs = stmt.executeQuery(query);
												ArrayList<String> release_year = new ArrayList<String>();
												while(rs.next()){
													release_year.add(rs.getString("release_year"));
												}//end while
													rs.close();
													stmt.close();
														
													for(String str :release_year)
													{
													%>
													<option ><%=str %></option>
													<%
													}
												
													%>
											</select>
										</div>
										<div class="col-md-6">
											<select name="releaseyear_to" style="cursor: pointer;">
												<option value="0">To</option>
												 <%  
												 for(String str :release_year)
													{
													%>
													<option ><%=str%></option>
													<%
													}
												 con.close();
													%>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-6 col-md-offset-2  ">
									<input class="submit" type="submit" value="submit" onclick="loading()">
								</div>
									<div class="col-md-2">
								<img alt="" style="visibility:collapse;" id='page_loader' src="images/fancybox_loading@2x.gif"  />
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="js/jquery.js"></script>
<script src="js/plugins.js"></script>
<script src="js/plugins2.js"></script>
<script src="js/custom.js"></script>

<script>

function mySubmit3()
{
	loading3();
	document.getElementById("formPaging").submit();
}

function loading3() 
{		document.getElementById('page_loader3').style.visibility = 'visible';
		
}


function mySubmit2()
{
	loading2();
	document.getElementById("formPaging").submit();
}

function loading2() 
{	
   
		document.getElementById('page_loader2').style.visibility = 'visible';
	
	
}

 function loading() 
 {	
    
		document.getElementById('page_loader').style.visibility = 'visible';
	
	
 }
 
 function loading5() 
 {	
    
		document.getElementById('page_loader5').style.visibility = 'visible';
	
	
 }
 
 function validateForm(form) {
	  var movietext = form.movietext.value;
	  var genre = form.genre.selectedIndex;
	  var rating = form.rating.selectedIndex;
	  var releaseyear_from = form.releaseyear_from.selectedIndex;
	  var releaseyear_to = form.releaseyear_to.selectedIndex;
	  
	  
	  if (genre == 0 && movietext == "" && rating ==0 && releaseyear_from==0 && releaseyear_to==0) {
	    alert("At least one Filter must be selected");
	    document.getElementById('page_loader').style.visibility = 'collapse';
	    return false;
	  }
	  return true;
	}
 
 function validateLoginForm(form){		
		var email = form.emaillogin.values;
		var password1 = form.passwordlogin.values;
		
		
		 if (email =="" && password1=="" ) 
		 {
			 alert("Login form must be filled");
			    document.getElementById('page_loader4').style.visibility = 'collapse';
			    return false;
		 }
		 else{
			 document.getElementById('page_loader4').style.visibility = 'visible';
		 }
		 		 
		 return true;
	 }

 function logout(){
	 document.getElementById("logoutform").submit();
 }
 
 function validateSignupForm(form){
		var username = form.username.values;
		var email = form.email.values;
		var password1 = form.password1.values;
		var password2 = form.password2.values;
		
		 if (username == "" && email =="" && password1=="" && password2=="") 
		 {
			 alert("Signup form must be filled");
			    document.getElementById('page_loader5').style.visibility = 'collapse';
			    return false;
		 }
		 
		 if(password1!=password2){
			 alert("Password and Re-Enter Password are mismatched");
			    document.getElementById('page_loader5').style.visibility = 'collapse';
			    return false;
		 }
		 
		 return true;
	 }
 
 function checkPassword(){
	 var pass1 = document.getElementById('password1').value;
	 var pass2 = document.getElementById('password2').value;
	 
	 if(pass1!=pass2)
		{
		 document.getElementById('error1').style.visibility = 'visible';
		 document.getElementById('right1').style.visibility = 'collapse';
             
		}
	 else{
		 document.getElementById('error1').style.visibility = 'collapse';
		 document.getElementById('right1').style.visibility = 'visible';
	 }
	 
 }
 
 function loading6() 
 {	
    
		document.getElementById('page_loader6').style.visibility = 'visible';
	
	
 }
 
 function validateCPL(form){
	 var namepl = form.namepl.value;
	  
	  
	  if (namepl == "") {
	    alert("Playlist name must be entered");
	    document.getElementById('page_loader6').style.visibility = 'collapse';
	    return false;
	  }
	  return true;
 }

 function getSelected()
 {
 	var selectedSource = document.getElementById("userplaylists").value;
 	return selectedSource;
 }
 
 function addPL(movieid)
 {
	 $.ajax({
		 url: "http://localhost:8080/MyMovieSearch/save.jsp",
	     type: "post",
	     dataType: "text",
	     data: {
	    	 playlistid:getSelected(),
			addmovieid:movieid
	        }, //close here
	     success: function(data) {
	          var values = $.trim(data); //triming value if there is any whitespaces
	          //alert(values);
	          if (values == "true") {
	            alert("Movie Added"); //show alert
	   			$('.openform').removeClass('openform');
	   		 	document.getElementById(movieid).style.visibility = 'collapse';
	          } else {
	            alert("Already in your Playlist");
	          }
	        }
	      });
	 
 }//end addPL
	
 function viewplaylist(){
	 document.getElementById("viewpl").submit();
 }
 
</script>


</body>

<!-- movielist07:38-->
</html>