<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>File Upload Response</title>
</head>
<body>
	<center>
	<table>
		<tr>
			<td>First file upload result =</td>
			<td>"<%=request.getAttribute("upload_message1")%>"
			</td>
		</tr>
		<tr>
			<td>Second file upload result =</td>
			<td>"<%=request.getAttribute("upload_message2")%>"
			</td>
		</tr>
	</table>
	</center>

</body>
</html>