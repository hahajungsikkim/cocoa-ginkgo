package com.kakao.assignment.util;

public class RegexUtil {

    public static String replaceString(String str) {
        String tagPattern = "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>";
        
        String returnStr = str;
        returnStr = returnStr.replaceAll(tagPattern, "");
        returnStr = returnStr.replaceAll("&gt;", ">");
        returnStr = returnStr.replaceAll("&lt;", "<");
        returnStr = returnStr.replaceAll("&quot;", "");
        returnStr = returnStr.replaceAll("&nbsp;", "");
        returnStr = returnStr.replaceAll("&amp;", "&");

        return returnStr;
    }
}
