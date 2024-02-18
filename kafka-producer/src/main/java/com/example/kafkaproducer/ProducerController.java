package com.example.kafkaproducer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProducerController {
  private final ProducerService service;

  // 메시지 보내기 Handler Method
  @PostMapping("/publish")
  public String publish(
    @RequestParam("message") String message
  ) {
//    service.send(message);
    service.sendWithCallback(message);
    return "published: " + message;
  }

  // PayloadDto 메시지 보내기 Handler Method
  @PostMapping("/publish-json")
  public String publishJson(
    @RequestBody
    PayloadDto dto
  ) {
    service.sendDto(dto);
    return "publish dto: " + dto;
  }
}
