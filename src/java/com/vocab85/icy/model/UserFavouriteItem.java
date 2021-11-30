/*
 * Author: jianqing
 * Date: Nov 22, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.model;

import cn.hutool.Hutool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import lombok.Data;

/**
 *
 * @author jianqing
 */
@Data
public class UserFavouriteItem
{

    private int powerupId;
    private int icyId;
    private String icyUsername, icyAvatarUrl, icyUserPingyin;

    public String getIcyUserPingyin()
    {
        if (StrUtil.isEmpty(icyUserPingyin))
        {
            icyUserPingyin = PinyinUtil.getPinyin(icyUsername);
        }
        return icyUserPingyin;
    }
    
    public static void main(String[] args)
    {
        UserFavouriteItem ufi = new UserFavouriteItem();
        ufi.setIcyUsername("高");
        System.out.println(ufi.getIcyUserPingyin());
    }
}
