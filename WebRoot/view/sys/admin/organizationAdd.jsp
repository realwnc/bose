<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">

    $(function() {
        
        $('#pid').combotree({
            url : '${path }/sys/organization/showTree.do',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto'
        });
        
        $('#organizationAddForm').form({
            url : '${path }/sys/organization/add.do',
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
                    parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为organization.jsp页面预定义好了
                    parent.$.modalDialog.handler.dialog('close');
                }
            }
        });
        
    });
</script>
<div style="padding: 3px;">
    <form id="organizationAddForm" method="post">
        <table class="i_table">
            <tr>
                <td align="right">编号</td>
                <td><input name="codex" type="text" placeholder="请输入部门编号" class="easyui-validatebox" data-options="required:true,validType:['string', 'length[1,64]']" style="width: 80px;height:25px; margin: 5px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                <td align="right">部门名称</td>
                <td><input name="name" type="text" placeholder="请输入部门名称" class="easyui-validatebox" data-options="required:true,validType:['string', 'length[1,64]']" style="width: 200px;height:25px; margin: 5px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                
            </tr>
            <tr>
                <td align="right">排序</td>
                <td><input name="seq"  class="easyui-numberspinner" style="width: 100px;height:25px; margin: 5px;border-radius: 5px; border: 1px solid #cdcdcd" required="required" data-options="editable:false" value="0"></td>
                <td align="right">菜单图标</td>
                <td><input  name="icon" value="icon-folder" style="width: 150px;height:25px; margin: 5px;border-radius: 5px; border: 1px solid #cdcdcd"/></td>
            </tr>
            <tr>
                <td align="right">地址</td>
                <td colspan="3"><input name="address" style="width: 300px;height:25px; margin: 5px;border-radius: 5px; border: 1px solid #cdcdcd"/></td>
            </tr>
            <tr>
                <td align="right">上级部门</td>
                <td colspan="3"><select id="pid" name="pid" style="width:200px;height: 25px;"></select>
                <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#pid').combotree('clear');" >清空</a></td>
                
            </tr>
        </table>
    </form>
</div>