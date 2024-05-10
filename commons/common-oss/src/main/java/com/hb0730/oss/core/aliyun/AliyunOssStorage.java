package com.hb0730.oss.core.aliyun;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLDecoder;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.hb0730.base.exception.OssException;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.oss.core.AbstractOssStorage;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云OSS 存储
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/4/13
 */
@Slf4j
public class AliyunOssStorage extends AbstractOssStorage {
    private volatile OSSClient ossClient;
    private AliyunOssProperties ossProperties;

    public AliyunOssStorage() {
    }

    public AliyunOssStorage(AliyunOssProperties ossProperties) {
        super(ossProperties);
        this.ossProperties = ossProperties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getClient() {
        return (T) this.ossClient;
    }

    @Override
    public void deleteUrl(String accessUrl) {
        has(accessUrl, "accessUrl is null");
        has(ossProperties, "ossProperties is null");
        String objectKey = ossProperties.renameObjectKey(accessUrl);
        if (objectKey != null) {
            _getOssClient().deleteObject(ossProperties.getBucketName(), objectKey);
        }
    }

    @Override
    public void removeObject(String objectKey, String bucketName) {
        has(bucketName, "bucketName is null")
                .has(objectKey, "objectKey is null");
        _getOssClient().deleteObject(bucketName, objectKey);
        log.info("delete object success, bucketName:{}, objectKey:{}", bucketName, objectKey);
    }

    @Override
    public String uploadFile(File file) {
        has(file, "file is null")
                .has(ossProperties, "ossProperties is null");
        String objectKey = ossProperties.renameObjectKey(file, "");
        return uploadFile(objectKey, file);
    }

    @Override
    public String uploadFile(String objectKey, File file) {
        return uploadFile(objectKey, ossProperties.getBucketName(), file);
    }

    @Override
    public String uploadFile(String objectKey, String bucketName, File file) {
        has(objectKey, "objectKey is null")
                .has(bucketName, "bucketName is null")
                .has(file, "file is null");
        try {
            checkBucket(bucketName);
            // 上传文件
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length());
            metadata.setContentType(getFileMimeType(file.getName()));
            _getOssClient().putObject(bucketName, objectKey, file, metadata);
            log.info("upload file success, bucketName:{}, objectKey:{}", bucketName, objectKey);
            return ossProperties.getAccessUrl(objectKey);
        } catch (Exception e) {
            log.error("upload file error", e);
            throw new OssException("upload file error", e);
        }
    }

    @Override
    public String upload(String objectKey, InputStream stream) {
        return upload(objectKey, ossProperties.getBucketName(), stream);
    }

    @Override
    public String upload(String objectKey, String bucketName, InputStream stream) {
        return doUpload(objectKey, bucketName, -1, "", stream);
    }

    @Override
    public String upload(String objectKey, long contentLength, String contentType, InputStream stream) {
        return doUpload(objectKey, ossProperties.getBucketName(), contentLength, contentType, stream);
    }

