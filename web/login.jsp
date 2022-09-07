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
            .lds-roller div:after{background: #3399ff;}

            #registerFrame, #passwordRecoveryFrame{
                width:80%; 
                border:0px;
                height:160px;
            }

            #passwordRecoveryFrame
            {
                height: 300px;
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

                    欢迎回来，请登录以继续使用：
                    <form>
                        <div id="f1" class="animated-text-input-container text-center" style="margin: auto;">
                            <input id="username" type="text" required title="user" name="username">
                            <label class="label-name"><span class="content-name">用户名/邮箱</span></label>
                        </div>
                        <div id="f2" class="animated-text-input-container text-center" style="margin: auto;">
                            <input id="password" type="password" required title="pass" name="password">
                            <label class="label-name"><span class="content-name">密码</span></label>
                        </div><br>
                        <button class="btn btn-primary" id="loginbtn" type="button" onclick="login();">
                            登录
                        </button> <br><br>

                        记住我：
                        <label class="switch">
                            <input type="checkbox" id="toggleRemember" name="remember" checked>
                            <span class="slider round"></span>
                        </label>
                        <br>
                        <a href="javascript:toggleRegisterLogin()">没有账号？立即注册 >>></a>  <!-- comment -->  
                        <a id="djidadcs" href="javascript:passwordRecovery()">忘记密码？</a>

                    </form>

                    <!-- comment -->
                    <div id="passwordRecoveryArea" style="display: none;">

                        <iframe id="passwordRecoveryFrame" name="recoveryFrame" onload="">

                        </iframe>
                    </div>
                </div>

                <div id="registerArea" style="display:none;">
                    欢迎注册, 即刻0代码拥有美好特效：
                    <br><br>
                    <form onsubmit="return register(this);" target="registerFrame" method="POST" action="Register">
                        <div id="r1" class="animated-text-input-container text-center" style="margin: auto;">
                            <input id="rusername" autofocus autocomplete="off" type="text" required title="用户名" name="username">
                            <label class="label-name"><span class="content-name">用户名, 60字符以下</span></label>
                        </div>
                        <div id="r2" class="animated-text-input-container text-center" style="margin: auto;">
                            <input id="remail" autocomplete="on" type="email" required title="你的电子邮箱" name="email">
                            <label class="label-name"><span class="content-name">电子邮箱， 60字符以下</span></label>
                        </div>
                        <div id="r3" class="animated-text-input-container text-center" style="margin: auto;">
                            <input id="rpassword" autocomplete="off" type="password" required title="密码" name="password">
                            <label class="label-name"><span class="content-name">密码</span></label>
                        </div>
                        <img id="captcha" src="Captcha" alt="点击更换验证码" style="cursor:pointer;" title="看不清？点击换一张" onclick="this.src = 'Captcha?timestamp=' + new Date().getMilliseconds();">
                        <div id="r4" class="animated-text-input-container text-center" style="margin: auto;">
                            <input id="rcaptcha" autocomplete="off" type="text" required title="验证码" name="captcha">
                            <label class="label-name"><span class="content-name">验证码</span></label>
                        </div>


                        <button class="btn btn-primary" type="submit">
                            立即注册
                        </button>
                    </form>


                    <br> <a href="javascript:toggleRegisterLogin()">已有账号?登录>>></a>
                </div>




                <div id="registerVerifyArea" style="display:none;">

                    <iframe id="registerFrame" name="registerFrame" src="registerpre.html" onload="registerFrameOnLoad()">

                    </iframe>
                </div>



                <a href="javascript:developer()">我是开发者</a><br><!-- 来嘛 -->
                <br>

                <div id="waitingArea" class="text-center" style="display: none; ">
                    <div>请稍等，正在登录...</div>
                    <div class="lds-roller" style=""><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>
                </div>

            </div>




        </div>

        <script>
            function passwordRecovery()
            {
                var port = window.location.port;
                document.getElementById("passwordRecoveryFrame").src = "./PasswordRecovery";
                        $("#passwordRecoveryArea").fadeIn("slow");
                $("#djidadcs").fadeOut();

            }


            function registerFrameOnLoad()
            {
                const idoc = document.getElementById("registerFrame").contentDocument;
                const statusE = idoc.getElementById("status");
                if (statusE === null)
                {
                    if (idoc.getElementsByTagName("body").innerText.includes("HTTP Status"))
                    {
                        Swal.fire("未知错误", "一个未知的错误发生了", "error");
                    }
                    return;
                }


                const status = statusE.value;

                switch (status) {
                    case "success":
                        //succs
                        Swal.fire("恭喜，注册成功", "您现在可以登录了！本页面5秒后将自动刷新！感谢您注册！", "success");
                        setTimeout(() => {
                            window.location.reload();
                        }, 5000);
                        break;

                    case "failed":
                        $("#rpassword").val("");
                        $("#registerArea").fadeIn();
                        document.getElementById("captcha").src = "Captcha?d=" + new Date().getMilliseconds();//reload captcha
                        var reason = idoc.getElementById("reason").innerText;
                        if (reason === "验证码错误")
                        {
                            Swal.fire("验证码错误", "抱歉，您输入的验证码有误，请重新输入。", "warning");

                            document.getElementById("rcaptcha").value = "";

                        } else if (reason === "该邮箱已被注册")
                        {
                            Swal.fire("该邮箱已被注册", "抱歉，您注册的邮箱已被注册，请重新输入。", "warning");
                            document.getElementById("remail").value = "";
                            document.getElementById("remail").focus();

                        } else if (reason === "邮件发送失败或邮箱不合法")
                        {
                            Swal.fire("邮件问题", "邮件发送失败或邮箱不合法", "error");
                            document.getElementById("remail").focus();
                        } else if (reason === "数据库错误了")
                        {
                            Swal.fire("数据库错误", "数据库发生了错误，请稍后尝试注册", "warning");
                        }
                        break;
                    case "fail2":

                        break;

                    default:

                        break;
                }
            }

            function register(form)
            {
                var user = document.getElementById("rusername").value;
                var pass = $.md5(document.getElementById("rpassword").value);

                if (user === "" || pass === "d41d8cd98f00b204e9800998ecf8427e")
                {
                    Swal.fire("请输入用户名和密码。", "用户名或密码为空", "warning");
                    return false;//blank username / password, prevent form from submitting.
                }

                document.getElementById("rpassword").value = pass;//replace the field with MD5 password.

                $("#registerArea").fadeOut("slow");
                $("#registerVerifyArea").fadeIn("slow");
                const vs = document.createElement("link");
                vs.href = "https://cdn.jsdelivr.net/gh/askask11/weblib@master/css/lds-loader.css";
                vs.rel = "stylesheet";
                const vs2 = document.createElement("style");
                vs2.innerHTML = ".lds-roller div:after{background: #3399ff;} body{text-align:center;}";
                document.getElementById("registerFrame").contentDocument.getElementsByTagName("head")[0].appendChild(vs);
                document.getElementById("registerFrame").contentDocument.getElementsByTagName("head")[0].appendChild(vs2);
                document.getElementById("registerFrame").contentDocument.getElementsByTagName("body")[0].innerHTML = "请稍等<br><div class='lds-roller'><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>";
                return true;//submit the form!
            }

            function toggleRegisterLogin()
            {
                $("#loginArea").fadeToggle("slow");
                $("#registerArea").fadeToggle("slow");
            }



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
                $.post("doLogin", {username: user, password: pass, remember: document.getElementById("toggleRemember").checked}, (data, status, xhr) => {
                    console.log("noo");
                    var jsono = data;
                    if (jsono["code"] === "OK")
                    {
                        window.location.href = "ManagePanel";
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
        <!-- Triggers a function to login immidiately-->
        <c:if test="${empty user}">
            <script src="js/autologin.js"></script>
        </c:if>
        <br><br><br>
        <footer class="text-center footer translucent">
            <small style="bottom: 0;">
                声明：此网站是Johnson为了方便大家管理活动自研的网站，不收集关于ICY账户的任何其它信息。ICY官方和本网站并无合作关系。背景图片来源网络，侵删。<br><!-- comment -->
                Johnson 2021 <a href="http://beian.miit.gov.cn">粤ICP备2022076116号</a>
            </small>

        </footer>

    </body>
</html>
