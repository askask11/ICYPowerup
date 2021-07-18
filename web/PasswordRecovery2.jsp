<%-- 
    Document   : PasswordRecovery2
    Created on : May 26, 2021, 7:34:34 AM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--JQuery-->
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/gh/placemarker/jQuery-MD5@master/jquery.md5.js"></script>
        <title>ICY Powerup-密码找回第二页</title>
    </head>
    <body>
        <br>
        ${message}
        您好，
        <form action="PasswordRecovery3" method="POST" accept-charset="UTF-8" onsubmit="return onsub()">
            请输入邮箱验证码：<input autocomplete="off" type="text" placeholder="邮箱验证码" required name="verify"><br>
            请输入新的密码：<input autocomplete="off" type="password" placeholder="密码" required id="pw1" name="password"><br>
            请再输入一次密码：<input autocomplete="off" type="password" placeholder="密码" required id="pw2"><br>
            <button type="submit" id="btbt">
                确认
            </button>
        </form>
        <script>
            function onsub()
            {
                const pw1dom = document.getElementById("pw1");
                const pw2dom = document.getElementById("pw2");
                
                if(pw1dom.value !== pw2dom.value)
                {
                    alert("前后密码不同，请检查并重新输入。");
                    return false;
                }
                
                pw1dom.value = $.md5(pw1dom.value);
                
                $("#btbt")[0].disabled = true;
                $("#btbt").html("请稍等...");
                return true;
            }
        </script>
        
    </body>
</html>
