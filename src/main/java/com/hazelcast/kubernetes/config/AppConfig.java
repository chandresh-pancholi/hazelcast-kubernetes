package com.hazelcast.kubernetes.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {
    @Value("${hazelcast.host}")
    private String hazelcastHost;

    @Value("${hazelcast.port}")
    private int hazelcastPort;

    @Value("${hazelcast.group.name}")
    private String hazelCastGroupName;

    @Value("${hazelcast.group.password}")
    private String hazelCastGroupPassword;
}
