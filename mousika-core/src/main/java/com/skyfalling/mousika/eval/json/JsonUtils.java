package com.skyfalling.mousika.eval.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;

/**
 * Created on 2022/2/15
 *
 * @author liyifei
 */
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper()
            //不序列化null值
            .setSerializationInclusion(Include.NON_NULL)
            // 允许字段名不用引号
            .configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            // 允许使用单引号
            .configure(Feature.ALLOW_SINGLE_QUOTES, true)
            // 允许数字含有前导0
            .configure(Feature.ALLOW_NUMERIC_LEADING_ZEROS, true)
            .configure(Feature.STRICT_DUPLICATE_DETECTION, true)
            // 允许未知的属性
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

    static {
        objectMapper.setSerializerFactory(
                objectMapper.getSerializerFactory().withSerializerModifier(new RuleNodeSerializerModifier())
        );
    }

    @SneakyThrows
    public static String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }
}
