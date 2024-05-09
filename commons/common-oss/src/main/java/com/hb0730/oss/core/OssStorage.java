package com.hb0730.oss.core;

import com.hb0730.base.exception.OssException;
import lombok.Data;

import java.io.File;
import java.io.InputStream;

/**
 * OSS 存储
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/12
 */
public interface OssStorage extends OssStorageInit {
    /**
     * 根据accessUrl删除文件
     *
     * @param accessUrl url
     */
    void deleteUrl(String accessUrl);

    /**
     * 删除文件
     *
     * @param objectKey  objectKey
     * @param bucketName bucketName
     */
    void removeObject(String objectKey, String bucketName);

    /**
     * 上传文件
     *
     * @param file file
     * @return accessUrl
     * @throws RuntimeException 异常
     */
    String uploadFile(File file);

    /**
     * 上传文件
     *
     * @param objectKey objectKey
     * @param file      file
     * @return accessUrl
     * @throws RuntimeException 异常
     */
    String uploadFile(String objectKey, File file);

    /**
     * 上传文件
     *
     * @param objectKey  objectKey
     * @param bucketName bucketName
     * @param file       file
     * @return accessUrl
     * @throws RuntimeException 异常
     */
    String uploadFile(String objectKey, String bucketName, File file);

    /**
     * 上传文件
     *
     * @param objectKey objectKey
     * @param stream    文件流
     * @return accessUrl
     * @throws RuntimeException 异常
     */
    String upload(String objectKey, InputStream stream);

    /**
     * 上传文件
     *
     * @param objectKey  objectKey
     * @param bucketName bucketName
     * @param stream     文件流
     * @return accessUrl
     * @throws RuntimeException 异常
     */
    String upload(String objectKey, String bucketName, InputStream stream);

    /**
     * 获取文件
     *
     * @param objectKey objectKey
     * @return 文件流
     */
    InputStream getFile(String objectKey);

    /**
     * 获取文件
     *
     * @param objectKey  objectKey
     * @param bucketName bucketName
     * @return 文件流
     */
    InputStream getFile(String objectKey, String bucketName);

    /**
     * 获取分享链接,默认过期时间1小时
     *
     * @param objectKey objectKey
     * @return url
     */
    default String getShareUrl(String objectKey) {
        return getShareUrl(objectKey, 3600);
    }

    /**
     * 获取分享链接
     *
     * @param objectKey objectKey
     * @param expires   过期时间，单位秒
     * @return url
     */
    String getShareUrl(String objectKey, long expires);

    /**
     * 获取分享链接
     *
     * @param objectKey  objectKey
     * @param bucketName bucketName
     * @param expires    过期时间，单位秒
     * @return url
     */
    String getShareUrl(String objectKey, String bucketName, long expires);

    /**
     * 获取临时访问凭证,默认10分钟
     *
     * @return 临时访问凭证
     */
    default OssStsToken getStsToken() {
        return getStsToken(3600);
    }


    /**
     * 获取临时访问凭证
     *
     * @param expires 过期时间，单位秒
     * @return 临时访问凭证
     */
    default OssStsToken getStsToken(long expires) {
        throw new OssException("not support, please override this method");
    }

    /**
     * 临时访问凭证
     */
    @Data
    static class OssStsToken {
        private String accessKey;
        private String accessSecret;
        private String securityToken;
        private String expiration;
    }
}