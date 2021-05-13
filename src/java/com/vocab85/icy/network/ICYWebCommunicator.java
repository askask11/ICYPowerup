/*
 * Author: jianqing
 * Date: May 10, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.network;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import java.io.IOException;
//import org.jboss.weld.context.http.Http;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author jianqing
 */
public class ICYWebCommunicator
{
    
    public static boolean verifyUserTag(String powerupid, String userId) throws IOException
    {
        System.clearProperty("javax.net.ssl.trustStore");
        System.out.println(System.getProperty("javax.net.ssl.trustStore"));
       String url = "https://www.icardyou.icu/userInfo/homePage?userId="+userId;
       String content = HttpUtil.downloadString(url, "UTF-8");
       Document document = Jsoup.parse(content);
       Element scriptTag = document.getElementById("powerup-script");
       //no such element
       if(scriptTag==null)
       {
           return false;
       }
       
       String userIdDetected = scriptTag.attr("data-powerup-id");
       
       return StrUtil.equals(userIdDetected, powerupid);
    }
    
    public static void main(String[] args) throws IOException
    {
        
    }
}
