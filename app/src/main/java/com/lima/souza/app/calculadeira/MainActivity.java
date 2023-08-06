package com.lima.souza.app.calculadeira;

import static com.lima.souza.app.calculadeira.EditTextUtils.getTextSizeInSP;
import static com.lima.souza.app.calculadeira.EditTextUtils.setTextSizeInSP;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView displayTextView;
    private EditText inputText;
    private StringBuilder currentInput = new StringBuilder();
    private HorizontalScrollView scrollView;

    private static final int[] buttonIds = new int[]{
            R.id.btnParentheses,
            R.id.btnDivision,
            R.id.btnMultiplication,
            R.id.btnPercent,
            R.id.btnSubtract,
            R.id.btnPlus,
            R.id.btnDot,
            R.id.btnZero,
            R.id.btnOne,
            R.id.btnTwo,
            R.id.btnThree,
            R.id.btnFour,
            R.id.btnFive,
            R.id.btnSix,
            R.id.btnSeven,
            R.id.btnEight,
            R.id.btnNine
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        setupClickListeners();
    }

    private void initComponents() {
        displayTextView = findViewById(R.id.displayTextView);
        scrollView = findViewById(R.id.horizontalScrollResult);
        inputText = findViewById(R.id.inputValue);
        inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        configInputChangeDetection();
    }

    private void configInputChangeDetection() {
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adjustTextSize(inputText);
                adjustTextSize(displayTextView);
            }
        });
    }

    private void setupClickListeners() {
        for (int buttonId : buttonIds) {
            View view = findViewById(buttonId);
            view.setOnClickListener(v -> {
                String input = v.getTag().toString();
                onInputDigit(input);
            });
        }
    }

    public String formatInputEnter(String inputValue, char newDigit) {
        if ((newDigit == '+' || newDigit == 'x' || newDigit == '/' || newDigit == '%') && inputValue.isEmpty()) {
            // Rule: If newDigit is an operator (+, x, /, %) and inputValue is empty, newDigit cannot be concatenated.
            return formatNumbersInput(inputValue);
        } else if (newDigit == '(') {
            // Rule: If newDigit is '(', and inputValue is empty, concat newDigit to inputValue.
            if (inputValue.isEmpty()) {
                return formatNumbersInput(inputValue + newDigit);
            } else {
                // Rule: If inputValue is not empty and the last character is a number, concat ')' instead of '('.
                char lastChar = inputValue.charAt(inputValue.length() - 1);
                int countOpenParentheses = countOccurrences(inputValue, '(');
                int countCloseParentheses = countOccurrences(inputValue, ')');

                if (Character.isDigit(lastChar)) {
                    return formatNumbersInput(inputValue + ')');
                } else if (lastChar == ')' && countOpenParentheses > countCloseParentheses) {
                    return formatNumbersInput(inputValue + ')');
                } else {
                    return formatNumbersInput(inputValue + newDigit);
                }
            }
        } else if (newDigit == '-') {
            // Rule: If newDigit is '-', and inputValue is empty, concat newDigit to inputValue.
            if (inputValue.isEmpty()) {
                return formatNumbersInput(inputValue + newDigit);
            } else {
                // Rule: If last character of inputValue is equal to '-', do not concat newDigit.
                char lastChar = inputValue.charAt(inputValue.length() - 1);
                if (lastChar == '-') {
                    return formatNumbersInput(inputValue);
                } else {
                    return formatNumbersInput(inputValue + newDigit);
                }
            }
        } else {
            // Rule: If newDigit is an operator (+, x, /, %) and the last character of inputValue
            // is also an operator (+, x, /, %), replace the last character by newDigit.
            if (!inputValue.isEmpty()) {
                char lastChar = inputValue.charAt(inputValue.length() - 1);
                if ("x%/+".contains(String.valueOf(lastChar)) && "x%/+".contains(String.valueOf(newDigit))) {
                    inputValue = (inputValue.substring(0, inputValue.length() - 1));
                    inputValue += newDigit;
                    return formatNumbersInput(inputValue);
                }
            }
            return formatNumbersInput(inputValue + newDigit);
        }
    }

    private int countOccurrences(String input, char target) {
        int count = 0;
        for (char c : input.toCharArray()) {
            if (c == target) {
                count++;
            }
        }
        return count;
    }


    public static String formatNumbersInput(String input) {
        StringBuilder formattedString = new StringBuilder();
        int count = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (Character.isDigit(c)) {
                if (count == 3) {
                    formattedString.append(".");
                    count = 0;
                }
                count++;
            } else {
                count = 0;
            }

            formattedString.append(c);
        }
        return formattedString.toString();
    }


    private void adjustTextSize(TextView textView) {
        TextPaint textPaint = new TextPaint();
        textPaint.set(textView.getPaint());

        String text = textView.getText().toString();
        float viewWidth;

        if (textView instanceof EditText) {
            viewWidth = textView.getWidth() - textView.getPaddingLeft() - textView.getPaddingRight();
        } else {
            viewWidth = scrollView.getWidth() - scrollView.getPaddingLeft() - scrollView.getPaddingRight();
        }

        float textWidth = Layout.getDesiredWidth(text, textPaint);
        float textSizeInSP = getTextSizeInSP(textView);
        float newTextSizeInSP = textSizeInSP - 10;
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();

        if (textWidth > viewWidth) {
            if (newTextSizeInSP < 40) {
                textView.setSingleLine(true);
                textView.setMaxLines(1);
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;

            } else {
                setTextSizeInSP(textView, newTextSizeInSP);
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                adjustTextSize(textView);
            }
        }
        textView.setLayoutParams(layoutParams);
    }


    // Button click event for numbers and operators
    public void onInputDigit(String input) {
        char newDigit = input.charAt(0);
        input = formatInputEnter(currentInput.toString(), input.charAt(0));
        if (!input.isEmpty()) {
            currentInput.setLength(0);
            currentInput.append(input.replace(".", ""));
        }
        inputText.setText(input);
        Log.i("newDigit", String.valueOf(newDigit));
        calculate(currentInput.toString());
    }


    // Button click event for the equal (=) button
    public void onEqualClick(View view) {
        calculate(currentInput.toString());
    }

    // Button click event for the clear (C) button
    public void onClearClick(View view) {
        currentInput.setLength(0);
        inputText.setText("");
        calculate("0");
    }

    public void deleteLastCharacter(View view) {
        if (currentInput.length() > 0) {
            String cuttedText = currentInput.substring(0, currentInput.length() - 1);
            currentInput.setLength(0);
            currentInput.append(cuttedText);
            cuttedText = formatNumbersInput(cuttedText);
            inputText.setText(cuttedText);
            if (!cuttedText.isEmpty()) {
                calculate(cuttedText);
            } else {
                calculate("0");
            }
        }
    }


    private void calculate(String text) {
        char lastCharacter = text.charAt(text.length() - 1);
        if (Character.isDigit(lastCharacter) || lastCharacter == ')') {
            try{
                double result = MathExpressionEvaluator.evaluateMathExpression(text);
                displayTextView.setText(String.valueOf(result));
            }catch(IllegalArgumentException e){}
        }
    }
}