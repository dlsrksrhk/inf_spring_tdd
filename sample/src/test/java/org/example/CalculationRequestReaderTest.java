package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class CalculationRequestReaderTest {
    @Test
    void 표준입력으로_데이터_읽어오기_테스트() {
        //given
        System.setIn(new ByteArrayInputStream("1 + 2".getBytes()));

        //when
        CalculationRequestReader calculationRequestReader = new CalculationRequestReader();
        CalculationRequest result = calculationRequestReader.read();

        //then
        assertEquals(1, result.getNum1());
        assertEquals("+", result.getOperation());
        assertEquals(2, result.getNum2());
    }
}