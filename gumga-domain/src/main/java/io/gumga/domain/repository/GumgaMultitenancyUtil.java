package io.gumga.domain.repository;

import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.GumgaMultitenancyPolicy;

public class GumgaMultitenancyUtil {

    public static String getMultitenancyPattern(GumgaMultitenancy tenacy) {
        GumgaMultitenancyPolicy policy = tenacy.policy();
        String oiPattern = GumgaThreadScope.organizationCode.get();
        if (oiPattern == null) {
            oiPattern = "";
        }
        switch (policy) {
            case ORGANIZATIONAL:
                int firstPointPosition = oiPattern.indexOf('.');
                oiPattern = oiPattern.substring(0, firstPointPosition + 1);
                break;
            case TOP_DOWN:
            default:
            //Usa o OI como está no threadscope

        }
        return oiPattern;
    }

}
