package com.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {
  private static final String TOPIC = "topic";
//  // 가장 기본적인 Consumer
//  @KafkaListener(topics = "topic")
//  public void listenMessage(String message) {
//      log.info("Consuming: {}", message);
//  }

//  // ConsumerRecord (Producer - sendWithCallback 메소드에서 보낸 메시지에 대한 상세 정보 보기)
//  // : Producer에서 보낸 메시지에 대한 상세 정보가 나와있다.
//  @KafkaListener(topics = TOPIC)
//  public void listenMessage(ConsumerRecord<String, String> consumerRecord) {
//    log.info("Consuming: {}", consumerRecord);
//  }

//  // partition 나눠듣기
//  // 실제로는 이렇게 해줄 필요는 없다.
//  // Group 안에 정의가 되어 있다면
//  // Consumer Group에 따라서 어떤 파티션에 붙을지 알아서 정의가 된다.
//  @KafkaListener(
//    topicPartitions = @TopicPartition(
//      topic = TOPIC, partitions = "0")
//  )
//  public void listenMessage0(String message) {
//    log.info("Consuming 0: {}", message);
//  }
//
//  @KafkaListener(
//    topicPartitions = @TopicPartition(
//      topic = TOPIC, partitions = "1"))
//  public void listenMessage1(String message) {
//    log.info("Consuming 1: {}", message);
//  }
//
//  @KafkaListener(
//    topicPartitions = @TopicPartition(
//      topic = TOPIC, partitions = "2"))
//  public void listenMessage2(String message) {
//    log.info("Consuming 2: {}", message);
//  }

  // payloadDto
  @KafkaListener(topics = TOPIC)
  public void listenMessage(PayloadDto dto) {
    log.info("Consuming: {}", dto);
  }
}
