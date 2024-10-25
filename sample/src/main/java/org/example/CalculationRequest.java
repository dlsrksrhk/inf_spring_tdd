package org.example;

public class CalculationRequest {
    private final long num1;
    private final long num2;
    private final String operation;

    public CalculationRequest(String[] parts) {
        if(parts.length != 3) {
            throw new BadRequestException();
        }
        if(!parts[1].matches("[+\\-*/]")) {
            throw new InvalidOperationException("Invalid operation : " + parts[1]);
        }

        this.num1 = Long.parseLong(parts[0]);
        this.num2 = Long.parseLong(parts[2]);
        this.operation = parts[1];
    }

    public long getNum1() {
        return num1;
    }

    public long getNum2() {
        return num2;
    }

    public String getOperation() {
        return operation;
    }
}
