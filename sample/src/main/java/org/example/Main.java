package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CalculationRequestReader calculationRequestReader = new CalculationRequestReader();
        CalculationRequest calculationRequest = calculationRequestReader.read();

        long answer = new Calculator().calculate(calculationRequest.getNum1(), calculationRequest.getOperation(), calculationRequest.getNum2());

        System.out.println(answer);
    }
}