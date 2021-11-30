/* 
 * Author: jianqing
 * Date: May 16, 2021
 * Description: This document is created for
 */
var onTargetUser = null;
function loadFavourites()
{

}

//switch search method
$(".stsel").click(function () {
    if ($("#st1")[0].checked)
    {
        //search use URL show

        $("#st1-div").fadeIn();
        $("#st2-div").fadeOut();
    } else if (document.getElementById("st2").checked)
    {
        ///console.log("2")
        $("#st1-div").fadeOut();
        $("#st2-div").fadeIn();
        $("#nicknameforid").fadeIn();
        document.getElementById("searchResult").innerHTML = "";
    } else
    {
        //console.log("3")
        $("#st1-div").fadeOut();
        $("#st2-div").fadeIn();
        $("#nicknameforid").fadeOut();
        document.getElementById("searchResult").innerHTML = "";
    }
});


function createSearchResultRow(id, imgsrc, name)
{
    const row = document.createElement("tr");
    const col1 = document.createElement("td");
    const col2 = document.createElement("td");
    const col3 = document.createElement("td");
    //const col4 = document.createElement("td");
    const radio = document.createElement("input");
    const nameEle = document.createElement("a");
    const imageEle = document.createElement("img");
    radio.name = "sr";
    radio.type = "radio";
    radio.value = id;
//        radio.style.setProperty("margin-left","10px");
//        radio.style.setProperty("margin-right","10px");
    nameEle.innerHTML = name;
    nameEle.href = "https://www.icardyou.icu/userInfo/homePage?userId=" + id;
    nameEle.title = "点击前往" + name + "的主页";
    nameEle.target = "_blank";
    //name.style.setProperty("margin-left","10px");
    imageEle.setAttribute("referrerpolicy", "no-referrer");
    imageEle.classList.add("preview-cf-img");
    imageEle.src = imgsrc + "";

    col1.appendChild(radio);
    col2.appendChild(imageEle);
    col3.appendChild(nameEle);
    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    return row;
}

function searchForId()
{
    function disableBtn()
    {
        const btn = document.getElementById("sfcfcfbt");
        btn.disabled = true;
        btn.innerHTML = "<span class='spinner-border spinner-border-sm'></span> 加载中..."
    }

    function enableBtn()
    {
        const btn = document.getElementById("sfcfcfbt");
        btn.disabled = false;
        btn.innerHTML = "搜索片友";
    }

    disableBtn();


    var val = $("#friendid")[0].value;
    if (!val)
    {
        enableBtn();
        Swal.fire("名字为空", "请输入片友昵称", "warning");
        return;
    }

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "SearchId?name=" + encodeURIComponent(val));
    xhr.onload = function () {
        enableBtn();

        var jsonr = JSON.parse(xhr.responseText);
        if (jsonr["code"] === "OK")
        {
            const data = jsonr["data"];
            const parentDiv = document.getElementById("searchResult");
            parentDiv.innerHTML = "";
            for (var i = 0, maxi = data.length; i < maxi; i++)
            {
                var irow = data[i];
                parentDiv.appendChild(createSearchResultRow(irow["id"], irow["head"], irow["nickname"]));
            }

        } else if (jsonr["code"] === "NoLogin")
        {
            Swal.fire("登录过期", "登录过期，请重新登录", "warning");
        } else
        {
            Swal.fire("未知错误", "发生了未知错误", "warning");
        }


    };
    xhr.onerror = function () {
        btn.disabled = false;
        btn.innerHTML = "搜索片友";
        Swal.fire("未知错误", "发生了未知错误", "warning");
    };
    xhr.send();
}


//submit for search
function submitSearchCardClick()
{
    function getSelectedFriend()
    {
        var doms = document.getElementsByName("sr");
        for (var i = 0, max = doms.length; i < max; i++) {
            var d = doms[i];
            if (d.checked)
            {
                return d.value;
            }
        }
        return "";
    }
    var userId;
    if ($("#st1")[0].checked)
    {
        //do st 1 check for input URL
        const inputtedurl = $("#friendurl")[0].value;

        if (inputtedurl === "")
        {
            Swal.fire("个人主页URL必填", "请填写个人主页URL", "info");
        }

        const components = inputtedurl.split("?");

        //check for legit url
        if (components.length < 2 || components[1] === "")
        {
            Swal.fire("主页URL不全", "请复制完整URL", "warning");
            return;
        }

        //then, get the query params and get the user id
        const queryString = new URLSearchParams(components[1]);
        userId = queryString.get("userId");
        if (userId === null || userId === "")
        {
            //examples would be "https://www.icardyou.icu/userInfo/homePage?alienId=12312" or "https://www.icardyou.icu/userInfo/homePage?userId=", these two would pass the previous check.
            Swal.fire("主页URL不全,缺少userId", "", "warning");
            return;
        }
    } else
    {
        //check for name search
        userId = getSelectedFriend();
        if (userId === "")
        {
            Swal.fire("请选择一个片友以继续", "请在给出的结果中选择一个片友继续，如果没有结果，请检查搜索的关键词。", "warning");
            return;
        }
    }


    function confirmSubmission()
    {
        const parentDiv = document.getElementById("pylink");
        parentDiv.innerHTML = '正在为您查找，请稍等！<br><br> <div class="lds-roller"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>';//clear out previous things
        //Activate captcha and send verification code to backend server.
        grecaptcha.ready(function () {
            grecaptcha.execute('6LdSILoZAAAAAF2f6ntG72Dj2gG2jNEqg8Q3Gpjh', {action: 'submit'}).then(function (token) {
                // 
                submitForSearch(userId, token);
            });
        });
    }

    const isFirstTime = localStorage.getItem("firsttime-fangchong");
    if (isFirstTime === null)
    {
        Swal.fire({
            title: '声明',
            html: "使用此功能，代表您同意我们ICY Powerup系统使用您的ICY账号查询您的历史收发记录和片友信息。<br>" +
                    "该功能仅做便民查询，查询结果和Powerup官方无关。<br>" +
                    "为保证服务质量，该功能由谷歌隐形验证码保护，继续使用代表您同意谷歌(Google)的<a href='https://policies.google.com/privacy' target='_blank'>" +
                    "隐私政策" +
                    "</a>以及<a href='https://policies.google.com/terms' target='_blank'>" +
                    "服务条款" +
                    "</a><br><br>\n\
                <input id='buzaitixnotice' type='checkbox'>不再提醒",
            icon: 'info',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '同意并继续',
            cancelButtonText: '取消'
        }).then((result) => {
            if (result.isConfirmed) {
                if (document.getElementById("buzaitixnotice").checked)
                {
                    localStorage.setItem("firsttime-fangchong", false);
                }

                confirmSubmission();
            }
        });
        return;
    }

    confirmSubmission();


}

