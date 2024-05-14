package com.hb0730.sys.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hb0730.base.exception.ServiceException;
import com.hb0730.base.utils.ObjectUtil;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.base.utils.TreeUtil;
import com.hb0730.common.api.JsfPage;
import com.hb0730.data.core.service.BaseService;
import com.hb0730.query.jpa.QueryHelper;
import com.hb0730.sys.domain.dto.AreaDto;
import com.hb0730.sys.domain.entity.SysArea;
import com.hb0730.sys.domain.query.AreaQuery;
import com.hb0730.sys.domain.vo.AreaTreeVo;
import com.hb0730.sys.repository.SysAreaRepository;
import com.hb0730.sys.service.mapstruct.SysAreaMapstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/10
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysAreaService extends BaseService<SysAreaRepository, SysArea, String> {
    private final SysAreaMapstruct mapstruct;


    /**
     * 树,查询已启用的
     *
     * @return .
     */
    public List<AreaTreeVo> treeEnabled() {
        List<SysArea> list = baseRepository.findAllByEnabledIsTrueOrderByCreated();
        List<AreaTreeVo> tree = mapstruct.toTreeVoList(list);
        return TreeUtil.buildTree(tree);
    }

    /**
     * 分页
     *
     * @param query .
     * @return .
     */
    public JsfPage<AreaDto> page(AreaQuery query) {
        fillQuery(query);
        Specification<SysArea> specification = QueryHelper.ofBean(query);
        Pageable page = QueryHelper.toPage(query);
        Page<SysArea> pageData = baseRepository.findAll(specification, page);
        List<AreaDto> res = mapstruct.toDtoList(pageData.getContent());
        return QueryHelper.toJsfPage(pageData, res);
    }

    /**
     * 列表
     *
     * @param query .
     * @return .
     */
    public List<AreaDto> list(AreaQuery query) {
        fillQuery(query);
        Specification<SysArea> specification = QueryHelper.ofBean(query);
        Sort sort = QueryHelper.toSort(query);
        List<SysArea> list = baseRepository.findAll(specification, sort);
        return mapstruct.toDtoList(list);
    }

    /**
     * 保存
     *
     * @param dto .
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(AreaDto dto) {
        String code = dto.getCode();
        if (baseRepository.existsByCode(code)) {
            throw new ServiceException("code已存在");
        }
        SysArea entity = mapstruct.toEntity(dto);
        fillParent(entity);
        baseRepository.save(entity);
    }

    /**
     * 更新
     *
     * @param dto .
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateById(AreaDto dto) {
        String id = dto.getId();
        String code = dto.getCode();
        if (baseRepository.existsByCodeAndIdNot(code, id)) {
            throw new ServiceException("code已存在");
        }
        SysArea entity = baseRepository.findById(id).orElseThrow(() -> new ServiceException("数据不存在"));
        BeanUtil.copyProperties(dto, entity, CopyOptions.create().ignoreNullValue());
        fillParent(entity);
        baseRepository.save(entity);
        updateEnabledChild(id, entity.getEnabled());
    }

    /**
     * 删除
     *
     * @param id .
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        if (baseRepository.hashChild(id)) {
            throw new ServiceException("存在子节点");
        }
        baseRepository.deleteById(id);
    }

    /**
     * 启用/禁用
     *
     * @param id      .
     * @param enabled .
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabledChild(String id, boolean enabled) {
        SysArea entity = baseRepository.findById(id).orElseThrow(() -> new ServiceException("数据不存在"));
        baseRepository.updateEnabledChild(entity.getPath(), enabled);
    }

    private void fillParent(SysArea entity) {
        entity.setId(entity.getCode());
        String parentId = entity.getParentId();
        if (StrUtil.isNotBlank(parentId)) {
            SysArea parent = baseRepository.findById(parentId).orElseThrow(() -> new ServiceException("父级数据不存在"));
            entity.setParentId(parent.getId());
            entity.setPath(parent.getPath() + "," + entity.getId());
            entity.setLevel(parent.getLevel() + 1);
            entity.setFullName(parent.getFullName() + " " + entity.getName());

        } else {
            entity.setId(entity.getCode());
            entity.setLevel(1);
            entity.setPath(entity.getId());
            entity.setFullName(entity.getName());
        }
    }

    private void fillQuery(AreaQuery query) {
        boolean fieldNonNull = ObjectUtil.fieldNonNull(query, "parentId", "name", "code", "enabled");
        if (!fieldNonNull) {
            query.setParentIdIsNull("parentId");
        }
    }
}
