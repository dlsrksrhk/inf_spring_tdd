package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CalculationRequestTest {
    @Test
    public void calculaton_요청_객체_만들기_테스트() {
        CalculationRequest calculationRequest = new CalculationRequest(new String[]{"1", "+", "2"});
        assertEquals(1, calculationRequest.getNum1());
        assertEquals("+", calculationRequest.getOperation());
        assertEquals(2, calculationRequest.getNum2());
    }

    @Test
    public void calculaton_요청_객체_만들기_파라미터_개수_잘못됐을때_예외_테스트() {
        assertThrows(BadRequestException.class, () -> new CalculationRequest(new String[]{"1", "+", "2", "3"}));
    }

    @Test
    public void calculaton_유효하지_않은_연산자_예외_테스트() {
        assertThrows(InvalidOperationException.class, () -> new CalculationRequest(new String[]{"1", "=", "2"}));
    }
}