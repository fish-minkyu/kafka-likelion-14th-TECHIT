# Kafka
- 2024.02.15 ~ 02.16 `14주차`
- 02.15 - Kafka 이론 학습 & Kafka 설치 및 실행
- 02.16 - Spring Kafka

`2월 15일`
- [Kafka 홈페이지](https://kafka.apache.org/)에서 Mac OS 기준, Scals를 다운 받음  
- Kafka를 실행(zookeeper & kafka)
```java
// zookeeper 실행
./bin/zookeeper-server-start.sh ./config/zookeeper.properties

// Kafka 실행 명령어
./bin/kafka-server-start.sh config/server.properties
```
- 복수의 Broker들을 설정 변경하고 실행 
```java
// server.properties의 24, 34, 62 라인 변경
// Broker를 구분하기 위한 설정으로 0, 1, 2로 변경
broker.id = 0

// Kafka의 HOST:PORT 설정, 9092 ~ 9094까지 설정
listeners=PLAINTEXT://localhost:9092

// Kafka의 로그가 저장되는 위치로, 겹치지 않게
// (/tmp/kafka-logs0 ~ /tmp/kafka-logs2)
log.dirs=/tmp/kafka-logs
```
- TOPIC 생성 및 실행 (Broker가 실행되고 있는 개수까지 Replica 설정 가능)


`2월 16일`
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
<div>KafkaService: sendDto</div>
<div>KafkaController: publishJson</div>
</details>



## README.md
[Kafka_Producer]()  
[Kafka_Consumer]()

## GitHub
- 강사님 GitHub  
[likelion-backend-8-kafka](https://github.com/edujeeho0/likelion-backend-8-kafka)

