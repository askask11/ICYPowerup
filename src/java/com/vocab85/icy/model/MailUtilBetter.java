/*
 * Author: jianqing
 * Date: May 19, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.model;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailUtil;
import java.nio.charset.Charset;

/**
 *
 * @author jianqing
 */
public class MailUtilBetter extends MailUtil
{

    public static void sendText(String to, String title, String content)
    {
        Mail mail = Mail.create();
        mail.setUseGlobalSession(false);
        mail.setCharset(Charset.forName("UTF-8"));
        mail.setTitle(title);
        mail.setContent(content);
        mail.setTos(to);
        mail.send();
    }
}
