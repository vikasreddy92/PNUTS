<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload Page</title>
</head>
<body>
    <center>
    <h1>Test Local Video Link</h1>
      
    <script type="text/javascript">
	function getFile() {
		document.getElementById('tempimg').style.visibility='visible';
	}
    </script>
    <input type="button" onclick="getFile();"/>
	<img id="tempimg" style="visibility:hidden" src="TestFileDownload?fileName=baboon1.jpg"/> 
	
	<img src="TestFileDownload?fileName=baboon1.jpg"/>
	<a href="TestFileDownload?fileName=VID01.mp4">video</a>
	
   
    <!--  
	<form action="ClientBeforeMealDataUpload" method="post" enctype="multipart/form-data">
	<table>
		<tr><td>Upload before-meal video:</td><td><input type="file" name="beforeMealFile"></td></tr>
	</table>
	<input type="submit" value="Upload">
	<input type="reset" value="Reset">
	</form>
	
	-->
	</center>
</body>
</html>