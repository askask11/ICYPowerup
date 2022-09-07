<%-- 
    Document   : CreateRedirect
    Created on : May 5, 2022, 2:31:20 AM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>激活二维码</title>
        <!-- Mobile Friendly Tag -->
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!--BS CSS-->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha256-93wNFzm2GO3EoByj9rKZCwGjAJAwr0nujPaOgwUt8ZQ=" crossorigin="anonymous">
        <!--Libraries-->
        <!--JQuery-->
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
        <!--Popper.js-->
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
        <!--Boostrap.js-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js" integrity="sha256-2JaAhvdQnfNMMnhWPauHOZ2k+dzftBmBjjownu3HC4g=" crossorigin="anonymous"></script>
        <!-- Sweet alert -->
        <script src="//cdn.jsdelivr.net/npm/sweetalert2@10"></script>
        <script src="https://cdn.jsdelivr.net/gh/askask11/multi-language@0.1.4/dist/multi-language.min.js" integrity="sha256-xL5PbSBwYLKYaUvdDP2S/lnF9s3oAPm/92GnACZBe2c=" crossorigin="anonymous"></script>
        <script>


            function request(url) {
                if (document.getElementById("upf").value === "")
                {
                    Swal.fire("文件为空", "请选择一个文件后上传/ Empty file.", "warning");
                    return;
                }
                Swal.fire("上传中", "正在上传您的文件，请耐心等候 / File uploading...", "info");
                const Http = new XMLHttpRequest();
                //const url =;
                Http.open("POST", url);
                Http.send(new FormData(document.getElementById("form1")));
                Http.onreadystatechange = (e) => {
                    if (Http.status === 200 && Http.readyState === 4) {
                        console.log(Http.responseText);
                        var jobj = JSON.parse(Http.responseText);
                        var msg = jobj.msg;
                        if (msg === "ok")
                        {
                            Swal.fire("上传成功", "文件上传成功，正在激活二维码/ Uploaded Successfully. Activating QR Code.", "success");
                            var jurl = jobj.picurl;
                            document.getElementById("urlField").innerHTML = jurl;
                            document.getElementById("submit").click();
                        } else
                        {
                            Swal.fire("文件上传失败", msg, "error");
                        }
                    }
                };
            }


        </script>

    </head>
    <body>

        
        <div class="container">
            <br>
            <div style="text-align: right;">
                Language/语言：
                <select class="form-control" id="languageSelect" style="width: inherit;display: inline;">
                    <option value="zh">
                        简体中文
                    </option>
                    <option value="en">
                        English
                    </option>
                </select>
            </div><br>
            <div class="jumbotron">
                <h2 class="text-center" id="bigTitle">
                    激活二维码
                </h2>
                <p id="intro1">在这里确认激活后，下次扫描此二维码时将直接重定向至您在下面输入的URL或照片等文件。</p>

            </div>

            <ul class="nav nav-tabs" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="#sharelink" id="navtag1">通过链接分享</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="#sharefile" id="navtag2">上传文件分享</a>
                </li>
            </ul>
            <div class="tab-content">
                <div id="sharelink" class="container tab-pane active"><br>
                    <p id="introLink">在明信片上分享音乐，电视剧，番剧？直接在网易云、Bilibili、QQ音乐等平台上选择以链接分享，然后将分享链接粘贴到这里即可！</p>
                    <form method="POST" action="../CreateRedirect"  >

                        <div class="form-group">
                            <label for="urlField" id="urlFieldLabel">请输入一个有效的URL</label>
                            <textarea class="form-control" id="urlField" rows="2" placeholder="在这里输入目的URL" name="destURL"></textarea>
                        </div><br>
                        <input name="key" value="${createRedirect}" type="hidden">
                        <button class="btn btn-outline-primary btn-block" id="submit" type="submit">
                            确认
                        </button>
                    </form>
                </div>
                <div id="sharefile" class="container tab-pane fade"><br> 
                    <p id="shareFileIntro">想要分享自己的照片、视频和其它文件？请直接在这里上传（文件大小<50MB)</p>
                    <form id="form1">
                        <div class="form-group">
                            <label for="upf" id="shareFileLabel">上传本地照片、视频. (请勿上传违法违规文件，否则后果自负）</label>
                            <input type="file" class="form-control-file" name="file" id="upf"><br>
                            <button  type="button" class="btn btn-outline-primary btn-block" id="uploadBtn" onclick="request('https://server.1000classes.com/bmcserver/UpHomework');">
                                上传
                            </button>
                        </div>
                    </form>
                </div>
                <!--<textarea placeholder="请输入URL..."></textarea>-->
            </div>
        </div>

        <script>


            (function transl() {
                var xhr = new XMLHttpRequest(); //Create an xhr instance
                var translator = new MultiLanguage("zh", [], document.getElementById("languageSelect")); // define translator, create a default instance

                xhr.open("GET", "../js/CreateRedirectTranslation.json"); // Define target file and HTTP method to use.

                xhr.onreadystatechange = (e) => {
                    if (xhr.readyState === 4 && xhr.status === 200)
                    {
                        var jsonResponse = JSON.parse(xhr.responseText); // get response text and parse it into JSON.
                        translator.addSheet(jsonResponse);
                    }
                }
                xhr.send()
            })();

        </script>
    </body>

</html>
