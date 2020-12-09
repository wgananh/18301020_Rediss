package com.bjtu.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    private static JedisPool jedisPool = JedisInstance.getInstance();
    private static Jedis jedis= jedisPool.getResource();

    public Utils() throws Exception {
        init();
    }

    public static Utils createFactory() throws Exception{
        return new Utils();
    }

    public static void init() throws Exception {
        jedis.set("set_counternum","0");
        jedis.set("get_counternum","0");
    }

    //将对key键的修改记录添加到hash中 key:"set_count"
    public static void setcounter(String key, String field_key, Timestamp timestamp) throws Exception{
        String time =timestamp.toString();
        jedis.hset(key, field_key, time);
    }

    public static String getcouter_num(String key){
        switch (key){
            case "set":
                String setnum= jedis.get("set_counternum");
                return setnum;
            case "get":
                String getnum= jedis.get("get_counternum");
                return getnum;
            default:
                return "0";
        }
    }

    //返回set或get的操作时间集合
    public static Vector<String> getcounter_time(String key,Date start,Date end) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Vector<String> result=new Vector<>();
        switch (key){
            case "set":
                Map<String,String> timeMap=jedis.hgetAll("set_count");
                int num= Integer.parseInt(jedis.get("set_counternum"));
                if(num==0)
                    return result;
                for(int i=1;i<=num;i++){
                    String temp_key="set_"+i;
                    String values=timeMap.get(temp_key);
                    Date current = format.parse(values);
                    if(current.after(start)&&current.before(end)){
                        result.add(values);
                    }
                }
                return result;

            case "get":
                Map<String,String> timeMap2=jedis.hgetAll("get_count");
                int num2= Integer.parseInt(jedis.get("get_counternum"));
                if(num2==0)
                    return result;
                for(int i=1;i<=num2;i++){
                    String temp_key="get_"+i;
                    String values=timeMap2.get(temp_key);
                    Date current = format.parse(values);
                    if(current.after(start)&&current.before(end)){
                        result.add(values);
                    }
                }
                return result;

            default:
                return result;
        }
    }

    //删除任意类型的key
    public static void del(String key) throws Exception {
        jedis.del(key);
    }

    //获得所有的key
    public static Set<String> getallkey() throws Exception{
        return jedis.smembers("all_key");
    }


    //////////////////string类型///////////////////
    public static String set(String key, String value) throws Exception{
        jedis.incr("set_counternum");
        String num=jedis.get("set_counternum");
        String field="set_"+num;
        setcounter("set_count",field,new Timestamp(System.currentTimeMillis()));
        jedis.sadd("all_key",key);
        return jedis.set(key,value);
    }

    public static String get(String key) throws Exception {
        jedis.incr("get_counternum");
        String num=jedis.get("get_counternum");
        String field="get_"+num;
        setcounter("get_count",field,new Timestamp(System.currentTimeMillis()));
        return jedis.get(key);
    }

    //追加string
    public static Long append(String key, String value) throws Exception {
        return jedis.append(key,value);
    }

    public static long incr(String key) throws Exception{
        return jedis.incr(key);
    }
    public static long decr(String key) throws Exception{
        return jedis.decr(key);
    }

    /////////////////////set类型//////////////////////////
    public static void sadd(String key, Set<String> value) throws Exception {
        for(String str: value){
            jedis.incr("set_counternum");
            String num=jedis.get("set_counternum");
            String field="set_"+num;
            setcounter("set_count",field,new Timestamp(System.currentTimeMillis()));
            jedis.sadd(key, str);
        }
        jedis.sadd("all_key",key);
    }

    //删除set中指定的元素
    public static void srem(String key, Set<String> value) throws Exception {
        Iterator<String> it = value.iterator();
        while(it.hasNext()){
            String str = it.next();
            jedis.srem(key, str);
        }
    }

    //获取key对应的所有value值
    public static Set<String> smembers(String key) throws Exception {
        jedis.incr("get_counternum");
        String num=jedis.get("get_counternum");
        String field="get_"+num;
        setcounter("get_count",field,new Timestamp(System.currentTimeMillis()));
        jedis.sadd("all_key",key);
        return jedis.smembers(key);
    }

    //获取key对应的value总个数
    public static Long scard(String key) throws Exception {
        return jedis.scard(key);
    }

    //判断set是否存在
    public static boolean sismember(String key, String value) throws Exception {
        return jedis.sismember(key,value);
    }

    //随机获取数据
    public static String srandmember(String key) throws Exception {
        jedis.incr("get_counternum");
        String num=jedis.get("get_counternum");
        String field="get_"+num;
        setcounter("get_count",field,new Timestamp(System.currentTimeMillis()));
        return jedis.srandmember(key);
    }

    /////////////////////////list类型////////////////////////////////
    public static void lpush(String key, List<String> list) throws Exception {
        for(String s: list){
            jedis.incr("set_counternum");
            String num=jedis.get("set_counternum");
            String field="set_"+num;
            setcounter("set_count",field,new Timestamp(System.currentTimeMillis()));
            jedis.lpush(key,s);
        }
        jedis.sadd("all_key",key);
    }

    //移除 (count：=0，全部；>0正向；<0逆向)
    public static long lrem(String key,long count,String value) throws Exception{
        return jedis.lrem(key,count,value);
    }

    //修改
    public static String lset(String key,long index,String value) throws Exception{
        return jedis.lset(key,index,value);
    }
    
    //获取list
    public static List<String> lrange(String key, Integer start, Integer end) throws Exception {
        jedis.incr("get_counternum");
        String num=jedis.get("get_counternum");
        String field="get_"+num;
        setcounter("get_count",field,new Timestamp(System.currentTimeMillis()));
        return jedis.lrange(key, start, end);
    }

    //获取list长度
    public static Long llen(String key) throws Exception{
        return jedis.llen(key);
    }

    ///////////hash类型/////////////////////////

    public static long hset(String key, String field, String value) throws Exception{
        jedis.incr("set_counternum");
        String num=jedis.get("set_counternum");
        String field_key="set_"+num;
        setcounter("set_count",field,new Timestamp(System.currentTimeMillis()));
        jedis.sadd("all_key",key);
        return jedis.hset(key, field, value);
    }

    public static String hget(String key, String field) throws Exception{
        jedis.incr("get_counternum");
        String num=jedis.get("get_counternum");
        String field_key="get_"+num;
        setcounter("get_count",field,new Timestamp(System.currentTimeMillis()));
        return jedis.hget(key, field);
    }

    public static long hdel(String key, String field) throws Exception{
        return jedis.hdel(key, field);
    }

    public static Map<String,String> getHashMap(String key) throws Exception{
        jedis.incr("get_counternum");
        String num=jedis.get("get_counternum");
        String field="get_"+num;
        setcounter("get_count",field,new Timestamp(System.currentTimeMillis()));
        return jedis.hgetAll(key);
    }


    ////////////////zset类型/////////////////
    public static Long zadd(String key,double score,String member) throws Exception{
        jedis.incr("set_counternum");
        String num=jedis.get("set_counternum");
        String field="set_"+num;
        setcounter("set_count",field,new Timestamp(System.currentTimeMillis()));
        jedis.sadd("all_key",key);
        return jedis.zadd(key,score,member);
    }

    public static Double zscore(String key,String member) throws Exception{
        jedis.incr("get_counternum");
        String num=jedis.get("get_counternum");
        String field="get_"+num;
        setcounter("get_count",field,new Timestamp(System.currentTimeMillis()));
        return jedis.zscore(key,member);
    }
    
    public static Long zcard(String key) throws Exception{
        return jedis.zcard(key);
    }

}
