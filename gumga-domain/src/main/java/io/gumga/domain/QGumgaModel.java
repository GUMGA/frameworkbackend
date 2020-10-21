package io.gumga.domain;

import io.gumga.domain.domains.GumgaOi;

import javax.annotation.processing.Generated;

import java.io.Serializable;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

/**
 * QGumgaModel is a Querydsl query type for GumgaModel
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QGumgaModel extends EntityPathBase<GumgaModel<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 1949626837L;

    public static final QGumgaModel gumgaModel = new QGumgaModel("gumgaModel");

    public final SimplePath<Serializable> id = createSimple("id", Serializable.class);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public final ComparablePath<GumgaOi> oi = createComparable("oi", java.io.Serializable.class);

    @SuppressWarnings("all")
    public QGumgaModel(String variable) {
        super((Class) GumgaModel.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QGumgaModel(Path<? extends GumgaModel> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QGumgaModel(PathMetadata metadata) {
        super((Class) GumgaModel.class, metadata);
    }

}
