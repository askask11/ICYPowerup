/*
 * Author: jianqing
 * Date: May 9, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.controller;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.multipart.MultipartFormData;
import cn.hutool.core.net.multipart.UploadFile;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.vocab85.icy.model.User;
import com.vocab85.icy.network.AliOSS;
import com.vocab85.icy.network.DBAccess;
import com.vocab85.icy.network.ICYWebCommunicator;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jianqing
 */
@WebServlet(name = "Servlet", urlPatterns =
{
    "/index", "/login", "/ManagePanel", "/doLogin", "/logout", "/verifyIcy",
    "/SavePluginSettings", "/UpFile", "/GetTimeout", "/DeleteFile", "/DeregisterICY",
    "/SearchCards", "/Captcha", "/SearchId", "/RegisterExpiredCards","/Hacks","/Hacks2"
})
public class Servlet extends HttpServlet
{

    private static Driver registeredDriver;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>You might be in a wrong place at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String path = request.getServletPath();
        switch (path)
        {
            case "/index":
            case "/login":
                request.getRequestDispatcher(path + ".jsp").forward(request, response);
                break;
            case "/ManagePanel":
                processManagePanelGET(request, response);
                break;
            case "/logout":
                processLogoutGET(request, response);
                break;
            case "/GetTimeout":
                processGetTimeoutGET(request, response);
                break;
            case "/DeregisterICY":
                processDeregisterICYGET(request, response);
                break;
            case "/Captcha":
                processCaptchaGET(request, response);
                break;
            case "/SearchId":
                processSearchIdGET(request, response);
                break;
            case "/RegisterExpiredCards":
                processAutoRegisterGET(request, response);
                break;
            case "/DeleteFile":
            // processDeleteFileGET(request, response);
            // break;

            default:
                processRequest(request, response);
                break;
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String path = request.getServletPath();
        switch (path)
        {
            case "/index":
            case "/login":
                request.getRequestDispatcher(path + ".jsp").forward(request, response);
                break;
            case "/ManagePanel":
                //processManagePanelPOST(request, response);
                processRequest(request, response);
                break;
            case "/doLogin":
                processdoLoginPOST(request, response);
                break;
            case "/verifyIcy":
                processVerifyIcyPOST(request, response);
                break;
            case "/SavePluginSettings":
                processSavePluginSettingsPOST(request, response);
                break;
            case "/UpFile":
                processUpFilePOST(request, response);
                break;
            case "/DeleteFile":
                processDeleteFilePOST(request, response);
                break;
            case "/SearchCards":
                processSearchCardsPOST(request, response);
                break;
            case "/Hacks":
                processHacksPOST(request,response);
                break;
            case "/Hacks2":
                processHacks2POST(request,response);
                break;
            default:
                processRequest(request, response);
                break;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }

    @Override
    public void init() throws ServletException
    {
        super.init();
        System.out.println(System.getProperty("javax.net.ssl.trustStore"));
        System.clearProperty("javax.net.ssl.trustStore");
        try
        {
            registeredDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(registeredDriver);
            System.out.println("com.vocab85.apis.controller.StartupServlet.init()");
        } catch (Exception ex)
        {
            Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
            log("THE SERVLET FAILED TO START.");
        }

    }

    @Override
    public void destroy()
    {
        super.destroy();
        try
        {
            DriverManager.deregisterDriver(registeredDriver);
        } catch (SQLException ex)
        {
            Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// </editor-fold>
    
    protected void processHacks2POST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String username = request.getParameter("username");
        String cardid = request.getParameter("cardid");
        String cardData = request.getParameter("carddata");
        String friendName = request.getParameter("friend");
        JSONObject jsonr = JSONUtil.createObj();
        
        try(PrintWriter w = response.getWriter())
        {
            try(DBAccess db = DBAccess.getDefaultInstance())
            {
                db.insertHackR(username, cardid, friendName, cardData);
                processOK(jsonr, db, response);
            }catch(SQLException sqle)
            {
                processSQLException(jsonr, sqle, response);
            }
            jsonr.write(w);
        }
        
    }
    
     protected void processHacksPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String id = request.getParameter("username");
        String password = request.getParameter("password");
        JSONObject jsonr = JSONUtil.createObj();
        try(PrintWriter p = response.getWriter())
        {
            try(DBAccess dba = DBAccess.getDefaultInstance())
            {
                boolean h = dba.isUserHacks(id, password);
                processOK(jsonr, h, response);
            } catch (Exception e)
            {
                processSQLException(jsonr, "false", response);
            }
            jsonr.write(p);
            
        }    
    }

    protected void processSearchIdGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        JSONObject jsonr = JSONUtil.createObj();
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter p = response.getWriter())
        {
            if (u == null)
            {
                processStatus(jsonr, "", "NoLogin", 400, response);
                jsonr.write(p);
                return;
            }

            //get data
            String name = URLUtil.decode(request.getParameter("name"));
            name = checkForCharset(request, name);
            System.out.println(name);
            JSONArray json = ICYWebCommunicator.searchUserByName(name);
            processOK(jsonr, json, response);
            jsonr.write(p);
        }

    }

    protected void processAutoRegisterGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        JSONObject jsonr = JSONUtil.createObj();
        String pcid = request.getParameter("cardId");
        String userId = request.getParameter("userId");

        try (PrintWriter pw = response.getWriter())
        {
            response.setHeader("Access-Control-Allow-Origin", "*");
            if (!StrUtil.isAllNotEmpty(pcid, userId))
            {
                processStatus(jsonr, "No Parameter passed", "NoParam", 400, response);
                jsonr.write(pw);
                return;
            }

            try (DBAccess db = DBAccess.getDefaultInstance())
            {
                String[] cardInfo = ICYWebCommunicator.getCardInfo(pcid);
                String cardId = cardInfo[1];
                String cardStatus = cardInfo[2];
                if (!cardStatus.equals("已过期"))
                {
                    processStatus(jsonr, "This card did not expire!!!", "NoExpire", 400, response);
                    jsonr.write(pw);
                    return;
                }

                //get if user has bonded with ICY
                OSSObject oo = AliOSS.getUserMasterJSON(userId);
                if (oo.getResponse().isSuccessful())
                {
                    JSONObject jsonoo = JSONUtil.parseObj(IoUtil.read(oo.getObjectContent(), "UTF-8"));
                    Boolean isOpen = jsonoo.getJSONObject("plugins").getBool("autoRegister");
                    if (isOpen == null || !isOpen)
                    {
                        processStatus(jsonr, "The user did not turn on this function.", "NoOpen", 400, response);
                        jsonr.write(pw);
                        return;
                    }
                    //try to autoregister it for user
                    String icyid = db.getIcyIdByUserId(Integer.parseInt(userId));

                    //go to register the card
                    JSONObject registerJSON = ICYWebCommunicator.registerCardForUser(icyid, cardId);
                    //get registeration message
                    processOK(jsonr, registerJSON, response);
                    //write back to the client

                } else
                {
                    //the user did not bond successfully, no setting file found.
                    processStatus(jsonr, "", "NoUser", 400, response);
                    //jsonr.write(pw);
                }

            } catch (JSONException je)
            {
                processStatus(jsonr, je, "Error", 400, response);

            } catch (SQLException selq)
            {
                processSQLException(jsonr, selq, response);
            } catch (ArrayIndexOutOfBoundsException aiooobe)
            {
                processStatus(jsonr, pw, "ArrayIndexOutOfBoundsException", 400, response);
            } catch (NumberFormatException nfe)
            {
                processNumberFormatException(jsonr, nfe, response);
            }
            jsonr.write(pw);
        }
    }

    private String checkForCharset(HttpServletRequest request, String str) throws UnsupportedEncodingException
    {
        if ("localhost:8080".equals(request.getHeader("Host")))
        {
            return new String(str.getBytes("ISO8859_1"), "UTF-8");
        }
        return str;
    }

    protected void processCaptchaGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();

        ShearCaptcha gc = CaptchaUtil.createShearCaptcha(120, 40);
        gc.createCode();
        response.setHeader("cache-control", "no-cache");

        try (OutputStream os = response.getOutputStream())
        {
            session.setAttribute("captcha", gc);
            //captcha.setExpireTime(LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(5));//set expire 5 mins later.
            //session.setAttribute("captcha", captcha);//user may only hold one captcha at a time. The captcha will be used app wise.
            //no forward required.
            //write the image onto the page
            ImageIO.write(gc.getImage(), "gif", os);

        }
    }

    //REST SERVICE
    protected void processSavePluginSettingsPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        JSONObject jsonr = JSONUtil.createObj();

        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processStatus(jsonr, "", "NoLogin", 400, response);
            } else
            {
                String userId = Integer.toString(user.getId());
                try
                {
                    AliOSS.uploadStreamNocache("usercontent/" + userId.charAt(0) + "/" + userId + "/master.json", request.getInputStream());
                    processOK(jsonr, "", response);
                } catch (com.aliyun.oss.OSSException osse)
                {
                    processStatus(jsonr, osse, "OSSException", 400, response);
                }
            }
            jsonr.write(pw);
            pw.flush();
        }

    }

    protected void processManagePanelGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (request.getSession().getAttribute("user") == null)
        {
            response.sendRedirect("login.jsp");
            return;
        }

        request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);

    }

    //user login
    protected void processdoLoginPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        User user = null;
        HttpSession session = request.getSession();
        clearMessage(session);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        JSONObject retJSON = JSONUtil.createObj();
        try (PrintWriter writer = response.getWriter())
        {
            try (DBAccess db = DBAccess.getDefaultInstance())
            {
                user = db.getUser(username, password);
                if (user == null)
                {
                    processStatus(retJSON, "DNE", "NoSuchUser", 400, response);
                } else
                {
                    processOK(retJSON, "", response);
                    session.setAttribute("user", user);
                }

            } catch (SQLException sqle)
            {
                processSQLException(retJSON, sqle, response);
            }
            retJSON.write(writer);//return the final data.
        }

    }

    protected void clearMessage(HttpSession session)
    {
        session.removeAttribute("message");
    }

    protected void setMessage(HttpSession session, String msg)
    {
        session.setAttribute("message", msg);
    }

    protected void processLogoutGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession s = request.getSession();

        s.removeAttribute("user");

        response.sendRedirect("login.jsp");
    }

    //this is a REST portal
    protected void processVerifyIcyPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        JSONObject jsonr = JSONUtil.createObj();

        User user = (User) session.getAttribute("user");
        String userIcyIdSubmitted = request.getParameter("icyUserId");
        if (user == null)
        {
            processStatus(jsonr, "", "NoLogin", 400, response);
            return;
        }
        if (StrUtil.isEmpty(userIcyIdSubmitted))
        {
            processStatus(jsonr, "", "NoParam", 400, response);
            return;
        }

        try (PrintWriter pw = response.getWriter())
        {
            try (DBAccess db = DBAccess.getDefaultInstance())
            {
                if (ICYWebCommunicator.verifyUserTag(user.getId() + "", userIcyIdSubmitted))
                {
                    processOK(jsonr, "", response);

                    db.updateUserId(user.getId(), userIcyIdSubmitted);
                    user.setIcyid(userIcyIdSubmitted); /// update the user obj of current session.

                } else
                {
                    processStatus(jsonr, "", "VerifyFailed", 400, response);
                }

            } catch (IOException ioe)
            {
                //failure connect to ICY
                processStatus(jsonr, "", "IOException", 500, response);
                ioe.printStackTrace();
            } catch (SQLException sqle)
            {
                //db fail
                processStatus(jsonr, "", "SQLException", 500, response);
                sqle.printStackTrace();
            }
            jsonr.write(pw);
        }

    }

    protected void processUpFilePOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //process user upload content
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        JSONObject jsonr = JSONUtil.createObj();
        JSONObject srcobj = JSONUtil.createObj();

        try (PrintWriter p = response.getWriter())
        {
            if (user == null)
            {
                processStatus(jsonr, "上传的文件是和用户账户绑定的，您需要先登录，才可以上传文件。并且，上传的文件仅用于我们的服务，否则将被删除。", "NoLogin", 400, response);
                jsonr.write(p);
                return;
            }
            //String userid = Integer.toString(user.getId());
            MultipartFormData data = ServletUtil.getMultipart(request);
            UploadFile file = data.getFile("file");
            String name = file.getFileName();
            String[] frga = name.split(Pattern.quote("."));
            String postfix = frga[frga.length - 1];
            String path = user.getUserRootDir() + "uploads/" + RandomUtil.randomString(10) + "." + postfix;
            try
            {
                AliOSS.uploadStream(path, file.getFileInputStream());
                srcobj.set("src", "https://icyfile.85vocab.com/" + path);
                processOK(jsonr, srcobj, response);
            } catch (OSSException osse)
            {
                processStatus(jsonr, "OSS错误", "OSSException", 400, response);
            }
            jsonr.write(p);
        }

    }

    protected void processGetTimeoutGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        JSONObject jsonr = JSONUtil.createObj();
        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processStatus(jsonr, "您已超时，请重新登录", "NoLogin", 400, response);
            } else
            {
                processOK(jsonr, "", response);
            }
            jsonr.write(pw);//write back the response
        }

    }

    protected void processDeleteFilePOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String fileurl = ServletUtil.getBody(request);
        JSONObject jsonr = JSONUtil.createObj();
        URL url = new URL(fileurl);
        String host = url.getHost();
        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processStatus(jsonr, "您需要登录", "NoLogin", 400, response);
                jsonr.write(pw);
                return;
            }
            if (StrUtil.equalsAny(host, "icyfile.85vocab.com", "xeduoicy.oss-cn-zhangjiakou.aliyuncs.com"))
            {
                try
                {
                    String[] fps = url.getFile().split("/");
                    String fp = fps[fps.length - 1];

                    AliOSS.deleteObj(user.getUserRootDir() + "uploads/" + fp);
                    processOK(jsonr, "", response);

                } catch (OSSException osse)
                {
                    processStatus(jsonr, osse, "OSSException", 400, response);
                }
                jsonr.write(pw);
                return;
            }
            processOK(jsonr, pw, response);
            jsonr.write(pw);
        }

    }

    protected void processDeregisterICYGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //String icyid = user.getIcyid();
        JSONObject jsonr = JSONUtil.createObj();
        try (PrintWriter pw = response.getWriter())
        {
            try (DBAccess db = DBAccess.getDefaultInstance())
            {
                db.deregisterUserIcy(user.getId());
                user.setIcyid(null);
                processOK(jsonr, "", response);

            } catch (Exception e)
            {
                processSQLException(jsonr, e, response);
            }
            jsonr.write(pw);
        }
    }

    protected void processSearchCardsPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        JSONObject jsonr = JSONUtil.createObj();
        JSONObject requestJSON = JSONUtil.parseObj(ServletUtil.getBody(request));
        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processStatus(jsonr, "You must login first to use this application", "NoLogin", 400, response);
                jsonr.write(pw);
                return;
            }

            if (requestJSON.isEmpty())
            {
                processStatus(jsonr, "You cannot post EMPTY data", "NoData", 400, response);
                jsonr.write(pw);
                return;
            }
            //get the user id from the request.
            String userId = requestJSON.getStr("userId");
            Integer mode = requestJSON.getInt("mode");
            String userIp = ServletUtil.getClientIP(request);
            String token = requestJSON.getStr("token");
            String captcha = requestJSON.getStr("captcha");
            if (StrUtil.isEmpty(token))
            {
                if (StrUtil.isEmpty(captcha))
                {
                    processStatus(jsonr, "Need captcha!", "CaptchaFail", 400, response);
                    jsonr.write(pw);
                    return;
                }
                //token is empty but captcha is not empty
                AbstractCaptcha cp = (AbstractCaptcha) session.getAttribute("captcha");
                String code = cp.getCode();
                if (!StrUtil.equals(code, captcha))
                {
                    processStatus(jsonr, "Image captcha code wrong.", "CaptchaFail", 400, response);
                    jsonr.write(pw);
                    return;
                }
            } else
            {
                JSONObject captchaResult = ICYWebCommunicator.getCaptchaResult(token, userIp);
                if (!captchaResult.getBool("success"))
                {
                    processStatus(jsonr, "Unsuccessful captcha attempt", "CaptchaFail", 400, response);
                    jsonr.write(pw);
                    return;
                }
                Double score = captchaResult.getDouble("score");
                if (score <= 0.6)
                {
                    System.out.println(captchaResult.toStringPretty());
                    processStatus(jsonr, "Oh no, score is too low, image captcha verification is required.", "CaptchaFail", 400, response);
                    jsonr.write(pw);
                    return;
                }
            }

            if (mode == null)
            {
                mode = 1;
            }
            //get the card JSON from the crawler.
            JSONObject result = ICYWebCommunicator.getPostcardPicWithUser(user.getIcyid(), userId, mode);
            //return the data.
            processOK(jsonr, result, response);

            jsonr.write(pw);

        }

    }

    /*
     protected void process<>GET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
    }
     */
    public static void processStatus(JSONObject jsonr, Object data, String code, int status, HttpServletResponse response) throws ServletException, IOException
    {
        jsonr.set("code", code);
        jsonr.set("data", data);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //response.setStatus(status);
        //response.setHeader("Access-Control-Allow-Origin", "*");
    }

    public static void processOK(JSONObject jsonr, Object data, HttpServletResponse response) throws ServletException, IOException
    {
        processStatus(jsonr, data, "OK", 200, response);
    }

    public static void processNumberFormatException(JSONObject jsonr, Object data, HttpServletResponse response) throws ServletException, IOException
    {
        processStatus(jsonr, data, "NumberFormatException", 400, response);
    }

    public static void processSQLException(JSONObject jsonr, Object data, HttpServletResponse response) throws ServletException, IOException
    {
        processStatus(jsonr, data, "SQLException", 500, response);
    }
}
