package net.bndy.cache;

import net.bndy.lib.cache.Cache;

public class JavaCache extends AbstractCache {

	@Override
	public void set(Object data) {
		Cache.put(this.getKey(data), data);
	}

	@Override
	public void set(Object data, long exp) {
		Cache.put(this.getKey(data), data, exp);
	}

	@Override
	public Object get(String key) {
		return Cache.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, Class<T> clazz) {
		return (T)Cache.get(key);
	}

	@Override
	public int del(String... keys) {
		int result = 0;
		for(String key: keys) {
			Cache.remove(key);
			result++;
		}
		return result;
	}

	@Override
	public int delAll() {
		int result = Cache.getSize();
		Cache.clear();
		return result;
	}

}
