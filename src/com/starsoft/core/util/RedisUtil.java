package com.starsoft.core.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
public class RedisUtil {
	private static JedisPool pool;
	private static AtomicLong atomic = new AtomicLong(0);
	private static String redisHost="127.0.0.1";
	private static int redisPort=6379;
	static{
		JedisPoolConfig jedisconfig = new JedisPoolConfig();
		jedisconfig.setMaxActive(200);
		jedisconfig.setMaxIdle(50);
		jedisconfig.setMaxWait(1000000);
		jedisconfig.setTestOnBorrow(false);
		pool= new JedisPool(jedisconfig, redisHost,redisPort);
	}
	
	/**
	 * 添加字符串
	 * @param key
	 * @param value
	 */
	public static void setString(String key, String value){
		Jedis jedis = pool.getResource();
		try {
			jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
	}
	/**
	 * 添加字符串-带有效期
	 * @param key
	 * @param value
	 * @param expire: 秒
	 */
	public static void setString(String key, String value, int expire){
		Jedis jedis = pool.getResource();
		try {
			jedis.setex(key, expire, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
	}
	/**
	 * 获取字符串
	 * @param key
	 * @return
	 */
	public static String getString(String key){
		Jedis jedis = pool.getResource();
		String value = null;
		try {
			if(jedis.exists(key)){
				value = jedis.get(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 添加数据到指定list集合中
	 * @param key
	 * @param value
	 */
	public static void setToList(String key, String value){
		Jedis jedis = pool.getResource();
		try {
			jedis.lpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
	}
	/**
	 * 删除list集合中指定序列数据
	 * @param key
	 * @param value
	 */
	public static void delToList(String key, String value){
		Jedis jedis = pool.getResource();
		try {
			jedis.lrem(key, 0, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
	}
	
	/**
	 * 获取list集合中指定序列数据
	 * @param key
	 * @param index
	 * @return
	 */
	public static String getFromList(String key, long index){
		String value = null;
		Jedis jedis = pool.getResource();
		try {
			if(jedis.exists(key) && jedis.llen(key)> index && index >= 0){
				value = jedis.lindex(key, index);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 根据key获取list数据集合
	 * @param key
	 * @return
	 */
	public static List<String> getAllList(String key){
		List<String> value = null;
		Jedis jedis = pool.getResource();
		try {
			if(jedis.exists(key)){
				value = jedis.lrange(key, 0, -1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 删除
	 * @param key
	 */
	public static void delToString(String key){
		Jedis jedis = pool.getResource();
		try {
			jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
	}
	
	/**
	 * 获取自增序列
	 * @return
	 */
	public static long getSeqNum(){
		return atomic.addAndGet(1);
	}
	/**
	 * 添加数据到指定map集合中
	 * @param key
	 * @param value
	 */
	public static void setToMap(String key, String field, String value){
		Jedis jedis = pool.getResource();
		try {
			jedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
	}
	
	/**
	 * 删除map集合中指定key数据
	 * @param key
	 * @param value
	 */
	public static void delToMap(String key, String field){
		Jedis jedis = pool.getResource();
		try {
			jedis.hdel(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
	}
	/**
	 * 获取map集合中指定key数据
	 * @param key
	 * @param index
	 * @return
	 */
	public static String getFromMap(String key, String field){
		String value = null;
		Jedis jedis = pool.getResource();
		try {
			value = jedis.hget(key, field);
			if("nil".equals(value)){
				value = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 根据key获取map数据集合
	 * @param key
	 * @return
	 */
	public static Map<String, String> getAllMap(String key){
		Map<String, String> value = null;
		Jedis jedis = pool.getResource();
		try {
			value = jedis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 未读记录+1操作
	 * @param key
	 * @param field
	 */
	public static void increaseToMap(String key, String field){
		String value = getFromMap(key, field);
		
		int intV = (value == null) ? 0 : Integer.valueOf(value)+1;
		
		setToMap(key, field, String.valueOf(intV));
	}
	
	
	/**
	 * 获取List Size
	 * @param key
	 * @return
	 */
	public static long getListSize(String key){
		long value = 0;
		Jedis jedis = pool.getResource();
		try {
			value = jedis.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取Map Size
	 * @param key
	 * @return
	 */
	public static long getMapSize(String key){
		long value = 0;
		Jedis jedis = pool.getResource();
		try {
			value = jedis.hlen(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 添加数据到Set
	 * @param key
	 * @param value
	 */
	public static void setToSet(String key, String value){
		Jedis jedis = pool.getResource();
		try {
			jedis.sadd(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
	}
	
	/**
	 * 删除数据到Set
	 * @param key
	 * @param value
	 */
	public static void delToSet(String key, String value){
		Jedis jedis = pool.getResource();
		try {
			jedis.srem(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
	}
	
	/**
	 * 获取Set
	 * @param key
	 * @param value
	 */
	public static Set<String> getAllSet(String key){
		Set<String> value = null;
		Jedis jedis = pool.getResource();
		try {
			value = jedis.smembers(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取Set Size
	 * @param key
	 * @param value
	 */
	public static long getSetSize(String key){
		long value = 0;
		Jedis jedis = pool.getResource();
		try {
			value = jedis.scard(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			pool.returnResource(jedis);
		}
		return value;
	}
}
