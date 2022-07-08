package com.example.calculator.Number;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Number  {
    private StringBuilder number = new StringBuilder("0");
    private int MAX_MANTISSA;
    private BigDecimal MAX_NUMBER, MIN_NUMBER;


    public Number(String number, int MAX_MANTISSA, int MAX_NUMBER, int MIN_NUMBER){
        this.MAX_MANTISSA = MAX_MANTISSA;
        this.MAX_NUMBER = new BigDecimal(MAX_NUMBER);
        this.MIN_NUMBER = new BigDecimal(MIN_NUMBER);
        setNumber(number);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public int getCountVoids(){
        int indexDot = number.indexOf(".");
        if (indexDot < 0) { return 0; }
        else { return MAX_MANTISSA - (number.length() - indexDot - 1); }
    }

    public boolean isDot() {
        return number.indexOf(".") >= 0;
    }

    public void setNumber(int number) {
        String strNumber = String.valueOf(number);
        this.number.insert(0, strNumber);
        this.number.setLength(strNumber.length());
    }

    public void setNumber(String number) {
        this.number.insert(0, number);
        this.number.setLength(number.length());
    }


    public void appendDigit(char ch) throws AppendError {
        boolean _isDot = isDot();
        if (!Character.isDigit(ch)) { throw new RuntimeException("NO_DIGIT"); }
        if ((!_isDot && checkNumber(new BigDecimal(number + Character.toString(ch)))) || (_isDot && getCountVoids() > 0)) { number.append(ch); }
        else { throw new AppendError("APPEND_ERROR"); }
    }

    public void appendDot() throws AppendError {
        if (isDot()){ throw new AppendError("ERROR DOT"); }
        else { number.append('.'); }
    }

    public void deleteLastCharacter() {
        if (number.length() > 0) { number.deleteCharAt(number.length() - 1); }
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
        BigDecimal result = num1.add(num2).setScale(MAX_MANTISSA, RoundingMode.FLOOR);
        if (checkNumber(result)) {
            setNumber(result.toString());
        }
        else {
            throw new MaxNumberError("MAX_NUMBER_ERROR");
        }
    }

    public void subtract(Number num) throws MaxNumberError {
        BigDecimal num1 = new BigDecimal(number.toString(), MathContext.DECIMAL32);
        BigDecimal num2 = new BigDecimal(num.toString(), MathContext.DECIMAL32);
        BigDecimal result = num1.subtract(num2).setScale(MAX_MANTISSA, RoundingMode.FLOOR);
        if (checkNumber(result)) {
            setNumber(result.toString());
        }
        else {
            throw new MaxNumberError("MAX_NUMBER_ERROR");
        }
    }

    public void divide (Number num) throws MaxNumberError {
        BigDecimal num1 = new BigDecimal(number.toString(), MathContext.DECIMAL32);
        BigDecimal num2 = new BigDecimal(num.toString(), MathContext.DECIMAL32);
        BigDecimal result = num1.divide(num2).setScale(MAX_MANTISSA, RoundingMode.FLOOR);
        if (checkNumber(result)) {
            setNumber(result.toString());
        }
        else {
            throw new MaxNumberError("MAX_NUMBER_ERROR");
        }
    }

    public void multiply (Number num) throws MaxNumberError{
        BigDecimal num1 = new BigDecimal(number.toString(), MathContext.DECIMAL32);
        BigDecimal num2 = new BigDecimal(num.toString(), MathContext.DECIMAL32);
        BigDecimal result = num1.multiply(num2).setScale(MAX_MANTISSA, RoundingMode.FLOOR);
        if (checkNumber(result)) {
            setNumber(result.toString());
        }
        else {
            throw new MaxNumberError("MAX_NUMBER_ERROR");
        }
    }

    public boolean checkNumber(BigDecimal num) {
       return num.compareTo(MAX_NUMBER) <= 0 && num.compareTo(MIN_NUMBER) >= 0;
    }

    public void changeSign() {
        if (number.length() > 0 && !number.equals("0")){
            if (number.charAt(0) == '-'){
                number.deleteCharAt(0);
            }
            else {
                number.insert(0, '-');
            }
        }
    }

    public boolean isNegative() {
        return number.length() > 0 && number.charAt(0) == '-';
    }

}




