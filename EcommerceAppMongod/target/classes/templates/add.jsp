<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Product</title>
</head>
<body>
	<form action="add" method="post" commandName="addform">
		<table>
		<tr><h1>Fill Product's Detail</h1></tr>
		<br><br>
		<tr>
			<td>Name  : <input type="text" name="name" /></td>
		</tr>
		<tr>
		<td>Category : <input type="text" name="category" /></td>
		</tr>
		<tr>
		<td>Price : <input type="text" name="price" /></td>
		</tr>
		</table>
		<input type="submit" value="OK">
	</form>
</body>
</html>