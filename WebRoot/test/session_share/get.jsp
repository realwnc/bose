 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>session</title>
</head>
<body>
<%String s = session.getId(); //获取session ID号  %>
 <%=s %>
 <br/>
 <br/>
<%=(String)session.getAttribute("wncheng")%>

</body>
</html>