package com.skyfalling.mousika.eval.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

/**
 * 修改运行时序列化方案
 */
public class RuleNodeSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        if (serializer instanceof BeanSerializerBase) {
            return new RuleNodeSerializer((BeanSerializerBase) serializer);
        }
        return serializer;
    }
}