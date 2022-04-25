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
						<li ><a href="movielist.jsp">movielist</a></li>
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
												
						</div>
						</li>&nbsp;&nbsp;
						
						<li ><a href="#" onclick="logout()">log out</a></li>&nbsp;&nbsp;
						
						</c:when>
						<c:otherwise>
						&nbsp;
						</c:otherwise>						
						</c:choose>				
						
					</ul>
				</div>
			<!-- /.navbar-collapse -->
	    </nav>
	    <form method="post" action="userlogout" class="form-style-1" name="logoutform" id="logoutform" style="display:none;" >
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
					<h1> Playlist - result</h1>
					<ul class="breadcumb">
						<li class="active"><a href="home.jsp">Home</a></li>
						<li> <span class="ion-ios-arrow-right"></span> View Playlist</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="page-single movie_list">
	<div class="container">
	
	<h2 style="color:white;">Playlist: ${playlistname}</h2>
	
	
	<br/>
	
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
						<form method="post" action="removemoviepl" class="form-style-1" name="removepl" id="removepl<c:out value="${fetch.getMovieid() }"/>" style="display:none;" >
						<input type="hidden" name="movieid" value='<c:out value="${fetch.getMovieid() }"/>'/>
						<input type="text" name="playlistid" value='${playlistid}'/>
						</form>						
							<p><a href="#" onclick="remove('<c:out value="${fetch.getMovieid() }"/>')"/><span style="color:yellow;">Remove from Playlist</span></a>	</p>
							<p><img alt="" style="visibility:collapse;width:16px;height:16px;" id='page_loader_remove' src="images/fancybox_loading@2x.gif"  /></p>
						</c:when>
						<c:otherwise>
						<p style="color:yellow;">&nbsp;</p>
						</c:otherwise>
						</c:choose>
						
					</div>
				</div>
				</c:forEach>
				<input type='hidden' name="pquery" value='<c:out value="${pquery}"/>'/>
			</div>
			<div class="col-md-4 col-sm-12 col-xs-12">
				<div class="sidebar">
					<div class="searh-form">
						<h4 class="sb-title">Search for movie</h4>						
						<form method="post" action="viewplaylist" class="form-style-1" name="searchPlaylist" required> 
							<div class="row">
								
								<div class="col-md-12 form-it">
									<label>Playlist</label>
									<div class="group-ip">
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


 function loading() 
 {	
    
		document.getElementById('page_loader').style.visibility = 'visible';
	
	
 }
 
 function logout(){
	 document.getElementById("logoutform").submit();
 }
 
function remove(movieid){
	document.getElementById('page_loader_remove').style.visibility = 'visible';
	document.getElementById("removepl"+movieid).submit();
}

  
</script>


</body>


</html>