<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<center><h1>Client Echo</h1></center>
<body>
<jsp:useBean id="pnuts_client"
	type="org.vkedco.nutrition.pnuts.client.ClientBean"
	scope="request" />
<p>
id: <jsp:getProperty name="pnuts_client" property="id" /><br>
email: <jsp:getProperty name="pnuts_client" property="email" /><br>
password: <jsp:getProperty name="pnuts_client" property="password" /><br>
confirmed password: <jsp:getProperty name="pnuts_client" property="confirmedPassword" /><br>
nutrition tip day: <jsp:getProperty name="pnuts_client" property="nutritionTipDay" /><br>
nutrition tip time: <jsp:getProperty name="pnuts_client" property="nutritionTipTime" /><br>
caloric summary day: <jsp:getProperty name="pnuts_client" property="caloricSummaryDay" /><br>
caloric summary time: <jsp:getProperty name="pnuts_client" property="caloricSummaryTime" /><br>
</body>
</html>