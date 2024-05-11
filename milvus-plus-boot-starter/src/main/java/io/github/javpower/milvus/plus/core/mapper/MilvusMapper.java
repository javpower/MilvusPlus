package io.github.javpower.milvus.plus.core.mapper;

import io.github.javpower.milvus.plus.annotation.MilvusCollection;
import io.github.javpower.milvus.plus.cache.ConversionCache;
import io.github.javpower.milvus.plus.cache.MilvusCache;
import io.github.javpower.milvus.plus.core.conditions.LambdaDeleteWrapper;
import io.github.javpower.milvus.plus.core.conditions.LambdaSearchWrapper;
import io.github.javpower.milvus.plus.core.conditions.Wrapper;
import io.github.javpower.milvus.plus.service.MilvusClient;
import io.github.javpower.milvus.plus.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author xgc
 **/
@Slf4j
public class MilvusMapper<T> {

    /**
     * 创建搜索构建器实例
     * @return 返回搜索构建器
     */
    public LambdaSearchWrapper<T> searchWrapper() {
        return lambda(new LambdaSearchWrapper<>());
    }

    /**
     * 创建删除构建器实例
     * @return 返回删除构建器
     */
    public LambdaDeleteWrapper<T> deleteWrapper() {
        return lambda(new LambdaDeleteWrapper<>());
    }

    /**
     * 创建通用构建器实例
     * @param wrapper 构建器实例
     * @return 返回构建器实例
     */
    public  <W> W lambda(Wrapper<W, T> wrapper) {
        // 获取实例化的类的类型参数T
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Class<T> entityType = (Class<T>) type;
        // 从实体类上获取@MilvusCollection注解
        MilvusCollection collectionAnnotation = entityType.getAnnotation(MilvusCollection.class);
        if (collectionAnnotation == null) {
            throw new IllegalStateException("Entity type " + entityType.getName() + " is not annotated with @MilvusCollection.");
        }
        ConversionCache<?, ?> conversionCache = MilvusCache.milvusCache.get(entityType);
        String collectionName = conversionCache == null ? null : conversionCache.getCollectionName();
        // 使用SpringUtil获取MilvusClient实例
        MilvusClient client = SpringUtils.getBean(MilvusClient.class);
        // 初始化构建器实例
        wrapper.init(collectionName, client, conversionCache, entityType);
        return wrapper.wrapper();
    }



}