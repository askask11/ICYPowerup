/*
 * Author: jianqing
 * Date: May 10, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.network;

import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.IOException;
import java.nio.charset.Charset;
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
        System.out.println(System.getProperty("javax.net.ssl.trustStore"));
        String url = "https://www.icardyou.icu/userInfo/homePage?userId=" + userId;
        String content = HttpUtil.downloadString(url, "UTF-8");
        Document document = Jsoup.parse(content);
        Element scriptTag = document.getElementById("powerup-script");
        //no such element
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
        if(cardImageContainer.isEmpty())
        {
            return new Elements();
        }
        return cardImageContainer.get(0).getElementsByTag("img");
    }
    
    
   

    public static JSONObject getPostcardPicWithUser(String icyid, String targetId)
    {
        // From given id, visit the user's home page to get the user's username, to generate the URL.
        String username = getUsernameByIcyId(icyid);
        String receiver = getUsernameByIcyId(targetId);
        
        String cookie = "user-info=" + icyid + ";" + username + ";" + "2021-01-01;2;1;0"; // user cookie
        // Create the JSONArray
        JSONArray jsonrarray = JSONUtil.createArray();
        JSONObject root = JSONUtil.createObj();
        root.set("sender", username);
        root.set("receiver", StrUtil.isEmpty(receiver)?"这位小伙伴":URLEncoder.createDefault().encode(receiver, Charset.forName("UTF-8")));
        JSONObject jsonr;
        // Try to visit the first page of user's send card, determine how many pages user has.
        HttpRequest req = HttpUtil.createGet("https://www.icardyou.icu/sendpostcard/myPostCard/1?status=&cardType=&nowPage=1");
        req.cookie(URLEncoder.createDefault().encode(cookie, Charset.forName("UTF-8")));
        HttpResponse resp = req.execute();
        Document document = Jsoup.parse(resp.body());//parse the document.
        // get the ">>" element, containing the URL for the last page of user.
        Element lastpage = document.getElementsByAttributeValue("title", "最后一页").get(0);
        // the last page is hidden in the URL of the button.
        String href = lastpage.getElementsByTag("a").get(0).attr("href"); 
        int pageNum = getLastPageFromURI(href.split("&"));//Find the "lastpage" from the URI query.
        
        //GO TO Each page
        for (int i = 1; i <= pageNum; i++)
        {
            //GO TO each page.
            req = HttpUtil.createGet("https://www.icardyou.icu/sendpostcard/myPostCard/1?status=&cardType=&nowPage=" + i);
            req.cookie(cookie);//set user auth cookie
            resp = req.execute();
            document = Jsoup.parse(resp.body());
            Elements trs = document.getElementsByTag("tr");//get all table rows of the "send postcard page"
            for (int j = 1; j < trs.size(); j++)//skip the first row, it is the table header.
            {
                //get ONE row from the table
                Element tr = trs.get(j);
                //get the 4th column of the row, 
                Element usernametd = tr.getElementsByTag("td").get(3);
                //read the userid from href (<a> tag), which is the username.
                href = usernametd.getElementsByTag("a").attr("href");
                //split the URI with the question mark, the query should only has the userId
                String[] hrefrags = href.split(Pattern.quote("?"));
                //replace the varible name part of user id, get the pure id.
                String stuid = hrefrags[1].replace("userId=", "").trim();
                
                //check if the user id is what we are looking for(target user)
                //the condition of if to crawl the image of this postcard.
                if (stuid.equals(targetId))
                {
                    // Get the 
                    Element pcele = 
                            tr.getElementsByTag("td")// get all columns of table
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
                        jsonr = JSONUtil.createObj();
                        jsonr.set("id", pcid);
                        jsonr.set("imgsrc", piclist1.attr("src"));
                        jsonr.set("pcsrc", pcurl);
                        jsonrarray.add(jsonr);
                    }

                }

            }
        }
        root.set("list", jsonrarray);
        return root;
    }

    public static void main(String[] args) throws IOException
    {
        System.out.println(getPostcardPicWithUser("32364", "38736").toStringPretty());
        
    }
}
