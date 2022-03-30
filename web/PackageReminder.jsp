<%-- 
    Document   : PackageReminder
    Created on : Mar 4, 2022, 6:17:25 PM
    Author     : jianqing
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Package Reminder</title>
        <%@include file="/WEB-INF/jspf/head.jspf" %>
    </head>
    <body>
        <br>
        <h1>Tracker Panel</h1>
        <br>
        <div class="container">

            <c:if test="${empty user}">
                <h2>Please <a href="index">login</a> before continuing.</h2>
            </c:if>

            <form>
                Add new package
                <!--Tracking #-->
                <div class="form-group">
                    <label for="trackingnum">Tracking #:</label>
                    <input name="tracking" class="form-control" id="trackingnum">
                    <small class="form-text text-muted">The number in system for tracking the package.</small>
                    
                </div>
                <!-- Platform and id -->
                <div class="form-group">
                    <label for="platformId">Platform ID:</label>
                    <input name="platformId" class="form-control" id="platformId" style="display: inline; width: auto;">
                    
                    <select class="form-control" name="platform" style="display: inline; width: auto;">
                        <option value="icy">
                            I Card You
                        </option>
                        <option value="PC">
                            Postcrossing
                        </option>
                    </select>
                    <br>
                    <small class="form-text text-muted">The platform with this card.</small>
                    
                </div>
                <button class="btn btn-success">Submit</button>
            </form>
                
                
        </div>
    </body>
</html>
