/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.core.service;

import java.io.File;
import java.io.Writer;
import java.nio.file.FileSystemException;
import java.util.Map;

/**
 * This is the basic generic inferring for the template engine types. If some of
 * the types differ from the ones specified here it's recommended to implement
 * the GumgaTemplateEngineService interface directly.
 *
 * @author gyowannyqueiroz
 * @since jul/2015
 */
public abstract class GumgaAbstractTemplateEngineAdapter implements
        GumgaTemplateEngineService<Map<String, Object>, Writer, String> {

    /**
     * Checks if the given folder exists. If not it tries to created it.
     *
     * @param _folder The folder to be checked
     * @throws FileSystemException Throws this exception if something goes wrong
     * with the template folder
     */
    public void checkFolder(String _folder) throws FileSystemException {
        if (_folder == null || _folder.isEmpty()) {
            throw new FileSystemException("Folder can't be empty");
        }
        File folder = new File(_folder);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new FileSystemException("Could not create " + _folder);
            }
        }
    }
}
