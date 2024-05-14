package com.hb0730.sys.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hb0730.base.exception.ServiceException;
import com.hb0730.base.utils.CollectionUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.common.api.JsfPage;
import com.hb0730.data.core.service.BaseService;
import com.hb0730.query.jpa.QueryHelper;
import com.hb0730.sys.domain.dto.NoticeDto;
import com.hb0730.sys.domain.entity.SysNotice;
import com.hb0730.sys.domain.query.NoticeQuery;
import com.hb0730.sys.repository.SysNoticeRepository;
import com.hb0730.sys.service.mapstruct.SysNoticeMapstruct;
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
 * @date 2024/4/29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysNoticeService extends BaseService<SysNoticeRepository, SysNotice, String> {
    private final SysNoticeMapstruct mapstruct;

    /**
     * 统计不在ids中的数量
     *
     * @param ids ids
     * @return 数量
     */
    public Integer countNotInIds(List<String> ids) {
        return baseRepository.countByIdNotInAndEnabledIsTrue(ids);
    }

    /**
     * 查询不在ids中的id
     *
     * @param ids ids
     * @return id
     */
    public List<String> getIdsNotInIds(List<String> ids) {
        List<SysNotice> res = baseRepository.findByIdNotInAndEnabledIsTrue(ids);
        if (CollectionUtil.isEmpty(res)) {
            return null;
        }
        return CollectionUtil.listToList(res, SysNotice::getId);
    }

    /**
     * 统计公告数量
     *
     * @return 数量
     */
    public int countNotices() {
        return baseRepository.countByEnabledIsTrue();
    }

    /**
     * 查询所有id
     *
     * @return id
     */
    public List<String> getIds() {
        List<SysNotice> res = baseRepository.findByEnabledIsTrue();
        if (CollectionUtil.isEmpty(res)) {
            return null;
        }
        return CollectionUtil.listToList(res, SysNotice::getId);
    }


    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页数据
     */
    public JsfPage<NoticeDto> page(NoticeQuery query) {
        Specification<SysNotice> specification = QueryHelper.ofBean(query);
        Pageable page = QueryHelper.toPage(query);
        Page<SysNotice> pageData = baseRepository.findAll(specification, page);
        List<NoticeDto> res = mapstruct.toDtoList(pageData.getContent());
        return QueryHelper.toJsfPage(pageData, res);
    }

    /**
     * 查询
     *
     * @param query 查询条件
     * @return 数据
     */
    public List<NoticeDto> list(NoticeQuery query) {
        Specification<SysNotice> specification = QueryHelper.ofBean(query);
        List<SysNotice> list = baseRepository.findAll(specification);
        return mapstruct.toDtoList(list);
    }

    /**
     * 保存
     *
     * @param dto dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(NoticeDto dto) {
        SysNotice entity = mapstruct.toEntity(dto);
        save(entity);
    }

    /**
     * 更新
     *
     * @param dto dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateById(NoticeDto dto) {
        String id = dto.getId();
        if (StrUtil.isBlank(id)) {
            throw new ServiceException("id不能为空");
        }
        SysNotice sysNotice = baseRepository.findById(id).orElseThrow(() -> new ServiceException("数据不存在"));
        BeanUtil.copyProperties(
                dto,
                sysNotice,
                CopyOptions.create().ignoreNullValue()
        );
        updateById(sysNotice);
    }

    /**
     * 通过id删除
     *
     * @param ids id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        removeByIds(ids);
    }

    /**
     * 通过id关闭
     *
     * @param ids id
     */
    @Transactional(rollbackFor = Exception.class)
    public void closeByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        List<SysNotice> res = baseRepository.findByIdNotInAndEnabledIsTrue(ids);
        if (CollectionUtil.isEmpty(res)) {
            return;
        }
        res.forEach(sysNotice -> {
            sysNotice.setEnabled(false);
            sysNotice.setModifiedBy("SYSTEM");
        });
        updateBatch(res);
    }

}
