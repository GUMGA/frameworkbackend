package io.gumga.domain;

@Deprecated
public interface Inferable<T> {

    public Class<T> clazz();

    public void setClazz(Class<T> clazz);

}
