/*
 * Author: jianqing
 * Date: Jun 4, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.model;

import cn.hutool.http.HttpUtil;
import java.util.regex.Pattern;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author jianqing
 */
@Data
public class ICYPostcard
{
    private int sequence;
    private String cardId;
    private int senderId;
    private int receiverId;
    
    
    //static methods
    public static int getUserIdFromLink(String url)
    {
        //System.out.println("com.vocab85.icy.model.ICYPostcard.getUserIdFromLink()");
        String[] frags;
        frags = url.split(Pattern.quote("?"));
        String q = frags[1];
        return Integer.parseInt(q.replace("userId=", ""));
    }
    
    
    public static ICYPostcard getPostcardFromId(int seqid)
    {
        //System.out.println("com.vocab85.icy.model.ICYPostcard.getPostcardFromId()");
        //HttpRequest request = HttpRequest.get("https://www.icardyou.icu/sendpostcard/postcardDetail/"+seqid);
        
        return getPostcardFromPage(HttpUtil.downloadString("https://www.icardyou.icu/sendpostcard/postcardDetail/"+seqid, "UTF-8"));
    }
    
    
    public static ICYPostcard getPostcardFromPage(String html)
    {
        //System.out.println("com.vocab85.icy.model.ICYPostcard.getPostcardFromPage(html)");
        return getPostcardFromPage(Jsoup.parse(html));
    }
    public static ICYPostcard getPostcardFromPage(Document doc)
    {
        //System.out.println("com.vocab85.icy.model.ICYPostcard.getPostcardFromPage(doc)");
        ICYPostcard icyp = new ICYPostcard();
        
        //get the head table.
        Element infoTable = doc.getElementsByClass("postcardDetail").get(0);
        //get the sender&receiver link
        Elements sender_receiver_a = infoTable.getElementsByTag("a"); // size=2
        
        int senderId = getUserIdFromLink(sender_receiver_a.get(0).attr("href"));
        int receiverId = getUserIdFromLink(sender_receiver_a.get(1).attr("href"));
        //get postcard id
        String cardId = doc.getElementsByClass("panel-title").get(0).ownText().replace("明信片", "").trim();
        //sequence ID of the postcard.
        
        int sequence = Integer.parseInt(doc.getElementsByAttributeValue("name","cardId").get(0).attr("value"));
        
        icyp.setSequence(sequence);
        icyp.setCardId(cardId);
        icyp.setSenderId(senderId);
        icyp.setReceiverId(receiverId);
        //System.out.println("com.vocab85.icy.model.ICYPostcard.getPostcardFromPage(doc)return");
        doc=null;
        return icyp;
    }
    
    
    public static void main(String[] args)
    {
        System.out.println(getPostcardFromPage(HttpUtil.downloadString("https://www.icardyou.icu/sendpostcard/postcardDetail/1140622", "UTF-8")));
    }
    
    
}
