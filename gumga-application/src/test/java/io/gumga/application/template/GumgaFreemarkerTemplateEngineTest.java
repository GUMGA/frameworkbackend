/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application.template;

import io.gumga.application.AbstractTest;
import io.gumga.application.service.GumgaFreemarkerTemplateEngineService;
import io.gumga.core.GumgaValues;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 * @author gyowannyqueiroz
 */
public class GumgaFreemarkerTemplateEngineTest extends AbstractTest {

    private GumgaFreemarkerTemplateEngineService gumgaFreemarkerTemplateEngineService;

    @Autowired
    private GumgaValues gumgaValues;

    private static final String OUTPUT_FOLDER = System.getProperty("user.dir") + "/templateEngineTestFolder";

    @Before
    public void startup() throws Exception {
        URL resourceUrl = getClass().getResource("/templates");
        Path resourcePath = Paths.get(resourceUrl.toURI());
        gumgaFreemarkerTemplateEngineService = new GumgaFreemarkerTemplateEngineService(resourcePath.toString(), "UTF-8");
        gumgaFreemarkerTemplateEngineService.init();
        assertNotNull(gumgaFreemarkerTemplateEngineService);
        gumgaFreemarkerTemplateEngineService.init();
        createOutputFolder(OUTPUT_FOLDER);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testParse() throws Exception {
        Map<String, Object> values = new HashMap<>();
        String name = "Lara Croft";
        values.put("name", name);
        values.put("age", new Integer(35));
        String result = gumgaFreemarkerTemplateEngineService.parse(values, "testTemplate.ftl");
        assertFalse(result == null || result.isEmpty());
        assertTrue(result.indexOf(name) > -1);
    }

    @Test
    public void testParseToFile() throws Exception {
        Map<String, Object> values = new HashMap<>();
        String name = "Lara Croft";
        values.put("name", name);
        values.put("age", new Integer(35));
        File file = new File(OUTPUT_FOLDER + "/testResult.txt");
        FileOutputStream fos = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(fos);
        try {
            gumgaFreemarkerTemplateEngineService.parse(values, "testTemplate.ftl", writer);
        } finally {
            fos.close();
            writer.flush();
            writer.close();
        }
        assertTrue(file.exists());
    }

    private void createOutputFolder(String folder) {
        File file = new File(folder);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();
    }
}
