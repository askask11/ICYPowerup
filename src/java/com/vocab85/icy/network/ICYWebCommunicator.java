/*
 * Author: jianqing
 * Date: May 10, 2021
 * Description: This document is created for icy web communicator.
 */
package com.vocab85.icy.network;

import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import com.vocab85.icy.controller.Servlet;
import com.vocab85.icy.model.ICYPostcard;
import com.vocab85.icy.model.ICYUser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
//import org.jboss.weld.context.http.Http;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jianqing Gao
 */
public class ICYWebCommunicator
{

    public static boolean verifyUserTag(String powerupid, String userId) throws IOException
    {
        System.clearProperty("javax.net.ssl.trustStore");
        // System.out.println(System.getProperty("javax.net.ssl.trustStore"));
        String url = "https://www.icardyou.icu/userInfo/homePage?userId=" + userId;
        String content = HttpUtil.downloadString(url, "UTF-8");
        Document document = Jsoup.parse(content);
        Element scriptTag = document.getElementById("powerup-script");
        //no such element, the user didn't input the element there.
        if (scriptTag == null)
        {
            return false;
        }

        String userIdDetected = scriptTag.attr("data-powerup-id");

        return StrUtil.equals(userIdDetected, powerupid);
    }

    public static String getUsernameByIcyId(String icyid)
    {
        String url = "https://www.icardyou.icu/userInfo/homePage?userId=" + icyid;
        String content = HttpUtil.downloadString(url, "UTF-8");
        Document document = Jsoup.parse(content);
        Element h3 = document.getElementsByTag("h3").get(0);
        return h3.html().trim();
    }

    public static ICYUser getICYUserProfile(String userId)
    {
        ICYUser u = new ICYUser();
        u.setId(userId);
        //crawl user page

        String url = "https://www.icardyou.icu/userInfo/homePage?userId=" + userId;
        String content = HttpUtil.downloadString(url, "UTF-8");
        Document document = Jsoup.parse(content);

        //get user's Username
        Element h3 = document.getElementsByTag("h3").get(0);
        u.setUsername(h3.html().trim());

        //get user's avatar address
        Element avatarImage = document.getElementsByTag("img").get(2);
        u.setAvatarUrl(avatarImage.attr("src"));

        //get user's power up 
        Element scriptTag = document.getElementById("powerup-script");
        //no such element, the user didn't input the element there.
        if (scriptTag != null)
        {
            u.setPowerupId(scriptTag.attr("data-powerup-id"));
        }

        return u;

    }

    private static int getLastPageFromURI(String[] urifrgs)

    {
        for (int i = 0; i < urifrgs.length; i++)
        {
            String urifrg = urifrgs[i];
            if (urifrg.contains("nowPage"))
            {
                return Integer.parseInt(urifrg.split("=")[1]);
            }
        }
        return 1;
    }

    public static Elements getPostcardPicURL(String pcurl)
    {
        HttpRequest req = HttpUtil.createGet(pcurl);
        HttpResponse resp = req.execute();
        Document document = Jsoup.parse(resp.body());
        Elements cardImageContainer = document.getElementsByClass("cardImg");
        if (cardImageContainer.isEmpty())
        {
            return new Elements();
        }
        return cardImageContainer.get(0).getElementsByTag("img");
    }

