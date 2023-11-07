package com.skyfalling.mousika;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.jar.asm.Opcodes;

/**
 * @author : liyifei
 * @created : 2023/11/6, 星期一
 * Copyright (c) 2004-2029 All Rights Reserved.
 **/
public class ByteBuddyTest {

    public void test() {
          new ByteBuddy().subclass(Object.class)
                    .defineField("replace", Object.class, Visibility.PUBLIC)
                    .make()
                    .load(Thread.currentThread().getContextClassLoader());
    }
}
