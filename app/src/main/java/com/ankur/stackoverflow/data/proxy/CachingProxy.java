package com.ankur.stackoverflow.data.proxy;

import android.text.TextUtils;

import com.ankur.stackoverflow.cache.Cache;
import com.ankur.stackoverflow.cache.CacheEvict;
import com.ankur.stackoverflow.cache.CacheService;
import com.ankur.stackoverflow.cache.CacheUpdate;
import com.ankur.stackoverflow.cache.Cacheable;
import com.ankur.stackoverflow.cache.Caching;
import com.ankur.stackoverflow.common.ValueWrapper;
import com.ankur.stackoverflow.utils.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachingProxy implements java.lang.reflect.InvocationHandler {
    private Object obj;

    public static Object newInstance(Object obj) {
        return java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj
                .getClass().getInterfaces(), new CachingProxy(obj));
    }

    public CachingProxy(Object obj) {
        this.obj = obj;
    }

    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        try {
            Map<Class, CacheOp> cacheOpMap = parseCacheAnnotations(m);

            if (cacheOpMap.size() > 0) {
                boolean cacheablePresent = false;
                Object result = null;
                CacheOp cacheOp = cacheOpMap.get(Cacheable.class);
                if (cacheOp != null) {
                    List<Cacheable> cacheables = cacheOp.cacheOps;
                    if (!CollectionUtils.isEmpty(cacheables)) {
                        cacheablePresent = true;
                        result = handleCacheable(m, args, cacheables.get(0));
                    }
                }

                if (!cacheablePresent) {
                    result = m.invoke(obj, args);
                }

                cacheOp = cacheOpMap.get(CacheEvict.class);
                if (cacheOp != null) {
                    List<CacheEvict> cacheEvicts = cacheOp.cacheOps;
                    if (!CollectionUtils.isEmpty(cacheEvicts)) {
                        for (CacheEvict cacheEvict : cacheEvicts) {
                            handleCacheEvict(args, cacheEvict);
                        }
                    }
                }

                cacheOp = cacheOpMap.get(CacheUpdate.class);
                if (cacheOp != null) {
                    List<CacheUpdate> cacheUpdates = cacheOp.cacheOps;
                    if (!CollectionUtils.isEmpty(cacheUpdates)) {
                        for (CacheUpdate cacheUpdate : cacheUpdates) {
                            handleCacheUpdate(args, cacheUpdate);
                        }
                    }
                }

                return result;
            }
            return m.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception", e);
        }
    }

    private Map<Class, CacheOp> parseCacheAnnotations(AnnotatedElement ae) {
        Map<Class, CacheOp> cacheOpMap = new HashMap<>();

        Collection<Cacheable> cacheables = getAnnotations(ae, Cacheable.class);
        if (cacheables != null) {
            CacheOp<Cacheable> cacheOp = lazyGetCacheOp(cacheOpMap, Cacheable.class);
            cacheOp.addAll(cacheables);
        }

        Collection<CacheEvict> cacheEvicts = getAnnotations(ae, CacheEvict.class);
        if (cacheEvicts != null) {
            CacheOp<CacheEvict> cacheOp = lazyGetCacheOp(cacheOpMap, CacheEvict.class);
            cacheOp.addAll(cacheEvicts);
        }

        Collection<CacheUpdate> cacheUpdates = getAnnotations(ae, CacheUpdate.class);
        if (cacheUpdates != null) {
            CacheOp<CacheUpdate> cacheOp = lazyGetCacheOp(cacheOpMap, CacheUpdate.class);
            cacheOp.addAll(cacheUpdates);
        }

        Collection<Caching> cachings = getAnnotations(ae, Caching.class);
        if (cachings != null) {
            for (Caching caching : cachings) {
                Cacheable[] _cacheables = caching.cacheable();
                if (!CollectionUtils.isEmpty(_cacheables)) {
                    CacheOp<Cacheable> cacheOp = lazyGetCacheOp(cacheOpMap, Cacheable.class);
                    cacheOp.addAll(Arrays.asList(_cacheables));
                }

                CacheEvict[] _cacheEvicts = caching.cacheEvict();
                if (!CollectionUtils.isEmpty(_cacheEvicts)) {
                    CacheOp<CacheEvict> cacheOp = lazyGetCacheOp(cacheOpMap, CacheEvict.class);
                    cacheOp.addAll(Arrays.asList(_cacheEvicts));
                }

                CacheUpdate[] _cacheUpdates = caching.cacheUpdate();
                if (!CollectionUtils.isEmpty(_cacheUpdates)) {
                    CacheOp<CacheUpdate> cacheOp = lazyGetCacheOp(cacheOpMap, CacheUpdate.class);
                    cacheOp.addAll(Arrays.asList(_cacheUpdates));
                }
            }
        }

        return cacheOpMap;
    }

    private <T> CacheOp<T> lazyGetCacheOp(Map<Class, CacheOp> cacheOpMap, Class<T> klass) {
        CacheOp<T> cacheOp = cacheOpMap.get(klass);
        if (cacheOp == null) {
            cacheOp = new CacheOp<T>();
            cacheOpMap.put(klass, cacheOp);
        }
        return cacheOp;
    }

    private <T extends Annotation> Collection<T> getAnnotations(AnnotatedElement ae, Class<T> annotationType) {
        Collection<T> anns = new ArrayList<T>(2);

        // look at raw annotation
        T ann = ae.getAnnotation(annotationType);
        if (ann != null) {
            anns.add(ann);
        }

        // scan meta-annotations
        for (Annotation metaAnn : ae.getAnnotations()) {
            ann = metaAnn.annotationType().getAnnotation(annotationType);
            if (ann != null) {
                anns.add(ann);
            }
        }

        return (anns.isEmpty() ? null : anns);
    }

    private Object handleCacheable(Method m, Object[] args, Cacheable cacheable) throws Exception {
        Cache cache = CacheService.getCache(cacheable.cacheName());
        String key = generateKey(cacheable.key(), cacheable.keyGenClass(), cacheable.keyGenMethod(), args);
        Object cacheResult = cache.get(key);

        if (cacheResult != null) {
            Object result = ((ValueWrapper) cacheResult).getValue();
            if (result instanceof ValueWrapper) {
                ((ValueWrapper) result).setCacheHit(true);
            }
            return result;
        }

        Object result = m.invoke(obj, args);
        Object resultUnwrapped = result;
        if (result instanceof ValueWrapper) {
            resultUnwrapped = ((ValueWrapper) result).getValue();
        }
        if (resultUnwrapped != null || cacheable.cacheNull()) {
            cache.put(key, result);
        }
        return result;
    }

    private void handleCacheEvict(Object[] args, CacheEvict cacheEvict) throws Exception {
        Cache cache = CacheService.getCache(cacheEvict.cacheName());
        String key = generateKey(cacheEvict.key(), cacheEvict.keyGenClass(), cacheEvict.keyGenMethod(), args);
        cache.evict(key);
    }

    private void handleCacheUpdate(Object[] args, CacheUpdate cacheUpdate) throws Exception {
        Cache cache = CacheService.getCache(cacheUpdate.cacheName());
        String key = generateKey(cacheUpdate.key(), cacheUpdate.keyGenClass(), cacheUpdate.keyGenMethod(), args);

        Object arg = args[cacheUpdate.argNum()];
        Object argUnwrapped = arg;
        if (arg instanceof ValueWrapper) {
            argUnwrapped = ((ValueWrapper) arg).getValue();
        }
        if (argUnwrapped != null || cacheUpdate.cacheNull()) {
            cache.put(key, arg);
        }
    }

    private String generateKey(String key, Class kenGenKlass, String keyGenMethodName, Object[] args) throws Exception {
        if (!TextUtils.isEmpty(key)) {
            return key;
        }

        Method keyGenMethod = getMethod(kenGenKlass, keyGenMethodName, args);
        return (String) keyGenMethod.invoke(null, args);
    }

    private Method getMethod(Class targetKlass, String methodName, Object[] parameters) {
        for (Method method : targetKlass.getMethods()) {
            if (!method.getName().equals(methodName)) {
                continue;
            }

            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != parameters.length) {
                continue;
            }

            boolean matches = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                if (!parameterTypes[i].isAssignableFrom(parameters[i]
                        .getClass())) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                return method;
            }
        }
        return null;
    }

    static class CacheOp<T> {
        List<T> cacheOps;

        public void addAll(Collection<T> ops) {
            if (cacheOps == null) {
                cacheOps = new ArrayList<>();
            }

            cacheOps.addAll(ops);
        }

        public void add(T op) {
            if (cacheOps == null) {
                cacheOps = new ArrayList<>();
            }

            cacheOps.add(op);
        }
    }
}