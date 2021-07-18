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


            .preview-img-container
            {
                overflow: scroll;
                max-width: 300px;
                max-height: 300px;
            }

            .search-friend-container
            {

                height: 100%;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                align-items: center;

            }

            .search-friend-caption
            {
                margin-top: 16px;

            }

            .colParent
            {

                margin-bottom: 30px;
            }
            .grecaptcha-badge { visibility: hidden; }
            .footer
            {

                width: 100%;
            }
            /* #searchResult
             {
                 
                 
                 height: auto;
                 
                 background-color: #f5f5f5;
             }
 
             #searchResult div
             {
 
                 line-height: 30px;
                 padding: 2px;
                 border-bottom: solid 1px #FFF;
             }
             
             #searchResult div input
             {
                 margin-left: 10px
             }
 
             #searchResult div span
             {
                 margin-left: 10px;
             }
             
             #searchResult div img
             {
                 width: 30px;
                 height: 30px;
                 margin-left: 10px;
             }*/

            .preview-cf-img
            {
                width:30px;
            }
        </style>

        <script src="https://cdn.jsdelivr.net/gh/isocra/TableDnD@master/dist/jquery.tablednd.1.0.5.min.js"></script>
        <script src="https://www.recaptcha.net/recaptcha/api.js?render=6LdSILoZAAAAAF2f6ntG72Dj2gG2jNEqg8Q3Gpjh" async defer></script>
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
                        <br>
<button class="btn btn-outline-primary" onclick="saveSetting();" style="float:right;">
    保存设置
