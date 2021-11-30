<%-- 
    Document   : ManagePanel
    Created on : May 9, 2021, 10:25:41 PM
    Author     : jianqing
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height: 100%;">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>管理我的个性化插件</title>
        <c:if test="${empty user}">
            <meta http-equiv="refresh" content="0; url=login.jsp" />
        </c:if>
        <%@include file="/WEB-INF/jspf/head.jspf" %>

        <link rel="stylesheet" href="css/ManagePanel.css"/>

        <script src="https://cdn.jsdelivr.net/gh/isocra/TableDnD@master/dist/jquery.tablednd.1.0.5.min.js"></script>
        <script src="https://www.recaptcha.net/recaptcha/api.js?render=6LdSILoZAAAAAF2f6ntG72Dj2gG2jNEqg8Q3Gpjh" async defer></script>
    </head>
    <body style="height:100%;">
        <%@include file="/WEB-INF/jspf/nav.jspf" %>
        <br>
        <br>
        <br>
        <div class="container translucent rounded" style="padding:23px;">
            <br>
            <h3>
                欢迎，<c:out value="${user.getUsername()}"></c:out>
                </h3>

                <br>

            <c:if test="${!user.isIcyVerified()}">
                <%@include file="/WEB-INF/jspf/ManagePanel/bondicyaccount.jspf" %>
            </c:if>

            <c:if test="${user.isIcyVerified()}">
                您已绑定ICY账号， 您可<a href="https://www.icardyou.icu/userInfo/homePage?userId=${user.getIcyid()}" target="_blank">转到ICY账户主页</a> 或 <a href="javascript:deregisterICY();">解除绑定</a>
                <br><br><br>

                <!--主页功能选项：特效还是防重-->
                <ul class="nav nav-tabs" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" data-toggle="tab" href="#home">主页特效</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="#menu1">一键防重</a>
                    </li>
                </ul>

                
                <!-- 选项下的功能操作区 -->
                <div class="tab-content">
                    
                    <!--Page Effect-->
                    <%@include file="/WEB-INF/jspf/ManagePanel/pageeffect-setting.jspf" %>


                    <!-- Postcard Searching Function -->
                    <%@include file="/WEB-INF/jspf/ManagePanel/searchcards.jspf" %>


                </div>
            </div>

            <br>

        </c:if>



        <input type="hidden" value="${user.getId()}" id="userid">
        <input type="hidden" value="${user.getUsername()}" id="username">     





        <script src="js/indexSettings.js"></script>
        <script src="js/searchCards.js"></script>
        <c:if test="${user.getToken()!=null}">
            <script>
            localStorage.setItem("token", "${user.getToken()}");
            </script>
        </c:if>
    </body>
</html>
