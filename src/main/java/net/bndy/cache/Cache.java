package net.bndy.cache;

public interface Cache {

    void set(Object data);
    void set(Object data, long exp);
    Object get(String key);
    <T> T get(String key, Class<T> clazz);
    int del(String... keys);
    int delAll();
}
