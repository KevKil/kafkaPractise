package com.kev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSenderExample {
	private final Logger LOG = LoggerFactory.getLogger(KafkaSenderExample.class);
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private RoutingKafkaTemplate routingKafkaTemplate;
	@Autowired
	private KafkaTemplate<String, User> userKafkaTemplate;
	
	void sendMessage(String message, String topicName) {
		LOG.info("Sending : {}", message);
		LOG.info("--------------------------------");
		kafkaTemplate.send(topicName, message);
	}
	
	void sendMessageWithRoutingTemplate(String message, String topicName) {
		LOG.info("Sending : {}", message);
		LOG.info("--------------------------------");

		routingKafkaTemplate.send(topicName, message.getBytes());
	}
	
	void sendCustomMessage(User user, String topicName) {
		LOG.info("Sending Json Serializer : {}", user);
		LOG.info("--------------------------------");

		userKafkaTemplate.send(topicName, user);
	}
	
}
