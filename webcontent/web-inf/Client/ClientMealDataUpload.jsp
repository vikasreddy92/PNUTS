<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="javax.servlet.RequestDispatcher"
	import="javax.servlet.ServletException"
	import="javax.servlet.annotation.WebServlet"
	import="javax.servlet.http.HttpServlet"
	import="javax.servlet.http.HttpServletRequest"
	import="javax.servlet.http.HttpServletResponse"
	import="org.vkedco.nutrition.pnuts.client.ClientBean"
	import="org.vkedco.nutrition.pnuts.CommonConstants" %>
<%
	//Checking for session, if client comes directly this page(without logging in), sends him back to login page.
	
    HttpSession httpSession = request.getSession(true);
	ClientBean cb = new ClientBean();
	cb = (ClientBean) httpSession.getAttribute(CommonConstants.CLIENT_BEAN_KEY);
	if (cb == null) {
		System.err.println("http session is null in upload jsp page");
		request.getRequestDispatcher(
				CommonConstants.ILLEGAL_CLIENT_ACCESS).forward(
				request, response);
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload</title>
</head>
<body>
	<center>
		<h1>Meal Data Upload</h1>
		<form action="ClientMealDataUpload" method="post"
			enctype="multipart/form-data">
			<table>
				<tr>
					<td>Upload before-meal video:</td>
					<td><input type="file" name="beforeMealFile" required ></td>
				</tr>
				<tr>
					<td>Upload after-meal (plate waste) video:</td>
					<td><input type="file" name="afterMealFile" required></td>
				</tr>
			</table>
			<input type="submit" value="Upload"> <input type="reset"
				value="Reset">
		</form>
	</center>
</body>
</html>