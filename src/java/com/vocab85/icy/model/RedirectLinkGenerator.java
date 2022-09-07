/*
 * Author: jianqing
 * Date: May 5, 2022
 * Description: This document is created for
 */
package com.vocab85.icy.model;

import cn.hutool.core.util.RandomUtil;
import com.vocab85.icy.network.DBAccess;
import java.util.Scanner;

/**
 *
 * @author jianqing
 */
public class RedirectLinkGenerator
{
    /**
     * Generate num amount of redirect links.
     * @param num The number of links to be generated.
     * @return 
     */
    public static String[] generate(int num)
    {
        String[] result = new String[num];
        for (int i = 0; i < num; i++)
        {
            result[i] = RandomUtil.randomString("qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM",5);
        }
        return result;
    }
    
    
    public static void main(String[] args)
    {
        
        Scanner keyboard = new Scanner(System.in);
        System.out.println("生成几个？");
        int gen = keyboard.nextInt();
        System.out.println("使用域名？ 1 = icytools.cn, 2 = icy.85vocab.com");
        int domainC = keyboard.nextInt();
        String[] str = generate(gen);
        System.out.println("1/3号码生成完毕，正在连接数据库...");
        try(DBAccess dba = DBAccess.getDefaultInstance())
        {
            System.out.println("2/3正在保存数据，请稍候...");
            dba.insertRedirectLink(str);
            //System.out.println(Arrays.toString(jj));
            for (String string : str)
            {
                System.out.println("https://"+(domainC==1?"icytools.cn":"icy.85vocab.com")+"/r/"+string);
            }
            System.out.println("");
//            for (String s : str)
//            {
//                System.out.println(s);
//            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
