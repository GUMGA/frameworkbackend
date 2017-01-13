/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api;

/**
 *
 * @author munif
 */
public class GumgaAtributeMetadata {

    private String name;
    private String javaType;
    private String type;
    private Boolean required;

    public GumgaAtributeMetadata(String name, String javaType) {
        this.name = name;
        this.javaType = javaType;
        this.type = javaScriptType(javaType);
        this.required = false;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getType() {
        return type;
    }

    private String javaScriptType(String javaType) {
        switch (javaType) {
            case "String":
                return "TEXT";
            case "BigDecimal":
            case "Integer":
            case "int":
            case "Float":
            case "float":
            case "Double":
            case "double":
            case "Long":
            case "long":
                return "NUMBER";
            case "boolean":
            case "Boolean":
                return "BOOLEAN";
            default:
                return javaType;
        }
    }

}
