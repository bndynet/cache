package net.bndy.cache;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.bndy.lib.AnnotationHelper;
import net.bndy.lib.CollectionHelper;
import net.bndy.lib.JsonHelper;
import net.bndy.lib.ReflectionHelper;
import net.bndy.lib.StringHelper;

public abstract class AbstractCache implements Cache {

    protected String getKey(Object data) {
    	Field fieldKey = CollectionHelper.first(ReflectionHelper.getAllFields(data.getClass()),
    			field -> AnnotationHelper.getFieldAnnotation(CacheKey.class, data.getClass(), field.getName()) != null);
    	if (fieldKey == null) {
    		
    	}
    	
        try {
			return data.getClass().getCanonicalName() + "#" + fieldKey.get(data);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
        
        return null;
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
