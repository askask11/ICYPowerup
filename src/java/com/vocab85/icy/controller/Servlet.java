/*
 * Author: jianqing
 * Date: May 9, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.controller;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.net.multipart.MultipartFormData;
import cn.hutool.core.net.multipart.UploadFile;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.vocab85.icy.model.CrawlUnregisteredPostcardTask;
import com.vocab85.icy.model.ICYPostcard;
import com.vocab85.icy.model.MailUtilBetter;
import com.vocab85.icy.model.UnregisteredPostcardList;
import com.vocab85.icy.model.User;
import com.vocab85.icy.model.UserFavouriteItem;
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
import java.time.LocalTime;
import java.util.HashMap;
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

import static com.vocab85.icy.network.AliOSS.logError;
import java.util.List;

/**
 *
 * @author jianqing
 */
@WebServlet(name = "Servlet", urlPatterns =
{
    "/index", "/login", "/ManagePanel", "/doLogin", "/logout", "/verifyIcy",
    "/SavePluginSettings", "/UpFile", "/GetTimeout", "/DeleteFile", "/DeregisterICY",
    "/SearchCards", "/Captcha", "/SearchId", "/RegisterExpiredCards", "/Hacks", "/Hacks2",
    "/Register", "/Register2", "/ChangePassword", "/ChangeUsername", "/ChangeEmail", "/PasswordRecovery", "/PasswordRecovery2", "/PasswordRecovery3",
    "/GetFavourite", "/UploadICYPhoto", "/AddFavouriteUser", "/GetUnregisteredCards", "/TestIdle", "/TestIdle2", "/SubmitUnregisteredCardTask",
    "/ClearAllUnregistedCard", "/CancelCrawlUC", "/ForgetCrawlCard", "/GuessCardType", "/r/*", "/r", "/CreateRedirect",
    "/ReportAbusePowerup"
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
            out.println("<h1>You might be in a wrong place at " + request.getContextPath() + "</h1>"
                    + "You are in " + request.getServletPath() + ""
                    + "Requested path info " + request.getPathInfo());
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
            case "/":
            case "/index":
            case "/login":
            case "/PasswordRecovery":
            case "/PasswordRecovery2":
                //case "/PasswordRecoveryEmbedded":
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
            case "/GetFavourites":
                processGetFavourites(request, response);
                break;

            case "/DeleteFile":
                // processDeleteFileGET(request, response);
                // break;
                break;
            case "/AddFavouriteUser":
                processAddFavouriteUserGET(request, response);
                break;
            case "/GetUnregisteredCards":
                processGetUnregisteredCardsGET(request, response);
                break;
            case "/SubmitUnregisteredCardTask":
                processSubmitUnregisteredCardTask(request, response);
                break;
            case "/ClearAllUnregistedCard":
                ICYPostcard.USERCRAWL_CACHE.clear();
                response.getWriter().write("OK");
                break;
            case "/CancelCrawlUC":
                processCancelCrawlUCGET(request, response);
                break;
            case "/ForgetCrawlCard":
                processForgetCrawlCardGET(request, response);
                break;
            case "/GuessCardType":
                processGuessCardTypeGET(request, response);
                break;
            case "/r":
                processRedirectGET(request, response);
                //response.getWriter().write("You are fine!" + request.getPathInfo());
                break;
            case "/ReportAbusePowerup":
                processReportAbusePowerupGET(request, response);

            //case "/TestIdle":
            //  processTestIdleGET(request, response);
            //break;
            //case "/TestIdle2":
            //processTestIdle2GET(request,response);
            //  break;
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
                processHacksPOST(request, response);
                break;
            case "/Hacks2":
                processHacks2POST(request, response);
                break;
            case "/Register":
                processRegisterPOST(request, response);
                break;
            case "/Register2":
                processRegister2POST(request, response);
                break;
            case "/ChangePassword":
                processChangePasswordPOST(request, response);
                break;
            case "/ChangeEmail":
                processChangeEmailPOST(request, response);
                break;
            case "/ChangeUsername":
                processChangeUsernamePOST(request, response);
                break;
            case "/PasswordRecovery2":
                processPasswordRecovery2POST(request, response);
                break;
            case "/PasswordRecovery3":
                processPasswordRecovery3POST(request, response);
                break;
            case "/UploadICYPhoto":
                processUploadICYPhotoPOST(request, response);
                break;
            case "/CreateRedirect":
                processCreateRedirectPOST(request, response);
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
        return "The central controller of ICY Powerup.";
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
            logError(ex);
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
            logError(ex);
        }
    }
