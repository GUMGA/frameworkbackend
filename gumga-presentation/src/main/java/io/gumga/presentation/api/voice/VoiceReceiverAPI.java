/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api.voice;

import io.gumga.application.nlp.GumgaNLP;
import org.springframework.beans.factory.annotation.Autowired;

import static gumga.framework.core.utils.NumericUtils.unsignedToBytes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/api")
public class VoiceReceiverAPI {

    @Autowired(required = false)
    private GumgaNLP gumgaNLP;

    private final String[] CONTEXT = {"Gumga", "Munif"};

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @RequestMapping(value = "/nlp", method = RequestMethod.GET)
    public Map nlp(@RequestParam("texto") String texto) {
        Map<String, Object> resposta = new HashMap<>();
        try {
            resposta.put("objects", gumgaNLP.createObjectsFromDocument(texto, "criar,lançar,fazer"));
        } catch (Exception ex) {
            Logger.getLogger(VoiceReceiverAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resposta;

    }

    @RequestMapping(value = "/voice", method = RequestMethod.POST)
    public Map recebeSom(HttpServletRequest httpRequest) throws IOException {
        String som = convertStreamToString(httpRequest.getInputStream());
        System.out.println("----->" + som);
        Map<String, Object> problemas = new HashMap<>();
        try {
            som = som.replaceFirst("data:audio/wav;base64,", "");
            byte[] decode = Base64.getDecoder().decode(som.substring(0, 512));
            int sampleRate = unsignedToBytes(decode[27]) * 256 * 256 * 256 + unsignedToBytes(decode[26]) * 256 * 256 + unsignedToBytes(decode[25]) * 256 + unsignedToBytes(decode[24]) * 1;

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> config = new HashMap<>();

            config.put("encoding", "LINEAR16");
            config.put("sampleRate", ""+sampleRate);
            config.put("languageCode", "pt-BR");
            config.put("maxAlternatives", "1");
            config.put("profanityFilter", "false");
            Map<String, Object> context = new HashMap<>();
            context.put("phrases", CONTEXT);
            config.put("speechContext", context); //EXPLORAR DEPOIS COM FRASES PARA "AJUDAR" o reconhecedor
            Map<String, Object> audio = new HashMap<>();
            audio.put("content", som);
            Map<String, Object> request = new HashMap<>();
            request.put("config", config);
            request.put("audio", audio);
            Map resposta = restTemplate.postForObject("https://speech.googleapis.com/v1beta1/speech:syncrecognize?key=AIzaSyC7E4dZ4EneRmSzVMs2qhyJYGoTK49FCYM", request, Map.class);
            List<Object> analiseSintatica = analiseSintatica(resposta);
            resposta.put("objects", analiseSintatica);
            return resposta;
        } catch (Exception ex) {
            problemas.put("exception", ex);
        }
        return problemas;
    }

    private List<Object> analiseSintatica(Map resposta) {
        List<Object> objetos = new ArrayList<>();
        try {
            List<Map> resultados = (List<Map>) resposta.get("results");
            for (Map resultado : resultados) {
                List<Map> alternativas = (List<Map>) resultado.get("alternatives");
                for (Map alternativa : alternativas) {
                    String texto = alternativa.get("transcript").toString();
                    objetos.add(gumgaNLP.createObjectsFromDocument(texto, "criar,lançar,fazer"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return objetos;

    }

}
