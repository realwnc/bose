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
	<script src="${staticpath}/js/jquery.md5.js"></script>
	<script src="${staticpath}/plugin/encrypt/js/rollups/aes.js"></script>
	<script src="${staticpath}/plugin/encrypt/js/components/mode-ecb.js"></script>
    <style>
        body {
            background: #ebebeb;
            font-family: "Helvetica Neue", "Hiragino Sans GB", "Microsoft YaHei", "\9ED1\4F53", Arial, sans-serif;
            color: #222;
            font-size: 12px;
        }
        * {
            padding: 0px;
            margin: 0px;
        }
        
        .ipt {
            border: 1px solid #d3d3d3;
            padding: 10px 10px;
            width: 290px;
            border-radius: 4px;
            padding-left: 35px;
            -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
            -webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
            -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
            transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s
        }
        .ipt:focus {
            border-color: #66afe9;
            outline: 0;
            -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6)
        }
        .u_logo {
            background: url("${viewpath}/sys/images/login/username.png") no-repeat;
            padding: 10px 10px;
            position: absolute;
            top: 43px;
            left: 40px;

        }
        .p_logo {
            background: url("${viewpath}/sys/images/login/password.png") no-repeat;
            padding: 10px 10px;
            position: absolute;
            top: 23px;
            left: 40px;
        }
        a {
            text-decoration: none;
        }
        .tou {
            background: url("${viewpath}/sys/images/login/tou.png") no-repeat;
            width: 97px;
            height: 92px;
            position: absolute;
            top: -87px;
            left: 140px;
        }
        .left_hand {
            background: url("${viewpath}/sys/images/login/left_hand.png") no-repeat;
            width: 32px;
            height: 37px;
            position: absolute;
            top: -38px;
            left: 150px;
        }
        .right_hand {
            background: url("${viewpath}/sys/images/login/right_hand.png") no-repeat;
            width: 32px;
            height: 37px;
            position: absolute;
            top: -38px;
            right: -64px;
        }
        .initial_left_hand {
            background: url("${viewpath}/sys/images/login/hand.png") no-repeat;
            width: 30px;
            height: 20px;
            position: absolute;
            top: -12px;
            left: 100px;
        }
        .initial_right_hand {
            background: url("${viewpath}/sys/images/login/hand.png") no-repeat;
            width: 30px;
            height: 20px;
            position: absolute;
            top: -12px;
            right: -112px;
        }
        .left_handing {
            background: url("${viewpath}/sys/images/login/left-handing.png") no-repeat;
            width: 30px;
            height: 20px;
            position: absolute;
            top: -24px;
            left: 139px;
        }
        .right_handinging {
            background: url("${viewpath}/sys/images/login/right_handing.png") no-repeat;
            width: 30px;
            height: 20px;
            position: absolute;
            top: -21px;
            left: 210px;
        }
		.v_logo {
            background: url("${viewpath}/sys/images/login/password.png") no-repeat;
            padding: 10px 10px;
            position: absolute;
            top: 22px;
            left: 40px;
        }
		.valcode {
            border: 1px solid #d3d3d3;
            padding: 10px 10px;
            width: 100px;
            border-radius: 4px;
            padding-left: 35px;
            -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
            -webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
            -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
            transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s
        }
        .valcode:focus {
            border-color: #66afe9;
            outline: 0;
            -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6)
        }
    </style>
    <script type="text/javascript">
    if (window != top) 
    	top.location.href = location.href; 
    
        $(function () {
            // 得到焦点
            $("#password").focus(function () {
                $("#left_hand").animate({
                    left: "150",
                    top: " -38"
                }, {
                    step: function () {
                        if (parseInt($("#left_hand").css("left")) > 140) {
                            $("#left_hand").attr("class", "left_hand");
                        }
                    }
                }, 2000);
                $("#right_hand").animate({
                    right: "-64",
                    top: "-38px"
                }, {
                    step: function () {
                        if (parseInt($("#right_hand").css("right")) > -70) {
                            $("#right_hand").attr("class", "right_hand");
                        }
                    }
                }, 2000);
            });
            // 失去焦点
            $("#password").blur(function () {
                $("#left_hand").attr("class", "initial_left_hand");
                $("#left_hand").attr("style", "left:100px;top:-12px;");
                $("#right_hand").attr("class", "initial_right_hand");
                $("#right_hand").attr("style", "right:-112px;top:-12px");
            });
            // 登录
            $('#loginform').form({
                url:'${path}/sys/login.do',
                onSubmit : function(param) {
                    progressLoad();
                    var isValid = $(this).form('validate');
                    if(!isValid){
                        progressClose();
                    }
                    return isValid;
                },
                success:function(result){
                    progressClose();
                    result = $.parseJSON(result);
					//alert(result);
                    if (result.success) {
                        top.location.href='${path }/sys/index.do';
                    }else{
                         $.messager.show({
                            title:'提示',
                            msg:'<div class="light-info"><div class="light-tip icon-tip"></div><div>'+result.msg+'</div></div>',
                            showType:'show'
                        });
                    }
                }
            });

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
<body onkeydown="enterlogin();">
<div style="background: rgb(255, 255, 255); margin: 100px auto auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px; height: 250px; text-align: center;">
    <form method="post" id="loginform">
		<input type="hidden" id="csrftoken" name="csrftoken" value="${csrftoken}" />
        <div style="width: 165px; height: 126px; position: absolute;">
            <div class="tou"></div>
            <div class="initial_left_hand" id="left_hand"></div>
            <div class="initial_right_hand" id="right_hand"></div>
        </div>
        <div style="padding: 30px 0px 10px; position: relative;">
            <span class="u_logo"></span>
            <input class="ipt" type="text" id="username" name="username" placeholder="请输入用户名或邮箱" value="" />
        </div>
        <div style="padding: 10px 0px 10px; position: relative;">
            <span class="p_logo"></span>
            <input class="ipt" id="password" type="password" name="password" placeholder="请输入密码" value="" />
        </div>		
		<div style="padding: 10px 0px 10px; position: relative;">
            <span class="v_logo"></span>
			<div style="text-align: left; margin-left: 30px;">
				<input class="valcode" type="text" id="valcode" name="valcode" placeholder="请输入验证码" value="" />			
				<img type="image" src="${path}/sys/captcha.do" id="captchaImage" title="刷新" style="cursor:pointer;text-align: left; margin-left: 10px;position: absolute;margin-top: 2px;"/>
			</div>
        </div>
        <div style="height: 40px; line-height: 40px; margin-top: 10px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
            <P style="margin: 0px 35px 20px 45px;">
                <span style="float: left;">
                    <!--<a style="color: rgb(204, 204, 204);" href="javascript:;">忘记密码?</a>-->
                </span>
                <span style="float: right;">
                    <!--<a style="color: rgb(204, 204, 204); margin-right: 10px;" href="javascript:;">注册</a>-->
                    <a style="background: rgb(0, 142, 173); padding: 7px 20px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;" href="javascript:;" onclick="submitForm()">登录</a>
                </span>
            </P>
        </div>
    </form>
</div>
<div style="text-align:center;">
	copyright by wncheng
</div>
</body>
</html>
