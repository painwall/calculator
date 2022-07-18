package com.example.calculator;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculator.Number.Number;
import com.example.calculator.Number.MaxNumberError;
import com.example.calculator.Number.AppendError;
import com.example.calculator.Number.ZeroError;

import java.util.function.Predicate;

public class MainActivity extends AppCompatActivity {
    private TextView field;
    final int MAX_MANTISSA = 5, MAX_NUMBER = 1000000000, MIN_NUMBER = -1000000000;
    Number num1 = new Number("0", MAX_MANTISSA, MAX_NUMBER, MIN_NUMBER),
            num2 = new Number("0", MAX_MANTISSA, MAX_NUMBER, MIN_NUMBER);
    char operator = ' ';
    private final String ERROR_MSG = "ERROR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        field = findViewById(R.id.field);
        ImageButton btn = findViewById(R.id.deleteButton);
        btn.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        setDefaultInterface();
                        setDefaultValues();
                        return true;
                    }
                }
        );

    }

    private void setDefaultInterface() {
        field.setText("");
        editFontSize();
    }

    private void setDefaultValues() {
        num1.setNumber(0);
        num2.setNumber(0);
        operator = ' ';
    }



    public void onNumberClick(View view){
        Button button = (Button)view;
        String text = field.getText().toString();
        try {
            if (text.equals(ERROR_MSG) || text.equals("0")) { field.setText(""); }
            if (operator == ' ') {
                num1.appendDigit(button.getText().charAt(0));
            }
            else {
                num2.appendDigit(button.getText().charAt(0));
            }

            if (text.length() > 0 && text.charAt(text.length() - 1) == ')') {
                renderField();
            }
            else {
                field.append(button.getText());
            }
            editFontSize();
        } catch (AppendError ignored) {}
    }

    public void onOperatorClick(View view){
        Button button = (Button)view;
        String buttonText = button.getText().toString();
        String fieldText = field.getText().toString();
        Predicate<String> conditionDigit = in -> in.length() > 0 && Character.isDigit(in.charAt(in.length() - 1));
        Predicate<String> conditionParenthesis = in -> in.length() > 0 && in.charAt(in.length() - 1) == ')';

        switch (buttonText) {
            case ("="):
                if (conditionDigit.test(fieldText) || conditionParenthesis.test(fieldText)) {
                    calc();
                }
                break;
            case ("±"):
                if (conditionDigit.test(fieldText) || conditionParenthesis.test(fieldText)) {
                    if (operator == ' ') { num1.changeSign(); }
                    else { num2.changeSign(); }
                    renderField();
                }
                break;
            default:
                if (conditionDigit.test(fieldText)) {
                    if (operator != ' ') { calc(); }
                    else {
                        operator = button.getText().charAt(0);
                        field.append(String.valueOf(operator));
                        editFontSize();
                    }
                }
                break;

        }
        System.out.println("OPERATOR: " + num1 + " " + operator + " " + num2);
    }

    public void onDotClick(View view) {
        try {
            StringBuilder text = new StringBuilder(field.getText().toString());
            boolean conditionDigit = text.length() > 0 && Character.isDigit(text.charAt(text.length() - 1));
            boolean conditionParenthesis = text.length() > 0 && text.charAt(text.length() - 1) == ')';
            if (conditionDigit || conditionParenthesis){
                if (operator == ' ') { num1.appendDot(); }
                else { num2.appendDot(); }

            }

            if (conditionDigit) {
                field.append(".");
            }
            else if (conditionParenthesis) {
                text.deleteCharAt(text.length() - 1);
                text.append('.');
                text.append(')');
            }
            editFontSize();


        } catch (AppendError ignore) { }

    }



    private void calc () {
        try {
            switch (operator){
                case ('+'):
                    num1.add(num2);
                    break;
                case ('-'):
                    num1.subtract(num2);
                    break;
                case ('*'):
                    num1.multiply(num2);
                    break;
                case ('/'):
                    try {
                        num1.divide(num2);
                    } catch (ZeroError e) {
                        setDefaultInterface();
                        field.setText(ERROR_MSG);
                        num1.setNumber(0);
                        num2.setNumber(0);
                        operator = ' ';
                        return;
                    }
                    break;
                default:
                    return;
            }

            setDefaultInterface();
            num2.setNumber(0);
            operator = ' ';
            field.setText(num1.toString());
        } catch (MaxNumberError maxNumberError) {
            setDefaultInterface();
            field.setText(ERROR_MSG);
            num1.setNumber(0);
            num2.setNumber(0);
            operator = ' ';
        }
    }

    protected void editFontSize() {
        float fontSize = field.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
        int countSymbols = field.getText().length();

        if (countSymbols <= 12 && fontSize != 40) {
            field.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        } else if (countSymbols > 12 && countSymbols <= 17 && fontSize != 30) {
            field.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        } else if (countSymbols > 17 && fontSize != 20) {
            field.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
    }

    protected void renderField() {
        StringBuilder text = new StringBuilder();
        boolean conditionOperator = operator != ' ';

        text.append(num1.toString());
        if (conditionOperator) {
            text.append(operator);
            if (num2.isNegative()) {
                text.append('(');
                text.append(num2);
                text.append(')');
            } else { text.append(num2); }
        }
        field.setText(text.toString());
        editFontSize();

    }

}