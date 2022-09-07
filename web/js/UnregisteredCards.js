/* 
 * Author: jianqing
 * Date: Mar 29, 2022
 * Description: This document is created for
 */
var mainData = null;
function updateTaskCard(taskObj)
{
    var totalCards = taskObj["totalCards"];
    var crawlProgress = taskObj["crawlProgress"];
    if (totalCards === 0)
    {
        totalCards = 1;
    }
    const percentage = Math.round(crawlProgress * 100 / totalCards);

    document.getElementById("reportPercentage").innerHTML = percentage + "%";
    document.getElementById("progressBar").style.setProperty("width", percentage + "%");
    document.getElementById("progressBar").innerHTML = crawlProgress + "/" + totalCards + " (" + percentage + "%)";

    /*         if (crawlProgress === totalCards - 1)
     {
     document.getElementById("reportPercentage").innerHTML = "";
     document.getElementById("progressBar").style.setProperty("width", percentage + "%");
     document.getElementById("progressBar").innerHTML = crawlProgress + 1 + "/" + totalCards + " (" + 100 + "%)";
     }*/
}


function pcstatustohtml(code)
{
    var statusHtml;
    switch (code)
        {
            case 0:
                statusHtml = "<span style='color:purple;'>未寄出</span>";
                break;
            case 1:
                statusHtml = "<span>已寄出</span>";
                break;
            case 2:
                statusHtml = "<span style='color:green;'>已收到</span>";
                break;
            case 3:
                statusHtml = "<span style='color:red;'>已过期</span>";
                break;
            default :
                statusHtml = "未知";
                break;
        }
        return statusHtml;
}

function showList()
{

    function getTableRowFromRowData(data,index)
    {
        var row = document.createElement("tr");
        var col1 = document.createElement("td");
        var col2 = document.createElement("td");
        var col3 = document.createElement("td");
        var col4 = document.createElement("td");
        //var col5 = document.createElement("td");

        //col 1 Sender name and link
        var senderNameAndLinkA = document.createElement("a");
        senderNameAndLinkA.innerHTML = data["senderName"];
        senderNameAndLinkA.href = "https://www.icardyou.icu/userInfo/homePage?userId=" + data["senderId"];
        senderNameAndLinkA.target = "_blank";
        col1.appendChild(senderNameAndLinkA);

        //col 2 send time
        col2.innerHTML = (data["sentTime"] === "" ? "--" : data["sentTime"]);

        //col 3 ID
        var cardIdAndLinkA = document.createElement("a");
        cardIdAndLinkA.innerHTML = data["cardId"];
        cardIdAndLinkA.href = "javascript:showDetail(" + index+");";
        //cardIdAndLinkA.target = "_blank";
        col3.appendChild(cardIdAndLinkA);

        //col 4 status
        var statusInt = data["status"];
        var statusHtml = pcstatustohtml(statusInt);
        col4.innerHTML = statusHtml;

        /*//col 5 operation
         //Operations TODO \\ SKIPPED
         var skipA = document.createElement("a");
         skipA.innerHTML = "暂时忽略";
         skipA.title = "在此报告中忽略该片，下次刷新时还会出现。";
         skipA.href = "javascript:skip('" + data["cardId"] + "');";
         col5.appendChild(skipA);*/



        row.appendChild(col1);
        row.appendChild(col2);
        row.appendChild(col3);
        row.appendChild(col4);
        //row.appendChild(col5);
        return row;
    }


    var tableBody = document.getElementById("tableBody");
    tableBody.innerHTML = "请稍等，正在处理中...";
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.status === 200 && xhr.readyState === 4)
        {
            var jsonr = JSON.parse(xhr.responseText);

            var coder = jsonr["code"];
            if (coder !== "OK")
            {
                //Timeout
                if (coder === "NoLogin")
                {
                    Swal.fire({icon: 'info', title: "为了您的安全，请您重新登录，谢谢。"});
                    return;
                }

                //other errors.
                Swal.fire("Error!");
                tableBody.innerHTML = "错误，生成失败。:(";
                return;
            }

            //check if there is active task running
            const taskObj = jsonr["task"];
            if (typeof taskObj !== "undefined")
            {
                updateTaskCard(taskObj);
                $("#reportProgressCard").fadeIn("slow");
                rountineCheckProgress();
            }


            //load previous reports
            tableBody.innerHTML = "";
            var data = jsonr["data"];
            mainData = data;
            if (data.length === 0)
            {
                tableBody.innerHTML = "<br>报告待生成。点击 \"生成报告\" 开始生成。";
                document.getElementById("time").innerHTML = "暂无报告";

            } else {
                //把报告加载出来
                document.getElementById("time").innerHTML = jsonr["time"];
                document.getElementById("totalCards").innerHTML = "，共" + data.length + "张。";
                for (var i = 0, max = data.length; i < max; i++) {
                    var rowData = data[i];
                    tableBody.appendChild(getTableRowFromRowData(rowData,i)); // append row to table
                }
            }



        }
    };
    xhr.open("GET", "GetUnregisteredCards");
    xhr.send();

}


