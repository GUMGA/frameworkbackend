/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gyowanny
 */
@Service(value = "reportService")
public class JasperReportService {

    /**
     * Gera o relatorio com os dados e parametros informados. Chamando este
     * metodo o programador pode exportar da forma como quiser a partir do
     * objeto <code>JasperPrint</code>. 
     * 
     * @param reportStream A input stream contendo o arquivo .jasper
     * @param data Os dados a serem populados no relatorio
     * @param params Os parametros do relatorio. Opcional
     * @return O relatorio populado mas nao renderizado
     * @throws JRException Quando houver erro no jasper
     * @throws IOException Quando houver erro no arquivo informado
     */
    public JasperPrint generateReport(InputStream reportStream,
            List data, Map<String, Object> params) throws JRException, IOException {
        if (data == null){
            data = Collections.emptyList();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                params, new JRBeanCollectionDataSource(data));
        return jasperPrint;
    }
    
    /**
     * Gera o relatorio com os dados e parametros informados e exporta para HTML. 
     *
     * @param reportStream A input stream contendo o arquivo .jasper
     * @param data Os dados a serem populados no relatorio
     * @param params Os parametros do relatorio. Opcional
     * @param destFile O arquivo de destino
     * @throws JRException
     * @throws IOException 
     */
    public void exportReportToHtmlFile(InputStream reportStream,
            List data, Map<String, Object> params, String destFile) throws JRException, IOException {
        JasperPrint jp = generateReport(reportStream, data, params);
        JasperExportManager.exportReportToHtmlFile(jp, destFile);
    }

    /**
     * Gera e exporta o relatorio de acordo com o tipo informado. OBS: Nao
     * exporta para HTML.
     *
     * @param reportStream A input stream contendo o arquivo .jasper
     * @param data Os dados a serem populados no relatorio
     * @param params Os parametros do relatorio. Opcional
     * @param outStream A output stream onde será salvo o relatório
     * @param type O tipo do relatorio
     * @throws JRException Quando houver erro no jasper
     * @throws IOException Quando houver erro no arquivo informado
     */
    public void exportReport(InputStream reportStream, OutputStream outStream,
            List data, Map<String, Object> params, ReportType type) throws JRException, IOException {
        if (!ReportType.HTML.equals(type)) {
            JasperPrint jasperPrint = generateReport(reportStream, data, params);
            switch (type) {
                case PDF:
                    JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
                    break;
                default:
                    JasperExportManager.exportReportToXmlStream(jasperPrint, outStream);
            }
        }
    }

}
