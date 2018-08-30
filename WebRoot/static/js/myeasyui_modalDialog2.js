$.modalDialog2 = function(options) {
 if ($.modalDialog2.handler == undefined) {// 避免重复弹出
var opts = $.extend({
   title : '',
   width : 840,
   height : 680,
   modal : true,
   onClose : function() {
    $.modalDialog2.handler = undefined;
    $(this).dialog('destroy');
   },
   onOpen : function() {
    // parent.$.messager.progress({
    // title : '提示',
    // text : '数据加载中，请稍后....'
    // }); 
   }
  }, options); 
  opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
  return $.modalDialog2.handler = $('<div/>').dialog(opts);
 }
};