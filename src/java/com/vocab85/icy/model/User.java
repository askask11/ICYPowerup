/*
 * Author: jianqing
 * Date: May 9, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.model;

import lombok.Data;

/**
 *
 * @author jianqing
 */
@Data
public class User
{

    private int id;
    private String username;
    private String password;
    private String icyid;
    private String token;

    private String email;
    //private boolean icyVerified;

    public boolean isIcyVerified()
    {
        return icyid!=null;
    }

    public String getUserRootDir()
    {
        return "usercontent/"+Integer.toString(id).charAt(0)+"/"+id+"/";
    }
}
