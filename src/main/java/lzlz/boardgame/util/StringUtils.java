package lzlz.boardgame.util;

public class StringUtils {
    /**
     * 过滤html标签
     */
    public static String filterHTML(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("<(S*?)[^>]*>.*?|<.*? />", "");

    }

    public static String maxLength(String text,int maxLength) {
        if (text == null) {
            return null;
        }
        int length = text.length();
        return text.substring(0,maxLength>length?length:maxLength);

    }

    /**
     * 过滤制表、换行符、空格
     */
    public static String filterBlank(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("\\s|\\t|\\r|\\n", "");
    }
}
