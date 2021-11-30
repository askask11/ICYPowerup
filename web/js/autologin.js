/* 
 * Author: jianqing
 * Date: May 26, 2021
 * Description: This document is created for
 */

$(document).ready(function () {
    //these applies only to login page
   function hideLoginAreaLoginPage()
   {
       $("#loginArea").fadeOut("slow")
       $("#waitingArea").fadeIn("slow");
   }
   
    function  showLoginAreaLoginPage()
    {
        $("#loginArea").fadeIn("slow")
        $("#waitingArea").fadeOut("slow");
    }
    
    var l = localStorage.getItem("token");
   if(l!==null && l!=="")
   {
        hideLoginAreaLoginPage();
        Swal.fire("自动登陆中，请稍候...","若您希望取消自动登录，在已登录的界面点击'退出'即可取消自动登录。","info");
       $.post("doLogin",{token:l},(data, status, xhr) => {
           if(data["code"]==="OK")
           {
               Swal.fire("登录成功","正在跳转","success");
               //get next page
               const queryStr = window.location.search;
               const params = new URLSearchParams(queryStr);
               const next = params.get("next");
               window.location.href = next===null?"ManagePanel":next;
               return;
           }
           Swal.fire("自动登录失败","自动登录信息已过期，请重新登录。","warning");
           window.localStorage.removeItem("token");
           showLoginAreaLoginPage();
       });
   }
});


