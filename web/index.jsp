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
                    比如主页飘下雪花，自定义的主页，（未来计划）背景音乐，收片动画，多彩站内信，智能活动管理，一键搜片友，
                    明信片防重，以及自助过期补登等炫酷又实用的功能！目前正在内部测试，只有获得资格才能使用哦！<br><!-- comment -->
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
                    目前处于内测阶段。
                    请<a href="https://www.icardyou.icu/userInfo/homePage?userId=32364" target="_blank">站内信Johnson</a>,
                    或联系Johnson的电子邮件vip@jianqinggao.com以获得账号。
                    获取账号后，根据提示绑定自己的ICY账号即可实现各种炫酷的功能啦！<br>
                    *部分功能可能不稳定
                </p>
            </section>
            <br><!-- comment -->
            <!--<section>
                <h4>
                    使用ICY Powerup会有白马王子降临吗？
                </h4>
                <p>
                    此工具时不提供任何担保，保证。 当然，如果你使用了此工具以后，你会变得像柚子酱一样与众不同，吸引白马王子的几率也会随之增加。但是我们对
                    此不提供任何保证，也不提供售后服务。此工具的代码只代表作者的原意，如果你问我为什么还没有白马王子，别怪我
                    实际上那是火星人的错，是他们把白马王子绑架了，呜呜呜~<br>
                    那柚子酱使用了此工具呢？这就得看柚子酱了. 但肯定的是，她还是比你们更与众不同。<br>
                    如果对此感觉很sad，那就哭吧哭吧不是罪，<br>
                </p>
            </section>-->
        </div>
    </body>
</html>
