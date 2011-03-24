<%@ page isELIgnored="false" contentType="text/html" pageEncoding="windows-1252"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<title>PlaceBooks admin</title>
</head>

<body>
<h1>Administration actions</h1>
<hr>
<a href="admin/new/placebook">Make a test PlaceBook</a>
<br />
<a href="admin/print/placebooks">List all PlaceBooks</a>
<br />
<a href="admin/delete/all">Delete all PlaceBooks</a>

<br />
<h1>Everytrail Tests</h1>
<div>
<form action='admin/test/everytrail/login' method='POST'>
	<h3>Log in</h3>
	<div>Username: <input type='text' name='username'></div>
	<div>Password: <input type='password' name='password'></div>
	<input type='submit' value='Log in'>
</form>
</div>
<div>
	<form action='admin/test/everytrail/pictures' method='POST'>
		<h3>List user's pictures</h3>
		<div>Username: <input type='text' name='username'></div>
		<div>Password: <input type='password' name='password'></div>
		<input type='submit' value='List'>
	</form>
</div>
<div>
	<form action='admin/test/everytrail/trips' method='POST'>
		<h3>List user's trips</h3>
		<div>Username: <input type='text' name='username'></div>
		<div>Password: <input type='password' name='password'></div>
		<input type='submit' value='List'>
	</form>
</div>
</body>
</html>