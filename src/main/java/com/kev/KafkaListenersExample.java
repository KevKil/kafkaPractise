package com.kev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenersExample {

	private final Logger LOG = LoggerFactory.getLogger(KafkaListenersExample.class);
	
	@KafkaListener(topics = "reflectoring-1")
	void listener(String data) {
		LOG.info(data);
	}
	
	@KafkaListener(
			topics = "reflectoring-1, reflectoring-2",
			groupId = "reflectoring-group-2")
	void commomListenerForMultipleTopics(String message) {
		LOG.info("MultipleToppicListener - {}", message);
	}
	
	@KafkaListener(topics = "reflectoring-bytes")
	void listenerForRoutingTemplate(String message) {
		LOG.info("RoutingTemplate BytesListener [{}]", message);
	}

	@KafkaListener(id = "1", topics = "reflectoring-user", groupId = "reflectoring-user-mc", containerFactory = "kafkaJsonListenerContainerFactory")
	void listenerWithMessageConverter(User user) {
		LOG.info("MessageConverterUserListener [{}]", user);
	}
	
}
