package com.example.kafkaconsumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

// @EnableKafka
// : Kafka를 사용하겠다.
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
  @Bean
  public ConsumerFactory<String, String> stringConsumerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    // 연결할 Kafka 브로커들
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
    // Consumer만 하는 설정들
    // Consumer Group ID
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "boot-group-1");
    // 처음 읽을 OFFSET 기준(다시 새로 시작했을 때, 몇번 Offset부터 읽어들일지 설정)
    // earliest: 모든 설정들을 읽어들인다.
    configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    // 데이터 역직렬화
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(configProps);
  }

  @Bean
  // Consumer를 만들어주는 Container Factory 객체
  // kafkaListenerContainerFactory의 이름을 정확히 나타내줘야 한다. (Bean 객체는 이름을 보고 찾기 때문)
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory
      = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(stringConsumerFactory());
    return factory;
  }
}
