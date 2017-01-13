package io.gumga.domain.customfields;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.SimplePath;
import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

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
    public QGumgaCustomizableModel(PathMetadata<?> metadata) {
        super((Class) GumgaCustomizableModel.class, metadata);
    }

}
