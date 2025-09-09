package com.wit.rest.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KafkaResultListener {

    private final Map<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();

    public void registerRequest(String correlationId, CompletableFuture<String> future) {
        pendingRequests.put(correlationId, future);
    }

    @KafkaListener(topics = "calc-results", groupId = "rest-group")
    public void listen(String message, ConsumerRecord<String, String> record) {
        String correlationId = new String(record.headers().lastHeader("correlationId").value());
        System.out.println("Received result: " + message + " [correlationId=" + correlationId + "]");

        CompletableFuture<String> future = pendingRequests.remove(correlationId);
        if (future != null) {
            future.complete(message);
        }
    }
}
