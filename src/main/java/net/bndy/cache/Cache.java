package net.bndy.cache;

public interface Cache {

    void set(Object data);
    void set(Object data, long exp);
    <T> T get(String key);
    int del(String... keys);
    int delAll();
}
