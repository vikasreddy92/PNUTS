<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<center><h1>Nutritionist Echo</h1></center>
<body>
<jsp:useBean id="pnuts_nutritionist"
	type="org.vkedco.nutrition.pnuts.nutritionist.NutritionistBean"
	scope="request" />
<p>
id: <jsp:getProperty name="pnuts_nutritionist" 
					property="id" /><br>
first name: <jsp:getProperty name="pnuts_nutritionist" 
					property="firstName" /><br>
last name: <jsp:getProperty name="pnuts_nutritionist" 
					property="lastName" /><br>
email: <jsp:getProperty name="pnuts_nutritionist" 
					property="email" /><br>
password: <jsp:getProperty name="pnuts_nutritionist" 
					property="password" /><br>
confirmed password: <jsp:getProperty name="pnuts_nutritionist" 
					property="confirmedPassword" /><br>
role: <jsp:getProperty name="pnuts_nutritionist" 
					property="role" /><br>
</body>
</html>