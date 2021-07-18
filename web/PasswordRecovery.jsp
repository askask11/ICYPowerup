<%-- 
    Document   : PasswordRecovery
    Created on : May 26, 2021, 6:57:07 AM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>
            密码找回
        </h1>
        <p>
            忘记密码可以通过以下步骤找回。
        </p>
        
        ${message}
        
        <form method="POST" action="PasswordRecovery2" accept-charset="utf-8" onsubmit="f();">
            请输入你的邮箱以继续密码找回：
            <input type="email" required placeholder="你的注册邮箱" name="email" size="25">
            <br><img src="Captcha" onclick="this.src='Captcha'" alt="captcha" title="看不清？换一张" style="cursor: pointer;">
            <input type="text" required placeholder="图形验证码" name="captcha">
            <button type="submit" id="ssss">
                确定
            </button>
        </form>
        <script>
            function f()
            {
                document.getElementById("ssss").disabled=true;
                document.getElementById("ssss").innerHTML="请稍等...";
            }
        </script>
    </body>
</html>
