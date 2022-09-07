<%-- 
    Document   : CreateRedirectResult
    Created on : May 5, 2022, 3:14:44 AM
    Author     : jianqing
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>激活结果</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
        <script src="https://cdn.jsdelivr.net/gh/askask11/multi-language@0.1.4/dist/multi-language.min.js" integrity="sha256-xL5PbSBwYLKYaUvdDP2S/lnF9s3oAPm/92GnACZBe2c=" crossorigin="anonymous"></script>
    </head>
    <body>

        
            
        <div class="container"><br>
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
                <h1 class="text-center" id="mainMessage">
                    ${message}
                </h1>
            </div>

            <c:if test="${showReturn}">
                <br>
                <button class="btn btn-outline-primary btn-block" onclick="history.back();" id="retBtn">
                    返回
                </button>
            </c:if>
        </div>
                <script>
            var translator = new MultiLanguage("zh",[
                {
                    "id":"mainMessage",
                    "langs":{
                        "zh":'${message}',
                        "en":'${message_en}'
                        
                    }
                },{
                    "id":"retBtn",
                    "langs":{
                        "zh":"返回",
                        "en":"Return"
                    }
                }
            ], document.getElementById("languageSelect"));
        </script>
    </body>
</html>
