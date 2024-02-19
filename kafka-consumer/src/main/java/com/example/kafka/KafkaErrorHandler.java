package com.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
// 정해진 PayloadDto 외 메시지는 에러를 발생한다.
// 그래서, 만약 Dto 양식과 맞지 않는 메시지가 온다면 ErrorHandler로 관리해준다.
// 오류가 발생한 레코드를 건너뛰고 다음 레코드를 읽혀준다.
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
