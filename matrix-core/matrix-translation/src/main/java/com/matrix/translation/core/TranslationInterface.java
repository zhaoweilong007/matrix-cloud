package com.matrix.translation.core;

import com.matrix.translation.annotation.Translation;

/**
 * 翻译接口 (实现类需标注 TranslationType 注解标明翻译类型)
 */
public interface TranslationInterface<T> {

    /**
     * 翻译
     *
     * @param key 需要被翻译的键(不为空)
     * @return 返回键对应的值
     */
    T translation(Object key, Translation translation);
}
