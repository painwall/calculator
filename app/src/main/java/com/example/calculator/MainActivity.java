package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculator.Number.Number;
import com.example.calculator.Number.MaxNumberError;
import com.example.calculator.Number.AppendError;

// что нужно сделать:
// удаление символов;
// очистка;
// отрицательные числа;
// сделать его красивым;
// проверка на огрмные числа;
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
    }


    public void onNumberClick(View view){
        Button button = (Button)view;
        try {
            if (operator == ' ') { num1.appendDigit(button.getText().charAt(0)); }
            else { num2.appendDigit(button.getText().charAt(0)); }
            field.append(button.getText());
        } catch (AppendError error) { System.out.println("ERROR");}

        System.out.println("test num1: " + num1);
        System.out.println("test num2 " + num2);
    }

    public void onOperatorClick(View view){
        Button button = (Button)view;
        String text = field.getText().toString();
        if (text.length() > 0 && Character.isDigit(text.charAt(text.length() - 1))){
            if (button.getText().equals("=") && operator != ' ') {
                calc();
            } else if (!button.getText().equals("=")){
                if (operator != ' '){
                    calc();
                }
                operator = button.getText().charAt(0);
                field.append(String.valueOf(operator));
            }
        }
    }

    public void onDotClick(View view){
        String text = field.getText().toString();
        if (text.length() > 0 && Character.isDigit(text.charAt(text.length() - 1))) {
            try {
                if (operator == ' ') {
                    num1.appendDot();
                } else {
                    num2.appendDot();
                }
                field.append(".");
            } catch (AppendError ignore) { }
        }
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

            System.out.println("RESULT: " + num1);
            num2.setNumber("0");
            operator = ' ';
            field.setText(num1.toString());
        } catch (MaxNumberError maxNumberError) {
            field.setText(ERROR_MSG);
        }
    }

}