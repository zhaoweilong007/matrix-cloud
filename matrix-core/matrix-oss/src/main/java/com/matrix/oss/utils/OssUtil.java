package com.matrix.oss.utils;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.matrix.auto.properties.OssProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/*
 * OSS对象存储服务实现类-阿里云
 * @author LeonZhou
 * @since 2023/6/20
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OssUtil {

    private static final OssProperties ossProperties = SpringUtil.getBean(OssProperties.class);


    public static String uploadFile(MultipartFile multipartFile, String filePathName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());

        try {

            //设置过期时间 9700小时
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000L * 60 * 60 * 24 * 7;
            expiration.setTime(expTimeMillis);

            InputStream inputStream = multipartFile.getInputStream();
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucketName(), filePathName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            log.info("result:{}", JSONUtil.toJsonStr(result));

        } catch (OSSException oe) {
            log.info("OSSException oe:{}", JSONUtil.toJsonStr(oe));
        } catch (ClientException ce) {
            log.info("Error Message:{}", ce.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

    public static void download(String objectName) {
        // 填写不包含Bucket名称在内的Object完整路径，例如testfolder/exampleobject.txt。
        String pathName = "D:\\test1\\1.txt";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        try {
            // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
            OSSObject ossObject = ossClient.getObject(ossProperties.getBucketName(), objectName);

            // 读取文件内容。
            log.info("Object content:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                log.info("\n" + line);
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            reader.close();
            // ossObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            ossObject.close();
        } catch (OSSException oe) {
            log.info("OSSException oe:{}", JSONUtil.toJsonStr(oe));
        } catch (Throwable ce) {
            log.info("Error Message:{}", ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}