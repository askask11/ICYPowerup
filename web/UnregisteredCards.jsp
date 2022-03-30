<%-- 
    Document   : UnregisteredCards
    Created on : Mar 29, 2022, 7:40:40 PM
    Author     : jianqing
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/WEB-INF/jspf/head.jspf" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>查看未登记卡片</title>
        <c:if test="${empty user}">
            <meta http-equiv="refresh" content="0; url=login.jsp" />
        </c:if>
        <link rel="stylesheet" href="css/ManagePanel.css"/>

    </head>
    <body>
        <%@include file="/WEB-INF/jspf/nav.jspf" %>
        <br>
        <br>
        <div class="container translucent rounded">
            <br><br>
            <h3>
                我的收件箱
            </h3>
            <br>
            <h4>你好, ${user.getUsername()}， 欢迎使用！</h4>
            <br>
            报告生成时间: 2022-03-29 20:00,
            <br>以下是在途中和未发送的明信片：
            <br>

            <br>
            <table class="table table-striped">

                <thead>
                <td>
                    发件人
                </td>
                <td>
                    发件时间
                </td>
                <td>
                    ID
                </td>
                <td>
                    状态
                </td>
                <!--<td>
                    操作
                </td>-->
                </thead>
                <tbody id="tableBody">
                    <tr>
                        <td>
                            Johnson Gao
                        </td>
                        <td>
                            2003-04-08 21:30
                        </td>
                        <td>
                            CNJJ2021
                        </td>
                        <td>
                            在途
                        </td>
                        <!--<td>
                            一键登记 | 拜了个拜 | gun
                        </td>-->
                    </tr>
                </tbody>
            </table>
            <br>
        </div>

        <script src="js/UnregisteredCards.js"></script> 
    </body>
</html>
