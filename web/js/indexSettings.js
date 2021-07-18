/* 
 * Author: jianqing
 * Date: May 16, 2021
 * Description: This document is created for index ManagePanel
 */




function isNumeric(str) {
    if (typeof str != "string")
        return false // we only process strings!  
    return !isNaN(str) && // use type coercion to parse the _entirety_ of the string (`parseFloat` alone does not do this)...
            !isNaN(parseFloat(str)) // ...and ensure strings of whitespace fail
}
function randomIntFromInterval(min, max) { // min and max included 
    return Math.floor(Math.random() * (max - min + 1) + min);
}

function isValidImageExtension(extensions, p)
{

    for (var i = 0, maxi = extensions.length; i < maxi; i++) {
        if (p.endsWith(extensions[i]))
        {
            return true;
        }
    }
    return false;
}

function switchInputModeToFlowerPlugin()
{
    document.getElementById("icon-input").value = "";
    document.getElementById("upload-modal-title").innerHTML = "添加落花特效图标";
    document.getElementById("upload-icon-button").setAttribute("onclick", "addImageFloweryPlugin();");

}

function switchInputModeToStatCard(gicon)
{
    document.getElementById("icon-input").value = "";
    document.getElementById("upload-modal-title").innerHTML = "更改数据显示个性化图标";
    document.getElementById("upload-icon-button").setAttribute("onclick", "setStatIcon(" + gicon + ");");

}

function setStatIcon(gicon)
{
    uploadIcon((xhr) => {
        var jsonr = JSON.parse(xhr.responseText);
        if (jsonr["code"] === "OK")
        {
            document.getElementById("gicon" + gicon).src = jsonr["data"]["src"];
            Swal.fire("图标更改成功", "更改完毕记得保存哦~", "success");
            document.getElementById("upload-cancel-button").click();
        } else if (jsonr["code"] === "OSSException")
        {
            Swal.fire("数据库错误", "数据库出错了，呜呜呜！", "error");
        } else
        {
            Swal.fire("未知错误", "出错了，呜呜呜！", "error");
        }

    }, (xhr) => {
        Swal.fire("未知错误", "出错了，呜呜呜！", "error");
    })
}

function updateStatIcon(ele, id)
{

    if (ele.value === "")
    {
        document.getElementById("gicon" + id).removeAttribute("width");
    } else
    {
        if (isNumeric(ele.value))
        {
            document.getElementById("gicon" + id).width = ele.value;
        }

    }
}


function deleteFile(fileurl)
{
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "DeleteFile");
    xhr.send(fileurl);
}

function uploadIcon(successFunc, errorFunc)
{
    $("#addIcon-upload1").fadeOut();
    $("#addIcon-upload2").fadeIn();
    const extensions = [".svg", ".png", ".jpg", ".jpeg", ".webp", ".gif"];
    const p = document.getElementById("icon-input").value;
    if (p === "")
    {
        Swal.fire("请选择上传文件", "文件为空", "warning");
        //invalidFunc();
        $("#addIcon-upload1").fadeIn();
        $("#addIcon-upload2").fadeOut();
        return;
    }

    if (!isValidImageExtension(extensions, p))
    {
        Swal.fire("文件格式错误", "请使用常见的图片格式，支持的有：" + extensions.join(", "), "warning");
        //invalidFunc();
        $("#addIcon-upload1").fadeIn();
        $("#addIcon-upload2").fadeOut();
        return;
    }

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "UpFile");
    xhr.send(new FormData(document.getElementById("icon-uploadform1")));
    xhr.onreadystatechange = (e) => {
        if (xhr.readyState === 4)
        {
            if (xhr.status === 200)
            {
                successFunc(xhr);
            } else
            {
                errorFunc(xhr);
            }
            $("#addIcon-upload1").fadeIn();
            $("#addIcon-upload2").fadeOut();
        }
    };


}

