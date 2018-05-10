/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api.voice;

import io.gumga.application.nlp.GumgaNLP;
import io.gumga.presentation.CustomGumgaRestTemplate;
import io.gumga.presentation.api.GumgaJsonRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static gumga.framework.core.utils.NumericUtils.unsignedToBytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API para o tratamento da recepção de voz para reconhecimento
 * @author munif
 */
@RestController
@RequestMapping("/api")
public class VoiceReceiverAPI {
    
    private static final Logger log = LoggerFactory.getLogger(VoiceReceiverAPI.class);

    @Autowired(required = false)
    private GumgaNLP gumgaNLP;
    @Autowired(required = false)
    private CustomGumgaRestTemplate gumgaRestTemplate;
    private final String[] CONTEXT = {"Gumga", "Munif"};

    /**
     * Faz a conversão de um objeto InputStream de entrada para String
     * @param is Objeto InputStream
     * @return String com o conteúdo do objeto de entrada
     */
    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Invoca o método de geração de instâncias a partir de um texto de linguagem natural {@link GumgaNLP}
     * Utiliza como verbos chave: "criar, lançar, fazer"
     * @param texto Texto de linguagem natural para análise
     * @return Coleção de objetos a serem instanciados
     */
    @RequestMapping(value = "/nlp", method = RequestMethod.GET)
    public Map nlp(@RequestParam("texto") String texto) {
        Map<String, Object> resposta = new HashMap<>();
        try {
            resposta.put("objects", gumgaNLP.createObjectsFromDocument(texto, "criar,lançar,fazer"));
        } catch (Exception ex) {
            log.error("Problemas no NLP.", ex);
        }
        return resposta;

    }

    /**
     * Recebe um objeto de áudio por requisição e o converte para uma String
     * faz o tratamento dos dados do arquivo de áudio e o envia para análise sintática
     * Invoca o método de geração de instâncias a partir de um texto de linguagem natural {@link GumgaNLP}
     * Utiliza como verbos chave: "criar, lançar, fazer"
     * @param httpRequest Requisição contendo os dados de áudio
     * @return Uma coleção (Map) contendo os objetos da análise sintática
     * @throws IOException
     */
    @RequestMapping(value = "/voice", method = RequestMethod.POST)
    public Map recebeSom(HttpServletRequest httpRequest) throws IOException {
        String som = convertStreamToString(httpRequest.getInputStream());
        log.info("----->" + som);
        Map<String, Object> problemas = new HashMap<>();
        try {
            som = som.replaceFirst("data:audio/wav;base64,", "");
            byte[] decode = Base64.getDecoder().decode(som.substring(0, 512));
            int sampleRate = unsignedToBytes(decode[27]) * 256 * 256 * 256 + unsignedToBytes(decode[26]) * 256 * 256 + unsignedToBytes(decode[25]) * 256 + unsignedToBytes(decode[24]) * 1;

            RestTemplate restTemplate = new GumgaJsonRestTemplate();
            restTemplate = gumgaRestTemplate != null ? gumgaRestTemplate.getRestTemplate(restTemplate) : restTemplate;
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
