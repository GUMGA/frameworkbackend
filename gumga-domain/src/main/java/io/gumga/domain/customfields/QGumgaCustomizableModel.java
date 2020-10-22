package io.gumga.domain.customfields;


import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.processing.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;
/**
 * QGumgaCustomizableModel is a Querydsl query type for GumgaCustomizableModel
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QGumgaCustomizableModel extends EntityPathBase<GumgaCustomizableModel<? extends java.io.Serializable>> {

    private static final long serialVersionUID = -874622509L;

    public static final QGumgaCustomizableModel gumgaCustomizableModel = new QGumgaCustomizableModel("gumgaCustomizableModel");

    public final QGumgaModel _super = new QGumgaModel(this);

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    @SuppressWarnings("all")
    public QGumgaCustomizableModel(String variable) {
        super((Class) GumgaCustomizableModel.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QGumgaCustomizableModel(Path<? extends GumgaCustomizableModel> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QGumgaCustomizableModel(PathMetadata metadata) {
        super((Class) GumgaCustomizableModel.class, metadata);
    }

}
