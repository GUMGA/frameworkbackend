package io.gumga.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Classe para conversão de LocalDate para Date e vice-versa
 */
@Converter(autoApply = true)
public class GumgaLocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    /**
     * Converte LocalDate para Date
     * @param localDate Data
     * @return Data Convertida
     */
    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {

        return localDate == null ? null : Date.valueOf(localDate);
    }

    /**
     * Converte Date para LocalDate
     * @param date Data
     * @return Data Convertida
     */
    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        return date == null ? null : date.toLocalDate();
    }
}
