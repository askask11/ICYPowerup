/*
 * Author: jianqing
 * Date: Mar 29, 2022
 * Description: This document is created for
 */
package com.vocab85.icy.model;

import java.util.List;

/**
 *
 * @author jianqing
 */
public class LongTask implements Runnable
{
    private List<LongTask> motherList;
    private int secLeft;
    private String taskId;
    
    public LongTask(List<LongTask> motherList)
    {
        this.motherList = motherList;
        taskId=null;
    }
    public LongTask(List<LongTask> m, String id)
    {
        this(m);
        taskId = id;
        
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    
    @Override
    public void run()
    {
       // LinkedList<String> s = new LinkedList<>();
        //this.motherList.add(this);
        this.motherList.add(this);
        long objectiveTime = System.currentTimeMillis() + 10*1000;
        while (System.currentTimeMillis() < objectiveTime){
            secLeft = (int)(objectiveTime-System.currentTimeMillis())/1000;
        }
        motherList.remove(this);
    }
    
    @Override
    public String toString()
    {
        return super.toString() + " sec=" + secLeft + " , id= "+taskId;
    }
    
}
