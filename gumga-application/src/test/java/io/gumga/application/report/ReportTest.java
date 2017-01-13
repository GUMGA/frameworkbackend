/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application.report;

import io.gumga.application.SpringConfig;
import io.gumga.application.service.JasperReportService;
import io.gumga.application.service.ReportType;
import net.sf.jasperreports.engine.JasperPrint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author gyowanny
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class ReportTest {

    @Autowired
    private JasperReportService reportService;

    @Before
    public void before() {
        assertNotNull("Report service is null", reportService);
    }

    @After
    public void after() {

    }

    @Test
    public void testGenerateReportJasperPrint() throws Exception {
        InputStream is = getInputStreamReportFile();
        JasperPrint jp = reportService.generateReport(is, createContactList(), null);
        assertNotNull("Jasper Print object could not be generated", jp);
    }

    @Test
    public void testGenerateAndExportReportToPDF() throws Exception {
        InputStream is = getInputStreamReportFile();
        String outputFile = System.getProperty("java.io.tmpdir") + "/reportOutput.pdf";
        FileOutputStream fos = new FileOutputStream(new File(outputFile));
        try {
            reportService.exportReport(is, fos, createContactList(), null, ReportType.PDF);
        } finally {
            fos.flush();
            fos.close();
        }
        assertTrue(new File(outputFile).exists());
    }

    @Test
    public void testGenerateAndExportReportToHTML() throws Exception {
        InputStream is = getInputStreamReportFile();
        String outputFile = System.getProperty("java.io.tmpdir") + "/reportOutput.html";
        reportService.exportReportToHtmlFile(is, createContactList(), null, outputFile);
        assertTrue(new File(outputFile).exists());
    }

    private InputStream getInputStreamReportFile() throws Exception {
        InputStream is = ReportTest.this.getClass().getResourceAsStream("/reports/test.jasper");
        assertNotNull("Report input stream error", is);
        return is;
    }

    private List<Contact> createContactList() {
        List<Contact> list = new ArrayList<>();
        list.add(new Contact("Munif Gebara", "123123"));
        list.add(new Contact("Cesar Baleco", "341234"));
        list.add(new Contact("Rafael Mangolin", "4123123"));
        list.add(new Contact("Mateus Miranda", "523423"));
        list.add(new Contact("Augusto Carniel", "41231"));
        list.add(new Contact("Igor Santana", "7235423"));
        list.add(new Contact("Guilherme Siquinelli", "541231"));
        list.add(new Contact("Gyowanny Queiroz", "4512341"));
        return list;
    }

}
