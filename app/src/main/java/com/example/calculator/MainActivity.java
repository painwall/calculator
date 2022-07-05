package com.example.calculator;

import android.os.Bundle;
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
        Display display = getWindowManager().getDefaultDisplay();
        final int width = display.getWidth();
        final int height = display.getHeight();

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
    }


    public void onNumberClick(View view){
        Button button = (Button)view;
        try {
            if (field.getText().toString().equals(ERROR_MSG)) { field.setText(""); }
            if (operator == ' ') {
                num1.appendDigit(button.getText().charAt(0));
            }
            else {
                num2.appendDigit(button.getText().charAt(0));
            }
            field.append(button.getText());
        } catch (AppendError ignored) {}
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

    public void onDotClick(View view) {
        try {
            String text = field.getText().toString();
            if (text.length() > 0 && Character.isDigit(text.charAt(text.length() - 1))){
                if (operator == ' ') { num1.appendDot(); }
                else { num2.appendDot(); }
                field.append(".");
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

}