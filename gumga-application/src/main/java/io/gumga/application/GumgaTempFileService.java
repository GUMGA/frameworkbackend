/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import io.gumga.core.GumgaValues;
import io.gumga.domain.domains.GumgaFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Random;

@Service
public class GumgaTempFileService {
    
    private static final Logger log=LoggerFactory.getLogger(GumgaTempFileService.class);

    @Autowired
    private GumgaValues gumgaValues;

    /**
     * Criar um arquivo temporario
     * @param gumgaFile
     * @return
     */
    public String create(GumgaFile gumgaFile) {
        String tempFileName = "uploadData" + (System.currentTimeMillis() * 1000 + new Random().nextInt(1000));

        try { //TODO Arrumar o tratamento da Exception
            File folder = new File(gumgaValues.getUploadTempDir());
            folder.mkdirs();
            FileOutputStream fos = new FileOutputStream(gumgaValues.getUploadTempDir() + "/" + tempFileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gumgaFile);
            oos.close();
            fos.close();
            return tempFileName;
        } catch (Exception ex) {
            log.error("erro ao criar arquivo temporario "+tempFileName, ex);
        }
        return "error";
    }

    /**
     * Remove o arquivo temporario
     * @param fileName nome do arquivo a ser removido
     * @return
     */
    public String delete(String fileName) {
        try { //TODO Arrumar o tratamento da Exception
            File file = new File(gumgaValues.getUploadTempDir() + "/" + fileName);
            file.delete();
            return "OK";
        } catch (Exception ex) {
            log.error("erro ao excluir arquivo temporario "+fileName, ex);
        }
        return "error";
    }

    /**
     * Encontrar o arquivo temporario
     * @param tempFileName nome do arquivo a ser procurado
     * @return
     */
    public GumgaFile find(String tempFileName) {
        if (tempFileName == null||tempFileName.isEmpty()){
            return null;
        }
        try { //TODO Melhorar o tratamento da Exception para FileNotFound
            FileInputStream fis = new FileInputStream(gumgaValues.getUploadTempDir() + "/" + tempFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            GumgaFile gf = (GumgaFile) ois.readObject();
            ois.close();
            fis.close();
            return gf;
        } catch (Exception ex) {
            log.error("erro ao recuperar arquivo temporario "+tempFileName, ex);
        }
        return null;
    }

}
