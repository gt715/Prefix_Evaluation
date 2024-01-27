package org.assignment;

public class Main {
    public static void main(String[] args) {

        String prefix = Prefix.getPrefix("file.xml");//dont forget to change the path from the resources to get differnt output
        if (prefix != null) {
            System.out.println("The prefix is:" + prefix);
            String infix = Evaluation.getInfix(prefix);
            int result = Evaluation.evaluatePrefix(prefix);
            System.out.println("This is the infix: " + infix);
            System.out.println("This is the value: " + result);
        }

    }
}
