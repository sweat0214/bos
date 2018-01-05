package cn.itcast.bos.redis.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by 宝宝心里苦丶 on 2018/1/4.
 */
public class JedisTest {
    @Test
    public void testRedis(){
        //连接localhost 默认端口 6379
        Jedis jedis = new Jedis("localhost");
        jedis.setex("company",30,"No sweet without sweat");
        System.out.println(jedis.get("company"));
    }
}
