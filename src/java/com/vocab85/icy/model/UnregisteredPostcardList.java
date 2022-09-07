/*
 * Author: jianqing
 * Date: Mar 30, 2022
 * Description: This document is created for
 */
package com.vocab85.icy.model;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import java.util.ArrayList;

/**
 *
 * @author jianqing
 */
public class UnregisteredPostcardList extends ArrayList<ICYPostcard>
{
    private DateTime crawlTime;
    
    //user icy id
    /**
     * User ICY ID.
     */
    private int icyId;
    
    

    public DateTime getCrawlTime()
    {
        return crawlTime;
    }

    public void setCrawlTime(DateTime crawlTime)
    {
        this.crawlTime = crawlTime;
    }

    public int getIcyId()
    {
        return icyId;
    }

    public void setIcyId(int icyId)
    {
        this.icyId = icyId;
    }
    
    
    public static void main(String[] args)
    {
        UnregisteredPostcardList s = new UnregisteredPostcardList();
        System.out.println(JSONUtil.parseArray(s));
                
    }
}
