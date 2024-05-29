package com.hb0730.code.generator.core.handler.keywords;

import com.hb0730.code.generator.core.handler.IKeyWordsHandler;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 基类关键字处理
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/27
 */
public abstract class BaseKeyWordsHandler implements IKeyWordsHandler {
    public final Set<String> keyWords;

    public BaseKeyWordsHandler(@NotNull List<String> keyWords) {
        this.keyWords = new HashSet<>(keyWords);
    }

    public BaseKeyWordsHandler(@NotNull Set<String> keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public @NotNull Collection<String> getKeyWords() {
        return keyWords;
    }

    @Override
    public boolean isKeyWords(@NotNull String columnName) {
        return getKeyWords().contains(columnName.toUpperCase(Locale.ENGLISH));
    }

}
