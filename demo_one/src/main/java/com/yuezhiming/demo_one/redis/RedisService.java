package com.yuezhiming.demo_one.redis;


import java.util.Map;

/**
 * Created by ASUSon 2020/4/16 20:01
 */


public interface RedisService {

   // 加入元素
    void setValue(String key, Map<String, Object> value);
    // 加入元素
    void setValue(String key, String value);
    // 加入元素
    void setValue(String key, Object value);
    //加入元素
    void setValue(String key, String value, Integer minutes);
    // 获取元素
    Object getMapValue(String key);
    // 获取元素
    Object getValue(String key);
    //删除元素
    void remove(String key);

    //获取元素
    String getStrValue(String key);


}
