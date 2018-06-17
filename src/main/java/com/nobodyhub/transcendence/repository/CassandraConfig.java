package com.nobodyhub.transcendence.repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yan_h
 * @since 2018/6/10
 */
@Configuration
@ComponentScan(basePackages = "com.nobodyhub.transcendence.repository")
public class CassandraConfig {
    @Value("${cassandra.host}")
    private String[] host;
    @Value("${cassandra.port}")
    private String port;
    @Value("${cassandra.keyspace}")
    private String keyspace;

    @Bean
    public Session session(Cluster cluster) {
        Session session = cluster.connect();
        session.execute(createKeyspaceCql(keyspace));
        session.execute(useKeyspaceCql(keyspace));
        return session;
    }

    @Bean
    public Cluster cluster() {
        return Cluster.builder()
                .addContactPoints(host)
                .withPort(Integer.parseInt(port))
                .withSocketOptions(new SocketOptions()
                        .setConnectTimeoutMillis(20000))
                .build();
    }

    private String createKeyspaceCql(String keyspace) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" CREATE KEYSPACE IF NOT EXISTS %s ", keyspace));
        sb.append(" WITH replication = ");
        sb.append(" { ");
        sb.append("  'class':'SimpleStrategy', ");
        sb.append("  'replication_factor':3 ");
        sb.append(" } ");
        return sb.toString();
    }

    private String useKeyspaceCql(String keyspace) {
        return String.format(" USE %s; ", keyspace);
    }
}
