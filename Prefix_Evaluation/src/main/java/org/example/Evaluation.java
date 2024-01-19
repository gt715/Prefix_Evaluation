package org.example;

import java.util.Stack;

public class Evaluation {

    // to convert a prefix expression to an infix expression
    public static String getInfix(String prefix) {
        String[] values = prefix.split(" ");
        Stack<String> stack = new Stack<>();
        int len = values.length;
        for(int i = len - 1; i >= 0; i--) {
            if(isOperator(values[i])) {
                String s1 = stack.pop();
                String s2 = stack.pop();
                stack.push("(" + s1 + " " + values[i] + " " + s2 + ")");
            } else {
                stack.push(values[i]);
            }
        }
        return stack.pop();
    }

    //  to evaluate a prefix expression
    public static double evaluatePrefix(String prefix) {
        String[] values = prefix.split(" ");
        Stack<Double> stack = new Stack<>();
        int len = values.length;
        for(int i = len - 1; i >= 0; i--) {
            if(isOperator(values[i])) {
                double operand1 = stack.pop();
                double operand2 = stack.pop();
                stack.push(calculate(values[i], operand1, operand2));
            } else {
                stack.push(Double.parseDouble(values[i]));
            }
        }
        return stack.pop();
    }

    //  to calculate the result of an operation
    private static double calculate(String operator, double operand1, double operand2) {
        if (operand2 == 0 && operator.equals("/"))
            throw new ArithmeticException("Can't divide by zero");
        return switch(operator) {
            case "+" -> operand1 + operand2;
            case "-" -> operand1 - operand2;
            case "*" -> operand1 * operand2;
            case "/" -> operand1 / operand2;
            case "^" ->  Math.pow(operand1, operand2);
            default -> 0;
        };
    }

    // to check if a string is an operator
    private static boolean isOperator(String s) {
        return s.matches("[+/*\\-^]") ;
    }

}
