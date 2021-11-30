/*
 * Author: jianqing
 * Date: May 10, 2021
 * Description: This document is created for
 */
package com.vocab85.icy.network;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.setting.Setting;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Date;

/**
 *
 * @author jianqing
 */
public class AliOSS
{

    public static final Setting oss_creds = new Setting("oss.setting");

    public static OSS getOssClient()
    {
        String endpoint = oss_creds.get("endpoint");
        String accessKeyId = oss_creds.get("id");
        String accessKeySecret = oss_creds.get("secret");

        //Init oss instance
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    public static void deleteObj(String objectName)
    {
        OSS ossClient = getOssClient();
        String bucketName = oss_creds.get("bucket");
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    public static void uploadString(String path, String content)
    {
        uploadStream(path, new ByteArrayInputStream(content.getBytes()));
    }

    public static void uploadStream(String path, InputStream stream)
    {
        OSS ossClient = getOssClient();
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(oss_creds.get("bucket"), path, stream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    public static void uploadStreamNocache(String path, InputStream stream)
    {
        OSS ossClient = getOssClient();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setCacheControl("no-cache");
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(oss_creds.get("bucket"), path, stream, metadata);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
    
    public static OSSObject getUserMasterJSON(String userid)
    {
        OSS ossclient = getOssClient();
        OSSObject oo = ossclient.getObject(oss_creds.get("bucket"), "usercontent/"+userid.charAt(0)+"/"+userid+"/master.json");
        return oo;
    }
    
    
    public static void logError(Throwable t)
    {
        String error = ExceptionUtil.stacktraceToString(t);
        uploadString("debug/"+DateUtil.today()+"/err-"+DateUtil.format(new Date(), "HH:mm:ss")+"-"+error.length()+".txt", error);
    }
    
    //public static void upl

    public static void main(String[] args) throws MalformedURLException, IOException
    {
        try
        {
            Integer.parseInt("shabi");
            System.out.println("OK");
        } catch (Exception e)
        {
            logError(e);
            System.out.println("Done!");
        }
    }
}
