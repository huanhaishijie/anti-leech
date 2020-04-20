package com.yuezhiming.demo_one.service.impl;/**
 * Created by ASUSon 2020/4/16 20:15
 */

import com.yuezhiming.demo_one.redis.RedisService;
import com.yuezhiming.demo_one.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: demo_one

 * @description:

 * @author: yuezm

 * @create: 2020-04-16 20:15
 **/
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    RedisService redisService;

    @Override
    public String get() {
        Object zhangsan = redisService.getValue("zhangsan");
        System.out.println(zhangsan.toString());
        return zhangsan.toString();
    }
}
