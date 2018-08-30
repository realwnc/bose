<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {

        $('#editUserPwdForm').form({
            url : '${path }/sys/user/editUserPwd.do',
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
                    parent.$.messager.alert('提示', result.msg, 'info');
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.msg, 'error');
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;">
            <form id="editUserPwdForm" method="post">
                <table class="i_table">
                    <tr>
                        <th align="right">登录名：</th>
                        <td><shiro:principal></shiro:principal></td>
                    </tr>
                    <tr>
                        <th align="right">原密码：</th>
                        <td><input name="oldPwd" type="password" placeholder="请输入原密码" class="easyui-validatebox" data-options="required:true" style="width: 160px;height:22px; margin: 5px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                    </tr>
                    <tr>
                        <th align="right">新密码：</th>
                        <td><input name="pwd" type="password" placeholder="请输入新密码" class="easyui-validatebox" data-options="required:true" style="width: 160px;height:22px; margin: 5px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                    </tr>
                    <tr>
                        <th align="right">重复密码：</th>
                        <td><input name="rePwd" type="password" placeholder="请再次输入新密码" class="easyui-validatebox" data-options="required:true,validType:'eqPwd[\'#editUserPwdForm input[name=pwd]\']'" style="width: 160px;height:22px; margin: 5px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                    </tr>
                </table>
            </form>
    </div>
</div>