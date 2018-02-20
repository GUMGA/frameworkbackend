package io.gumga.domain;

import io.gumga.domain.shared.MaximumSharesExceededException;

/**
 * Created by felipesabadinifacina on 07/10/17.
 */
class GumgaStringList {

    public static String add(String base, String value, int max) {
        if(base == null) {
            base = "";
        }
        if (!contains(base, value)) {
            String toReturn = base + value + ",";
//            if (toReturn.length() > max) {
//                throw new MaximumSharesExceededException("Capacidade de compartilhamentos excedida.");
//            }
            return toReturn;
        }
        return base;
    }

    public static String remove(String base, String value) {
        return base.replaceAll("," + value + ",", ",");
    }

    public static String removeAll() {
        return ",";
    }

    private static boolean contains(String base, String value) {
        return base.contains("," + value + ",");
    }

}
