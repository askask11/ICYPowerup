/* 
 * Author: jianqing
 * Date: May 16, 2021
 * Description: This document is created for
 */
$(".stsel").click(function () {
   if($("#st1")[0].checked) 
   {
       //search use URL show
       $("#st1-div").fadeIn();
       $("#st2-div").fadeOut();
   }else
   {
       $("#st1-div").fadeOut();
       $("#st2-div").fadeIn();
       
   }
});

function searchForId()
{
    const btn = document.getElementById("sfcfcfbt");
    btn.disabled=true;
    btn.innerHTML="<span class='spinner-border spinner-border-sm'></span> 加载中..."
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
        nameEle.href="https://www.icardyou.icu/userInfo/homePage?userId="+id;
        nameEle.title = "点击前往"+name+"的主页";
        nameEle.target = "_blank";
        //name.style.setProperty("margin-left","10px");
        imageEle.setAttribute("referrerpolicy","no-referrer");
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
    var val = $("#friendid")[0].value;
    if (!val)
    {
        Swal.fire("名字为空", "请输入片友昵称", "warning");
        return;
    }

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "SearchId?name="+encodeURIComponent(val));
    xhr.onload = function () {

        btn.disabled=false;
        btn.innerHTML = "搜索片友";
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
        btn.disabled=false;
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
            if(d.checked)
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
    }else
    {
        //check for name search
        userId = getSelectedFriend();
        if(userId==="")
        {
            Swal.fire("请选择一个片友以继续", "请在给出的结果中选择一个片友继续，如果没有结果，请检查搜索的关键词。", "warning");
            return;
        }
    }


    const parentDiv = document.getElementById("pylink");
    parentDiv.innerHTML = '正在为您查找，请稍等！<br><br> <div class="lds-roller"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>';//clear out previous things
    //Activate captcha and send verification code to backend server.
    grecaptcha.ready(function () {
        grecaptcha.execute('6LdSILoZAAAAAF2f6ntG72Dj2gG2jNEqg8Q3Gpjh', {action: 'submit'}).then(function (token) {
            // Add your logic to submit to your backend server here.
            submitForSearch(userId, token);
        });
    });

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
                //do things
                var data = jsonr["data"]["list"];
                var sender = jsonr["data"]["sender"];
                var receiver = decodeURIComponent(jsonr["data"]["receiver"]);
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
                    imgElement.setAttribute("SameSite", "Strict")
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

        }
    };

    xhr.onerror = function () {
        parentDiv.innerHTML = "";
        Swal.fire("未知错误", "错误未知", "error");
    };

    xhr.send(JSON.stringify(data));
}

function deregisterICY()
{
    Swal.fire({
        title: '确定吗?',
        text: "该操作将解绑你的Powerup账号和ICY账号!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '是的，立即解绑!',
        cancelButtonText: '取消'

    }).then((result) => {
        if (result.isConfirmed) {
            //to actually deregister it.
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "DeregisterICY");
            xhr.send();
            xhr.onload = () => {
                var jsonr = JSON.parse(xhr.responseText);
                if (jsonr["code"] == "OK")
                {
                    alert("解除绑定成功！");//block the thread
                    window.location.reload();
                } else if (jsonr["code"] === "SQLException")
                {
                    Swal.fire("数据库错误", "好像失败了，请稍后再试。", "error");
                } else
                {
                    Swal.fire("未知错误", "好像失败了，请稍后再试。", "error");
                }
            };
        }
    })

}


