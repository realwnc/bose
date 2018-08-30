<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {

        $('#organizationId').combotree({
            url : '${path }/sys/organization/showTree.do',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto'
        });

        $('#roleIds').combotree({
            url: '${path }/sys/role/showTree.do',
            multiple: true,
            required: true,
            panelHeight : 'auto'
        });

        $('#userAddForm').form({
            url : '${path }/sys/user/add.do',
            onSubmit : function() {
                progressLoad();
                var isValid = $(this).form('validate');
                if (!isValid) {
                    progressClose();
                }
                return isValid;
            },
            success : function(result) {
                progressClose();
                result = $.parseJSON(result);
                if (result.success) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('提示', result.msg, 'warning');
                }
            }
        });
        
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="userAddForm" method="post">
            <table class="i_table">
                <tr>
                    <td align="right">登录名</td>
                    <td><input name="username" type="text" placeholder="请输入用户名称" class="easyui-validatebox" data-options="required:true,validType:['string', 'length[1,64]']" value="" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                    <td align="right">姓名</td>
                    <td><input name="name" type="text" placeholder="请输入姓名" class="easyui-validatebox" data-options="required:true,validType:['string', 'length[1,64]']" value="" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                </tr>
                <tr>
                    <td align="right">密码</td>
                    <td><input name="password" type="password" placeholder="请输入密码" class="easyui-validatebox" data-options="required:true" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                    <td align="right">性别</td>
                    <td>
                        <select name="sex" class="easyui-combobox" data-options="width:140,height:25,editable:false,panelHeight:'auto'">
                            <option value="0" selected="selected">男</option>
                            <option value="1" >女</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">年龄</td>
                    <td><input type="text" name="age" class="easyui-numberbox" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd"/></td>
                    <td align="right">用户类型</td>
                    <td>
                        <select name="userType" class="easyui-combobox" data-options="width:140,height:25,editable:false,panelHeight:'auto'">
                            <option value="0">管理员</option>
                            <option value="1" selected="selected">用户</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">部门</td>
                    <td><select id="organizationId" name="organizationId" style="width: 140px; height: 25px;" class="easyui-validatebox" data-options="required:true"></select></td>
                    <td align="right">角色</td>
                    <td><select id="roleIds" name="roleIds" style="width: 140px; height: 25px;"></select></td>
                </tr>
                <tr>
                    <td align="right">电话</td>
                    <td>
                        <input type="text" name="phone" class="easyui-numberbox"/>
                    </td>
                    <td align="right">用户状态</td>
                    <td>
                        <select id="statusx" name="statusx" class="easyui-combobox" data-options="width:140,height:25,editable:false,panelHeight:'auto'">
                                <option value="0">正常</option>
                                <option value="1">停用</option>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>