/*
 * Author: jianqing
 * Date: Jun 4, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.network;

/**
 *
 * @author jianqing
 */
public interface ProgressListener
{
    public void onSuccess(int id);
    public void onFailure(int id, Exception ex);
    
}