function submitForSearch(userId, token = "", captcha = "")
{
    const parentDiv = document.getElementById("pylink");
    ///POST the data to the server

    var data = {"userId": userId, "mode": Number.parseInt(document.getElementById("searchFilter").value), "token": token, "captcha": captcha};

    //TO DO complete xhr post server and return data process.
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "SearchCards");
    xhr.onload = function () {
        parentDiv.innerHTML = "";
        if (xhr.status === 200)
        {
            var jsonr = JSON.parse(xhr.responseText);
            if (jsonr["code"] === "OK")
            {
                $("#favbtn").fadeIn("fast");
                onTargetUser = jsonr["data"];
                //do things
                var data = onTargetUser["list"];

                var sender = decodeURIComponent(onTargetUser["sender"]);
                var receiver = decodeURIComponent(onTargetUser["receiver"]);
                $("#sender").html(sender);
                $("#xhbid").html(receiver);
                var row = document.createElement("div");
                row.classList.add("row");
                for (var i = 0, maxi = data.length; i < maxi; i++)
                {
                    var img = data[i];
                    var colParent = document.createElement("div");
                    var imgContainer = document.createElement("div");
                    var imgElement = document.createElement("img");
                    var imgComment = document.createElement("div");
                    var imgCommentA = document.createElement("a");

                    //set the image itself
                    imgElement.crossorigin = "anonymous";
                    imgElement.src = img["imgsrc"];
                    imgElement.setAttribute("referrerpolicy", "no-referrer");
                    imgElement.style.setProperty("width", "100%");
                    imgElement.setAttribute("onclick", "window.open(this.src,'_blank');");
                    imgElement.setAttribute("SameSite", "Lax");
                    //the Postcard ID
                    imgCommentA.href = img["pcsrc"];
                    imgCommentA.innerHTML = img["id"];
                    imgCommentA.target = "_blank";
                    imgComment.classList.add("search-friend-caption");
                    imgComment.appendChild(imgCommentA);
                    imgContainer.classList.add("search-friend-container");
                    imgContainer.appendChild(imgElement);
                    imgContainer.appendChild(imgComment);
                    colParent.classList.add("col-md");
                    colParent.classList.add("colParent")
                    colParent.appendChild(imgContainer);
                    row.appendChild(colParent);

                    if ((i + 1) % 4 === 0 || i === maxi - 1)
                    {
                        parentDiv.appendChild(row);
                        row = document.createElement("div");
                        row.classList.add("row");
                    }

                }
            } else if (jsonr["code"] === "NoLogin") {
                Swal.fire("登录超时", "您的登录已超时，请重新登录！", "error");
            } else if (jsonr["code"] === "CaptchaFail")
            {
                Swal.fire({
                    title: '请输入验证码',
                    input: 'text',
                    html: "<img src='Captcha?bruh=" + randomIntFromInterval(1000, 9999) + "' onclick='this.src=\"Captcha?bruh=\"+randomIntFromInterval(1000,9999);' style='cursor:pointer;' title='点一下更换验证码'>",
                    inputValue: "",
                    showCancelButton: true,
                    cancelButtonText: "取消",
                    inputValidator: (value) => {
                        if (!value) {
                            return '请输入验证码！';
                        }
                        submitForSearch(userId, "", value);
                    }
                });


            } else
            {
                Swal.fire("未知错误", "错误未知", "error");
            }

            //show/hide favourite
            if (jsonr["code"] === "OK")
            {
                $("#favbtn").show();
            } else
            {
                $("#favbtn").hide();
            }

        }
    };

    xhr.onerror = function () {
        parentDiv.innerHTML = "";
        Swal.fire("未知错误", "错误未知", "error");
    };

    xhr.send(JSON.stringify(data));
}


function favouriteUser()
{
    //check if it is on active user
    if (onTargetUser === null)
    {
        Swal.fire("请先指定用户", "需要先搜索用户", "error");
        return;
    }

    $("#favbtn").html("请稍等...正在收藏")
    //try to submit fav request
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "AddFavouriteUser?icyUsername=" + onTargetUser["receiver"] + "&icyId=" + onTargetUser["receiverId"] + "&icyAvatarUrl=" + onTargetUser["receiverAvatarUrl"]);
    xhr.send();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var jsonr = JSON.parse(xhr.responseText);
            var code = jsonr["code"];
            switch (code)
            {
                case "OK":
                    Swal.fire("收藏成功", "TA已添加到您的收藏夹中", "success");
                    break;
                case "FavExists":
                    Swal.fire("已收藏", "TA已经被收藏过了哦！", "info");
                    break;
                default:
                    Swal.fire("系统错误", "代码：" + code + "error");
                    break;
            }
        }
    }
}
