package com.nines.sys.config.shiro.cache;

import com.nines.sys.entity.SysUser;
import com.nines.sys.util.Constant;
import com.nines.sys.util.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.*;

/**
 * 重写shiro的cache保存读取
 * @author TYJ
 * @date 2020/10/30 9:16
 */
public class MyCache implements Cache {

    /**
     * shiro 权限缓存前缀
     */
    private final static String FREFIX_SHIRO_CACHE = Constant.FREFIX_SHIRO_CACHE;

    /**
     * 权限缓存 有效时间（分钟）
     */
    private static final long SHIRO_CACHE_EXPIRE_TIME = 60 * 1000 * Constant.SHIRO_CACHE_EXPIRE_TIME;

    private String getKey(Object key){
        String username = ((SysUser) ((SimplePrincipalCollection) key).getPrimaryPrincipal()).getUserName();
        return FREFIX_SHIRO_CACHE + username;
    }

    /**
     * 获取缓存
     */
    @Override
    public Object get(Object key) throws CacheException {
        if (RedisUtil.hasKey(this.getKey(key))){
            return RedisUtil.get(this.getKey(key));
        }
        return null;
    }

    /**
     * 保存缓存
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        return RedisUtil.set(this.getKey(key), value, SHIRO_CACHE_EXPIRE_TIME);
    }

    /**
     * 移除缓存
     */
    @Override
    public Object remove(Object key) throws CacheException {
        if (RedisUtil.hasKey(this.getKey(key))){
            RedisUtil.del(this.getKey(key));
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {
        RedisUtil.del(FREFIX_SHIRO_CACHE + "*");
    }

    @Override
    public int size() {
        List list = (List) RedisUtil.get(FREFIX_SHIRO_CACHE + "*");
        return list.size();
    }

    @Override
    public Set keys() {
        List list = (List) RedisUtil.get(FREFIX_SHIRO_CACHE + "*");
        Set<Object> set = new HashSet<>();
        set.addAll(list);
        return set;
    }

    @Override
    public Collection values() {
        Set keys = this.keys();
        List<Object> values = new ArrayList<>();
        for (Object key : keys) {
            values.add(RedisUtil.get((String) key));
        }
        return values;
    }
}
