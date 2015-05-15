package com.ankur.stackoverflow.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheUpdate {

    String cacheName() default "default";

    /**
     * Hardcoded key value.
     *
     * @return
     */
    String key() default "";

    /**
     * key generator class
     *
     * @return
     */
    Class keyGenClass() default CacheKeyGenerator.class;

    /**
     * key generator method.
     *
     * @return
     */
    String keyGenMethod() default "";

    boolean cacheNull() default false;

    int argNum() default 0;
}
