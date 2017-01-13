package io.gumga.domain.domains;

/**
 * Representa uma Imagem
 *
 * @author munif
 */
public class GumgaImage extends GumgaFile {

    public GumgaImage() {
    }

    public GumgaImage(String name, long size, String mimeType, byte[] bytes) {
        super(name, size, mimeType, bytes);
    }

    public GumgaImage(GumgaFile other) {
        super(other);
    }

}
