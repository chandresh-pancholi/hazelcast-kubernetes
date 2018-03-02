package com.hazelcast.kubernetes.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryConfig;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.GroupConfig;
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
        Config config = new Config();

        config.setInstanceName("hazelcast-kubernetes");

        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("dev");
        groupConfig.setPassword("dev-pass");
        config.setGroupConfig(groupConfig);

        config.getNetworkConfig().setPort(5701);
        config.getNetworkConfig().setPortAutoIncrement(true);

        //Only important to prior version of  hazelcast 3.8
//        config.setProperty("hazelcast.discovery.enabled", "true");

        hazelcastNetworkingConfig(config);

        return config;
    }

    public void hazelcastNetworkingConfig(Config config) {
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);

        config.getNetworkConfig().getJoin().setDiscoveryConfig(hazelcastDiscoveryStrategy(config));
    }

    public DiscoveryConfig hazelcastDiscoveryStrategy(Config config) {
        DiscoveryConfig discoveryConfig = config.getNetworkConfig().getJoin().getDiscoveryConfig();

        DiscoveryStrategyConfig strategyConfig = new DiscoveryStrategyConfig("com.hazelcast.kubernetes.HazelcastKubernetesDiscoveryStrategy");
        strategyConfig.addProperty("service-name", "hazelcast-kubernetes");
        strategyConfig.addProperty("service-label-name", "");
        strategyConfig.addProperty("service-label-value", "");
        strategyConfig.addProperty("namespace", "default");
        strategyConfig.addProperty("service-dns", "hazelcast-kubernetes.default.svc.local");
        strategyConfig.addProperty("service-dns-timeout", "10");

        discoveryConfig.addDiscoveryStrategyConfig(strategyConfig);
        return discoveryConfig;
    }


}
