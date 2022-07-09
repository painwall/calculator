package com.example.calculator;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Space;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculator.Number.Number;
import com.example.calculator.Number.MaxNumberError;
import com.example.calculator.Number.AppendError;

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


//        GridLayout layout_width_buttons = findViewById(R.id.layout_with_buttons);
//        double spaces_height = (width - (field_num1.getHeight() + field_num2.getHeight() + layout_width_buttons.getHeight())) / 2.;
//        Space space_top = findViewById(R.id.space_top);
//        space_top.setHeigth();
//        Space space_space_bottom = findViewById(R.id.space_bottom);
    }

    protected void setDefaultInterface() {
        field.setText("");
        editFontSize();
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
            field.append(button.getText());
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
            String text = field.getText().toString();
            if (text.length() > 0 && Character.isDigit(text.charAt(text.length() - 1))){
                if (operator == ' ') { num1.appendDot(); }
                else { num2.appendDot(); }
                field.append(".");
                editFontSize();
            }
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
                    num1.divide(num2);
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
        System.out.println("RENDER");
        StringBuilder text = new StringBuilder();
        boolean conditionNum1 = !num1.toString().equals("0");
        boolean conditionOperator = operator != ' ';
        boolean conditionNum2 = !num2.toString().equals("0");

        if (conditionNum1){
            text.append(num1.toString());
        }
        if (conditionNum1 && conditionOperator) {
            text.append(operator);
        }
        if (conditionNum1 && conditionOperator && conditionNum2){
            if (num2.isNegative()) {
                text.append('(');
                text.append(num2);
                text.append(')');
            } else { text.append(num2); }
        }
        field.setText(text.toString());

    }

}