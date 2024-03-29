/* 
 * Author: Johnson @ ICY
 * Date: May 13, 2021
 * Description: This document is created for index.js
 */

((script) => {
    /**
     * Function for load the snow flake plugin.
     * @param {type} data
     * @returns {undefined}
     */
    function loadSnowFlake(data)
    {
        if (!data["render"])
        {
            return;
        }
        const iconset = data["iconset"];
        //Inject the image elements into the main page
        var link = document.createElement("link");
        var snowflakes = document.createElement("div")
        link.rel = "stylesheet";
        link.type = "text/css";
        link.href = "https://icyfile.85vocab.com/public/flowers/SnowFlake.css";
        $("head")[0].appendChild(link);
        //set attribute for the parent div which to hold all image divs
        snowflakes.classList.add("snowflakes");
        snowflakes.setAttribute("aria-hidden", "true");
        for (var i = 0, maxi = iconset.length; i < maxi; i++)
        {
            var imageObj = iconset[i] // get the object of the image from image array
            if (imageObj["render"] === false)
            {
                continue;
            }
            // var imageWidth = 18;
            var imageDivDOM = document.createElement("div") // the div which to hold the image
            var imageDOM = document.createElement("img") // the image itself
            imageDivDOM.classList.add("snowflake")
            //do a type check, to see if user define the image type or just want to use default value

            //all set, set the attribute of the image itself
            imageDOM.src = imageObj["src"];
            imageDOM.width = imageObj["width"];
            imageDivDOM.appendChild(imageDOM); // put the image DOM inside of the image div
            snowflakes.appendChild(imageDivDOM); // append the imageDivDOM into the parent div
        }
        $("body")[0].appendChild(snowflakes);
        //版权声明，请勿去除。

        //增加隐藏特效按钮

        var btnGroup = document.getElementsByClassName("buttons")[0];
        var hideEffectBtn = document.createElement("a");
        hideEffectBtn.id = "hideEffectBtn";
        hideEffectBtn.innerHTML = "隐藏特效";
        hideEffectBtn.href = "javascript:hideEffect()";
        btnGroup.appendChild(hideEffectBtn);

        console.log("萌花飘飘插件，Johnson 制作，https://www.icardyou.icu/userInfo/homePage?userId=32364")
    }


    /**
     * Function for loading the colorful profile plugin.
     * @param {type} data
     * @returns {undefined}
     */
    function initProfile(data) {
        if (!data["render"])
        {
            return;
        }
        function createElement(s)
        {
            return document.createElement(s);
        }
        function setImageWidth(img, w)
        {
            if (w !== undefined || w !== "")
            {
                img.style.setProperty("width", w + "px");
            }
        }
        const IMAGE_CLASS_NAME = "je";
        const briefIntro = $("table")[0].children[0].children[1].children[0].innerHTML
        const lastLoginDate = briefIntro.substring(briefIntro.indexOf("最后登录："), briefIntro.lastIndexOf("，")).replace("最后登录：", "").split("-")

        //Get all user stats, with each of them being represented by a child.
        const userinfo = $(".text-info")[1].children // textinfo is a containers with all the user stats, with each of them
        //being represented by a child.
        const userintohtml = $(".text-info")[1].innerHTML
        var diff = 0;

        //Get the user registeration date/amount of cards sent.
        const date = userinfo[0].innerHTML
        const sendcard = userinfo[1].innerHTML

        //Determine if the user has a "success rate".  Note: Users who send less than 20 cards will not have their stats generated.
        //Which, will cause the parent container "text-info" to shrink in size.
        // The following part takes account of the issue.

        if (userintohtml.includes("成功率"))
        {
            //获取 "纯数字" 成功率
            var successrate = userinfo[2].innerHTML.replace("%", "")

        } else
        {
            //The user does not have a success rate yet, mark as "Unknown";
            var successrate = "--"
            diff++;
        }
        // "3-diff" takes account of the size shrink. If the user info does not have success rate,
        // we should get userinfo[2] instead of userinfo[3]
        const receivecard = userinfo[3 - diff].innerHTML
        //same, some user doesn't have "registeration rate" either. In this case, the size of the parent container
        // will shrink by 1.
        if (userintohtml.includes("登记率"))
        {
            var receiverate = userinfo[4 - diff].innerHTML.replace("%", "")
        } else
        {
            diff++;
            receiverate = "--";
        }

        const takeactivity = userinfo[5 - diff].innerHTML
        const participate_number = userinfo[6 - diff].innerHTML

        //Declear and initalize elements
        const root_table = createElement("div");
        const style_ele = createElement("style");
        const col1 = createElement("div");
        const col2 = createElement("div");
        const col3 = createElement("div");
        const col4 = createElement("div");
        const registerImage = createElement("img");
        const sendImage = createElement("img");
        const receiveImage = createElement("img");
        const exchangeImage = createElement("img");
        const registerDateText = createElement("div");
        const sendText = createElement("div");
        const receiveText = createElement("div");
        const organizeActivityText = createElement("div");
        const joinActivityText = createElement("div");
        const successRateProgressParent = createElement("div");
        const registerRateProgressParent = createElement("div");
        const successRateProgress = createElement("div");
        const registerRateProgress = createElement("div");
        //set up root tabel
        /*addClass(root_table,'row')
         addClass(root_table,'text-center')*/
        root_table.classList.add("row");
        root_table.classList.add("text-center");
        //set up the 4 columns
        /*var sls = "col-sm-3"
         addClass(col1,sls)
         addClass(col2,sls)
         addClass(col3,sls)
         addClass(col4,sls)*/
        col1.classList.add("col-sm-3");
        col2.classList.add("col-sm-3");
        col3.classList.add("col-sm-3");
        col4.classList.add("col-sm-3");
        //set up images

        registerImage.classList.add(IMAGE_CLASS_NAME);
        registerImage.src = data["icon1"]["src"];
        setImageWidth(registerImage, data["icon1"]["width"]);


        sendImage.classList.add(IMAGE_CLASS_NAME);
        sendImage.src = data["icon2"]["src"];
        setImageWidth(sendImage, data["icon1"]["width"])

        receiveImage.classList.add(IMAGE_CLASS_NAME);
        receiveImage.src = data["icon3"]["src"];
        setImageWidth(receiveImage, data["icon3"]["width"])

        exchangeImage.classList.add(IMAGE_CLASS_NAME);
        exchangeImage.src = data["icon4"]["src"];
        setImageWidth(exchangeImage, data["icon4"]["width"]);
        //set up texts
        registerDateText.innerHTML = "注册于 <strong>" + date + "</strong><br>最后登录 <strong>" + lastLoginDate[0] + "年" + lastLoginDate[1] + "月" + lastLoginDate[2] + "日" + "</strong>"
        sendText.innerHTML = "累计发片 <strong>" + sendcard + "</strong>张"
        receiveText.innerHTML = "累计收片 <strong>" + receivecard + "</strong>张"
        organizeActivityText.innerHTML = "发起活动 <strong>" + takeactivity + "</strong>次"
        joinActivityText.innerHTML = "参加活动 <strong>" + participate_number + "</strong>次"

        //set up progress bar parents
        //addClass(registerRateProgressParent,"progress")
        //addClass(successRateProgressParent,"progress")
        registerRateProgressParent.classList.add("progress")
        successRateProgressParent.classList.add("progress")

        //set up progress bars
        /*addClass(successRateProgress,"progress-bar")
         addClass(successRateProgress,"progress-bar-info")
         addClass(successRateProgress,"progress-bar-striped")*/
        successRateProgress.classList.add("progress-bar")
        successRateProgress.classList.add("progress-bar-info")
        successRateProgress.classList.add("progress-bar-striped")
        $(successRateProgress).attr("role", "progressbar")
        successRateProgress.setAttribute("aria-valuenow", successrate)
        successRateProgress.setAttribute("aria-valuemin", 0)
        successRateProgress.setAttribute("aria-valuemax", 100)
        successRateProgress.style.setProperty("width", successrate + "%")
        successRateProgress.innerHTML = "成功率" + successrate + "%"
        successRateProgress.addEventListener("mouseover", () => {
            successRateProgress.classList.add("active")
        })
        successRateProgress.addEventListener("mouseout", () => {
            successRateProgress.classList.remove("active")
        })

        //addClass(registerRateProgress,"progress-bar")
        //addClass(registerRateProgress,"progress-bar-success")
        //addClass(registerRateProgress,"progress-bar-striped")
        registerRateProgress.classList.add("progress-bar")
        registerRateProgress.classList.add("progress-bar-success")
        registerRateProgress.classList.add("progress-bar-striped")
        registerRateProgress.setAttribute("role", "progressbar")
        registerRateProgress.setAttribute("aria-valuenow", receiverate)
        registerRateProgress.setAttribute("aria-valuemin", 0)
        registerRateProgress.setAttribute("aria-valuemax", 100)
        registerRateProgress.style.setProperty("width", receiverate + "%")
        registerRateProgress.innerHTML = "登记率" + receiverate + "%"
        registerRateProgress.addEventListener("mouseover", () => {
            registerRateProgress.classList.add("active")
        })
        registerRateProgress.addEventListener("mouseout", () => {
            registerRateProgress.classList.remove("active")
        })
        //set up style
        style_ele.innerHTML = " .je {\n" +
                "            " +
                "            height: initial !important;\n" +
                "            border-radius: initial !important;\n" +
                "            margin-right: initial !important;\n" +
                "            border: initial !important;\n" +
                "        }";
        //adding children to components


        successRateProgressParent.appendChild(successRateProgress)
        registerRateProgressParent.appendChild(registerRateProgress)

        col1.appendChild(registerImage)
        col1.appendChild(registerDateText)
        col2.appendChild(sendImage)
        col2.appendChild(sendText)
        col2.appendChild(successRateProgressParent)
        col3.appendChild(receiveImage)
        col3.appendChild(receiveText)
        col3.appendChild(registerRateProgressParent)
        col4.appendChild(exchangeImage)
        col4.appendChild(organizeActivityText)
        col4.appendChild(joinActivityText)
        root_table.appendChild(col1)
        root_table.appendChild(col2)
        root_table.appendChild(col3)
        root_table.appendChild(col4)
        root_table.id = "statTable";
        $("head")[0].appendChild(style_ele)

        var originalText = $(".text-info")[1].innerHTML;
        var backUpEle = document.createElement("div");
        backUpEle.style.display = "none";
        backUpEle.innerHTML = originalText;
        backUpEle.id = "pureTextStat";
        $(".text-info")[1].innerHTML = "";
        $(".text-info")[1].appendChild(root_table);
        $(".text-info")[1].appendChild(backUpEle);
    }

    function initAutoRegisteration(data, userId)
    {
        if (data === undefined || !data["render"])
        {
            return;
        }

        var s = document.createElement("script");
        s.src = "https://icyfile.85vocab.com/public/AutoRegister.js";
        s.setAttribute("data-powerupid", userId);

        $("head")[0].appendChild(s);

    }

    function initICYLottery(data)
    {
        if (!data["render"])
        {
            return;
        }

        //load the script.
        var s = document.createElement("script");
        s.src = "https://icyfile.85vocab.com/public/ICYDecider.js";
        document.getElementsByTagName("head")[0].appendChild(s);
    }
    ////////////////////////////MAIN MASTER//////////////////////////

    var xhr = new XMLHttpRequest();
    var userid = script.getAttribute("data-powerup-id");
    xhr.open("GET", "https://icyfile.85vocab.com/usercontent/" + userid[0] + "/" + userid + "/master.json");
    xhr.onload = () => {
//response JSON for user setting
        var jsonr = JSON.parse(xhr.responseText);
        //load settings on page
        var plugins = jsonr["plugins"];
        var installedPlugins = Object.keys(plugins);
        for (var i = 0, maxi = installedPlugins.length; i < maxi; i++)
        {
            //load plugins
            // At here, the script checks which script the users has
            // 
            var pluginname = installedPlugins[i];
            switch (pluginname) {
                case "flowers":
                    loadSnowFlake(plugins[pluginname]);
                    break;
                case "statcard":
                    initProfile(plugins[pluginname]);
                    break;
                case "autoRegister":
                    initAutoRegisteration(plugins[pluginname], userid);
                    break;
                case "icylottery":
                    initICYLottery(plugins[pluginname]);
                    break;
                default :
                    console.log("Not supported" + pluginname);
                    break;
            }
        }

    };
    xhr.onerror = () => {
        alert("主页插件加载失败，请检查是否复制正确。若仍然失败，请把目前的主页链接私信Johnson.");
    };
    xhr.send();


})(document.currentScript);

