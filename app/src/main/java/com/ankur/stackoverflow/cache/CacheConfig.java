package com.ankur.stackoverflow.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheConfig {
    /*
       time to live (usually for a cache zone). -1 indicates no expiry
     */
    long ttl() default -1;

    long maxSize() default -1;

    /**
     * eviction policy (usually for a cache zone).
     *
     * @return
     */
    EvictionPolicy evictionPolicy() default EvictionPolicy.NO_EVICTION;
}