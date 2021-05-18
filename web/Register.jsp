<%-- 
    Document   : Register
    Created on : May 18, 2021, 9:27:26 PM
    Author     : jianqing
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>注册确认</title>
        
    </head>
    <body>
        
        
        <c:choose>
            <c:when test="${successv}">
                ${message2}
                我们刚刚向您的邮箱发送了一个验证码，
                <form target="_self" action="Register2" method="POST">
                    <label for="vi">请输入邮箱验证码：</label>
                    <input id="vi" type="text" size="6" name="verify"> 
                    <button type="submit">
                        提交
                    </button>
                </form>
                <input type="hidden" id="status" value="forverify">
            </c:when>
            <c:otherwise>
                ${message}
                <input type="hidden" id="status" value="failed">
            </c:otherwise>
        </c:choose>
        
    </body>
</html>
