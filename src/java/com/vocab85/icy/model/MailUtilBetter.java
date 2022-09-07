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
    
    public static void sendVerifyText(String username, String verify, String email)
    {
         MailUtilBetter.sendText(email, "ICY Powerup 邮箱验证", "您好，" + username + "\n 您刚刚通过该邮箱注册了"
                    + "ICY Powerup 服务， 以下是您申请的验证码：\n"
                    + verify + "\n\n祝您生活愉快\n\n"
                    + "ICY Powerup 开发 Johnson 敬上"
                    + "\n\n=================\n"
                    + "机密性通知：此电子邮件通讯和所有附件"
                    + "可能包含机密和特权信息以供该邮件的指定收件人使用"
                    + "如果您不是预期或指定的收件人，则"
                    + "特此通知您，您是因投递错误而收到该信息的，并且"
                    + "严格禁止任何对其内容的审查，披露，传播，分发或复制\n"
                    + "如果您错误地收到了这份邮件，请"
                    + "通过退回电子邮件的方式通知发件人，并删除和/或销毁该文件的所有副本，"
                    + "交流和任何附件。\n\nCONFIDENTIALITY NOTICE: This e-mail communication and any attachments\n"
                    + "may contain confidential and privileged information for the use of the\n"
                    + "designated recipients named above. If you are not the intended recipient, you\n"
                    + "are hereby notified that you have received this communication in error and that\n"
                    + "any review, disclosure, dissemination, distribution or copying of it or its contents\n"
                    + "is strictly prohibited. If you have received this communication in error, please\n"
                    + "notify the sender by return e-mail and delete and/or destroy all copies of this\n"
                    + "communication and any attachments.");
    }
    
    public static void sendPasswordRecoveryText(String verify, String email)
    {
        MailUtilBetter.sendText(email, "ICY Powerup 密码恢复验证", "您好，" + "\n 您刚刚申请了找回密码服务，"
                    + "以下是您申请的验证码：\n"
                    + verify + "\n\n祝您生活愉快\n\n"
                    + "ICY Powerup 开发 Johnson 敬上"
                            + "\n"
                    + "\n\n=================\n"
                    + "机密性通知：此电子邮件通讯和所有附件"
                    + "可能包含机密和特权信息以供该邮件的指定收件人使用"
                    + "如果您不是预期或指定的收件人，则"
                    + "特此通知您，您是因投递错误而收到该信息的，并且"
                    + "严格禁止任何对其内容的审查，披露，传播，分发或复制\n"
                    + "如果您错误地收到了这份邮件，请"
                    + "通过退回电子邮件的方式通知发件人，并删除和/或销毁该文件的所有副本，"
                    + "交流和任何附件。\n\nCONFIDENTIALITY NOTICE: This e-mail communication and any attachments\n"
                    + "may contain confidential and privileged information for the use of the\n"
                    + "designated recipients named above. If you are not the intended recipient, you\n"
                    + "are hereby notified that you have received this communication in error and that\n"
                    + "any review, disclosure, dissemination, distribution or copying of it or its contents\n"
                    + "is strictly prohibited. If you have received this communication in error, please\n"
                    + "notify the sender by return e-mail and delete and/or destroy all copies of this\n"
                    + "communication and any attachments.");
    }
    
    public static void main(String[] args)
    {
        MailUtil.sendText("xeduoover18@gmail.com", "nihao", "wo fei chng hao");
        System.out.println("Yay!");
    }
    
}
