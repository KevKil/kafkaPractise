package com.kev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InitSender {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private KafkaSenderExample kafkaSenderExample;
	
	@Value("${spring.kafka.topic-1}")
	private String topic1;
	@Value("${spring.kafka.topic-2}")
	private String topic2;
	@Value("${spring.kafka.topic-4}")
	private String topic4;
	
	@EventListener
	void initiateSendingMessage(ApplicationReadyEvent event) throws InterruptedException {
		Thread.sleep(5000);
		LOG.info("---------------------------------");
		kafkaSenderExample.sendMessage("I'll be recevied by MultipleTopicListener, Listener & ClassLevel KafkaHandler", topic1);

		Thread.sleep(5000);
		LOG.info("---------------------------------");
		kafkaSenderExample.sendMessageWithRoutingTemplate("I'm sent using RoutingTemplate", "reflectoring-bytes");
		
		Thread.sleep(5000);
		LOG.info("---------------------------------");
		kafkaSenderExample.sendCustomMessage(new User("Lucario"), topic4);
	}
}
