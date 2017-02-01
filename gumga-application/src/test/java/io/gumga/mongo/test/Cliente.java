package io.gumga.mongo.test;

import io.gumga.application.mongo.GumgaMongoModel;

public class Cliente extends GumgaMongoModel {

    private String nome;
    private String telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

}
