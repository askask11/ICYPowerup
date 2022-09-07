/*
 * Author: jianqing
 * Date: Jun 4, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.model;

import cn.hutool.http.HttpUtil;
import java.util.ArrayList;
import java.util.List;
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

    public static final List<CrawlUnregisteredPostcardTask> CRAWLREG_TASKS = new ArrayList<>();
    public static final List<UnregisteredPostcardList> USERCRAWL_CACHE = new ArrayList<>();
    private int sequence;
    private String cardId;
    private int senderId;
    private int receiverId;
    private int status;
    private String sentTime;
    private String[] picURL;
    private String senderName;
    //private String cardType;
    public static final int PCSTATUS_NOT_SENT = 0, PCSTATUS_SENT = 1, PCSTATUS_RECEIVED = 2, PCSTATUS_EXPIRED = 3;

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

        return getPostcardFromPage(HttpUtil.downloadString("https://www.icardyou.icu/sendpostcard/postcardDetail/" + seqid, "UTF-8"));
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
        String senderName = sender_receiver_a.get(0).ownText().trim();
        int receiverId = getUserIdFromLink(sender_receiver_a.get(1).attr("href"));
        String sentTime = infoTable.getElementsByClass("table").get(0).getElementsByTag("tr").get(2).getElementsByTag("td").get(1).ownText().trim();
        

        //this refers to the green bar on top of the page.
        Element panelTitle = doc.getElementsByClass("panel-title").get(0);

        //get postcard id
        String cardId = panelTitle.ownText().replace("明信片", "").trim();

        //get the status of the card.
        String status = panelTitle.getElementsByTag("span").get(0).ownText();
        int statusId;
        switch (status)
        {
            case "未寄出":
                statusId = PCSTATUS_NOT_SENT;
                break;
            case "已寄出":
                statusId = PCSTATUS_SENT;
                break;
            case "已收到":
                statusId = PCSTATUS_RECEIVED;
                break;
            case "已过期":
                statusId = PCSTATUS_EXPIRED;
                break;
            default:
                statusId = 4;
                break;
        }
        
        
        //get images of the card
        Elements cardImgContainer = doc.getElementsByClass("cardImg");
        if(cardImgContainer.isEmpty())
        {
            icyp.setPicURL(null);
        }else
        {
            //System.err.println("not empty");
            Elements imgElements = cardImgContainer.get(0).getElementsByTag("img");
            String[] imgUrlList = new String[imgElements.size()];
            for (int i = 0; i < imgElements.size(); i++)
            {
                imgUrlList[i] = imgElements.get(i).attr("src");
            }
            icyp.setPicURL(imgUrlList);
        }
        
        //sequence ID of the postcard.
        int sequence = Integer.parseInt(doc.getElementsByAttributeValue("name", "cardId").get(0).attr("value"));

        icyp.setSequence(sequence);
        icyp.setCardId(cardId);
        icyp.setSenderId(senderId);
        icyp.setReceiverId(receiverId);
        icyp.setStatus(statusId);
        icyp.setSentTime(sentTime);
        icyp.setSenderName(senderName);
        //System.out.println("com.vocab85.icy.model.ICYPostcard.getPostcardFromPage(doc)return");
        doc = null;
        return icyp;
    }
    
    
    

    public static void main(String[] args)
    {
        System.out.println(getPostcardFromPage(HttpUtil.downloadString("https://www.icardyou.icu/sendpostcard/postcardDetail/1178793", "UTF-8")));
    }

}
