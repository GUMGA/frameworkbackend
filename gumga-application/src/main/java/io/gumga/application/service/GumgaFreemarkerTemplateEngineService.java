/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application.service;

import com.google.common.io.Files;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.gumga.core.GumgaValues;
import io.gumga.core.exception.TemplateEngineException;
import io.gumga.core.service.GumgaAbstractTemplateEngineAdapter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Template engine service implementation using the Freemarker engine.
 * http://freemarker.org
 *
 * @author gyowannyqueiroz
 * @since jul/2015
 */
@Service
public class GumgaFreemarkerTemplateEngineService extends GumgaAbstractTemplateEngineAdapter {

    private static final Logger log = LoggerFactory.getLogger(GumgaFreemarkerTemplateEngineService.class);

    private static Configuration cfg;
    private String templateFolder;
    private String defaultEncoding;

    public GumgaFreemarkerTemplateEngineService() {
        //É necessario deixar um construtor default
    }

    /**
     * Construtor
     * @param templateFolder Diretório dos templates
     * @param defaultEncoding Codificação padrão dos templates
     */
    public GumgaFreemarkerTemplateEngineService(String templateFolder,
            String defaultEncoding) {
        this.templateFolder = templateFolder;
        this.defaultEncoding = defaultEncoding;
    }

    /**
     * Configura diretório dos templates
     * @param folder The folder where the templates are located
     */
    @Override
    public void setTemplateFolder(String folder) {
        templateFolder = folder;
    }

    /**
     * Configura codificação padrão
     * @param encoding The encoding of the templates like UTF-8 and etc.
     */
    @Override
    public void setDefaultEncoding(String encoding) {
        defaultEncoding = encoding;
    }

    /**
     * Substitui variáveis de template pelos valores granvando em um arquivo
     * @param values The values to be merged with the template
     * @param template The template to be parsed
     * @param out The object responsible to save the output file. It may be an
     * OutputStream for example.
     * @throws TemplateEngineException
     */
    @Override
    public void parse(Map<String, Object> values, String template, Writer out) throws TemplateEngineException {
        try {
            if (cfg == null) {
                initStatic();
            }
            Template t = cfg.getTemplate(template);
            t.process(values, out);
        } catch (IOException | TemplateException ex) {
            throw new TemplateEngineException(String.format("An error occurred while parsing the template - %s", template), ex);
        }
    }

    /**
     * Substitui variáveis de template pelos valores
     * @param values The values to be merged with the template
     * @param template The template to be parsed
     * @return
     * @throws TemplateEngineException
     */
    @Override
    public String parse(Map<String, Object> values, String template) throws TemplateEngineException {
        StringWriter out = new StringWriter();
        parse(values, template, out);
        return out.toString();
    }

    /**
     * Inicializa as configurações iniciais do Freemaker
     * @throws TemplateEngineException
     */
    @Override
    public void init() throws TemplateEngineException {
        if (cfg == null) {
            try {
                //Creates the template folder if it doesn't exists...
                checkFolder(this.templateFolder);
                //...then copies default templates in there
                URL resourceUrl = getClass().getResource("/templates");
                Path resourcePath = Paths.get(resourceUrl.toURI());
                for (File file : resourcePath.toFile().listFiles()) {
                    File destination = new File(this.templateFolder + File.separator + file.getName());
                    if (!destination.exists()) {
                        Files.copy(file, destination);
                    }
                }
                initStatic();
                cfg.setDirectoryForTemplateLoading(new File(this.templateFolder));
                cfg.setDefaultEncoding(this.defaultEncoding);
                cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            } catch (IOException ex) {
                throw new TemplateEngineException("An error occurred while initializating the template engine", ex);
            } catch (URISyntaxException e) {
                throw new TemplateEngineException("An error occurred while initializating the template engine", e);
            } catch (java.nio.file.FileSystemNotFoundException ex) {
                log.warn("------->Templates não encontrados." + ex);
            }

        }
    }

    /**
     * Inicializa as configurações iniciais do Freemaker estaticamente
     */
    private static synchronized void initStatic() {
        cfg = new Configuration(Configuration.VERSION_2_3_22);
    }
}