</button>
<br><br>
<h3>主页特效</h3> 
                        <br><!-- Table for management -->
                        <!-- Rounded switch -->
                        <%--管理桌面--%>
                        <div id='settings' style="display: none;">
                            <label class="switch">
                                <input type="checkbox" id="toggleFlowerPlugin">
                                <span class="slider round"></span>
                            </label>

                            <br>
                            落花特效个性化：(建议同时开启10个以下)
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
                                    开关/删除
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
                                            <label class="switch">
                                                <input type="checkbox" checked class="switchControl">
                                                <span class="slider round"></span>
                                            </label>
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
                                            <label class="switch">
                                                <input type="checkbox" checked class="switchControl">
                                                <span class="slider round"></span>
                                            </label>
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
                                            <label class="switch">
                                                <input type="checkbox" checked  class="switchControl">
                                                <span class="slider round"></span>
                                            </label>
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
                                            <label class="switch">
                                                <input type="checkbox" checked  class="switchControl">
                                                <span class="slider round"></span>
                                            </label>
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
                                            <label class="switch">
                                                <input type="checkbox" checked  class="switchControl">
                                                <span class="slider round"></span>
                                            </label>
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
                                            <label class="switch">
                                                <input type="checkbox" checked class="switchControl">
                                                <span class="slider round"></span>
                                            </label>
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
                                            <label class="switch">
                                                <input type="checkbox" checked class="switchControl">
                                                <span class="slider round"></span>
                                            </label>
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
                                            <label class="switch">
                                                <input type="checkbox" checked class="switchControl">
                                                <span class="slider round"></span>
                                            </label>
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
                                            <label class="switch">
                                                <input type="checkbox" checked class="switchControl">
                                                <span class="slider round"></span>
                                            </label>
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
                                            <label class="switch">
                                                <input type="checkbox" checked class="switchControl">
                                                <span class="slider round"></span>
                                            </label>
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
                                                    <small>上传的图片将会公众可见，请尊重版权，禁止上传违法文件。如有违反，用户承担一切后果。我们保留移除任何我们认为不恰当的内容的权利。</small>
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
                            <!-- Modal -->
                            <div class="modal fade" id="customizeModal" tabindex="-1" role="dialog" aria-labelledby="cmModalLabel" aria-hidden="true">
                              <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                  <div class="modal-header">
                                    <h5 class="modal-title" id="cmModalLabel">个性化显示功能</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                      <span aria-hidden="true">&times;</span>
                                    </button>
                                  </div>
                                  <div class="modal-body">
                                      开启后，我们会优化你的主页的个人数据显示方式，由原来的<br>
                                      <img src="img/oldstat.png" alt="老数显" style="width:100%;" onclick="window.open(this.src, '_blank');"/>
                                      变成↓
                                      <img src="img/newstat.png" alt="新数显" style="width:100%;" onclick="window.open(this.src, '_blank');"/>
                                      显示的四张图片可以在这里进行自定义，您可上传自己喜欢的图片和调整大小。<br>
                                      <strong>注意：</strong>此功能仅优化排版显示，<strong>不能</strong>更改数据。<br>上传的图片将在互联网完全公开，请尊重版权，请勿上传违法图片。我们有权
                                      删除任何我们认为不恰当的内容，但我们不对上传的内容负责。
                                  </div>
                                  <div class="modal-footer">
                                    <button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
                                    
                                  </div>
                                </div>
                              </div>
                            </div>
                            <!-- - -个性化主页stat- - -->
                            <h3>个性化显示  <img alt="?" width="16" src="https://xeduocdn.sirv.com/icons/q.png" style="cursor:pointer;" data-toggle="modal" data-target="#customizeModal"></h3>
                            <br>
                            <!-- Rounded switch 开关-->
                            <label class="switch">
                                <input type="checkbox" id="toggleStatcardsPlugin">
                                <span class="slider round"></span>
                            </label>
                            <br><br>
                            <div style="max-width:100%; overflow: scroll;">
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
                            </div>
                            <!--自助补登记开关，默认关-->
                            <br>
                            <h4>
                                自助补登记  <img width="16" src="https://xeduocdn.sirv.com/icons/q.png" style="cursor:pointer;" data-toggle="modal" data-target="#mdex">
                            </h4>
                            
                            
                            是否允许小伙伴自助补登记发给你的过期明信片？
                             <label class="switch">
                                <input id="autoRegister" type="checkbox" class="switchControl">
                                <span class="slider round"></span>
                            </label>
                            <br>
                            
                            
                          <h4>
                              "选择困难小助手" <img width="16" src="https://xeduocdn.sirv.com/icons/q.png" style="cursor:pointer;" data-toggle="modal" data-target="#explainLottery">
                           </h4>
                            
                            <!--小助手开关-->
                            开启并设置以后，你的主页可以实现简单的随机选择功能。<label class="switch">
                                <input id="icyLotterySwitch" type="checkbox" class="switchControl">
                                <span class="slider round"></span>
                            </label>
                            <br><br><br>
                            <%--关于此插件的解释--%>
                           <div id="explainLottery" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="explainLotteryTitle" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                              <div class="modal-content">
                                <div class="modal-header">
                                  <h5 class="modal-title" id="explainLotteryTitle">选择困难小助手</h5>
                                  
                                  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                  </button>
                                </div>
                                <div class="modal-body">
                                   
                                        开启并设置以后，你的主页可以实现简单的随机选择功能。
                                        
                                    <hr>
                                    <strong style="line-height:5px;">设置方法：</strong><br>
                                        比如，你希望抽取的列表是 《今晚吃什么》
                                   
                                   <ul>
                                       <li>
                                           香香的红烧排骨
                                       </li>
                                       <li>
                                           辣辣的剁椒鱼头
                                       </li>
                                       <li>
                                           凉凉的西北风
                                       </li>
                                   </ul>
                                    
                                   
                                   
                                    在ICY个人主页设置中，编辑文本如下：<br>
                                    <textarea style="height:140px; width:100%;" readonly >
