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

/**
 * Template engine service implementation using the Freemarker engine.
 * http://freemarker.org
 *
 * @author gyowannyqueiroz
 * @since jul/2015
 */
@Service
public class GumgaFreemarkerTemplateEngineService extends GumgaAbstractTemplateEngineAdapter {

    private static Configuration cfg;

    private String templateFolder;
    private String defaultEncoding;


    public GumgaFreemarkerTemplateEngineService() {
        //É necessario deixar um construtor default
    }

    public GumgaFreemarkerTemplateEngineService(String templateFolder,
            String defaultEncoding) {
        this.templateFolder = templateFolder;
        this.defaultEncoding = defaultEncoding;
    }

    @Override
    public void setTemplateFolder(String folder) {
        templateFolder = folder;
    }

    @Override
    public void setDefaultEncoding(String encoding) {
        defaultEncoding = encoding;
    }

    @Override
    public void parse(Map<String, Object> values, String template, Writer out) throws TemplateEngineException {
        try {
            Template t = cfg.getTemplate(template);
            t.process(values, out);
        } catch (IOException | TemplateException ex) {
            throw new TemplateEngineException(String.format("An error occurred while parsing the template - %s", template), ex);
        }
    }

    @Override
    public String parse(Map<String, Object> values, String template) throws TemplateEngineException {
        StringWriter out = new StringWriter();
        parse(values, template, out);
        return out.toString();
    }

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
            }
            catch (java.nio.file.FileSystemNotFoundException ex){
                System.out.println("------->Templates não encontrados."+ex);
            }
            
        }
    }

    private static synchronized void initStatic() {
        cfg = new Configuration(Configuration.VERSION_2_3_22);
    }
}
