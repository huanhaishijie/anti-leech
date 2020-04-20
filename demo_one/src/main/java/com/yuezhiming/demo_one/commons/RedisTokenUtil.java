package com.yuezhiming.demo_one.commons;/**
 * Created by ASUSon 2020/4/16 21:51
 */

import com.yuezhiming.demo_one.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.function.Predicate;

/**
 * @program: demo_one

 * @description:

 * @author: yuezm

 * @create: 2020-04-16 21:51
 **/
@Component
public class RedisTokenUtil {

    @Autowired
    RedisService redisService;

    public  Predicate<String> isExistToken = key -> {
        if(StringUtils.isEmpty(key)) return false;
        Object token = redisService.getStrValue(key);
        if(token == null) return false;
        redisService.remove(key);
        return true;
    };

    public String getToken(){
        String key = "token_" +
                "" + UUID.randomUUID().toString().replace("-", "").substring(2, 12);
        redisService.setValue(key, key, 3);
        return key;
    }

}
