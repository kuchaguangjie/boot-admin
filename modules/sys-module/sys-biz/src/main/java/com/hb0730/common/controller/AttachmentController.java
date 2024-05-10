package com.hb0730.common.controller;

import com.hb0730.base.R;
import com.hb0730.base.exception.ServiceException;
import com.hb0730.base.utils.OssUtil;
import com.hb0730.common.domain.dto.AttachmentDto;
import com.hb0730.common.domain.vo.FilePresignedUrlRespVO;
import com.hb0730.common.service.AttachmentService;
import com.hb0730.common.service.CommonsOssService;
import com.hb0730.oss.core.OssStorage;
import com.hb0730.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/9
 */
@RestController
@RequestMapping("/common/attachment")
@Tag(name = "公共接口：附件管理")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;
    private final CommonsOssService commonsOssService;

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 路径
     * @return 附件信息
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "模式一: 后端上传")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "path", description = "路径", required = false)
    })
    public R<AttachmentDto> uploadFile(@RequestPart MultipartFile file, @RequestParam(required = false) String path) {
        Optional<OssStorage> ossStorage = commonsOssService.getOssStorage(SecurityUtil.getSysCode());
        if (ossStorage.isEmpty()) {
            return R.NG("未找到OSS配置");
        }
        OssStorage storage = ossStorage.get();
        try {
            AttachmentDto attachment = uploadFile(file, path, storage);
            attachmentService.save(attachment);
            return R.OK(attachment);
        } catch (IOException e) {
            return R.NG("上传文件失败");
        }
    }

    /**
     * 生成文件预签名地址
     *
     * @return 文件预签名地址
     */
    @GetMapping("/presigned-url")
    @Operation(summary = "生成文件预签名地址", description = "模式二: 前端上传")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "filename", description = "文件名,支持path格式,2023/10/xxx.png",
                    required =
                            true)
            , @io.swagger.v3.oas.annotations.Parameter(name = "contentType", description = "文件类型", required = false)
    })
    public R<FilePresignedUrlRespVO> presignedUrl(@RequestParam String filename, @RequestParam(required = false) String contentType) {
        Optional<OssStorage> ossStorage = commonsOssService.getOssStorage(SecurityUtil.getSysCode());
        if (ossStorage.isEmpty()) {
            return R.NG("未找到OSS配置");
        }
        Map<String, String> params = new HashMap<>();
        if (contentType != null) {
            params.put("Content-Type", contentType);
        }
        OssStorage storage = ossStorage.get();
        OssStorage.PresignedUrl presignedUrl = storage.getPresignedUrl(filename, params);
        if (presignedUrl != null) {
            FilePresignedUrlRespVO respVO = new FilePresignedUrlRespVO();
            respVO.setAccessUrl(presignedUrl.getAccessUrl());
            respVO.setUploadUrl(presignedUrl.getPresignedUrl());
            return R.OK(respVO);
        }
        throw new ServiceException("生成文件预签名地址失败");
    }

    @PostMapping("/create")
    @Operation(summary = "创建文件", description = "模式二：前端上传文件：配合 presigned-url 接口，记录上传了上传的文件信息")
    public R<String> createFile(@Valid @RequestBody AttachmentDto attachmentDto) {
        String sysCode = SecurityUtil.getSysCode();
        attachmentDto.setSysCode(sysCode);
        attachmentService.save(attachmentDto);
        return R.OK();
    }


    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 路径
     * @return 附件信息
     */
    private AttachmentDto uploadFile(MultipartFile file, String path, OssStorage storage) throws IOException {
        String objectKey = OssUtil.renameObjectKey(file.getOriginalFilename(), path);
        long size = file.getSize();
        String contentType = file.getContentType();
        String accessUrl = storage.upload(objectKey, size, contentType, file.getInputStream());

        AttachmentDto attachment = new AttachmentDto();
        attachment.setDisplayName(objectKey);
        attachment.setPermalink(accessUrl);
        attachment.setSysCode(SecurityUtil.getSysCode());
        attachment.setSize(size);
        attachment.setMediaType(contentType);
        return attachment;
    }

}
