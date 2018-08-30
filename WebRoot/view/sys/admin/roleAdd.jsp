<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#roleAddForm').form({
            url : '${path }/sys/role/add.do',
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
                    parent.$.messager.alert('错误', result.msg, 'error');
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false" >
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;" >
        <form id="roleAddForm" method="post">
            <table class="i_table">
                <tr>
                    <td align="right">角色名称</td>
                    <td><input name="name" type="text" placeholder="请输入角色名称" class="easyui-validatebox span2" data-options="required:true,validType:['string', 'length[1,64]']" value="" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                </tr>
                <tr>
                    <td align="right">排序</td>
                    <td><input name="seq" value="0" class="easyui-numberspinner" style="width: 140px; height: 25px;" required="required" data-options="editable:false" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                </tr>
                <tr>
                    <td align="right">状态</td>
                    <td>
                        <select id="statusx" name="statusx" class="easyui-combobox" data-options="width:120,height:25,editable:false,panelHeight:'auto'">
                                    <option value="0">正常</option>
                                    <option value="1">停用</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">备注</td>
                    <td colspan="3"><input id="description" name="description" class="easyui-textbox" data-options="multiline:true,required:false" style="width:320px;height:50px"></td>
                </tr>
            </table>
        </form>
    </div>
</div>