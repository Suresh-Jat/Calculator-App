package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaring all buttons and textViews and variables
    TextView resultTv, solutionTv;
    Button btnClearAll, btnPercentage, btnBackspace, btnDivide, btnAdd, btnSubtract, btnEquals, btnMultiply;
    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnDoubleZero, btnZero, btnPoint;
    Boolean clearScreen = false;
    StringBuilder currentInput = new StringBuilder();
    Stack<Double> operandStack = new Stack<>();
    Stack<Character> operatorStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieving values from each button and textView

        resultTv = findViewById(R.id.resultTv);
        solutionTv = findViewById(R.id.solutionTv);

        assignId(btnClearAll, R.id.btnClearAll);
        assignId(btnPercentage, R.id.btnPercentage);
        assignId(btnBackspace, R.id.btnBackspace);
        assignId(btnDivide, R.id.btnDivide);
        assignId(btnAdd, R.id.btnAdd);
        assignId(btnSubtract, R.id.btnSubtract);
        assignId(btnEquals, R.id.btnEquals);
        assignId(btnMultiply, R.id.btnMultiply);
        assignId(btnOne, R.id.btnOne);
        assignId(btnTwo, R.id.btnTwo);
        assignId(btnThree, R.id.btnThree);
        assignId(btnFour, R.id.btnFour);
        assignId(btnFive, R.id.btnFive);
        assignId(btnSix, R.id.btnSix);
        assignId(btnSeven, R.id.btnSeven);
        assignId(btnEight, R.id.btnEight);
        assignId(btnNine, R.id.btnNine);
        assignId(btnDoubleZero, R.id.btnDoubleZero);
        assignId(btnZero, R.id.btnZero);
        assignId(btnPoint, R.id.btnPoint);


    }

    // Function for assigning ids
    void assignId(Button btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        String btnText = btn.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();


        //Conditions AC, C and '='
        if (btnText.equals("=")) {
            //    solutionTv.setText(resultTv.getText());
            // Call the solution condition and setText to resultTv
            if (!dataToCalculate.equals("")) {
                clearScreen = true;
                performCalculation(dataToCalculate);
            }
            if (clearScreen) {
                solutionTv.setText("");
            }
            return;
        }

        if (btnText.equals("AC")) {
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }

        if (btnText.equals("⌫")) {
            if (!dataToCalculate.equals("")) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            } else {
                return;
            }
        } else {
            dataToCalculate = dataToCalculate + btnText;
        }

        solutionTv.setText(dataToCalculate);
    }

    private void performCalculation(String expression){

        String[] tokens = expression.split("[+\\-x/%]");
        if (tokens.length == 2) {
            try {
                double operand1 = Double.parseDouble(tokens[0]);
                double operand2 = Double.parseDouble(tokens[1]);
                //                double operand3 = Double.parseDouble(tokens[2]);
                //                double operand4 = Double.parseDouble(tokens[3]);
                //                double operand5 = Double.parseDouble(tokens[4]);
                double result=0.0;
                switch (expression.charAt(tokens[0].length())) {

                    case '+':
                        result = operand1 + operand2;
                        break;
                    case '-':
                        result = operand1 - operand2;
                        break;
                    case '/':
                        if (operand2 != 0) {
                            result = operand1 / operand2;
                        } else {
                            // Handle division by zero error
                            resultTv.setText(getText(R.string.error_message));
                            return;
                        }
                        break;
                    case 'x':
                        result = operand1 * operand2;
                        break;
                    case '%':
                        result = operand1 * (operand2 / 100);
                        break;
                    default:
                        resultTv.setText(getText(R.string.error_message));
                }
                if (result == (int) result) {
                    // If it's a whole number, display it without the decimal part
                    resultTv.setText(String.valueOf((int) result));
                } else {
                    // If it has a decimal part, display it as it is
                    String formattedResult = String.format(Locale.US, "%.2f", result);
                    resultTv.setText(String.valueOf(formattedResult));
                }
            } catch (NumberFormatException e) {
                // Handle invalid numeric format error
                resultTv.setText(getText(R.string.error_message));
            }
        } else {
            resultTv.setText(getText(R.string.error_message));
        }
    }}





