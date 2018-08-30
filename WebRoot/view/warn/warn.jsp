<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <div style="height:100%;background:url('${viewpath}/warn/images/tips.jpg') no-repeat 100% 100%; background-size: 100% 100%; text-align:center;max-width:540px;max-height:300px">
			<div style="height: 30px; margin: 0 auto; font-size: 24px; color: #0000ee; font-weight: bold;; padding-top: 50px;padding-left: 50px;">${message}</div>
		</div>
    </div>
</div>

