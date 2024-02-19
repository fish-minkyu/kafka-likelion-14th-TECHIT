package com.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaErrorHandler implements CommonErrorHandler {
  @Override
  public void handleOtherException(
    Exception exception,
    // ?: 와일드 카드 형
    Consumer<?, ?> consumer, // consumer와
    MessageListenerContainer container, // 해당 consumer을 관리하는 container 전달
    boolean batchListener
  ) {
    log.error("Exception: {}", exception.getMessage());
    log.warn("Consumer: {}", consumer);
    log.warn("Container: {}", container);
    // 역직렬화 과정에서 예외가 발생했다.
    if (exception instanceof RecordDeserializationException ex) {
        // 다음 레코드를 읽겠다.
        consumer.seek(ex.topicPartition(), ex.offset() + 1L);
        consumer.commitSync();
    }
  }
}
