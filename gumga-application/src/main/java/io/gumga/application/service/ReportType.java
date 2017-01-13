/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application.service;

import com.google.common.net.MediaType;

/**
 *
 * @author gyowanny
 */
public enum ReportType {

    PDF(MediaType.PDF.toString()), 
    HTML(MediaType.HTML_UTF_8.toString()), 
    XLS(MediaType.MICROSOFT_EXCEL.toString()), 
    DOCX(MediaType.MICROSOFT_WORD.toString()), 
    ODT(MediaType.OPENDOCUMENT_TEXT.toString()), 
    ODS(MediaType.OPENDOCUMENT_SPREADSHEET.toString()), 
    PPTX(MediaType.MICROSOFT_POWERPOINT.toString()), 
    RTF(MediaType.RTF_UTF_8.toString()), 
    TEXT(MediaType.TEXT_JAVASCRIPT_UTF_8.toString()), 
    XML_4_SWF(MediaType.SHOCKWAVE_FLASH.toString()), 
    JSON(MediaType.JSON_UTF_8.toString());

    private String contentType;
    

    private ReportType(String _contentType) {
        contentType = _contentType;
    }

    public String getContentType() {
        return contentType;
    }

}
