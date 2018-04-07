package net.bndy.cache;

import net.bndy.lib.CollectionHelper;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

public class RedisCache extends AbstractCache {

    private ShardedJedisPool shardedJedisPool;
    private List<Server> servers = new ArrayList<>();

    public RedisCache (String servers) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>();
        for(String server: servers.split("[,;|]")) {
            int separatorIndex = server.lastIndexOf(":");
            String host = server.substring(0, separatorIndex);
            int port = Integer.parseInt(server.substring(separatorIndex + 1));
            Server server1 = new Server(host, port);
            this.servers.add(server1);

            JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port);
            jedisShardInfoList.add(jedisShardInfo);
        }

        this.shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, jedisShardInfoList);
    }

    @Override
    public void set(Object data) {
        ShardedJedis jedis = null;
        try {
            jedis = this.shardedJedisPool.getResource();
            jedis.set(this.getKey(data), this.getJson(data));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void set(Object data, long exp) {
        String key = this.getKey(data);
        ShardedJedis jedis = null;
        try {
            jedis = this.shardedJedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.set(this.getKey(data), this.getJson(data), "XX", "EX", exp);
            } else {
                jedis.set(this.getKey(data), this.getJson(data), "NX", "EX", exp);
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public <T> T get(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = this.shardedJedisPool.getResource();
            if (jedis.exists(key)) {
                String json = jedis.get(key);
                return this.<T>deserialize(json);
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return null;
    }

    @Override
    public int del(String... keys) {
        int result = 0;
        ShardedJedis jedis = null;
        try {
            jedis = this.shardedJedisPool.getResource();
            for (String key : keys) {
                jedis.del(key);
                result++;
            }
            return result;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public int delAll() {
        Server masterServer = CollectionHelper.first(this.servers);
        Jedis jedis = null;
        try {
            jedis = new Jedis(masterServer.getHost(), masterServer.getPort());
            int result = jedis.dbSize().intValue();
            jedis.flushDB();
            return result;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
