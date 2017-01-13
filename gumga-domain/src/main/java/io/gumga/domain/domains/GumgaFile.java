/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.domains;

import java.util.Arrays;
import java.util.Objects;

/**
 * Representa um arquivo
 *
 * @author munif
 */
public class GumgaFile extends GumgaDomain {

    private String name;
    private long size;
    private String mimeType;
    private byte[] bytes;

    public GumgaFile() {
    }

    public GumgaFile(String name, long size, String mimeType, byte[] bytes) {
        this.name = name;
        this.size = size;
        this.mimeType = mimeType;
        this.bytes = bytes;
    }

    public GumgaFile(GumgaFile other) {
        if (other != null) {
            this.name = other.name;
            this.size = other.size;
            this.mimeType = other.mimeType;
            this.bytes = other.bytes;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + (int) (this.size ^ (this.size >>> 32));
        hash = 23 * hash + Objects.hashCode(this.mimeType);
        hash = 23 * hash + Arrays.hashCode(this.bytes);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GumgaFile other = (GumgaFile) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.mimeType, other.mimeType)) {
            return false;
        }
        if (!Arrays.equals(this.bytes, other.bytes)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GumgaFile{" + "name=" + name + ", size=" + size + ", mimeType=" + mimeType + '}';
    }

}
