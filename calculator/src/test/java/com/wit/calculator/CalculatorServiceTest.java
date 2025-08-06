package com.wit.calculator;

import com.wit.calculator.service.CalculatorService;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private final CalculatorService service = new CalculatorService();

    @Test
    void testSum() {
        BigDecimal result = service.calculate("SUM", BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        assertEquals(BigDecimal.valueOf(15), result);
    }

    @Test
    void testSubtract() {
        BigDecimal result = service.calculate("SUBTRACTION", BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        assertEquals(BigDecimal.valueOf(5), result);
    }

    @Test
    void testMultiply() {
        BigDecimal result = service.calculate("MULTIPLICATION", BigDecimal.valueOf(3), BigDecimal.valueOf(7));
        assertEquals(BigDecimal.valueOf(21), result);
    }

    @Test
    void testDivide() {
        BigDecimal result = service.calculate("DIVISION", BigDecimal.valueOf(10), BigDecimal.valueOf(4));
        assertEquals(BigDecimal.valueOf(2.5).setScale(10, RoundingMode.HALF_UP), result);
    }

    @Test
    void testDivisionByZeroThrows() {
        assertThrows(ArithmeticException.class,
                () -> service.calculate("DIVISION", BigDecimal.TEN, BigDecimal.ZERO));
    }

    @Test
    void testUnknownOperationThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculate("POWER", BigDecimal.TEN, BigDecimal.ONE));
    }
}
