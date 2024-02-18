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
//    // 가장 기본적인 Consumer
//    @KafkaListener(topics = "topic")
//    public void listenMessage(String message) {
//        log.info("Consuming: {}", message);
//    }

//    // ConsumerRecord
//    @KafkaListener(topics = TOPIC)
//    public void listenMessage(ConsumerRecord<String, String> consumerRecord) {
//        log.info("Consuming: {}", consumerRecord);
//    }

//    // partition 나눠듣기
//    @KafkaListener(
//            topicPartitions = @TopicPartition(
//                    topic = TOPIC, partitions = "0")
//    )
//    public void listenMessage0(String message) {
//        log.info("Consuming 0: {}", message);
//    }
//
//    @KafkaListener(
//            topicPartitions = @TopicPartition(
//                    topic = TOPIC, partitions = "1"))
//    public void listenMessage1(String message) {
//        log.info("Consuming 1: {}", message);
//    }
//
//    @KafkaListener(
//            topicPartitions = @TopicPartition(
//                    topic = TOPIC, partitions = "2"))
//    public void listenMessage2(String message) {
//        log.info("Consuming 2: {}", message);
//    }

    // payloadDto
    @KafkaListener(topics = TOPIC)
    public void listenMessage(PayloadDto dto) {
        log.info("Consuming: {}", dto);
    }
}
