package com.skyfalling.mousika.ui.tree.resolver;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.skyfalling.mousika.ui.tree.node.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理UI树节点类型的序列化与反序列化,支持多态
 *
 * @author liyifei
 */
@Slf4j
public class NodeTypeResolver extends TypeIdResolverBase {
    @Override
    public String idFromValue(Object value) {
        return value.getClass().getSimpleName().substring(0, 1).toUpperCase();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        log.info("suggestedType{}", suggestedType);
        return idFromValue(value);
    }

    @Override
    public Id getMechanism() {
        return Id.CUSTOM;
    }


    @SneakyThrows
    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        switch (id) {
            case "A":
                return SimpleType.constructUnsafe(ANode.class);
            case "F":
                return SimpleType.constructUnsafe(FNode.class);
            case "G":
                return SimpleType.constructUnsafe(GNode.class);
            case "L":
                return SimpleType.constructUnsafe(LNode.class);
            case "P":
                return SimpleType.constructUnsafe(PNode.class);
            case "R":
                return SimpleType.constructUnsafe(RNode.class);
            case "T":
                return SimpleType.constructUnsafe(TreeNode.class);
            case "Z":
                return SimpleType.constructUnsafe(ZNode.class);
            default:
                return super.typeFromId(context, id);
        }
    }
}