function hideEffect()
{
    $(".snowflakes").hide();
    alert("再按一次隐藏收发数据显示");
    var b = document.getElementById("hideEffectBtn");
    var t = b.innerHTML;
    if (t !== "隐藏特效")
    {

        $("#statTable").fadeOut("slow", () => {
            $("#pureTextStat").fadeIn("slow");
        });
        alert("数据显示已重置，如发现违规使用插件请站内信Johnson.");
        $("#hideEffectBtn").html("检举违规使用特效");
        b.href = "javascript:reportAbusePowerup();";
        return;
    }
    b.innerHTML = "重置收发数据显示";

}

function reportAbusePowerup()
{
    var xhr = new XMLHttpRequest();
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('userId');
    var powerUpId = 0;
    try{
        powerUpId = document.getElementById("powerup-script").getAttribute("data-powerup-id");
    }catch (exception) {
        console.log(exception);
    }

    xhr.open("GET","icy.85vocab.com/ReportAbusePowerup?powerupId=" + powerUpId + "&icyId=" + userId);
   xhr.onreadystatechange = function (e) {
        if(xhr.readyState === 4 && xhr.status === 200)
        {
            alert("举报成功，感谢您的贡献！");
            $("#hideEffectBtn").html("已举报该用户");
            $("#hideEffectBtn").attr({"href":"#"});
        }else if(xhr.readyState === 4)
        {
            alert("抱歉，举报失败，请站内信Johnson解决");
            $("#hideEffectBtn").html("请站内信Johnson");
            $("#hideEffectBtn").attr({"href":"https://www.icardyou.icu/userInfo/homePage?userId=32364"});
        }
    };
    
    xhr.onerror = function () {
         alert("抱歉，举报失败，请站内信Johnson解决");
            $("#hideEffectBtn").html("请站内信Johnson");
            $("#hideEffectBtn").attr({"href":"https://www.icardyou.icu/userInfo/homePage?userId=32364"});
        
    };
    xhr.send();
}

