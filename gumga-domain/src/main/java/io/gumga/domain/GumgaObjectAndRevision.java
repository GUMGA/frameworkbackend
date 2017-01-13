package io.gumga.domain;

/**
 * Classe utilizada para prover as versões anteriores de objetos marcados com
 * auditoria.
 *
 * @author munif
 */
public class GumgaObjectAndRevision {

    private GumgaRevisionEntity gumgaRevisionEntity;

    private Object object;

    public GumgaObjectAndRevision(GumgaRevisionEntity gumgaRevisionEntity, Object object) {
        this.gumgaRevisionEntity = gumgaRevisionEntity;
        this.object = object;
    }

    public GumgaRevisionEntity getGumgaRevisionEntity() {
        return gumgaRevisionEntity;
    }

    public void setGumgaRevisionEntity(GumgaRevisionEntity gumgaRevisionEntity) {
        this.gumgaRevisionEntity = gumgaRevisionEntity;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
