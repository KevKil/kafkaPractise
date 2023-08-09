package com.kev.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.kev.User;

@Configuration
public class KafkaProducerConfig {
	
	private final Logger LOG = LoggerFactory.getLogger(KafkaProducerConfig.class);
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	
	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		// Host and port which  kafka is running
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		// Serializer for the key
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// Serializer for the value
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
	}
	// Will create kafka producer instances
	@Bean
	public ProducerFactory<String,String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}
	// Sends messages to the respactive topics
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		// Send message can be used to configure an async callback with Futures
		// Without Futures (sync) we register ProducerListener bean to handle sendMessage response
		
		KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
		kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
		kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
			@Override
			public void onSuccess(
					ProducerRecord<String, String> producerRecord,
					RecordMetadata recordMetadata) {
				LOG.info("ACK from ProducerLitener message: {} offset: {}", 
						producerRecord.value(), recordMetadata.offset());
			}
		});
		return kafkaTemplate;
	}
	// Routing is for when you have multiple producers with diff configs and we want to select a
	// producer at runtime based on topic name
	@Bean
	public RoutingKafkaTemplate routingTemplate(GenericApplicationContext context) {
		// ProducerFactory with Bytes serializer
	    Map<String, Object> props = new HashMap<>();
	    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
	      bootstrapServers);
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
	      StringSerializer.class);
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
	      ByteArraySerializer.class);
	    DefaultKafkaProducerFactory<Object, Object> bytesPF = new DefaultKafkaProducerFactory<>(props);
	    context.registerBean(DefaultKafkaProducerFactory.class,"bytesPF",bytesPF );
	    
	 // ProducerFactory with String serializer
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
	      StringSerializer.class);
	    DefaultKafkaProducerFactory<Object, Object> stringPF = 
	      new DefaultKafkaProducerFactory<>(props);
	    
	    Map<Pattern, ProducerFactory<Object, Object>> map = new LinkedHashMap<>();
	    map.put(Pattern.compile(".*-bytes"),bytesPF);
	    map.put(Pattern.compile("reflectoring-.*"), stringPF);
	    
		return new RoutingKafkaTemplate(map);	
	}
	
	@Bean
	public ProducerFactory<String, User> userProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	@Bean
	public KafkaTemplate<String, User> userKafkaTemplate() {
		return new KafkaTemplate<>(userProducerFactory());
	}
	
 }
