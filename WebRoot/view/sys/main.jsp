<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/commons/basejs.jsp" %>
<link rel="stylesheet" type="text/css" href="${staticpath}/sys/css/icommon.css" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理平台</title>
<style>
	
</style>
<script type="text/javascript">
    var index_layout;
    var index_tabs;
    var layout_west_tree;

    $(function() {
        index_layout = $('#index_layout').layout({
            fit : true
        });
        index_tabs = $('#index_tabs').tabs({
            fit : true,
            border : false,
            tools : [{
                iconCls : 'icon-home',
                handler : function() {
                    index_tabs.tabs('select', 0);
                }
            }, {
                iconCls : 'icon-refresh',
                handler : function() {
                    var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
                    index_tabs.tabs('getTab', index).panel('open').panel('refresh');
                }
            }, {
                iconCls : 'icon-del',
                handler : function() {
                    var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
                    var tab = index_tabs.tabs('getTab', index);
                    if (tab.panel('options').closable) {
                        index_tabs.tabs('close', index);
                    }
                }
            } ]
        });

		//绑定tabs的右键菜单 begin
		$("#index_tabs").tabs({
			onContextMenu:function(e,title){
				e.preventDefault();
				$('#tabsMenu').menu('show', {  
					left: e.pageX,  
					top: e.pageY  
				}).data("tabTitle",title);
			}
		});

		//实例化menu的onClick事件
		$("#tabsMenu").menu({
			onClick:function(item){
				CloseTab(this,item.name);
			}
		});

		//几个关闭事件的实现
		function CloseTab(menu,type){
			var curTabTitle = $(menu).data("tabTitle");
			var tabs = $("#index_tabs");

			if(type === "close"){
				tabs.tabs("close",curTabTitle);
				return;
			}

			var allTabs = tabs.tabs("tabs");
			var closeTabsTitle = [];

			$.each(allTabs,function(){
				var opt = $(this).panel("options");
				if(opt.closable && opt.title != curTabTitle && type === "Other"){
					closeTabsTitle.push(opt.title);
				}else if(opt.closable && type === "All"){
					closeTabsTitle.push(opt.title);
				}
			});

			for(var i = 0;i<closeTabsTitle.length;i++){
				tabs.tabs("close",closeTabsTitle[i]);
			}
		}
		//绑定tabs的右键菜单 endxx
    });



    function addTab(title, href, icon) {
        var tt = $('#index_tabs');
        icon = icon || 'menu_icon_service';
        if (tt.tabs('exists', title)) {
            tt.tabs('select', title);
            var currTab = tt.tabs('getTab', title);
            tt.tabs('update', {tab: currTab, options: {content: content, closable: true}});
        } else {
            if (href) {
                var content = '<iframe frameborder="0" src="' + href + '" style="border:0;width:100%;height:99.5%;"></iframe>';
            } else {
                var content = '未实现';
            }
            tt.tabs('add', {
                title : title,
                content : content,
                closable : true,
                iconCls: icon
            });
        }
    }

    function logout(){
        $.messager.confirm('提示','确定要退出?',function(r){
            if (r){
                progressLoad();
                $.post('${path }/sys/logout.do', function(result) {
                    if(result.success){
                        progressClose();
                        window.location.href='${path }/sys/';
                    }
                }, 'json');
            }
        });
    }

    function editUserPwd() {
        parent.$.modalDialog({
            title : '修改密码',
            width : 300,
            height : 250,
            href : '${path }/sys/user/editPwdPage.do',
            buttons : [ {
                text : '确定',
                handler : function() {
                    var f = parent.$.modalDialog.handler.find('#editUserPwdForm');
                    f.submit();
                }
            } ]
        });
    }

</script>
</head>
<body>
<div id="loading" style="position: fixed;top: -50%;left: -50%;width: 200%;height: 200%;background: #fff;z-index: 100;overflow: hidden;">
	<img src="${staticpath}/sys/images/ajax-loader.gif" style="position: absolute;top: 0;left: 0;right: 0;bottom: 0;margin: auto;"/>
