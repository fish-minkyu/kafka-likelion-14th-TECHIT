# Kafka - Consumer
- 2024.02.16

<details>
<summary><strong>Consumer Page</strong></summary>

- 메시지 받기
<div>KafkaConsumerConfig</div>
<div>KafkaConsumer: listenMessage</div>

- 비동기적으로 메시지 받기
<div>KafkaConsumerConfig</div>
<div>KafkaConsumer: listenMessage</div>


- PayloadDto 양식으로 메시지 받기
<div>KafkaDtoConsumerConfig</div>
<div>PayloadDto</div>
<div>KafkaConsumer: listenMessage</div>
<div>KafkaErrorHandler</div>
</details>

- PayloadDto로 메시지를 주고 받게 되면, 예외 처리가 까다로워 진다.  
  Dto 양식 외 메시지가 오게 되면 `KafkaErrorHandler`를 통해 에러 처리를 해줬다.

## 스팩
- Spring Boot 3.2.2
- Lombok
- Spring - Kafka
- jackson-databind:2.16.1

## Key Point

- 메시지 받기 & 비동기적으로 메시지 받기 설정  
[KafkaConsumerConfig](/src/main/java/com/example/kafka/KafkaConsumerConfig.java)
```java
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // 연결할 Kafka 브로커들
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Consumer Group ID
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "boot-group-1");
        // 처음 읽을 OFFSET 기준
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 데이터 역직렬화
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory());
        return factory;
    }
}
```

- 메시지 받기  
[KakaoConsumer](/src/main/java/com/example/kafka/KafkaConsumer.java)
```java
@Slf4j
@Component
public class KafkaConsumer {
  private static final String TOPIC = "topic";
  // 가장 기본적인 Consumer
  @KafkaListener(topics = "topic")
  public void listenMessage(String message) {
    log.info("Consuming: {}", message);
  }
}
```


- 비동기적으로 메시지 받기  
[KakaoConsumer](/src/main/java/com/example/kafka/KafkaConsumer.java)
```java
@Slf4j
@Component
public class KafkaConsumer {
  // ConsumerRecord (Producer - sendWithCallback 메소드에서 보낸 메시지에 대한 상세 정보 보기)
  // : Producer에서 보낸 메시지에 대한 상세 정보가 나와있다.
  @KafkaListener(topics = TOPIC)
  public void listenMessage(ConsumerRecord<String, String> consumerRecord) {
    log.info("Consuming: {}", consumerRecord);
  }
}
```

- PayloadDto로 메시지 받기  
[KafkaDtoConsumerConfig]()
```java
@EnableKafka
@Configuration
public class KafkaDtoConsumerConfig {
    @Bean
    public ConsumerFactory<String, PayloadDto> dtoConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // 연결할 Kafka 브로커들
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Consumer Group ID
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "boot-group-1");
        // 처음 읽을 OFFSET 기준
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 데이터 역직렬화
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(
                configProps,
                new StringDeserializer(),
                new JsonDeserializer<>(PayloadDto.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PayloadDto> kafkaListenerContainerFactory(
            KafkaErrorHandler kafkaErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, PayloadDto> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(dtoConsumerFactory());
        // Kafka Error Handler
        factory.setCommonErrorHandler(kafkaErrorHandler);
        return factory;
    }
}
```

[KafkaConsumer](/src/main/java/com/example/kafka/KafkaConsumer.java)
```java
@Slf4j
@Component
public class KafkaConsumer {
  private static final String TOPIC = "topic";

  // payloadDto
  @KafkaListener(topics = TOPIC)
  public void listenMessage(PayloadDto dto) {
    log.info("Consuming: {}", dto);
  }
}
```

[KafkaErrorHandler](/src/main/java/com/example/kafka/KafkaErrorHandler.java)
```java
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
```
