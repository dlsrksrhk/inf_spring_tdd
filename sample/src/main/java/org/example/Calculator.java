package org.example;

public class Calculator {
    public long calculate(long num1, String operation, long num2) {
        return switch (operation) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            default -> throw new InvalidOperationException("Invalid operation : " + operation);
        };
    }
}
