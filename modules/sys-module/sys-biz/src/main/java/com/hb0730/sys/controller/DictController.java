package com.hb0730.sys.controller;

import com.hb0730.base.R;
import com.hb0730.common.api.JsfPage;
import com.hb0730.common.domain.SelectOptionVo;
import com.hb0730.sys.domain.dto.DictDto;
import com.hb0730.sys.domain.dto.DictItemDto;
import com.hb0730.sys.domain.query.DictItemQuery;
import com.hb0730.sys.domain.query.DictQuery;
import com.hb0730.sys.service.SysDictItemService;
import com.hb0730.sys.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/5
 */
@RestController
@RequestMapping("/sys/dict")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "管理端：字典管理")
public class DictController {
    private final SysDictService dictService;
    private final SysDictItemService dictItemService;


    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @PreAuthorize("hasAnyAuthority('sys:dict:query')")
    public R<JsfPage<DictDto>> page(DictQuery query) {
        JsfPage<DictDto> res = dictService.page(query);
        return R.OK(res);
    }

    @GetMapping
    @Operation(summary = "查询")
    public R<List<DictDto>> list(DictQuery query) {
        List<DictDto> res = dictService.list(query);
        return R.OK(res);
    }

    @PostMapping
    @Operation(summary = "新增字典")
    @PreAuthorize("hasAnyAuthority('sys:dict:save')")
    @ApiResponse(description = "成功,返回对应的ID", responseCode = "200")
    public R<String> save(@Valid @RequestBody DictDto dto) {
        String id = dictService.save(dto);
        return R.OK(id);
    }

    @PutMapping
    @Operation(summary = "修改字典")
    @PreAuthorize("hasAnyAuthority('sys:dict:update')")
    public R<String> update(@Valid @RequestBody DictDto dto) {
        String id = dto.getId();
        if (id == null) {
            return R.NG("id不能为空");
        }
        dictService.updateById(dto);
        return R.OK();
    }

    @DeleteMapping
    @Operation(summary = "删除字典")
    @PreAuthorize("hasAnyAuthority('sys:dict:delete')")
    public R<String> delete(String id) {
        dictService.deleteById(id);
        return R.OK();
    }


    @GetMapping("/item")
    @Operation(summary = "查询字典项")
    public R<List<DictItemDto>> listItem(DictItemQuery query) {
        List<DictItemDto> res = dictItemService.list(query);
        return R.OK(res);
    }

    @GetMapping("/item/page")
    @Operation(summary = "分页查询字典项")
    @PreAuthorize("hasAnyAuthority('sys:dict:item:query')")
    public R<JsfPage<DictItemDto>> pageItem(DictItemQuery query) {
        JsfPage<DictItemDto> res = dictItemService.page(query);
        return R.OK(res);
    }

    @PostMapping("/item")
    @Operation(summary = "新增字典项")
    @PreAuthorize("hasAnyAuthority('sys:dict:item:save')")
    public R<String> saveItem(@Valid @RequestBody DictItemDto dto) {
        dictItemService.save(dto);
        return R.OK();
    }

    @PutMapping("/item")
    @Operation(summary = "修改字典项")
    @PreAuthorize("hasAnyAuthority('sys:dict:item:update')")
    public R<String> updateItem(@Valid @RequestBody DictItemDto dto) {
        dictItemService.updateById(dto);
        return R.OK();
    }

    @DeleteMapping("/item")
    @Operation(summary = "删除字典项")
    @PreAuthorize("hasAnyAuthority('sys:dict:item:delete')")
    public R<String> deleteItem(String id) {
        dictItemService.deleteById(id);
        return R.OK();
    }


    /**
     * 查询字典项选项
     *
     * @param type 字典类型
     * @return .
     */
    @GetMapping("/options")
    @Operation(summary = "查询字典项选项")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "type", description = "字典类型", required = true)
    })
    public R<List<SelectOptionVo>> selectOption(@RequestParam String type) {
        List<SelectOptionVo> res = dictItemService.selectOption(type);
        return R.OK(res);
    }

}
