package com.wit.calculator.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    public BigDecimal calculate(String operation, BigDecimal a, BigDecimal b) {
        switch (operation) {
            case "SUM":
                return a.add(b);
            case "SUBTRACTION":
                return a.subtract(b);
            case "MULTIPLICATION":
                return a.multiply(b);
            case "DIVISION":
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a.divide(b, 10, RoundingMode.HALF_UP);
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }
}