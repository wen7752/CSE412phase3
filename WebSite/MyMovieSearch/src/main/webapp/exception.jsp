<%@ page isErrorPage="true" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Exception</title>
<meta charset="UTF-8">

	<link rel="stylesheet" href="css/bootstrap.min.css" />

    <!--Google Font-->
    <link rel="stylesheet" href='http://fonts.googleapis.com/css?family=Dosis:400,700,500|Nunito:300,400,600' />
	<!-- Mobile specific meta -->
	<meta name=viewport content="width=device-width, initial-scale=1">
	<meta name="format-detection" content="telephone-no">

	<!-- CSS files -->
	<link rel="stylesheet" href="css/plugins.css">
	<link rel="stylesheet" href="css/style.css">
	
</head>
<body style="background: url('images/uploads/error-bg.jpg') no-repeat;">
<div class="container text-center text-danger ">
<img alt="" src="images/uploads/err-img.png">
<br/>
<h3 class="text-danger">
<%= exception %>
</h3> 
</div>



</body>
</html>