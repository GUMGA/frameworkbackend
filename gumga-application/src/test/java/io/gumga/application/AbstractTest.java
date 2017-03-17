/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author gyowannyqueiroz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public abstract class AbstractTest {

    @Autowired
    private DataSource dataSource;

    public AbstractTest() {
    }

    public void setUpDatabase(List<String> dataSetFileNames) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ArrayList inputs = new ArrayList();
        dataSetFileNames.forEach((f) -> {
            inputs.add(Paths.get("src/test/resources/DBUnit/" + f, new String[0]));
        });
        Charset charset = StandardCharsets.UTF_8;

        try {
            baos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n <dataset>\n".getBytes(charset));
            Iterator e = inputs.iterator();

            while(e.hasNext()) {
                Path connection = (Path)e.next();
                String bais = new String(Files.readAllBytes(connection), charset);
                bais = bais.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
                bais = bais.replaceAll("<dataset>", "");
                bais = bais.replaceAll("</dataset>", "");
                baos.write(bais.getBytes(charset));
            }

            baos.write("</dataset>".getBytes(charset));
            Connection e1 = DataSourceUtils.getConnection(this.dataSource);
            DatabaseConnection connection1 = new DatabaseConnection(e1);
            ByteArrayInputStream bais1 = new ByteArrayInputStream(baos.toByteArray());
            FlatXmlDataSet additionalDataSet = (new FlatXmlDataSetBuilder()).setColumnSensing(true).build(bais1);
            DatabaseOperation.CLEAN_INSERT.execute(connection1, additionalDataSet);
        } catch (Exception var9) {
            System.err.println(var9);
        }

    }
}
