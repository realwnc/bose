<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户登录</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">
    <%@ include file="/commons/basejs.jsp" %>
	<link rel="stylesheet" href="${staticpath}/plugin/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<script src="${staticpath}/js/jquery.md5.js"></script>
	<script src="${staticpath}/plugin/encrypt/js/rollups/aes.js"></script>
	<script src="${staticpath}/plugin/encrypt/js/components/mode-ecb.js"></script>
    <style>
        body{color:#fff; font-family:"微软雅黑"; font-size:14px;}
		.wrap1{position:absolute; top:0; right:0; bottom:0; left:0; margin:auto }/*把整个屏幕真正撑开--而且能自己实现居中*/
        //.main_content{background:url(images/main_bg.png) repeat; margin-left:auto; margin-right:auto; text-align:left; float:none; border-radius:8px;}
        .main_content{background: transparent; border: 1px solid; margin-left:auto; margin-right:auto; text-align:left; float:none; border-radius:8px;}
		.form-group{position:relative;}
		.login_btn{display:block; background:#3872f6; color:#fff; font-size:15px; width:100%; line-height:45px; border-radius:3px; border:none; }
		.login_input{width:100%; border:1px solid #3872f6; border-radius:3px; line-height:35px; padding:2px 5px 2px 30px; background:none;}
		.icon_font{position:absolute; bottom:15px; left:10px; font-size:18px; color:#3872f6;}
        .font16{font-size:16px;}
        .font24{font-size:24px;}
		.mg-t20{margin-top:20px;}
		@media (min-width:200px){.pd-xs-20{padding:10px;}}
		@media (min-width:768px){.pd-sm-50{padding:30px;}}
		#grad {
		  background: -webkit-linear-gradient(#4990c1, #52a3d2, #6186a3); /* Safari 5.1 - 6.0 */
		  background: -o-linear-gradient(#4990c1, #52a3d2, #6186a3); /* Opera 11.1 - 12.0 */
		  background: -moz-linear-gradient(#4990c1, #52a3d2, #6186a3); /* Firefox 3.6 - 15 */
		  background: linear-gradient(#4990c1, #52a3d2, #6186a3); /* 标准的语法 */
		}
        
    </style>
    <script type="text/javascript">
    if (window != top) 
    	top.location.href = location.href; 
    
        $(function () {
			$('#captchaImage').click(function() 
			{
				$('#captchaImage').attr("src", "${path}/sys/captcha.do?timestamp=" + (new Date()).valueOf());
			});
        });

        function submitForm(){
			var isValid = $('#loginform').form('validate');
			if(!isValid){
				return;
			}
			progressLoad();
			var csrftoken = $("#csrftoken").val();
			var valcode = $("#valcode").val();
			var password = $("#password").val();
			var username = $("#username").val();
			password = $.md5(username+password);
			
			username = Encrypt(username);
			$.post('${path}/sys/login.do', {				
				username : username,
				password : password,
				valcode : valcode,
				csrftoken : csrftoken
			}, function(result) {
				progressClose();
				if (result.success) {
					top.location.href='${path}/sys/index.do';
				}else{
					 $.messager.show({
						title:'提示',
						msg:'<div class="light-info"><div class="light-tip icon-tip"></div><div>'+result.msg+'</div></div>',
						showType:'show'
					});
				}
			}, 'JSON');
            //$('#loginform').submit();
        }

        function clearForm(){
            $('#loginform').form('clear');
        }
        //回车登录
        function enterlogin(){
            if (event.keyCode == 13){
                event.returnValue=false;
                event.cancel = true;
                //$('#loginform').submit();
				submitForm();
            }
        }
		var aeskey = '${aeskey}';
		function Encrypt(word){  
			var key = CryptoJS.enc.Utf8.parse(aeskey);//"abcdefgabcdefg12" 
  
			var srcs = CryptoJS.enc.Utf8.parse(word);  
			var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});  
			return encrypted.toString();  
		}  
		function Decrypt(word){  
			var key = CryptoJS.enc.Utf8.parse(aeskey);//"abcdefgabcdefg12" 
	  
			var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});  
			return CryptoJS.enc.Utf8.stringify(decrypt).toString();  
		}
		
    </script>
</head>
<body onkeydown="enterlogin();" style="background:url(images/bg.jpg) no-repeat;">
	<div class="container wrap1" style="height:450px;">
		<h2 class="mg-b20 text-center">管理平台登录</h2>
		<div class="col-sm-8 col-md-5 center-auto pd-sm-50 pd-xs-20 main_content">
			<p class="text-center font24">用户登录</p>
			<form method="post" id="loginform">
				<input type="hidden" id="csrftoken" name="csrftoken" value="${csrftoken}" />
				<div class="form-group mg-t20">
					<i class="icon-user icon_font"></i>
					<input type="email" class="login_input" id="username" name="username" placeholder="请输入用户名" />
				</div>
				<div class="form-group mg-t20">
					<i class="icon-lock icon_font"></i>
					<input type="password" class="login_input" id="password" name="password" placeholder="请输入密码" />
				</div>
				<div class="form-group mg-t20">
					<i class="icon-flow icon_font"></i>
					<input type="text" class="login_input" id="valcode" name="valcode" placeholder="请输入验证码" style="width: 50%"/>
					<img type="image" src="${path}/sys/captcha.do" id="captchaImage" title="刷新" style="height:100%; cursor:pointer;text-align: left; "/>
				</div>

				<div style="height: 40px; line-height: 40px; margin-top: 10px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
					<span style="float: left;">
                        <!--<a style="color: rgb(204, 204, 204);" href="javascript:;">忘记密码?</a>-->
                    </span>
                    <span style="float: right;">
                        <!--<a style="color: rgb(204, 204, 204); margin-right: 10px;" href="javascript:;">注册</a>-->
                        <a style="background: rgb(0, 142, 173); padding: 7px 20px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;" href="javascript:;" onclick="submitForm()">登录</a>
                    </span>
				</div>
			</form>
        </div><!--row end-->
        <div style="text-align:center;">
            copyright by wncheng
        </div>
	</div><!--container end-->


    


</body>
</html>
