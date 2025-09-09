package com.wit.rest.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendRequest(String message) {
        kafkaTemplate.send("calc-requests", message);
        System.out.println("Sent to Kafka: " + message);

        String correlationId = UUID.randomUUID().toString();

        ProducerRecord<String, String> record =
                new ProducerRecord<>("calc-requests", message);
        record.headers().add("correlationId", correlationId.getBytes());

        kafkaTemplate.send(record);
        System.out.println("Sent to Kafka: " + message + " [correlationId=" + correlationId + "]");

        return correlationId;

    }
}
