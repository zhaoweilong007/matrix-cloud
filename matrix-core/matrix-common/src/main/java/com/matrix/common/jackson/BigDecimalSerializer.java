package com.matrix.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.matrix.common.annotation.BigDecimalFormat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author ZhaoWeiLong
 * @since 2023/7/18
 **/
public class BigDecimalSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    protected final static int MAX_BIG_DECIMAL_SCALE = 9999;
    private BigDecimalFormat bigDecimalFormat;

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        BigDecimalFormat bigDecimalFormat = property.getAnnotation(BigDecimalFormat.class);
        if (Objects.nonNull(bigDecimalFormat)) {
            this.bigDecimalFormat = bigDecimalFormat;
        }
        return this;
    }


    // 24-Aug-2016, tatu: [core#315] prevent possible DoS vector, so we need this
    protected boolean _verifyBigDecimalRange(JsonGenerator gen, BigDecimal value) throws IOException {
        int scale = value.scale();
        return ((scale >= -MAX_BIG_DECIMAL_SCALE) && (scale <= MAX_BIG_DECIMAL_SCALE));
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        if (!(value instanceof BigDecimal bigDecimal)) {
            gen.writeObject(value);
            return;
        }

        if (gen.isEnabled(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)) {
            final BigDecimal bd = bigDecimal;
            // 24-Aug-2016, tatu: [core#315] prevent possible DoS vector, so we need this
            if (!_verifyBigDecimalRange(gen, bd)) {
                // ... but wouldn't it be nice to trigger error via generator? Alas,
                // no method to do that. So we'll do...
                final String errorMsg = String.format(
                        "Attempt to write plain `java.math.BigDecimal` (see JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN) with illegal scale (%d): needs to be between [-%d, %d]",
                        bd.scale(), MAX_BIG_DECIMAL_SCALE, MAX_BIG_DECIMAL_SCALE);
                provider.reportMappingProblem(errorMsg);
            }
        }
        if (bigDecimalFormat == null) {
            gen.writeString(bigDecimal.toPlainString());
            return;
        }

        final BigDecimalFormat.Format format = bigDecimalFormat.format();
        var mappingResult = switch (format) {
            case normal -> bigDecimal;
            case multi -> bigDecimal.multiply(BigDecimal.valueOf(bigDecimalFormat.multiple()));
            case divide -> bigDecimal.divide(BigDecimal.valueOf(bigDecimalFormat.multiple()), bigDecimalFormat.mode());
        };
        gen.writeString(mappingResult.toPlainString());
    }


}
