package com.lambdaworks.redis.cluster.commands;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

import com.lambdaworks.redis.FastShutdown;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.TestSettings;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.cluster.ClusterTestUtil;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.commands.CustomCommandTest;

/**
 * @author Mark Paluch
 */
public class CustomClusterCommandTest extends CustomCommandTest {

    private static RedisClusterClient redisClusterClient;
    private StatefulRedisClusterConnection<String, String> clusterConnection;

    @BeforeClass
    public static void setupClient() {
        redisClusterClient = new RedisClusterClient(
                RedisURI.Builder.redis(TestSettings.host(), TestSettings.port(900)).build());
    }

    @AfterClass
    public static void closeClient() {
        FastShutdown.shutdown(redisClusterClient);
    }

    @Before
    public void openConnection() throws Exception {
        redis = connect();
        ClusterTestUtil.flushDatabaseOfAllNodes(clusterConnection);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected RedisCommands<String, String> connect() {
        clusterConnection = redisClusterClient.connectCluster().getStatefulConnection();
        return ClusterTestUtil.redisCommandsOverCluster(clusterConnection);
    }

    @Ignore("MULTI not available on Redis Cluster")
    @Override
    public void dispatchTransactions() throws Exception {
    }
}