    private static void crawlAllPostcardsWithUser(String authCookie, String targetId, String pageNum, String mode, JSONArray jsonrarray)
    {
        //GO TO each page.
        HttpRequest req = HttpUtil.createGet("https://www.icardyou.icu/sendpostcard/myPostCard/" + mode + "?status=&cardType=&nowPage=" + pageNum);
        req.cookie(authCookie);//set user auth cookie
        HttpResponse resp = req.execute();
        Document document = Jsoup.parse(resp.body());
        Elements trs = document.getElementsByTag("tr");//get all table rows of the "send postcard page"
        for (int j = 1; j < trs.size(); j++)//skip the first row, it is the table header.
        {
            //get ONE row from the table
            Element tr = trs.get(j);
            //get the 4th column of the row, 
            Element usernametd = tr.getElementsByTag("td").get(3);
            //read the userid from href (<a> tag), which is the username.
            String href = usernametd.getElementsByTag("a").attr("href");
            //split the URI with the question mark, the query should only has the userId
            String[] hrefrags = href.split(Pattern.quote("?"));
            //replace the varible name part of user id, get the pure id.
            String stuid = hrefrags[1].replace("userId=", "").trim();

            //check if the user id is what we are looking for(target user)
            //the condition of if to crawl the image of this postcard.
            if (stuid.equals(targetId))
            {
                // Get the 
                Element pcele
                        = tr.getElementsByTag("td")// get all columns of table
                                .get(1) // get the 2nd column (the postcard id)
                                .getElementsByTag("a")// get the 1st element <a> tag of postcard id (should only have 1)
                                .get(0);//the id of the card,

                // Get the postcard ID of the cards
                String pcid = pcele.html().trim();
                // Get the URI to visit the postcard.
                String pcurl = pcele.attr("href");
                // make relative URI to absolute URL by adding the host
                pcurl = "https://www.icardyou.icu" + pcurl;
                // call the function to get all the pictures of the postcard.
                Elements piclist = getPostcardPicURL(pcurl);
                // Load all the images into the arraylist, each image is an indivisual object but share same ID
                for (Element piclist1 : piclist)
                {
                    //create JSON object and put crawled data inside.
                    JSONObject jsonr = JSONUtil.createObj();
                    jsonr.set("id", pcid);
                    jsonr.set("imgsrc", piclist1.attr("src"));
                    jsonr.set("pcsrc", pcurl);
                    jsonrarray.add(jsonr);
                }

            }

        }
    }

    private static int getUserMaxPostcardPageNum(String cookie, String mode)
    {
        // Try to visit the first page of user's send card, determine how many pages user has.
        HttpRequest req = HttpUtil.createGet("https://www.icardyou.icu/sendpostcard/myPostCard/" + mode + "?status=&cardType=&nowPage=1");
        req.cookie(URLEncoder.createDefault().encode(cookie, Charset.forName("UTF-8")));
        HttpResponse resp = req.execute();
        Document document = Jsoup.parse(resp.body());//parse the document.
        //System.out.println(resp.body());
        // get the ">>" element, containing the URL for the last page of user.

        Elements lastpageElementset = document.getElementsByAttributeValue("title", "最后一页");
        if (lastpageElementset.size() < 1)
        {
            return 1;
        }
        Element lastpage = lastpageElementset.first();
        // the last page is hidden in the URL of the button.
        String href = lastpage.getElementsByTag("a").get(0).attr("href");
        int pageNum = getLastPageFromURI(href.split("&"));//Find the "lastpage" from the URI query.
        return pageNum;
    }

    public static String crawlCardType(String cardId, String icyId)
    {
        HttpRequest req = HttpUtil.createGet("https://www.icardyou.icu/sendpostcard/myPostCard/1");
        String cookie = URLEncoder.createDefault().encode(getAuthCookie(icyId, "--"), Charset.forName("UTF-8"));
        req.cookie(cookie);
        HttpResponse resp = req.execute();
        Document doc = Jsoup.parse(resp.body());
        int maxPage = 1;
        //the "last page" button
        Element ele= doc.selectFirst("body > div.usercenter-container > div.main > div > div > div > nav > ul > li:nth-child(12) > a");
        if(ele!=null)
        {
            String hrefA = ele.attr("href");
            String[] frags = hrefA.split("=");
            maxPage = Integer.parseInt(frags[frags.length-1]);
        }
        
        for (int i = 1; i <= maxPage; i++)
        {
            req = HttpUtil.createGet("https://www.icardyou.icu/sendpostcard/myPostCard/1?status=&cardType=&nowPage=" + i);
            req.cookie(cookie);
            resp = req.execute();
            doc = Jsoup.parse(resp.body());
            Elements eles = doc.getElementsMatchingOwnText(cardId);
            if(!eles.isEmpty())
            {
                ele = eles.first();
                return ele.parent().parent().child(0).html();
                 
            }
        }
        return null;
    }
    
    

    private static void crawlAllPostcardsWithUser(String authCookie, String targetId, String mode, JSONArray jsonrarray)
    {
        int pageNum = getUserMaxPostcardPageNum(authCookie, mode);
        //GO TO Each page
        for (int i = 1; i <= pageNum; i++)
        {
            crawlAllPostcardsWithUser(authCookie, targetId, Integer.toString(i), mode, jsonrarray);
        }
    }

    public static String getAuthCookie(String icyId, String username)
    {
        return "user-info=" + icyId + ";" + username + ";" + "2021-01-01;2;1;0"; // user cookie
    }

