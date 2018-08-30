<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/view/resource/js/jquery-easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/view/resource/js/jquery-easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/view/resource/js/jquery-easyui/demo/demo.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/view/resource/js/jquery-easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/view/resource/js/jquery-easyui/jquery.easyui.min.js"></script>

<input type="hidden" id="basepath" name="basepath" value="<%=basePath %>" />
<!-- 全局变量basepath -->
<script type="text/javascript">
var basepath = $("#basepath").val();
</script>