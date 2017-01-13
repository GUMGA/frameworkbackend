/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import io.gumga.core.GumgaValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class GumgaLoggerService {

    private static final Logger log = LoggerFactory.getLogger(GumgaLogService.class);

    private final SimpleDateFormat sdf;

    @Autowired
    private GumgaValues gumgaValues;

    public GumgaLoggerService() {
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * Adiciona uma mensagem no gumgalog.txt
     * @param msg mensagem a ser adicionada no arquivo
     * @param level index da execption do StrackTrace
     */
    public void logToFile(String msg, int level) {
        try {
            String line = createLogLine(msg, level);
            File folder = new File(gumgaValues.getLogDir());
            folder.mkdirs();
            File log = new File(folder, "gumgalog.txt");
            FileWriter fw = new FileWriter(log, true);
            fw.write(line);
            fw.close();
        } catch (IOException ex) {
            log.error("Problema ao loggar no arquivo", ex);
        }
    }

    /**
     *
     * @param msg mensagem do log
     * @param level index da execption do StrackTrace
     * @return
     */
    public String createLogLine(String msg, int level) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[level];
        String line = sdf.format(new Date()) + " " + stackTraceElement.getClassName() + " " + stackTraceElement.getMethodName() + " " + stackTraceElement.getLineNumber() + " " + msg + "\n";
        return line;

    }
}