    protected String doUpload(String objectKey, String bucketName, long contentLength,
                              String contentType, InputStream stream) {
        has(objectKey, "objectKey is null")
                .has(bucketName, "bucketName is null")
                .has(stream, "stream is null");
        try {
            checkBucket(bucketName);
            contentLength = contentLength <= 0 ? stream.available() : contentLength;
            contentType = StrUtil.isBlank(contentType) ? "application/octet-stream" : contentType;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);
            metadata.setContentType(contentType);
            _getOssClient().putObject(bucketName, objectKey, stream, metadata);
            log.info("upload file success, bucketName:{}, objectKey:{}", bucketName, objectKey);
            return ossProperties.getAccessUrl(objectKey);
        } catch (Exception e) {
            log.error("upload file error", e);
            throw new OssException("upload file error", e);
        }
    }

    @Override
    public InputStream getFile(String objectKey) {
        return getFile(objectKey, ossProperties.getBucketName());
    }

    @Override
    public InputStream getFile(String objectKey, String bucketName) {
        has(objectKey, "objectKey is null")
                .has(bucketName, "bucketName is null");
        try (OSSObject ossObject = _getOssClient().getObject(bucketName, objectKey)) {
            if (ossObject != null) {
                return new BufferedInputStream(ossObject.getObjectContent());
            }
        } catch (Exception e) {
            log.error("get file error", e);
        }
        return null;
    }

    @Override
    public String getShareUrl(String objectKey, long expires) {
        return getShareUrl(objectKey, ossProperties.getBucketName(), expires);
    }

    @Override
    public String getShareUrl(String objectKey, String bucketName, long expires) {
        has(objectKey, "objectKey is null")
                .has(bucketName, "bucketName is null");
        try {
            if (_getOssClient().doesObjectExist(bucketName, objectKey)) {
                Calendar rightNow = Calendar.getInstance();
                rightNow.add(Calendar.SECOND, (int) expires);
                URL url = _getOssClient().generatePresignedUrl(
                        bucketName,
                        objectKey,
                        rightNow.getTime()
                );
                return URLDecoder.decode(url.toString(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("get share url error", e);
        }
        return null;
    }


    @Override
    public OssStsToken getStsToken(long expires) {
        // 添加endpoint
        DefaultProfile.addEndpoint(ossProperties.getRegion(), "Sts", "sts.aliyuncs.com");
        // 构造default profile（参数留空，无需添加region ID）
        DefaultProfile profile = DefaultProfile.getProfile(ossProperties.getRegion(), ossProperties.getAccessKey(), ossProperties.getSecretKey());
        // 构造client
        DefaultAcsClient acsClient = new DefaultAcsClient(profile);
        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setSysMethod(com.aliyuncs.http.MethodType.POST);
        request.setRoleArn(ossProperties.getRoleArn());
        request.setRoleSessionName(ossProperties.getRoleSessionName());
        request.setPolicy(null);
        //默认3分钟
        request.setDurationSeconds(expires);
        try {
            AssumeRoleResponse acsResponse = acsClient.getAcsResponse(request);
            OssStsToken ossStsToken = new OssStsToken();
            ossStsToken.setAccessKey(acsResponse.getCredentials().getAccessKeyId());
            ossStsToken.setAccessSecret(acsResponse.getCredentials().getAccessKeySecret());
            ossStsToken.setSecurityToken(acsResponse.getCredentials().getSecurityToken());
            ossStsToken.setExpiration(acsResponse.getCredentials().getExpiration());
            ossStsToken.setBucketName(ossProperties.getBucketName());
            return ossStsToken;

        } catch (Exception e) {
            log.error("get sts token error", e);
            throw new OssException("get sts token error", e);
        } finally {
            // 关闭client
            acsClient.shutdown();
        }
    }

    @Override
    public PresignedUrl getPresignedUrl(String objectKey, long expires, Map<String, String> headers) {
        has(objectKey, "objectKey is null");
        try {
            checkBucket(ossProperties.getBucketName());

            // 设置用户自定义元数据。
            Map<String, String> userMetadata = new HashMap<String, String>();


            Calendar rightNow = Calendar.getInstance();
            rightNow.add(Calendar.SECOND, (int) expires);

            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(ossProperties.getBucketName(),
                    objectKey, com.aliyun.oss.HttpMethod.PUT);
            // 设置过期时间。
            request.setExpiration(rightNow.getTime());
            // 设置请求头。
            request.setHeaders(headers);
            // 设置用户自定义元数据。
            request.setUserMetadata(userMetadata);

            // 生成签名URL（HTTP PUT请求）。
            URL url = _getOssClient().generatePresignedUrl(request);
            String presignedUrl = URLDecoder.decode(url.toString(), StandardCharsets.UTF_8);
            String accessUrl = ossProperties.getAccessUrl(objectKey);
            return new PresignedUrl(presignedUrl, accessUrl);
        } catch (Exception e) {
            log.error("get presigned url error", e);
            throw new OssException("get presigned url error", e);
        }
    }

    /**
     * 获取文件的MIME类型
     *
     * @param fileName 文件名
     * @return MIME类型
     */
    protected String getFileMimeType(String fileName) {
        return FileUtil.getMimeType(fileName);
    }

    @Override
    public void init() {
        if (null == ossClient) {
            synchronized (this) {
                if (null == ossClient) {
                    ossClient = buildClient();
                }
            }
        }
    }

    private OSSClient _getOssClient() {
        if (null == ossClient) {
            init();
        }
        return ossClient;
    }

    /**
     * 检查bucket是否存在,不存在则创建
     *
     * @param bucketName bucketName
     */
    private void checkBucket(String bucketName) {
        has(bucketName, "bucketName is null");
        if (!_getOssClient().doesBucketExist(bucketName)) {
            this.ossClient.createBucket(bucketName);
            this.ossClient.setBucketAcl(bucketName, CannedAccessControlList.parse(ossProperties.getEndpointPolicy()));
        }
    }

    private OSSClient buildClient() {
        return (OSSClient) OSSClientBuilder
                .create()
                .region(ossProperties.getRegion())
                .endpoint(ossProperties.getEndpoint())
                .credentialsProvider(
                        new DefaultCredentialProvider(
                                ossProperties.getAccessKey(),
                                ossProperties.getSecretKey())
                )
                .build();
    }
}