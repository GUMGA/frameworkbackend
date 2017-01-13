/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.core;

import java.io.Serializable;

/**
 * Representa uma pesquisa em para armazenar em GumgaUserData
 *
 * @author munif
 */
public class QueryToSave implements Serializable {

    private String name;
    private String data;
    private String page;

    public QueryToSave() {
    }

    public QueryToSave(String name, String data, String page) {
        this.name = name;
        this.data = data;
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "QueryToSave{" + "name=" + name + ", data=" + data + ", page=" + page + '}';
    }

}
