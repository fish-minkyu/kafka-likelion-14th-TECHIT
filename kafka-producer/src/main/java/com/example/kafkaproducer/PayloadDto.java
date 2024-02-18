package com.example.kafkaproducer;

import lombok.Data;

@Data
// 누가 보냈는지 DTO에다가 표시
public class PayloadDto {
  private String producer;
  private String message;
}
