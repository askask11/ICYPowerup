/* 
 * Author: jianqing
 * Date: May 26, 2021
 * Description: This document is created for
 */

function lockButton(btn)
{
    btn.disabled=true;
    btn.innerHTML="<span class='spinner-border spinner-border-sm'></span> 请稍等";
}
function unlockButton(btn)
{
    btn.disabled=false;
    btn.innerHTML="OK"
}

function changeEmail()
{
    var emailadd = document.getElementById("email").value;
    const btn = document.getElementById("emailConfBtn");
    lockButton(btn);
    
    if (emailadd === "")
    {
        Swal.fire("邮件为空，请重新输入。");
        return;
    }

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "ChangeEmail");
    xhr.onload = function () {
        
        var jsonr = JSON.parse(xhr.responseText);
        const code = jsonr["code"];
        unlockButton(btn);
        switch (code) {
            case "OK-verify":
                Swal.fire("验证码已发送","验证码已发送至您邮箱，请查收后填入","success");
                $("#emailVerify").fadeIn("slow");
                break;
            case "TooFrequent":
                Swal.fire("过于频繁","您验证码申请过于频繁，请两分钟后再试","error");
                break;
            case "OK":
                Swal.fire("更改成功","您的邮箱已更改成功","success");
                break;
            case "EmailExists":
                Swal.fire("该邮箱已被注册","请输入其它邮箱","error");
                break;
            case "NoLogin":
                Swal.fire("登录超时", "您的登录已超时，请重新登录(3s).", "warning");
                setTimeout(() => {
                    window.location.reload();
                }, 3000);
                break;
            case "BadParam":
                Swal.fire("邮箱格式错误", "您输入的不是一个正确的邮箱格式，请重新输入", "warning");
                break;
            case "BadVerify":
                Swal.fire("验证码错误","您输入的验证码错误，请重新输入","warning");
                break;
            case "SQLException":
                Swal.fire("数据库错误", "数据库出错了", "error");
                break;
            default:
                Swal.fire("未知错误", "发生了未知错误", "warning");
                break;
        }
    };

    xhr.onerror = function () {
        unlockButton(btn);
        Swal.fire("未知错误", "发生了未知错误", "warning");
    };

    xhr.send(new FormData(document.getElementById("emailForm")));
}

function changePassword()
{
    var pw = document.getElementById("password").value;
    var btn =  document.getElementById("passwordConfBtn");
    lockButton(btn);
    if (pw === "")
    {
        Swal.fire("密码为空，请重新输入。");
        return;
    }

    document.getElementById("passwordr").value = $.md5(pw);
    document.getElementById("password").value = "";

    
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "ChangePassword");
    xhr.onload = function () {
        unlockButton(btn);
        var jsonr = JSON.parse(xhr.responseText);
        const code = jsonr["code"];

        switch (code) {
            case "OK":
                Swal.fire("更改成功", "您的密码更改成功", "success");
                break;
            case "NoLogin":
                Swal.fire("登录超时", "您的登录已超时，请重新登录(3s).", "warning");
                setTimeout(() => {
                    window.location.reload();
                }, 3000);
                break;
            case "BadParam":
                Swal.fire("密码格式错误", "呜呜呜呜呜", "warning");
                break;
            case "SQLException":
                Swal.fire("数据库错误", "数据库出错了:(呜呜呜呜呜", "error");
                break;
            default:
                Swal.fire("未知错误", "发生了未知错误", "warning");
                break;
        }
    };

    xhr.onerror = function () {
        unlockButton(btn);
        Swal.fire("未知错误", "发生了未知错误", "warning");
    };

    xhr.send(new FormData(document.getElementById("passwordForm")));

}

function changeUsername()
{
    var pw = document.getElementById("username").value;
    const btn = document.getElementById("usernameConfBtn");
    lockButton(btn);
    if (pw === "")
    {
        Swal.fire("用户名为空，请重新输入。");
        return;
    }


    var xhr = new XMLHttpRequest();
    xhr.open("POST", "ChangeUsername");
    xhr.onload = function () {
        unlockButton(btn);
        var jsonr = JSON.parse(xhr.responseText);
        const code = jsonr["code"];

        switch (code) {
            case "OK":
                Swal.fire("更改成功", "您的用户名更改成功", "success");
                break;
            case "NoLogin":
                Swal.fire("登录超时", "您的登录已超时，请重新登录(3s).", "warning");
                setTimeout(() => {
                    window.location.reload();
                }, 3000);
                break;
            case "BadParam":
                Swal.fire("密码格式错误", "呜呜呜呜呜", "warning");
                break;
            case "SQLException":
                Swal.fire("数据库错误", "数据库出错了:(呜呜呜呜呜", "error");
                break;
            default:
                Swal.fire("未知错误", "发生了未知错误", "warning");
                break;
        }
    };

    xhr.onerror = function () {
        unlockButton(btn);
        Swal.fire("未知错误", "发生了未知错误", "warning");
    };

    xhr.send(new FormData(document.getElementById("usernameForm")));

}