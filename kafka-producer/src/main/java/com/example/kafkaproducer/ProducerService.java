package com.example.kafkaproducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
  private final KafkaTemplate<String, String> stringKafkaTemplate;

  public void send(String message) {
    stringKafkaTemplate.send("topic", message);
  }
}
