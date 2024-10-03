package com.kucw.security.cache;

import com.kucw.security.cache.model.BaseTxData;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/** cache 暫存 */
@Component
public class CacheManager {

    private final ConcurrentHashMap<String, CacheObject<? extends BaseTxData>> cacheMap = new ConcurrentHashMap<>();

    // 通用的 setTxData 方法
    public <T extends BaseTxData> void setTxData(T data, long ttl) {
        String key = data.getClass().getSimpleName();
        // 緩存時間
        long expiryTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(ttl, TimeUnit.SECONDS);
        cacheMap.put(key, new CacheObject<>(data, expiryTime));
    }

    // 通用的 getTxData 方法
    @SuppressWarnings("unchecked")
    public <T extends BaseTxData> T getTxData(Class<T> clazz) {

        String key = clazz.getSimpleName();
        CacheObject<? extends BaseTxData> cacheObject = cacheMap.get(key);
        if (cacheObject == null || System.currentTimeMillis() > cacheObject.getExpiryTime()) {
            cacheMap.remove(key);
            return null; // 資料不存在或已過期
        }
        return (T) cacheObject.getValue();
    }

    // 清理過期的 cache
    public void cleanUp() {
        cacheMap.entrySet().removeIf(entry -> System.currentTimeMillis() > entry.getValue().getExpiryTime());
    }

    // 內部 CacheObject 類
    private static class CacheObject<T extends BaseTxData> {
        private final T value;
        private final long expiryTime;

        public CacheObject(T value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        public T getValue() {
            return value;
        }

        public long getExpiryTime() {
            return expiryTime;
        }
    }
}
