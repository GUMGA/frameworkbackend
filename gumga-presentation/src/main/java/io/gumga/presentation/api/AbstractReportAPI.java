/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api;

import io.gumga.application.service.JasperReportService;
import io.gumga.application.service.ReportType;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gyowanny
 */
public abstract class AbstractReportAPI {

    @Autowired
    private JasperReportService reportService;

    private String reportsFolder;

    /**
     * Construtor default
     *
     * @param _reportsFolder
     */
    public AbstractReportAPI(String _reportsFolder) {
        reportsFolder = _reportsFolder;
    }

    public void generateAndExportReport(String reportName, List data, Map<String, Object> params,
            HttpServletRequest request, HttpServletResponse response, ReportType type) throws JRException, IOException {
        InputStream is = getResourceAsInputStream(request, reportName);
        setContentType(response, reportName, type);
        reportService.exportReport(is, response.getOutputStream(), data, params, type);
    }

    public void generateAndExportHTMLReport(String reportName, String destFile, List data, Map<String, Object> params,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {
        InputStream is = getResourceAsInputStream(request, reportName);
        setContentType(response, reportName, ReportType.HTML);
        reportService.exportReportToHtmlFile(is, data, params, destFile);

        //Set the output content
        InputStream generatedIs = request.getServletContext().getResourceAsStream(destFile);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] bytes = new byte[16384];

        while ((nRead = generatedIs.read(bytes, 0, bytes.length)) != -1) {
            buffer.write(bytes, 0, nRead);
        }

        buffer.flush();
        bytes = buffer.toByteArray();
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes, 0, bytes.length);
    }

    protected InputStream getResourceAsInputStream(HttpServletRequest request, String reportName) {
        return request.getServletContext().getResourceAsStream(getFullPath(reportsFolder, reportName));
    }

    protected void setContentType(HttpServletResponse response, String reportName, ReportType type) {
        response.setContentType(type.getContentType());
        response.setHeader("Content-disposition", "inline; filename=" + reportName);
    }

    protected String getFullPath(String folder, String file) {
        String sep = folder.endsWith(File.separator) ? "" : File.separator;
        return folder.concat(sep).concat(file);
    }

}
