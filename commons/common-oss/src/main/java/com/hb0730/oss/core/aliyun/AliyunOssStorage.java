package com.hb0730.oss.core.aliyun;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLDecoder;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.hb0730.base.exception.OssException;
import com.hb0730.oss.core.AbstractOssStorage;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

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
        String objectKey = ossProperties.getObjectKey(accessUrl);
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
        String objectKey = ossProperties.getObjectKey(file, "");
        return uploadFile(objectKey, file);
    }

    @Override
    public String uploadFile(String objectKey, File file) {
        has(objectKey, "objectKey is null")
                .has(file, "file is null");
        return uploadFile(objectKey, ossProperties.getBucketName(), file);
    }

    @Override
    public String uploadFile(String objectKey, String bucketName, File file) {
        has(objectKey, "objectKey is null")
                .has(bucketName, "bucketName is null")
                .has(file, "file is null");
        checkBucket(bucketName);
        // 上传文件
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());
        metadata.setContentType(getFileMimeType(file.getName()));
        _getOssClient().putObject(bucketName, objectKey, file, metadata);
        log.info("upload file success, bucketName:{}, objectKey:{}", bucketName, objectKey);
        return ossProperties.getAccessUrl(objectKey);

    }

    @Override
    public String upload(String objectKey, InputStream stream) {
        has(objectKey, "objectKey is null")
                .has(stream, "stream is null");
        return upload(objectKey, ossProperties.getBucketName(), stream);
    }

    @Override
    public String upload(String objectKey, String bucketName, InputStream stream) {
        has(objectKey, "objectKey is null")
                .has(bucketName, "bucketName is null")
                .has(stream, "stream is null");
        return doUpload(objectKey, bucketName, stream);
    }

    protected String doUpload(String objectKey, String bucketName, InputStream stream) {
        has(objectKey, "objectKey is null")
                .has(bucketName, "bucketName is null")
                .has(stream, "stream is null");

        _getOssClient().putObject(bucketName, objectKey, stream);
        log.info("upload file success, bucketName:{}, objectKey:{}", bucketName, objectKey);
        return ossProperties.getAccessUrl(objectKey);
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
            return ossStsToken;

        } catch (Exception e) {
            log.error("get sts token error", e);
            throw new OssException("get sts token error", e);
        } finally {
            // 关闭client
            acsClient.shutdown();
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
        if (!this.ossClient.doesBucketExist(bucketName)) {
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