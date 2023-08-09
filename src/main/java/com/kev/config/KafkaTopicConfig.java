package com.kev.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic topic1() {
		return TopicBuilder.name("reflectoring-1").build();
	}
	
	@Bean
	public NewTopic topic2() {
		return TopicBuilder.name("reflectoring-2").build();
	}
	
	@Bean
	NewTopic topicBytes() {
		return TopicBuilder.name("reflectoring-bytes").build();
	}
	
	@Bean
	NewTopic topicUser() {
		return TopicBuilder.name("reflectoring-user").build();
	}
	
	// KafkaAdmin bean is responsible for creating new topics in our broker
//	@Bean
//	KafkaAdmin admin() {
//		Map<String, Object> configs = new HashMap<>();
//		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, configs)
//	}
	//In springboot it is Autoconfigured
}