    public static JSONObject getPostcardPicWithUser(String icyid, String targetId, int mode)
    {
        // From given id, visit the user's home page to get the user's username, to generate the URL.

        String username = "你";//getUsernameByIcyId(icyid);
        ICYUser targetUser = getICYUserProfile(targetId);
        String receiver = targetUser.getUsername();

        String cookie = "user-info=" + icyid + ";" + username + ";" + "2021-01-01;2;1;0"; // user cookie
        // Create the JSONArray
        JSONArray jsonrarray = JSONUtil.createArray();
        JSONObject root = JSONUtil.createObj();
        root.set("sender", URLEncoder.createDefault().encode(username, Charset.forName("UTF-8")));
        root.set("receiver", StrUtil.isEmpty(receiver) ? "Ta" : URLEncoder.createDefault().encode(receiver, Charset.forName("UTF-8")));
        root.set("receiverId", targetId);
        root.set("receiverAvatarUrl", targetUser.getAvatarUrl());
        switch (mode)
        {
            case 3:
                crawlAllPostcardsWithUser(cookie, targetId, "1", jsonrarray);
                crawlAllPostcardsWithUser(cookie, targetId, "2", jsonrarray);
                break;
            case 2:
            case 1:
                crawlAllPostcardsWithUser(cookie, targetId, Integer.toString(mode), jsonrarray);
                break;

        }

        root.set("list", jsonrarray);
        return root;
    }

    public static JSONObject getCaptchaResult(String token, String userIp)
    {
        HttpRequest request = HttpUtil.createPost("https://www.recaptcha.net/recaptcha/api/siteverify");
        Setting setting = new Setting("captcha.setting");

        request.form("secret", setting.getStr("key"));
        request.form("response", token);
        request.form("remoteip", userIp);

        HttpResponse response = request.execute();

        return JSONUtil.parseObj(response.body());
    }

    public static JSONArray searchUserByName(String name)
    {
        HttpRequest request = HttpUtil.createPost("https://www.icardyou.icu/search/userList");
        HttpResponse response;
        request.header("Referer", "https://www.icardyou.icu/games/gameList/1");
        request.header("X-Requested-With", "XMLHttpRequest");
        request.form("searchWords", name);
        response = request.execute();
        /* System.out.println("com.vocab85.icy.network.ICYWebCommunicator.searchUserByName()");
        System.out.println("Response Body" + response.body());
        System.out.println(response);
        System.out.println(request);*/
        return JSONUtil.parseArray(response.body());
    }

    public static int getFirstCardEverInICY() throws IOException, InterruptedException
    {
        for (int i = 1; i < 520; i++)
        {
            System.out.println("Trying #" + i + "");
            HttpRequest r = HttpUtil.createRequest(Method.HEAD, "https://www.icardyou.icu/sendpostcard/postcardDetail/" + i);
            HttpResponse rep = r.execute();
            if (rep.isOk())
            {
                System.out.println("#" + i + " is working!");
                return i;
            } else
            {
                System.out.println("#" + i + " returns " + rep.getStatus());
            }
            Thread.sleep(500);
        }
        return 383;
    }

    public static String[] getCardInfo(String pcid)
    {
        try
        {
            String c = HttpUtil.downloadString("https://www.icardyou.icu/sendpostcard/postcardDetail/" + pcid, "UTF-8");
            Document doc = Jsoup.parse(c);
            String[] receiveInfo = doc.getElementsByClass("panel-title").get(0).text().split(" ");//[明信片, 383, 已收到]
            String[] otherInfo;
            Element senderInfoTableElement = doc.getElementsByClass("table").first();
            Element senderDateElement = senderInfoTableElement.getElementsByTag("tr").get(2).getElementsByTag("td").get(1);//third row is send date
            Element senderElement = senderInfoTableElement.getElementsByTag("a").first();
            String sendDate = senderDateElement.text().trim();
            String sender = senderElement.text().trim();
            otherInfo = new String[]
            {
                sender, sendDate
            };

            return ArrayUtil.addAll(receiveInfo, otherInfo);
        } catch (Exception e)
        {
            return null;
        }
    }