// </editor-fold>

    protected void processReportAbusePowerupGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        ///receive params from the frontend and pass it to the db
        String powerupIdStr = request.getParameter("powerupId");
        String icyIdStr = request.getParameter("icyId");
        JSONObject jsonr = JSONUtil.createObj();
        int powerupId;
        int icyId;

        try (PrintWriter pw = response.getWriter())
        {
            //parse id into int and check for valid id (no string, etc.)
            try(DBAccess dba = DBAccess.getDefaultInstance())
            {
                powerupId = Integer.parseInt(powerupIdStr);
                icyId = Integer.parseInt(icyIdStr);
                dba.insertIntoReportRecord(powerupId, icyId, new DateTime().toString());
                processOK(jsonr, dba, response);
            } catch (NumberFormatException e)
            {
                processNumberFormatException(jsonr, e, response);
            } catch (SQLException sqle)
            {
                processSQLException(jsonr, sqle, response);
            }
            jsonr.write(pw);
            
        }

    }

    protected void processCreateRedirectPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String destURL = request.getParameter("destURL");
        String key = (String) request.getSession().getAttribute("createRedirect");
        HttpSession session = request.getSession();
        String msg = null;
        String msgen = null;
        boolean showRet = true;
        if (StrUtil.isEmpty(key))
        {
            msg = "会话已过期，请重新扫码或点击返回。";
            msgen = "Session expired. Please scan the QR Code again.";
        } else if (StrUtil.isEmpty(destURL))
        {
            msg = "请输入一个URL";
            msgen = "Please enter a URL.";
        } else if (!Validator.isUrl(destURL.trim()))
        {
            msg = "请输入一个符合格式的URL";
            msgen = "Please enter a valid URL.";
        } else
        {
            try (DBAccess dba = DBAccess.getDefaultInstance())
            {
                dba.updateRedirectRecord(key, destURL.trim());
                msg = "<strong style=\"color:green;\">激活成功！</strong>";
                msgen = "<strong style=\"color:green;\">QR Code Activated!</strong>";
                showRet = false;
            } catch (Exception e)
            {
                msg = "激活失败，原因" + e.toString();
                msgen = "Failed to activate QR Code due to " + e.toString();
            }

        }
        session.setAttribute("message", msg);
        session.setAttribute("message_en", msgen);
        session.setAttribute("showReturn", showRet);
        request.getRequestDispatcher("/WEB-INF/CreateRedirectResult.jsp").forward(request, response);

    }

    /**
     * Process the request to redirect user.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRedirectGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String hash = request.getPathInfo();
        //check for path info
        if (StrUtil.isEmpty(hash))
        {
            session.setAttribute("message", "该链接不存在");
            session.setAttribute("showReturn", false);
            response.setStatus(404);
            request.getRequestDispatcher("/WEB-INF/CreateRedirectResult.jsp").forward(request, response);
            return;
        }
        hash = hash.replace("/", "");
        String destURL = null;
        try (DBAccess dba = DBAccess.getDefaultInstance())
        {
            destURL = dba.getRedirectLink(hash);
            //the request key doesn't exist.
            if (destURL == null)
            {
                session.setAttribute("message", "该链接不存在");
                session.setAttribute("showReturn", false);
                response.setStatus(404);
                request.getRequestDispatcher("/WEB-INF/CreateRedirectResult.jsp").forward(request, response);
                return;

            }

            //the requested key exists, but there is no destination url set.
            if (destURL.isEmpty())
            {

                //send to register page
                request.getSession().setAttribute("createRedirect", hash);
                request.getRequestDispatcher("/WEB-INF/CreateRedirect.jsp").forward(request, response);
                return;
            }

            //request has set a destination URL.
            response.sendRedirect(destURL);

        } catch (Exception e)
        {
            AliOSS.logError(e);
        }
    }

    protected void processAddFavouriteUserGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //initalize parameters
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //receive parameter icyUsername, icyId, icyAvatarUrl from request
        //String icyUsername, icyId, icyAvatarUrl;
        UserFavouriteItem ufi = new UserFavouriteItem();
        JSONObject jsonr = JSONUtil.createObj();
        try (PrintWriter pw = response.getWriter())
        {
            //check if user has logged in
            if (user == null)
            {
                processNoLogin(jsonr, response);
                jsonr.write(pw);
                return;
            }
            //get param userid from session
            ufi.setIcyId(user.getId());
            //get other params from request
            ufi.setIcyUsername(request.getParameter("icyUsername"));
            ufi.setIcyId(Integer.parseInt(request.getParameter("icyId")));
            ufi.setIcyAvatarUrl(request.getParameter("icyAvatarUrl"));

            try (DBAccess dba = DBAccess.getDefaultInstance())
            {
                if (dba.isUserFavouriteExists(ufi.getPowerupId(), ufi.getIcyId()))
                {
                    processStatus(jsonr, "User Favourite Exists", "FavExists", 400, response);
                } else
                {
                    dba.insertIntoUserFavourite(ufi);
                    processOK(jsonr, "", response);
                }

            } catch (SQLException sqle)
            {
                processSQLException(jsonr, sqle, response);
            }
            jsonr.write(pw);
        } catch (Exception e)
        {
            logError(e);
        }
    }

    protected void processUploadICYPhotoPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");

        JSONObject jsonr = JSONUtil.createObj();

        MultipartFormData formData = ServletUtil.getMultipart(request);
        UploadFile icyImage = formData.getFile("image");
        String postcardURL = formData.getParam("postcardURL");
        //String uploader = formData.getParam("uploader");
        String[] pcurlcomp = postcardURL.split("/");
        String posrcardId = pcurlcomp[pcurlcomp.length - 1];
        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processNoLogin(jsonr, response);
                jsonr.write(pw);
                return;
            }

            ///TODO: COMMUNICATE WITH ICY SERVER.
            try
            {
                ICYWebCommunicator.uploadImageICY(icyImage.getFileContent(), icyImage.getFileName(), posrcardId, user.getIcyid(), user.getUsername());
            } catch (IOException ex)
            {
                processStatus(jsonr, ex.getMessage(), "Error", 500, response);
            } finally
            {
                processOK(jsonr, null, response);
            }
            jsonr.write(pw);
        }
    }

    protected void processGetFavourites(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");

        JSONObject jsonr = JSONUtil.createObj();

        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processNoLogin(jsonr, response);
                jsonr.write(pw);
                return;
            }

            try (DBAccess dba = DBAccess.getDefaultInstance())
            {
                JSONArray result = dba.getUserFaviouriteById(user.getId());
                processStatus(jsonr, result, "OK", 200, response);
                jsonr.write(pw);
            } catch (Exception ex)
            {
                ex.printStackTrace();
                processSQLException(jsonr, ex, response);
            }
        }

    }

    protected void processPasswordRecovery3POST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //get required information for password recovery/
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("emailPR");
        String verifyInput = request.getParameter("verify");
        String verifyCode = (String) session.getAttribute("emailVerifyPR");//PR=password recovery
        String password = request.getParameter("password");

        //Session has expired, this is the attribute which the last session inherited here.
        if (StrUtil.isEmpty(email))
        {
            setMessage(session, "抱歉，没有检测到您的邮箱，可能会话已过期，请重试。");
            response.sendRedirect("PasswordRecovery");
            return;
        }

        // Check for email verify code. 
        if (!StrUtil.equals(verifyCode.toUpperCase(), verifyInput.toUpperCase()))
        {
            setMessage(session, "抱歉，邮箱验证码错误，请重试。");
            response.sendRedirect("PasswordRecovery2");
            return;
        }

        try (DBAccess db = DBAccess.getDefaultInstance())
        {
            db.updatePasswordByEmail(email, password);
            session.removeAttribute("message");
            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);

        } catch (Exception e)
        {
            setMessage(session, "出了点问题，请稍候再试。");
            logError(e);
            response.sendRedirect("PasswordRecovery2");
        }

    }

    protected void processPasswordRecovery2POST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        AbstractCaptcha captcha = (AbstractCaptcha) session.getAttribute("captcha");
        String captchaInput = request.getParameter("captcha");

        //check for captcha
        if (!StrUtil.equals(captchaInput.toUpperCase(), captcha.getCode().toUpperCase()))
        {
            session.setAttribute("message", "<strong style='color:red;'>验证码错误，请重试。</strong>");
            response.sendRedirect("PasswordRecovery");
            return;
        }

        if (!isEmail(email))
        {
            session.setAttribute("message", "<strong style='color:red;'>您输入的邮箱地址不正确，请重新输入。</strong>");
            response.sendRedirect("PasswordRecovery");
            return;
        }

        String verify = Integer.toString(RandomUtil.randomInt(100, 999999));

        try (DBAccess dba = DBAccess.getDefaultInstance())
        {

            if (!dba.isUserExists(email))
            {
                setMessage(session, "该邮箱还未注册, 请输入一个已注册的邮箱。");
                response.sendRedirect("PasswordRecovery");
                return;
            }

            MailUtilBetter.sendPasswordRecoveryText(verify, email);
            session.setAttribute("emailVerifyPR", verify);
            session.setAttribute("emailPR", email);
            session.removeAttribute("message");
            session.removeAttribute("captcha");

            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
        } catch (SQLException sqle)
        {
            setMessage(session, "数据库出错了，呜呜呜呜。");
            logError(sqle);
            response.sendRedirect("PasswordRecovery");
        } catch (Exception ex)
        {
            logError(ex);
            setMessage(session, "验证邮件发送失败了，呜呜呜呜呜。");
            response.sendRedirect("PasswordRecovery");
        }

    }

    protected void processChangeUsernamePOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String username = ServletUtil.getMultipart(request).getParam("username");
        JSONObject jsonr = JSONUtil.createObj();
        User user = (User) request.getSession().getAttribute("user");
        try (PrintWriter pw = response.getWriter())
        {
            if (!checkAuth(jsonr, user, response, pw, username))
            {
                return;
            }

            try (DBAccess dba = DBAccess.getDefaultInstance())
            {
                int dr = dba.updateUsername(user.getId(), username);
                processOK(jsonr, dr, response);
                user.setUsername(username);
            } catch (Exception e)
            {

                processSQLException(jsonr, e, response);
            }
            jsonr.write(pw);
        }
    }

    protected void processChangeEmailPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        MultipartFormData mp = ServletUtil.getMultipart(request);
        String email = mp.getParam("email");
        String verify = mp.getParam("verify");

        JSONObject jsonr = JSONUtil.createObj();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String realVerify = (String) session.getAttribute("verifyEmail");
        try (PrintWriter pw = response.getWriter())
        {
            if (!checkAuth(jsonr, user, response, pw, email))
            {
                return;
            }

            if (!isEmail(email))
            {
                processStatus(jsonr, "Illeagl parameter: email", "BadParam", 400, response);
                jsonr.write(pw);
                return;
            }
            try (DBAccess d = DBAccess.getDefaultInstance())
            {
                //check if email exists
                if (d.isUserExists(email))
                {
                    processStatus(jsonr, "The email was registered by another user.", "EmailExists", 400, response);
                    jsonr.write(pw);
                    return;
                }

                if (StrUtil.isEmpty(verify))
                {
                    //the user has not requested a verify, record timestamp and send user a verify.
                    LocalTime timenow = LocalTime.now();
                    LocalTime time = (LocalTime) session.getAttribute("emailVerify-ts");
                    if (time != null)
                    {
                        if (timenow.isBefore(time.plusMinutes(2)))
                        {
                            processStatus(jsonr, "", "TooFrequent", 400, response);
                            jsonr.write(pw);
                            return;
                        }

                    }
                    session.setAttribute("emailVerify-ts", timenow);

                    realVerify = Integer.toString(RandomUtil.randomInt(1000, 9999));
                    session.setAttribute("verifyEmail", realVerify);
                    try
                    {
                        MailUtilBetter.sendVerifyText(user.getUsername(), realVerify, email);
                        processStatus(jsonr, "", "OK-verify", 200, response);
                    } catch (Exception e)
                    {
                        processStatus(jsonr, e, "Exception", 400, response);
                    }

                } else
                {
                    //user respond verify
                    if (StrUtil.equals(verify, realVerify))
                    {
                        int i = d.updateEmail(user.getId(), email);
                        session.removeAttribute("verifyEmail");
                        processOK(jsonr, i, response);
                        user.setEmail(email);
                    } else
                    {
                        processStatus(jsonr, "", "BadVerify", 400, response);
                    }

                }
            } catch (Exception e)
            {
                processSQLException(jsonr, e, response);
            }
            jsonr.write(pw);

        }
    }

    protected void processChangePasswordPOST(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String pass = ServletUtil.getMultipart(req).getParam("password");
        JSONObject jsonr = JSONUtil.createObj();
        User user = (User) req.getSession().getAttribute("user");

        try (PrintWriter pw = resp.getWriter())
        {
            if (!checkAuth(jsonr, user, resp, pw, pass))
            {
                return;
            }

            if (pass.length() != 32)
            {
                processStatus(jsonr, "The password is not md5 encrypted!", "BadParam", 400, resp);
                jsonr.write(pw);
                return;
            }

            int id = user.getId();

            try (DBAccess dba = DBAccess.getDefaultInstance())
            {
                int rowsssss = dba.updatePassword(id, pass);
                processOK(jsonr, rowsssss, resp);
            } catch (SQLException sqle)
            {
                processSQLException(jsonr, sqle, resp);
            }

            jsonr.write(pw);

        }

    }

    protected void processRegister2POST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        HashMap<String, String> userr = (HashMap<String, String>) session.getAttribute("ruser");
        String verify = request.getParameter("verify");

        if (userr == null)
        {
            session.setAttribute("successr", false);
            session.setAttribute("redo", false);
            session.setAttribute("message", "很抱歉，注册失败,<br>"
                    + "原因：<strong>会话已过期</strong><br>"
                    + "请您刷新并重新尝试注册，谢谢。");
            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
            return;
        }

        if (StrUtil.isBlank(verify))
        {
            session.setAttribute("successr", false);
            session.setAttribute("redo", true);
            session.setAttribute("message", "很抱歉，注册失败,<br>"
                    + "原因：<strong>验证码为空</strong><br>"
                    //+ "请您刷新并重新尝试注册，谢谢。"
                    + "");
            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
            return;
        }

        if (!StrUtil.equals(verify.toUpperCase(), userr.get("verify").toUpperCase()))
        {
            session.setAttribute("successr", false);
            session.setAttribute("redo", true);
            session.setAttribute("message", "很抱歉，注册失败,<br>"
                    + "原因：<strong>验证码错误</strong><br>"
                    //+ "请您刷新并重新尝试注册，谢谢。"
                    + "");
            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
            return;
        }

        try (DBAccess dba = DBAccess.getDefaultInstance())
        {
            dba.addUser(RandomUtil.randomInt(999999), userr.get("username"), userr.get("password"), userr.get("email"));
            session.setAttribute("successr", true);
            session.setAttribute("redo", false);
            session.removeAttribute("message");
        } catch (SQLException ex)
        {
            Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
            session.setAttribute("successr", false);
            session.setAttribute("redo", false);
            session.setAttribute("message", "很抱歉，注册失败,<br>"
                    + "原因：<strong>数据库故障</strong><br>"
                    + "请稍候重新尝试注册，谢谢。"
                    + "");
        }
        request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
    }

    protected void processRegisterPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String captcha = request.getParameter("captcha");
        String email = request.getParameter("email");
        String verify = Integer.toString(RandomUtil.randomInt(1000, 9999));

        username = fixCharset(username);
        HttpSession session = request.getSession();
        AbstractCaptcha cap = (AbstractCaptcha) session.getAttribute("captcha");
        //check for empty params
        if (!StrUtil.isAllNotBlank(username, password, captcha, email))
        {
            //fail. return.
            session.setAttribute("successv", Boolean.FALSE);
            session.setAttribute("message", "很抱歉，注册失败,<br>"
                    + "原因：<strong id='reason'>用户信息不全</strong><br>"
                    + "请您刷新并重新尝试注册，谢谢。");
            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
            return;
        }

        //check for captcha code
        if (cap == null || !StrUtil.equals(captcha.toUpperCase(), cap.getCode().toUpperCase()))
        {
            session.setAttribute("successv", false);
            session.setAttribute("message", "很抱歉，注册失败,<br>"
                    + "原因：<strong id='reason'>验证码错误</strong><br>"
                    + "请您刷新并重新尝试注册，谢谢。"
                    + "");
            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
            return;
        }

        session.removeAttribute("captcha");

        //check for duplicate emails
        try (DBAccess dba = DBAccess.getDefaultInstance())
        {
            if (dba.isUserExists(email))
            {
                session.setAttribute("successv", false);
                session.setAttribute("message", "很抱歉，注册失败,<br>"
                        + "原因：<strong id='reason'>该邮箱已被注册</strong><br>"
                        + "请您刷新并重新尝试注册，谢谢。");
                request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
                return;
            }

        } catch (SQLException swle)
        {
            session.setAttribute("successv", false);
            session.setAttribute("message", "很抱歉，注册失败,<br>"
                    + "原因：<strong id='reason'>数据库错误了</strong><br>"
                    + "请您稍候重新尝试注册，谢谢。");
            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
            return;
        }

        try
        {
            //send verificiation code
            MailUtilBetter.sendVerifyText(username, verify, email);
        } catch (Exception d)
        {
            d.printStackTrace();
            logError(d);
            session.setAttribute("successv", false);
            session.setAttribute("message", "很抱歉，注册失败,<br>"
                    + "原因：<strong id='reason'>邮件发送失败或邮箱不合法</strong><br>"
                    + "请您重新尝试注册，谢谢。");
            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
            return;
        }
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("password", password);
        userMap.put("email", email);
        userMap.put("verify", verify);

        //set status
        session.setAttribute("successv", Boolean.TRUE);
        session.setAttribute("ruser", userMap);
        request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);

    }

    protected void processHacks2POST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String username = request.getParameter("username");
        String cardid = request.getParameter("cardid");
        String cardData = request.getParameter("carddata");
        String friendName = request.getParameter("friend");
        JSONObject jsonr = JSONUtil.createObj();

        try (PrintWriter w = response.getWriter())
        {
            try (DBAccess db = DBAccess.getDefaultInstance())
            {
                db.insertHackR(username, cardid, friendName, cardData);
                processOK(jsonr, db, response);
            } catch (SQLException sqle)
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
        try (PrintWriter p = response.getWriter())
        {
            try (DBAccess dba = DBAccess.getDefaultInstance())
            {
                boolean h = dba.isUserHacks(id, password);
                processOK(jsonr, h, response);
            } catch (Exception e)
            {
                processSQLException(jsonr, e, response);
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
        String pcid = request.getParameter("cardId").trim().toUpperCase();
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
                //get if user has bonded with ICY
                OSSObject oo = AliOSS.getUserMasterJSON(userId);
                if (oo.getResponse().isSuccessful())
                {

                    //check user function status, see if user has turned on this function
                    JSONObject jsonoo = JSONUtil.parseObj(IoUtil.read(oo.getObjectContent(), "UTF-8"));
                    Boolean isOpen = jsonoo.getJSONObject("plugins").getJSONObject("autoRegister").getBool("render");
                    if (isOpen == null || !isOpen)
                    {
                        processStatus(jsonr, "The user did not turn on this function.", "NoOpen", 400, response);
                        jsonr.write(pw);
                        return;
                    }

                    //check card status
                    //card info is the one that actually crawl from the web.
                    //go to register the card
                    String cardURLId = db.getCardSeqByCardId(pcid);

                    if (cardURLId == null)
                    {
                        processStatus(jsonr, "The indicated card ID could not be found at the database yet.", "NotFound", 400, response);
                        jsonr.write(pw);
                        return;
                    }
                    //warning

                    String[] cardInfo = ICYWebCommunicator.getCardInfo(cardURLId);
                    if (cardInfo == null)
                    {
                        processStatus(jsonr, "The card ID not in record.", "NotFound", 400, response);
                        jsonr.write(pw);
                        return;
                    }
                    //String cardId = cardInfo[1];
                    String cardStatus = cardInfo[2];
                    String sender = cardInfo[3];
                    String sendDate = cardInfo[4];
                    if (!cardStatus.equals("已过期"))
                    {
                        //the card did not expire.
                        processStatus(jsonr, "This card did not expire!!!", "NoExpire", 400, response);
                        jsonr.write(pw);
                        return;
                    }

                    //try to autoregister it for user
                    User user = db.getUserByUserId(Integer.parseInt(userId));
                    String icyid = user.getIcyid();

                    JSONObject registerJSON = ICYWebCommunicator.registerCardForUser(icyid, pcid);

                    //get registeration message
                    processOK(jsonr, registerJSON, response);

                    //write back to the client
                    //jsonr.write(pw); it is done at the last step.
                    //send an email to the user
                    String userEmail = user.getEmail();

                    if (StrUtil.isNotBlank(userEmail) && registerJSON.getInt("resultCode") == 200)
                    {
                        MailUtilBetter.sendText(user.getEmail(), "【ICY过期自助补登】" + pcid + "已由站内小伙伴自助登记", ""
                                + "您好，\n"
                                + sender + "于" + sendDate + "向您发送的卡片ID：" + pcid + "已由小伙伴通过站内自助补登记功能登记\n"
                                + "特此通知。\n"
                                + ""
                                + "ICY Powerup 敬上");
                    }

                } else
                {
                    //the user did not bond successfully, no setting file found.
                    processStatus(jsonr, "", "NoUser", 400, response);
                    //jsonr.write(pw);
                }

            } catch (JSONException je)
            {
                logError(je);
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

    public static String checkForCharset(HttpServletRequest req, String str) throws UnsupportedEncodingException
    {
        if (req.getHeader("Host").equals("localhost:8080"))
        {
            return fixCharset(str);
        }
        return str;
    }

    public static String fixCharset(String str) throws UnsupportedEncodingException
    {
        return new String(str.getBytes("ISO8859_1"), "UTF-8");
    }

    protected void processCaptchaGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();

        ShearCaptcha gc = CaptchaUtil.createShearCaptcha(120, 40);
        gc.createCode();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

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
                    logError(osse);
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
        String remember = request.getParameter("remember");
        String token = request.getParameter("token");
        JSONObject retJSON = JSONUtil.createObj();
        try (PrintWriter writer = response.getWriter())
        {
            try (DBAccess db = DBAccess.getDefaultInstance())
            {
                //login by token or email or username/password
                if (StrUtil.isNotBlank(token))
                {
                    user = db.getUserByToken(token);
                } else if (isEmail(username))
                {
                    user = db.getUserByEmail(username, password);
                    if (user == null)
                    {
                        //the username is not a valid email, try to fetch with username.
                        user = db.getUser(username, password);
                    }
                } else
                {
                    user = db.getUser(username, password);
                }

                //check if the login is successful
                if (user == null)
                {
                    processStatus(retJSON, "DNE", "NoSuchUser", 400, response);
                    retJSON.write(writer);
                } else
                {
                    session.setAttribute("user", user);
                    processOK(retJSON, "", response);
                    retJSON.write(writer);
                    System.out.println("ok");

                    //remember user
                    if (StrUtil.equals(remember, "true"))
                    {
                        token = RandomUtil.randomString(19);
                        user.setToken(token);
                        db.setUserToken(user.getId(), token);
                    }

                    db.insertUserLoginRecord(user.getId(), DateTime.now().toString());

                }

            } catch (SQLException sqle)
            {
                processSQLException(retJSON, sqle, response);
                retJSON.write(writer);
            }
            //retJSON.write(writer);//return the final data.
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
        User user = (User) s.getAttribute("user");
        String soft = request.getParameter("soft");
        if (user != null && !StrUtil.equals(soft, "true"))
        {
            try (DBAccess db = DBAccess.getDefaultInstance())
            {
                db.clearUserToken(user.getId());
            } catch (Exception e)
            {
                logError(e);
                e.printStackTrace();
            }
        }
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
                logError(ioe);
                processStatus(jsonr, "", "IOException", 500, response);

                ioe.printStackTrace();
            } catch (SQLException sqle)
            {
                //db fail
                logError(sqle);
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
                    logError(osse);
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
                logError(e);
            }
            jsonr.write(pw);
        }
    }

    protected void processSearchCardsPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        //Initalize session variables.
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        JSONObject jsonr = JSONUtil.createObj();
        JSONObject requestJSON = JSONUtil.parseObj(ServletUtil.getBody(request));
        try (PrintWriter pw = response.getWriter())
        {
            //check user login status
            if (user == null)
            {
                processStatus(jsonr, "You must login first to use this application", "NoLogin", 400, response);
                jsonr.write(pw);
                return;
            }
            //check payload data before read
            if (requestJSON.isEmpty())
            {
                processStatus(jsonr, "You cannot post EMPTY data", "NoData", 400, response);
                jsonr.write(pw);
                return;
            }
            //get the user creds from the request.
            String userId = requestJSON.getStr("userId");
            Integer mode = requestJSON.getInt("mode");
            String userIp = ServletUtil.getClientIP(request);
            String token = requestJSON.getStr("token");
            String captcha = requestJSON.getStr("captcha");

            //check either type of captcha: google captcha or image captcha
            if (StrUtil.isEmpty(token))
            {
                //check for image captcha
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
                //check for google captcha, post a request to google server to verify the result
                JSONObject captchaResult = ICYWebCommunicator.getCaptchaResult(token, userIp);
                if (!captchaResult.getBool("success"))
                {
                    processStatus(jsonr, "Unsuccessful captcha attempt", "CaptchaFail", 400, response);
                    jsonr.write(pw);
                    return;
                }
                //check score of captcha
                Double score = captchaResult.getDouble("score");
                if (score <= 0.6)
                {
                    System.out.println(captchaResult.toStringPretty());
                    processStatus(jsonr, "Oh no, score is too low, image captcha verification is required.", "CaptchaFail", 400, response);
                    jsonr.write(pw);
                    return;
                }
            }

            //assign default crawling mode: 我发给TA的片
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

    private UnregisteredPostcardList isUserHasUnregisteredPostcardCache(int icyid)
    {
        List<UnregisteredPostcardList> totalList = ICYPostcard.USERCRAWL_CACHE;
        for (UnregisteredPostcardList unregisteredPostcardList : totalList)
        {
            if (unregisteredPostcardList.getIcyId() == icyid)
            {
                return unregisteredPostcardList;
            }
        }
        return null;
    }

    private CrawlUnregisteredPostcardTask getUserUnregisteredTask(int icyid)
    {
        List<CrawlUnregisteredPostcardTask> tasks = ICYPostcard.CRAWLREG_TASKS;
        for (CrawlUnregisteredPostcardTask task : tasks)
        {
            if (task.getIcyId() == icyid)
            {
                return task;
            }
        }
        return null;
    }

    public void processGetUnregisteredCardsGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String progressOnly = request.getParameter("progressOnly");

        //String icyid = user.getIcyid();
        JSONObject jsonr = JSONUtil.createObj();
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processNoLogin(jsonr, response);
                jsonr.write(pw);
                pw.flush();
                pw.close();
                return;
            }

            String icyIdStr = user.getIcyid();
            if (StrUtil.isEmpty(icyIdStr))
            {
                processNoParam(jsonr, response);
                jsonr.write(pw);
                return;
            }
            //If User has generated a report already.

            if (StrUtil.isEmpty(progressOnly))
            {
                UnregisteredPostcardList cache = isUserHasUnregisteredPostcardCache(Integer.parseInt(icyIdStr));
                if (cache == null)
                {
                    processOK(jsonr, JSONUtil.createArray(), response);
                } else
                {
                    processOK(jsonr, JSONUtil.parseArray(cache), response);
                    jsonr.set("time", cache.getCrawlTime().toString("yyyy-MM-dd HH:mm"));
                }
            } else
            {
                processOK(jsonr, JSONUtil.createArray(), response);
            }

            CrawlUnregisteredPostcardTask task = getUserUnregisteredTask(Integer.parseInt(icyIdStr));
            if (task != null)
            {
                jsonr.set("task", JSONUtil.parseObj(task));
                //j/sonr.set("lastCrewTime", tas)
            }

            jsonr.write(pw);
        }
    }

    public void processGuessCardTypeGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int sequence = Integer.parseInt(request.getParameter("cardSequence"));
        int sender = Integer.parseInt(request.getParameter("sender"));
        int receiver = Integer.parseInt(request.getParameter("receiver"));
        //String icyid = user.getIcyid();
        JSONObject jsonr = JSONUtil.createObj();
        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processNoLogin(jsonr, response);
                jsonr.write(pw);
                pw.flush();
                pw.close();
                return;
            }

            try (DBAccess dba = DBAccess.getDefaultInstance())
            {
                String upperGuess = dba.getPostcardIdBySenderReceiverSq(sequence + 1, receiver, sender);
                String lowerGuess = dba.getPostcardIdBySenderReceiverSq(sequence - 1, receiver, sender);
                JSONObject guesses = JSONUtil.createObj();
                guesses.set("upperGuess", upperGuess);
                guesses.set("lowerGuess", lowerGuess);
                processOK(jsonr, guesses, response);
            } catch (Exception e)
            {
                processSQLException(jsonr, e, response);
            }

            jsonr.write(pw);
        }
    }

    public void processSubmitUnregisteredCardTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        //String icyid = user.getIcyid();
        JSONObject jsonr = JSONUtil.createObj();
        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processNoLogin(jsonr, response);
                jsonr.write(pw);
                pw.flush();
                pw.close();
                return;
            }
            String icyIdStr = user.getIcyid();
            int icyId = Integer.parseInt(icyIdStr);

            //频繁检查，若用户有超级权限则跳过。Frequency Check
            if (!user.isHacks())
            {
                UnregisteredPostcardList li = isUserHasUnregisteredPostcardCache(icyId);
                if (li != null)
                {
                    DateTime lastTime = (DateTime) li.getCrawlTime().clone();
                    DateTime now = DateTime.now();
                    //每个用户每30分钟只能爬一次
                    if (now.isBefore(lastTime.setField(DateField.MINUTE, lastTime.getField(DateField.MINUTE) + 30)))
                    {
                        processStatus(jsonr, "", "TooFrequent", 403, response);
                        jsonr.write(pw);
                        pw.close();
                        return;
                    }
                }
            }

            CrawlUnregisteredPostcardTask task = getUserUnregisteredTask(icyId);
            if (task == null)
            {
                task = new CrawlUnregisteredPostcardTask(user.getId(), icyId);
                ThreadUtil.execAsync(task);
                processOK(jsonr, JSONUtil.parseObj(task), response);
            } else
            {
                processStatus(jsonr, JSONUtil.parseObj(task), "HasTask", 403, response);
            }
            jsonr.write(pw);

        }
    }

    public void processCancelCrawlUCGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        JSONObject jsonr = JSONUtil.createObj();

        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processNoLogin(jsonr, response);
                jsonr.write(pw);
                return;
            }
            //Cancel task in accordance to icyid.
            int icyId = Integer.parseInt(user.getIcyid());
            CrawlUnregisteredPostcardTask t = getUserUnregisteredTask(icyId);
            if (t == null)
            {
                processStatus(jsonr, t, "TaskNotFound", 404, response);

            } else
            {
                t.setHalt(true);
                processOK(jsonr, "", response);

            }

            jsonr.write(pw);

        }
    }

    public void processForgetCrawlCardGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        JSONObject jsonr = JSONUtil.createObj();
        String pcId = request.getParameter("pcid");

        try (PrintWriter pw = response.getWriter())
        {
            if (user == null)
            {
                processNoLogin(jsonr, response);
                jsonr.write(pw);
                return;
            }
            if (StrUtil.isEmpty(pcId))
            {
                processNoParam(jsonr, response);
                jsonr.write(pw);
                return;
            }
            int icyId = Integer.parseInt(user.getIcyid());
            UnregisteredPostcardList cl = isUserHasUnregisteredPostcardCache(icyId);
            int s = cl.size();
            for (int i = 0; i < s; i++)
            {
                if (StrUtil.equals(cl.get(i).getCardId(), pcId))
                {
                    cl.remove(i);
                    break;
                }
            }

            processOK(jsonr, "", response);
            jsonr.write(pw);
        }
    }

    //Future<?> f;
    /*
    private boolean ifIdEx(String id)
    {
        return tasks.stream().anyMatch(task -> (task.getTaskId().equals(id)));
    }
    public void processTestIdleGET(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        PrintWriter pw= response.getWriter();
                
       String id = request.getParameter("id");
        //String haha = this.toString();
        
        
        if(!ifIdEx(id))
        {
            LongTask task = new LongTask(tasks,id);
            ThreadUtil.execAsync(task);
            pw.println("Holy Cow!");
        }else
        {
            pw.print("ID Exists!");
        }
        
        
        pw.flush();
        pw.close();
    }
    public void processTestIdle2GET(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        try(PrintWriter pw = response.getWriter())
        {
            pw.println(this.toString());
            pw.println(tasks.toString());
            //pw.println(Arrays.toString(ThreadUtil.getThreads()));
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
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        //response.setStatus(status);
        //response.setHeader("Access-Control-Allow-Origin", "*");
    }

    public static void processNoParam(JSONObject jsonr, HttpServletResponse resp) throws ServletException, IOException
    {
        processStatus(jsonr, "Required parameters are not fulfilled!", "NoParam", 400, resp);
    }

    public static void processNoLogin(JSONObject jsonr, HttpServletResponse resp) throws ServletException, IOException
    {
        processStatus(jsonr, "You need to login first to use this function. Please login!", "NoLogin", 400, resp);
    }

    public static void processOK(JSONObject jsonr, Object data, HttpServletResponse response) throws ServletException, IOException
    {
        processStatus(jsonr, data, "OK", 200, response);
    }

    public static void processNumberFormatException(JSONObject jsonr, Object data, HttpServletResponse response) throws ServletException, IOException
    {
        processStatus(jsonr, data, "NumberFormatException", 400, response);
    }

    public static void processSQLException(JSONObject jsonr, Throwable data, HttpServletResponse response) throws ServletException, IOException
    {
        logError(data);
        processStatus(jsonr, data, "SQLException", 500, response);
    }

    public static boolean isEmail(String email)
    {
        return ReUtil.isMatch("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", email);
    }

    public boolean checkAuth(JSONObject jsonr, User user, HttpServletResponse resp, PrintWriter pw, String... requiredParams) throws ServletException, IOException
    {
        if (!StrUtil.isAllNotEmpty(requiredParams))
        {
            processNoParam(jsonr, resp);
            jsonr.write(pw);
            return false;
        }

        if (user == null)
        {
            processNoLogin(jsonr, resp);
            jsonr.write(pw);
            return false;
        }

        return true;
    }
}
