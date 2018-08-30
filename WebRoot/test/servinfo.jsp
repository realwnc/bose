<%@ page language="java" import="java.util.*,java.net.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
 <title>Tomcat+memcached共享session测试</title>
</head>
<body>

<%
String ipaddr="unknown";
String hostname="unknown";
String ip="unknown";
try{
        InetAddress addr = InetAddress.getLocalHost();
        ipaddr=addr.getHostAddress().toString();//获得本机IP
        hostname=addr.getHostName().toString();//获得本机名称

        ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
    }
}
catch(Exception e)
{
        ipaddr="Exception";
        hostname="Exception";
}
%>
SessionID:<b><%=session.getId()%></b>
<BR>
ServerName:<b><%=request.getServerName()%></b>
<BR>
ServerIP:<b><%=ipaddr%></b>
<BR>
ServerPort:<b><%=request.getServerPort()%></b>
<BR>
protocal:<b><%=request.getScheme()%></b>
<BR>
getRequestURL:<b><%=request.getRequestURL() %></b>
<BR>
X-Forwarded-Scheme:<b><%=request.getHeader("X-Forwarded-Scheme")%></b>
<BR>
x-forwarded-for:<b><%=request.getHeader("x-forwarded-for")%></b>
<BR>
internet ip:<b><%=ip%> 

</body>
</html>