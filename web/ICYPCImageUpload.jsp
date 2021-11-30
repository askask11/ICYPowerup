<%-- 
    Document   : ImageUploader
    Created on : Sep 18, 2021, 6:45:57 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        ${user==null?"<meta http-equiv='refresh' content='0; url=index?next=ICYPCImageUpload.jsp'>":""}
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

        </style>
        <title>ICY PC Image Upload</title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/nav.jspf" %>
        <br><!-- comment -->

        <div class="container translucent" style="border-radius: 6px;">
            <br>
            <h2>
                ICY PC Image Uploader
            </h2>
            <br>
            <h4>Upload Images for Postcard Overseas!</h4>
            <br>
            <div id="f1" class="animated-text-input-container text-center" style="margin: auto; width:100%;">
                <input id="postcardURL" type="url" required title="Postcard URL" name="url">
                <label class="label-name"><span class="content-name">ICY Postcard URL</span></label>
            </div>
            <!--<div id="f2" class="animated-text-input-container text-center" >
                <input id="uploader" type="url" required title="Uploader" name="uploader" value="${user.getUsername()}">
                <label class="label-name"><span class="content-name">Uploader</span></label>
            </div>-->
            
            <br>
            <input type="file" id="imgInput">
            <br>
            <br>
            <button class="btn btn-primary" onclick="uploadImage();">
                Upload
            </button>

            <!-- Status Done! -->

            <div id="done" class="centralized" style="display:none;">
                <strong style="color: green;">
                    Image has been uploaded! <a id="pclink">View Postcard</a>
                </strong>
            </div>

            <div id="loader" class="centralized" style="display:none;">
                Please Wait....
                <%@include file="/WEB-INF/jspf/lds-loader.jspf" %>
            </div>

            <!--End of page-->
            <br><br>
        </div>
        <script>
            function uploadImage()
            {
                //check input
                const fileInput = document.getElementById("imgInput").value;
                const postcardURL = document.getElementById("postcardURL").value;
                //var uploader = document.getElementById("uploader").value;
                //uploader = uploader===""?"ICY Powerup":uploader;

                if (fileInput === "" || postcardURL === "")
                {
                    Swal.fire("Missing Required Info","Please fill out all required fields.","error");
                    return;
                }
                
                $("#loader").fadeIn("slow");
                $("#done").fadeOut("slow");
                //make it in form data and upload

                let photo = document.getElementById("imgInput").files[0];  // file from input
                let req = new XMLHttpRequest();
                let formData = new FormData();

                formData.append("image", photo);
                formData.append("postcardURL", postcardURL);
                //formData.append("uploader",uploader);
                
                req.open("POST", 'UploadICYPhoto'); ////Send to backend.
                req.send(formData);
                
                req.onreadystatechange = (e)=>{
                    if(req.readyState === 4 && req.status === 200)
                    {
                        $("#loader").fadeOut("slow");
                        $("#done").fadeIn("slow");
                        $("#postcardURL").attr({"href":postcardURL});
                        var json = JSON.parse(req.responseText);
                        if(json["code"]==="OK")
                        {
                            Swal.fire("Success!","The image has been uploaded to ICY server!","success");
                            return;
                        }
                        Swal.fire("Ooops!",json.msg,"warning");
                    }else if(req.readyState === 4)
                    {
                        $("#loader").fadeOut("slow");
                        Swal.fire("Fatal Error","Failed to upload image.","error");
                    }
                };
            }
        </script>


    </body>
</html>
