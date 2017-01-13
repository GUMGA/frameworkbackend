package io.gumga.domain;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SimplePath;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

/**
 * QGumgaModel is a Querydsl query type for GumgaModel
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QGumgaModel extends EntityPathBase<GumgaModel<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 1949626837L;

    public static final QGumgaModel gumgaModel = new QGumgaModel("gumgaModel");

    public final SimplePath<java.io.Serializable> id = createSimple("id", java.io.Serializable.class);

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
    public QGumgaModel(PathMetadata<?> metadata) {
        super((Class) GumgaModel.class, metadata);
    }

}
