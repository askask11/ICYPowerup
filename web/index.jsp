<%-- 
    Document   : index
    Created on : May 9, 2021, 8:16:28 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--
Author: jianqing
Date: May 9, 2021
Description: This document is created for index page
-->
<html>
    <head>
        <title>ICY Powerup</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@include file="/WEB-INF/jspf/head.jspf" %>
        <style>
            body
            {
                background-image:  url('https://xeduocdn.sirv.com/Images/aesthetic-anime-01.jpg');
            } 
            .translucent
            {
                background-color: rgba(255,255,255,0.85);
            }
            
        </style>
        ${user==null?"":"<meta http-equiv='refresh' content='0; url=ManagePanel'>"}
    </head>
    <body>
        <!-- A grey horizontal navbar that becomes vertical on small screens -->
        <%@include file="/WEB-INF/jspf/nav.jspf" %>

        <br>
        <div class="container translucent text-center">
            <br><br><!-- comment -->
            <h1>ICY Powerup</h1>
            <br>
            <br>
            <button type="button" style="width:50%; margin:auto;" class="btn btn-outline-primary btn-block" onclick="window.location='login.jsp'">开始使用</button>
            <br><br>
        </div>
        <br>
        <div class="container translucent">
            <br><!-- comment -->
            <section>
                <h4>
                    什么是ICY Powerup，能做什么？
                </h4>
                <p>
                    ICY Powerup 是一个第三方平台，为大家免费提供了一些可以在ICY网站使用的额外功能~
                    比如主页飘下雪花，自定义的主页，一键防重，过期自助补登. 目前正在测试阶段，注册账号即可参与测试哦！<br><!-- comment -->
                    更重要的是，你<strong>不需要任何编程技术！</strong><br><!-- comment -->
                    就算你是电脑白痴，你也会感觉像技术
                    大佬一样~
                </p>
            </section>
            <br><!-- comment -->
            <section>
                <h4>
                    ICY Powerup 安全吗？
                </h4>
                <p>
                    ICY Powerup 采用行业标准加密技术对您的信息加密，我们不会主动泄密, 请放心。<br>
                </p>
            </section>

            <br><!-- comment -->
            <section>
                <h4>
                    如何使用ICY Powerup?
                </h4>
                <p>
                    第一次使用可以<a href="login.jsp">点击这里</a>，选择注册账号后即可注册啦！<br><!-- comment -->
                    如果注册失败，请<a href="https://www.icardyou.icu/message/messageDetail?targetUserId=32364" target="_blank">站内信Johnson</a>
                    获取账号密码~谢谢你的支持！
                </p>
            </section>
            <br><!-- comment -->
            <!--<section>
                <h4>
                    使用ICY Powerup会有白马王子降临吗？
                </h4>
                <p>
                    此工具不提供任何担保，保证。 当然，如果你使用了此工具以后，你会变得像白马王子一样与众不同，吸引白马王子的几率也会随之增加。但是我们对
                    此不提供任何保证，也不提供售后服务。此工具的代码只代表作者的原意，如果你问我为什么还没有白马王子，别怪我
                    实际上那是火星人的错，是他们把白马王子绑架了，呜呜呜~<br>
                   、
                </p>
            </section>-->
            <c:if test="${empty user}">
                <script src="js/autologin.js"></script>
            </c:if>
        </div>
    </body>
</html>
