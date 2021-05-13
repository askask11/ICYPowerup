<%-- 
    Document   : login
    Created on : May 9, 2021, 8:13:49 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:100%;">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>登录ICY Powerup</title>
        <%@include file="/WEB-INF/jspf/head.jspf" %>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/askask11/weblib@0.1-beta.1/css/animated-field.css" integrity="sha256-iEJJ2kYJy729hQJtUHrgdmjURlghCZaQyLl9nPLmdIo=" crossorigin="anonymous"><!-- comment -->
        
        <script src="https://cdn.jsdelivr.net/gh/placemarker/jQuery-MD5@master/jquery.md5.js"></script>

        <style>
            body
            {
                background-image:  url(https://xeduocdn.sirv.com/Images/aesthetic-anime-01.jpg);
            }
            .translucent
            {
                background-color: rgba(255,255,255,0.85);
            }

            .centralized
            {
                margin: auto;
            }

            .footer
            {
                position: fixed;
                bottom: 0;
                width: 100%;
            }

        </style>
        ${user==null?"":"<meta http-equiv='refresh' content='0; url=ManagePanel'>"}
    </head>
    <body style="height:100%;">
        <%@include file="/WEB-INF/jspf/nav.jspf" %>
        <br>
        <br>
        <div class="container text-center">


            <div class="centralized rounded translucent text-center">
                <br>
                <h1>欢迎</h1>
                <br><!-- comment -->

                <!--<p style="color:saddlebrown;"> 继续登录，拥有炫酷实用的特效</p>-->

                <br><!-- login -->

                <button class="btn btn-primary" id="contbtn" style="display: none;">
                    继续
                </button>
                <div id="loginArea" style="" class="">

                    请登录以继续使用（不是ICY的账户！）
                    <form>
                        <div id="f1" class="animated-text-input-container text-center" style="margin: auto;">
                            <input id="username" type="text" required title="user" name="username">
                            <label class="label-name"><span class="content-name">用户名</span></label>
                        </div>
                        <div id="f2" class="animated-text-input-container text-center" style="margin: auto;">
                            <input id="password" type="password" required title="pass" name="password">
                            <label class="label-name"><span class="content-name">密码</span></label>
                        </div>
                        <button class="btn btn-success" id="loginbtn" type="button" onclick="login();">
                            登录
                        </button><br><br>
                        关于账户，请<a href="">站内信Johnson注册</a><br><!-- kaifa -->
                        <a href="javascript:developer()">我是开发者</a>
                    </form>

                    <br><!-- comment -->

                </div>

                <div id="waitingArea" class="text-center" style="display: none;">
                    <div>请稍等，正在登录...</div>
                    <div class="lds-roller"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>
                </div>
            </div>
        </div>
        <script>
            function login()
            {
                //get variables
                var pass = $.md5($("#password")[0].value);

                var user = $("#username")[0].value;

                ///check for empty submission
                if (user === "")
                {
                    Swal.fire({
                        title: "用户名为空",
                        text: "用户名为必填",
                        icon: "error"
                    });
                    return;
                }
                if (pass === "d41d8cd98f00b204e9800998ecf8427e")
                {
                    Swal.fire({
                        title: "密码为空",
                        text: "密码为必填",
                        icon: "error"
                    });
                    return;
                }

                //fade things in
                $("#loginArea").fadeOut("slow", () => {
                    $("#waitingArea").fadeIn("slow");
                })
                
                //post user login data.
                $.post("doLogin", {username: user, password: pass}, (data, status, xhr) => {
                    console.log("noo");
                    var jsono = data;
                    if (jsono["code"] === "OK")
                    {
                        window.location = "ManagePanel";
                        return;
                    }
                    //ooops, an error occured.
                    $("#loginArea").fadeIn("slow", () => {
                        $("#waitingArea").fadeOut("slow");
                    });
                    if (jsono["code"] == "NoSuchUser")
                    {
                        Swal.fire({
                            title: '用户名或密码错误',
                            text: '请检查您的用户信息',
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                        return;
                    }

                    //other unknown errors
                    Swal.fire({
                        title: '服务器错误',
                        text: data,
                        icon: 'error',
                        confirmButtonText: '去报告'
                    });

                }, "json").fail((resp) => {
                    Swal.fire({
                        title: '服务器错误',
                        text: resp.responseText,
                        icon: 'error',
                        confirmButtonText: '去报告'
                    });
                });

            }

            function developer()
            {
                Swal.fire(
                        '感谢您的兴趣',
                        '欢迎各位技术大佬的宝贵意见<br>该项目即将开源，敬请期待！<br>若在使用本应用中发现存在的不足与漏洞，烦请联系vip@jianqinggao.com反馈，十分感谢！',
                        'info'
                        );
            }
        </script>
        <br><br><br>
        <footer class="text-center footer translucent">
            <small style="bottom: 0;">
                声明：此网站是Johnson为了方便大家管理活动自研的网站，不收集关于ICY账户的任何其它信息。ICY官方和本网站并无合作关系。背景图片来源网络，侵删。<br><!-- comment -->
                Johnson 2021 <a href="http://beian.miit.gov.cn">粤ICP备2020113241号</a>
            </small>

        </footer>

    </body>
</html>
