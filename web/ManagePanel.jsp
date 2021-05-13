<%-- 
    Document   : ManagePanel
    Created on : May 9, 2021, 10:25:41 PM
    Author     : jianqing
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height: 100%;">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>管理我的个性化插件</title>
        <c:if test="${empty user}">
            <meta http-equiv="refresh" content="0; url=login.jsp" />
        </c:if>
        <%@include file="/WEB-INF/jspf/head.jspf" %>
        <style>
            body
            {
                background-image:  url('https://xeduocdn.sirv.com/Images/aesthetic-anime-01.jpg');
            } 
            .translucent
            {
                background-color: rgba(255,255,255,0.85);
            }
            .je{

                /*height: initial !important;*/
                border-radius: initial !important;
                margin-right: initial !important;
                border: initial !important;
            }
            .transparent
            {
                background-color: rgba(255,255,255,0);
                border:0px;

            }
            .transparent.width-input, .transparent.height-input
            {
                border-bottom: 1px black ridge;
                width:50px;
            }
            .lds-roller div:after
            {
                background-color: dodgerblue;
            }
            .preview-img
            {


            }

            .preview-img-container
            {
                overflow: scroll;
                max-width: 300px;
                max-height: 300px;
            }
        </style>
        <link rel="stylesheet" href="css/switch.css"/>
        <script src="https://cdn.jsdelivr.net/gh/isocra/TableDnD@master/dist/jquery.tablednd.1.0.5.min.js"></script>
    </head>
    <body style="height:100%;">
        <%@include file="/WEB-INF/jspf/nav.jspf" %>
        <br>
        <br>
        <br>
        <div class="container translucent rounded" style="padding:23px;">
            <br>
            <h3>
                欢迎，<c:out value="${user.getUsername()}"></c:out>
                </h3>

                <br>

            <c:if test="${!user.isIcyVerified()}">
                您还没有绑定ICY账号，请先绑定。<br><br>
                <form id="va1">
                    为了账号安全，我们需要验证即将绑定的账号属于你，请：<br>
                    1. <a href="https://www.icardyou.icu/userInfo/userCenter" target="_blank">点击</a>进入你的ICY个人设置页面<br><!-- explain -->
                    2. 复制以下代码并粘贴到"个人介绍"的最下方（它不会在你的个人主页的网页上显示）完毕后，点击保存。<br>
                    <textarea readonly class='form-control'><script src="https://icyfile.85vocab.com/public/index.js" data-powerup-id="${user.getId()}" id="powerup-script"></script></textarea>

                    3.点击"预览主页"，复制网页URL，请在这里粘贴你的ICY个人主页URL:
                    <div class='form-group'>
                        <label for='icyurl'>
                            你的ICY个人主页URL：
                        </label>
                        <input class='form-control' id='icyurl' placeholder="https://www.icardyou.icu/userInfo/homePage?userId=?????">
                    </div>

                    <button class='btn btn-primary' onclick="verify()" type="button" id="verifyButton">验证及绑定</button>
                </form>
                    <div id="va2" class="text-center" style="display: none;">
                        请您稍等，正在验证。<br><%@include file="/WEB-INF/jspf/lds-loader.jspf" %>
                    </div>
            </c:if>

            <c:if test="${user.isIcyVerified()}">
                您已绑定ICY账号， 您可<a href="https://www.icardyou.icu/userInfo/homePage?userId=${user.getIcyid()}" target="_blank">转到ICY账户主页</a> 或 <a href="javascript:deregisterICY();">解除绑定</a>
                <br><br><br>

                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" data-toggle="tab" href="#home">主页特效</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="#menu1">一键防重</a>
                    </li>
                    <!--<li class="nav-item">
                      <a class="nav-link" data-toggle="tab" href="#menu2">Menu 2</a>
                    </li>-->
                </ul>

                <!-- Tab panes -->
                <div class="tab-content">
                    <div id="home" class="container tab-pane active"><br>
                        主页特效安装：
                        <textarea readonly class='form-control'><script src="https://icyfile.85vocab.com/public/index.js" data-powerup-id="${user.getId()}" id="powerup-script"></script></textarea>
                        <br><h3>主页特效</h3> 
                        <br><!-- Table for management -->
                        <!-- Rounded switch -->
                        <div id='settings' style="display: none;">
                            <label class="switch">
                                <input type="checkbox" id="toggleFlowerPlugin">
                                <span class="slider round"></span>
                            </label>

                            <br>
                            落花特效个性化：(推荐个数10个以下)
                            <table class="table table-striped text-center" id="floweryPlugin">
                                <thead>
                                <td>
                                    #
                                </td>
                                <td>
                                    预览
                                </td>
                                <td>
                                    宽 (px)
                                </td>
                                <td>

                                </td>
                                </thead>
                                <tbody id="floweryPluginTBody">
                                    <tr>
                                        <td>
                                            1
                                        </td>
                                        <td>
                                            <div class="preview-img-container">
                                                <img class="preview-img" src="https://cdn.jsdelivr.net/gh/askask11/ICYPlugins@master/Images/sakura (2).svg" width="18">
                                            </div>
                                        </td>
                                        <td>
                                            <input class="transparent width-input"  type="number" placeholder="" value="18" oninput="updateImageSize(this);">
                                        </td>
                                        <td>
                                            <button class="btn btn-primary">删除</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            2
                                        </td>
                                        <td>
                                            <img class="preview-img" src="https://cdn.jsdelivr.net/gh/askask11/ICYPlugins@master/Images/cherry-blossom.svg" width="18">
                                        </td>
                                        <td>
                                            <input class="transparent width-input"  type="number" placeholder="" value="18" oninput="updateImageSize(this);">
                                        </td>
                                        <td>
                                            <button class="btn btn-primary">删除</button>
                                        </td>
                                    </tr><!-- comment -->
                                    <tr>
                                        <td>
                                            3
                                        </td>
                                        <td>
                                            <img class="preview-img" src="https://cdn.jsdelivr.net/gh/askask11/ICYPlugins@master/Images/snowflake (1).svg" width="18">
                                        </td>
                                        <td>
                                            <input class="transparent width-input"  type="number" placeholder="" value="18" oninput="updateImageSize(this);">
                                        </td>
                                        <td>
                                            <button class="btn btn-primary">删除</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            4
                                        </td>
                                        <td>
                                            <img class="preview-img" src="https://cdn.jsdelivr.net/gh/askask11/ICYPlugins@master/Images/heart.svg" width="18">
                                        </td>
                                        <td>
                                            <input class="transparent width-input"  type="number" placeholder="" value="18" oninput="updateImageSize(this);">
                                        </td>
                                        <td>
                                            <button class="btn btn-primary">删除</button>
                                        </td>
                                    </tr><!-- comment -->
                                    <tr>
                                        <td>
                                            5
                                        </td>
                                        <td>
                                            <img class="preview-img" src="https://cdn.jsdelivr.net/gh/askask11/ICYPlugins@master/Images/pawprint.svg" width="18">
                                        </td>
                                        <td>
                                            <input class="transparent width-input" type="number" placeholder="" value="18" oninput="updateImageSize(this);">
                                        </td>
                                        <td>
                                            <button class="btn btn-primary">删除</button>
                                        </td>
                                    </tr><!-- comment -->
                                    <tr>
                                        <td>
                                            6
                                        </td>
                                        <td>
                                            <img class="preview-img" src="https://cdn.jsdelivr.net/gh/askask11/ICYPlugins@master/Images/cherry-blossom (1).svg" width="18">
                                        </td>
                                        <td>
                                            <input class="transparent width-input" type="number" placeholder="" value="18" oninput="updateImageSize(this);">
                                        </td>
                                        <td>
                                            <button class="btn btn-primary">删除</button>
                                        </td>
                                    </tr><!-- comment -->
                                    <tr>
                                        <td>
                                            7
                                        </td>
                                        <td>
                                            <img class="preview-img" src="https://cdn.jsdelivr.net/gh/askask11/ICYPlugins@master/Images/snowflake.svg" width="18">
                                        </td>
                                        <td>
                                            <input class="transparent width-input"  type="number" placeholder="" value="18" oninput="updateImageSize(this);">
                                        </td>
                                        <td>
                                            <button class="btn btn-primary">删除</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            8
                                        </td>
                                        <td>
                                            <img class="preview-img" src="https://cdn.jsdelivr.net/gh/askask11/ICYPlugins@master/Images/sakura (2).svg" width="18">
                                        </td>
                                        <td>
                                            <input class="transparent width-input"  type="number" placeholder="" value="18" oninput="updateImageSize(this);">
                                        </td>
                                        <td>
                                            <button class="btn btn-primary">删除</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            9
                                        </td>
                                        <td>
                                            <img class="preview-img" src="https://cdn.jsdelivr.net/gh/askask11/ICYPlugins@master/Images/cherry-blossom (2).svg" width="18">
                                        </td>
                                        <td>
                                            <input class="transparent width-input"  type="number" placeholder="" value="18" oninput="updateImageSize(this);">
                                        </td>
                                        <td>
                                            <button class="btn btn-primary">删除</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            10
                                        </td>
                                        <td>
                                            <img class="preview-img" src="https://cdn.jsdelivr.net/gh/askask11/ICYPlugins@master/Images/sakura.svg" width="18">
                                        </td>
                                        <td>
                                            <input class="transparent width-input"  type="number" placeholder="" value="18" oninput="updateImageSize(this);">
                                        </td>
                                        <td>
                                            <button class="btn btn-primary">删除</button>
                                        </td>
                                    </tr><!-- comment -->

                                </tbody>
                            </table>
                            <!-- comment -->
                            <button class="btn btn-success" data-toggle="modal" data-target="#addIcon" onclick="switchInputModeToFlowerPlugin()">
                                添加
                            </button>
                            <br><br>
                            <!--添加文件Modal-->

                            <div class="modal fade text-center" id="addIcon" tabindex="-1" role="dialog">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="upload-modal-title">添加落花特效图标</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div id="addIcon-upload1">
                                            <form id="icon-uploadform1">
                                                <div id="pre-upload">
                                                    <p>自定义上传图标<br>在下面选择文件，并点击保存即可开始上传。</p>
                                                    <input type="file" class="form-control"  id="icon-input" name="file">
                                                </div>
                                            </form>
                                        </div>
                                        <div id="addIcon-upload2" class="text-center" style="display:none;">
                                            请稍等，正在上传... <br><%@include file="/WEB-INF/jspf/lds-loader.jspf" %>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-primary" onclick="addImageFloweryPlugin();" id="upload-icon-button">保存</button>
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal" id="upload-cancel-button">取消</button>
                                        </div>
                                    </div>
                                </div>
                            </div>




                            <br>
                            <!-- - -个性化主页stat- - -->
                            <h3>个性化显示</h3>
                            <br>
                            <!-- Rounded switch 开关-->
                            <label class="switch">
                                <input type="checkbox" id="toggleStatcardsPlugin">
                                <span class="slider round"></span>
                            </label>
                            <br><br>
                            <table class="table table-striped text-center" id="statcardsPlugin">
                                <thead>
                                <td>
                                    登录信息
                                </td>
                                <td>
                                    累计发片
                                </td>
                                <td>
                                    累计收片
                                </td>
                                <td>
                                    发起/参与活动
                                </td>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>
                                            <div class="preview-img-container">
                                            <img onclick="window.open(this.src, '_blank')" id='gicon1' class="je" src="https://cdn.jsdelivr.net/gh/askask11/files@main/log-in.svg" width="50">
                                            </div>
                                        </td>
                                        <td>
                                            <div class="preview-img-container">
                                            <img onclick="window.open(this.src, '_blank')" id='gicon2' class="je" src="https://cdn.jsdelivr.net/gh/askask11/files@main/sendmail.svg" width="50">
                                            </div>
                                        </td>
                                        <td>
                                            <div class="preview-img-container">
                                            <img onclick="window.open(this.src, '_blank')" id='gicon3' class="je" src="https://cdn.jsdelivr.net/gh/askask11/files@main/getmail.svg" width="50">
                                                   </div>
                                        </td>
                                        <td>
                                            <div class="preview-img-container">
                                            <img onclick="window.open(this.src, '_blank')" id='gicon4' class="je" src="https://cdn.jsdelivr.net/gh/askask11/files@main/exchange.svg" width="50">
                                                   </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <input type="number" style="width:50px;" id="giconinput1" class="transparent width-input" oninput="updateStatIcon(this,1);" value="50" >
                                        </td>
                                        <td>
                                            <input  type="number" style="width:50px;" id="giconinput2" class="transparent width-input" oninput="updateStatIcon(this,2);" value="50"  >
                                        </td>
                                        <td>
                                            <input type="number" style="width:50px;" id="giconinput3" class="transparent width-input" oninput="updateStatIcon(this,3);" value="50" >
                                        </td><!-- comment -->
                                        <td>
                                            <input type="number" style="width:50px;" id="giconinput4" class="transparent width-input" oninput="updateStatIcon(this,4);" value="50" >
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <button class="btn btn-primary" onclick="switchInputModeToStatCard(1);" data-toggle="modal" data-target="#addIcon">
                                                更换
                                            </button>
                                        </td>
                                        <td>
                                            <button class="btn btn-primary" onclick="switchInputModeToStatCard(2);" data-toggle="modal" data-target="#addIcon">
                                                更换
                                            </button>
                                        </td>
                                        <td>
                                            <button class="btn btn-primary" onclick="switchInputModeToStatCard(3);" data-toggle="modal" data-target="#addIcon">
                                                更换
                                            </button>
                                        </td>
                                        <td>
                                            <button class="btn btn-primary" onclick="switchInputModeToStatCard(4);" data-toggle="modal" data-target="#addIcon">
                                                更换
                                            </button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <button type="button" class="btn btn-outline-primary btn-block" onclick="saveSetting();">保存</button>
                        </div>
                        <div id='waitingarea' class="text-center" style=''>
                            您好，请稍等...<br><br>
                            <%@include file="/WEB-INF/jspf/lds-loader.jspf" %>
                        </div>
                    </div>


                    <div id="menu1" class="container tab-pane fade"><br>

                        <!--通过片友搜图-->
                        <label for="puy">
                            输入片友的主页链接：
                        </label>
                        <div class="input-group"><input type="text" class="form-control" id="friendurl" placeholder="https://www.icardyou.icu/userInfo/homePage?userId=xxxxx" disabled>
                            <button class="btn btn-primary" onclick="Swal.fire('敬请期待：一键智能防重', '该功能可以一键显示你和某位片友所有发过的片和图片，无需登录后在自己的片目录里面一个一个查找，摆脱繁琐，提高效率，达到防重的目的。此功能正在加班开发中，敬请期待。', 'info');">
                                查找
                            </button></div>


                    </div>
                    <!--<div id="menu2" class="container tab-pane fade"><br>
                      <h3>Menu 2</h3>
                      <p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam.</p>
                    </div>-->
                </div>
            </div>

            <br>

        </c:if>



        <input type="hidden" value="${user.getId()}" id="userid">
        <input type="hidden" value="${user.getUsername()}" id="username">     



        <script>

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

            function createFlowerDataTableRow(imgwidth, src)
            {
                var row = document.createElement("tr");
                var col1 = document.createElement("td");
                var col2 = document.createElement("td");
                var col3 = document.createElement("td");
                var col4 = document.createElement("td");
                var delBtn = document.createElement("button");
                var img = document.createElement("img");
                var imgContainer = document.createElement("div");
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

                delBtn.setAttribute("onclick", "removeFlowerImage(this);");
                delBtn.classList.add("btn");
                delBtn.classList.add("btn-primary");
                delBtn.innerHTML = "删除";

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
                        var row = createFlowerDataTableRow("18", url);
                        const tablebody = document.getElementById("floweryPluginTBody");
                        row.id = "flowertr-" + randomIntFromInterval(1000, 9999);
                        tablebody.appendChild(row);
                        indexFlowerTable(tablebody);
                        Swal.fire("上传成功", "您的图标已添加成功。完成更改后记得保存哦！", "success");
                        document.getElementById("icon-input").value = "";
                        document.getElementById("upload-cancel-button").click();
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
                    var row = createFlowerDataTableRow(imgobj.width, imgobj.src);
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
                    const keys = ["flowers", "statcard"];
                    for (var i = 0, max = keys.length; i < max; i++) {
                        var plugin = json["plugins"][keys[i]];

                        //the plugin JSON object is empty, load the default from HTML.
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
                        //var height = whcol.getElementsByClassName("height-input")[0].value;
                        iconset[iconset.length] = {"src": img.src, "width": width, "height": ""};
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
                var jsonr = {"username": username, "userid": userid}
                var jsonPlugins = {"flowers": getFlowerPlugins(), "statcard": getStatcardPlugins()}

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
        </script>
        <script>
            //submit for search
            function submitForSearch()
            {
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
                const userId = queryString.get("userId");
                if (userId === null || userId === "")
                {
                    //examples would be "https://www.icardyou.icu/userInfo/homePage?alienId=12312" or "https://www.icardyou.icu/userInfo/homePage?userId=", these two would pass the previous check.
                    Swal.fire("主页URL不全,缺少userId", "", "warning");
                    return;
                }


            }

            function deregisterICY()
            {
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
        </script>
    </body>
</html>
