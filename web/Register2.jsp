<%-- 
    Document   : Register2
    Created on : May 18, 2021, 10:40:12 PM
    Author     : jianqing
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Powerup注册验证</title>
    </head>
    <body>
        <c:if test="${successr}">
            <h4 style="color:green;">
                恭喜，注册成功！
            </h4>
            现在您可以使用您的账户登录Powerup 啦~<br>
            <%-- 您的用户名：${userr.get("username")}<br><!-- comment -->
            您的邮箱：${userr.get("email")}<br><!-- comment -->--%>
            祝您使用愉快！
            <input type="hidden" id="status" value="success">
        </c:if>
        <c:if test="${!successr}">
            ${message}
            <input type="hidden" id="status" value="fail2">
        </c:if>

        <c:if test="${redo}">
            <form action="Register2" method="POST">
                重新输入邮箱验证码：
                <input id="vi" required type="text" size="6" name="verify"> 
                <button type="submit">
                    提交
                </button>
            </form>
        </c:if>
    </body>
</html>
