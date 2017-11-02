<!-- 模仿天猫整站j2ee 教程 为how2j.cn 版权所有-->
<!-- 本教程仅用于学习使用，切勿用于非法用途，由此引起一切后果与本站无关-->
<!-- 供购买者学习，请勿私自传播，否则自行承担相关法律责任-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<script type="text/javascript">
	$(function(){
		<c:if test="${!empty msg}">
			$("span.errorMessage").html("${msg}");
			$("div.registerErrorMessageDiv").css("visibility","visible");
		</c:if>
		
		$(".registerForm").submit(function(){
			if($("#name").val().length==0) {
				$("span.errorMessage").html("用户名不能为空");
				$("div.registerErrorMessageDiv").css("visibility","visible");
				return false;
			}
			if($("#password").val().length==0) {
				$("span.errorMessage").html("密码不能为空");
				$("div.registerErrorMessageDiv").css("visibility","visible");
				return false;
			}
			if($("#repeatpassword").val().length==0) {
				$("span.errorMessage").html("确认密码不能为空");
				$("div.registerErrorMessageDiv").css("visibility","visible");
				return false;
			}
			if($("#repeatpassword").val()!=$("#password").val()) {
				$("span.errorMessage").html("密码不一致");
				$("div.registerErrorMessageDiv").css("visibility","visible");
				return false;
			}
		});
	});
</script>

<form method="post" action="foreregister" class="registerForm">
	<div class="registerErrorMessageDiv">
		<div class="alert alert-danger" role="alert">
		  <button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>
		  	<span class="errorMessage"></span>
		</div>		
	</div>
	<div class="registerDiv">
	    <table align="center" class="registerTable">
	        <tbody><tr>
	            <td class="registerTip registerTableLeftTD">设置会员名</td>
	            <td></td>
	        </tr>
	        <tr>
	            <td class="registerTableLeftTD">登录名</td>
	            <td class="registerTableRightTD"><input placeholder="会员名一旦设置成功，无法修改" name="name" id="name"> </td>
	        </tr>
	        <tr>
	            <td class="registerTip registerTableLeftTD">设置登录密码</td>
	            <td class="registerTableRightTD">登录时验证，保护账号信息</td>
	        </tr>     
	        <tr>
	            <td class="registerTableLeftTD">登录密码</td>
	            <td class="registerTableRightTD"><input type="password" placeholder="设置你的登录密码" name="password" id="password"> </td>
	        </tr>
	        <tr>
	            <td class="registerTableLeftTD">密码确认</td>
	            <td class="registerTableRightTD"><input type="password" placeholder="请再次输入你的密码" id="repeatpassword"> </td>
	        </tr>
	        <tr>
	            <td class="registerButtonTD" colspan="2">
	                <a href="#nowhere"><button>提   交</button></a>
	            </td>
	        </tr>             
	    </tbody></table>
	</div>
</form>	

