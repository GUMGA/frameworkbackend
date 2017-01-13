/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Quando o formato não é @Temporal(javax.persistence.TemporalType.DATE) e não
 * {@literal @}Temporal(javax.persistence.TemporalType.TIMESTAMP) o framework
 * envia a data em formato simples, não iso, ocasionando problemas. Assim, foi
 * criado um formatador para estes casos sem impactar nos sistemas que já tratam
 * as datas. Para utilizar a formatação, utilizar como no exemplo:<br>
 *
 * {@literal @}JsonSerialize(using = GumgaIsoDataSerializer.class)<br>
 * {@literal @}Temporal(javax.persistence.TemporalType.DATE) private Date
 * viewedAt;<br>
 *
 *
 * @author munif
 */
@Component
public class GumgaIsoDataSerializer extends JsonSerializer<Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
        String formattedDate = dateFormat.format(date);
        gen.writeString(formattedDate);
    }

}
