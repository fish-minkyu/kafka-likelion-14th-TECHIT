package com.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
  // "topic" 상수화 시키기
  private static final String TOPIC = "topic";

  private final KafkaTemplate<String, String> stringKafkaTemplate;
  // KafkaTemplate - PayloadDto 설정 DI
  private final KafkaTemplate<String, PayloadDto> payloadKafkaTemplate;



  public void send(String message) {
    // partition과 key를 정해주면 하나의 partition에만 집중적으로 보낼 수 있다.
//    stringKafkaTemplate.send(TOPIC, 0, "key", message);
    stringKafkaTemplate.send(TOPIC, message);
  }

  // 비동기 send
  public void sendWithCallback(String message) {
    // CompletableFuture == Promise
    // So, 비동기 작업이 가능하다.
    CompletableFuture<SendResult<String, String>> sendResultFuture
      = stringKafkaTemplate.send(TOPIC, message);
    // 비동기 작업이 완료되면, 그 다음 ()안에 작업을 하겠다.
    sendResultFuture.whenComplete((sendResult, throwable) -> {
      log.info("send().whenComplete()");
      log.info(String.valueOf(sendResult));
      log.info(String.valueOf(throwable));
    });
    // "end sendWithCallback()"가 먼저 출력이 된다.
    // 그 이유는 sendResultFuture는 비동기적으로 작동이 되기에
    // 비동기 작업이 완료 된 후, sendResultFuture.whenComplete()가 실행되기 때문
    // So, sendResultFuture 작업을 시켜놓고 컴퓨터는 log.info("end sendWithCallback()");를 실행한다.
    log.info("end sendWithCallback()");
  }

  // 비동기 작업을 동기적으로 처리하기 (보내는 쪽에서 Result를 확인할 수 있다.)
  public void sendResultSync(String message) {
    CompletableFuture<SendResult<String, String >> sendResultFuture
      = stringKafkaTemplate.send(TOPIC, message);
    // 확실히 응답을 받고 진행하고 싶다면 get()으로 가져와
    // 동기식으로 처리할 수 있다.
    try {
      SendResult<String, String > sendResult = sendResultFuture.get();
    } catch (InterruptedException | ExecutionException ignored) {}
  }

  // PaylaodDto 메시지 보내기
  public void sendDto(PayloadDto dto) {
    payloadKafkaTemplate.send(TOPIC, dto);
  }
}
