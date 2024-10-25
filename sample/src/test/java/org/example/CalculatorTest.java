package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    @Test
    void calculator_덧셈_테스트() {
        Calculator calculator = new Calculator();
        assertEquals(3, calculator.calculate(1, "+", 2));
    }
    @Test
    void calculator_뺄셈_테스트() {
        Calculator calculator = new Calculator();
        assertEquals(-1, calculator.calculate(1, "-", 2));
    }

    @Test
    void calculator_곱셈_테스트() {
        Calculator calculator = new Calculator();
        assertEquals(2, calculator.calculate(1, "*", 2));
    }

    @Test
    void calculator_나눗셈_테스트() {
        Calculator calculator = new Calculator();
        assertEquals(2, calculator.calculate(4, "/", 2));
    }

    @Test
    void Calulotor_연산자_오류_테스트(){
        Calculator calculator = new Calculator();
        assertThrows(InvalidOperationException.class, () -> calculator.calculate(1, "%", 2));
    }
}