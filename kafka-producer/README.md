# Kafka - Producer
- 2024.02.16

<details>
<summary><strong>Producer Page</strong></summary>

- 메시지 보내기(단순 메시지만 보내기)
<div>KafkaProducerConfig: stringProducerFactory & stringKafkaTemplate</div>
<div>KafkaService: send</div>
<div>KafkaController: publish</div>

- 비동기적으로 메시지 보내기(메시지에 대한 정보 확인 가능)
<div>KafkaProducerConfig: stringProducerFactory & stringKafkaTemplate</div>
<div>KafkaService: sendWithCallback & sendResultSync</div>
<div>KafkaController: publish</div>

- Dto 양식으로 메시지 보내기
<div>KafkaProducerConfig: payloadProducerFactory & payloadKafkaTemplate</div>
<div>PayloadDto</div>
<div>KafkaService: sendDto</div>
<div>KafkaController: publishJson</div>
</details>

- Kafka로 Broker를 생성할 topic에 맞게 생성을 해준다.

## 스팩
- Spring Boot 3.2.2
- Spring Web
- Lombok
- Spring - Kafka

## Key Point

- 메시지 보내기 & 비동기적으로 메시지 보내기 설정  
[KafkaProducerConfig](/src/main/java/com/example/kafka/KafkaProducerConfig.java)
```java
@Configuration
public class KafkaProducerConfig {
  @Bean
  // Producer 설정
  public ProducerFactory<String, String> stringProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    // 연결할 Kafka Broker들
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    // 데이터 직렬화 (java 객체를 주고 받는 방법)
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    // Ack
    configProps.put(ProducerConfig.ACKS_CONFIG, "all");
    // 데이터를 어떤 규칙으로 특정 Partition에 전달할지
    // RoundRobinPartitioner: Producer가 한번씩 돌면서 Partition에 메시지를 넣어준다.
    configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class);

    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  // Kafka Template 정의
  // : Kafka Template을 사용하면 Kafka에게 메시지를 전달할 수 있다.
  public KafkaTemplate<String, String> stringKafkaTemplate() {
    return new KafkaTemplate<>(stringProducerFactory());
  }
}
```

- 메시지 보내기  
[ProducerService](/src/main/java/com/example/kafka/ProducerService.java)
```java
  // 단순 메시지 보내기
  public void send(String message) {
    // partition과 key를 정해주면 하나의 partition에만 집중적으로 보낼 수 있다.
//    stringKafkaTemplate.send(TOPIC, 0, "key", message);
    stringKafkaTemplate.send(TOPIC, message);
  }
```

- 비동기적으로 메시지 보내기  
[ProducerService](/src/main/java/com/example/kafka/ProducerService.java)
```java
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
```


- Dto 양식으로 메시지 보내기   

[KafkaProducerConfig](/src/main/java/com/example/kafka/KafkaProducerConfig.java)
```java
@Configuration
public class KafkaProducerConfig {
  @Bean
  public ProducerFactory<String, PayloadDto> payloadProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    // 연결할 Kafka Broker들
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    // 데이터 직렬화
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  // Kafka Template - PayloadDto
  public KafkaTemplate<String, PayloadDto> payloadKafkaTemplate() {
    return new KafkaTemplate<>(payloadProducerFactory());
  }
}
```
[ProducerService](/src/main/java/com/example/kafka/ProducerService.java)
```java
  // PaylaodDto 메시지 보내기
  public void sendDto(PayloadDto dto) {
    payloadKafkaTemplate.send(TOPIC, dto);
  }
```