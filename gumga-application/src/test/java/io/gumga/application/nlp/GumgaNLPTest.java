package io.gumga.application.nlp;

import io.gumga.application.SpringConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class GumgaNLPTest {

    @Autowired
    private GumgaNLP gnlp;


    @Test
    public void testCceateObjectsFromDocument() throws Exception {
        if (gnlp == null){
            return;
        }
        List objects = gnlp.createObjectsFromDocument("crie uma casa de cor amarela e valor 50000"
                + " e faça uma conta com para o cliente João da Silva com valor de 532.27"
                + " e lance uma nota para o cliente Munif Gebar Junior com valor de 543.89",
                "criar,lançar,fazer");
        System.out.println(objects);
        assertEquals(3, objects.size());
    }

}
