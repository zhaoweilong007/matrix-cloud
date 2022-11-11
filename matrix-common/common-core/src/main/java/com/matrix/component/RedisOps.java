package com.matrix.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.matrix.entity.vo.CacheHashKey;
import com.matrix.entity.vo.CacheKey;
import com.matrix.entity.vo.NullVal;
import com.matrix.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述： redis操作工具类
 *
 * @author zwl
 * @since 2022/8/8 15:28
 **/
@Slf4j
public class RedisOps {
    private static final Map<String, Object> KEY_LOCKS = new ConcurrentHashMap<>();
    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOps;
    private final HashOperations<String, Object, Object> hashOps;
    private final ListOperations<String, Object> listOps;
    private final SetOperations<String, Object> setOps;
    private final ZSetOperations<String, Object> zSetOps;
    private final StringRedisTemplate stringRedisTemplate;
    private final boolean defaultCacheNullVal;

    public RedisOps(@NotNull RedisTemplate<String, Object> redisTemplate, @NotNull StringRedisTemplate stringRedisTemplate, boolean defaultCacheNullVal) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
        this.hashOps = redisTemplate.opsForHash();
        this.listOps = redisTemplate.opsForList();
        this.setOps = redisTemplate.opsForSet();
        this.zSetOps = redisTemplate.opsForZSet();
        this.stringRedisTemplate = stringRedisTemplate;
        this.defaultCacheNullVal = defaultCacheNullVal;
    }

    private void setExpire(CacheKey key) {
        if (key != null && key.getExpire() != null) {
            this.redisTemplate.expire(key.getKey(), key.getExpire());
        }

    }

    private static <T> boolean isNullVal(T value) {
        boolean isNull = value == null || NullVal.class.equals(value.getClass());
        return isNull || value.getClass().equals(Object.class) || value instanceof Map && ((Map) value).isEmpty();
    }

    private NullVal newNullVal() {
        return new NullVal();
    }

    private <T> T returnVal(T value) {
        return isNullVal(value) ? null : value;
    }

    public Long del(@NotNull CacheKey... keys) {
        return this.del(Lists.newArrayList(keys));
    }

    public Long del(@NotNull String... keys) {
        return this.del(Lists.newArrayList(keys));
    }

    public Long del(@NotNull List<String> keys) {
        if (CollUtil.isEmpty(keys)) {
            return 0L;
        } else {
            List<List<String>> partitionKeys = Lists.partition(keys, 1000);
            long count = 0L;

            List list;
            for (Iterator var5 = partitionKeys.iterator(); var5.hasNext(); count += this.redisTemplate.delete(list)) {
                list = (List) var5.next();
            }

            return count;
        }
    }

    public Long del(@NotNull Collection<CacheKey> keys) {
        return this.del(Lists.newArrayList(keys));
    }

    public Long unlink(@NotNull CacheKey... keys) {
        return this.unlinkStrs(Arrays.stream(keys).map(CacheKey::getKey).collect(Collectors.toList()));
    }

    public Long unlink(@NotNull String... keys) {
        return this.unlinkStrs(Arrays.stream(keys).collect(Collectors.toList()));
    }

    public Long unlinkCacheKeys(@NotNull Collection<CacheKey> keys) {
        return this.unlinkStrs(keys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
    }

    public Long unlinkStrs(@NotNull List<String> keys) {
        if (CollUtil.isEmpty(keys)) {
            return 0L;
        } else {
            List<List<String>> partitionKeys = Lists.partition(keys, 1000);
            long count = 0L;

            List list;
            for (Iterator var5 = partitionKeys.iterator(); var5.hasNext(); count += this.redisTemplate.unlink(list)) {
                list = (List) var5.next();
            }

            return count;
        }
    }

    public void scanUnlink(@NotNull String pattern) {
        log.info("pattern={}", pattern);
        if (!StrUtil.isEmpty(pattern) && !"*".equals(pattern.trim())) {
            List<String> keys = this.scan(pattern);
            log.info("keys={}", keys.size());
            if (!CollUtil.isEmpty(keys)) {
                this.unlinkStrs(keys);
            }
        } else {

            throw new ServiceException(-1, "必须指定匹配符");
        }
    }

    public List<String> scan(@NotNull String pattern) {
        List<String> keyList = new ArrayList<>();
        this.scan(pattern, (item) -> {
            Object key = this.redisTemplate.getKeySerializer().deserialize(item);
            if (ObjectUtil.isNotEmpty(key)) {
                keyList.add(String.valueOf(key));
            }

        });
        return keyList;
    }

    private void scan(String pattern, Consumer<byte[]> consumer) {
        this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(1000L).match(pattern).build());
            Throwable var4 = null;

            Object var5;
            try {
                cursor.forEachRemaining(consumer);
                var5 = null;
            } catch (Throwable var14) {
                var4 = var14;
                throw var14;
            } finally {
                if (var4 != null) {
                    try {
                        cursor.close();
                    } catch (Throwable var13) {
                        var4.addSuppressed(var13);
                    }
                } else {
                    cursor.close();
                }

            }

            return var5;
        });
    }

    public Set<String> keys(@NotNull String pattern) {
        return this.redisTemplate.keys(pattern);
    }

    public Boolean exists(@NotNull String key) {
        return this.redisTemplate.hasKey(key);
    }

    public String randomKey() {
        return this.redisTemplate.randomKey();
    }

    public void rename(@NotNull String oldKey, @NotNull String newKey) {
        this.redisTemplate.rename(oldKey, newKey);
    }

    public Boolean renameNx(@NotNull String oldKey, String newKey) {
        return this.redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    public Boolean move(@NotNull String key, int dbIndex) {
        return this.redisTemplate.move(key, dbIndex);
    }

    public Boolean expire(@NotNull String key, long seconds) {
        Assert.notEmpty(key, "key不能为空");
        return this.redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public Boolean expire(@NotNull String key, @NotNull Duration timeout) {
        return this.expire(key, timeout.getSeconds());
    }

    public Boolean expireAt(@NotNull String key, @NotNull Date date) {
        return this.redisTemplate.expireAt(key, date);
    }

    public Boolean expireAt(@NotNull String key, long unixTimestamp) {
        return this.expireAt(key, new Date(unixTimestamp));
    }

    public Boolean pExpire(@NotNull String key, long milliseconds) {
        return this.redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
    }

    public Boolean persist(@NotNull String key) {
        return this.redisTemplate.persist(key);
    }

    public String type(@NotNull String key) {
        DataType type = this.redisTemplate.type(key);
        return type == null ? DataType.NONE.code() : type.code();
    }

    public Long ttl(@NotNull String key) {
        return this.redisTemplate.getExpire(key);
    }

    public Long pTtl(@NotNull String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    public void set(@NotNull String key, Object value, boolean... cacheNullValues) {
        Assert.notNull(key, "key不能为空");
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        if (cacheNullVal || value != null) {
            this.valueOps.set(key, value == null ? this.newNullVal() : value);
        }
    }

    public void set(@NotNull CacheKey cacheKey, Object value, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        Assert.notNull(cacheKey, "cacheKey不能为空");
        String key = cacheKey.getKey();
        Duration expire = cacheKey.getExpire();
        if (expire == null) {
            this.set(key, value, cacheNullVal);
        } else {
            this.setEx(key, value, expire, cacheNullVal);
        }

    }

    public void setEx(@NotNull String key, Object value, Duration timeout, boolean... cacheNullValues) {
        Assert.notNull(key, "key不能为空");
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        if (cacheNullVal || value != null) {
            this.valueOps.set(key, value == null ? this.newNullVal() : value, timeout);
        }
    }

    public void setEx(@NotNull String key, Object value, long seconds, boolean... cacheNullValues) {
        this.setEx(key, value, Duration.ofSeconds(seconds), cacheNullValues);
    }

    @Nullable
    public Boolean setXx(@NotNull String key, String value, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        return this.valueOps.setIfPresent(key, cacheNullVal && value == null ? this.newNullVal() : value);
    }

    @Nullable
    public Boolean setXx(@NotNull String key, String value, long seconds, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        return this.valueOps.setIfPresent(key, cacheNullVal && value == null ? this.newNullVal() : value, seconds, TimeUnit.SECONDS);
    }

    @Nullable
    public Boolean setXx(@NotNull String key, String value, Duration timeout, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        return this.valueOps.setIfPresent(key, cacheNullVal && value == null ? this.newNullVal() : value, timeout);
    }

    @Nullable
    public Boolean setNx(@NotNull String key, String value, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        return this.valueOps.setIfAbsent(key, cacheNullVal && value == null ? this.newNullVal() : value);
    }

    @Nullable
    public <T> T get(@NotNull String key, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        T value = (T) this.valueOps.get(key);
        if (value == null && cacheNullVal) {
            this.set(key, this.newNullVal(), true);
        }

        return this.returnVal(value);
    }

    @Nullable
    public <T> T get(@NotNull String key, Function<String, T> loader, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        T value = (T) this.valueOps.get(key);
        if (value != null) {
            return this.returnVal(value);
        } else {
            synchronized (KEY_LOCKS.computeIfAbsent(key, (v) -> new Object())) {
                value = (T) this.valueOps.get(key);
                if (value != null) {
                    return this.returnVal(value);
                }

                try {
                    value = loader.apply(key);
                    this.set(key, value, cacheNullVal);
                } finally {
                    KEY_LOCKS.remove(key);
                }
            }

            return this.returnVal(value);
        }
    }

    public <T> T getSet(@NotNull String key, Object value) {
        Assert.notNull(key, "cacheKey不能为空");
        T val = (T) this.valueOps.getAndSet(key, value == null ? this.newNullVal() : value);
        return this.returnVal(val);
    }

    @Nullable
    public <T> T get(@NotNull CacheKey key, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        Assert.notNull(key, "cacheKey不能为空");
        Assert.notNull(key.getKey(), "key不能为空");
        T value = (T) this.valueOps.get(key.getKey());
        if (value == null && cacheNullVal) {
            this.set(key, this.newNullVal(), true);
        }

        return this.returnVal(value);
    }

    @Nullable
    public <T> T get(@NotNull CacheKey key, Function<CacheKey, T> loader, boolean... cacheNullValues) {
        Assert.notNull(key, "cacheKey不能为空");
        Assert.notNull(key.getKey(), "key不能为空");
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        T value = (T) this.valueOps.get(key.getKey());
        if (value != null) {
            return this.returnVal(value);
        } else {
            synchronized (KEY_LOCKS.computeIfAbsent(key.getKey(), (v) -> new Object())) {
                value = (T) this.valueOps.get(key.getKey());
                if (value != null) {
                    return this.returnVal(value);
                }

                try {
                    value = loader.apply(key);
                    this.set(key, value, cacheNullVal);
                } finally {
                    KEY_LOCKS.remove(key.getKey());
                }
            }

            return this.returnVal(value);
        }
    }

    @Nullable
    public Long strLen(@NotNull String key) {
        return this.valueOps.size(key);
    }

    @Nullable
    public Integer append(@NotNull String key, String value) {
        return this.valueOps.append(key, value);
    }

    public void setRange(@NotNull String key, String value, long offset) {
        this.valueOps.set(key, value, offset);
    }

    public String getRange(@NotNull String key, long start, long end) {
        return this.valueOps.get(key, start, end);
    }

    private Map<String, Object> mSetMap(@NotNull Map<String, Object> map, boolean cacheNullVal) {
        Map<String, Object> mSetMap = new HashMap<>(map.size());
        map.forEach((k, v) -> {
            if (v == null && cacheNullVal) {
                mSetMap.put(k, this.newNullVal());
            } else {
                mSetMap.put(k, v);
            }
        });
        return mSetMap;
    }

    public void mSet(@NotNull Map<String, Object> map, boolean cacheNullVal) {
        Map<String, Object> mSetMap = this.mSetMap(map, cacheNullVal);
        this.valueOps.multiSet(mSetMap);
    }

    public void mSet(@NotNull Map<String, Object> map) {
        this.mSet(map, this.defaultCacheNullVal);
    }

    public void mSetNx(@NotNull Map<String, Object> map, boolean cacheNullVal) {
        Map<String, Object> mSetMap = this.mSetMap(map, cacheNullVal);
        this.valueOps.multiSetIfAbsent(mSetMap);
    }

    public void mSetNx(@NotNull Map<String, Object> map) {
        this.mSetNx(map, this.defaultCacheNullVal);
    }

    public <T> List<T> mGet(@NotNull String... keys) {
        return this.mGet(Arrays.asList(keys));
    }

    public <T> List<T> mGet(@NotNull CacheKey... keys) {
        return this.mGetByCacheKey(Arrays.asList(keys));
    }

    public <T> List<T> mGet(@NotNull Collection<String> keys) {
        List<T> list = (List<T>) this.valueOps.multiGet(keys);
        return list == null ? Collections.emptyList() : list.stream().map(this::returnVal).collect(Collectors.toList());
    }

    public <T> List<T> mGetByCacheKey(@NotNull Collection<CacheKey> cacheKeys) {
        List<String> keys = cacheKeys.stream().map(CacheKey::getKey).collect(Collectors.toList());
        List<T> list = (List<T>) this.valueOps.multiGet(keys);
        return list == null ? Collections.emptyList() : list.stream().map(this::returnVal).collect(Collectors.toList());
    }

    public Long incr(@NotNull CacheKey key) {
        Long increment = this.stringRedisTemplate.opsForValue().increment(key.getKey());
        this.setExpire(key);
        return increment;
    }

    public Long incrBy(@NotNull CacheKey key, long increment) {
        Long incrBy = this.stringRedisTemplate.opsForValue().increment(key.getKey(), increment);
        this.setExpire(key);
        return incrBy;
    }

    public Double incrByFloat(@NotNull CacheKey key, double increment) {
        Double incrByFloat = this.stringRedisTemplate.opsForValue().increment(key.getKey(), increment);
        this.setExpire(key);
        return incrByFloat;
    }

    public Long getCounter(@NotNull CacheKey key, Long... defaultValue) {
        Object val = this.stringRedisTemplate.opsForValue().get(key.getKey());
        if (isNullVal(val)) {
            return defaultValue.length > 0 ? defaultValue[0] : null;
        } else {
            return Convert.toLong(val);
        }
    }

    public Long getCounter(@NotNull CacheKey key, Function<CacheKey, Long> loader) {
        Object val = this.stringRedisTemplate.opsForValue().get(key.getKey());
        return isNullVal(val) ? loader.apply(key) : Convert.toLong(val);
    }

    public Long decr(@NotNull CacheKey key) {
        Long decr = this.stringRedisTemplate.opsForValue().decrement(key.getKey());
        this.setExpire(key);
        return decr;
    }

    public Long decrBy(@NotNull CacheKey key, long decrement) {
        Long decr = this.stringRedisTemplate.opsForValue().decrement(key.getKey(), decrement);
        this.setExpire(key);
        return decr;
    }

    public void hSet(@NotNull String key, @NotNull Object field, Object value, boolean... cacheNullValues) {
        Assert.notEmpty(key, "key不能为空");
        Assert.notNull(field, "field不能为空");
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        if (cacheNullVal || value != null) {
            this.hashOps.put(key, field, value == null ? this.newNullVal() : value);
        }
    }

    public void hSet(@NotNull CacheHashKey key, Object value, boolean... cacheNullValues) {
        Assert.notNull(key, "CacheHashKey不能为空");
        this.hSet(key.getKey(), key.getField(), value, cacheNullValues);
        this.setExpire(key);
    }

    @Nullable
    public <T> T hGet(@NotNull String key, @NotNull Object field, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        T value = (T) this.hashOps.get(key, field);
        if (value == null && cacheNullVal) {
            this.hSet(key, field, this.newNullVal(), true);
        }

        return this.returnVal(value);
    }

    @Nullable
    public <T> T hGet(@NotNull String key, @NotNull Object field, BiFunction<String, Object, T> loader, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        T value = (T) this.hashOps.get(key, field);
        if (value != null) {
            return this.returnVal(value);
        } else {
            String lockKey = key + "@" + field;
            synchronized (KEY_LOCKS.computeIfAbsent(lockKey, (v) -> new Object())) {
                value = (T) this.hashOps.get(key, field);
                if (value != null) {
                    return this.returnVal(value);
                }

                try {
                    value = loader.apply(key, field);
                    this.hSet(key, field, value, cacheNullVal);
                } finally {
                    KEY_LOCKS.remove(lockKey);
                }
            }

            return this.returnVal(value);
        }
    }

    @Nullable
    public <T> T hGet(@NotNull CacheHashKey key, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        Assert.notNull(key, "CacheHashKey不能为空");
        Assert.notEmpty(key.getKey(), "key不能为空");
        Assert.notNull(key.getField(), "field不能为空");
        T value = (T) this.hashOps.get(key.getKey(), key.getField());
        if (value == null && cacheNullVal) {
            this.hSet(key, this.newNullVal(), true);
        }

        return this.returnVal(value);
    }

    @Nullable
    public <T> T hGet(@NotNull CacheHashKey key, Function<CacheHashKey, T> loader, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        T value = (T) this.hashOps.get(key.getKey(), key.getField());
        if (value != null) {
            return this.returnVal(value);
        } else {
            String lockKey = key.getKey() + "@" + key.getField();
            synchronized (KEY_LOCKS.computeIfAbsent(lockKey, (v) -> new Object())) {
                value = (T) this.hashOps.get(key.getKey(), key.getField());
                if (value != null) {
                    return this.returnVal(value);
                }

                try {
                    value = loader.apply(key);
                    this.hSet(key, value, cacheNullVal);
                } finally {
                    KEY_LOCKS.remove(key.getKey());
                }
            }

            return this.returnVal(value);
        }
    }

    public Boolean hExists(@NotNull String key, @NotNull Object field) {
        return this.hashOps.hasKey(key, field);
    }

    public Boolean hExists(@NotNull CacheHashKey cacheHashKey) {
        return this.hashOps.hasKey(cacheHashKey.getKey(), cacheHashKey.getField());
    }

    public Long hDel(@NotNull String key, Object... fields) {
        return this.hashOps.delete(key, fields);
    }

    public Long hDel(@NotNull CacheHashKey key) {
        return this.hashOps.delete(key.getKey(), key.getField());
    }

    public Long hLen(@NotNull String key) {
        return this.hashOps.size(key);
    }

    public Long hStrLen(@NotNull String key, @NotNull Object field) {
        return this.hashOps.lengthOfValue(key, field);
    }

    public Long hIncrBy(@NotNull CacheHashKey key, long increment) {
        Long hIncrBy = this.stringRedisTemplate.opsForHash().increment(key.getKey(), key.getField(), increment);
        if (key.getExpire() != null) {
            this.stringRedisTemplate.expire(key.getKey(), key.getExpire());
        }

        return hIncrBy;
    }

    public Double hIncrByFloat(@NotNull CacheHashKey key, double increment) {
        Double hIncrBy = this.stringRedisTemplate.opsForHash().increment(key.getKey(), key.getField(), increment);
        if (key.getExpire() != null) {
            this.stringRedisTemplate.expire(key.getKey(), key.getExpire());
        }

        return hIncrBy;
    }

    public <K, V> void hmSet(@NotNull String key, @NotNull Map<K, V> hash, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        Map<Object, Object> newMap = new HashMap<>(hash.size());
        hash.forEach((k, v) -> {
            if (v == null && cacheNullVal) {
                newMap.put(k, this.newNullVal());
            } else {
                newMap.put(k, v);
            }

        });
        this.hashOps.putAll(key, newMap);
    }

    public List<Object> hmGet(@NotNull String key, @NotNull Object... fields) {
        return this.hmGet(key, Arrays.asList(fields));
    }

    public List hmGet(@NotNull String key, @NotNull Collection<Object> fields) {
        List<Object> list = this.hashOps.multiGet(key, fields);
        return list.stream().map(this::returnVal).collect(Collectors.toList());
    }

    public <HK> Set<HK> hKeys(@NotNull String key) {
        return (Set<HK>) this.hashOps.keys(key);
    }

    public <HV> List<HV> hVals(@NotNull String key) {
        return (List<HV>) this.hashOps.values(key);
    }

    public <K, V> Map<K, V> hGetAll(@NotNull String key) {
        Map<K, V> map = (Map<K, V>) this.hashOps.entries(key);
        return this.returnMapVal(map);
    }

    public <K, V> Map<K, V> hGetAll(@NotNull CacheHashKey key) {
        Map<K, V> map = (Map<K, V>) this.hashOps.entries(key.getKey());
        return this.returnMapVal(map);
    }

    private <K, V> Map<K, V> returnMapVal(Map<K, V> map) {
        Map<K, V> newMap = new HashMap<>(map.size());
        if (MapUtil.isNotEmpty(map)) {
            map.forEach((k, v) -> {
                if (!isNullVal(v)) {
                    newMap.put(k, v);
                }

            });
        }

        return newMap;
    }

    @Nullable
    public <K, V> Map<K, V> hGetAll(@NotNull CacheHashKey key, Function<CacheHashKey, Map<K, V>> loader, boolean... cacheNullValues) {
        boolean cacheNullVal = cacheNullValues.length > 0 ? cacheNullValues[0] : this.defaultCacheNullVal;
        Map<K, V> map = (Map<K, V>) this.hashOps.entries(key.getKey());
        if (MapUtil.isNotEmpty(map)) {
            return this.returnMapVal(map);
        } else {
            String lockKey = key.getKey();
            synchronized (KEY_LOCKS.computeIfAbsent(lockKey, (v) -> new Object())) {
                map = (Map<K, V>) this.hashOps.entries(key.getKey());
                if (MapUtil.isNotEmpty(map)) {
                    return this.returnMapVal(map);
                }

                try {
                    map = loader.apply(key);
                    this.hmSet(key.getKey(), map, cacheNullVal);
                } finally {
                    KEY_LOCKS.remove(key.getKey());
                }
            }

            return this.returnMapVal(map);
        }
    }

    @Nullable
    public Long lPush(@NotNull String key, Object... values) {
        return this.listOps.leftPushAll(key, values);
    }

    @Nullable
    public Long lPush(@NotNull String key, Collection<Object> values) {
        return this.listOps.leftPushAll(key, values);
    }

    @Nullable
    public Long lPushX(@NotNull String key, Object values) {
        return this.listOps.leftPushIfPresent(key, values);
    }

    @Nullable
    public Long rPush(@NotNull String key, Object... values) {
        return this.listOps.rightPushAll(key, values);
    }

    @Nullable
    public Long rPush(@NotNull String key, Collection<Object> values) {
        return this.listOps.rightPushAll(key, values);
    }

    @Nullable
    public Long rPushX(@NotNull String key, Object value) {
        return this.listOps.rightPushIfPresent(key, value);
    }

    @Nullable
    public <T> T lPop(@NotNull String key) {
        return (T) this.listOps.leftPop(key);
    }

    public <T> T rPop(@NotNull String key) {
        return (T) this.listOps.rightPop(key);
    }

    public <T> T rPoplPush(String sourceKey, String destinationKey) {
        return (T) this.listOps.rightPopAndLeftPush(sourceKey, destinationKey);
    }

    @Nullable
    public Long lRem(@NotNull String key, long count, Object value) {
        return this.listOps.remove(key, count, value);
    }

    @Nullable
    public Long lLen(@NotNull String key) {
        return this.listOps.size(key);
    }

    @Nullable
    public <T> T lIndex(@NotNull String key, long index) {
        return (T) this.listOps.index(key, index);
    }

    @Nullable
    public Long lInsert(@NotNull String key, Object pivot, Object value) {
        return this.listOps.leftPush(key, pivot, value);
    }

    @Nullable
    public Long rInsert(@NotNull String key, Object pivot, Object value) {
        return this.listOps.rightPush(key, pivot, value);
    }

    public void lSet(@NotNull String key, long index, Object value) {
        this.listOps.set(key, index, value);
    }

    @Nullable
    public List<Object> lRange(@NotNull String key, long start, long end) {
        return this.listOps.range(key, start, end);
    }

    public void lTrim(@NotNull String key, long start, long end) {
        this.listOps.trim(key, start, end);
    }

    public <V> Long sAdd(@NotNull CacheKey key, V... members) {
        Long count = this.setOps.add(key.getKey(), members);
        this.setExpire(key);
        return count;
    }

    public <V> Long sAdd(@NotNull CacheKey key, Collection<V> members) {
        Long count = this.setOps.add(key.getKey(), members.toArray());
        this.setExpire(key);
        return count;
    }

    public Boolean sIsMember(@NotNull CacheKey key, Object member) {
        return this.setOps.isMember(key.getKey(), member);
    }

    @Nullable
    public <T> T sPop(@NotNull CacheKey key) {
        return (T) this.setOps.pop(key.getKey());
    }

    @Nullable
    public <T> T sRandMember(@NotNull CacheKey key) {
        return (T) this.setOps.randomMember(key.getKey());
    }

    @Nullable
    public <V> Set<V> sRandMember(@NotNull CacheKey key, long count) {
        return (Set<V>) this.setOps.distinctRandomMembers(key.getKey(), count);
    }

    @Nullable
    public <V> List<V> sRandMembers(@NotNull CacheKey key, long count) {
        return (List<V>) this.setOps.randomMembers(key.getKey(), count);
    }

    @Nullable
    public Long sRem(@NotNull CacheKey key, Object... members) {
        return this.setOps.remove(key.getKey(), members);
    }

    public <V> Boolean sMove(@NotNull CacheKey sourceKey, CacheKey destinationKey, V value) {
        return this.setOps.move(sourceKey.getKey(), value, destinationKey.getKey());
    }

    public Long sCard(@NotNull CacheKey key) {
        return this.setOps.size(key.getKey());
    }

    @Nullable
    public <V> Set<V> sMembers(@NotNull CacheKey key) {
        return (Set<V>) this.setOps.members(key.getKey());
    }

    @Nullable
    public <V> Set<V> sInter(@NotNull CacheKey key, @NotNull CacheKey otherKey) {
        return (Set<V>) this.setOps.intersect(key.getKey(), otherKey.getKey());
    }

    @Nullable
    public Set<Object> sInter(@NotNull CacheKey key, Collection<CacheKey> otherKeys) {
        return this.setOps.intersect(key.getKey(), otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
    }

    @Nullable
    public <V> Set<V> sInter(Collection<CacheKey> otherKeys) {
        return (Set<V>) this.setOps.intersect(otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
    }

    @Nullable
    public Long sInterStore(@NotNull CacheKey key, @NotNull CacheKey otherKey, @NotNull CacheKey destKey) {
        return this.setOps.intersectAndStore(key.getKey(), otherKey.getKey(), destKey.getKey());
    }

    @Nullable
    public Long sInterStore(@NotNull CacheKey key, @NotNull Collection<CacheKey> otherKeys, @NotNull CacheKey destKey) {
        return this.setOps.intersectAndStore(key.getKey(), otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()), destKey.getKey());
    }

    @Nullable
    public Long sInterStore(Collection<CacheKey> otherKeys, @NotNull CacheKey destKey) {
        return this.setOps.intersectAndStore(otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()), destKey.getKey());
    }

    @Nullable
    public <V> Set<V> sUnion(@NotNull CacheKey key, @NotNull CacheKey otherKey) {
        return (Set<V>) this.setOps.union(key.getKey(), otherKey.getKey());
    }

    @Nullable
    public <V> Set<V> sUnion(@NotNull CacheKey key, Collection<CacheKey> otherKeys) {
        return this.setOps.union(key.getKey(), (Collection) otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
    }

    @Nullable
    public <V> Set<V> sUnion(Collection<CacheKey> otherKeys) {
        return (Set<V>) this.setOps.union(otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
    }

    public Long sUnionStore(@NotNull CacheKey key, @NotNull CacheKey otherKey, @NotNull CacheKey distKey) {
        return this.setOps.unionAndStore(key.getKey(), otherKey.getKey(), distKey.getKey());
    }

    public Long sUnionStore(Collection<CacheKey> otherKeys, @NotNull CacheKey distKey) {
        return this.setOps.unionAndStore(otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()), distKey.getKey());
    }

    @Nullable
    public <V> Set<V> sDiff(@NotNull CacheKey key, @NotNull CacheKey otherKey) {
        return (Set<V>) this.setOps.difference(key.getKey(), otherKey.getKey());
    }

    public <V> Set<V> sDiff(Collection<CacheKey> otherKeys) {
        return this.setOps.difference((Collection) otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
    }

    public Long sDiffStore(@NotNull CacheKey key, @NotNull CacheKey otherKey, @NotNull CacheKey distKey) {
        return this.setOps.differenceAndStore(key.getKey(), otherKey.getKey(), distKey.getKey());
    }

    public Long sDiffStore(Collection<CacheKey> otherKeys, @NotNull CacheKey distKey) {
        return this.setOps.differenceAndStore(otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()), distKey.getKey());
    }

    public Boolean zAdd(@NotNull String key, Object member, double score) {
        return this.zSetOps.add(key, member, score);
    }

    public Long zAdd(@NotNull String key, Map<Object, Double> scoreMembers) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet();
        scoreMembers.forEach((score, member) -> {
            tuples.add(new DefaultTypedTuple(score, member));
        });
        return this.zSetOps.add(key, tuples);
    }

    public Double zScore(@NotNull String key, Object member) {
        return this.zSetOps.score(key, member);
    }

    public Double zIncrBy(@NotNull String key, Object member, double score) {
        return this.zSetOps.incrementScore(key, member, score);
    }

    public Long zCard(@NotNull String key) {
        return this.zSetOps.zCard(key);
    }

    public Long zCount(@NotNull String key, double min, double max) {
        return this.zSetOps.count(key, min, max);
    }

    @Nullable
    public Set<Object> zRange(@NotNull String key, long start, long end) {
        return this.zSetOps.range(key, start, end);
    }

    @Nullable
    public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(@NotNull String key, long start, long end) {
        return this.zSetOps.rangeWithScores(key, start, end);
    }

    @Nullable
    public Set<Object> zRevrange(@NotNull String key, long start, long end) {
        return this.zSetOps.reverseRange(key, start, end);
    }

    @Nullable
    public Set<ZSetOperations.TypedTuple<Object>> zRevrangeWithScores(@NotNull String key, long start, long end) {
        return this.zSetOps.reverseRangeWithScores(key, start, end);
    }

    public Set<Object> zRangeByScore(@NotNull String key, double min, double max) {
        return this.zSetOps.rangeByScore(key, min, max);
    }

    public Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(@NotNull String key, double min, double max) {
        return this.zSetOps.rangeByScoreWithScores(key, min, max);
    }

    public Set<Object> zReverseRange(@NotNull String key, double min, double max) {
        return this.zSetOps.reverseRangeByScore(key, min, max);
    }

    public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeByScoreWithScores(@NotNull String key, double min, double max) {
        return this.zSetOps.reverseRangeByScoreWithScores(key, min, max);
    }

    @Nullable
    public Long zRank(@NotNull String key, Object member) {
        return this.zSetOps.rank(key, member);
    }

    public Long zRevrank(@NotNull String key, Object member) {
        return this.zSetOps.reverseRank(key, member);
    }

    public Long zRem(@NotNull String key, Object... members) {
        return this.zSetOps.remove(key, members);
    }

    public Long zRem(@NotNull String key, long start, long end) {
        return this.zSetOps.removeRange(key, start, end);
    }

    public Long zRemRangeByScore(@NotNull String key, double min, double max) {
        return this.zSetOps.removeRangeByScore(key, min, max);
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return this.redisTemplate;
    }

    public ValueOperations<String, Object> getValueOps() {
        return this.valueOps;
    }

    public HashOperations<String, Object, Object> getHashOps() {
        return this.hashOps;
    }

    public ListOperations<String, Object> getListOps() {
        return this.listOps;
    }

    public SetOperations<String, Object> getSetOps() {
        return this.setOps;
    }

    public ZSetOperations<String, Object> getZSetOps() {
        return this.zSetOps;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return this.stringRedisTemplate;
    }

    public boolean isDefaultCacheNullVal() {
        return this.defaultCacheNullVal;
    }

}
