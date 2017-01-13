package io.gumga.domain;

import io.gumga.domain.domains.GumgaMoney;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Classe formatadora para moeda
 *
 * @author gyowanny
 */
public class CurrencyFormatter {

    private Locale locale = Locale.getDefault();

    private int decimalPrecision = -1;

    private String symbol;

    private String format;

    private DecimalFormat formatter;

    private static Map<String, String> symbolMap = new HashMap<>();

    public CurrencyFormatter() {
        buildPattern();
    }

    public CurrencyFormatter(int decimalPrecision) {
        this.decimalPrecision = decimalPrecision;
        buildPattern();
    }

    public CurrencyFormatter(Locale locale) {
        this.locale = locale;
        buildPattern();
    }

    public CurrencyFormatter(Locale locale, int decimalPrecision) {
        this.locale = locale;
        this.decimalPrecision = decimalPrecision;
        buildPattern();
    }

    public static void replaceSymbol(String originalSymbol, String replacement) {
        symbolMap.put(originalSymbol, replacement);
    }

    /**
     * Formata o valor como moeda adicionando o Simbolo da mesma.
     *
     * @param value O valor a ser formatado
     * @return Valor formatado como moeda
     */
    public String format(GumgaMoney value) {
        return format(value.getValue(), true);
    }

    /**
     * Formata o valor como moeda adicionando o Simbolo da mesma.
     *
     * @param value O valor a ser formatado
     * @return Valor formatado como moeda
     */
    public String format(BigDecimal value) {
        return format(value, true);
    }

    /**
     * Formata o valor como moeda.
     *
     * @param value O valor a ser formatado
     * @param withSymbol Se deve incluir o simbolo da moeda no valor formatado
     * @return Valor formatado como moeda
     */
    public String format(GumgaMoney value, boolean withSymbol) {
        return format(value.getValue(), withSymbol);
    }

    /**
     * Formata o valor como moeda.
     *
     * @param value O valor a ser formatado
     * @param withSymbol Se deve incluir o simbolo da moeda no valor formatado
     * @return Valor formatado como moeda
     */
    public String format(BigDecimal value, boolean withSymbol) {
        String formattedValue = formatter.format(value);
        return withSymbol ? String.format("%s %s", symbol, formattedValue) : formattedValue;
    }

    protected void buildPattern() {
        Currency currency = Currency.getInstance(locale);
        if (decimalPrecision == -1) {
            decimalPrecision = currency.getDefaultFractionDigits();
        }
        symbol = symbolMap.containsKey(currency.getSymbol()) ? symbolMap.get(currency.getSymbol()) : currency.getSymbol();
        DecimalFormatSymbols symbolsFmt = new DecimalFormatSymbols(locale);
        format = "#,##0." + replicate("0", decimalPrecision);
        formatter = new DecimalFormat(format, symbolsFmt);
    }

    private String replicate(String value, int times) {
        String result = "";
        for (int i = 0; i < times; i++) {
            result = result.concat(value);
        }
        return result;
    }
}
