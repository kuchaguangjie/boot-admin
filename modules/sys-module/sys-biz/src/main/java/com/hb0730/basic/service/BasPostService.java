package com.hb0730.basic.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hb0730.base.exception.ServiceException;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.basic.domain.dto.BasPostDto;
import com.hb0730.basic.domain.entity.BasPost;
import com.hb0730.basic.domain.query.BasPostQuery;
import com.hb0730.basic.repository.BasPostRepository;
import com.hb0730.basic.service.mapstruct.BasPostMapstruct;
import com.hb0730.common.api.JsfPage;
import com.hb0730.data.core.service.BaseService;
import com.hb0730.query.jpa.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/7
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BasPostService extends BaseService<BasPostRepository, BasPost, String> {
    private final BasPostMapstruct mapstruct;


    /**
     * 分页查询
     *
     * @param query .
     * @return .
     */
    public JsfPage<BasPostDto> page(BasPostQuery query) {
        Pageable page = QueryHelper.toPage(query);
        Specification<BasPost> specification = QueryHelper.ofBean(query);
        Page<BasPost> pageData = baseRepository.findAll(specification, page);
        List<BasPostDto> res = mapstruct.toDtoList(pageData.getContent());
        return QueryHelper.toJsfPage(pageData, res);
    }

    /**
     * 查询
     *
     * @param query .
     * @return .
     */
    public List<BasPostDto> list(BasPostQuery query) {
        Specification<BasPost> specification = QueryHelper.ofBean(query);
        List<BasPost> list = baseRepository.findAll(specification);
        return mapstruct.toDtoList(list);
    }

    /**
     * 根据编码和系统编码查询
     *
     * @param code    .
     * @param sysCode .
     * @param id      需要排除的id
     * @return .
     */
    public Boolean existsByCodeAndSysCode(String code, String sysCode, String id) {
        if (StrUtil.isBlank(id)) {
            return baseRepository.existsByCodeAndSysCode(code, sysCode);
        }
        return baseRepository.existsByCodeAndSysCodeAndIdNot(code, sysCode, id);
    }

    /**
     * 保存
     *
     * @param dto .
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(BasPostDto dto) {
        Boolean exists = existsByCodeAndSysCode(dto.getCode(), dto.getSysCode(), dto.getId());
        if (exists) {
            throw new ServiceException("编码已存在");
        }
        BasPost entity = mapstruct.toEntity(dto);
        save(entity);
    }

    /**
     * 更新
     *
     * @param dto .
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateById(BasPostDto dto) {
        Boolean exists = existsByCodeAndSysCode(dto.getCode(), dto.getSysCode(), dto.getId());
        if (exists) {
            throw new ServiceException("编码已存在");
        }
        BasPost post = findById(dto.getId());
        if (post == null) {
            throw new ServiceException("数据不存在");
        }
        BeanUtil.copyProperties(dto, post, CopyOptions.create().setIgnoreNullValue(true));
        updateById(post);
    }

    /**
     * 删除
     *
     * @param id .
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        BasPost post = findById(id);
        if (post == null) {
            throw new ServiceException("数据不存在");
        }
        if (baseRepository.existsUserPostByPostId(id)) {
            throw new ServiceException("存在用户岗位关联");
        }
        remove(post);
    }

}
