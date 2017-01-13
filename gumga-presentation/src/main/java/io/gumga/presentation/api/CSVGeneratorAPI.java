/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.application.GumgaService;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import javax.persistence.Version;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author munif
 */
public interface CSVGeneratorAPI {

    final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    final String CSV_SEPARATOR = ";";
    final String CSV_LINE_DELIMITER = "\r\n";
    final Logger log = LoggerFactory.getLogger(CSVGeneratorAPI.class);

    GumgaService getGumgaService();

    @org.springframework.transaction.annotation.Transactional
    @ApiOperation(value = "csv", notes = "Gera resultado da pesquisa em um arquivo CSV.")
    @RequestMapping(value = "/csv", method = RequestMethod.GET)
    @ResponseBody
    default void geraCSV(HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        QueryObject qo = new QueryObject();
        SearchResult pesquisa = getGumgaService().pesquisa(qo);
        if (pesquisa.getValues().isEmpty()) {
            response.getWriter().write("NO DATA");
            return;
        }
        sb.append(classToCsvTitle(pesquisa.getValues().get(0).getClass()));
        for (Object obj : pesquisa.getValues()) {
            sb.append(objectToCsvLine(obj));
        }
        response.getWriter().write(sb.toString());
    }

    @Transactional
    @ApiOperation(value = "csvupload", notes = "Faz importação via csv.")
    @RequestMapping(method = RequestMethod.POST, value = "/csvupload")
    default SearchResult<String> csvUpload(@RequestParam MultipartFile csv) throws IOException {
        int numeroLinha = 0;
        Class clazz = getGumgaService().clazz();
        InputStreamReader isr = new InputStreamReader(csv.getInputStream());
        BufferedReader bf = new BufferedReader(isr);
        String linha;
        String[] atributos = bf.readLine().split(CSV_SEPARATOR);
        numeroLinha++;
        Field idField = getIdField(clazz);
        Map<String, String> atributoValor = new HashMap<>();
        Map<String, Field> atributoField = new HashMap<>();
        for (String atributo : atributos) {
            atributoValor.put(atributo, null);
        }
        for (Field f : getAllAtributes(clazz)) {
            f.setAccessible(true);
            atributoField.put(f.getName(), f);
        }
        List<String> problemas = new ArrayList<>();
        while ((linha = bf.readLine()) != null) {
            numeroLinha++;
            String coluna = "";
            if (linha.trim().isEmpty()) {
                continue;
            }
            try {
                String[] valores = linha.split(CSV_SEPARATOR);
                for (int i = 0; i < valores.length; i++) {
                    atributoValor.put(atributos[i], valores[i]);
                }
                Object entidade;
                String stringChave = atributoValor.get(idField.getName());
                if (stringChave != null && !stringChave.trim().isEmpty()) {
                    entidade = getGumgaService().view(new Long(stringChave));
                } else {
                    entidade = clazz.newInstance();
                }
                for (int i = 0; i < atributos.length; i++) {
                    coluna = atributos[i];
                    Field atributo = atributoField.get(coluna);
                    if (coluna.equals(idField.getName())) {
                        continue;
                    }
                    if (coluna.equals("oi")) {
                        continue;
                    }
                    if (atributo.isAnnotationPresent(Version.class)) {
                        continue;
                    }
                    Class type = atributo.getType();
                    String valorString = atributoValor.get(atributos[i]);
                    if (valorString == null || valorString.trim().isEmpty()) {
                        continue;
                    }
                    if (GumgaModel.class.isAssignableFrom(type)) {
                        GumgaModel gm = (GumgaModel) atributo.get(entidade);
                        if (gm == null || !gm.getId().toString().equals(valorString)) {
                            Long valorLong = new Long(valorString);
                            Object entidadeAssociada = getGumgaService().genercView(type, valorLong);
                            atributo.set(entidade, entidadeAssociada);
                        }

                    } else if (type.equals(Date.class)) {
                        Date valorDate = SDF.parse(valorString);
                        atributo.set(entidade, valorDate);
                    } else {
                        Constructor constructorString = type.getConstructor(String.class);
                        Object valorAtributo = constructorString.newInstance(valorString);
                        atributo.set(entidade, valorAtributo);
                    }
                }
                coluna = "ao salvar registro completo";
                getGumgaService().save(entidade);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | ParseException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                problemas.add("Linha:" + numeroLinha + " Coluna:" + coluna + " Problema:" + ex);
            }
        }
        return new SearchResult<>(0, problemas.size(), problemas.size(), problemas);
    }

