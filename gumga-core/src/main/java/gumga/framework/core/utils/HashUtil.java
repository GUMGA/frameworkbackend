package gumga.framework.core.utils;


public class HashUtil {

    private HashUtil(){}
    public static String generate(String a) {
        int hash = 7;
        for (int i = 0; i < a.length(); i++) {
            hash = hash*31 + a.charAt(i);
        }
        return "param" + (hash < 0 ? hash * -1 : hash) + "";
    }
}