function createFlowerDataTableRow(imgwidth, src, render)
{
    var row = document.createElement("tr");
    var col1 = document.createElement("td");
    var col2 = document.createElement("td");
    var col3 = document.createElement("td");
    var col4 = document.createElement("td");
    var delBtn = document.createElement("button");
    var img = document.createElement("img");
    var imgContainer = document.createElement("div");
    var switchLabel = document.createElement("label");
    var switchInput = document.createElement("input");
    var switchSlider = document.createElement("span");
    var switchSpace = document.createElement("span");
    imgContainer.classList.add("preview-img-container");
    //console.log("d")
    img.setAttribute("onclick", "window.open(this.src,'_blank');");
    img.classList.add("preview-img");
    col1.innerHTML = "<span class='serial'>" + "</span>";
    //var h = "", w = "";

    if (imgwidth !== "")
    {
        img.width = imgwidth;
    }

    img.src = src;
    imgContainer.appendChild(img);
    col2.appendChild(imgContainer);
    col3.innerHTML = "<input oninput='updateImageSize(this);' type='number' class='transparent width-input' size='4' value='" + imgwidth + "' > ";

    //OPERATION COLUMN
    delBtn.setAttribute("onclick", "removeFlowerImage(this);");
    delBtn.classList.add("btn");
    delBtn.classList.add("btn-primary");
    delBtn.innerHTML = "删除";

    switchLabel.classList.add("switch");
    switchInput.classList.add("switchControl");
    switchInput.setAttribute("type", "checkbox");
    if (typeof render === "boolean")
    {
        switchInput.checked = render;
    } else
    {
        switchInput.checked = true;
    }
    switchSlider.classList.add("slider");
    switchSlider.classList.add("round");
    switchSpace.innerHTML = " &nbsp; ";

    switchLabel.appendChild(switchInput);
    switchLabel.appendChild(switchSlider);

    col4.appendChild(switchLabel);
    col4.appendChild(switchSpace);
    col4.appendChild(delBtn);

    //add to the row
    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    row.appendChild(col4);
    return row;
}

function indexFlowerTable(table)
{
    var tableRows = table.children;
    for (var j = 0, maxj = tableRows.length; j < maxj; j++) {
        var row = tableRows[j];
        row.getElementsByClassName("serial")[0].innerHTML = j + 1;
    }
}

function addImageFloweryPlugin()
{

    uploadIcon((xhr) => {
        const jsonr = JSON.parse(xhr.responseText);
        if (jsonr["code"] === "OK")
        {
            const url = jsonr["data"]["src"];
            //create and put that on the table
            var row = createFlowerDataTableRow("18", url, true);
            const tablebody = document.getElementById("floweryPluginTBody");
            row.id = "flowertr-" + randomIntFromInterval(1000, 9999);
            tablebody.appendChild(row);
            indexFlowerTable(tablebody);
            Swal.fire("上传成功", "您的图标已添加成功。完成更改后记得保存哦！", "success");
            document.getElementById("icon-input").value = "";
            document.getElementById("upload-cancel-button").click();
            $("#floweryPluginTBody").tableDnD({
                onDrop: (table, row) => {
                    indexFlowerTable(table);
                }});

        } else if (jsonr["code"] === "OSSException")
        {
            Swal.fire("数据库错误", "请稍后再试", "error");
        } else
        {
            Swal.fire("错误", "请稍后再试", "error");
        }


    }, (xhr) => {
        Swal.fire("未知错误发生了", "网络错误" + xhr.responseText, "error");

    });

}


//load settings into page (LOADERS)




