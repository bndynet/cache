package net.bndy.cache;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;

public class MemcachedCache extends AbstractCache {

    private MemcachedClient memcachedClient;

    public MemcachedCache (String servers) {
        try {
            this.memcachedClient = new MemcachedClient(AddrUtil.getAddresses(servers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void set(Object data) {
        this.memcachedClient.set(this.getKey(data), 0, data);
    }

    @Override
    public void set(Object data, long exp) {
        this.memcachedClient.set(this.getKey(data), (int) exp, data);
    }
    
    @Override
    public Object get(String key) {
        return this.memcachedClient.get(key);
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T get(String key, Class<T> clazz) {
    	key = clazz.getCanonicalName() + "#" + key;
        Object obj = this.get(key);
        if (obj != null) {
            return (T)obj;
        }
        return null;
    }

    @Override
    public int del(String... keys) {
        int result = 0;
        for (String key : keys) {
            this.memcachedClient.delete(key);
            result++;
        }
        return result;
    }

    @Override
    public int delAll() {
        this.memcachedClient.flush();
        return -1;
    }
}
