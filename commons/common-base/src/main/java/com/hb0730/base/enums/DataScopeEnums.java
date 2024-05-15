package com.hb0730.base.enums;

import lombok.Getter;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/15
 */
@Getter
public enum DataScopeEnums implements ValueEnum<Integer> {
    ALL(0, "全部数据"),
    THIS_LEVEL(1, "本级数据"),
    THIS_LEVEL_CHILDREN(2, "本级及子级数据"),
    CUSTOMIZE(3, "自定义数据"),
    ;

    private final Integer value;
    private final String desc;

    DataScopeEnums(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
