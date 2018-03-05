package com.hazelcast.kubernetes.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class HazelcastContext {
    @Autowired
    private AppConfig appConfig;


    @Bean
    public HazelcastInstance hazelCastInstance() {

        Config config = hazelcastConfig();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);

        return instance;
    }

    public Config hazelcastConfig() {
        Config config =  new Config();
        config.setInstanceName("landlord");

        config.getNetworkConfig().setPort(5701);
        config.getNetworkConfig().setPortAutoIncrement(true);

        config.setProperty("hazelcast.discovery.enabled", "true");
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);

        config.getNetworkConfig().getJoin().setDiscoveryConfig(hazelcastDiscoveryStrategy(config));
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        return config;
    }



    public DiscoveryConfig hazelcastDiscoveryStrategy(Config config) {
        JoinConfig join = config.getNetworkConfig().getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().setEnabled(false);

        DiscoveryConfig discoveryConfig = join.getDiscoveryConfig();
        discoveryConfig.isEnabled();
        DiscoveryStrategyConfig strategyConfig = new DiscoveryStrategyConfig("com.hazelcast.kubernetes.HazelcastKubernetesDiscoveryStrategy");
        strategyConfig.addProperty("service-name", "hazelcast-kubernetes");

        discoveryConfig.addDiscoveryStrategyConfig(strategyConfig);
        return discoveryConfig;

    }


}
