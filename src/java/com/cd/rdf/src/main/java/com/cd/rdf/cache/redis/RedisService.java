package com.cd.rdf.cache.redis;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * <pre>
 * redis访问服务类
 * </pre>
 * 
 * @author awens
 * @version 1.00.01
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */

public class RedisService {

    private final static Logger log              = LoggerFactory.getLogger(RedisService.class);

    public final static String  REDIS_SERVICE_ID = "sfjr_serviceproxy_redis_service";

    private JedisSentinelPool   jedisSentinelPool;

    //aliyun parameters
    private static String       url              = "83cc322f531640d3.m.cnsza.kvstore.aliyuncs.com";

    private static int          port             = 6379;

    private String              password         = "83cc322f531640d3:YcsRedis2016";

    private int                 timeout          = 30 * 60;                                        // 30 minutes

    private final static int    maxIdle          = 500;                                            // 最大空闲连接数, 应用自己评估，不要超过AliCloudDB for Redis每个实例最大的连接数

    private final static int    maxTotal         = 30000;                                          // 最大连接数, 应用自己评估，不要超过AliCloudDB for Redis每个实例最大的连接数

    private JedisPool           pool             = null;

    private boolean             isProduction     = true;                                           // false = 测试环境，true = 生产环境


    public RedisService(String production, String redisUrl, int timeout, String authPassword) {
        log.info("初始化Redis:" + redisUrl + ", isProduction:" + production + ",timeout:" + timeout + ",authPassword:" + authPassword);
        this.isProduction = Boolean.parseBoolean(production);
        this.timeout = timeout;
        this.password = authPassword;
        if (isProduction) {
            String[] tmp = redisUrl.split(":");
            url = tmp[0];
            port = Integer.parseInt(tmp[1]);
            log.info("url:" + url);
            log.info("port:" + port);
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(maxIdle);
            config.setMaxTotal(maxTotal);
            config.setTestOnBorrow(false);
            config.setTestOnReturn(false);
            pool = new JedisPool(config, url, port, timeout, password);
        } else {
            Set<String> sentinels = new HashSet<String>();
            String[] ips = redisUrl.split(";");
            for (String ip : ips) {
                sentinels.add(ip);
            }
            JedisPoolConfig jedisConfig = new JedisPoolConfig();
            jedisConfig.setMaxIdle(maxIdle);
            jedisConfig.setMaxTotal(maxTotal);
            jedisConfig.setTestOnBorrow(true);
            jedisSentinelPool = new JedisSentinelPool("mymaster", sentinels, jedisConfig, password);
        }
    }

    public void set(String key, String value, int expiredSeconds) {
        Jedis jedis = getJedis();
        jedis.setex(key, expiredSeconds, value);
        returnJedisResource(jedis);
    }

    public void expire(String key, int expiredSeconds) {
        Jedis jedis = getJedis();
        jedis.expire(key, expiredSeconds);
        returnJedisResource(jedis);
    }

    public String get(String key) {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        returnJedisResource(jedis);
        return value;
    }

    public String get(String key, int expiredSeconds) {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        if (StringUtils.isNotBlank(value)) {
            jedis.expire(key, expiredSeconds);
        }
        returnJedisResource(jedis);
        return value;
    }

    public void del(String key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        } finally {
            if (jedis != null)
                returnJedisResource(jedis);
        }
    }

    public void setObject(String key, RedisObject object) {
        Jedis jedis = getJedis();
        try {
            byte[] value = SerializeUtil.serialize(object);
            jedis.setex(key.getBytes(), timeout, value);
        } finally {
            if (jedis != null)
                returnJedisResource(jedis);
        }
        //log.info("setObject(" + key + ") - " + object + ", timeout:"+maxTimeoutSeconds+" seconds");
    }

    public void setObject(String key, RedisObject object, int seconds) {
        Jedis jedis = getJedis();
        try {
            byte[] value = SerializeUtil.serialize(object);
            jedis.setex(key.getBytes(), seconds, value);
        } finally {
            if (jedis != null)
                returnJedisResource(jedis);
        }
        //log.info("setObject(" + key + ") - " + object + ", timeout:"+maxTimeoutSeconds+" seconds");
    }

    public RedisObject getObject(String key) {
        Jedis jedis = getJedis();
        byte[] vo = null;
        try {
            vo = jedis.get(key.getBytes());
            jedis.expire(key.getBytes(), timeout);
        } catch (ClassCastException ex) {
            log.info(">>>无法读取到值，值失效:" + vo);
            vo = null;
        } finally {
            if (jedis != null)
                returnJedisResource(jedis);
        }
        if (vo == null || vo.length == 0) {
            return null;
        }
        RedisObject redisObject = (RedisObject) SerializeUtil.unserialize(vo);
        //log.info("getObject(" + key + ") - " + redisObject + ", timeout:"+maxTimeoutSeconds+" seconds");
        return redisObject;
    }

    /**
     * 从连接池中获取jedis连接
     * 
     * @return
     */
    private Jedis getJedis() {
        if (isProduction) {
            return pool.getResource();
        } else {
            return this.jedisSentinelPool.getResource();
        }
    }

    /**
     * 把jedis连接返回连接池
     * 
     * @param jedis
     */
    private void returnJedisResource(Jedis jedis) {
        if (isProduction) {
            this.pool.returnResource(jedis);
        } else {
            this.jedisSentinelPool.returnResource(jedis);
        }
    }
}