package com.wit.rest.controller;

import com.wit.rest.kafka.KafkaProducer;
import com.wit.rest.kafka.KafkaResultListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final KafkaProducer producer;
    private final KafkaResultListener resultListener;

    public CalculatorController(KafkaProducer producer, KafkaResultListener resultListener) {
        this.producer = producer;
        this.resultListener = resultListener;
    }

    @GetMapping("/sum")
    public ResponseEntity<Map<String, String>> sum(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) throws Exception {
       /* BigDecimal result = a.add(b);
        producer.sendMessage("SUM:" + a + "," + b);

         return ResponseEntity.ok(Collections.singletonMap("result", result.toString())); */
        // Create message with operation and operands
        String message = "SUM:" + a + "," + b;

        // Send request to Kafka and get a correlationId
        String correlationId = producer.sendRequest(message);

        // Create a CompletableFuture to wait for the Calculator result
        CompletableFuture<String> future = new CompletableFuture<>();
        resultListener.registerRequest(correlationId, future);

        // Wait up to 5 seconds for the result
        String result = future.get(5, TimeUnit.SECONDS);

        return ResponseEntity.ok(Collections.singletonMap("result", result));
    }
    @GetMapping("/subtract")
    public ResponseEntity<Map<String, String>> subtract(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) throws Exception {
        /*BigDecimal result = a.subtract(b);
        // producer.sendMessage("SUBTRACTION:" + a + "," + b);
        //return ResponseEntity.ok(Collections.singletonMap("result", result.toString())); */
        String message = "SUBTRACTION:" + a + "," + b;
        String correlationId = producer.sendRequest(message);
        // Future to wait for answer
        CompletableFuture<String> future = new CompletableFuture<>();
        resultListener.registerRequest(correlationId, future);

        // Espera resultado até 5 segundos
        String result = future.get(5, TimeUnit.SECONDS);

        return ResponseEntity.ok(Collections.singletonMap("result", result));
    }
    @GetMapping("/multiply")
    public ResponseEntity<Map<String, String>> multiply(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) throws Exception {
      /*  BigDecimal result = a.multiply(b);
        producer.sendMessage("MULTIPLICATION:" + a + "," + b);
        return ResponseEntity.ok(Collections.singletonMap("result", result.toString())); */
        String message = "MULTIPLICATION:" + a + "," + b;
        String correlationId = producer.sendRequest(message);

        CompletableFuture<String> future = new CompletableFuture<>();
        resultListener.registerRequest(correlationId, future);

        String result = future.get(5, TimeUnit.SECONDS);
        return ResponseEntity.ok(Collections.singletonMap("result", result));
    }

    @GetMapping("/divide")
    public ResponseEntity<Map<String, String>> divide(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) throws Exception {
      /*  if (b.compareTo(BigDecimal.ZERO) == 0) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Division by zero"));
        }
        BigDecimal result = a.divide(b, 10, BigDecimal.ROUND_HALF_UP);
        producer.sendMessage("DIVISON:" + a + "," + b);
        return ResponseEntity.ok(Collections.singletonMap("result", result.toString())); */
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            // Division by zero check
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", "Division by zero"));
        }

        String message = "DIVISION:" + a + "," + b;
        String correlationId = producer.sendRequest(message);

        CompletableFuture<String> future = new CompletableFuture<>();
        resultListener.registerRequest(correlationId, future);

        String result = future.get(5, TimeUnit.SECONDS);
        return ResponseEntity.ok(Collections.singletonMap("result", result));
    }

}