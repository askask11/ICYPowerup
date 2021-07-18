/* 
 * Author: jianqing
 * Date: May 17, 2021
 * Description: This document is created for
 */

((script) => {
    var userId = script.getAttribute("data-powerupid");
    var input = document.createElement("input");
    var form_group = document.createElement("div");
    var sbm_btn = document.createElement("button");
    var spanEx = document.createElement("label");
    var parentForm = document.createElement("div");
    //var inputDiv = document.createElement("div");



    spanEx.innerHTML = "过期自助补登：";
    spanEx.for = "expiredCD";
    spanEx.classList.add("control-label");
    //spanEx.classList.add("col-sm-3");
    input.classList.add("form-control");
    input.type = "text";
    input.placeholder = "输入过期明信片ID";
    input.id = "expiredCD";
    input.style.setProperty("width", "80%");
    input.style.setProperty("margin-right", "10px");
    //inputDiv.classList.add("col-sm-9");
    //inputDiv.appendChild(input);
    sbm_btn.classList.add("btn");
    sbm_btn.classList.add("btn-primary");
    //sbm_btn.style.setProperty("margin-top","11px");
    sbm_btn.innerHTML = "提交";
    sbm_btn.setAttribute("onclick", "autoRegisterExpiredPostcards(" + userId + ");");
    parentForm.classList.add("form-inline");


    form_group.classList.add("form-group");
    form_group.style.setProperty("width","100%");
    form_group.appendChild(spanEx);
    form_group.appendChild(input);
    form_group.appendChild(sbm_btn);
    parentForm.appendChild(form_group);


    document.getElementsByClassName("well")[0].children[0].appendChild(parentForm);
    //$(".user-homepage")[0].appendChild(parentForm);


})(document.currentScript);

function autoRegisterExpiredPostcards(userId)
{
    //var cardurl = document.getElementById("expiredCD").value;
    var cardid=document.getElementById("expiredCD").value;
    if (cardid === "")
    {
        alert("请输入过期的明信片ID", "warning");
        return;
    }

    /*if (!cardurl.startsWith("https://www.icardyou.icu/sendpostcard/postcardDetail/"))
    {
        alert("请输入明信片页面的URL，不是明信片ID", "warning");
        return;
    }
    var splicec = cardurl.split("/");
    cardid = splicec[splicec.length - 1];
    if (cardurl === "")
    {
        alert("地址不合法，请检查后重新输入", "warning");
        return;
    }*/

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "https://icy.85vocab.com/RegisterExpiredCards?userId=" + userId + "&cardId=" + cardid);
    xhr.onload = () => {
        const jsonr = JSON.parse(xhr.responseText);
        const code = jsonr["code"];
        switch (code)
        {
            case "OK":
                const daaa = jsonr["data"];
                alert(daaa["resultMessage"], daaa["resultCode"] === 200 ? "success" : "warning");
                if(daaa["resultCode"]===200)
                {
                    window.open("https://www.icardyou.icu/sendpostcard/postcardDetail/"+daaa["resultBody"].split(";")[0]);
                }
                break;
            case "NoExpire":
                alert("这张卡片还没有过期哦，请过期了再来登记吧！","warning");
                break;
            case "NoUser":
                alert("补登记失败，该用户暂未和ICY账户绑定", "warning");
                break;
            case "NoOpen":
                alert("补登记失败，该用户暂未开启该功能", "warning");
                break;
            case "SQLException":
                alert("补登记代理服务器数据库，就，，没有脑子，呜呜呜呜呜，请稍后重试", "warning");
                break;
            case "ArrayIndexOutOfBoundsException":
            case "NumberFormatException":
                alert("请输入正确的用户ID", "warning");
                break;
            case "NoParam":
                alert("参数错误，请检查你输入的链接是否完整，或者用户设置不完整，请稍后再试。", "warning");
                break;
            case "NotFound":
                alert("抱歉，暂时未找到这张片，请稍后再试。","warning");
                break;
            default:
                alert("补登记代理服务器数据库，就，，没有脑子，呜呜呜呜呜  我错了 未知错误", "warning");
                break;
        }


    };
    alert("正在查找，请稍等。","info");
    xhr.send();

}

