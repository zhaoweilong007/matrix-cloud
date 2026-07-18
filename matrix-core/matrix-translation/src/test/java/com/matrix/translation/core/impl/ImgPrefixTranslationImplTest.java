package com.matrix.translation.core.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.matrix.auto.properties.OssProperties;
import com.matrix.translation.annotation.Translation;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ImgPrefixTranslationImplTest {

    @Test
    void translatesOnlyStringValuesAndDoesNotMutateSourceMap() {
        OssProperties properties = new OssProperties();
        properties.setPrefix("https://cdn.example/");
        Translation translation = mock(Translation.class);
        when(translation.other()).thenReturn(";");
        when(translation.fields()).thenReturn(new String[] {"image", "count", "items"});
        Map<String, Object> source = new LinkedHashMap<>();
        source.put("image", "a.png");
        source.put("count", 3);
        source.put("items", List.of("b.png", 4));

        Object result = new ImgPrefixTranslationImpl(properties).translation(source, translation);

        assertNotSame(source, result);
        assertEquals("a.png", source.get("image"));
        assertEquals(
                Map.of(
                        "image", "https://cdn.example/a.png",
                        "count", 3,
                        "items", List.of("https://cdn.example/b.png", 4)),
                result);
    }

    @Test
    void preservesNonStringListItems() {
        OssProperties properties = new OssProperties();
        properties.setPrefix("https://cdn.example/");
        Translation translation = mock(Translation.class);
        when(translation.other()).thenReturn(";");

        Object result = new ImgPrefixTranslationImpl(properties)
                .translation(List.of("a.png", 1), translation);

        assertEquals(List.of("https://cdn.example/a.png", 1), result);
    }
}
