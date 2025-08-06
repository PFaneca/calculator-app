package com.wit.rest.controller;

import com.wit.rest.kafka.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final KafkaProducer producer;

    public CalculatorController(KafkaProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/sum")
    public ResponseEntity<Map<String, String>> sum(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        BigDecimal result = a.add(b);
        producer.sendMessage("SUM:" + a + "," + b);
        return ResponseEntity.ok(Collections.singletonMap("result", result.toString()));
    }
    @GetMapping("/subtract")
    public ResponseEntity<Map<String, String>> subtract(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        BigDecimal result = a.subtract(b);
        producer.sendMessage("SUBTRACTION:" + a + "," + b);
        return ResponseEntity.ok(Collections.singletonMap("result", result.toString()));
    }
    @GetMapping("/multiply")
    public ResponseEntity<Map<String, String>> multiply(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        BigDecimal result = a.multiply(b);
        producer.sendMessage("MULTIPLICATION:" + a + "," + b);
        return ResponseEntity.ok(Collections.singletonMap("result", result.toString()));
    }

    @GetMapping("/divide")
    public ResponseEntity<Map<String, String>> divide(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Division by zero"));
        }
        BigDecimal result = a.divide(b, 10, BigDecimal.ROUND_HALF_UP);
        producer.sendMessage("DIVISON:" + a + "," + b);
        return ResponseEntity.ok(Collections.singletonMap("result", result.toString()));
    }

}