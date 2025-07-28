package com.example.sales_service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaTopicConfig {
    @Bean
    public NewTopic createMyTopic() {
        return TopicBuilder.name("product-events")
            .partitions(1)
            .replicas(1)
            .build();
    }
}
