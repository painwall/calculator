package com.example.calculator;

import java.math.BigDecimal;
import java.math.MathContext;

public class Number  {
    private StringBuilder number = new StringBuilder("0");
    private int MAX_MANTISSA, MAX_NUMBER, LEN_MAX_NUMBER;


    Number (String number, int MAX_MANTISSA, int MAX_NUMBER){
        this.MAX_MANTISSA = MAX_MANTISSA;
        this.MAX_NUMBER = MAX_NUMBER;
        this.LEN_MAX_NUMBER = String.valueOf(MAX_NUMBER).length();
        setNumber(number);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public int getCountVoids(){
        int indexDot = number.indexOf(".");
        if (indexDot < 0) { return 0; }
        else { return MAX_MANTISSA - (number.length() - indexDot); }
    }

    public boolean isDot() {
        return number.indexOf(".") >= 0;
    }

    public void setNumber(String number) {
        System.out.println("NUMBER: " + number);
        this.number.insert(0, number);
        this.number.setLength(number.length());
        System.out.println("NUMBER: " + this.number);
    }


    public void appendDigit(char ch) throws AppendError {
        boolean _isDot = isDot();
        if (ch != '.' && !Character.isDigit(ch)) {throw new RuntimeException("NO_DIGIT");}
        if ((!_isDot && ch == '.') || (!_isDot && ch != '.' && number.length() + 1 <= LEN_MAX_NUMBER) || (_isDot && getCountVoids() > 0)) { number.append(ch); }
        else { throw new AppendError("APPEND_ERROR"); }
    }

    public void appendDot() throws AppendError {
        if (isDot()){
            throw new AppendError("");
        }
    }

    public void deleteLastCharacter(){
        if (number.length() > 0) {
            number.deleteCharAt(number.length() - 1);
        }
    }

    public String toString() {
        int indexDot = number.indexOf(".");
        if (indexDot > 0 && indexDot != number.length() - 1) {
            for (int i = number.length() - 1; i > number.indexOf(".") && number.charAt(i) == '0'; i--) {
                number.deleteCharAt(i);
            }
        }
        if (indexDot > 0 && indexDot == number.length() - 1){
            number.deleteCharAt(indexDot);
        }
        return number.toString();
    }

    public void add(Number num) throws MaxNumberError {
        BigDecimal num1 = new BigDecimal(number.toString(), MathContext.DECIMAL32);
        BigDecimal num2 = new BigDecimal(num.toString(), MathContext.DECIMAL32);
        BigDecimal result = num1.add(num2).setScale(MAX_MANTISSA, BigDecimal.ROUND_FLOOR);
        if (checkNumber(result)) {
            throw new MaxNumberError("MAX_NUMBER_ERROR");
        }
        else {
            setNumber(result.toString());
        }
    }

    public void subtract(Number num) throws MaxNumberError {
        BigDecimal num1 = new BigDecimal(number.toString(), MathContext.DECIMAL32);
        BigDecimal num2 = new BigDecimal(num.toString(), MathContext.DECIMAL32);
        BigDecimal result = num1.subtract(num2).setScale(MAX_MANTISSA, BigDecimal.ROUND_FLOOR);
        if (checkNumber(result)) {
            throw new MaxNumberError("MAX_NUMBER_ERROR");
        }
        else {
            setNumber(result.toString());
        }
    }

    public void divide (Number num) throws MaxNumberError {
        BigDecimal num1 = new BigDecimal(number.toString(), MathContext.DECIMAL32);
        BigDecimal num2 = new BigDecimal(num.toString(), MathContext.DECIMAL32);
        BigDecimal result = num1.divide(num2).setScale(MAX_MANTISSA, BigDecimal.ROUND_FLOOR);
        if (checkNumber(result)) {
            throw new MaxNumberError("MAX_NUMBER_ERROR");
        }
        else {
            setNumber(result.toString());
        }
    }

    public void multiply (Number num) throws MaxNumberError{
        BigDecimal num1 = new BigDecimal(number.toString(), MathContext.DECIMAL32);
        BigDecimal num2 = new BigDecimal(num.toString(), MathContext.DECIMAL32);
        BigDecimal result = num1.multiply(num2).setScale(MAX_MANTISSA, BigDecimal.ROUND_FLOOR);
        if (checkNumber(result)) {
            throw new MaxNumberError("MAX_NUMBER_ERROR");
        }
        else {
            setNumber(result.toString());
        }
    }

    public boolean checkNumber(BigDecimal num) {
        return num.compareTo(new BigDecimal(MAX_NUMBER)) > 0;
    }
}


class AppendError extends Exception {
    AppendError(String msg){
        super(msg);
    }
}

class MaxNumberError extends Exception {
    MaxNumberError(String msg) { super(msg); }
}