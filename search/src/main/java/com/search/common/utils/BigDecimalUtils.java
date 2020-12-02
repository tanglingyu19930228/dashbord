package com.search.common.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 */
public class BigDecimalUtils {
    /** 默认小数点后两位 */
    private static final Integer BASIC_ROUND = 2;

    private static final String REG = "-?[0-9]+(\\.[0-9]+)?";

    /**
     * 校验 @see #checkParams(Object v1, Object v2)
     * 两数相加
     * @param v1 第一个参数
     * @param v2 第二个参数
     * @return 返回String 字符串
     */
    public static String add(Object v1,Object v2){
        checkParams(v1,v2);
        BigDecimal add = new BigDecimal(String.valueOf(v1)).add(new BigDecimal(String.valueOf(v2)));
        return add.toPlainString();
    }

    /**
     * 校验 @see #checkParams(Object v1, Object v2)
     * 两数相减
     * @param v1 第一个参数
     * @param v2 第二个参数
     * @return 返回String 字符串
     */
    public static String reduce(Object v1,Object v2){
        checkParams(v1,v2);
        BigDecimal subtract = new BigDecimal(String.valueOf(v1)).subtract(new BigDecimal(String.valueOf(v2)));
        return subtract.toPlainString();
    }

    /**
     * 两数相乘
     * @param v1 v1
     * @param v2 v2
     * @return v2*v2
     */
    public static String mul(Object v1,Object v2){
        checkParams(v1,v2);
        BigDecimal multiply = new BigDecimal(String.valueOf(v1)).multiply(new BigDecimal(String.valueOf(v2)));
        return multiply.toPlainString();
    }

    private static String divByRound(Object v1,Object v2,Integer round){
        BigDecimal divide = new BigDecimal(String.valueOf(v1)).divide(new BigDecimal(String.valueOf(v2)), round, RoundingMode.HALF_UP);
        return divide.toPlainString();
    }

    /**
     * 两数相除
     * @param v1 除数
     * @param v2 被除数
     * @param round 除后所取精度
     * @return 返回结果
     */
    public static String divRound2(Object v1,Object v2,Integer round){
        checkParams(v1,v2);
        return divByRound(v1,v2,round);
    }
    /**
     * 两数相除 精度取2
     * @param v1 除数
     * @param v2 被除数
     * @return 返回结果
     */
    public static String div(Object v1,Object v2){
        checkParams(v1, v2);
        return divByRound(v1,v2,BASIC_ROUND);
    }

    /**
     * 取整
     * @param v1 值
     * @param round 位数
     * @return 返回结果
     */
    public static String round(Object v1,Integer round){
        checkParams(v1,1);
        BigDecimal round1 = new BigDecimal(String.valueOf(v1));
        BigDecimal round2 = round1.round(new MathContext(round));
        return round2.toPlainString();
    }

    public static boolean compare(Object v1, Object v2){
        checkParams(v1, v2);
        BigDecimal value1 =new BigDecimal(round(new BigDecimal(String.valueOf(v1)),100));
        BigDecimal value2 = new BigDecimal(round(new BigDecimal(String.valueOf(v2)),100));
        return value1.compareTo(value2) > 0;
    }
    public static boolean compareEqualsNumber(Object v1, Object v2){
        checkParams(v1, v2);
        BigDecimal value1 =new BigDecimal(round(new BigDecimal(String.valueOf(v1)),100));
        BigDecimal value2 = new BigDecimal(round(new BigDecimal(String.valueOf(v2)),100));
        return value1.compareTo(value2) == 0;
    }

    private static void checkParams(Object v1, Object v2) {
        if(!(v1 instanceof String)&&!(v1 instanceof Number)){
            throw new IllegalArgumentException("第一个参数传入不正确");
        }
        if(v1 instanceof String){
            String s = String.valueOf(v1);
            Pattern compile = Pattern.compile(REG);
            Matcher matcher = compile.matcher(s);
            if(!matcher.matches()){
                throw new IllegalArgumentException("第一个参数传入的为一个字符串"+s+"，但包含非数字的字符串");
            }
        }
        if(!(v2 instanceof String)&&!(v2 instanceof Number)){
            throw new IllegalArgumentException("第二个参数传入不正确");
        }
        if(v2 instanceof String){
            String s = String.valueOf(v1);
            Pattern compile = Pattern.compile(REG);
            Matcher matcher = compile.matcher(s);
            if(!matcher.matches()){
                throw new IllegalArgumentException("第一个参数传入的为二个字符串"+s+"，但包含非数字的字符串");
            }
        }
    }
}