<div class="lot-topics" data-control="up">
香香的红烧排骨
辣辣的剁椒鱼头
凉凉的西北风
</div>
                                            </textarea><br>
                                            里面的三行换成你希望抽取的列表内容，一行一项。如果某一行太长了被"顺"到下一行了，没关系，只要不是回车敲的换行，都算同一行。
                                            <br><!-- comment --><br>
                                            <strong>解释：</strong><br><code>data-control="up"</code>意思是，抽奖按钮放上方。
                                            <br>如果你希望抽奖按钮出现在下面，请换成
                                            <code>data-control="down"</code><br>
                                            我已经尽力把大家的打字量降到最小，为了方便大家使用，未来会上线代码生成器。<br>
                                            如果希望关闭此功能，在此设置后抽奖功能将会立即关闭，按钮也不会显示，不需要对主页做任何修改。
                                            这样想开启时无需再动代码，直接开启即可。
                                            <br>
                                            <a href="https://www.icardyou.icu/message/messageDetail?targetUserId=32364" target="_blank">仍然有问题？点此站内信我</a>
                                            <hr>
                                            <small>该功能仅作为娱乐，结果由电脑随机抽取，我们不会对列表内容和最终结果负责。</small>
                                            <br>
                                        </div>
                                        <div class="modal-footer">

                                            <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- FEP UP-->





                            <!--More functions, add above. ABSOLUTE END OF THE PANEL-->

                            <button type="button" class="btn btn-outline-primary btn-block" onclick="saveSetting();">保存</button>
                        </div>

                        <!-- explanation of what the hack is function -->
                        <div id="mdex" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="mdlb2" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="mdlb2">自助补登记功能</h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <p>
                                            开启后，你的主页底部会出现一个表单（一个输入框和一个按钮），小伙伴可以通过
                                            这里输入过期明信片的URL。该URL会被提交到我们的系统，然后系统会识别该明信片是否过期。
                                            如果该明信片已过期，我们将会帮您完成此明信片的登记工作。<br>
                                            如果小伙伴尝试登记的明信片实际未过期，将会登记失败。<br>
                                            示例(实际效果在每台电脑上可能不同)：
                                        </p>
                                        <div class="form-inline"><div class="form-group" style="width: 100%;"><label class="control-label">过期自助补登：</label><input class="form-control" type="text" placeholder="输入过期明信片页面URL" id="expiredCD" style="width: 80%; margin-right: 10px;"><button class="btn btn-primary" onclick="Swal.fire('提交明信片进行补登', '小伙伴点击这个按钮以后，我们会验证该明信片的状态，如果实际过期，将会进行登记。', 'success');">提交</button></div></div>
                                        <p>
                                            <br>
                                            该功能默认关闭。如果你希望小伙伴能够通过这样的表单自助登记过期明信片，请开启此功能。<br>
                                            如果你不希望小伙伴自助登记过期明信片，请关闭此功能。<br>
                                            <strong>注意：</strong>此功能需要使用你的ICY账号进行登记操作。开启此功能，代表你同意我们使用你的ICY账号进行必要的查询和登记过期明信片。<br>
                                        </p>
                                    </div>
                                    <div class="modal-footer">

                                        <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
                                    </div>
                                </div>
                            </div>
                        </div>




                        <div id='waitingarea' class="text-center" style=''>
                            您好，请稍等...<br><br>
                            <%@include file="/WEB-INF/jspf/lds-loader.jspf" %>
                        </div>
                    </div>


                    <!-- 
                    
                    
                    ******
                    
                    一键防重
                    
                    ******
                    
                    -->
                    <div id="menu1" class="container tab-pane fade"><br>
                        <img width="16" src="https://xeduocdn.sirv.com/icons/q.png" style="cursor:pointer;" data-toggle="modal" data-target="#mdyjfc">

                        <!-- 一键防重介绍帮助 -->
                        <div class="modal fade" id="mdyjfc" tabindex="-1" role="dialog" aria-labelledby="mdyjfclabel" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="mdyjfclabel">一键防重</h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <h4>一键防重是什么？</h4><!-- comment -->
                                        一键防重可以方便地把你和任意一位片友所交换过的明信片列在一个页面上，方便你查询和这位片友所有交换片的照片。
                                        <br>这里只能查询到在ICY上存在编号的，存在照片的明信片。不存在照片的明信片将会被忽略。
                                        <br><!-- comment -->
                                        <h4>怎么搜索片友？</h4>
                                        您可以通过一个片友的主页URL来定位片友，也可以通过昵称搜索定位片友。<br>
                                        如果通过主页URL定位，只需把片友主页URL粘贴即可。
                                        <br>如果通过昵称定位片友，请在"搜索条件"选择昵称，然后输入片友的昵称。点击"搜索片友"，系统会找出前10位符合
                                        搜索条件的小伙伴显示在名单上，和他们的头像。 点击左侧的单选框选择小伙伴，
                                        然后点击"确认并查找"即可查找和该片友换过的片。
                                        <h4>声明</h4>
                                        使用此功能，代表您同意我们ICY Powerup系统使用您的ICY账号查询您的历史收发记录和片友信息。<br>
                                        该功能仅做便民查询，和Powerup官方无关。<br>
                                        为保证服务质量，该功能由谷歌隐形验证码保护，继续使用代表您同意谷歌(Google)的<a href="https://policies.google.com/privacy" target="_blank">
                                            隐私政策
                                        </a>以及<a href="https://policies.google.com/terms" target="_blank">
                                            服务条款
                                        </a>

                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        搜索条件：
                        <input type="radio" id="st1" name="st" class="stsel" checked>
                        <label for="st1">
                            主页链接
                        </label>

                        <input type="radio" id="st2" name="st" class="stsel">
                        <label for="st2">
                            昵称（支持模糊搜索）
                        </label>
                        <br>
                        显示明信片类别：
                        <select id="searchFilter">
                            <option value="1" selected>
                                我发给Ta的
                            </option>
                            <option value="2">
                                Ta发给我的
                            </option>
                            <option value="3">
                                所有交换过的
                            </option>
                        </select>
                        <div id="st2-div" style="display:none;">
                            <div class="input-group">
                                <input type="text" class="form-control st2" id="friendid" placeholder="输入昵称搜片友">
                                <button onclick="searchForId()" class="btn btn-success st2" id="sfcfcfbt">
                                    搜索片友
                                </button><!-- 搜索结果 -->
                            </div>
                            <table class="table table-striped">
                                <thead>
                                <td>
                                    选择
                                </td> 
                                <td>
                                    头像
                                </td> 
                                <td>
                                    昵称
                                </td> 

                                </thead>
                                <tbody id="searchResult">
                                    <tr>
                                        <td>

                                        </td> 
                                        <td>

                                        </td> 
                                        <td>

                                        </td> 

                                    </tr>
                                </tbody>
                            </table>
                            <br><!-- comment -->
                            选择完毕后点击：
                            <button class="btn btn-primary" onclick="submitSearchCardClick();">
                                确认并查找
                            </button>
                        </div>

                        <div id="st1-div">



                            <div  class="animated-text-input-container text-center" style="margin: auto; width:100%;">
                                <input id="friendurl" type="text" required title="好友主页URL">
                                <label class="label-name"><span class="content-name">输入片友主页URL:</span></label>

                                <!--
                                <input type="text" class="form-control st1" id="friendurl" placeholder="https://www.icardyou.icu/userInfo/homePage?userId=xxxxx" autocomplete="on" >
                                <!--<img src="Captcha" onclick="this.src = 'Captcha'"><input id="searchCaptcha" size="4" placeholder="验证码">-->                  

                            </div> 
                            <button class="btn btn-primary" onclick="submitSearchCardClick();">
                                查找
                            </button>
                        </div>
                        <br>

                        <p>
                            这是<span id="sender">你</span>和<span id="xhbid">这位小伙伴</span>交换过的片(已上传照片的)：
                        </p>
                        <hr>
                        <div id="pylink" class="text-center">

                        </div>

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



        <script src="js/indexSettings.js"></script>


        <script src="js/searchCards.js"></script>
        <!--<footer class="translucent text-center footer">
            This site is protected by reCAPTCHA and the Google
            <a href="https://policies.google.com/privacy">Privacy Policy</a> and
            <a href="https://policies.google.com/terms">Terms of Service</a> apply.
        </footer>-->
        <c:if test="${user.getToken()!=null}">
            <script>
                               localStorage.setItem("token", "${user.getToken()}");
            </script>
        </c:if>
    </body>
</html>
