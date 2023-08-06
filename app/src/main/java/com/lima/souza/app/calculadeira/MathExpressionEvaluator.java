package com.lima.souza.app.calculadeira;

import android.util.Log;

import java.util.Stack;

public class MathExpressionEvaluator {

    public static double evaluateMathExpression(String expression) {
        Log.i("evaluateMathExpression", expression);
        String postfix = infixToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    private static String infixToPostfix(String infix) {
        Log.i("infixToPostfix", infix);
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();

        String[] tokens = infix.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)|(?<=\\()|(?=\\()|(?<=\\))|(?=\\)|\\s+)");
        for (String token : tokens) {
            char c = token.charAt(0);
            if (isNumber(token)) {
                postfix.append(token).append(" ");
            } else if (isOperator(c)) {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(' && precedence(c) <= precedence(operatorStack.peek())) {
                    postfix.append(operatorStack.pop()).append(" ");
                }
                operatorStack.push(c);
            } else if (c == '(') {
                operatorStack.push(c);
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    postfix.append(operatorStack.pop()).append(" ");
                }
                operatorStack.pop(); // Pop the '('
            } else {
                throw new IllegalArgumentException("Invalid character: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() == '(') {
                throw new IllegalArgumentException("Mismatched parentheses in the expression: " + infix);
            }
            postfix.append(operatorStack.pop()).append(" ");
        }

        Log.i("infixToPostfix - return", postfix.toString().trim());
        return postfix.toString().trim();
    }

    private static double evaluatePostfix(String postfix) {
        Stack<Double> operandStack = new Stack<>();

        String[] tokens = postfix.split("\\s+");
        for (String token : tokens) {
            if (isNumber(token)) {
                operandStack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                if (operandStack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression: " + postfix);
                }
                double b = operandStack.pop();
                double a = operandStack.pop();
                double result = performOperation(a, b, token.charAt(0));
                operandStack.push(result);
            } else {
                throw new IllegalArgumentException("Invalid character: " + token);
            }
        }

        if (operandStack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: " + postfix);
        }

        return operandStack.pop();
    }

    private static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static double performOperation(double a, double b, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case 'x':
                return a * b;
            case '/':
                return a / b;
            case '%':
                return a % b;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == 'x' || c == '/' || c == '%';
    }

    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case 'x':
            case '/':
            case '%':
                return 2;
            default:
                return 0;
        }
    }

    private static boolean isValidFloatingPointNumber(String number) {
        return number.matches("[+-]?\\d*\\.?\\d+");
    }
}
