package io.gumga.application;

public class GumgaHqlElement {

    public final String before;
    public final String after;

    public GumgaHqlElement(String before, String after) {
        this.before = before;
        this.after = after;
    }

    @Override
    public String toString() {
        return "HqlElement{" + "before=" + before + ", after=" + after + '}';
    }

}
