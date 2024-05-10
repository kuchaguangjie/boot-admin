package com.hb0730.common.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/8
 */
@RestController
@RequestMapping("/commons")
@Tag(name = "公共接口")
@RequiredArgsConstructor
public class CommonsController {
}
