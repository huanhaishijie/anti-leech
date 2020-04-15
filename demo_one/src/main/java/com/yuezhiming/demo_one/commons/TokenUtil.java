package com.yuezhiming.demo_one.commons;/**
 * Created by ASUSon 2020/4/13 21:29
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @program: demo_one

 * @description:

 * @author: yuezm

 * @create: 2020-04-13 21:29
 **/
public class TokenUtil {

    private static Map<String, String>  map = new ConcurrentHashMap<>();
    private static Predicate<String> isExist = key -> map.get(key) == null;

    public static synchronized String getToken(){
        String token = String.valueOf(System.currentTimeMillis());
        map.put(token, token);
        return token;
    }

    public static Boolean tokenExist(String token){
        if(isExist.test(token)) return false;
        map.remove(token);
        return true;
    }

}
