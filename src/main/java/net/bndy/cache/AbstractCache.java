package net.bndy.cache;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.bndy.lib.JsonHelper;
import net.bndy.lib.StringHelper;

public abstract class AbstractCache implements Cache {

    protected String getKey(Object data) {
    	// TODO: get key from data
        return data.getClass().getName();
    }

    protected String getJson(Object data) {
        try {
			return JsonHelper.toString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
    }

    protected <T> T deserialize(String json, Class<T> clazz) {
    	if (StringHelper.isNullOrWhiteSpace(json)) {
    		return null;
    	}

        try {
			return StringHelper.toJson(json, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
}