function submitTask()
{
    document.getElementById("genNewReport").disabled = true;
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4)
        {
            const jsonr = JSON.parse(xhr.responseText);
            if (jsonr["code"] !== "OK")
            {
                if (jsonr["code"] === "HasTask")
                {
                    Swal.fire({icon: 'info', title: '报告正在生成', text: '您目前有正在生成的报告，请稍候再试。'});
                    return;
                }
                if (jsonr["code"] === "TooFrequent")
                {
                    Swal.fire({icon: 'info', title: '报告生成过于频繁', text: '每30分钟只能生成一次'});
                    return;
                }
                Swal.fire("发生错误！");
                return;
            }

            Swal.fire({
                icon: 'success',
                title: '成功',
                text: '您的收件报告生成请求已提交，大约需要1-5分钟完成。您可放心关闭此窗口',
                // footer: '<a href="">Why do I have this issue?</a>'
            });
            $("#reportProgressCard").fadeIn("slow");
            rountineCheckProgress();
        }
    };
    xhr.open("GET", "SubmitUnregisteredCardTask");
    xhr.send();
}


function checkProgress(parentInterval)
{

    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4)
        {
            const jsonr = JSON.parse(xhr.responseText);
            if (jsonr["code"] !== "OK")
            {
                //Swal.fire({icon:'error', title:"发生错误", text:"暂时无法检查更新"});
                clearInterval(parentInterval);
                return;
            }


            const taskObj = jsonr["task"];
            if (typeof taskObj === "undefined")
            {
                $("#reportProgressCard").fadeOut();
                clearInterval(parentInterval);
                showList();
                return;
            }
            updateTaskCard(taskObj);
        }
    };

    xhr.open("GET", "GetUnregisteredCards?progressOnly=true");
    xhr.send();
}



function rountineCheckProgress()
{
    var x = setInterval(function () {
        checkProgress(x);
    }, 900);
}

function cancel()
{
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4)
        {
            var jsonr = JSON.parse(xhr.responseText);
            if (jsonr["code"] === "OK")
            {
                Swal.fire({icon: 'success', title: '取消成功', text: '报告生成已取消，将显示已获取的结果'});
            } else
            {
                Swal.fire({icon: 'error', title: '取消失败', text: 'oh no cancellation failed.'})
            }
        }
    };
    xhr.open("GET", "CancelCrawlUC");
    xhr.send();
}

function cancelConfirm()
{
    Swal.fire({
        title: '取消报告生成？',
        text: "每30分钟只能生成一次",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        cancelButtonText: '继续生成',
        confirmButtonText: '取消生成'
    }).then((result) => {
        if (result.isConfirmed) {
            cancel();
        }
    });
}

function skip(id)
{
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4)
        {
            var jsonr = JSON.parse(xhr.responseText);
            if (jsonr["code"] !== "OK")
            {
                Swal.fire("忽略失败...");
                return;
            }
            showList();
        }
    };
    xhr.open("GET", "ForgetCrawlCard?pcid=" + id);
    xhr.send();
}

