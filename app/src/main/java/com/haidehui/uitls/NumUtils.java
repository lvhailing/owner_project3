package com.haidehui.uitls;

/**
 * Created by hasee on 2016/11/9.
 */
public class NumUtils {

    public static boolean isTwoDecimal(String d){

        boolean flag = false;
        flag = d.matches("^(?!0+(?:\\.0+)?$)(?:[1-9]\\d*|0)(?:\\.\\d{1,2})?$");

        return flag;

    }

}
