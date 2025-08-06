package com.wit.rest;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSumEndpoint() throws Exception {
        mockMvc.perform(get("/api/sum")
                        .param("a", "10")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("15"));
    }

    @Test
    void testSubtractEndpoint() throws Exception {
        mockMvc.perform(get("/api/subtract")
                        .param("a", "10")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("5"));
    }

    @Test
    void testMultiplyEndpoint() throws Exception {
        mockMvc.perform(get("/api/multiply")
                        .param("a", "3")
                        .param("b", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("21"));
    }

    @Test
    void testDivideEndpoint() throws Exception {
        mockMvc.perform(get("/api/divide")
                        .param("a", "10")
                        .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("5.0000000000"));
    }

    @Test
    void testDivideByZero() throws Exception {
        mockMvc.perform(get("/api/divide")
                        .param("a", "10")
                        .param("b", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Division by zero"));
    }
}
