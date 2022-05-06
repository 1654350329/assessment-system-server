package com.tree.clouds.assessment.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiniuUtil {
    //    private static String accessKey = "BGUpQznLLmK0W230OZBiAoaCuavRh-7OYSnLFdtT";
//    private static String secretKey = "x_OuqdffUQwKLDM0bhOJIA6Mv9Ac37qFe1g21ywb";
//    private static String bucket = "xpwgh";
//    private static String fontUrl = "https://xpwghoss.3dy.me/";
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String fontUrl;

    /**
     * 获取上传凭证
     */
    public String getUploadCredential() {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
        return upToken;
    }

    //密钥配置

    public Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }

    /**
     * 文件上传
     *
     * @param zone          华东	Zone.zone0()
     *                      华北	Zone.zone1()
     *                      华南	Zone.zone2()
     *                      北美	Zone.zoneNa0()
     * @param upToken       上传凭证
     * @param localFilePath 需要上传的文件本地路径
     * @return
     */
    public DefaultPutRet fileUpload(Zone zone, String upToken, String localFilePath) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            // 解析上传成功的结果
            return new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                // ignore
            }
        }
        return null;

    }

    public String fileUpload(String filePath) {
        DefaultPutRet defaultPutRet = fileUpload(Zone.zone2(), getUploadCredential(), filePath);
        if (defaultPutRet != null) {
            return fontUrl + defaultPutRet.key;
        }
        return null;
    }
//
//    public static void main(String[] args) {
//
//        DefaultPutRet defaultPutRet = fileUpload(Zone.zone2(), QiniuUtil.getUploadCredential(), "D:\\配置截图.jpg");
//        System.out.println("defaultPutRet = " + fontUrl + defaultPutRet.key);
//    }
}