function showDetail(index)
{
    function getImageGroupText(pcJson)
    {
        var picurls = pcJson["picURL"];
        
        if(typeof picurls === "undefined")
        {
            return "该明信片暂无图片";
        }
        var result="<br>明信片图片：<br><br>";
        for (var i = 0, max = picurls.length; i < max; i++) {
            result = result+"<img crossorigin='anonymous' referrerpolicy='no-referrer' style='width:70%;' src='"+picurls[i]+"'><br>";
        }
        return result;
    }
    var pcJson = mainData[index];
    
    Swal.fire({
        title: '<strong><a href="https://www.icardyou.icu/sendpostcard/postcardDetail/'+pcJson["sequence"]+'" target="_blank">'+pcJson["cardId"]+'</a></strong>',
        icon: 'info',
        html:
                "<br>寄件人："+pcJson["senderName"]+"<br>状态:"+pcstatustohtml(pcJson["status"])+"<br>寄出时间："+(pcJson["sentTime"]===""?"--":pcJson["sentTime"])+"<br><hr>\
明信片类型可能是： <span id='guessedPostcardType'>"+guessPostcardType(pcJson["sequence"],pcJson["senderId"],pcJson["receiverId"])+"</span> \n\
        <hr>"+
                getImageGroupText(pcJson)+
                '',
        showCloseButton: true,
        showCancelButton: true,
        showDenyButton:true,
        focusConfirm: false,
        denyButtonText:'暂时忽略此片',
        confirmButtonText:
                '转到对应页面',
                // confirmButtonAriaLabel: '访问官网',
        cancelButtonText:
                '取消',
        //cancelButtonAriaLabel: 'Thumbs down'
    }).then((result)=>{
        if(result.isConfirmed)
        {
            window.open("https://www.icardyou.icu/sendpostcard/postcardDetail/"+pcJson["sequence"],"_blank");
        }else if(result.isDenied)
        {
            skip(mainData[index]["cardId"]);
        }
    });
    
   
}

function guessPostcardType(sequence,sender,receiver)
{
    //changePostcardTypeLabel("");
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if(xhr.readyState === 4)
        {
            var jsonr = JSON.parse(xhr.responseText);
            if(jsonr["code"]!=="OK")
            {
                return "获取失败";
            }
            var upperGuess = jsonr["data"]["upperGuess"];
            var lowerGuess = jsonr["data"]["lowerGuess"];
            //const tips = "<br><small>小提示：打开自己ICY的发片列表，找到明信片ID\""+pcJson["cardId"]+ "\"即可看到这两张片所对应的活动。</small>"
            if(typeof upperGuess!=="undefined" && typeof lowerGuess !=="undefined")
            {
               changePostcardTypeLabel( "互寄活动。 该明信片和你寄的明信片<a target='_blank' href='https://www.icardyou.icu/sendpostcard/postcardDetail/"+(Number.parseInt(sequence)+1)+"'>"+upperGuess+"</a>\n\
、 <a target='_blank' href='https://www.icardyou.icu/sendpostcard/postcardDetail/" + (Number.parseInt(sequence)-1) + "'>"+lowerGuess+"</a> 同时生成的。请到自己的icy账户，已寄出明信片里\n\
确认明信片类型。");
            }else if(typeof upperGuess !== "undefined")
            {
                changePostcardTypeLabel( "互寄活动。 你寄出的是<a target='_blank' href='https://www.icardyou.icu/sendpostcard/postcardDetail/"+(Number.parseInt(sequence)+1)+"'>"+upperGuess+"</a>\n\
"+"<br><br><small style='color:grey'>小提示：打开自己ICY的<a href='https://www.icardyou.icu/sendpostcard/myPostCard/1'>发片列表</a>，找到明信片ID \""+upperGuess+ "\" 即可看到这两张片所对应的活动。</small>");
            }else if(typeof lowerGuess !== "undefined")
            {
                changePostcardTypeLabel( "互寄活动。 你寄出的是<a target='_blank' href='https://www.icardyou.icu/sendpostcard/postcardDetail/"+(Number.parseInt(sequence)-1)+"'>"+lowerGuess+"</a>\n\
"+"<br><br><small style='color:grey'>小提示：打开自己ICY的<a target='_blank' href='https://www.icardyou.icu/sendpostcard/myPostCard/1'>发片列表</a>，找到明信片ID \""+lowerGuess+ "\" 即可看到这两张片所对应的活动。</small>");
            }else
            {
                changePostcardTypeLabel( "赠送，回寄，发片散片活动，和其它非活动片。 ");
            }
        }
    };
    xhr.open("GET","GuessCardType?cardSequence="+sequence+"&sender="+sender+"&receiver="+receiver);
    xhr.send();
    return " <div class=\"spinner-border text-primary\" role=\"status\"><span class=\"sr-only\">正在尝试获取该明信片类型...</span></div>";
}

function changePostcardTypeLabel(txt)
{
    var d = document.getElementById("guessedPostcardType");
    if(d!==null)
        d.innerHTML = txt;
}



showList();
