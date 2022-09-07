/*
 * Author: jianqing
 * Date: Mar 30, 2022
 * Description: This document is created for
 */
package com.vocab85.icy.model;

import cn.hutool.core.date.DateTime;
import com.vocab85.icy.network.AliOSS;
import com.vocab85.icy.network.DBAccess;
import com.vocab85.icy.network.ICYWebCommunicator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;

/**
 *
 * @author jianqing
 */
@Data
public class CrawlUnregisteredPostcardTask implements Runnable
{

    private int userId;
    private int icyId;
    private int totalCards, crawlProgress;
    private boolean halt;
    private boolean inError;
    private DateTime crawlTime;  

    public CrawlUnregisteredPostcardTask(int userId, int icyId)
    {
        this.userId = userId;
        this.icyId = icyId;
        totalCards = 0;
        crawlProgress = 0;
        halt = false;
        crawlTime=null;
    }

    public void getUserPostcardsNotReceived() throws SQLException
    {
        ArrayList<Integer> pcIdList;
        UnregisteredPostcardList userCards = new UnregisteredPostcardList();
        ArrayList<Integer> pcIdTobeUpdated = new ArrayList<>();
        crawlTime = DateTime.now();
        userCards.setCrawlTime(crawlTime);
        userCards.setIcyId(icyId);

        try (DBAccess dba = DBAccess.getDefaultInstance())
        {
            //Stage 1 trying to get not registered ids from db.
            pcIdList = dba.getCardSeqReceivedNotRegisteredByICYUserId(icyId);
            totalCards = pcIdList.size();
            //Stage 2 try to crawl each individual ids.

            for (int i = 0; i < totalCards; i++)
            {
                if (halt)
                {
                    break;
                }
                //限速
                double objectiveTime = System.currentTimeMillis() + 0.2*1000;
        while (System.currentTimeMillis() < objectiveTime){
            
        }
                crawlProgress = i + 1;
                Integer postcardId = pcIdList.get(i);
                ICYPostcard postcard = ICYPostcard.getPostcardFromId(postcardId);
                if (ICYPostcard.PCSTATUS_RECEIVED == postcard.getStatus())
                {
                    pcIdTobeUpdated.add(postcardId);
                } else
                {
                    //The postcards that are actually not registered.
                    userCards.add(postcard);
                }
            }
        }
        //update the database about new findings.
        
            Thread t = new Thread(() ->
            {
                try (DBAccess dba = DBAccess.getDefaultInstance())
                {
                    dba.insertIntoUserCrawlRequestRecord(userId, crawlProgress, userCards.getCrawlTime().toString(),halt);
                    dba.updateCardsStatusByCardIdAsReceived(pcIdTobeUpdated);
                } catch (SQLException ex)
                {
                    Logger.getLogger(ICYWebCommunicator.class.getName()).log(Level.SEVERE, null, ex);
                    AliOSS.logError(ex);
                }
            });
            t.start();
        
            /*//if this is a cancelled task, don't replace the one in cache.
            if(halt)
            {
                return;
            }*/
        
        int s = ICYPostcard.USERCRAWL_CACHE.size();
        for (int i = 0; i < s; i++)
        {
            if (ICYPostcard.USERCRAWL_CACHE.get(i).getIcyId() == icyId);
            {
                ICYPostcard.USERCRAWL_CACHE.remove(i);
                break;
            }
        }

        ICYPostcard.USERCRAWL_CACHE.add(userCards);
    }

    @Override
    public void run()
    {
        try
        {
            ICYPostcard.CRAWLREG_TASKS.add(this);
            getUserPostcardsNotReceived();
            
        } catch (SQLException ex)
        {
            inError = true;
            //ICYPostcard.CRAWLREG_TASKS.remove(this)
            Logger.getLogger(CrawlUnregisteredPostcardTask.class.getName()).log(Level.SEVERE, null, ex);
            AliOSS.logError(ex);

        }
        ICYPostcard.CRAWLREG_TASKS.remove(this);
    }

    public static void main(String[] args)
    {
        CrawlUnregisteredPostcardTask t = new CrawlUnregisteredPostcardTask(12, 12);

    }
}