function loadFlowerDataSetting(data)
{
    var tablebody = document.getElementById("floweryPluginTBody");
    tablebody.innerHTML = "";
    var render = data["render"];
    if (render)
    {
        document.getElementById("toggleFlowerPlugin").checked = true;
    }
    var imagespics = data["iconset"];

    for (var i = 0, max = imagespics.length; i < max; i++)
    {
        var imgobj = imagespics[i];
        var row = createFlowerDataTableRow(imgobj.width, imgobj.src, imgobj["render"]);
        row.getElementsByClassName("serial")[0].innerHTML = (i + 1);
        //add row to table
        row.id = "flowertr-" + i;
        tablebody.appendChild(row);
    }
    $("#floweryPluginTBody").tableDnD({
        onDrop: (table, row) => {
            indexFlowerTable(table);
        }});

}


function loadStatDataSetting(data)
{
    document.getElementById("toggleStatcardsPlugin").checked = data["render"];
    for (var i = 1, max = 5; i < max; i++) {
        $("#gicon" + i)[0].src = data["icon" + i]["src"];
        var w = data["icon" + i]["width"];
        if (typeof w !== "undefined" && w !== "")
        {
            $("#gicon" + i)[0].width = w;
            $("#giconinput" + i)[0].value = w;
        }
    }
}

function loadAutoRegisterSetting(data)
{
    document.getElementById("autoRegister").checked = data["render"];
}

function loadICYLottery(data)
{
    document.getElementById("icyLotterySwitch").checked = data["render"];
}


function loadSettings()
{
    var userid = document.getElementById("userid").value;

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "https://icyfile.85vocab.com/usercontent/" + userid[0] + "/" + userid + "/master.json");
    xhr.onload = () => {
        $("#waitingarea").fadeOut("slow");
        $("#settings").fadeIn("slow");
        if (xhr.status !== 200)
        {
            return;
        }
        var json = JSON.parse(xhr.responseText);
        const keys = ["flowers", "statcard", "autoRegister","icylottery"];
        for (var i = 0, max = keys.length; i < max; i++) {
            var plugin = json["plugins"][keys[i]];

            //the plugin JSON object is empty, load the default setting from HTML.
            if (plugin === undefined)
            {
                continue;
            }

            //then, load the specific setting
            //the thing is, if the JSONObject for the specific plugin is not defined, it will not load it and show the default.
            //the default setting table is already written on the webpage. By default, everything is off.
            //the user is free to change things in anyway he wants.
            //once the user hits save, everything will be uploaded and override previous settings.
            switch (i)
            {
                case 0:
                    //flower
                    loadFlowerDataSetting(plugin);
                    break;
                case 1:
                    //statcard
                    loadStatDataSetting(plugin);
                    break;
                case 2:
                    loadAutoRegisterSetting(plugin);
                    break;
                case 3:
                    loadICYLottery(plugin);
                    break;
            }
        }

    }

    xhr.onerror = () => {
        $("#waitingarea").fadeOut("slow");
        $("#settings").fadeIn("slow");
        Swal.fire("Error loading content", xhr.responseText, "error");

    }
    xhr.send();
}



