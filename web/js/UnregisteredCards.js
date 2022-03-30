/* 
 * Author: jianqing
 * Date: Mar 29, 2022
 * Description: This document is created for
 */


(function showList()
{

    function getTableRowFromRowData(data)
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
        col2.innerHTML = (data["sentTime"]===""?"--":data["sentTime"]);

        //col 3 ID
        var cardIdAndLinkA = document.createElement("a");
        cardIdAndLinkA.innerHTML = data["cardId"];
        cardIdAndLinkA.href = "https://www.icardyou.icu/sendpostcard/postcardDetail/" + data["sequence"];
        cardIdAndLinkA.target = "_blank";
        col3.appendChild(cardIdAndLinkA);

        //col 4 status
        var statusInt = data["status"];
        var statusHtml;
        switch (statusInt) 
        {
            case 0:
                statusHtml = "未寄出";
                break;
            case 1:
                statusHtml = "已寄出";
                break;
            case 2:
                statusHtml = "已收到";
                break;
            case 3:
                statusHtml = "已过期";
                break;
            default :
                statusHtml = "未知";
                break;
        }
        col4.innerHTML = statusHtml;
        
        //col 5 operation
        //Operations TODO \\ SKIPPED


        row.appendChild(col1);
        row.appendChild(col2);
        row.appendChild(col3);
        row.appendChild(col4);
        //row.appendChild(col5);
        return row;
    }


    var tableBody = document.getElementById("tableBody");
    tableBody.innerHTML="请稍等，正在处理中...";
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.status === 200 && xhr.readyState === 4)
        {
            var jsonr = JSON.parse(xhr.responseText);
            var coder = jsonr["code"];
            if (coder !== "OK")
            {
                Swal.fire("Error!");
                tableBody.innerHTML="错误，生成失败。:(";
                return;
            }
            tableBody.innerHTML="";
            var data = jsonr["data"];
            
            for (var i = 0, max = data.length; i < max; i++) {
                var rowData = data[i];
                tableBody.appendChild(getTableRowFromRowData(rowData)); // append row to table
            }
        }
    };
    xhr.open("GET","GetUnregisteredCards");
    xhr.send();

})();