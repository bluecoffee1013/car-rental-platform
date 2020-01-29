package com.yepit.mapp.framework.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Map;

/**
 *
 * @author qianlong
 * @date 2017/8/20
 */
public class JsonUtils {

    /**
     * 将对象转换成json字符串
     * @param object
     * @return
     */
    public static String Object2Json(Object object) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    /**
     * 将json转换成Map对象
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json) {
        Gson gson = new Gson();
        Map<String, Object> maps = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        return maps;
    }

    public static <T>T jsonToJavaBean(String json,Class clazz) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        return (T)mapper.readValue(json,clazz);
    }
}