function saveSetting()
{
    //functions to get the indivisual setting info
    function getFlowerPlugins()
    {
        var tbody = document.getElementById("floweryPluginTBody");
        var rows = tbody.children;
        var onoff = document.getElementById("toggleFlowerPlugin").checked;
        var iconset = [];
        var flowerJSON = {"render": onoff};
        for (var i = 0, maxi = rows.length; i < maxi; i++)
        {
            var row = rows[i];
            var img = row.children[1].getElementsByClassName("preview-img")[0];
            var whcol = row.children[2];
            var width = whcol.getElementsByClassName("width-input")[0].value;
            var render = row.getElementsByClassName("switchControl")[0].checked;
            //var height = whcol.getElementsByClassName("height-input")[0].value;
            iconset[iconset.length] = {"src": img.src, "width": width, "height": "", "render": render};
        }
        flowerJSON["iconset"] = iconset;
        return flowerJSON;
    }

///get the statcard plugin setting from the page.
    function getStatcardPlugins()
    {
        return {"render": document.getElementById("toggleStatcardsPlugin").checked,
            "icon1": {"src": $("#gicon1")[0].src, "width": $("#giconinput1")[0].value},
            "icon2": {"src": $("#gicon2")[0].src, "width": $("#giconinput2")[0].value},
            "icon3": {"src": $("#gicon3")[0].src, "width": $("#giconinput3")[0].value},
            "icon4": {"src": $("#gicon4")[0].src, "width": $("#giconinput4")[0].value}
        };
    }

    //fade area
    $("#settings").fadeOut();
    $("#waitingarea").fadeIn("slow");

    var username = document.getElementById("username").value;
    var userid = document.getElementById("userid").value;
    var jsonr = {"username": username, "userid": userid};
    
    ///****
    // HERE, IS WHERE ALL THE PLUGINS GETTING LOAD INTO USER'S master.json. 
    // FUNCTION EXPAND POINT FEP
    //****///
    var jsonPlugins = {
        //"key":{JSON Object}
        "flowers": getFlowerPlugins(),
        "statcard": getStatcardPlugins(),
        "autoRegister": {"render": document.getElementById("autoRegister").checked},
        "icylottery":{"render":document.getElementById("icyLotterySwitch").checked}
    };
    
    

    jsonr["plugins"] = jsonPlugins;

    //UPLOAD the script to the server

    var xhr = new XMLHttpRequest();

    xhr.open("POST", "SavePluginSettings", true);

    xhr.onload = () => {
        //fade table back in
        $("#settings").fadeIn();
        $("#waitingarea").fadeOut("slow");
        var resp = JSON.parse(xhr.responseText);

        if (resp["code"] === "OK")
        {
            Swal.fire("保存成功", "您的设置保存成功！", "success");
        } else if (resp["code"] === "NoLogin")
        {
            Swal.fire("会话超时", "您的会话已超时，请重新登录(3s)", "warning");
            setTimeout(() => {
                window.href = "login.jsp"
            }, 3000);
        } else if (resp["code"] === "OSSException")
        {
            Swal.fire("数据库错误", "呜呜呜~数据库出错了快和站长联系吧！", "error");
        } else
        {
            Seal.fire("未知错误", "未知的系统错误发生了！请稍后再试！<br>" + JSON.stringify(resp), "error");
        }

    };

    xhr.onerror = () => {
        //fade table back in
        $("#settings").fadeIn();
        $("#waitingarea").fadeOut("slow");
        Seal.fire("未知错误", "未知的系统错误发生了！请稍后再试！<br>" + xhr.responseText, "error");
    };

    xhr.send(JSON.stringify(jsonr));

}


