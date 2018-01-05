package cn.itcast.bos.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * Created by 宝宝心里苦丶 on 2018/1/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedis(){
        //保存key value
        //设置30秒失效
        redisTemplate.opsForValue().set("city","北京",30, TimeUnit.SECONDS);

        System.out.println(redisTemplate.opsForValue().get("city"));
    }
}
