package sausure.io.personallibrary.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a String Util
 */
public class StringUtil {
    /**
     * is null or its length is 0
     *
     * @param str
     * @return
     *      if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(String str){
        return (null == str || 0 == str.length());
    }

    public static boolean isNull(String str){
        if(isEmpty(str))
            return true;
        else
            return ("null".equals(str) || "NULL".equals(str));
    }
    /**
     * is null or is made by spaces
     * @param str
     * @return
     *      if str is null or its size is 0 or is made by spaces, return true, else return false
     * */
    public static boolean isBlank(String str){
        return (null == str || 0 == str.trim().length());
    }

    public static String noNull(String str){
        if(isBlank(str))
            return "";
        else
            return str;
    }


    /**
     * is null or is made by spaces or is 0 price
     * @param price
     * @return
     *      if price is null or its size is 0 or is made by spaces or is 0(0.000) price, return true, else return false
     * */
    public static boolean isPriceNull(String price){
        String zeroPriceRgx = "^[0]+\\.?[0]*$";
        if(isBlank(price)) {
            return true;
        }else{
            Pattern pattern = Pattern.compile(zeroPriceRgx);
            Matcher matcher = pattern.matcher(price);
            return matcher.find();
        }

    }
}
