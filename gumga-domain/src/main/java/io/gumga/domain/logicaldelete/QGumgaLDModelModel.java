package io.gumga.domain.logicaldelete;


import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;

/**
 * QGumgaCustomizableModel is a Querydsl query type for GumgaCustomizableModel
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QGumgaLDModelModel extends EntityPathBase<GumgaLDModel<? extends java.io.Serializable>> {

    private static final long serialVersionUID = -874622509L;

    public static final QGumgaLDModelModel gumgaLDModel = new QGumgaLDModelModel("gumgaLDModel");

    public final QGumgaModel _super = new QGumgaModel(this);

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public final BooleanPath gumgaActive = createBoolean("gumgaActive");

    @SuppressWarnings("all")
    public QGumgaLDModelModel(String variable) {
        super((Class) GumgaLDModel.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QGumgaLDModelModel(Path<? extends GumgaLDModel> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QGumgaLDModelModel(PathMetadata metadata) {
        super((Class) GumgaLDModel.class, metadata);
    }

}
