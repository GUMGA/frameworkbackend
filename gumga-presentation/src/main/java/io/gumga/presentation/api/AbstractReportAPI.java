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
 * Classe com métodos de acesso e manipulação de Relatórios
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

    /**
     * Gera e exporta um objeto de relatório
     * @param reportName String com o nome do relatório
     * @param data Lista de dados
     * @param params Coleção de parâmetros de relatório
     * @param request Objeto HttpServletRequest contendo a requisição {@link HttpServletRequest}
     * @param response Objeto HttpServletResponse contendo a resposta {@link HttpServletResponse}
     * @param type Tipo do relatório {@link ReportType}
     * @throws JRException
     * @throws IOException
     */
    public void generateAndExportReport(String reportName, List data, Map<String, Object> params,
            HttpServletRequest request, HttpServletResponse response, ReportType type) throws JRException, IOException {
        InputStream is = getResourceAsInputStream(request, reportName);
        setContentType(response, reportName, type);
        reportService.exportReport(is, response.getOutputStream(), data, params, type);
    }
    /**
     * Gera e exporta um objeto de relatório em HTML
     * @param reportName String com o nome do relatório
     * @param destFile String com o nome do arquivo de destino
     * @param data Dados a serem exportados
     * @param params Coleção de parâmetros
     * @param request Objeto HttpServletRequest contendo a requisição {@link HttpServletRequest}
     * @param response Objeto HttpServletResponse contendo a resposta {@link HttpServletResponse}
     * @throws JRException
     * @throws IOException
     */
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

    /**
     * Obtém relatório pelo nome e o retorna como uma fonte de dados contendo o arquivo selecionado
     * @param request Objeto HttpServletRequest contendo a requisição {@link HttpServletRequest}
     * @param reportName String com o nome do relatório buscado
     * @return Fonte de dados (InputStream) contendo o relatório requisitado
     */
    protected InputStream getResourceAsInputStream(HttpServletRequest request, String reportName) {
        return request.getServletContext().getResourceAsStream(getFullPath(reportsFolder, reportName));
    }

    /**
     * Carrega uma requisição com o Tipo de relatório e nome
     * {@link HttpServletResponse}
     * {@link ReportType}
     * @param response Objeto HttpServletResponse contendo a resposta {@link HttpServletRequest}
     * @param reportName String com o nome do relatório
     * @param type Tipo do relatório {@link ReportType}
     */
    protected void setContentType(HttpServletResponse response, String reportName, ReportType type) {
        response.setContentType(type.getContentType());
        response.setHeader("Content-disposition", "inline; filename=" + reportName);
    }

    /**
     * Carrega o diretório de um arquivo
     * @param folder String com o nome da pasta desejada
     * @param file String com o nome do arquivo desejado
     * @return String com o diretório completo do arquivo
     */
    protected String getFullPath(String folder, String file) {
        String sep = folder.endsWith(File.separator) ? "" : File.separator;
        return folder.concat(sep).concat(file);
    }

}
