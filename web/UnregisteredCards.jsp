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
                待登记明信片
            </h3>
            <br>
            <h4>你好, ${user.getUsername()}, 欢迎使用!</h4>
            <br>
            <button class="btn btn-outline-primary" onclick="submitTask();" id="genNewReport">
                生成ICY待登记明信片报告
            </button>


            <br>
            <br>
            <div class="card " style='background-color: rgba(255,255,255,0.1); display: none;' id='reportProgressCard'>
                <div class='card-body'>
                    您有一份正在生成的报告，
                    报告生成进度：<span id="reportPercentage"></span>
                    <div class="progress">
                        <div id="progressBar" class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar"  style="width: 2%"></div>
                    </div><!-- comment -->
                    
                    <a href="javascript:cancelConfirm()">取消</a>
                </div>
            </div>
            <br>以下是暂未被您登记的明信片<span id="totalCards"></span>上次缓存时间:<span id="time">暂无</span>
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
                            Johnson
                        </td>
                        <td>
                            2003-04-08 21:30
                        </td>
                        <td>
                            FNNA0170
                        </td>
                        <td>
                            已过期
                        </td>
                        <!--<td>
                            忽略
                        </td>-->
                    </tr>
                </tbody>
            </table>
            <br>
        </div>

        <script src="js/UnregisteredCards.js"></script> 
    </body>
</html>
