package com.hb0730.common.controller;

import com.hb0730.base.R;
import com.hb0730.sys.domain.dto.AreaDto;
import com.hb0730.sys.domain.query.AreaQuery;
import com.hb0730.sys.service.SysAreaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地区
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/10
 */
@RestController
@RequestMapping("/common/area")
@RequiredArgsConstructor
public class AreaController {
    private final SysAreaService areaService;


    /**
     * 查询
     *
     * @param parentId .
     * @return .
     */
    @GetMapping
    @Operation(summary = "查询", description = "如果parentId为空,则查询的第一级,且只查询已启用的地区信息")
    public R<List<AreaDto>> find(@RequestParam(required = false) String parentId) {
        AreaQuery areaQuery = new AreaQuery();
        areaQuery.setParentId(parentId);
        areaQuery.setEnabled(true);
        List<AreaDto> list = areaService.list(areaQuery);
        return R.OK(list);
    }
}