function verify()
{
    $("#va1").fadeToggle();
    $("#va2").fadeToggle();
    //try to split icy url
    const inputtedurl = $("#icyurl")[0].value;

    if (inputtedurl === "")
    {
        Swal.fire("个人主页URL必填", "请填写个人主页URL", "info");
        $("#va1").fadeToggle();
        $("va2").fadeToggle();
        return;
    }

    const components = inputtedurl.split("?");

    //check for legit url
    if (components.length < 2 || components[1] === "")
    {
        Swal.fire("主页URL不全", "请前往<a href='https://www.icardyou.icu/userInfo/userCenter' target='_blank'>个人中心</a>，点击预览主页，然后复制打开的页面的URL并粘贴到本页的输入框。正常的URL格式如输入框上所示", "warning");
        $("#va1").fadeToggle();
        $("#va2").fadeToggle();
        return;
    }

    //then, get the query params and get the user id
    const queryString = new URLSearchParams(components[1]);
    const userId = queryString.get("userId");
    if (userId === null || userId === "")
    {
        //examples would be "https://www.icardyou.icu/userInfo/homePage?alienId=12312" or "https://www.icardyou.icu/userInfo/homePage?userId=", these two would pass the previous check.
        Swal.fire("主页URL不全,缺少userId", "请前往<a href='https://www.icardyou.icu/userInfo/userCenter' target='_blank'>个人中心</a>，点击预览主页，然后复制打开的页面的URL并粘贴到本页的输入框。正常的URL格式如输入框上所示", "warning");
        return;
    }

    //after that, submit the info to the server for verification.
    $.post("verifyIcy", {icyUserId: userId}, (data, status, xhr) => {
        //handle return stuff
        if (data["code"] === "OK")
        {
            alert("绑定成功！")
            window.location.reload();
            return;
        }

        if (data["code"] == "VerifyFailed")
        {
            Swal.fire("绑定失败", "我们暂未检测到您的验证标识，请检查是否复制正确完整，是否已保存。");
            $("#va1").fadeToggle();
            $("#va2").fadeToggle();
            return;
        }

        if (data["code"] == "IOException")
        {
            Swal.fire("ICY服务器忙", "本站服务器无法联系ICY服务器，请稍后再试", "error");
            $("#va1").fadeToggle();
            $("#va2").fadeToggle();
            return;
        }

        if (data["code"] == "NoLogin")
        {
            Swal.fire("登录过期", "您的登录已过期，请<a href='login.jsp'>重新登录</a>!", "warning");
            $("#va1").fadeToggle();
            $("#va2").fadeToggle();
            return;
        }

        if (data["code"] == "NoParam")
        {
            Swal.fire("ID为空", "ICY ID不能为空，请重新输入您的主页URL", "warning");
            $("#va1").fadeToggle();
            $("#va2").fadeToggle();
            return;
        }

        if (data["code"] == "SQLException")
        {
            Swal.fire("数据库错误", "服务器数据库错误，请反馈vip@jianqinggao.com", "error");
            $("#va1").fadeToggle();
            $("#va2").fadeToggle();
            return;
        }
    }, "json").fail((xhr) => {
        //handle server error
        Swal.fire("服务器错误", "未知的错误，请反馈", "error");
        $("#va1").fadeToggle();
        $("#va2").fadeToggle();
    });


}



function updateImageSize(ele)
{
    var imgobj = ele.parentNode.parentNode.getElementsByClassName("preview-img")[0];
    var val = ele.value;
    //see if it's width or height.
    //if the input is empty, remove the attribute.
    if (ele.classList.contains("width-input"))
    {
        if (isNumeric(ele.value))
        {
            imgobj.width = ele.value;
        }

        if (ele.value === "")
        {
            imgobj.removeAttribute("width");
        }
    } else
    {
        imgobj.height = ele.value;
        if (ele.value === "")
        {
            imgobj.removeAttribute("height");
        }
    }

}

function removeFlowerImage(ele)
{
    Swal.fire({
        title: '确认删除' + ele.parentNode.parentNode.getElementsByClassName("preview-img-container")[0].parentNode.innerHTML + "",
        text: "该操作将不可逆",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '删除',
        cancelButtonText: "取消"
    }).then((result) => {
        if (result.isConfirmed) {

            var tr = ele.parentNode.parentNode;
            var url = tr.getElementsByClassName("preview-img")[0].src;
            tr.remove();
            indexFlowerTable($("#floweryPluginTBody")[0]);
            Swal.fire(
                    '删除成功!',
                    '你的图标已被删除',
                    'success'
                    );
            deleteFile(url);
        }
    });

}

function checkTimeOut()
{
    var tint = setInterval(function () {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "GetTimeout");
        xhr.send();
        xhr.onload = function () {
            const jsonr = JSON.parse(xhr.responseText);
            const code = jsonr["code"];
            if (code === "NoLogin")
            {
                window.location.href = "login.jsp";
            }
        };
    }, 30000);
}
$(document).ready(() => {
    loadSettings();
    $("#floweryPluginTBody").tableDnD({
        onDrop: (table, row) => {
            indexFlowerTable(table);
        }
    });
    checkTimeOut();
});
      