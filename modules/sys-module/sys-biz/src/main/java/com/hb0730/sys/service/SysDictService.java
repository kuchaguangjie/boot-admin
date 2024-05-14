package com.hb0730.sys.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hb0730.base.exception.ServiceException;
import com.hb0730.base.utils.StrUtil;
import com.hb0730.common.api.JsfPage;
import com.hb0730.data.core.service.BaseService;
import com.hb0730.query.jpa.QueryHelper;
import com.hb0730.sys.domain.dto.DictDto;
import com.hb0730.sys.domain.entity.SysDict;
import com.hb0730.sys.domain.query.DictQuery;
import com.hb0730.sys.repository.SysDictRepository;
import com.hb0730.sys.service.mapstruct.SysDictMapstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/5
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysDictService extends BaseService<SysDictRepository, SysDict, String> {
    private final SysDictMapstruct mapstruct;


    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页数据
     */
    public JsfPage<DictDto> page(DictQuery query) {
        Pageable page = QueryHelper.toPage(query);
        Specification<SysDict> specification = QueryHelper.ofBean(query);
        Page<SysDict> pageData = baseRepository.findAll(specification, page);
        List<DictDto> res = mapstruct.toDtoList(pageData.getContent());
        return QueryHelper.toJsfPage(pageData, res);
    }

    /**
     * 查询
     *
     * @param query 查询条件
     * @return 数据
     */
    public List<DictDto> list(DictQuery query) {
        Specification<SysDict> specification = QueryHelper.ofBean(query);
        List<SysDict> list = baseRepository.findAll(specification);
        return mapstruct.toDtoList(list);
    }

    /**
     * 根据类型查询
     *
     * @param type 类型
     * @param id   id
     * @return 是否存在
     */
    public Boolean existsByType(String type, String id) {
        if (StrUtil.isNotBlank(id)) {
            return baseRepository.existsByTypeAndIdNot(type, id);
        }
        return baseRepository.existsByType(type);
    }

    /**
     * 保存
     *
     * @param dto 数据
     */
    @Transactional(rollbackOn = Exception.class)
    public String save(DictDto dto) {
        SysDict entity = mapstruct.toEntity(dto);
        Boolean existsType = existsByType(entity.getType(), entity.getId());
        if (existsType) {
            throw new ServiceException("字典类型已存在");
        }
        save(entity);
        return entity.getId();
    }

    /**
     * 根据ID更新
     *
     * @param dto .
     */
    @Transactional(rollbackOn = Exception.class)
    public void updateById(DictDto dto) {
        String id = dto.getId();
        if (StrUtil.isBlank(id)) {
            throw new ServiceException("id不能为空");
        }
        Boolean existsType = existsByType(dto.getType(), dto.getId());
        if (existsType) {
            throw new ServiceException("字典类型已存在");
        }
        SysDict entity = baseRepository.findById(id).orElseThrow(() -> new ServiceException("数据不存在"));
        BeanUtil.copyProperties(dto, entity, CopyOptions.create().ignoreNullValue());
        updateById(entity);
    }

    /**
     * 根据ID删除
     *
     * @param id id
     */
    @Transactional(rollbackOn = Exception.class)
    public void deleteById(String id) {
        SysDict dict = getById(id);
        if (null == dict) {
            return;
        }
        remove(dict);
    }


}
