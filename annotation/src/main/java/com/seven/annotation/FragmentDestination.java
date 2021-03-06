package com.seven.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface FragmentDestination {
    String pageUrl();

    boolean isLogin() default false;

    boolean isStart() default false;

}