    @ApiOperation(value = "csvuploadvalidate", notes = "Faz validação da importação via csv.")
    @RequestMapping(method = RequestMethod.POST, value = "/csvuploadvalidate")
    default SearchResult<String> csvUploadValidate(@RequestParam MultipartFile csv) throws IOException {
        int numeroLinha = 0;
        Class clazz = getGumgaService().clazz();
        InputStreamReader isr = new InputStreamReader(csv.getInputStream());
        BufferedReader bf = new BufferedReader(isr);
        String linha;
        String[] atributos = bf.readLine().split(CSV_SEPARATOR);
        numeroLinha++;
        Field idField = getIdField(clazz);
        Map<String, String> atributoValor = new HashMap<>();
        Map<String, Field> atributoField = new HashMap<>();
        for (String atributo : atributos) {
            atributoValor.put(atributo, null);
        }
        for (Field f : getAllAtributes(clazz)) {
            f.setAccessible(true);
            atributoField.put(f.getName(), f);
        }
        List<String> problemas = new ArrayList<>();
        while ((linha = bf.readLine()) != null) {
            numeroLinha++;
            String coluna = "";
            if (linha.trim().isEmpty()) {
                continue;
            }
            try {
                String[] valores = linha.split(CSV_SEPARATOR);
                for (int i = 0; i < valores.length; i++) {
                    atributoValor.put(atributos[i], valores[i]);
                }
                Object entidade;
                String stringChave = atributoValor.get(idField.getName());
                if (stringChave != null && !stringChave.trim().isEmpty()) {
                    entidade = getGumgaService().view(new Long(stringChave));
                } else {
                    entidade = clazz.newInstance();
                }
                for (int i = 0; i < atributos.length; i++) {
                    coluna = atributos[i];
                    Field atributo = atributoField.get(coluna);
                    if (coluna.equals(idField.getName())) {
                        continue;
                    }
                    if (coluna.equals("oi")) {
                        continue;
                    }
                    if (atributo.isAnnotationPresent(Version.class)) {
                        continue;
                    }
                    Class type = atributo.getType();
                    String valorString = atributoValor.get(atributos[i]);
                    if (valorString == null || valorString.trim().isEmpty()) {
                        continue;
                    }
                    if (GumgaModel.class.isAssignableFrom(type)) {
                        GumgaModel gm = (GumgaModel) atributo.get(entidade);
                        if (gm == null || !gm.getId().toString().equals(valorString)) {
                            Long valorLong = new Long(valorString);
                            Object entidadeAssociada = getGumgaService().genercView(type, valorLong);
                            atributo.set(entidade, entidadeAssociada);
                        }

                    } else if (type.equals(Date.class)) {
                        Date valorDate = SDF.parse(valorString);
                        atributo.set(entidade, valorDate);
                    } else {
                        Constructor constructorString = type.getConstructor(String.class);
                        Object valorAtributo = constructorString.newInstance(valorString);
                        atributo.set(entidade, valorAtributo);
                    }
                }
                coluna = "ao salvar registro completo";
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | ParseException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                problemas.add("Linha:" + numeroLinha + " Coluna:" + coluna + " Problema:" + ex);
            }
        }
        return new SearchResult<>(0, problemas.size(), problemas.size(), problemas);
    }

    public static String classToCsvTitle(Class clazz) {
        StringBuilder sb = new StringBuilder();
        for (Field f : getAllAtributes(clazz)) {
            sb.append(f.getName());
            sb.append(CSV_SEPARATOR);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(CSV_LINE_DELIMITER);
        return sb.toString();
    }

    public static StringBuilder objectToCsvLine(Object gm) {
        StringBuilder sb = new StringBuilder();
        for (Field f : getAllAtributes(gm.getClass())) {
            try {
                f.setAccessible(true);
                if (f.get(gm) != null) {
                    Field idField = getIdField(f.getType());
                    if (idField != null) {
                        idField.setAccessible(true);
                        Object idValue = idField.get(f.get(gm));
                        sb.append(idValue.toString());
                    } else if (f.getType().equals(Date.class)) {
                        sb.append(SDF.format(f.get(gm)));
                    } else {
                        sb.append(f.get(gm).toString());
                    }
                }
            } catch (Exception ex) {
                log.error("erro ao criar linha csv", ex);
            }
            sb.append(CSV_SEPARATOR);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(CSV_LINE_DELIMITER);
        return sb;
    }

    public static Field getIdField(Class clazz) {
        for (Field f : getAllAtributes(clazz)) {
            if (f.isAnnotationPresent(Id.class)) {
                return f;
            }
        }
        return null;
    }

    public static List<Field> getAllAtributes(Class clazz) {
        List<Field> fields = new ArrayList<>();
        Class superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            fields.addAll(getAllAtributes(clazz.getSuperclass()));
        }
        for (Field f : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers())) {
                fields.add(f);
            }
        }
        return fields;
    }

}
