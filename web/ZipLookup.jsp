<%-- 
    Document   : ZipLookup
    Created on : Jul 20, 2021, 8:31:06 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="/WEB-INF/jspf/head.jspf" %>
        <title>邮编查询</title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/nav.jspf" %>
        <br><br><!-- comment -->
        <h1 class="text-center">邮编查询</h1>
        <br>
        <div class="container text-center">
             键入地址格式：省+市+区+街道+门牌号
        
        <br>
        <input type="text" class="form-control">
        <br>
        <button class="btn btn-primary">
            查询
        </button>
            
           
        </div>
       
    </body>
</html>
