/*
 * Author: jianqing
 * Date: May 10, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.test;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailUtil;
import java.nio.charset.Charset;

/**
 *
 * @author jianqing
 */
public class TestURLUtil
{
    public static void main(String[] args)
    {
        
        Mail mail = Mail.create();
        mail.setUseGlobalSession(false);
        mail.setCharset(Charset.forName("UTF-8"));
        mail.setTitle("Test Mail");
        mail.setContent("Ni Hao!");
        mail.setTos("hfisdbvgidsryilsyvilsrygiwuy@qbguguguguq.com");
        mail.send();
        
    }
}
