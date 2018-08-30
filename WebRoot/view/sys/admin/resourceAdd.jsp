<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {

        $('#pid').combotree({
            url : '${path }/sys/resource/showAllTree.do',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto'
        });

        $('#resourceAddForm').form({
            url : '${path }/sys/resource/add.do',
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
                    parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为resource.jsp页面预定义好了
                    //parent.layout_west_tree.tree('reload');
                    parent.$.modalDialog.handler.dialog('close');
                }
            }
        });
        
    });
</script>
<div style="padding: 3px;">
    <form id="resourceAddForm" method="post">
        <table class="i_table">
            <tr>
                <td align="right">资源名称</td>
                <td><input name="name" type="text" placeholder="请输入资源名称" class="easyui-validatebox span2" data-options="required:true" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                <td align="right">资源类型</td>
                <td><select name="resourceType" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd">
                            <option value="0">菜单</option>
                            <option value="1">按钮</option>
                </select></td>
            </tr>
            <tr>
                <td align="right">资源路径</td>
                <td><input name="url" type="text" placeholder="请输入资源路径" class="easyui-validatebox span2" data-options="" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd"></td>
                <td align="right">排序</td>
                <td><input name="seq" value="0"  class="easyui-numberspinner" style="width: 140px; height: 25px;" required="required" data-options="editable:false"></td>
            </tr>
            <tr>
                <td align="right">菜单图标</td>
                <td><input  name="icon" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd"/></td>
                <td align="right">状态</td>
                <td ><select name="status" class="easyui-combobox" data-options="width:140,height:25,editable:false,panelHeight:'auto'">
                            <option value="0">正常</option>
                            <option value="1">停用</option>
                </select></td>
            </tr>
            <tr>
                <td align="right">上级资源</td>
                <td colspan="3"><select id="pid" name="pid" style="width: 160px;height:25px; margin: 1px;border-radius: 5px; border: 1px solid #cdcdcd"></select>
                <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#pid').combotree('clear');" >清空</a></td>
            </tr>
        </table>
    </form>
</div>