package com.example.kafkaconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {
  // 가장 기본적인 Consumer
  @KafkaListener(topics = "topic")
  public void listenMessage(String message) {
    log.info("Consuming: {}", message);
  }
}
