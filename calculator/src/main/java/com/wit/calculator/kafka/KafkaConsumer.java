package com.wit.calculator.kafka;

import com.wit.calculator.service.CalculatorService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaConsumer {

    private final CalculatorService service;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaConsumer(CalculatorService service, KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.service = service;
    }

  /*  @KafkaListener(topics = "calc-requests", groupId = "calc-group")
    public void listen(String message) {
        System.out.println("Received from Kafka: " + message);

        try {
            // Message format example: "SUM:10,5"
            String[] parts = message.split(":");
            String operation = parts[0];
            String[] values = parts[1].split(",");

            BigDecimal a = new BigDecimal(values[0]);
            BigDecimal b = new BigDecimal(values[1]);

            BigDecimal result = service.calculate(operation, a, b);
            System.out.println("Result: " + result);

        } catch (Exception e) {
            System.out.println(" Error processing the message: " + e.getMessage());
        }
    } */
  @KafkaListener(topics = "calc-requests", groupId = "calc-group")
  public void listen(String message, ConsumerRecord<String, String> record) {
      String correlationId = new String(record.headers().lastHeader("correlationId").value());
      System.out.println("Received from Kafka: " + message + " [correlationId=" + correlationId + "]");

      try {
          String[] parts = message.split(":");
          String operation = parts[0];
          String[] values = parts[1].split(",");

          BigDecimal a = new BigDecimal(values[0]);
          BigDecimal b = new BigDecimal(values[1]);

          BigDecimal result = service.calculate(operation, a, b);
          System.out.println("Result: " + result);

          // Publish response with the same correlationId
          ProducerRecord<String, String> response =
                  new ProducerRecord<>("calc-results", result.toString());
          response.headers().add("correlationId", correlationId.getBytes());

          kafkaTemplate.send(response);

      } catch (Exception e) {
          System.out.println("Error processing the message: " + e.getMessage());
      }
  }
}