package org.assignment;

import java.util.Stack;

public class Evaluation {

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

    public static int evaluatePrefix(String prefix) {
        String[] values = prefix.split(" ");
        Stack<Integer> stack = new Stack<>();
        int len = values.length;
        for(int i = len - 1; i >= 0; i--) {
            if(isOperator(values[i])) {
                int operand1 = stack.pop();
                int operand2 = stack.pop();
                stack.push(calculate(values[i], operand1, operand2));
            } else {
                stack.push(Integer.parseInt(values[i]));
            }
        }
        return stack.pop();
    }

    private static int calculate(String operator, int operand1, int operand2) {
        if (operand2 == 0 && operator.equals("/"))
            throw new ArithmeticException("Can't divide by zero");
        return switch(operator) {
            case "+" -> operand1 + operand2;
            case "-" -> operand1 - operand2;
            case "*" -> operand1 * operand2;
            case "/" -> operand1 / operand2;
            case "^" -> (int) Math.pow(operand1, operand2);
            default -> 0;
        };
    }

    private static boolean isOperator(String s) {
        return s.matches("[+/*\\-^]");
    }
}
