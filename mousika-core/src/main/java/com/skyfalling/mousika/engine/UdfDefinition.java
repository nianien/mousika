package com.skyfalling.mousika.engine;

import lombok.*;

/**
 * UDF定义
 *
 * @author liyifei <liyifei@kuaishou.com>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UdfDefinition {

    /**
     * udf分组
     */
    private String group;
    /**
     * udf名称
     */
    @NonNull
    private String name;
    /**
     * udf对象
     */
    @NonNull
    private Object udf;

}
