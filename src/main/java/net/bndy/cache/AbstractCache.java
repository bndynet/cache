package net.bndy.cache;

public abstract class AbstractCache implements Cache {

    protected String getKey(Object data) {
        return data.getClass().getName();
    }

    protected String getJson(Object data) {
        // TODO:
        return "";
    }

    protected <T> T deserialize(String json) {
        // TODO
        Object o = null;
        return (T)o;
    }
}