    public static JSONObject registerCardForUser(String icyUserId, String cardId) throws UnsupportedEncodingException
    {
        String authCookie = getAuthCookie(icyUserId, "autoregisterservice");
        HttpRequest request = HttpUtil.createPost("https://www.icardyou.icu/sendpostcard/confirmReceive");
        request.header("X-Requested-With", "XMLHttpRequest");
        request.header("Origin", "https://www.icardyou.icu");
        request.header("Referer", "https://www.icardyou.icu/sendpostcard/toReceive");
        request.form("cardNO", cardId);
        request.form("comment", "过期自助补登");
        request.cookie(authCookie);
        //System.out.println(request);
        HttpResponse response = request.execute();
        String responseBody = response.body();

        JSONObject json = JSONUtil.parseObj(Servlet.fixCharset(responseBody));
//        System.out.println(json.toStringPretty());
//        String m = json.getStr("resultMessage");
//        int jj = json.getInt("resultCode");
        return json;
    }

    /**
     * Crawl card info from id to id, inclusive both sides.
     *
     * @param beginId
     * @param endId
     * @param listener
     * @return
     * @throws SQLException
     */
    public static ArrayList<ICYPostcard> crawlICYCards(final int beginId, final int endId, final ProgressListener listener) throws SQLException, InterruptedException
    {
        ArrayList<ICYPostcard> cardList = new ArrayList<>(endId - beginId);
        //System.out.println("arrlist size" + (endId-beginId));
        for (int i = 0, maxi = endId - beginId; i <= maxi; i++)
        {
            Thread.sleep(100);
            try
            {
                //System.out.println("Add card #"+(beginId+i));
                //System.out.println();
                cardList.add(ICYPostcard.getPostcardFromId(beginId + i));
                listener.onSuccess(beginId + i);
            } catch (Exception ex)
            {
                listener.onFailure(beginId + i, ex);
            }
        }
        return cardList;
    }

    //will crawl 1+increment
    public static ArrayList<ICYPostcard> crawlICYCards(final int increment, final DBAccess dbobj, final ProgressListener listener) throws SQLException, InterruptedException
    {
        System.out.println("Getting the latest crawled card.");
        int lat = dbobj.getLatestCrawledCardSeqId();//get the latest index
        //System.out.println("Latest crawled card: " + lat);
        return crawlICYCards(lat + 1, lat + 1 + increment, listener);
    }

    public static ArrayList<ICYPostcard> crawlICYCardsLatId(final int increment, final int lat, final ProgressListener listener) throws SQLException, InterruptedException
    {
        // System.out.println("Getting the latest crawled card.");
        //int lat = dbobj.getLatestCrawledCardSeqId();//get the latest index
        //System.out.println("Latest crawled card: " + lat);
        return crawlICYCards(lat + 1, lat + 1 + increment, listener);
    }

    public static String searchFriendByMainpageKeyword(String keyword)
    {
        //Request the send postcard page.
        HttpRequest req = HttpUtil.createGet("https://www.icardyou.icu/sendpostcard/myPostCard/" + "1" + "?status=&cardType=&nowPage=" + "3");
        req.cookie("user-info=32364%3BJohnson%3B2003-04-08%3B2%3B1%3B18");//set user auth cookie
        HttpResponse resp = req.execute();//dispatch request
        Document document = Jsoup.parse(resp.body());//load response body, pass result from HttpRequest to Jsoup, let Jsoup handle the rest.
        Elements trs = document.getElementsByTag("tr");//get all table rows of the "send postcard page"
        for (int j = 1; j < trs.size(); j++)//skip the first row, it is the table header.
        {

            //get ONE row from the table
            Element tr = trs.get(j);
            //get the 4th column of the row, 
            Element usernametd = tr.getElementsByTag("td").get(3);
            //Read the userid from href of the <a> tag 
            //Example: <a href="https://www.icardyou.icu/1234567">Johnson</a>
            String href = usernametd.getElementsByTag("a").attr("href");
            System.out.println(href);
            req = HttpRequest.get(href);

            resp = req.execute();

            document = Jsoup.parse(resp.body());

            Elements wells = document.getElementsByClass("well");

            if (wells.isEmpty())
            {
                continue;
            }
            Element well = wells.get(0);
            System.out.println(well.text());
            if (well.text().contains(keyword))
            {

                System.out.println(href);
                return href;
            }

        }
        return "404";
    }

    public static void uploadImageICY(byte[] imagebyte, String fileName, String postcardId, String userId, String uploader)
    {
        HttpRequest request = HttpUtil.createPost("https://www.icardyou.icu/sendpostcard/uploadImg");
        request.form("file", imagebyte, fileName);
        request.form("cardId", postcardId);
        request.cookie("user-info=" + userId + ";" + uploader + ";2000-1-1;2;2;0");
        HttpResponse resp = request.execute();

    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        System.out.println(Arrays.toString(getCardInfo("1142322")));
    }
}
