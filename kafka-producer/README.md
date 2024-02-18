1. 기본 메시지 보내기

KafkaProducerConfig
ProducerController
ProducerService

2. Dto로 메시지 보내기 (단, 예외처리가 까다롭고 복잡하다.)
  : Dto 양식대로 메시지를 보내야 하는데
Consumer가 메시지들을 받아들이지 못한다면, Kafka는 이 메시지들을 무조건적으로 
읽어줘야 한다고 생각하기 때문에, 컴퓨터가 무너지는 원인이 될 수 있다.
읽었다 -> 메시지를 읽는 것을 실패했다.(DTO 양식과 달라서) -> 안읽었다고 표시 -> 다시 해당 메시지를 가지고 온다.

JSON으로 보내기 때문에 받는 쪽은 String으로 역직렬화해도 상관없다.
(JSON도 문자열이니까)

KafkaProducerConfig
ProducerController
ProducerService
PayloadDto