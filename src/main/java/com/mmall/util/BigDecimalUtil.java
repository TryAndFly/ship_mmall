package com.mmall.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
    private BigDecimalUtil(){

    }

    public static BigDecimal add(double x1, double x2){
        BigDecimal b1 = new BigDecimal(Double.toString(x1));
        BigDecimal b2 = new BigDecimal(Double.toString(x2));
        return b1.add(b2);
    }

    public static BigDecimal sub(double x1, double x2){
        BigDecimal b1 = new BigDecimal(Double.toString(x1));
        BigDecimal b2 = new BigDecimal(Double.toString(x2));
        return b1.subtract(b2);
    }

    public static BigDecimal mul(double x1, double x2){
        BigDecimal b1 = new BigDecimal(Double.toString(x1));
        BigDecimal b2 = new BigDecimal(Double.toString(x2));
        return b1.multiply(b2);
    }

    public static BigDecimal div(double x1, double x2){
        BigDecimal b1 = new BigDecimal(Double.toString(x1));
        BigDecimal b2 = new BigDecimal(Double.toString(x2));
        //四舍五入保留两位小数
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
    }






}