/*==========================================================================//
        switch (btnText) {
            case "=":
                handleEquals();
                break;
            case "⌫":
                handleBackspace();
                break;
            case "AC":
                clearAll();
                break;
            default:
                handleInput(btnText);
                break;
        }
    }

    private void handleInput(String input) {
        currentInput.append(input);
        updateExpression();
    }

    private void updateExpression() {
        solutionTv.setText(currentInput.toString());
    }

    // Function to clear all data on click "AC"
    private void clearAll() {
        currentInput.setLength(0);
        operandStack.clear();
        operatorStack.clear();
        updateExpression();
    }

    private void handleBackspace() {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            updateExpression();
        }
    }
    private void handleEquals() {
        if (currentInput.length() > 0) {
            processInput();
            evaluateExpression();
            updateResult();
            clearAll();
        }
    }

    private void processInput() {
        try {
            double operand = Double.parseDouble(currentInput.toString());
            operandStack.push(operand);
            currentInput.setLength(0);
        } catch (NumberFormatException e) {
            // Handle invalid input
            resultTv.setText(R.string.error_message);
            clearAll();
        }
    }

    private void evaluateExpression() {
        while (!operatorStack.isEmpty()) {
            performOperation();
        }
    }


    private void performOperation() {
        char operator = operatorStack.pop();
        if (operandStack.size() < 2) {
            resultTv.setText(R.string.error_message);
            clearAll();
            return;
        }

        double operand2 = operandStack.pop();
        double operand1 = operandStack.pop();
        double result = 0.0;


        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                if (operand2 != 0) {
                    result = operand1 / operand2;
                } else {
                    // Handle division by zero
                    resultTv.setText(R.string.error_message);
                    clearAll();
                    return;
                }
            case '%':
                result = operand1 * (operand2 / 100);
                return;
            default:
                resultTv.setText(R.string.error_message);
                clearAll();
        }

        operandStack.push(result);

    }

    private void updateResult(){
        if (!operandStack.isEmpty()){
            double result=operandStack.pop();
            resultTv.setText(String.valueOf(result));
        }
    }
//======================================================================*/






 /*
//    // Function to remove the last character from the expression
//    private void removeLastCharacter() {
//        String dataToCalculate = solutionTv.getText().toString();
//        if (!dataToCalculate.isEmpty()) {
//            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
//            solutionTv.setText(dataToCalculate);
//        }
//
//        // Removing last operand or operator
//        if (dataToCalculate.isEmpty() || isOperator(dataToCalculate.charAt(dataToCalculate.length() - 1))) {
//            if (!operators.isEmpty()) {
//                operators.remove(operators.size() - 1);
//            } else {
//                if (!operands.isEmpty()) {
//                    operands.remove(operands.size() - 1);
//                }
//            }
//        }
//    }
//
//    private boolean isOperator(char c) {
//        return c == '+' || c == '-' || c == 'x' || c == '/' || c == '%';
//    }
//
//    // Function to add the last operand to the list
//    private void addOperand(String operand) {
//        try {
//            double value = Double.parseDouble(operand);
//            operands.add(value);
//        } catch (NumberFormatException e) {
//            // Handle invalid numeric format error
//            resultTv.setText(R.string.error_message);
//        }
//    }
//
//    private void performCalculation(String expression) {
//        // Splitting the expression into operands and operator
//
//        try {
//
//            double result = 0.0;
//
////        String[] tokens = expression.split("[+\\-x/%]");
//
//            addOperand(solutionTv.getText().toString());
//
//            for (int i = 0; i < operators.size(); i++) {
//                char operator = operators.get(i);
//                double operand = operands.get(i + 1);
//
//                switch (operator) {
//                    case '+':
//                        result += operand;
//                        break;
//                    case '-':
//                        result -= operand;
//                        break;
//                    case 'x':
//                        result *= operand;
//                        break;
//                    case '/':
//                        if (operand != 0) {
//                            result /= operand;
//                        } else {
//                            resultTv.setText(R.string.error_message);
//                            return;
//                        }
//                        break;
//                    case '%':
//                        result = result * (operand / 100);
//                        break;
//                    default:
//                        resultTv.setText(R.string.error_message);
//                        break;
//                }
//            }
//
//            if (result == (int) result) {
//                // If it's a whole number, display it without the decimal part
//                resultTv.setText(String.valueOf((int) result));
//            } else {
//                // If it has a decimal part, display it as it is
//                String formattedResult = String.format(Locale.US, "%.2f", result);
//                resultTv.setText(String.valueOf(formattedResult));
//            }
//
//        } catch (Exception e) {
//            resultTv.setText(R.string.error_message);
//        }
*/

    //===========================================================================//


