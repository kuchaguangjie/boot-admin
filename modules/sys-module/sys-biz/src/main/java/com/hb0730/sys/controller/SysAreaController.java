package com.hb0730.sys.controller;

import com.hb0730.base.R;
import com.hb0730.common.api.JsfPage;
import com.hb0730.sys.domain.dto.AreaDto;
import com.hb0730.sys.domain.query.AreaQuery;
import com.hb0730.sys.domain.vo.AreaTreeVo;
import com.hb0730.sys.service.SysAreaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地区
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/10
 */
@RestController
@RequestMapping("/sys/area")
@RequiredArgsConstructor
public class SysAreaController {
    private final SysAreaService areaService;

    /**
     * 树,查询已启用的
     *
     * @return .
     */
    @GetMapping("/tree")
    @Operation(summary = "树形结构地区", description = "查询已启用的")
    public R<List<AreaTreeVo>> treeEnabled() {
        return R.OK(areaService.treeEnabled());
    }

    /**
     * 分页
     *
     * @param query .
     * @return .
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @PreAuthorize("hasAnyAuthority('sys:area:query')")
    public R<JsfPage<AreaDto>> page(AreaQuery query) {
        return R.OK(areaService.page(query));
    }

    /**
     * 查询
     *
     * @param query .
     * @return .
     */
    @GetMapping("/list")
    @Operation(summary = "查询")
    @PreAuthorize("hasAnyAuthority('sys:area:query')")
    public R<List<AreaDto>> list(AreaQuery query) {
        return R.OK(areaService.list(query));
    }

    /**
     * 保存
     *
     * @param dto .
     * @return .
     */
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAnyAuthority('sys:area:save')")
    public R<String> save(@Valid @RequestBody AreaDto dto) {
        areaService.save(dto);
        return R.OK();
    }

    /**
     * 更新
     *
     * @param dto .
     * @return .
     */
    @PutMapping
    @Operation(summary = "更新")
    @PreAuthorize("hasAnyAuthority('sys:area:update')")
    public R<String> update(@Valid @RequestBody AreaDto dto) {
        areaService.updateById(dto);
        return R.OK();
    }


    /**
     * 删除
     *
     * @param id 主键
     * @return .
     */
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAnyAuthority('sys:area:delete')")
    public R<String> delete(String id) {
        areaService.deleteById(id);
        return R.OK();
    }
}