</div>
<div id="index_layout">
	<div data-options="region:'north',border:false" class="header">
		<div>
			<span style="float: right; padding-right: 20px; margin-top: 15px; color: #333">
			欢迎 <b><shiro:principal></shiro:principal></b>&nbsp;&nbsp; 
			<shiro:hasPermission name="/user/editPwdPage.do"><a href="javascript:void(0)" onclick="editUserPwd()" class="easyui-linkbutton" plain="true" icon="icon-edit" >修改密码</a></shiro:hasPermission>&nbsp;&nbsp;			
			<a href="javascript:void(0)" onclick="logout()" class="easyui-linkbutton" plain="true" icon="icon-clear">安全退出</a>
			<!--<span class="header"></span>-->
		</div>
	</div>
	<div data-options="region:'west',split:true" title="菜单" style="width: 160px; overflow: hidden;overflow-y:auto; padding:0px">
		<div class="easyui-accordion  i_accordion_menu" fit="true" border="false">
			<shiro:hasPermission name="/sys/root.do">
			<div title="系统管理" style="overflow: auto;">
				<div class="nav-item" style="margin-top:2px;margin-bottom:2px;border-bottom:1px solid #d6d6d6;">
					<a href="javascript:addTab('用户管理','${path}/sys/user/manager.do','menu_icon_datadeal')">
						<span class="menu_icon_datadeal"></span>
						<span>用户管理</span>
					</a>
				</div>
				<div class="nav-item" style="margin-top:2px;margin-bottom:2px;border-bottom:1px solid #d6d6d6;">
					<a href="javascript:addTab('部门管理','${path}/sys/organization/manager.do','menu_icon_datadeal')">
						<span class="menu_icon_datadeal"></span>
						<span>部门管理</span>
					</a>
				</div>
				<div class="nav-item" style="margin-top:2px;margin-bottom:2px;border-bottom:1px solid #d6d6d6;">
					<a href="javascript:addTab('角色管理','${path}/sys/role/manager.do','menu_icon_datadeal')">
						<span class="menu_icon_datadeal"></span>
						<span>角色管理</span>
					</a>
				</div>
				<div class="nav-item" style="margin-top:2px;margin-bottom:2px;border-bottom:1px solid #d6d6d6;">
					<a href="javascript:addTab('资源管理','${path}/sys/resource/manager.do','menu_icon_datadeal')">
						<span class="menu_icon_datadeal"></span>
						<span>资源管理</span>
					</a>
				</div>
			</div>
			</shiro:hasPermission>
			<!-- crm manager begin -->
			<!-- crm manager endxx -->
			<shiro:hasPermission name="/sysLog/root.do">
			<div title="日志管理" style="overflow: auto;">
				<div class="nav-item" style="margin-top:2px;margin-bottom:2px;border-bottom:1px solid #d6d6d6;">
					<a href="javascript:addTab('操作日志','${path}/sys/sysLog/manager.do','menu_icon_datadeal')">
						<span class="menu_icon_datadeal"></span>
						<span>操作日志</span>
					</a>
				</div>
			</div>
			</shiro:hasPermission>
		</div>

	</div>
	<div data-options="region:'center'" style="overflow: auto;">
		<div id="index_tabs" style="overflow: hidden;">
			<div title="首页" data-options="border:false" style="overflow: hidden; text-align: top; padding:0em;;">
				<img src="${base}/static/images/main.jpg" width="100%"/>
			</div>
		</div>
	</div>
	<div data-options="region:'south',border:false" style="height: 30px;line-height:30px; overflow: hidden;text-align: center;background-color: #eee" >Copyright © 2017 power by 广东移动</div>
</div>

<div id="tabsMenu" class="easyui-menu" style="width:120px;">  
	<div name="close">关闭</div>  
	<div name="Other">关闭其他</div>  
	<div name="All">关闭所有</div>
</div> 
    <!--[if lte IE 7]>
    <div id="ie6-warning"><p>您正在使用 低版本浏览器，在本页面可能会导致部分功能无法使用。建议您升级到 <a href="http://www.microsoft.com/china/windows/internet-explorer/" target="_blank">Internet Explorer 8</a> 或以下浏览器：
    <a href="http://www.mozillaonline.com/" target="_blank">Firefox</a> / <a href="http://www.google.com/chrome/?hl=zh-CN" target="_blank">Chrome</a> / <a href="http://www.apple.com.cn/safari/" target="_blank">Safari</a> / <a href="http://www.operachina.com/" target="_blank">Opera</a></p></div>
    <![endif]-->

    <style>
        /*ie6提示*/
        #ie6-warning{width:100%;position:absolute;top:0;left:0;background:#fae692;padding:5px 0;font-size:12px}
        #ie6-warning p{width:960px;margin:0 auto;}
    </style>
</body>
</html>