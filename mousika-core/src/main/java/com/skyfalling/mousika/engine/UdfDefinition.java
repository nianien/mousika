package com.skyfalling.mousika.engine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UDF定义
 * @author liyifei <liyifei@kuaishou.com>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UdfDefinition {

    /**
     * udf名称
     */
    private String name;
    /**
     * udf对象
     */
    private Object udf;

}
